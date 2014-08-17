/*
  BoardConsistency1 - The first, if the board is altered unintentionally.

  Copyright (C) 2003 The Java-Chess team <info@java-chess.de>

  This program is free software; you can redistribute it and/or
  modify it under the terms of the GNU General Public License
  as published by the Free Software Foundation; either version 2
  of the License, or (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package de.java_chess.javaChess.engine.tst;

import junit.framework.TestCase;
import de.java_chess.javaChess.bitboard.BitBoard;
import de.java_chess.javaChess.bitboard.BitBoardImpl;
import de.java_chess.javaChess.engine.ChessEngine;
import de.java_chess.javaChess.engine.ChessEngineImpl;
import de.java_chess.javaChess.game.Game;
import de.java_chess.javaChess.game.GameImpl;
import de.java_chess.javaChess.ply.Ply;
import de.java_chess.javaChess.ply.PlyImpl;
import de.java_chess.javaChess.position.PositionImpl;


/**
 * A test to check, if the the boards are passed correctly to the ply generator,
 * if we search with a search depth > 1. At the time, the computer suggests
 * e7-e5 after the opening e2-e4,g8-f6,e4-e5 , which is a illegal move.
 */
public class BoardConsistency1 extends TestCase {

	// Instance variables

	/**
	 * The current game.
	 */
	Game _game;

	/**
	 * The chess board.
	 */
	BitBoard _board;

	/**
	 * The chess engine.
	 */
	ChessEngine _engine;

	// Constructors

	/**
	 * Create a new instance of this test.
	 */
	public BoardConsistency1() {
		super(
		"Engine does not alter the board unintentionally");
		}

	// Methods

	/**
	 * Run the actual test(s).
	 * @return 
	 */
	public void runTest() {
		testboardConsistency();
	}

	/**
	 * Prepare the test(s).
	 */
	protected void setUp() {

		// Create a new game.
		_game = new GameImpl();

		// Create a new board.
		_board = new BitBoardImpl();

		// Create a engine instance.
		_engine = new ChessEngineImpl(_game, null, _board, false);

		// Set the pieces on their initial positions.
		_board.initialPosition();
	}

	/**
	 * Run the actual test.
	 */
	public void testboardConsistency() {

		assertTrue("Row1 complete after initial position",
				(_board.getEmptySquares() & 0xFFL) == 0L);
        
		// System.out.println(Long.toHexString(_board.getEmptySquares()));
        
		// Move the white pawn from e2 - e4
		doPly(new PlyImpl(new PositionImpl(12), new PositionImpl(12 + 16),
				false));

		assertTrue("Row1 complete after e2 - e4",
				(_board.getEmptySquares() & 0xFFL) == 0L);
		// System.out.println(Long.toHexString(_board.getEmptySquares()));

		// Compute the best ply for this game position.
		Ply bestPly = _engine.computeBestPly();

		doPly(bestPly);

		assertTrue("Row1 complete after " + bestPly.toString(),
				(_board.getEmptySquares() & 0xFFL) == 0L);
		// System.out.println(Long.toHexString(_board.getEmptySquares()));

		// Move the white pawn from d2 - d4
		doPly(new PlyImpl(new PositionImpl(11), new PositionImpl(11 + 16),
				false));

		assertTrue("Row1 complete after d2 - d4",
				(_board.getEmptySquares() & 0xFFL) == 0L);
		// System.out.println(Long.toHexString(_board.getEmptySquares()));

		// Compute the best ply for this game position.
		bestPly = _engine.computeBestPly();
		doPly(bestPly);

		assertTrue("Row1 complete after " + bestPly.toString(),
				(_board.getEmptySquares() & 0xFFL) == 0L);
		// System.out.println(Long.toHexString(_board.getEmptySquares()));
	}

	/**
	 * Perform a ply.
	 * 
	 * @param ply
	 *            The ply to perform.
	 */
	private void doPly(final Ply ply) {
		_game.doPly(ply);
		_board.doPly(ply);
	}
}
