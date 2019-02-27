import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String a[]) {
        try (ServerSocket serverSocket = new ServerSocket(1234);
        ){
            System.out.println("Waiting for a client...");
            Socket socket = serverSocket.accept();
            System.out.println("Got the connection with address - " + socket.getInetAddress() +
                    " on port " + socket.getLocalPort());
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            String inputLine;
            while ((inputLine = in.readUTF()) != null) {
                System.out.println("Client wrote " + inputLine);
                String respond = "You wrote " + inputLine;
                System.out.println("Sending him back - " + respond);
                out.writeUTF(respond);
                out.flush();
            }
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
