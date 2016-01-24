package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import controller.Player;
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
            while (true) {
                String input = in.readLine().trim();
                if (input.equals("Connection.Shutdown")) {
                    shutdown();
                } else if (input.startsWith(Protocol.CLIENT_CORE_JOIN)) {
                	long millis = System.currentTimeMillis() % 1000;
                    clientName = "player" + millis;
                //TODO Server string analyzer
                }
                //server.analyzeString(this, input);
            }
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

}