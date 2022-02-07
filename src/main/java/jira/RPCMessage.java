package jira;

import java.io.Serializable;

public class RPCMessage implements Serializable {
    private final String token;
    private final String className;
    private final String methodName;
    private final Object[] args;

    public RPCMessage(String token, String className, String methodName, Object[] args) {
        this.token = token;
        this.className = className;
        this.methodName = methodName;
        this.args = args;
    }

    public <T> T getArg(int index) {
        return (T) args[index];
    }

    public String getToken() {
        return token;
    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }

    public Object[] getArgs() {
        return args;
    }
}
