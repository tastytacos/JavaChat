package server;

import lombok.Data;
import org.joda.time.LocalTime;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

@Data
public class UserSession extends Thread {
    private Socket socket;
    private DataOutputStream outputStream;

    private DataInputStream inputStream;

    UserSession(Socket socket) {
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
            outputStream.writeUTF("Welcome to the chat, User " + this.getId());
            outputStream.writeUTF("Enjoy chatting");
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

    private LocalTime getTime() {
        return new LocalTime();
    }

    private void log(String userInput) {
        System.out.println("On " + getTime() + " User " + getId() + " wrote - " + userInput);
    }

    private void disconnect() {
        try {
            System.out.println("User " + this.getId() + " logged out");
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
            String message = user != this ? handleMessage(text) : "You on " + getTime() + ": " + text;
            try {
                user.getOutputStream().writeUTF(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private String handleMessage(String input) {
        return "User " + this.getId() + " on " + getTime() + ": " + input;
    }


}
