package application;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import application.dataTypes.ChatMessage;
import application.dataTypes.User;

public class Server {
	
	public static ArrayList<ClientHandler> clients = new ArrayList<>();
	private static final int PORT = 55555;		//sets the location for the port
	private static ExecutorService pool = Executors.newFixedThreadPool(5);
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		ServerSocket serverSocket = new ServerSocket(PORT); //Opens a server socket on the port(8000)
		ClientHandler.udb.addUser(new User("Admin","Admin"));
		
		// in this while loop a new client socket is created for every new client connecting
		while(true) {								
			System.out.println("Server sprinting");
			Socket client = serverSocket.accept(); //listener thread
			System.out.println("Client connected");
			ClientHandler clientThread = new ClientHandler(client, clients); 
			clients.add( clientThread);
			pool.execute(clientThread);

			System.out.println(clients.size());
		}
	}
}
