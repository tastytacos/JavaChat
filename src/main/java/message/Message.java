package message;

import org.joda.time.LocalTime;

import java.io.Serializable;

public class Message implements Serializable {
    public Message() {
    }

    public String getMessageAuthor() {
        return messageAuthor;
    }

    public void setMessageAuthor(String messageAuthor) {
        this.messageAuthor = messageAuthor;
    }

    public LocalTime getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(LocalTime messageTime) {
        this.messageTime = messageTime;
    }

    public Message(String messageAuthor, LocalTime messageTime) {
        this.messageAuthor = messageAuthor;
        this.messageTime = messageTime;
    }

    private String messageAuthor;
    private LocalTime messageTime;
}
