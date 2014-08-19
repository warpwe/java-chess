/*
  PlyGeneratorTest7 - A test for a valid bishop move.

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
import de.java_chess.javaChess.ply.CastlingPlyImpl;
import de.java_chess.javaChess.ply.IPly;
import de.java_chess.javaChess.ply.PlyImpl;
import de.java_chess.javaChess.position.PositionImpl;


/**
 * A simple test for the ply generator, if it generates
 * a bishop move f1-c4.
 */
public class PlyGeneratorTest10 extends TestCase {

    // Instance variables

    /**
     * A game for the generator.
     */
    IGame _game;

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

    /**
     * The moves before the problem occures.
     */
    String [] [] _prevMoves = {
    		{"d2","d4"}, {"e7","e6"},
    		{"e2","e4"}, {"f8","b4"},
    		{"c2","c3"}, {"b4","f8"},
    		{"f2","f4"}, {"d8","h4"},
    		{"g2","g3"}, {"h4","d8"},
    		{"g1","f3"}, {"c7","c6"},
    		{"f1","g2"}, {"d7","d5"},
    		{"e4","e5"}, {"b7","b5"},
    		{"e1","g1"}, {"f7","f6"},
    		{"b2","b4"}, {"c8","b7"},
    		{"a2","a4"}, {"f6","e5"},
    		{"f4","e5"}, {"f8","e7"},
    		{"a4","b5"}, {"c6","b5"},
    		{"b1","d2"}, {"h7","h5"},
    		{"h2","h4"}, {"a7","a5"},
    		{"d2","b3"}, {"b8","c6"},
    		{"f3","g5"}, {"d8","c8"},
    		{"d1","f3"}, {"c6","d8"},
    		{"a1","a5"}, {"a8","a5"},
    		{"b3","a5"}, {"g7","g6"},
    		{"a5","b7"}, {"c8","b7"},
    		{"g5","e6"}, {"d8","e6"},
    		{"f3","f7"}, {"e8","d8"},
    		{"f7","e6"}, {"b7","a7"},
    		{"e6","d5"}, {"a7","d7"},
    		{"d5","d7"}, {"d8","d7"},
    		{"f1","f7"}, {"d7","c7"},
    		{"c1","g5"}, {"c7","d7"},
    		{"f7","g7"}, {"d7","e8"},
    		{"g2","c6"}, {"e8","f8"},
    		{"g7","g6"}, {"h8","h7"},
    		{"c6","d5"}, {"e7","g5"},
    		{"g6","g8"}, {"f8","e7"},
    		{"g8","g5"}, {"e7","d8"},
    		{"g5","f5"}, {"h7","e7"},
    		{"f5","h5"}, {"d8","c7"},
    		{"h5","f5"}, {"e7","g7"},
    		{"f5","f7"}, {"g7","f7"},
    		{"d5","f7"}, {"c7","d8"},
    		{"g3","g4"}, {"d8","e7"},
    		{"f7","d5"}, {"e7","d7"},
    		{"g1","f2"}, {"d7","c7"},
    		{"f2","f3"}, {"c7","b6"},
    		{"f3","f4"}, {"b6","c7"},
    		{"f4","f5"}, {"c7","b6"},
    		{"f5","e6"}, {"b6","c7"},
    		{"e6","e7"}, {"c7","b6"},
    		{"e7","d7"}
    		};

    
    // Constructors

    /**
     * Create a new instance of this test.
     */
    public PlyGeneratorTest10() {
	super( "K�nig zieht mit b6-a5 ins Schach");
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
	_plyGenerator = new PlyGenerator( _game, (IBitBoard)_board, new PlyHashtableImpl( 100));

	// And the analyzer.
	_analyzer = new BitBoardAnalyzerImpl( _game, _plyGenerator);

	_plyGenerator.setAnalyzer( _analyzer);

	// Set the pieces on their initial positions.
	_board.initialPosition();

	// Perform the moves to get to the tested board position.
	for( int i=0; i < _prevMoves.length; i++) {
		// castling
		if( i == 16 ){
			    doPly( new CastlingPlyImpl( new PositionImpl( _prevMoves[i][0]), false));
			    continue;
		}

		doPly( new PlyImpl( new PositionImpl( _prevMoves[i][0]), new PositionImpl( _prevMoves[i][1]), false));
	}
    }

    /**
     * Run the actual test.
     */
    public void testgenerator() {

	// Get the plies for black
	IPly [] plies = _plyGenerator.getPliesForColor( false);

	// Check potential plys.
	boolean containsMove = false;

	for( int i = 0; i < plies.length; i++) {
		System.out.println(i + " " + plies[i].toString());
		if (plies[i].toString().contentEquals("b6-a5"))
			{
			 containsMove = true;			
			}; 
	}
	System.out.println("-----");

	assertFalse( "K�nig zieht mit b6-a5 ins Schach", containsMove);
    }

    /**
     * Perform a ply.
     *
     * @param ply The ply to perform.
     */
    private void doPly( IPly ply) {
	_game.doPly( ply);
	_board.doPly( ply);
    }
}
