package server;

import message.Message;

import java.util.List;

public interface MessageManager {
    void writeMessage(Message message);
    List<Message> getNTextMessages(int messagesAmount);
}
