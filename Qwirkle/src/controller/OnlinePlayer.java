package controller;

import model.Move;

import java.util.ArrayList;
import java.util.Scanner;

public class OnlinePlayer extends HumanPlayer {

    public OnlinePlayer(String name) {
        super(name);
    }

    @Override
    public ArrayList<Move> determineMove(Board board) {
       return super.determineMove(board);
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
