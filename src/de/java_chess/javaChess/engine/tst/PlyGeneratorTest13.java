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
import de.java_chess.javaChess.piece.Piece;
import de.java_chess.javaChess.ply.CastlingPlyImpl;
import de.java_chess.javaChess.ply.EnPassantPlyImpl;
import de.java_chess.javaChess.ply.Ply;
import de.java_chess.javaChess.ply.PlyImpl;
import de.java_chess.javaChess.ply.TransformationPlyImpl;
import de.java_chess.javaChess.position.PositionImpl;


/**
 * A simple test for the ply generator, if it generates
 * a bishop move f1-c4.
 */
public class PlyGeneratorTest13 extends TestCase {

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
    		{"c2","c3"}, {"b4","a5"},
    		{"b2","b4"}, {"a5","b6"},
    		{"f2","f4"}, {"d8","h4"},
    		{"g2","g3"}, {"h4","d8"},
    		{"f1","g2"}, {"g7","g5"},
    		{"d1","g4"}, {"h7","h5"},
    		{"g4","g5"}, {"b6","d4"},
    		{"g5","d8"}, {"e8","d8"},
    		{"c3","d4"}, {"b8","c6"},
    		{"g1","e2"}, {"c6","b4"},
    		{"b1","a3"}, {"f7","f6"},
    		{"e1","g1"}, {"b4","d3"},
    		{"c1","e3"}, {"c7","c6"},
    		{"f1","d1"}, {"d3","b2"},
    		{"d1","b1"}, {"b2","d3"},
    		{"b1","b3"}, {"d3","e5"},
    		{"d4","e5"}, {"f6","e5"},
    		{"a1","d1"}, {"g8","f6"},
    		{"f4","e5"}, {"f6","g4"},
    		{"e2","f4"}, {"d7","d5"},
    		{"e5","d6"}, {"e6","e5"},
    		{"f4","g6"}, {"h8","g8"},
    		{"g6","e7"}, {"g4","e3"},
    		{"b3","e3"}, {"c8","e6"},
    		{"e7","g8"}, {"e6","g8"},
    		{"g2","h3"}, {"g8","f7"},
    		{"d6","d7"}, {"b7","b5"},
    		{"e3","f3"}, {"b5","b4"},
    		{"f3","f7"}, {"b4","a3"},
    		{"f7","f8"}, {"d8","c7"},
    		{"f8","a8"}, {"c6","c5"},
    		{"d7","d8"}, {"c7","c6"},
    		{"d8","d7"}, {"c6","b6"},
    		{"a8","b8"}, {"b6","a6"},
    		{"d7","b5"}
    		};

    
    // Constructors

    /**
     * Create a new instance of this test.
     */
    public PlyGeneratorTest13() {
	super( "König ist Matt");
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
		if( i == 26 ){
			    doPly( new CastlingPlyImpl( new PositionImpl( _prevMoves[i][0]), false));
			    continue;
		}
		// enpassant
		if( i == 44 ){
			    doPly( new EnPassantPlyImpl( new PositionImpl( _prevMoves[i][0]),new PositionImpl( _prevMoves[i][1]),new PositionImpl( "d5")));
			    continue;
		}
		// transformation
		if( i == 66 ){
			    doPly( new TransformationPlyImpl( new PositionImpl( _prevMoves[i][0]),new PositionImpl( _prevMoves[i][1]), Piece.QUEEN, false));
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
		containsMove = true;			
	}
	System.out.println("-----");

	assertFalse( "König ist Matt", containsMove);
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
