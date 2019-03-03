package client;


import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    private ServerListener serverListener;
    private static int port = 1234;
    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
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
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
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
        client.sendMessage(userName);
        while (true) {
            String message = scanner.nextLine();
            client.sendMessage(message);
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

    private void sendMessage(String message) {
        try {
            outputStream.writeUTF(message);
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
                    String serverMessage = inputStream.readUTF();
                    System.out.println(serverMessage);
                } catch (Exception e) {
                    System.out.println("You are offline");
                    client.disconnect();
                    break;
                }
            }
        }
    }
}

























