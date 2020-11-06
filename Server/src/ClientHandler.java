
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {
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
					String ushername = (String) ((ArrayList<?>)something).get(0); 
					String password = (String) ((ArrayList<?>)something).get(1);
					int userIndex = 0;
					for(int i= 0 ; i < udb.getUsers().size() ; i++ ) {		//goes through every line till it reaches the last line
						if(udb.getUsers().get(i).getUsername().compareTo(ushername) == 0) { //Finds the written username in the list if possible 
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
					String usercheck = ((String)something); //the received String is put into the variable 
					boolean check = false;
					for(int i= 0 ; i < udb.getUsers().size() ; i++ ) { //goes through every line till it reaches the last line
						if(udb.getUsers().get(i).getUsername().compareTo(usercheck) == 0) {  //checks if the username is already in use
							check = true;
							oosout.writeBoolean(check);
							break;
						}else {
							check = false;
							oosout.writeBoolean(check);
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
					for(Chatroom i: crm.getChatrooms()) { //going through the chatroom IDs
						if(i.getChatId() == ((ChatMessage)something).getRoomID()) {// if the messages ID matches the Room ID
							i.addMessage((ChatMessage)something);  //Adds a message to the chatroom						
						}
					}
					//Sends the message out to all active clients in the chatroom
			    	for(ClientHandler i: Server.clients) {
			    		for(String j: i.getClientUser().getChatRooms()) {
			    			if(j.compareTo(((ChatMessage)something).getRoomID())==0) {
			    				i.oosout.writeObject((ChatMessage)something);
			    			}
			    		}
			    	}
				}
			}
		} catch (ClassNotFoundException | IOException e) { //the OIS and OOS can throw InputOutputExceptions
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				oosout.close();
				oisin.close();
			} catch (IOException e) {	//the OIS and OOS can throw InputOutputExceptions
				// TODO Auto-generated catch block
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
