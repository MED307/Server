import java.io.FileWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Scanner;


public class UserDataBase {

	ArrayList<User> users = new ArrayList<>();
	
	
	//Create the user
	void newUser(String username, String password) {
		User newUser = new User(username,password);
		users.add(newUser);
	}
	
	//delete the user
	void deleteUser(String username) {
		for(int i = 0 ; i < users.size(); i++) {
			if(users.get(i).getUsername() == username ) {
				users.remove(i);
			}
		}
	}
	
	public ArrayList<User> getUsers(){
		return users;
	}
	
	void addUser(User user) {
		users.add(user);
	}
}

