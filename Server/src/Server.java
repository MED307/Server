import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
	
	public static ArrayList<ClientHandler> clients = new ArrayList<>();
	private static final int PORT = 8000;		//sets the location for the port
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		ServerSocket serverSocket = new ServerSocket(PORT); //Opens a server socket on the port(8000)
		
		// in this while loop a new client socket is created for every new client connecting
		while(true) {								
			System.out.println("Server sprinting");
			Socket client = serverSocket.accept(); //listener thread
			System.out.println("Client connected");
			ClientHandler clientThread = new ClientHandler(client); 
			clients.add(clientThread);
			System.out.println(clients.size());
		}
	}
}
