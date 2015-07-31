package com.yeahmobi.yscheduler.common;

import java.io.StringWriter;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

public class VelocityUtil {

    private static final VelocityEngine _ve;
    static {
        _ve = new VelocityEngine();
        _ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "class");
        _ve.setProperty("class.resource.loader.class", ClasspathResourceLoader.class.getName());
        _ve.setProperty("class.resource.loader.cache", true);
        _ve.setProperty("class.resource.loader.modificationCheckInterval", "-1");
        _ve.setProperty("input.encoding", "UTF-8");
        _ve.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.NullLogSystem");
        _ve.init();
    }

    public static String build(Map<String, Object> map, String file) {
        // 取得velocity的模版
        Template t = _ve.getTemplate(file);

        // 取得velocity的上下文context
        VelocityContext context = new VelocityContext();
        // 把数据填入上下文
        if (map != null) {
            for (Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                context.put(key, entry.getValue());
            }
        }
        // 输出流
        StringWriter writer = new StringWriter();
        // 转换输出
        t.merge(context, writer);

        return writer.toString();
    }
}
