package application.dataTypes;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Chatroom implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
	
	// The name of the chatroom
	private String name;
	
	// The id for the chatroom 
	private String chatId;
	
	
	// An ArrayList that stores all of the users in the chatroom
	private ArrayList<String> users = new ArrayList<>();
	
	//an arrayList of chat messages
	private ArrayList<ChatMessage> messages = new ArrayList<>();
	
	// A User object that stores the user that created the chatroom
	private User creator;
	
	// The constructor for the Chatroom class that creates a Chatroom where the user creating the chatroom is assigned the creator 
	// and where the chatroom is given a specific name
	public Chatroom(User user, String name){
		
		this.creator = user;
		this.name = name;
		
		// A random integer is assigned as the id for the chatroom
		Random rd = new Random();
        this.chatId = Integer.toString(rd.nextInt());
	}
	
	// Method that returns the name of the chatroom
	public String getChatroomName() {
		
		return this.name;
		
	}
	
	// Method that returns the unique id of the chatroom
	public String getChatId() {
		
		return chatId;
	}
	
	// Method that returns the name of the creator of the chatroom 
    public String getCreatorName() {
    	
		return this.creator.getUsername();
		
	}
    
    // Method that is used to display a message
	public String displayMessage(User user, String message) {
		
		return user.getUsername() + ": " + message;
		
	}
	
	// Method that is used to send a message
	public void sendMessage(String message) {
		
		//chatHistory.add(displayMessage());
		
	}
	public ArrayList<ChatMessage> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<ChatMessage> messages) {
        this.messages = messages;
    }

    public void addMessage(ChatMessage message) {
        this.messages.add(message);
    }

	public ArrayList<String> getUsers() {
		return users;
	}

	public void setUsers(ArrayList<String> users) {
		this.users = users;
	}
}
