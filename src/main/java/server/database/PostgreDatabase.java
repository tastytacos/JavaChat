package server.database;

import message.Message;
import message.TextMessage;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;

import java.sql.*;

class PostgreDatabase extends Database {
    private String url = "jdbc:postgresql://localhost:5432/db_messages";
    private String login = "postgres";
    private String password = "postgres";
    private String messagesTableName = "messages";
    @Override
    protected int getMessagesAmount() {
        int messagesAmount = 0;
        try(Connection connection = getConnection()){
             PreparedStatement statement = connection.prepareStatement("SELECT count(message_id) FROM "
                     + messagesTableName);
             ResultSet resultSet = statement.executeQuery();
             while (resultSet.next()){
                 messagesAmount = resultSet.getInt(1);
             }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messagesAmount;
    }

    @Override
    protected Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection(url, login, password);
    }

    @Override
    protected void insert(Connection connection, Message message) throws SQLException {
        TextMessage textMessage = (TextMessage) message;
        PreparedStatement statement = connection.prepareStatement("INSERT INTO " + messagesTableName + " (AUTHOR, MESSAGE_TEXT, " +
                "MESSAGE_TIME) VALUES (?, ?, ?)");
        statement.setString(1, textMessage.getMessageAuthor());
        statement.setString(2, textMessage.getMessageText());
        Timestamp timestamp = Timestamp.valueOf(textMessage.getFormattedMessageTime(textMessage.getMessageTime()));
        statement.setTimestamp(3, timestamp);
        statement.execute();
        statement.close();
    }

    @Override
    protected Message getMessageById(int messageId){
        TextMessage message = null;
        try(Connection connection = getConnection()){
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + messagesTableName + " WHERE " +
                    "message_id = " + Integer.toString(messageId));
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                String message_author = resultSet.getString("author");
                String message_text = resultSet.getString("message_text");
                Timestamp message_time = resultSet.getTimestamp("message_time");
                DateTime dateTime = new DateTime(message_time.getTime());
                message = new TextMessage(message_text, message_author, dateTime);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return message;
    }

}
