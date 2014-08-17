/*
  AnalyzerTest1 - The first test for the board analyzer.

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
import de.java_chess.javaChess.board.Board;
import de.java_chess.javaChess.engine.IBitBoardAnalyzer;
import de.java_chess.javaChess.engine.BitBoardAnalyzerImpl;
import de.java_chess.javaChess.engine.PlyGenerator;
import de.java_chess.javaChess.engine.hashtable.PlyHashtableImpl;
import de.java_chess.javaChess.game.Game;
import de.java_chess.javaChess.game.GameImpl;
import de.java_chess.javaChess.ply.PlyImpl;
import de.java_chess.javaChess.position.PositionImpl;


/**
 * A simple test for the BitBoardAnalyzer.
 */
public class Analyzertst1 extends TestCase {

    // Instance variables
    
    /**
     * A game for the generator.
     */
    Game _game;

    /**
     * The analyzed board.
     */
    Board _board;

    /**
     * A ply generator.
     */
    PlyGenerator _plyGenerator;

    /**
     * The analyzer.
     */
    IBitBoardAnalyzer _analyzer;

    
    // Constructors

    /**
     * Create a new instance of this test.
     */
    public Analyzertst1() {
	super( "A simple analyzer test with a attack on the black queen");
    }

    
    // Methods 

    /**
     * Run the actual test(s).
     */
    public void runTest() {
	testanalyzer1();
    }
    
    /**
     * Prepare the test(s).
     */
    protected void setUp() {

	// Create a new game.
	_game = new GameImpl();

	// Create a new board.
	_board = new BitBoardImpl();

	// Create the ply generator.
	_plyGenerator = new PlyGenerator( _game, new PlyHashtableImpl( 1000));

	// And the analyzer.
	_analyzer = new BitBoardAnalyzerImpl( _game, _plyGenerator);

	// Set the pieces on their initial positions.
	_board.initialPosition();

	// Move the black knight to f6
	_board.doPly( new PlyImpl( new PositionImpl( 62), new PositionImpl( 62 - 17), false));

	// Move a white pawn to the 6th row
	_board.doPly( new PlyImpl( new PositionImpl( 11), new PositionImpl( 11 + 32), false));
    }
	
    /**
     * Run the actual test.
     */
    public void testanalyzer1() {

	// Do the 1st analysis
	short score1 = _analyzer.analyze( (BitBoard)_board, true);

	// Move the pawn and attack a black pawn
	_board.doPly( new PlyImpl( new PositionImpl( 43), new PositionImpl( 52), true));

	// Analyze the board after the ply
	short score2 = _analyzer.analyze( (BitBoard)_board, false);

	// Check if the 2nd board is better for white.
	assertTrue( "Analyzer did not recognize attack on black piece", score2 > score1);

	// Now attack the black queen.
	_board.doPly( new PlyImpl( new PositionImpl( 52), new PositionImpl( 59), true));

	// Analyze the board again
	short score3 = _analyzer.analyze( (BitBoard)_board, false);

	assertTrue( "Analyzer does not recognize attack on black queen", score3 > score2);
    }
}



