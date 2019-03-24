package server.database;

import message.Message;
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
    public List<Message> getNTextMessages(int messagesToGet) {
        List<Message> textMessages;
        synchronized (this) {
            textMessages = database.getAllMessages();
        }
        if (textMessages.size() > messagesToGet) {
            return textMessages.subList(textMessages.size() - messagesToGet, textMessages.size());
        } else {
            return textMessages;
        }
    }
}
