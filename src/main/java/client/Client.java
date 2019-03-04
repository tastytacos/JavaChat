package client;


import message.Message;
import message.TextMessage;
import org.joda.time.LocalTime;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    private ServerListener serverListener;
    private static int port = 1234;
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private InetAddress server;

    public Client(InetAddress server) {
        this.server = server;
    }

    private boolean start() {
        try {
            socket = new Socket(server, port);
        } catch (IOException e) {
            System.out.println("Error connecting to the server");
            e.printStackTrace();
            return false;
        }
        try {
            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        serverListener = new ServerListener(this);
        serverListener.start();
        return true;
    }

    public static void main(String a[]) {
        Client client = null;
        Scanner scanner = new Scanner(System.in);
        try {
            client = new Client(InetAddress.getLocalHost());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        client.start();
        System.out.println("Enter your nickname: ");
        String userName = scanner.nextLine();
        client.sendMessage(new Message(userName, new LocalTime()));
        while (true) {
            String message = scanner.nextLine();
            client.sendMessage(new TextMessage(message, userName, new LocalTime()));
            if (message.equalsIgnoreCase("quit")) {
                break;
            }
        }
        client.disconnect();
    }

    private void disconnect() {
        try {
            outputStream.close();
            inputStream.close();
            socket.close();
            serverListener.setListeningOff();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void sendMessage(Message textMessage) {
        try {
            outputStream.writeObject(textMessage);
        } catch (IOException e) {
            System.out.println("Message sending failed");
            e.printStackTrace();
        }
    }

    class ServerListener extends Thread {
        Client client;
        private volatile boolean listening;

        void setListenerOn() {
            listening = true;
        }

        void setListeningOff() {
            listening = false;
        }

        public ServerListener(Client client) {
            this.client = client;
            listening = true;
        }

        @Override
        public void run() {
            while (listening) {
                try {
                    TextMessage serverTextMessage = (TextMessage) inputStream.readObject();
                    displayMessage(serverTextMessage);
                } catch (Exception e) {
                    System.out.println("You are offline");
                    client.disconnect();
                    break;
                }
            }
        }
    }

    private void displayMessage(TextMessage textMessage) {
        System.out.println(textMessage.getMessageTime() + " " + textMessage.getMessageAuthor() +
                " > " + textMessage.getMessageText());
    }
}

























