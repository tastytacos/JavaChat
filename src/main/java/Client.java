import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

    public static void main(String a[]) {
        int port = 1234;
        try (Socket clientSocket = new Socket(InetAddress.getLocalHost(), port);
             DataInputStream in = new DataInputStream(clientSocket.getInputStream());
             DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
             BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in))
        ){

            String userInput;
            System.out.println("Successfully connected to the server - " + clientSocket.getChannel());
            System.out.println("You can type something: ");
            while ((userInput = keyboard.readLine()) != null) {
                System.out.println("Sending data to the server...");
                out.writeUTF(userInput);
                System.out.println("Server send back:");
                System.out.println(in.readUTF());
                System.out.println();
                System.out.println();
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

























