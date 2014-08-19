/*
  KingInKnightCheckTest - Tests all cases, when a knight checks a king.

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
import de.java_chess.javaChess.bitboard.IBitBoard;
import de.java_chess.javaChess.bitboard.BitBoardImpl;
import de.java_chess.javaChess.board.Board;
import de.java_chess.javaChess.engine.IBitBoardAnalyzer;
import de.java_chess.javaChess.engine.BitBoardAnalyzerImpl;
import de.java_chess.javaChess.engine.PlyGenerator;
import de.java_chess.javaChess.engine.hashtable.PlyHashtableImpl;
import de.java_chess.javaChess.game.IGame;
import de.java_chess.javaChess.game.GameImpl;
import de.java_chess.javaChess.piece.IPiece;
import de.java_chess.javaChess.piece.PieceImpl;
import de.java_chess.javaChess.position.Position;
import de.java_chess.javaChess.position.PositionImpl;


/**
 * Test all cases, when a knight checks a king.
 */
public class KingInKnightCheckTest extends TestCase {

    // Instance variables
    
    /**
     * The positions of the 2 kings.
     */
    int _whiteKingPos, _blackKingPos;

    /**
     * A game for the generator.
     */
    IGame _game;

    /**
     * A board for the pieces.
     */
    Board _board;
    
    /**
     * The analyzer needs ply generator
     */
    PlyGenerator _plyGenerator;
    
    /**
     * And a analyzer.
     */
    IBitBoardAnalyzer _analyzer;
    

    // Constructors

    /**
     * Create a new instance of this test.
     */
    public KingInKnightCheckTest() {
	super( "King is in knight check test");
    }


    // Methods

    /**
     * Run the actual test(s).
     */
    public void runTest() {
    	testsknight();
    }

    /**
     * Prepare the test(s).
     */
    protected void setUp() {

	// Set the positions of the 2 kings.
	_whiteKingPos = 4;
	_blackKingPos = 60;

	// Create a new game.
	_game = new GameImpl();

	// Create a new board.
	_board = new BitBoardImpl();

	// Create the ply generator (the hashtable is not used here).
	_plyGenerator = new PlyGenerator( _game, new PlyHashtableImpl( 100));

	// And the analyzer.
	_analyzer = new BitBoardAnalyzerImpl( _game, _plyGenerator);

	// Set the 2 kings on the board
	_board.setPiece( new PieceImpl( IPiece.KING, IPiece.BLACK), new PositionImpl( _blackKingPos));
	_board.setPiece( new PieceImpl( IPiece.KING, IPiece.WHITE), new PositionImpl( _whiteKingPos));
    }

    /**
     * Run the actual knight tests.
     */
    public void testsknight() {

	// Now set the black knight on every other square to see if the analyzer
	// reports a check.
	for( int i = 63; i >= 0; i--) {

	    Position currentPosition = new PositionImpl( i);

	    if( null == _board.getPiece( currentPosition)) {  // If this square is empty

		_board.setPiece( new PieceImpl( IPiece.KNIGHT, IPiece.BLACK), currentPosition);  // Set a black knight there.

		int squareIndexDifference = Math.abs( i - _whiteKingPos);

		// The white king is in check, if the square difference is
		// 6, 10, 15 or 17 (count the squares on a board to verify this).
		boolean whiteKingIsInCheck = ( ( squareIndexDifference == 6)
					       || ( squareIndexDifference == 10)
					       || ( squareIndexDifference == 15)
					       || ( squareIndexDifference == 17));

		// Only if the white king is in check, the analyzer should return BLACK_WIN.
		assertTrue( "Wrong check status with knight on square " + i, whiteKingIsInCheck == _analyzer.isInCheck( (IBitBoard)_board, true));

		_board.setPiece( null, currentPosition);  // Remove the black knight from the current square.
	    }
	}
    }
    
}




