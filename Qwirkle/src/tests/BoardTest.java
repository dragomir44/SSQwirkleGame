package tests;

import model.*;
import controller.*;
import view.*;

import java.util.*;

import org.junit.Before;
import static org.junit.Assert.*;

import org.junit.Test;

public class BoardTest {
	private Board board;
	private Tile tile;
	private Tile tile1;
	private Tile tile2;
	private Tile tile3;
	private Tile tile4;
	private Tile tile5;
	private Tile tile6;
	private Tile tile7;
	private Tile tile8;
	private Tile tile9;
	
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
		tile8 = new Tile(Tile.Shape.ƒ, Tile.Colour.R);
		tile9 = new Tile(Tile.Shape.Ø, Tile.Colour.Y);
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
	public void testDimensions() {
		ArrayList<Move> moves = new ArrayList<Move>();
		
		// 0 on row
		Move move1 = new Move(0, 1, tile);
		moves.add(move1);
		assertFalse(board.setField(moves));
		
		// 0 on col
		Move move2 = new Move(1, 0, tile2);
		moves.clear();
		moves.add(move2);
		assertFalse(board.setField(moves));
		
		// 16 on row
		Move move3 = new Move(16, 1, tile3);
		moves.clear();
		moves.add(move3);
		assertFalse(board.setField(moves));
		
		// 16 on col
		Move move4 = new Move(1, 16, tile4);
		moves.clear();
		moves.add(move4);
		assertFalse(board.setField(moves));
		
		// -1 on col
		Move move5 = new Move(1, -1, tile5);
		moves.clear();
		moves.add(move5);
		assertFalse(board.setField(moves));
		
		// -1 on row
		Move move6 = new Move(-1, 1, tile6);
		moves.clear();
		moves.add(move6);
		assertFalse(board.setField(moves));
		
		// Same row and col
		Move move7 = new Move(5, 9, tile8);
		Move move8 = new Move(5, 9, tile9);
		moves.clear();
		moves.add(move7);
		moves.add(move8);
		assertFalse(board.setField(moves));
	
	}
	
	@Test
	public void testLinesSix() {
		ArrayList<Move> moves = new ArrayList<Move>();
		Move move1 = new Move(1, 1, new Tile(Tile.Shape.X, Tile.Colour.R));
		Move move2 = new Move(2, 1, new Tile(Tile.Shape.X, Tile.Colour.B));
		Move move3 = new Move(3, 1, new Tile(Tile.Shape.X, Tile.Colour.O));
		Move move4 = new Move(4, 1, new Tile(Tile.Shape.X, Tile.Colour.P));
		Move move5 = new Move(5, 1, new Tile(Tile.Shape.X, Tile.Colour.G));
		Move move6 = new Move(6, 1, new Tile(Tile.Shape.X, Tile.Colour.Y));
		moves.add(move1);
		moves.add(move2);
		moves.add(move3);
		moves.add(move4);
		moves.add(move5);
		moves.add(move6);
		assertTrue(board.setField(moves));
		
		// Points qwirkle test
	
		System.out.println(board.toString());
		System.out.println(board.getPoints(moves));
		assertEquals(21, board.getPoints(moves));
		
		// More than 6
		ArrayList<Move> moves2 = new ArrayList<Move>();
		Move move7 = new Move(1, 7, tile7);
		moves2.add(move7);
		assertFalse(board.setField(moves2));
		
		//No adjecent tiles
		ArrayList<Move> moves3 = new ArrayList<Move>();
		Move move8 = new Move(2, 9, tile8);
		moves3.add(move8);
		assertFalse(board.setField(moves3));
		

	}
	
	@Test
	public void testPoints() {
		Move move1 = new Move(4, 5, tile);
		Move move2 = new Move(4, 6, tile1);
		Move move3 = new Move(4, 7, tile3);
		Move move4 = new Move(4, 8, tile2);
		Move move5 = new Move(5, 8, tile6);
		
		ArrayList<Move> moves = new ArrayList<Move>();
		moves.add(move1);
		board.setField(moves);
		assertEquals(1, board.getPoints(moves));
		
		moves.clear();
		moves.add(move2);
		moves.add(move3);
		board.setField(moves);
		assertEquals(3, board.getPoints(moves));
		
		moves.clear();
		moves.add(move4);
		moves.add(move5);
		assertTrue(board.setField(moves));
		assertEquals(6, board.getPoints(moves));
		
		
	}
	
	public void allTest() {
		ArrayList<Move> moves = new ArrayList<Move>();
		Move move1 = new Move(4, 5, tile);
		moves.add(move1);
		
		board.getErrors();
		board.isEmpty();
		board.toString();
	}

}
