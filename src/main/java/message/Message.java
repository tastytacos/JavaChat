package message;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {
    private static transient DateTimeFormatter formatter = DateTimeFormat.forPattern("MM-dd-yyyy HH:mm:ss");
    public Message() {
    }

    public String getMessageAuthor() {
        return messageAuthor;
    }

    public void setMessageAuthor(String messageAuthor) {
        this.messageAuthor = messageAuthor;
    }

    public String getFormattedMessageTime(DateTime dateTime){
        return dateTime.toString(formatter);
    }

    public static DateTime dateTimeFromString(String dateTime){
        return formatter.parseDateTime(dateTime);
    }

    public DateTime getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(DateTime messageTime) {
        this.messageTime = messageTime;
    }

    public Message(String messageAuthor, DateTime messageTime) {
        this.messageAuthor = messageAuthor;
        this.messageTime = messageTime;
    }

    private String messageAuthor;
    private DateTime messageTime;
}
