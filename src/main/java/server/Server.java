package server;


import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private static String adminUsername = "Admin";
    public synchronized static List<UserSession> getSessions() {
        return sessions;
    }
    private static String filename = "Messages.txt";

    public static File getMessagesStorage() {
        return MessagesStorage;
    }

    private static File MessagesStorage = new File(System.getProperty("user.dir") + "/src/main/resources/" + filename);
    private static int messagesToGet = 10;

    public static int getAmountMessagesToGet() {
        return messagesToGet;
    }

    private static List<UserSession> sessions = new ArrayList<>();

    public static void main(String a[]) {
        try (ServerSocket serverSocket = new ServerSocket(1234)) {
            System.out.println("Waiting for a client...");
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Got the connection with address - " + socket.getInetAddress() +
                        " on port " + socket.getPort());
                UserSession userSession = new UserSession(socket);
                sessions.add(userSession);
                userSession.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getAdminUsername() {
        return adminUsername;
    }
}
