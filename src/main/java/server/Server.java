package server;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    public synchronized static List<UserSession> getSessions() {
        return sessions;
    }
//    private static String fileName = System.getProperty()
    private static int messagesToGet = 10;

    public static int getMessagesToGet() {
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
}
