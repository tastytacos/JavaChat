package server;


import manager.ConfigurationDataManager;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private static String adminUsername = ConfigurationDataManager.getValueByXMLTag("admin-username");

    public synchronized static List<UserSession> getSessions() {
        return sessions;
    }

    private static String filename = ConfigurationDataManager.getValueByXMLTag("text-file-storage-name");
    private static int portNumber = Integer.parseInt(ConfigurationDataManager.getValueByXMLTag("port-number"));
    private static int maximumUsersAmount = Integer.parseInt(ConfigurationDataManager.getValueByXMLTag("maximum-users-amount"));

    public static File getMessagesStorage() {
        return MessagesStorage;
    }

    private static File MessagesStorage = new File(System.getProperty("user.dir") + "/src/main/resources/" + filename);
    private static int messagesToGet = Integer.parseInt(ConfigurationDataManager.getValueByXMLTag("amount-messages-to-get"));

    public static int getAmountMessagesToGet() {
        return messagesToGet;
    }

    private static List<UserSession> sessions = new ArrayList<>();

    public static void main(String a[]) {
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            System.out.println("Waiting for a client...");
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Got the connection with address - " + socket.getInetAddress() +
                        " on port " + socket.getPort());
                if (sessions.size() >= maximumUsersAmount) {
                    try (DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream())) {
                        dataOutputStream.writeUTF("Server can not accept more, than 10 users");
                        socket.close();
                    }
                } else {
                    UserSession userSession = new UserSession(socket);
                    sessions.add(userSession);
                    userSession.start();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getAdminUsername() {
        return adminUsername;
    }
}
