package com.yeahmobi.yscheduler.common.notice;

import java.util.List;

/**
 * @author Ryan Sun
 */
public class Message {

    private String       subject;
    private String       content;
    private List<String> to;

    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getTo() {
        return this.to;
    }

    public void setTo(List<String> to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return "Message [subject=" + this.subject + ", content=" + this.content + ", to=" + this.to + "]";
    }

    /**
     * @param subject
     * @param content
     * @param to
     */
    public Message(String subject, String content, List<String> to) {
        super();
        this.subject = subject;
        this.content = content;
        this.to = to;
    }

    public Message() {

    }
}
