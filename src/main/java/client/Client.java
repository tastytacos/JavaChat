package client;


import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

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

        new ServerListener(this).start();
        return true;
    }

    public static void main(String a[]) {
        Client client = null;
        try {
            client = new Client(InetAddress.getLocalHost());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        client.start();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String message = scanner.nextLine();
            if (message.equalsIgnoreCase("quit")) {
                break;
            }
            client.sendMessage(message);
        }
        client.disconnect();
    }

    private void disconnect() {
        try {
            outputStream.close();
            inputStream.close();
            socket.close();
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

        public ServerListener(Client client) {
            this.client = client;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    String serverMessage = inputStream.readUTF();
                    System.out.println(serverMessage);
                    System.out.print(" >");
                } catch (Exception e) {
                    System.out.println("Server failed!");
                    e.printStackTrace();
                    client.disconnect();
                }

            }
        }
    }
}

























