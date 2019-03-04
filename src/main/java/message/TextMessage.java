package message;

import org.joda.time.LocalTime;

import java.io.Serializable;

public class TextMessage extends Message implements Serializable {
    private String messageText;
    private String messageAuthor;

    public TextMessage(String messageText, String messageAuthor, LocalTime messageTime) {
        this.messageAuthor = messageAuthor;
        this.messageTime = messageTime;
        this.messageText = messageText;
    }

    private LocalTime messageTime;


    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
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
}
