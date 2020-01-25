package KingTokyo.client;

import java.util.*;
import java.io.*;
import java.net.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Scanner;

// Refactor this!!!!!
public class Client {
    private Socket socket;
    private DataOutputStream outToServer;
    private BufferedReader inFromServer;
    private Random rnd;
    private Scanner sc;

    public Client() {
        rnd = ThreadLocalRandom.current();
        sc = new Scanner(System.in);
    }

    public void connectToServer(String host, int port) {
        try{
            socket = new Socket(host, port);
            outToServer = new DataOutputStream(socket.getOutputStream());
            inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String name = inFromServer.readLine();
            System.out.println(name);
        } catch(Exception e) {
            System.out.println("Could not connect to server.");
        }
    }

    public void startClient(boolean bot) {
        try{
            while(true) {
                String[] message = inFromServer.readLine().split(":");
                for(int i=0; i<message.length; i++) {
                    System.out.println(message[i].toString());
                }
                if(message[0].equalsIgnoreCase("VICTORY")) {
                    outToServer.writeBytes("Bye!\n");
                } else if(message[0].equalsIgnoreCase("ATTACKED")) {
                    if(bot)
                        outToServer.writeBytes("YES\n");
                    else {
                        outToServer.writeBytes(sc.nextLine() + "\n");
                    }
                } else if(message[0].equalsIgnoreCase("ROLLED")) {
                    if(bot) {
                        rnd = ThreadLocalRandom.current();
                        int num1 = rnd.nextInt(2) + 4;
                        int num2 = rnd.nextInt(2) + 1;
                        String reroll = ""+num1+","+num2+"\n";
                        outToServer.writeBytes(reroll);// Some randomness at least
                    } else {
                        outToServer.writeBytes(sc.nextLine() + "\n");
                    }
                } else if(message[0].equalsIgnoreCase("PURCHASE")) {
                    if(bot)
                        outToServer.writeBytes("-1\n");
                    else
                        outToServer.writeBytes(sc.nextLine() + "\n");
                } else if(message[0].equalsIgnoreCase("INFO")) {
                    if(bot)
                        outToServer.writeBytes("OK\n");
                    else    
                        outToServer.writeBytes(sc.nextLine() + "\n");
                } 
                /* else {
                    if(bot)
                        outToServer.writeBytes("OK\n");
                    else {
                        System.out.println("Press [ENTER]");
                        sc.nextLine();
                        outToServer.writeBytes("OK\n");
                    }
                } */
                System.out.println("\n");
            } 
            
        } catch(Exception e) {
            System.out.println("Server disconnected.");
        }
    }

    public static void main(String argv[]) {
        Client client = new Client();
        client.connectToServer("localhost", 2048);
        if(argv.length != 0) //Syntax: java KingTokyoPowerUpClient bot
            client.startClient(true);
        else
            client.startClient(false);
    }
}