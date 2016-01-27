package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

import model.Move;
import model.Tile;
import view.MultiplayerGame;

public class ClientHandler extends ServerMethods {

    private Server server;
    public boolean rematch;
    private MultiplayerGame game;

    public ClientHandler(Server serverArg, Socket sock) throws IOException {
        in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
        server = serverArg;
        this.sock = sock;
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

	@Override
    public void sendMessage(String msg) {
   		super.sendMessage(msg);
    	server.print(this, msg);
    }

	@Override
	public void shutdown() {
        server.removeHandler(this);
    }

	@Override
    public String getClientName() {
		String name = super.getClientName();
    	if (name == null) {
    		name = String.valueOf(sock.getInetAddress());
    	}
		return name;
    }
    
    public void announce() throws IOException {
        clientName = in.readLine();
        server.sendMessage(this, "[" + clientName + " has entered]");
    }

    public synchronized void readString(ClientHandler handler, String msg) {
    	System.out.println("Received:  " + msg);
    	String[] input = msg.split(Protocol.MESSAGESEPERATOR);
		switch (input[0]) {
			case Protocol.CLIENT_CORE_EXTENSION:
				sendMessage(Protocol.SERVER_CORE_EXTENSION);
				break;
			case Protocol.CLIENT_CORE_LOGIN:
				if (input[1].contains(" ") || server.getLobby().contains(input[1])) {
					sendMessage(Protocol.SERVER_CORE_LOGIN_DENIED);
				} else {
					server.addHandler(handler);
					clientName = input[1];
					server.getLobby().add(this);
					System.out.println("Lobby size: " + server.getLobby().size());
					System.out.println("Lobby : " + server.getLobby().iterator().toString());
					sendMessage(Protocol.SERVER_CORE_LOGIN_ACCEPTED);
					System.out.println("Clientname " + clientName + " assigned to " + this);
				}
				break;
			case Protocol.CLIENT_CORE_JOIN:
				Random rn = new Random();
				String clientNo = Integer.toString(rn.nextInt(99) + 1);
				clientName = "Player" + clientNo;
				sendMessage(Protocol.SERVER_CORE_JOIN_ACCEPTED + 
							  Protocol.MESSAGESEPERATOR + clientName);
				System.out.println("Clientname " + clientName + " set to " + this.getClientName());
				server.getLobby().add(this);
				System.out.println("Lobbby size: " + server.getLobby().size());
				System.out.println("Lobby : " + server.getLobby().toArray().toString());
				break;
				// when Protocol.CLIENT_CORE_JOIN_DENIED?
			case Protocol.CLIENT_CORE_PLAYERS:
				//TODO changes to get opponents
				StringBuilder players = getPlayersFromLobby();
				sendMessage(Protocol.SERVER_CORE_PLAYERS + players.toString());
				break;
			case Protocol.CLIENT_CORE_START:
				server.startGame(this);
				//TODO SERVER_CORE_TURN
				//TODO make method in Server to get a list of game
				server.sendMessageToGamePlayers(server.getPlayersOfHandler(this), 
								Protocol.SERVER_CORE_TURN + 
								Protocol.MESSAGESEPERATOR + this.getClientName());
				break;
			case Protocol.CLIENT_CORE_MOVE:
				Move receivedMove = stringToMove(input);
				//TODO Check is a single move is valid
				// if (game.getBoard().isValidMove(receivedMove)) {
				if (true) {
					//plaats move op server gameboard
					sendMessage(Protocol.SERVER_CORE_MOVE_ACCEPTED);
					//TODO get all values from Move
					server.sendMessageToGamePlayers(server.getPlayersOfHandler(this), 
									 Protocol.SERVER_CORE_MOVE_MADE + Protocol.MESSAGESEPERATOR + 
									 input[1] + Protocol.MESSAGESEPERATOR + 
									 input[2] + Protocol.MESSAGESEPERATOR + 
									 input[3] + Protocol.MESSAGESEPERATOR + 
									 input[4]);
				} else {
					// if tile is not in hand of client i.e.
					sendMessage(Protocol.SERVER_CORE_MOVE_DENIED);
				}
				// makeMove(handler, input[1]);
				break;
			case Protocol.CLIENT_CORE_DONE:	
				sendMessage(Protocol.SERVER_CORE_SCORE + Protocol.MESSAGESEPERATOR + 
							  this.getClientName() + Protocol.MESSAGESEPERATOR + 
				//TODO add getScore command from game
						      "100");
				//sendMessageToGamePlayers(SERVER_CORE_SEND_TILE) met het juiste aantal tiles in Shape en Kleur als integer
				sendMessage(Protocol.SERVER_CORE_DONE);
				break;
			case Protocol.CLIENT_CORE_SWAP:
				Tile receivedTile = stringToTile(input);
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
				break;
			default:
				System.out.println(msg);
				break;
		}
    }


	public void sendTilesToClient(ArrayList<Tile> tiles) {
		String cmd = Protocol.SERVER_CORE_SEND_TILE;
		String sp = Protocol.MESSAGESEPERATOR;
		String done = Protocol.SERVER_CORE_DONE;
		for (Tile tile : tiles) {
			String[] tileNumbers = tileToString(tile);
			String s = tileNumbers[0]; // shape
			String c = tileNumbers[1];	// color
			sendMessage(cmd + sp + s + sp + c);
		}
		sendMessage(done);
	}
    public StringBuilder getPlayersFromLobby() {
		StringBuilder players = new StringBuilder();
		for (int i = 0; i < server.getLobby().size(); i++) {
			players.append(Protocol.MESSAGESEPERATOR 
						  + server.getLobby().get(i).getClientName());
		}
		return players;
    }
}