package server;

import controller.*;
import model.Move;
import model.Tile;
import model.TradeMove;
import model.Tile.Colour;
import model.Tile.Shape;
import view.MultiplayerGame;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

public class Client extends Thread {

	private String clientName;
	private int port;
	private InetAddress host;
	private Socket sock;
	private BufferedReader in;
	private BufferedWriter out;
	private BufferedReader playerInput;
	private boolean firstTurn;
	private Board board = new Board();
	private Player player;
	private ArrayList<String> opponents;
	private ArrayList<String> playersInServer;
	private ArrayList<Move> movesMade;

	
	public Client() throws IOException {
		//clientName = getInput("Name:");
		//port = Integer.parseInt(getInput("Port:"));
		//TEST: REMOVE AFTER TESTING
		Random rn = new Random();
		clientName = "Player" + rn.nextInt(1000);
		port = 1337;
		switch (clientName) {
			case "-N":
				player = new ComputerPlayer(new NaiveStrategy(), "Fred");
				break;
			case "-S":
				player = new ComputerPlayer(new SmartStrategy(), "Fred");
				break;
			default:
				player = new HumanPlayer(clientName);
				break;
		}


		host = InetAddress.getLocalHost();
		playerInput = new BufferedReader(new InputStreamReader(System.in));
		firstTurn = true;
		try {
			sock = new Socket(host, port);
	    	in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
	    	out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
	    	sendMessage(Protocol.CLIENT_CORE_EXTENSION);
        } catch (IOException e) {
            System.out.println("ERROR: could not create a socket on " + host
                    + " and port " + port);
		}	
	}
	
    public void run() {
		try {
			while (true) {
				String line = in.readLine();
				if (line != null) {
					System.out.println(line);
					readString(line);
				} else {
					break;
				}
			}
		} catch (IOException e) {
			shutdown();
		}
	}
    

    public synchronized void readString(String msg) throws IOException {
    	String[] input = msg.split(Protocol.MESSAGESEPERATOR);
    	String game;
    	boolean done = false;
		do {
			switch (input[0]) {
				case Protocol.SERVER_CORE_EXTENSION:
					if (clientName != null) {
						sendMessage(Protocol.CLIENT_CORE_LOGIN 
								  + Protocol.MESSAGESEPERATOR + clientName);
					} else {
						sendMessage(Protocol.CLIENT_CORE_JOIN 
								  + Protocol.MESSAGESEPERATOR + clientName);
					}
					done = true;
					break;
				case Protocol.SERVER_CORE_LOGIN_DENIED:
					System.out.println("Login denied. Try again with a different name");
					shutdown();
					done = true;
					break;
				case Protocol.SERVER_CORE_LOGIN_ACCEPTED:
					game = getInput("Type 'Y' to start a game");
					if (game.equals("Y")) {
						sendMessage(Protocol.CLIENT_CORE_START);
					}
					done = true;
					break;
				case Protocol.SERVER_CORE_JOIN_DENIED:
					System.out.println("Join denied. Try again");
					shutdown();
					done = true;
					break;
				case Protocol.SERVER_CORE_JOIN_ACCEPTED:
					clientName = input[1];
					System.out.println("Joined server as: " + input[1]);
					game = getInput("Type 'Y' to start a game");
					if (game.equals("Y")) {
						sendMessage(Protocol.CLIENT_CORE_START);
					}
					done = true;
					break;
				case Protocol.SERVER_CORE_START_DENIED:
					System.out.println("Server does not want to start");
					break;
				case Protocol.SERVER_CORE_START:
					for (int i = 1; i < input.length; i++) {
						String naam = input[i];
						opponents.add(naam);
					}
					// start een bord/game met aantal opponents
					// met aantal opponents
					done = true;
					break;
				// sendMessage(Protocol.CLIENT_CORE_PLAYERS) to ask for players in server
				case Protocol.SERVER_CORE_PLAYERS:
					playersInServer = new ArrayList<String>();
					for (int i = 1; i < input.length; i++) {
						String naam = input[i];
						playersInServer.add(naam);
					}
					done = true;
					break;
				case Protocol.SERVER_CORE_TURN:
					//bepaal current op eigen bord door turn
					String turnPlayer = input[1];
					if (turnPlayer.equals(clientName)) {
						getMove();
						//make moves
						//sendMessage(Protocol.CLIENT_CORE_MOVE + move)
						//wacht voor Protocol.SERVER_CORE_MOVE_ACCEPTED van client
						//maak meer moves
						//if (last move sent) sendMessage(Protocol.CLIENT_CORE_DONE)
						//if (Protocol.SERVER_CORE_MOVE_DENIED) 
					}
					done = true;
					break;
				case Protocol.SERVER_CORE_MOVE_MADE:
					Move move = translateStringToMove(input);
					movesMade.add(move);
					break;
				case Protocol.SERVER_CORE_DONE:
					// this is sent after tiles from bag got given to player
					
					makeMove(movesMade);
					movesMade.clear();
					done = true;
					break;
				case Protocol.SERVER_CORE_SCORE:
					addScores(input);
					break;
				case Protocol.SERVER_CORE_SEND_TILE:
					Tile receivedTile = translateTile(input);
					//give this tile to the player
					
					done = true;
					break;
				case Protocol.SERVER_CORE_SWAP_ACCEPTED:
					System.out.println("Done swapping");
					done = true;
					break;
				case Protocol.SERVER_CORE_SWAP_DENIED:
					System.out.println("Swap denied");
					
					break;
				case Protocol.SERVER_CORE_GAME_ENDED:
					addScores(input);
					System.out.println("The game has ended");
					shutdown();
					done = true;
					break;
				case Protocol.SERVER_CORE_TIMEOUT_EXCEPTION:
					System.out.println("Player <" + input[1] + "> timed out");
					shutdown();
					done = true;
					break;
				default:
					System.out.println(msg);
			}
		} while (!done);
    }
    
    public Move translateStringToMove(String[] moveInput) {
    	int x = Integer.parseInt(moveInput[1]);
    	int y = Integer.parseInt(moveInput[2]);
    	int shape = Integer.parseInt(moveInput[3]);
    	int colour = Integer.parseInt(moveInput[4]);
    	Tile moveTile = new Tile(shape, colour);
    	Move move = new Move(y, x, moveTile);
    	return move;
    }
    
    public String translateMoveToString(Move move) {
    	String x = Integer.toString(move.col);
    	String y = Integer.toString(move.row);
    	String s = null;
		String c = null;
		int count1 = 1;
		for (Shape shape : Shape.values()) {
			if (move.tile.getShape().equals(shape)) {
				s = Integer.toString(count1);
				break;
			}
			count1++;
		}
		int count2 = 1;
		for (Colour colour : Colour.values()) {
			if (move.tile.getColour().equals(colour)) {
				c = Integer.toString(count2);
				break;
			}
			count2++;
		}
		String stringMove = x + " " + y + " " + s + " " + c; 
		return stringMove;
    }
    
    public Tile translateTile(String[] tileInput) {
    	Tile tile = new Tile(Integer.parseInt(tileInput[1]), Integer.parseInt(tileInput[2]));
    	return tile;
    }
    
    public void addScores(String[] scoreInput) {
		for (int i = 1; i < scoreInput.length; i = i + 2) {
			String name = scoreInput[i];
			int score = Integer.parseInt(scoreInput[i + 1]);
			// add name + score pair to something
		}
    	//return something;
    }
    
    
	public String getInput(String question) throws IOException {
	    String input = null;
        System.out.println(question);
        input = new BufferedReader(new InputStreamReader(System.in)).readLine().trim();
        return input;
    }
	
	public void sendMessage(String message) {
		try {
			out.write(message);
			out.newLine();
			out.flush();
		} catch (IOException e) {
			shutdown();
		}
    }

	public void shutdown() {
		System.out.println("CLOSING SOCKET");
		try {
			sock.close();
			in.close();
			out.close();
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	public void makeMove(ArrayList<Move> moves) {
		board.setField(moves);
	}

	//TODO tradeTiles
	public void receiveTiles(Set<Integer> tilenrs) {
//		player.getHand().addTiles();
	}

	//TODO client's turn to place tiles
	public void getMove() {
		ArrayList<Move> moves = player.determineMove(board);
		if (moves.get(0) instanceof TradeMove) { // decided to trade tiles
			Set<Integer> tileNrs = ((TradeMove) moves.get(0)).tileNrs;
			//TODO send trade tiles to server
			ArrayList<Tile> tradeTiles = new ArrayList<Tile>();
			for (int nr : tileNrs) {
				tradeTiles.add(player.getHand().getTiles().get(nr));
			}
			if (tradeTiles(tradeTiles)) { // trading tiles was succesfull
				player.getHand().removeTiles(tileNrs);
			}
		} else {
			//TODO send tiles to place to server
			if (placeTiles(moves)) {
				//succesfully placed tiles
				for (Move move : moves) {
					player.getHand().removeTile(move.tile);
				}
			}
		}
	}

	//TODO method that sends tiles for trading
	public boolean tradeTiles(ArrayList<Tile> tiles) {

		return false;
	}

	//TODO method that sends tiles to be placed
	public boolean placeTiles(ArrayList<Move> moves) {
		//TODO if first move, place on 0,0 etc...
		return false;
	}

	public String getClientName() {
		return clientName;
	}


    public static void main(String[] args) throws IOException {
        Client c1 = new Client();
        c1.start();

        
    }
}