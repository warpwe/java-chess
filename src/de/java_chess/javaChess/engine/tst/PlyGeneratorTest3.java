/*
  PlyGeneratorTest3 - A test for illegal queen moves.

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
import de.java_chess.javaChess.ply.Ply;
import de.java_chess.javaChess.ply.PlyImpl;
import de.java_chess.javaChess.position.PositionImpl;


/**
 * A simple test for the ply generator, if it generates
 * a queen move h4-a5.
 */
public class PlyGeneratorTest3 extends TestCase {

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
    public PlyGeneratorTest3() {
	super( "A simple ply generator test for a illegal queen move h4-a5");
    }

    
    // Methods

    /**
     * Run the actual test(s).
     */
    public void runTest() {
	testgenerator();
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
	_plyGenerator = new PlyGenerator( _game, (BitBoard)_board, new PlyHashtableImpl( 100));

	// And the analyzer.
	_analyzer = new BitBoardAnalyzerImpl( _game, _plyGenerator);

	_plyGenerator.setAnalyzer( _analyzer);

	// Set the pieces on their initial positions.
	_board.initialPosition();

	// Move the white pawn from e2 - e4
	doPly( new PlyImpl( new PositionImpl( 12), new PositionImpl( 12 + 16), false));

	// Move the black pawn from e7 - e5
	doPly( new PlyImpl( new PositionImpl( 52), new PositionImpl( 36), false));

	// Move f2 - f3
	doPly( new PlyImpl( new PositionImpl( 13), new PositionImpl( 13 + 8), false));

	// Move the black queen to h4
	doPly( new PlyImpl( new PositionImpl( 59), new PositionImpl( 31), false));

	// Move g2-g3
	doPly( new PlyImpl( new PositionImpl( 14), new PositionImpl( 14 + 8), false));
    }

    /**
     * Run the actual test.
     */
    public void testgenerator() {

	// Get the plies for black
	Ply [] plies = _plyGenerator.getPliesForColor( false);

	// Check if h4-a5 was delivered as a potential ply.
	boolean containsIllegalMove = false;

	for( int i = 0; i < plies.length; i++) {
	    if( plies[i].getSource().getSquareIndex() == 31
		&& plies[i].getDestination().getSquareIndex() == 32) {
		containsIllegalMove = true;
		break;
	    }
	}

	assertTrue( "PlyGenerator does not computes move h4-a5", !containsIllegalMove);
    }

    /**
     * Perform a ply.
     *
     * @param ply The ply to perform.
     */
    private void doPly( Ply ply) {
	_game.doPly( ply);
	_board.doPly( ply);
    }
}
