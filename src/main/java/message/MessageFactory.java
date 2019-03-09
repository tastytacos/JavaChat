package message;


import org.joda.time.LocalTime;
import server.Server;

public class MessageFactory {
    public static String adminName = Server.getAdminUsername();

    public static TextMessage getTextMessage(String text){
        LocalTime time = new LocalTime();
        String name = adminName;
        TextMessage textMessage = new TextMessage(text, name, time);
        return textMessage;
    }

    public static void setAdminName(String adminName) {
        MessageFactory.adminName = adminName;
    }
}
