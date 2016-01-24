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
    private List<ClientHandler> connecting;
    private List<ClientHandler> lobby;
    private List<ClientHandler> ingame;
    private Map<Game, ClientHandler[]> gameMap = new HashMap<Game, ClientHandler[]>();
    private ServerSocket sock;


    public static void main(String[] args) {
        new Server();
    }

    public Server() {
        connecting = new ArrayList<ClientHandler>();
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
                new Server();
            }
            startServer();
        } catch (NumberFormatException e) {
            serverMessage("*ERROR* port is not an integer");
            new Server();
        }
    }

    private void startServer() {
        try {
            sock = new ServerSocket(port);
            int clientNo = 0;
            while (true) {
                Socket socket = sock.accept();
                ClientHandler handler = new ClientHandler(this, socket);
                handler.start();
                addHandler(handler);
                System.out.println("<Client " + (++clientNo) + "> connected");
            }
        } catch (IOException e) {
            serverMessage("*ERROR* Socket couldn't be created.");
        }
	}


	public String getInput(String question) {
        String input = null;
        try {
            if (question != "") {
                System.out.println(question);
            }
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
        if (lobby.contains(handler) || ingame.contains(handler)
                || connecting.contains(handler)) {
            serverMessage("Sending " + message + "to " + handler.getClientName());
            handler.sendMessage(message);
        }
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
        connecting.add(handler);
    }

    public synchronized void removeHandler(ClientHandler handler) {
        if (connecting.contains(handler)) {
            connecting.remove(handler);
        } else if (ingame.contains(handler)) {
            ingame.remove(handler);
        } else if (lobby.contains(handler)) {
            ingame.remove(handler);
        }
        serverMessage(handler.getClientName() + " has left the game");
        //send message to other players that this guy left
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
    }

    public synchronized void readString(ClientHandler handler, String msg) {
    	serverMessage(handler + " sends " + msg);
    	String[] input = msg.split(Protocol.MESSAGESEPERATOR);
		do {
			switch (input[0]) {
				case Protocol.CLIENT_CORE_JOIN:
					connecting.remove(handler);

					break;
				case Protocol.CLIENT_CORE_MOVE:
					makeMove(handler, input[1]);
					break;
				case Protocol.CLIENT_CORE_PLAYERS:
					for (int i = 0; i < lobby.size(); i++) {
						sendMessage(handler, Protocol.SERVER_CORE_PLAYERS +
							   Protocol.MESSAGESEPERATOR + lobby.get(i).getClientName());
					}
					break;
			}
		} while (true);
    }


}
