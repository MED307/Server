package application;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

import application.dataTypes.ChatMessage;
import application.dataTypes.Chatroom;
import application.dataTypes.User;

public class ClientHandler extends Thread {
	private Socket client;				// a socket with the name client
	public volatile static UserDataBase udb = new UserDataBase();	//an instance of the class UserDataBase, used to call methods in the class
	public volatile static ChatroomManager crm = new ChatroomManager(); //an instance of the class ChatroomManager, used to call methods in the class
	private ArrayList<ClientHandler> clients;
	private User clientUser;	//used to get information from the connected client
	private ObjectInputStream oisin = null;
	private ObjectOutputStream oosout = null;
	private boolean connected = true;
	private ReentrantLock lock = new ReentrantLock();
	
	//the constructor
	public ClientHandler(Socket clientSocket, ArrayList<ClientHandler> _clients) throws IOException { 
		this.client = clientSocket;
		this.oisin = new ObjectInputStream(client.getInputStream());   //used to read the objects sent by the client
		this.oosout = new ObjectOutputStream(client.getOutputStream());  //used to write objects and send them to the client
		this.clients = _clients;
	}
	
	@Override
	public void run() {
		try {
			while(connected) {
				//The following segment is meant for user creation and login
				this.oosout.reset();
				Object something = this.oisin.readObject(); //creates an object that can read the input from the client
				//check user login
				if(something instanceof ArrayList<?>) {  //if the received object is an ArryList do the following 
					System.out.println("received ArrayList");
					String username = (String) ((ArrayList<?>)something).get(0); 
					String password = (String) ((ArrayList<?>)something).get(1);
					int userIndex = 0;
					for(int i= 0 ; i < udb.getUsers().size() ; i++ ) {		//goes through every line till it reaches the last line
						if(udb.getUsers().get(i).getUsername().compareTo(username) == 0) { //Finds the written username in the list if possible 
							userIndex = i;
						}
						
						if(udb.getUsers().get(userIndex).getPassword().compareTo(password) == 0 ) {	//checks the passwords connected to the username
							oosout.writeObject(udb.getUsers().get(userIndex));
							setClientUser(udb.getUsers().get(userIndex));
							System.out.println("Returned User");
							break;
						}
					}
				}//login done
				
				//creates a new user
				if(something instanceof String) {		//if received object is a string do the following 
					System.out.println("received String");
					String chatID = ((String)something); //the received String is put into the variable 
					for(Chatroom i: crm.getChatrooms()) { //goes through every chatroom till it reaches the last chatroom
						if(i.getChatId().compareTo(chatID) == 0) {  //checks if the chatroom matches the requested one
							oosout.writeObject(i);					// Sends it to the client
							System.out.println("returned Chatroom");
							System.out.println(i.getMessages());
							break;
						}
					}

				}
				//adds the new user to the list
				if(something instanceof User) {		
					udb.addUser((User)something);
					System.out.println("user added");
				} 
				//chatrooms
				//Object chat = oisin.readObject();	//creates an object that can read the input from the client
				if(something instanceof ChatMessage){
					System.out.println("received Message");
					ChatMessage message = (ChatMessage)something;
					lock.lock();
					try {
						for(Chatroom i: crm.getChatrooms()) { //going through the chatroom IDs
							if(i.getChatId().compareTo(message.getRoomID()) == 0) {// if the messages ID matches the Room ID
								i.addMessage(message);  //Adds a message to the chatroom
								System.out.println("added Message to Chatroom");
								System.out.println(i.getMessages());
							}
						}
					}
					finally
					{
						lock.unlock();
						for(ClientHandler i: clients) 
						{
							i.oosout.writeObject(message);
							System.out.println("sendMessage");
						}
						oosout.writeObject(message);
						System.out.println("returned Message");
					}

					//Sends the message out to all active clients in the chatroom
				}
				if(something instanceof Chatroom){
					System.out.println("received Chatroom");
					crm.addChatroom((Chatroom)something);
					for	(String j:((Chatroom)something).getUsers())
					{
						for (User i: udb.getUsers())
						{
							if(i.getUsername().compareTo(j) == 0)
							{
								i.addChatRoom(((Chatroom)something).getChatId());
								for (User u: udb.getUsers())
								{
									if (u.getId().compareTo(getClientUser().getId()) == 0) 
									{
										u.addChatRoom(((Chatroom)something).getChatId());
									}
								}
								System.out.println("added Chatroom");
								break;
							}
						}
					}
				}
			}
		} catch (ClassNotFoundException | IOException  e) { //the OIS and OOS can throw InputOutputExceptions
			e.printStackTrace();
			connected = false;
		}
		try {
			oosout.close();
			oisin.close();
			client.close();
			Server.clients.remove(this);
			System.out.print(Server.clients.size());
				
		} catch (IOException e) {	//the OIS and OOS can throw InputOutputExceptions
			e.printStackTrace();
		}	
	}
	public User getClientUser() {
		return this.clientUser;
	}
	public void setClientUser(User clientUser) {
		this.clientUser = clientUser;
	}
	
	
}
