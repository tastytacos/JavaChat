package server.database;

import message.Message;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

abstract class Database {
    protected abstract int getMessagesAmount();
    protected abstract Connection getConnection() throws SQLException;
    protected abstract void insert(Connection connection, Message message) throws SQLException;
    protected abstract Message getMessageById(int messageId);
    protected abstract List<Message> getAllMessages();
}
