/**
 *
 */
package com.yeahmobi.yscheduler.agentframework.agent.event;

import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.beanutils.ConstructorUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yeahmobi.yscheduler.agentframework.exception.EventHandlerInitializeFailException;

/**
 * @author Leo.Liang
 */
public class DefaultEventMapper extends BaseEventMapper {

    private static final Logger log                     = LoggerFactory.getLogger(DefaultEventMapper.class);
    private static final String EVENT_MAPPING_FILE_NAME = "/event-handler.properties";

    public void init() {
        try {
            Properties eventMapping = readEventMapping();

            for (String key : eventMapping.stringPropertyNames()) {
                if (StringUtils.isNotBlank(key)) {
                    String handlerClassName = eventMapping.getProperty(key);
                    if (StringUtils.isNotBlank(handlerClassName)) {
                        if (!this.handlers.containsKey(key)) {
                            Class<?> handlerClass = Class.forName(StringUtils.trim(handlerClassName));
                            if (EventHandler.class.isAssignableFrom(handlerClass)) {
                                this.handlers.put(key,
                                                  (EventHandler) ConstructorUtils.invokeConstructor(handlerClass, null));
                                log.info("Register one event handler. Key={}, class={}", key, handlerClassName);
                            } else {
                                log.error("Handler class {} with key {} doesn't implement EventHandler interface.",
                                          handlerClassName, key);
                                throw new EventHandlerInitializeFailException(
                                                                              String.format("Handler class %s with key %s doesn't implement EventHandler interface.",
                                                                                            handlerClassName, key));
                            }
                        } else {
                            log.error("Handler({}) already existed.", key);
                            throw new EventHandlerInitializeFailException(String.format("Handler(%s) already existed.",
                                                                                        key));
                        }
                    }
                }
            }
        } catch (Throwable e) {
            log.error("Init event mapping failed.", e);
            throw new EventHandlerInitializeFailException("Fail to init event mapping.", e);
        }
    }

    private Properties readEventMapping() {
        Properties eventHandlerMapping = new Properties();

        InputStream in = null;
        try {
            in = DefaultEventMapper.class.getResourceAsStream(EVENT_MAPPING_FILE_NAME);
            eventHandlerMapping.load(in);
        } catch (Throwable e) {
            throw new EventHandlerInitializeFailException("Fail to read event-handler.properties.");
        } finally {
            IOUtils.closeQuietly(in);
        }

        return eventHandlerMapping;
    }

}
