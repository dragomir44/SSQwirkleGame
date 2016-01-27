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
    private ArrayList<OnlinePlayer> players;
    private ArrayList<ClientHandler> handlers;

    public MultiplayerGame(ArrayList<OnlinePlayer> players, ArrayList<ClientHandler> handlers) {
        super(new ArrayList<Player>(players)); // initally send empty player list
        this.players = players;
        this.handlers = handlers;
        for (ClientHandler handler : handlers) {
            // pass this game object to each handler
            handler.setGame(this);
        }
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
    protected boolean readBoolean(String prompt, String yes, String no) {
        return false;
    }

    @Override
    public boolean makeMove(Player curPlayer, ArrayList<Move> moves) {
        System.out.println("Player " + curPlayer + " made moves: " + moves);
//        board.setField(moves);
        return true;
    }

    public void placeTiles(ArrayList<Move> moves) {
        board.setField(moves);
    }

    @Override
    public void play() {
        update();
        while (!gameOver()) {
            if (bag.getBag().isEmpty() &&
                    !board.hasPossibleMoves(players.get(current).getHand().getTiles())) {
                System.out.println("No move possible, skipped a turn");
            } else {
                boolean validMove = false;
                OnlinePlayer curPlayer = players.get(current);
                curPlayer.writeString(curPlayer.getName() + "'s turn:");
                do {
                    ArrayList<Move> moves = curPlayer.determineMove(board);
                    validMove = curPlayer.getHandler().isMoveDone();
                } while (!validMove);
            curPlayer.getHandler().moveIsOver();
            }
            update();
            current = (current + 1) % numberOfPlayers;

        }
        System.out.println("Game over!");
    }

    @Override
    public boolean gameOver() {
        return false;
    }
}
