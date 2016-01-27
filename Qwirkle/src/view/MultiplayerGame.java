package view;

import controller.Board;
import controller.OnlinePlayer;
import controller.Player;
import model.Bag;
import model.Move;
import model.Tile;
import server.ClientHandler;

import java.util.ArrayList;
import java.util.Set;

public class MultiplayerGame extends Game {
    // hier handle je de moves voor localOnlinePlayer en de clientHandler
    private Board board;
    private Bag bag;
    private int current;
    private int numberOfPlayers;
    private ArrayList<OnlinePlayer> players;
    private ArrayList<ClientHandler> handlers;

    public MultiplayerGame(ArrayList<OnlinePlayer> players, ArrayList<ClientHandler> handlers) {
        super(new ArrayList<Player>()); // initally send empty player list
        this.players = players;
        this.handlers = handlers;
    }

    public void addPlayer(String playerName) {

    }

    public void broadcastMessage(String msg) {
        for (ClientHandler handler : handlers) {
            handler.sendMessage(msg);
        }
    }
    //TODO scores

    @Override
    public ArrayList<Tile> replenishTiles(Player p) {
        System.out.println("Replenishing tiles in Multiplayer game");
    	ArrayList<Tile> newTiles = super.replenishTiles(p);
        ((OnlinePlayer) p).getHandler().sendTilesToClient(newTiles);
        System.out.println("Tiles " + newTiles.toString() + " send to " + p.getName());
    	return newTiles;
    }
    
    //If player wants to replace tiles
    @Override
    public ArrayList<Tile> replaceTiles(Player p,  Set<Integer> tilenrs) {
        return null;
    }

    @Override
    public void start() {
        broadcastMessage("Game started");
        super.start();
    	//Send tiles from dealTiles() to server in MultiplayerGame
    }

    @Override
    public void dealTiles() {
        for (OnlinePlayer player : players) {
            replenishTiles(player);
        }
    }

    @Override
    public void play() {
        System.out.println("Started playing game");
        while(true) {
            // infinite loop. playing
        }
//        System.out.println("Escaped infinite loop");

    }

    @Override
    protected boolean readBoolean(String prompt, String yes, String no) {
        return false;
    }

    @Override
    public boolean makeMove(Player curPlayer, ArrayList<Move> moves) {
        return false;
    }

    public void placeTiles(ArrayList<Move> moves) {
        board.setField(moves);
    }

    @Override
    public boolean gameOver() {
        return false;
    }

    public Board getBoard() {
    	return board;
    }
}
