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
import de.java_chess.javaChess.bitboard.BitBoard;
import de.java_chess.javaChess.bitboard.BitBoardImpl;
import de.java_chess.javaChess.board.Board;
import de.java_chess.javaChess.engine.IBitBoardAnalyzer;
import de.java_chess.javaChess.engine.BitBoardAnalyzerImpl;
import de.java_chess.javaChess.engine.PlyGenerator;
import de.java_chess.javaChess.engine.hashtable.PlyHashtableImpl;
import de.java_chess.javaChess.game.Game;
import de.java_chess.javaChess.game.GameImpl;
import de.java_chess.javaChess.ply.CastlingPlyImpl;
import de.java_chess.javaChess.ply.Ply;
import de.java_chess.javaChess.ply.PlyImpl;
import de.java_chess.javaChess.position.PositionImpl;


/**
 * A simple test for the ply generator, if it generates
 * a bishop move f1-c4.
 */
public class PlyGeneratorTest8 extends TestCase {

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

    /**
     * The moves before the problem occures.
     */
    String [] [] _prevMoves = {
    		{"d2","d4"}, {"e7","e6"},
    		{"e2","e4"}, {"f8","b4"},
    		{"c2","c3"}, {"b4","f8"},
    		{"f2","f3"}, {"d8","h4"},
    		{"g2","g3"}, {"h4","d8"},
    		{"f1","g2"}, {"c7","c6"},
    		{"f3","f4"}, {"h7","h5"},
    		{"g1","f3"}, {"d7","d5"},
    		{"e4","e5"}, {"f7","f6"},
    		{"e1","g1"}, {"b7","b5"},
    		{"d1","d3"}, {"c8","b7"},
    		{"d3","g6"}, {"e8","e7"},
    		{"f1","e1"}, {"a7","a5"},
    		{"f4","f5"}, {"d8","b6"},
    		{"e5","f6"}, {"g7","f6"},
    		{"e1","e6"}, {"e7","d8"},
    		{"g6","e8"}, {"d8","c7"},
    		{"e8","f7"}, {"g8","e7"},
    		{"f7","f6"}, {"h8","g8"},
    		{"e6","e7"}, {"f8","e7"},
    		{"f6","e7"}, {"b8","d7"},
    		{"c1","f4"}, {"c7","c8"},
    		{"e7","f7"}, {"g8","f8"},
    		{"f7","h5"}, {"d7","f6"},
    		{"h5","h6"}, {"f6","d7"},
    		{"b1","d2"}, {"f8","e8"},
    		{"f5","f6"}, {"d7","f8"},
    		{"f6","f7"}, {"e8","d8"},
    		{"f4","g5"}, {"c8","b8"},
    		{"g5","d8"}, {"b6","d8"},
    		{"a1","e1"}, {"a5","a4"},
    		{"e1","e8"}, {"d8","e8"},
    		{"f7","e8"}, {"b8","a7"},
    		{"e8","a8"}, {"b7","a8"},
    		{"h6","f8"}, {"a8","b7"},
    		{"f8","c5"}, {"a7","b8"},
    		{"f3","e5"}, {"b8","a8"},
    		{"d2","f3"}, {"a8","b8"},
    		{"e5","d7"}, {"b8","a8"},
    		{"f3","e5"}
    		};

    
    // Constructors

    /**
     * Create a new instance of this test.
     */
    public PlyGeneratorTest8() {
	super( "König zieht mit a8-a7 ins Schach");
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

	// Perform the moves to get to the tested board position.
	for( int i=0; i < _prevMoves.length; i++) {
		// castling
		if( i == 18 ){
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
	Ply [] plies = _plyGenerator.getPliesForColor( false);

	// Check potential plys.
	boolean containsMove = false;

	for( int i = 0; i < plies.length; i++) {
		System.out.println(i + " " + plies[i].toString());
		if (plies[i].toString().contentEquals("a8-a7"))
			{
			 containsMove = true;			
			}; 
	}
	System.out.println("-----");

	assertFalse( "König zieht mit a8-a7 ins Schach", containsMove);
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
