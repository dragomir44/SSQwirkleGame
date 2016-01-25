package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

import controller.Player;
import model.Move;
import view.Game;

public class ClientHandler extends Thread {

    private Server server;
    private Socket sock;
    private BufferedWriter out;
    private BufferedReader in;
    private String clientName;
    private Game game;
    private Player player;
    public boolean rematch;

    public ClientHandler(Server serverArg, Socket sock) throws IOException {
        in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
        server = serverArg;
        this.sock = sock;
    }

    //Handle client commands
    public void run() {
        try {
        	String msg = in.readLine().trim();
            do {
            	switch (msg) {
            		case "Connection.Shutdown":
            			shutdown();
            			break;
            		//starts with
            		case Protocol.CLIENT_CORE_JOIN:
            			Random rn = new Random();
            			clientName = "Player" + rn.nextInt(100);
            			break;
            	}
            	server.readString(this, msg);
            } while (true);
        } catch (IOException e) {
            shutdown();
        }
    }

    public void sendMessage(String msg) {
        try {
            out.write(msg);
            out.newLine();
            out.flush();
        } catch (IOException e) {
            shutdown();
        }
    }

    private void shutdown() {
        server.removeHandler(this);
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public String getClientName() {
        return clientName;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public boolean makeMove(ArrayList<Move> moves) {
        // stuur moves naar server
        // lees of ze correct zijn
    }

    // ontvang moves van de server
    public ArrayList<Move> determineMove() {

    }

    public

}