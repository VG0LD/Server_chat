import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientList implements Runnable {
    private Server server;
    private PrintWriter outMessage;
    private Scanner inMessage;
    private Socket clientSocket;

    private static int client_count = 0;

    public ClientList(Socket socket, Server server) {
        try {
            client_count++;
            this.server = server;
            this.clientSocket = socket;
            this.outMessage = new PrintWriter(socket.getOutputStream(), true);
            this.inMessage = new Scanner(socket.getInputStream());

            // Notification of all clients about new one
            server.sendMessageToAllClients("New client connected! Total clients: " + client_count);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        server.removeClient(this);
        client_count--;
        server.sendMessageToAllClients("Client disconnected! Total clients: " + client_count);
    }

    public void sendMessage(String message) {
        outMessage.println(message);
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (inMessage.hasNextLine()) {
                    String clientMessage = inMessage.nextLine();
                    if (clientMessage.equals("End")) {
                        closeConnection(); // End connection
                        break;
                    }
                    server.sendMessageToAllClients(clientMessage);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
