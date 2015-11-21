package data;

/**
 * Created by mordreth on 11/20/15.
 */
public class Message {
    String id;
    String remitentEmail;
    String recipientEmail;
    String subject;
    String content;

    public Message(String id, String remitentEmail, String subject, String recipientEmail, String content) {
        this.id = id;
        this.remitentEmail = remitentEmail;
        this.subject = subject;
        this.recipientEmail = recipientEmail;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public String getRemitentEmail() {
        return remitentEmail;
    }

    public void setRemitentEmail(String remitentEmail) {
        this.remitentEmail = remitentEmail;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
