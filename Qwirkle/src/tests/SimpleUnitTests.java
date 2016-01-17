
package tests;

import model.*;
import controller.*;
import view.*;

import java.util.*;

import org.junit.Before;
import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleUnitTests {
	private Board board;
	private Tile tile;
	private Tile tile1;
	private Tile tile2;
	private Tile tile3;
	private Tile tile4;
	private Tile tile5;
	private Tile tile6;
	private Tile tile7;
	
	@Before
	public void setUp() {
		board = new Board();
		tile = new Tile(Tile.Shape.X, Tile.Colour.R);
		tile1 = new Tile(Tile.Shape.X, Tile.Colour.B);
		tile2 = new Tile(Tile.Shape.X, Tile.Colour.G);
		tile3 = new Tile(Tile.Shape.X, Tile.Colour.P);
		tile4 = new Tile(Tile.Shape.O, Tile.Colour.O);
		tile5 = new Tile(Tile.Shape.Ø, Tile.Colour.R);
		tile6 = new Tile(Tile.Shape.¥, Tile.Colour.G);
		tile7 = new Tile(Tile.Shape.ƒ, Tile.Colour.G);
		tile7 = new Tile(Tile.Shape.ƒ, Tile.Colour.R);
	}

	@Test
	public void testValidMove() {
		ArrayList<Move> moves = new ArrayList<Move>();
		
		Move move1 = new Move(4, 5, tile);
		moves.add(move1);
		assertTrue(board.isValidMove(moves));
		
		Move move2 = new Move(4, 6, tile1);
		Move move3 = new Move(4, 7, tile2);
		moves.add(move2);
		moves.add(move3);
		assertTrue(board.isValidMove(moves));
		
		assertTrue(board.setField(moves));
		// double color
		Move move4 = new Move(4, 8, tile);
		moves.clear();
		moves.add(move4);
		assertFalse(board.setField(moves));
		
		// same color as neighbour but wrong line shape
		Move move5 = new Move(4, 8, tile7);
		moves.clear();
		moves.add(move5);
		assertFalse(board.setField(moves));
		
	}
	
	@Test
	public void testPoints() {
		Move move1 = new Move(4, 5, tile);
		Move move2 = new Move(4, 6, tile1);
		Move move3 = new Move(4, 7, tile2);
		Move move4 = new Move(4, 8, tile3);
		Move move5 = new Move(5, 8, tile4);
		
		ArrayList<Move> moves = new ArrayList<Move>();
		moves.add(move1);
		moves.add(move2);
		moves.add(move3);
		moves.add(move4);
		
		board.setField(moves);
		assertEquals(4, board.getPoints(moves));
//		
//		moves.clear();
//		moves.add(move2);
//		moves.add(move3);
//		board.setField(moves);
//		assertEquals(3, board.getPoints(moves));
//		
//		moves.clear();
//		moves.add(move4);
//		moves.add(move5);
//		assertTrue(board.setField(moves));
//		assertEquals(6, board.getPoints(moves));
	}

}
