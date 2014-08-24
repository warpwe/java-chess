/*
 * KingInKnightCheckTest - Tests all cases, when a knight checks a king. Copyright (C) 2003 The
 * Java-Chess team <info@java-chess.de> This program is free software; you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option) any later version. This
 * program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details. You should have received a copy of the GNU General Public
 * License along with this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 */

package com.github.warpwe.javachess.test.engine;

import com.github.warpwe.javachess.bitboard.BitBoardImpl;
import com.github.warpwe.javachess.bitboard.IBitBoard;
import com.github.warpwe.javachess.board.Board;
import com.github.warpwe.javachess.engine.BitBoardAnalyzerImpl;
import com.github.warpwe.javachess.engine.IBitBoardAnalyzer;
import com.github.warpwe.javachess.engine.PlyGenerator;
import com.github.warpwe.javachess.engine.hashtable.PlyHashtableImpl;
import com.github.warpwe.javachess.game.GameImpl;
import com.github.warpwe.javachess.game.IGame;
import com.github.warpwe.javachess.piece.IPiece;
import com.github.warpwe.javachess.piece.PieceImpl;
import com.github.warpwe.javachess.position.Position;
import com.github.warpwe.javachess.position.PositionImpl;

import junit.framework.TestCase;

/**
 * Test all cases, when a knight checks a king.
 */
public class KingInKnightCheckTest extends TestCase {

  // Instance variables

  /**
   * The positions of the 2 kings.
   */
  int whiteKingPos, blackKingPos;

  /**
   * A game for the generator.
   */
  IGame game;

  /**
   * A board for the pieces.
   */
  Board board;

  /**
   * The analyzer needs ply generator
   */
  PlyGenerator plyGenerator;

  /**
   * And a analyzer.
   */
  IBitBoardAnalyzer analyzer;

  // Constructors
  /**
   * Create a new instance of this test.
   */
  public KingInKnightCheckTest() {
    super("King is in knight check test");
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
    whiteKingPos = 4;
    blackKingPos = 60;

    // Create a new game.
    game = new GameImpl();

    // Create a new board.
    board = new BitBoardImpl();

    // Create the ply generator (the hashtable is not used here).
    plyGenerator = new PlyGenerator(game, new PlyHashtableImpl(100));

    // And the analyzer.
    analyzer = new BitBoardAnalyzerImpl(game, plyGenerator);

    // Set the 2 kings on the board
    board.setPiece(new PieceImpl(IPiece.KING, IPiece.BLACK), new PositionImpl(blackKingPos));
    board.setPiece(new PieceImpl(IPiece.KING, IPiece.WHITE), new PositionImpl(whiteKingPos));
  }

  /**
   * Run the actual knight tests.
   */
  public void testsknight() {

    // Now set the black knight on every other square to see if the analyzer
    // reports a check.
    for (int i = 63; i >= 0; i--) {

      Position currentPosition = new PositionImpl(i);

      if (null == board.getPiece(currentPosition)) {  // If this square is empty

        // Set a black knight
        board.setPiece(new PieceImpl(IPiece.KNIGHT, IPiece.BLACK), currentPosition);

        int squareIndexDifference = Math.abs(i - whiteKingPos);

        /*
         * The white king is in check, if the square difference is 6, 10, 15 or 17 (count the
         * squares on a board to verify this).
         */
        boolean whiteKingIsInCheck = ((squareIndexDifference == 6) || (squareIndexDifference == 10)
            || (squareIndexDifference == 15) || (squareIndexDifference == 17));

        // Only if the white king is in check, the analyzer should return BLACK_WIN.
        assertTrue("Wrong check status with knight on square " + i,
            whiteKingIsInCheck == analyzer.isInCheck((IBitBoard) board, true));

        // Remove the black knight from the current square.
        board.setPiece(null, currentPosition);
      }
    }
  }
}
