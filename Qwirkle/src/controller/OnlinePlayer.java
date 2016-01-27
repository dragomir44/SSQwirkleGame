package controller;


import model.Move;
import model.Tile;
import server.ClientHandler;
import server.Protocol;

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

    public ArrayList<Move> determineMove(Board board) {
        // send message to client that it's his turn
        handler.sendTurn(getName());
        return null;
    }
}
