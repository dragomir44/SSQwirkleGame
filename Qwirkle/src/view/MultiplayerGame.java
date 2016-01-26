package view;

import controller.Board;
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
    private Player players;
    private int numberOfPlayers;
    private ClientHandler client;

    public MultiplayerGame() {
        super(new ArrayList<Player>()); // initally send empty player list
    }

    public void addPlayer(String playerName) {

    }

    @Override
    public ArrayList<Tile> replenishTiles(Player p) {
    	ArrayList<Tile> newTiles = super.replenishTiles(p);
    	//TODO send new tiles to player
        return newTiles;
    }
    
    //If player wants to replace tiles
    @Override
    public ArrayList<Tile> replaceTiles(Player p,  Set<Integer> tilenrs) {
        return null;
    }

    @Override
    public void start() {

    }

    @Override
    public void play() {

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

//    private void play() {
//        boolean gameOver = true;
//        while(!gameOver) {
//            // wacht op moves van clientHandler
//            if (client.bufferAvailable()) {
//                ArrayList<Move> clientMoves = client.getMoveBuffer();
//                board.setField(clientMoves);
//            }
//            if (client.playerTurnBuffer().equals(localName)) { // jouw beurt
//                client.makeMove(localOnlinePlayer.makeMove());
//            }
//
//            if (client.gameOverBufferAvailable()) {
//                if(client.getGameOver()) {
//                    print(client.getScores());
//                    gameOver = true;
//                };
//            }
//
//        }
//    }


}
