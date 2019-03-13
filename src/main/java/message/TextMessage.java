package message;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;

import java.io.Serializable;

public class TextMessage extends Message implements Serializable {
    private String messageText;

    public TextMessage(String messageText, String messageAuthor, DateTime messageTime) {
        super(messageAuthor, messageTime);
        this.messageText = messageText;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

}
