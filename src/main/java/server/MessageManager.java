package server;

import java.util.List;

interface MessageManager {
    void writeMessage(String message);
    List<String> getNMessages(int messagesAmount);
}
