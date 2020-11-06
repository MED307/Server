package application;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import application.dataTypes.ChatMessage;
import application.dataTypes.Chatroom;
import application.dataTypes.User;

public class ClientHandler extends Thread {
	private Socket client;				// a socket with the name client
	public static UserDataBase udb = new UserDataBase();	//an instance of the class UserDataBase, used to call methods in the class
	public static ChatroomManager crm = new ChatroomManager(); //an instance of the class ChatroomManager, used to call methods in the class
	private User clientUser;	//used to get information from the connected client
	private ObjectInputStream oisin = null;
	private ObjectOutputStream oosout = null;
	
	//the constructor
	public ClientHandler(Socket clientSocket) throws IOException { 
		this.client = clientSocket;
		this.oisin = new ObjectInputStream(client.getInputStream());   //used to read the objects sent by the client
		this.oosout = new ObjectOutputStream(client.getOutputStream());  //used to write objects and send them to the client
	}
	
	@Override
	public void run() {
		try {
			while(true) {
				//The following segment is meant for user creation and login
				Object something = this.oisin.readObject(); //creates an object that can read the input from the client
				//check user login
				if(something instanceof ArrayList<?>) {  //if the received object is an ArryList do the following 
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
							System.out.println("SendClient");
							break;
						} else {
							System.out.println("wrong username/password");
						}
					}
				}//login done
				
				//creates a new user
				if(something instanceof String) {		//if received object is a string do the following 
					System.out.println("received String");
					String chatID = ((String)something); //the received String is put into the variable 
					for(Chatroom i: crm.getChatrooms()) { //goes through every line till it reaches the last line
						if(i.getChatId().compareTo(chatID) == 0) {  //checks if the username is already in use
							oosout.writeObject(i);
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
					for(Chatroom i: crm.getChatrooms()) { //going through the chatroom IDs
						if(i.getChatId() == ((ChatMessage)something).getRoomID()) {// if the messages ID matches the Room ID
							i.addMessage((ChatMessage)something);  //Adds a message to the chatroom						
						}
					}
					//Sends the message out to all active clients in the chatroom
			    	for(Thread i: Server.clients) {
			    		for(String j: ((ClientHandler)i).getClientUser().getChatRooms()) {
			    			if(j.compareTo(((ChatMessage)something).getRoomID())==0 && ((ClientHandler)i).getClientUser().getId().compareTo(((ChatMessage)something).getUser()) == 0) {
			    				((ClientHandler)i).oosout.writeObject((ChatMessage)something);
			    			}
			    		}
			    	}
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
								break;
							}
						}
					}
				}
			}
		} catch (ClassNotFoundException | IOException e) { //the OIS and OOS can throw InputOutputExceptions
			e.printStackTrace();
		}
		finally {
			try {
				oosout.close();
				oisin.close();
			} catch (IOException e) {	//the OIS and OOS can throw InputOutputExceptions
				e.printStackTrace();
			}
		}	
	}
	public User getClientUser() {
		return clientUser;
	}
	public void setClientUser(User clientUser) {
		this.clientUser = clientUser;
	}
	
	
}
