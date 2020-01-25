package KingTokyo.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import KingTokyo.monsters.*;
import KingTokyo.cards.*;
import KingTokyo.game.*;

public class Server {
    private static Scanner sc;
    private ServerSocket socket;
    private Game game;

    /**
     * Initializes a game state. Use {@link #connectToPlayers(int, int)} to connect to
     * players and {@link #start()} to start the game.
     * @param monsters The monsters to play.
     * @param deck The deck to use.
     */
    public Server(ArrayList<Monster> monsters, Deck deck) {
        sc = new Scanner(System.in);
        game = new Game(monsters, deck);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        CardFactory cardFactory = new CardFactory();
        Deck deck = cardFactory.createNormalDeck();

        MonsterFactory monsterFactory = new MonsterFactory();
        ArrayList<Monster> monsters = monsterFactory.getMonsters(2);

        Server server = new Server(monsters, deck);
        server.connectToPlayers(2048, 2);
        server.startGame();
        server.disconnect();
    }

    public void startGame() {
        game.run();
    }

    /**
     * Connects to other players through the specified socket.
     * 
     * Waits until the given number of players has connected.
     * @param socketNum Socket number to connect through.
     * @param noPlayers Number of players excluding the server.
     */
    public void connectToPlayers(int socketNum, int noPlayers) {
        // Server stuffs
        try {
            this.socket = new ServerSocket(socketNum);

            for (int onlineClient = 0; onlineClient < noPlayers; onlineClient++) {
                Monster player = game.state.monsters.get(onlineClient);
                Socket connectionSocket = socket.accept();
                BufferedReader inFromClient = new BufferedReader(
                        new InputStreamReader(connectionSocket.getInputStream()));
                DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
                outToClient.writeBytes("You are the monster: " + player.getName() + "\n");
                player.setSocket(connectionSocket);
                player.setReader(inFromClient);
                player.setOutputStream(outToClient);
                System.out.println("Connected to " + player.getName());
            }
        } catch (Exception e) {
            System.out.println("Could not connect.");
        }
    }

    /**
     * Closes the server socket
     */
    public void disconnect() {
        try {
            socket.close();
        } catch (Exception e) {}
    }

    /**
     * Sends a message to a specified monster's client. Returns a response from the 
     * monster.
     * 
     * @param monster The receiving monster.
     * @param message The message to send.
     * @return Returns a response from the monster.
     */
    public static String sendMessage(Monster monster, String message) {
        String response = "";
        if (monster.getSocket() != null) {
            try {
                monster.getOutputStream().writeBytes(message);
                response = monster.getReader().readLine();
            } catch (Exception e) {}
        } else {
            String[] theMessage = message.split(":");
            for (int i = 0; i < theMessage.length; i++) {
                System.out.println(theMessage[i].toString());
            }
            if (!(theMessage[0].equals("ATTACKED") || theMessage[0].equals("ROLLED")
                    || theMessage[0].equals("PURCHASE"))) {
                System.out.println("Press [ENTER]");
            }
            response = sc.nextLine();
        }
        return response;
    }
}
