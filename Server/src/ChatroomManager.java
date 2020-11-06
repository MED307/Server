import java.util.ArrayList;

public class ChatroomManager {
	
	// An ArrayList of chatrooms is created
	private ArrayList<Chatroom> chatrooms = new ArrayList<>();
	
	
	// A method that returns an ArrayList with all the chatrooms
    public ArrayList<Chatroom> getChatrooms() {
		
	    	return chatrooms;

    }
	
	// A method that creates a new chatroom
	public void createChatroom(User user, String name) {
		
		// A new chatroom object is declared
		Chatroom newChatroom;
		
		// The new chatroom object is instantiated
		newChatroom = new Chatroom(user,name);
		
		// The new chatroom is added to the ArrayList of chatrooms
		chatrooms.add(newChatroom);
		
	}
	
	// A method that deletes the chatroom chosen by the user
	public void deleteChatroom(Chatroom chatroom) {
		
		// A for each loop that goes through all of the chatrooms
		for(Chatroom i : chatrooms) {
			
			// An if-statement that checks whether the id of the chosen user matches the id of any of the chatrooms in the database
			if(i.getChatId() == chatroom.getChatId()) {
				
				// The chatroom with the matching id is removed
				chatrooms.remove(i);
		}
	}
  }
}