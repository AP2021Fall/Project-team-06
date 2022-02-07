package jira.client.view;

import jira.RPCMessage;

import java.io.*;
import java.net.Socket;

public class RPCExecutor {
    private static String token;
    private static final String serverIp = "127.0.0.1";
    private static final int serverPort = 5000;

    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    public RPCExecutor() {
        try {
            setToken();
            closeAll();
            this.socket = new Socket(serverIp, serverPort);
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());
        }
        catch (IOException e) {
            throw new RuntimeException("ERROR");
        }
    }

    private void closeAll() {
        try {
            if (this.socket != null) {
                this.socket.close();
                this.objectInputStream.close();
                this.objectOutputStream.close();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setToken() {
        if (token != null)
            return;

        try {
            this.socket = new Socket(serverIp, serverPort);
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());

            RPCMessage rpcMessage = new RPCMessage("", "UserController", "genToken", null);
            token = (String) exec(rpcMessage);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Object exec(RPCMessage rpcMessage) {
        try {
            objectOutputStream.writeObject(rpcMessage);
            objectOutputStream.flush();
            Object output = objectInputStream.readObject();
            closeAll();
            return output;
        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Object execute(String className, String methodName, Object... args) {
        RPCMessage rpcMessage = new RPCMessage(token, className, methodName, args);
        return exec(rpcMessage);
    }
}
