package server;

import message.Message;
import message.TextMessage;

import java.util.List;

public interface MessageManager {
    void writeMessage(Message message);
    List<TextMessage> getNTextMessages(int messagesAmount);
}
