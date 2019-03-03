package server;

import lombok.Data;
import org.joda.time.LocalTime;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

@Data
public class UserSession extends Thread {
    private Socket socket;
    private DataOutputStream outputStream;
    private MessageManager manager = FileManager.getInstance(Server.getMessagesStorage());
    private DataInputStream inputStream;
    private String username;

    UserSession(Socket socket) {
        System.out.println(Server.getMessagesStorage().getAbsolutePath());
        this.socket = socket;
        try {
            outputStream = new DataOutputStream(socket.getOutputStream());
            inputStream = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        try {
            username = inputStream.readUTF();
            sendMessageToUser("Welcome to the chat, " + username);
            sendMessageToUser("Enjoy chatting");
            List<String> previousMessages = manager.getNMessages(Server.getMessagesToGet());
            if (previousMessages == null) {
                sendMessageToUser("You are first in this chat!");
            } else {
                sendMessageToUser("----------------------------");
                previousMessages.forEach(this::sendMessageToUser);
                sendMessageToUser("----------------------------");
            }
            while (true) {
                String userInput = inputStream.readUTF();
                log(userInput);
                if (userInput.equalsIgnoreCase("quit")) {
                    sendEveryoneMessage("User " + this.getId() + " left the chat");
                    disconnect();
                    break;
                }
                sendEveryoneMessage(userInput);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMessageToUser(String message) {
        try {
            outputStream.writeUTF(message);
        } catch (IOException e) {
            System.out.println("Message delivery failed");
            e.printStackTrace();
        }
    }

    private LocalTime getTime() {
        return new LocalTime();
    }

    private void log(String userInput) {
        String message = "On " + getTime() + " " + username + " wrote - " + userInput;
        System.out.println(message);
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

    private void sendEveryoneMessage(String text) {
        List<UserSession> users = Server.getSessions();
        users.forEach(user -> {
            String message = handleMessage(text);
            try {
                user.getOutputStream().writeUTF(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private String handleMessage(String input) {
        return username + " on " + getTime() + ": " + input;
    }


}
