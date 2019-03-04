package server;

import message.*;
import org.joda.time.LocalTime;

import java.io.*;
import java.net.Socket;
import java.util.List;


public class UserSession extends Thread {
    private Socket socket;

    private MessageManager manager = FileManager.getInstance(Server.getMessagesStorage());
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    public String getUsername() {
        return username;
    }

    private String username;

    UserSession(Socket socket) {
        System.out.println(Server.getMessagesStorage().getAbsolutePath());
        this.socket = socket;
        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ObjectOutputStream getOutputStream() {
        return outputStream;
    }


    @Override
    public void run() {
        try {
            Message message = (Message) inputStream.readObject();
            username = message.getMessageAuthor();
            sendMessageToUser(MessageFactory.getTextMessage("Welcome to the chat, " + username));
            sendMessageToUser(MessageFactory.getTextMessage("Enjoy chatting"));
            sendEveryoneExceptUserMessage(MessageFactory.getTextMessage("User " + username + " joined the chat"));
//            showLastNMessages(Server.getAmountMessagesToGet());
            while (true) {
                TextMessage userMessage = (TextMessage) inputStream.readObject();
                log(userMessage);
                if (userMessage.getMessageText().equalsIgnoreCase("quit")) {
                    sendEveryoneMessage(MessageFactory.getTextMessage("User " + username + " left the chat"));
                    disconnect();
                    break;
                }
                sendEveryoneMessage(userMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showLastNMessages(int amountMessages) {
        List<TextMessage> previousMessages = manager.getNTextMessages(Server.getAmountMessagesToGet());
        if (previousMessages == null) {
            sendMessageToUser(MessageFactory.getTextMessage("You are first in this chat!"));
        } else {
            sendMessageToUser(MessageFactory.getTextMessage("----------------------------"));
            previousMessages.forEach(this::sendMessageToUser);
            sendMessageToUser(MessageFactory.getTextMessage("----------------------------"));
        }

    }


    private void sendEveryoneExceptUserMessage(Message Message) {
        List<UserSession> userSessions = Server.getSessions();
        userSessions.forEach(userSession -> {
            if (userSession != this) {
                try {
                    userSession.getOutputStream().writeObject(Message);
                } catch (IOException e) {
                    System.out.println("Message sending to " + userSession.getUsername() + " failed");
                    e.printStackTrace();
                }
            }
        });
    }

    private void sendMessageToUser(Message message) {
        try {
            outputStream.writeObject(message);
        } catch (IOException e) {
            System.out.println("Message delivery to " + username + " failed");
            e.printStackTrace();
        }
    }

    private LocalTime getTime() {
        return new LocalTime();
    }

    private void log(Message message) {
        String sout = "On " + message.getMessageTime() + " " + username + " wrote - " + message;
        System.out.println(sout);
        manager.writeMessage(message);
    }

    private void disconnect() {
        try {
            System.out.println(username + " logged out");
            Server.getSessions().remove(this);
            inputStream.close();
            outputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendEveryoneMessage(Message message) {
        List<UserSession> users = Server.getSessions();
        users.forEach(user -> {
            try {
                user.getOutputStream().writeObject(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
