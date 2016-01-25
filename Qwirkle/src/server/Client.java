package server;

import controller.*;
import model.Move;
import model.Tile;
import model.TradeMove;
import view.MultiplayerGame;

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

	
	public Client() throws IOException {
		clientName = getInput("Name:");
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

		port = Integer.parseInt(getInput("Port:"));
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
					//analyze strings
				} else {
					break;
				}
			}
		} catch (IOException e) {
			shutdown();
		}
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

	//TODO client received played moves, place them on board
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
			if(placeTiles(moves)) {
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