package tests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import model.Tile;
import model.Tile.Colour;
import model.Tile.Shape;
import server.Protocol;

public class ServerTest {
	private ArrayList<String> lobby;
	private ArrayList<String> opponents;
	private Tile tile;
	
	@Before
	public void setUp() throws IOException {
		lobby = new ArrayList<String>();
		opponents = new ArrayList<String>();
		lobby.add("Sergey");
		lobby.add("Frank");
		lobby.add("Fred");
		lobby.add("rob");
		lobby.add("henk");
		tile = new Tile(Shape.$, Colour.G);
	}
	
	
	@Test
	public void getLobbyPlayersTest() {
		StringBuilder players = new StringBuilder();
		for (int i = 0; i < lobby.size(); i++) {
			players.append(" " + lobby.get(i));
		}
		String stringToSend = Protocol.SERVER_CORE_PLAYERS + players.toString();
		System.out.println(stringToSend);
	}
	
	@Test 
	public void stringSplitTest() {
		String players = Protocol.SERVER_CORE_PLAYERS + Protocol.MESSAGESEPERATOR
				  + "Sergey Frank Fred";
		System.out.println(players);
		String[] splitPlayers = players.split(Protocol.MESSAGESEPERATOR);
		for (int i = 1; i < splitPlayers.length; i++) {
			String naam = splitPlayers[i];
			opponents.add(naam);
		}
		System.out.println(opponents);
	}
	
	@Test 
	public void scoreTest() {
		String scores = Protocol.SERVER_CORE_SCORE + Protocol.MESSAGESEPERATOR
				  + "Sergey 102 Frank 11 Fred 5923";
		System.out.println(scores);
		String[] splitScores = scores.split(Protocol.MESSAGESEPERATOR);
		for (int i = 1; i < splitScores.length; i = i + 2) {
			String naam = splitScores[i];
			int score = Integer.parseInt(splitScores[i + 1]);
			System.out.println(naam + ": " + score);
		}
	}
	
	@Test
	public void lobbyGameTest() {
		ArrayList<String> players = new ArrayList<String>();
		ArrayList<String> lobbys = new ArrayList<String>();
		lobbys.add("Henk");
		lobbys.add("Sergey");
		players.add(lobbys.remove(0));
		assertEquals(players.get(0), "Henk");
	}
	
	@Test
	public void tileTranslateTest() throws IOException {
		String c = null;
		for (Colour colour : Colour.values()) {
			if (tile.getColour().equals(colour)) {
				c = colour.toString();
				break;
			}
		}
		assertEquals(c, tile.getColour().toString());
	}
	
	
	
}
