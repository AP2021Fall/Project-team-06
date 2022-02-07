package jira.server.controller;

import jira.server.model.User;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import jira.RPCMessage;

public class RPCServer {
    private static final int PORT = 5000;

    private ServerSocket serverSocket;
    private HashMap<String, User> tokenMap;

    public RPCServer () {
        this.tokenMap = new HashMap<>();

        try {
            this.serverSocket = new ServerSocket(PORT);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void serveForever() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> System.out.println("Server shutting down")));

        while (true) {
            try {
                Socket switchedSocket = serverSocket.accept();
                new Thread(() -> handleClient(switchedSocket)).start();
            }
            catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    private void handleClient(Socket switchedSocket) {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(switchedSocket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(switchedSocket.getInputStream());

            RPCMessage rpcMessage = (RPCMessage) objectInputStream.readObject();
            Object rpcReturn = executeRpc(rpcMessage);

            objectOutputStream.writeObject(rpcReturn);
            objectOutputStream.flush();

            switchedSocket.close();
            objectInputStream.close();
            objectOutputStream.close();
        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Object executeRpc(RPCMessage rpcMessage) {
        String className = rpcMessage.getClassName();
        String methodName = rpcMessage.getMethodName();

        try {
            Class<?> controllerClass = Class.forName(this.getClass().getPackageName() + "." + className);
            Object controllerObject = controllerClass.getMethod("getController").invoke(null);
            Method[] methods = controllerObject.getClass().getMethods();
            for (Method method: methods)
                if (getTailName(method.getName()).equals(methodName))
                    return method.invoke(controllerObject, rpcMessage.getArgs());
            return null;
        }
        catch (ClassNotFoundException | IllegalAccessException |
                NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String getTailName(String name) {
        String[] nameSplit = name.split("\\.");
        return nameSplit[nameSplit.length-1];
    }
}
