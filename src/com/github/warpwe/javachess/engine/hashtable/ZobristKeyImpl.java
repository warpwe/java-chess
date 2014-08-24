/*
 * ZobristKeyImpl - Implements Zobrist keys for plies. Copyright (C) 2003 The Java-Chess team
 * <info@java-chess.de> This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free Software Foundation;
 * either version 2 of the License, or (at your option) any later version. This program is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details. You should have received a copy of the GNU General Public
 * License along with this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 */

package com.github.warpwe.javachess.engine.hashtable;

import java.util.Random;

import com.github.warpwe.javachess.bitboard.IBitBoard;
import com.github.warpwe.javachess.piece.IPiece;
import com.github.warpwe.javachess.position.Position;
import com.github.warpwe.javachess.position.PositionImpl;

/**
 * This class implements the computation of a Zobrist key.
 */
public class ZobristKeyImpl {

  // Static variables

  /**
   * The only instance of this class (singleton pattern).
   */
  private static ZobristKeyImpl instance = null;

  /**
   * The random numbers for the key computation.
   */
  private long[][][] factors;

  /**
   * The factor for black moves.
   */
  private long blackMoves;

  // Instance variables

  // Constructors

  /**
   * Create a new key instance.
   */
  private ZobristKeyImpl() {

    // Create a new multidimensional array.
    factors = new long[2][6][64];

    Random rand = new Random();

    for (int i = 0; i < 64; i++) {
      for (int j = 0; j < 6; j++) {
        factors[0][j][i] = rand.nextLong();
        factors[1][j][i] = rand.nextLong();
      }
    }

    blackMoves = rand.nextLong();
  }

  // Methods

  /**
   * Get the only instance of this class.
   */
  public static final ZobristKeyImpl getInstance() {
    if (instance == null) {
      instance = new ZobristKeyImpl();
    }
    return instance;
  }

  /**
   * Compute a key for a board and a color.
   *
   * @param board
   *          The current board.
   * @param white
   *          Flag to indicate if white moves.
   * @return The zobrist key.
   */
  public long computeKey(IBitBoard board, boolean white) {

    long val = 0L;

    // Get a bitmask of the empty squares to speedup the
    // test for a piece on the square.
    long emptySquareMask = board.getEmptySquares();

    // Reuse the same position object for each square.
    Position curPosition = new PositionImpl(0);

    for (int square = 0; square < 64; square++) {
      if ((emptySquareMask & 1) == 0) {  // If there's a piece on the square
        curPosition.setSquareIndex(square);
        IPiece p = board.getPiece(curPosition);

        if (p != null) {
          val ^= factors[p.getColor()][p.getType() - 1][square];
        }
      }
      emptySquareMask >>>= 1;  // Shift the mask to test for the next square.
    }

    if (!white) {
      val ^= blackMoves;
    }

    return val;
  }
}
