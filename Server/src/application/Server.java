package application;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import application.dataTypes.User;

public class Server {
	
	public static ArrayList<Thread> clients = new ArrayList<>();
	private static final int PORT = 55555;		//sets the location for the port
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		ServerSocket serverSocket = new ServerSocket(PORT); //Opens a server socket on the port(8000)
		ClientHandler.udb.addUser(new User("Admin","Admin"));
		
		// in this while loop a new client socket is created for every new client connecting
		while(true) {								
			System.out.println("Server sprinting");
			Socket client = serverSocket.accept(); //listener thread
			System.out.println("Client connected");
			Thread clientThread = new ClientHandler(client); 
			clientThread.start();
			clients.add(clientThread);

			System.out.println(clients.size());
		}
	}
}
