package model;
import java.io.*;
import java.util.ArrayList;

public class UserSave{

    private static ArrayList<User> users = new ArrayList<>();

    protected static ArrayList<User> getUsers() {
        return users;
    }

    protected static void addUser(User user) {
        users.add(user);
    }

    protected static void load(){
        try {
            FileInputStream fileIn = new FileInputStream("UsersSave");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            users = (ArrayList<User>) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException ex){
            return;
        } catch (ClassNotFoundException ex) {
            return;
        }
        return;
    }

    protected static void save(){
        try{
            FileOutputStream fileOut = new FileOutputStream("UsersSave");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(users);
            out.close();
            fileOut.close();
        } catch (IOException ex) {
            System.out.println("IOException save error");
        }
    }
}
