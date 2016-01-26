package server;


import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Move;

import java.io.BufferedReader;
import view.Game;

public class Server {
    private int port;
    private List<ClientHandler> threads;
    private List<ClientHandler> lobby;
    private List<ClientHandler> ingame;
    private Map<Game, ClientHandler[]> gameMap = new HashMap<Game, ClientHandler[]>();
    private ServerSocket sock;


    public static void main(String[] args) {
        new Server();
    }

    public Server() {
        threads = new ArrayList<ClientHandler>();
        lobby = new ArrayList<ClientHandler>();
        ingame = new ArrayList<ClientHandler>();
        try {
            //String portString = getInput("(leave empty for port 1337) \nPort: ");
          //  if (portString.equals("")) {
            	port = Integer.parseInt(Protocol.PORT);
          //  } else {
          //      port = Integer.parseInt(portString);
         //   }
            try {
                serverMessage("Starting with IP: " + InetAddress.getLocalHost()
                		  + " and port: " + port + "\n");
            } catch (UnknownHostException e) {
                serverMessage("*ERROR* Couldn't find IP");
            }
            startServer();
        } catch (NumberFormatException e) {
            serverMessage("*ERROR* port is not an integer");
        }
    }

    private void startServer() {
        try {
            sock = new ServerSocket(port);
            int clientNo = 0;
            while (sock != null) {
                Socket clientSocket = sock.accept();
                ClientHandler handler = new ClientHandler(this, clientSocket);
                handler.start();
                addHandler(handler);
                System.out.println("<Client" + (++clientNo) + " " 
                		  + handler.getClientName() + "> connected");
            }
        } catch (IOException e) {
            serverMessage("*ERROR* Socket couldn't be created.");
        }
	}


	public String getInput(String question) {
        String input = null;
        try {
        	System.out.println(question);    
            input = new BufferedReader(new InputStreamReader(System.in)).readLine().trim();
            if (input.equalsIgnoreCase("exit")) {
                shutdown();
            }
        } catch (IOException e) {
            serverMessage("*ERROR* Something went wrong");
            shutdown();
        }
        return input;
    }

    public void sendMessage(ClientHandler handler, String message) {
        serverMessage("Sending " + message + " to " + handler.getClientName());
        handler.sendMessage(message);
    }


	private void shutdown() {
		serverMessage("CLOSING SERVER");
        try {
            sock.close();
        } catch (IOException e) {
            System.exit(0);
        }
        System.exit(0);
	}

    public synchronized void addHandler(ClientHandler handler) {
        threads.add(handler);
    }

    public synchronized void removeHandler(ClientHandler handler) {
    	threads.remove(handler);
        serverMessage(handler.getClientName() + " has left the game");
    }
    
    public synchronized void addHandlerToLobby(ClientHandler handler) {
    	lobby.add(handler);
    }

    public synchronized List<ClientHandler> getLobby() {
    	return lobby;
    }
    
    private void serverMessage(String msg) {
    	System.err.println("SERVER: " + msg);
    }
    //TODO  Make method to send to every other player in game

    public ClientHandler otherHandler(ClientHandler handler) {
    	//TODO get other players from game
    	return null;
    }

    private void makeMove(ClientHandler handler, String move) {
    	//TODO Make move method
        // zet tile int1 int2 om naar Tile shape, color
        // zet String move om naar row, col, Tile
        // voeg toe aan ArrayList<Move>
        // koppel aan LocalOnlinePlayer


    }

    public void broadcast(String msg) {
    	System.out.println(msg);
        for (ClientHandler thread : threads) {
        	if (thread != null) {
        		thread.sendMessage(msg);
        	}
        }
    }

}
