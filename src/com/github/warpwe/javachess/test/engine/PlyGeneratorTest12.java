/*
 * PlyGeneratorTest7 - A test for a valid bishop move. Copyright (C) 2003 The Java-Chess team
 * <info@java-chess.de> This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free Software Foundation;
 * either version 2 of the License, or (at your option) any later version. This program is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
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
import com.github.warpwe.javachess.ply.CastlingPlyImpl;
import com.github.warpwe.javachess.ply.IPly;
import com.github.warpwe.javachess.ply.PlyImpl;
import com.github.warpwe.javachess.position.PositionImpl;

import junit.framework.TestCase;

/**
 * A simple test for the ply generator, if it generates a bishop move f1-c4.
 */
public class PlyGeneratorTest12 extends TestCase {

  // Instance variables

  /**
   * A game for the generator.
   */
  IGame game;

  /**
   * The analyzed board.
   */
  Board board;

  /**
   * A ply generator.
   */
  PlyGenerator plyGenerator;

  /**
   * The analyzer.
   */
  IBitBoardAnalyzer analyzer;

  /**
   * The moves before the problem occures.
   */
  String[][] previousMoves = {
      {
          "f2", "f4"
      }, {
          "d7", "d5"
      }, {
          "c2", "c4"
      }, {
          "d5", "c4"
      }, {
          "d1", "a4"
      }, {
          "c7", "c6"
      }, {
          "a4", "c4"
      }, {
          "b7", "b5"
      }, {
          "c4", "c2"
      }, {
          "d8", "d4"
      }, {
          "e2", "e3"
      }, {
          "d4", "b4"
      }, {
          "g1", "f3"
      }, {
          "f7", "f5"
      }, {
          "f1", "e2"
      }, {
          "e7", "e5"
      }, {
          "a2", "a3"
      }, {
          "b4", "d6"
      }, {
          "f3", "e5"
      }, {
          "g7", "g6"
      }, {
          "e1", "g1"
      }, {
          "d6", "d5"
      }, {
          "e2", "f3"
      }, {
          "d5", "d6"
      }, {
          "e5", "c6"
      }, {
          "b8", "c6"
      }, {
          "c2", "c6"
      }, {
          "d6", "c6"
      }, {
          "f3", "c6"
      }, {
          "c8", "d7"
      }, {
          "c6", "a8"
      }, {
          "f8", "c5"
      }, {
          "d2", "d4"
      }, {
          "c5", "b6"
      }, {
          "a8", "f3"
      }, {
          "h7", "h5"
      }, {
          "b1", "c3"
      }, {
          "a7", "a5"
      }, {
          "f3", "e2"
      }, {
          "d7", "c6"
      }, {
          "c3", "b5"
      }, {
          "e8", "d7"
      }, {
          "c1", "d2"
      }, {
          "g8", "f6"
      }, {
          "b5", "c3"
      }, {
          "h8", "e8"
      }, {
          "a1", "c1"
      }, {
          "e8", "e6"
      }, {
          "f1", "d1"
      }, {
          "a5", "a4"
      }, {
          "g1", "f2"
      }, {
          "b6", "d4"
      }, {
          "e3", "d4"
      }, {
          "h5", "h4"
      }, {
          "h2", "h3"
      }, {
          "d7", "d8"
      }, {
          "d2", "e3"
      }, {
          "g6", "g5"
      }, {
          "d4", "d5"
      }, {
          "c6", "d5"
      }, {
          "c3", "d5"
      }, {
          "f6", "d7"
      }, {
          "f4", "g5"
      }, {
          "e6", "d6"
      }, {
          "d5", "f6"
      }, {
          "d6", "d1"
      }, {
          "c1", "d1"
      }, {
          "d8", "c8"
      }, {
          "d1", "d7"
      }, {
          "c8", "b8"
      }, {
          "e3", "f4"
      }, {
          "b8", "a8"
      }, {
          "e2", "f3"
      }
  };

  // Constructors

  /**
   * Create a new instance of this test.
   */
  public PlyGeneratorTest12() {
    super("Matt wird nicht erkannt, warum?");
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
    game = new GameImpl();

    // Create a new board.
    board = new BitBoardImpl();

    // Create the ply generator.
    plyGenerator = new PlyGenerator(game, (IBitBoard) board, new PlyHashtableImpl(100));

    // And the analyzer.
    analyzer = new BitBoardAnalyzerImpl(game, plyGenerator);

    plyGenerator.setAnalyzer(analyzer);

    // Set the pieces on their initial positions.
    board.initialPosition();

    // Perform the moves to get to the tested board position.
    for (int i = 0; i < previousMoves.length; i++) {
      // castling
      if (i == 20) {
        doPly(new CastlingPlyImpl(new PositionImpl(previousMoves[i][0]), false));
        continue;
      }

      doPly(new PlyImpl(new PositionImpl(previousMoves[i][0]),
          new PositionImpl(previousMoves[i][1]), false));
    }
  }

  /**
   * Run the actual test.
   */
  public void testgenerator() {

    // Get the plies for black
    IPly[] plies = plyGenerator.getPliesForColor(false);

    // Check potential plys.
    boolean containsMove = false;

    for (int i = 0; i < plies.length; i++) {
      System.out.println(i + " " + plies[i].toString());
      containsMove = true;
    }
    System.out.println("-----");

    assertFalse("K�nig ist Matt", containsMove);
  }

  /**
   * Perform a ply.
   *
   * @param ply
   *          The ply to perform.
   */
  private void doPly(IPly ply) {
    game.doPly(ply);
    board.doPly(ply);
  }
}
