
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class User implements Serializable {//Serializable allows conversion data types to bytes and stores the data and datatype. so it can be sent through Sockets


    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String username;
    private String password;
    private Chatroom currentChatRoom;
    private String id;

    private ArrayList<String> chatRooms = new ArrayList<>();

    public User(String username, String password){
        this.username = username;
        this.password = password;

        Random cd = new Random();
        id = username + cd.nextInt();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public ArrayList<String> getChatRooms() {
        return chatRooms;
    }

    public void setChatRooms(ArrayList<String> chatRooms) {
        this.chatRooms = chatRooms;
    }

    public void addChatRoom(String chatRoom) {
        this.chatRooms.add(chatRoom);
    }

    public Chatroom getCurrentChatRoom() {
        return currentChatRoom;
    }

    public void setCurrentChatRoom(Chatroom currentChatRoom) {
        this.currentChatRoom = currentChatRoom;
    }

}