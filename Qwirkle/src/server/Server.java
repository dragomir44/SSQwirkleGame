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

import controller.OnlinePlayer;
import java.io.BufferedReader;
import view.Game;
import view.MultiplayerGame;

public class Server {
    private int port;
    private List<ClientHandler> threads;
    private List<ClientHandler> lobby;
    private List<ClientHandler> ingame;
    private Map<Game, ArrayList<ClientHandler>> gameMap = 
    			  new HashMap<Game, ArrayList<ClientHandler>>();
    private ServerSocket sock;


    public static void main(String[] args) {
        new Server();
    }

    public Server() {
        threads = new ArrayList<ClientHandler>();
        lobby = new ArrayList<ClientHandler>();
        ingame = new ArrayList<ClientHandler>();
        try {
            String portString = getInput("(leave empty for port 1337) \nPort: ");
            if (portString.equals("")) {
            	port = Integer.parseInt(Protocol.PORT);
            } else {
                port = Integer.parseInt(portString);
            }
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
                System.out.println("\n<Client" + (++clientNo) + " " 
                		  + handler.getClientName() + "> connected");
            }
        } catch (IOException e) {
            serverMessage("*ERROR* Socket couldn't be created.");
        }
	}

	public MultiplayerGame startGame(ClientHandler handler) {
        MultiplayerGame game;
        StringBuilder playersString = new StringBuilder();
		OnlinePlayer player;
		if (lobby.size() != 1) {
            ArrayList<OnlinePlayer> players = new ArrayList<OnlinePlayer>();
            ArrayList<ClientHandler> handlers = new ArrayList<>();
			while (players.size() < 4 && !lobby.isEmpty()) {
                ClientHandler playerHandler = lobby.remove(0);
                handlers.add(playerHandler);
				player = new OnlinePlayer(playerHandler);
				ingame.add(playerHandler);
				players.add(player);
				playersString.append(Protocol.MESSAGESEPERATOR + player.getName());
			}
            game = new MultiplayerGame(players, handlers);
            gameMap.put(game, handlers);
            sendMessageToGamePlayers(handlers, Protocol.SERVER_CORE_START 
            			  + playersString.toString());
            System.out.println("Starting the game");
            game.start();
		} else {
			sendMessage(handler, "You're alone... can't start a game now");
			sendMessage(handler, Protocol.SERVER_CORE_START_DENIED);
            game = null;
		}
		return game;
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
    
    //test
    public void print(ClientHandler handler, String msg) {
    	System.out.println("Sending:   " + msg + " to " + handler.getClientName());
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
        sendMessageToGamePlayers(getOpponentsOfHandler(handler), 
        				Protocol.SERVER_CORE_TIMEOUT_EXCEPTION 
        			  + Protocol.MESSAGESEPERATOR + handler.getClientName());
    }

    public synchronized List<ClientHandler> getLobby() {
    	return lobby;
    }
    
    public synchronized List<ClientHandler> getIngame() {
    	return ingame;
    }
    
    public synchronized Map<Game, ArrayList<ClientHandler>> getGameMap() {
    	return gameMap;
    }
    
	public ArrayList<ClientHandler> getPlayersOfHandler(ClientHandler handler) {
		ArrayList<ClientHandler> playersOfHandler = new ArrayList<ClientHandler>();
		for (ArrayList<ClientHandler> handlers : gameMap.values()) {
			if (handlers.contains(handler)) {
				playersOfHandler = handlers;
			}
		}
		return playersOfHandler;
	}
	
	public ArrayList<ClientHandler> getOpponentsOfHandler(ClientHandler handler) {
		ArrayList<ClientHandler> opponents = new ArrayList<ClientHandler>();
		for (ArrayList<ClientHandler> handlers : gameMap.values()) {
			if (handlers.contains(handler)) {
				opponents = handlers;
				opponents.remove(handler);
			}
		}
		return opponents;
	}

    private void makeMove(ClientHandler handler, String move) {
    	//TODO Make move method
        // zet tile int1 int2 om naar Tile shape, color
        // zet String move om naar row, col, Tile
        // voeg toe aan ArrayList<Move>
        // koppel aan LocalOnlinePlayer


    }
    
    public void sendMessage(ClientHandler handler, String message) {
        handler.sendMessage(message);
    }
    
	public void sendMessageToGamePlayers(ArrayList<ClientHandler> players, String msg) {
		for (ClientHandler handler : players) {
			sendMessage(handler, msg);
		}
	}
	
    private void serverMessage(String msg) {
    	System.err.println("SERVER: " + msg);
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
