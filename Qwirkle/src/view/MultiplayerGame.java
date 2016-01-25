package view;

import controller.Board;
import controller.Player;
import model.Bag;
import model.Move;
import server.ClientHandler;

import java.util.ArrayList;

public class MultiplayerGame {
    // hier handle je de moves voor localOnlinePlayer en de clientHandler
    private Board board;
    private Player[] players;
    private int numberOfPlayers;
    private ClientHandler client;

    public void MultiplayerGame() {

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
