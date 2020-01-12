package com.ibrahim.mailreader.model;

/**
 *
 * @author Ibrahim Chowdhury
 */
public class MessageModel {

    private String subject;
    private String body;
    private String fromEmail;
    private String mailContentType;
    private String receiveDate;
    private long receiveTime;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public String getMailContentType() {
        return mailContentType;
    }

    public void setMailContentType(String mailContentType) {
        this.mailContentType = mailContentType;
    }

    public String getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(String receiveDate) {
        this.receiveDate = receiveDate;
    }

    public long getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(long receiveTime) {
        this.receiveTime = receiveTime;
    }
    
    

}
