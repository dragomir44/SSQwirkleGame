package controller;


import model.Move;
import model.Tile;
import server.ClientHandler;

import java.util.ArrayList;

public class OnlinePlayer extends Player {
    // enemy online player is een player aan de server kant
    // deze class verwerkt handler moves.
    ClientHandler handler;

    public OnlinePlayer(ClientHandler handler) {
        super(handler.getClientName());
        this.handler = handler;
    }

    public ClientHandler getHandler() {
        return handler;
    }
    @Override
    public ArrayList<Move> determineMove(Board board) {
//        ArrayList<Move> moves = new ArrayList<>();
//        boolean valid = false;
//        do {
//            moves = client.determineMove();
//            if(board.isValidMove(moves)) {
//                valid = true;
//            }
//        } while(!valid);
      	return null;
    }
      
//    @Override
//    pulic void sendString(String msg) {
//    	// send text to player
//    }

}
