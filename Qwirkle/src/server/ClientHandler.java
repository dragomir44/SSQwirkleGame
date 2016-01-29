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
	private MultiplayerGame game;
    public boolean rematch;
	private ArrayList<Move> clientMoveBuffer = new ArrayList<>();
	private boolean moveDone = false;
	protected String name;

    public ClientHandler(Server serverArg, Socket sock) throws IOException {
        in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
        server = serverArg;
        this.sock = sock;
    }

	public void setGame(MultiplayerGame game) {
		this.game = game;
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
		String clientName = super.getClientName();
    	if (clientName == null) {
    		clientName = String.valueOf(sock.getInetAddress());
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
				System.out.println("Received input: " + input);
				if (input.length == 5) {
					Move receivedMove = stringToMove(input);
					System.out.println("Handler received move: " + receivedMove.toString());
					clientMoveBuffer.add(receivedMove);
					// check if buffer is valid
					System.out.println("The board: " + game.getBoard().toString());
					if (game.getBoard().isValidMove(clientMoveBuffer)) {
						//plaats move op server gameboard
						sendMessage(Protocol.SERVER_CORE_MOVE_ACCEPTED);
						game.broadcastMessage(Protocol.SERVER_CORE_MOVE_MADE + 
										Protocol.MESSAGESEPERATOR +
										input[1] + Protocol.MESSAGESEPERATOR +
										input[2] + Protocol.MESSAGESEPERATOR +
										input[3] + Protocol.MESSAGESEPERATOR +
										input[4]);
					} else {
						clientMoveBuffer.remove(receivedMove);
					}
				} else {
					sendMessage(Protocol.SERVER_CORE_MOVE_DENIED);
				}
				break;
			case Protocol.CLIENT_CORE_DONE:	
				sendMessage(Protocol.SERVER_CORE_SCORE + Protocol.MESSAGESEPERATOR + 
							  clientName + Protocol.MESSAGESEPERATOR +
				//TODO add getScore command from game
						      "100");
				moveDone = true;
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

	public synchronized boolean isMoveDone() {
		return moveDone;
	}
	public synchronized void moveIsOver() {
		moveDone = false;
	}

	public void sendTurn(String clientName) {
		sendMessage(Protocol.SERVER_CORE_TURN + Protocol.MESSAGESEPERATOR + clientName);
	}
}