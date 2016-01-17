
package tests;

import model.*;
import model.Tile.Colour;
import model.Tile.Shape;
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
	private Tile tile10;
	
	@Before
	public void setUp() {
		board = new Board();
		tile = new Tile(Tile.Shape.X, Tile.Colour.R);
		tile1 = new Tile(Tile.Shape.X, Tile.Colour.B);
		tile2 = new Tile(Tile.Shape.X, Tile.Colour.G);
		tile3 = new Tile(Tile.Shape.X, Tile.Colour.P);
		tile4 = new Tile(Tile.Shape.O, Tile.Colour.O);
		tile5 = new Tile(Tile.Shape.�, Tile.Colour.R);
		tile6 = new Tile(Tile.Shape.�, Tile.Colour.G);
		tile7 = new Tile(Tile.Shape.�, Tile.Colour.G);
		tile8 = new Tile(Tile.Shape.�, Tile.Colour.R);
		tile9 = new Tile(Tile.Shape.X, Tile.Colour.G);
		tile10 = new Tile(Tile.Shape.X, Tile.Colour.R);
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
		moves.add(new Move(4, 8, new Tile(Shape.X, Colour.O)));
		moves.add(new Move(4, 9, new Tile(Shape.X, Colour.Y)));

		assertTrue(board.isValidMove(moves));
		
		assertTrue(board.setField(moves));
		// double color
		Move move4 = new Move(4, 8, tile);
		moves.clear();
		moves.add(move4);
		assertFalse(board.setField(moves));
		
		// test squares
		moves.clear();
		moves.add(new Move(5, 5, tile8));
		moves.add(new Move(5, 6, tile9));
		assertFalse(board.setField(moves));
		
		// same color as neighbour but wrong line shape
		Move move5 = new Move(4, 8, tile7);
		moves.clear();
		moves.add(move5);
		assertFalse(board.setField(moves));
		
		// same row and col 
		ArrayList<Move> moves2 = new ArrayList<Move>();
		Move move6 = new Move(4, 5, tile8);
		moves2.add(move6);
		assertFalse(board.setField(moves2));
		
		// more than 6
		moves2.clear();
		Move move7 = new Move(4, 10, tile10);
		moves2.add(move7);
		assertFalse(board.setField(moves2));
		
		// no adjecent tiles
		moves2.clear();
		Move move8 = new Move(10, 10, tile10);
		moves2.add(move8);
		assertFalse(board.setField(moves2));
		System.out.println(board.getErrors());
	}
	
	@Test
	public void testDimensions() {
		//Valid tests
		ArrayList<Move> moves1 = new ArrayList<Move>();
		
		Move move1 = new Move(0, 0, tile);
		Move move2 = new Move(0, board.rows, tile2);
		Move move3 = new Move(board.cols, 0, tile3);
		Move move4 = new Move(board.cols, board.rows, tile4);

		moves1.add(move1);
		assertTrue(board.isValidMove(moves1));
		
		moves1.clear();
		moves1.add(move2);
		assertTrue(board.isValidMove(moves1));
		
		moves1.clear();
		moves1.add(move3);
		assertTrue(board.isValidMove(moves1));
		
		moves1.clear();
		moves1.add(move4);
		assertTrue(board.isValidMove(moves1));


		//Invalid tests
		ArrayList<Move> moves2 = new ArrayList<Move>();
		
		//1 too much tests
		Move move5 = new Move(0 - 1, 0, tile);
		Move move6 = new Move(0, 0 - 1, tile);
		Move move7 = new Move(0, board.rows + 1, tile2);
		Move move8 = new Move(board.cols + 1, 0, tile3);
		Move move9 = new Move(board.cols + 1, board.rows + 1, tile4);

		moves2.add(move5);
		assertFalse(board.isValidMove(moves2));
		
		moves2.clear();
		moves2.add(move6);
		assertFalse(board.isValidMove(moves2));
		
		moves2.clear();
		moves2.add(move7);
		assertFalse(board.isValidMove(moves2));
		
		moves2.clear();
		moves2.add(move8);
		assertFalse(board.isValidMove(moves2));
		
		moves2.clear();
		moves2.add(move9);
		assertFalse(board.isValidMove(moves2));
	}
	
	
	@Test
	public void testPoints() {
		Move move1 = new Move(4, 5, tile);
		Move move2 = new Move(4, 6, tile1);
		Move move3 = new Move(4, 7, tile2);
		Move move4 = new Move(4, 8, tile3);
		
		// First move and 1 point
		ArrayList<Move> moves = new ArrayList<Move>();
		moves.add(move1);
		board.setField(moves);
		assertEquals(1, board.getPoints(moves));
		
		// 2 points
		moves.clear();
		moves.add(move2);
		board.setField(moves);
		assertEquals(2, board.getPoints(moves));
		
		// 3 points
		moves.clear();
		moves.add(move3);
		board.setField(moves);
		assertEquals(3, board.getPoints(moves));
		
		// Qwirkle
		moves.clear();
		moves.add(move4);
		moves.add(new Move(4, 9, new Tile(Shape.X, Colour.O)));
		moves.add(new Move(4, 10, new Tile(Shape.X, Colour.Y)));
		
		board.setField(moves);
		assertEquals(12, board.getPoints(moves));
	}
	


}
