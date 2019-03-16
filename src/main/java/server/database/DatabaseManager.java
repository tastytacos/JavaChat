package server.database;

import message.Message;
import message.TextMessage;
import server.MessageManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager implements MessageManager {
    private volatile Database database = new PostgreDatabase();

    @Override
    public void writeMessage(Message message) {
        synchronized (this) {
            try (Connection connection = database.getConnection()) {
                database.insert(connection, message);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<TextMessage> getNTextMessages(int messagesToGet) {
        List<TextMessage> textMessages = new ArrayList<>();
        synchronized (this) {
            // todo this is a very very bad solution. It works but it must be remastered in next versions
            int messagesAmount = database.getMessagesAmount();
            for (int i = 1; i <= messagesAmount; i++) {
                TextMessage textMessage = null;
                textMessage = (TextMessage) database.getMessageById(i);
                textMessages.add(textMessage);
            }
        }
        if (textMessages.size() > messagesToGet) {
            return textMessages.subList(textMessages.size() - messagesToGet, textMessages.size());
        } else {
            return textMessages;
        }
    }
}
