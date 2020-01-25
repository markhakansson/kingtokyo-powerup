package KingTokyo.monsters;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.net.Socket;

public class Player {
    private String name;
    private Socket connection;
    private BufferedReader inFromClient;
    private DataOutputStream outToClient;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setSocket(Socket socket) {
        this.connection = socket;
    }

    public Socket getSocket() {
        return this.connection;
    }

    public void setReader(BufferedReader reader) {
        this.inFromClient = reader;
    }

    public BufferedReader getReader() {
        return this.inFromClient;
    }

    public void setOutputStream(DataOutputStream stream) {
        this.outToClient = stream;
    }

    public DataOutputStream getOutputStream() {
        return this.outToClient;
    }
}