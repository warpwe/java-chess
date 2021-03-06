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
public class PlyGeneratorTest9 extends TestCase {

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
          "d2", "d4"
      }, {
          "e7", "e6"
      }, {
          "e2", "e4"
      }, {
          "f8", "b4"
      }, {
          "c2", "c3"
      }, {
          "b4", "a5"
      }, {
          "f2", "f4"
      }, {
          "d8", "h4"
      }, {
          "g2", "g3"
      }, {
          "h4", "d8"
      }, {
          "g1", "f3"
      }, {
          "b8", "c6"
      }, {
          "b1", "d2"
      }, {
          "d7", "d6"
      }, {
          "d2", "b3"
      }, {
          "a5", "b6"
      }, {
          "c1", "e3"
      }, {
          "f7", "f6"
      }, {
          "d1", "c2"
      }, {
          "a7", "a5"
      }, {
          "e1", "c1"
      }, {
          "a5", "a4"
      }, {
          "b3", "d2"
      }, {
          "g7", "g6"
      }, {
          "f1", "b5"
      }, {
          "c8", "d7"
      }, {
          "c1", "b1"
      }, {
          "c6", "d4"
      }, {
          "b5", "d7"
      }, {
          "d8", "d7"
      }, {
          "c3", "d4"
      }, {
          "d7", "b5"
      }, {
          "d1", "c1"
      }, {
          "b5", "e2"
      }, {
          "h1", "e1"
      }, {
          "e2", "g2"
      }, {
          "e1", "g1"
      }, {
          "g2", "e2"
      }, {
          "c1", "e1"
      }, {
          "e2", "b5"
      }, {
          "g1", "g2"
      }, {
          "b5", "b4"
      }, {
          "e1", "c1"
      }, {
          "c7", "c6"
      }, {
          "d2", "c4"
      }, {
          "b6", "c7"
      }, {
          "e3", "d2"
      }, {
          "b4", "b5"
      }, {
          "d2", "e3"
      }, {
          "h7", "h5"
      }, {
          "e4", "e5"
      }, {
          "f6", "f5"
      }, {
          "e5", "d6"
      }, {
          "c7", "b8"
      }, {
          "f3", "e5"
      }, {
          "h8", "h7"
      }, {
          "e5", "g6"
      }, {
          "e8", "f7"
      }, {
          "g6", "e5"
      }, {
          "f7", "e8"
      }, {
          "c4", "d2"
      }, {
          "b8", "d6"
      }, {
          "d2", "f3"
      }, {
          "d6", "e5"
      }, {
          "d4", "e5"
      }, {
          "b5", "d5"
      }, {
          "f3", "g5"
      }, {
          "h7", "g7"
      }, {
          "g2", "d2"
      }, {
          "d5", "a5"
      }, {
          "g5", "e6"
      }, {
          "g7", "g6"
      }, {
          "e6", "d4"
      }, {
          "a8", "d8"
      }, {
          "d4", "f5"
      }, {
          "d8", "d2"
      }, {
          "e3", "d2"
      }, {
          "a5", "b5"
      }, {
          "f5", "d6"
      }, {
          "g6", "d6"
      }, {
          "e5", "d6"
      }, {
          "b5", "d5"
      }, {
          "d2", "b4"
      }, {
          "d5", "f7"
      }, {
          "c1", "e1"
      }, {
          "e8", "d8"
      }, {
          "b4", "a5"
      }, {
          "d8", "c8"
      }, {
          "a5", "c7"
      }, {
          "b7", "b5"
      }, {
          "c2", "e4"
      }, {
          "c8", "b7"
      }, {
          "e4", "d4"
      }, {
          "f7", "d5"
      }, {
          "d4", "b6"
      }
  };

  // Constructors

  /**
   * Create a new instance of this test.
   */
  public PlyGeneratorTest9() {
    super("Koenig zieht mit b7-a7 ins Schach");
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
        doPly(new CastlingPlyImpl(new PositionImpl(previousMoves[i][0]), true));
        continue;
      }

      doPly(new PlyImpl(new PositionImpl(previousMoves[i][0]), new PositionImpl(previousMoves[i][1]),
          false));
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
      if (plies[i].toString().contentEquals("b7-a7")) {
        containsMove = true;
      }
      ;
    }
    System.out.println("-----");

    assertFalse("Koenig zieht mit b7-a7 ins Schach", containsMove);
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
