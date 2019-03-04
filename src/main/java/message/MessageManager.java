package message;

import java.util.List;

public interface MessageManager {
    void writeMessage(Message message);
    List<TextMessage> getNTextMessages(int messagesAmount);
}
