package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

import controller.Player;
import model.Move;
import model.Tile;
import view.Game;
import view.MultiplayerGame;

public class ClientHandler extends Thread {

    private Server server;
    private BufferedWriter out;
    private BufferedReader in;
    private String clientName;
    private Socket socket;
    public boolean rematch;
    private MultiplayerGame game;

    public ClientHandler(Server serverArg, Socket sock) throws IOException {
        in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
        server = serverArg;
        socket = sock;
    }

    public void run() {
		try {
			while (true) {
				String line = in.readLine();
				if (line != null) {
					readString(this, line);
				} else {
					break;
				}
			}
		} catch (IOException e) {
			shutdown();
		}
	}

    public void sendMessage(String msg) {
    	//Test
    	server.print(this, msg);
        try {
            out.write(msg);
            out.newLine();
            out.flush();
        } catch (IOException e) {
        	System.out.println("CONNECTION LOST");
            shutdown();
        }
    }

    private void shutdown() {
        server.removeHandler(this);
    }
    
    public String getClientName() {
    	if (clientName == null) {
    		return String.valueOf(socket.getInetAddress());
    	} else {
    		return clientName;
    	}
    }
    
    public void announce() throws IOException {
        clientName = in.readLine();
        server.sendMessage(this, "[" + clientName + " has entered]");
    }

    public synchronized void readString(ClientHandler handler, String msg) {
    	System.out.println("Received: " + msg);
    	String[] input = msg.split(Protocol.MESSAGESEPERATOR);
    	boolean done = false;
		do {
			switch (input[0]) {
				case Protocol.CLIENT_CORE_EXTENSION:
					sendMessage(Protocol.SERVER_CORE_EXTENSION);
					done = true;
					break;
				case Protocol.CLIENT_CORE_LOGIN:
					if (input[1].contains(" ") || server.getLobby().contains(input[1])) {
						sendMessage(Protocol.SERVER_CORE_LOGIN_DENIED);
					} else {
						server.addHandler(handler);
						clientName = input[1];
						server.addHandlerToLobby(this);
						System.out.println("Lobby size: " + server.getLobby().size());
						System.out.println("Lobby : " + server.getLobby().iterator().toString());
						sendMessage(Protocol.SERVER_CORE_LOGIN_ACCEPTED);
						System.out.println("Clientname " + clientName + " assigned to " + this);
					}
					done = true;
					break;
				case Protocol.CLIENT_CORE_JOIN:
					Random rn = new Random();
					String clientNo = Integer.toString(rn.nextInt(99) + 1);
					clientName = "Player" + clientNo;
					sendMessage(Protocol.SERVER_CORE_JOIN_ACCEPTED + 
								  Protocol.MESSAGESEPERATOR + clientName);
					System.out.println("Clientname " + clientName + " set to " + this.getName());
					server.addHandlerToLobby(this);
					System.out.println("Lobbby size: " + server.getLobby().size());
					System.out.println("Lobby : " + server.getLobby().toArray().toString());
					done = true;
					break;
					// when Protocol.CLIENT_CORE_JOIN_DENIED?
				case Protocol.CLIENT_CORE_PLAYERS:
					StringBuilder players = getPlayersFromLobby();
					sendMessage(Protocol.SERVER_CORE_PLAYERS + players.toString());
					done = true;
					break;
				case Protocol.CLIENT_CORE_START:
					//Check amount of players in lobby --> create game with those
					game = new MultiplayerGame();
	//				game.addPlayer(Player clientPlayer);
					game.start();
					//send all clients clients names +  SERVER_CORE_START
					//SERVER_CORE_TURN
					// else SERVER_CORE_START_DENIED
					done = true;
					break;
				case Protocol.CLIENT_CORE_MOVE:
					Move receivedMove = translateMove(input);
					// if move = valid
					if (true) {
						//plaats move op server gameboard
						sendMessage(Protocol.SERVER_CORE_MOVE_ACCEPTED);
						//stuur alle spelers deze move
					} else {
						// if tile is not in hand of client i.e.
						sendMessage(Protocol.SERVER_CORE_MOVE_DENIED);
					}
					// makeMove(handler, input[1]);
					done = true;
					break;
				case Protocol.CLIENT_CORE_DONE:
					// sendMessage(SERVER_CORE_SEND_TILE) met het juiste aantal tiles in Shape en Kleur als integer
					sendMessage(Protocol.SERVER_CORE_DONE);
					//broadcast SERVER_CORE_SCORE met (Naam Integer) paren
					done = true;
					break;
				case Protocol.CLIENT_CORE_SWAP:
					Tile receivedTile = translateTile(input);
					// if tile = valid
					// check if tile is in player hand:
					//game.replaceTiles(player, tilenrs);
					if (true) {
						//plaats move op server gameboard
						sendMessage(Protocol.SERVER_CORE_MOVE_ACCEPTED);
						//stuur alle spelers deze move
					} else {
						// if tile is not in hand of client i.e.
						sendMessage(Protocol.SERVER_CORE_MOVE_DENIED);
					}	
					done = true;
					break;
				default:
					System.out.println(msg);
					break;
			}
		} while (!done);
    }
    
    public Move translateMove(String[] moveInput) {
    	int x = Integer.parseInt(moveInput[1]);
    	int y = Integer.parseInt(moveInput[2]);
    	int shape = Integer.parseInt(moveInput[3]);
    	int colour = Integer.parseInt(moveInput[4]);
    	Tile moveTile = new Tile(shape, colour);
    	Move move = new Move(x, y, moveTile);
    	return move;
    }
  
    public Tile translateTile(String[] tileInput) {
    	Tile tile = new Tile(Integer.parseInt(tileInput[1]), Integer.parseInt(tileInput[2]));
    	return tile;
    }
    
    public StringBuilder getPlayersFromLobby() {
		StringBuilder players = new StringBuilder();
		for (int i = 0; i < server.getLobby().size(); i++) {
			players.append(Protocol.MESSAGESEPERATOR 
						  + server.getLobby().get(i).getClientName());
		}
		return players;
    }

    public boolean makeMove(ArrayList<Move> moves) {
        // stuur moves naar server
        // lees of ze correct zijn
    	return true;
    }

    // ontvang moves van de server
    public ArrayList<Move> determineMove() {
    	return null;
    }

}