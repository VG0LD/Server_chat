import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private static final int PORT = 4045;
    private ArrayList<ClientList> clients = new ArrayList<>();

    public Server() {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT); // Port
            System.out.println("Server has started");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientList client = new ClientList(clientSocket, this); // New client
                clients.add(client);
                new Thread(client).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessageToAllClients(String message) {
        for (ClientList entry : clients) {
            entry.sendMessage(message);
        }
    }

    public void removeClient(ClientList clientList) {
        clients.remove(clientList);
    }

    public static void main(String[] args) {
        new Server();
    }
}
