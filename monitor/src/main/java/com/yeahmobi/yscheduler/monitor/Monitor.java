package com.yeahmobi.yscheduler.monitor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.yeahmobi.yscheduler.common.notice.EmailSender;
import com.yeahmobi.yscheduler.common.notice.Message;
import com.yeahmobi.yscheduler.common.notice.SmsSender;
import com.yeahmobi.yscheduler.model.Task;
import com.yeahmobi.yscheduler.model.User;
import com.yeahmobi.yscheduler.model.service.AgentService;
import com.yeahmobi.yscheduler.model.service.TaskInstanceService;
import com.yeahmobi.yscheduler.model.service.TaskService;
import com.yeahmobi.yscheduler.model.service.UserService;

public class Monitor {

    private static final Logger LOGGER         = LoggerFactory.getLogger(Monitor.class);

    private static final long   INTERVAL       = 60 * 1000;

    private static final String CONTENT_FORMAT = "[yscheduler-monitor][%s]在[%s]没有被调度";

    @Autowired
    private TaskService         taskService;

    @Autowired
    private AgentService        agentService;

    @Autowired
    private TaskInstanceService taskInstanceService;

    @Autowired
    private UserService         userService;

    @Autowired
    private SmsSender           smsSender;

    @Autowired
    private EmailSender         mailSender;

    @SuppressWarnings("resource")
    public static void main(String[] args) {
        ApplicationContext context = new FileSystemXmlApplicationContext("classpath:spring/applicationContext.xml");
        Monitor monitor = (Monitor) context.getBean("monitor");
        monitor.run();
    }

    private void run() {
        LOGGER.info("Start..");
        long currentTime = System.currentTimeMillis();
        long time = ((currentTime / INTERVAL) - 1) * INTERVAL;

        try {
            Date scheduleTime = new Date(time);
            List<Task> tasks = this.taskService.listHeartbeatTask();
            for (Task task : tasks) {
                if (this.agentService.get(task.getAgentId()).getEnable()) {
                    try {
                        if (!this.taskInstanceService.exist(task.getId(), scheduleTime)) {
                            Message message = new Message();
                            String dateStr = DateFormatUtils.format(scheduleTime, "yyyy-MM-dd HH:mm:ss");
                            String content = String.format(CONTENT_FORMAT, task.getName(), dateStr);
                            message.setSubject(content);
                            message.setContent(content);
                            User user = this.userService.get("admin");

                            List<String> to = new ArrayList<String>();
                            message.setTo(to);
                            String email = user.getEmail();
                            if (email != null) {
                                to.add(email);
                            }
                            this.mailSender.send(message);
                            to = new ArrayList<String>();
                            String telephone = user.getTelephone();
                            if (telephone != null) {
                                to.add(telephone);
                            }
                            message.setTo(to);
                            this.smsSender.send(message);
                        }
                    } catch (Exception e) {
                        LOGGER.error(e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

}
