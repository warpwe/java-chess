/*
  ZobristKeyImpl - Implements Zobrist keys for plies.

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

package de.java_chess.javaChess.engine.hashtable;

import java.util.Random;

import de.java_chess.javaChess.bitboard.IBitBoard;
import de.java_chess.javaChess.piece.IPiece;
import de.java_chess.javaChess.position.Position;
import de.java_chess.javaChess.position.PositionImpl;


/**
 * This class implements the computation of a Zobrist key.
 */
public class ZobristKeyImpl {

    // Static variables

    /**
     * The only instance of this class (singleton pattern).
     */
    private static ZobristKeyImpl _instance = null;

    /**
     * The random numbers for the key computation.
     */
    private long [] [] [] _factors;

    /**
     * The factor for black moves.
     */
    private long _blackMoves;


    // Instance variables

    
    // Constructors

    /**
     * Create a new key instance.
     */
    private ZobristKeyImpl() {

	// Create a new multidimensional array.
	_factors = new long[2][6][64];

	Random rand = new Random();

	for( int i = 0; i < 64; i++) {
	    for( int j = 0; j < 6; j++) {
		_factors[0][j][i] = rand.nextLong();
		_factors[1][j][i] = rand.nextLong();
	    }
	}

	_blackMoves = rand.nextLong();
    }

    // Methods

    /**
     * Get the only instance of this class.
     */
    public static final ZobristKeyImpl getInstance() {
	if( _instance == null) {
	    _instance = new ZobristKeyImpl();
	}
	return _instance;
    }

    /**
     * Compute a key for a board and a color.
     *
     * @param board The current board.
     * @param white Flag to indicate if white moves.
     *
     * @return The zobrist key.
     */
    public long computeKey( IBitBoard board, boolean white) {

	long val = 0L;

	// Get a bitmask of the empty squares to speedup the 
	// test for a piece on the square.
	long emptySquareMask = board.getEmptySquares();

	// Reuse the same position object for each square.
	Position curPosition = new PositionImpl( 0);

	for( int square = 0; square < 64; square++) {
	    if( ( emptySquareMask & 1) == 0) {  // If there's a piece on the square
		curPosition.setSquareIndex( square);
		IPiece p = board.getPiece( curPosition);
		
		if( p != null) {
		    val ^= _factors[p.getColor()][p.getType() - 1][square];
		}
	    }
	    emptySquareMask >>>= 1;  // Shift the mask to test for the next square.
	}

	if( ! white) {
	    val ^= _blackMoves;
	}

	return val;
    }
}
