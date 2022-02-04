package jira.model;
import jira.controller.UserController;

import java.io.*;
import java.util.ArrayList;

public class UserSave{
    public static final String USERS_SAVE_FILE = "UsersSave";

    public static void init() {
        File saveFile = new File(USERS_SAVE_FILE);
        try {
            saveFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        UserController.getController().loadUsers();
    }

    private static ArrayList<User> users = new ArrayList<>();

    protected static ArrayList<User> getUsers() {
        return users;
    }

    protected static void addUser(User user) {
        users.add(user);
    }

    protected static void load(){
        try {
            FileInputStream fileIn = new FileInputStream(USERS_SAVE_FILE);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            Object savedObj = in.readObject();

            if (savedObj != null)
                users = (ArrayList<User>) savedObj;
            else
                users = new ArrayList<>();

            in.close();
            fileIn.close();
        } catch (EOFException e) {
            return;
        } catch (ClassNotFoundException | IOException ex){
            ex.printStackTrace();
        }

        System.out.println("LOADED");
    }

    protected static void save(){
        try{
            FileOutputStream fileOut = new FileOutputStream("UsersSave");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(users);
            out.close();
            fileOut.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        System.out.println("SAVED");
    }
}
