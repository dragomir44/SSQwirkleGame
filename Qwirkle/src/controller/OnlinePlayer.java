package controller;


import model.Move;
import server.ClientHandler;

import java.util.ArrayList;

public class OnlinePlayer extends Player {
    // enemy online player is een player aan de server kant
    // deze class verwerkt handler moves.
    ClientHandler client;

    public OnlinePlayer(String name, ClientHandler client) {
        super(name);
        this.client = client;
    }

    @Override
    public ArrayList<Move> determineMove(Board board) {
        // client handler: ArrayList<Move> moves = handler.determineMove();
        // return moves;
    	return null;
    }
      
//    @Override
//    pulic void sendString(String msg) {
//    	// send text to player
//    }

}
