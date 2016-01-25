package controller;

import model.Move;

import java.util.ArrayList;
import java.util.Scanner;

public class LocalOnlinePlayer extends HumanPlayer {

    public LocalOnlinePlayer(String name) {
        super(name);
    }

    @Override
    public ArrayList<Move> determineMove(Board board) {
        ArrayList<Move> moves = super.determineMove(board);
        // stuur moves naar clientHandler
        // handler.makeMove(ArrayList<Move>)
        return moves; // plaats moves op eigen board
    }

    @Override
    protected String readString(String prompt) {
        //TODO multiplayer in
        return "";
    }

    @Override
    public void writeString(String msg) {
        //TODO multiplayer out
    }
}
