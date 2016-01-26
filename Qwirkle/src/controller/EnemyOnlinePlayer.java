package controller;


import model.Move;

import java.util.ArrayList;

public class EnemyOnlinePlayer extends Player {
    // enemy online player is een player aan de server kant
    // deze class verwerkt handler moves.

    public EnemyOnlinePlayer(String name) {
        super(name);
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
