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
public class PlyGeneratorTest11 extends TestCase {

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
          "b4", "f8"
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
          "c7", "c6"
      }, {
          "f1", "g2"
      }, {
          "d7", "d5"
      }, {
          "e4", "e5"
      }, {
          "b7", "b5"
      }, {
          "e1", "g1"
      }, {
          "f7", "f6"
      }, {
          "b1", "d2"
      }, {
          "c8", "b7"
      }, {
          "d2", "b3"
      }, {
          "h7", "h6"
      }, {
          "a2", "a4"
      }, {
          "b5", "a4"
      }, {
          "a1", "a4"
      }, {
          "b7", "c8"
      }, {
          "f1", "e1"
      }, {
          "f8", "e7"
      }, {
          "f3", "h4"
      }, {
          "e8", "f7"
      }, {
          "d1", "h5"
      }, {
          "g7", "g6"
      }, {
          "h5", "g6"
      }, {
          "f7", "f8"
      }, {
          "g6", "h5"
      }, {
          "h8", "h7"
      }, {
          "h4", "g6"
      }, {
          "f8", "g7"
      }, {
          "g6", "h4"
      }, {
          "g7", "f8"
      }, {
          "b3", "a1"
      }, {
          "h7", "g7"
      }, {
          "a1", "c2"
      }, {
          "c8", "b7"
      }, {
          "c2", "e3"
      }, {
          "a7", "a5"
      }, {
          "g2", "h3"
      }, {
          "d8", "c8"
      }, {
          "c1", "d2"
      }, {
          "c6", "c5"
      }, {
          "e1", "a1"
      }, {
          "c5", "d4"
      }, {
          "c3", "d4"
      }, {
          "c8", "c6"
      }, {
          "a4", "a5"
      }, {
          "a8", "a5"
      }, {
          "a1", "a5"
      }, {
          "c6", "e8"
      }, {
          "h5", "e8"
      }, {
          "f8", "e8"
      }, {
          "h4", "f3"
      }, {
          "f6", "e5"
      }, {
          "f3", "e5"
      }, {
          "e7", "f6"
      }, {
          "h3", "e6"
      }, {
          "g8", "e7"
      }, {
          "e3", "g4"
      }, {
          "f6", "e5"
      }, {
          "g4", "e5"
      }, {
          "e7", "c6"
      }, {
          "a5", "d5"
      }, {
          "g7", "e7"
      }, {
          "e6", "h3"
      }, {
          "c6", "e5"
      }, {
          "d5", "e5"
      }, {
          "b8", "c6"
      }, {
          "e5", "e7"
      }, {
          "e8", "e7"
      }, {
          "d2", "c3"
      }, {
          "h6", "h5"
      }, {
          "h3", "g2"
      }, {
          "e7", "d7"
      }, {
          "g2", "f3"
      }, {
          "c6", "d4"
      }, {
          "f3", "b7"
      }, {
          "d4", "e2"
      }, {
          "g1", "f2"
      }, {
          "e2", "c1"
      }, {
          "b7", "f3"
      }, {
          "d7", "c8"
      }, {
          "f3", "h5"
      }, {
          "c8", "b8"
      }, {
          "h5", "g6"
      }, {
          "b8", "a8"
      }, {
          "g6", "c2"
      }, {
          "a8", "a7"
      }, {
          "f2", "e3"
      }, {
          "c1", "a2"
      }, {
          "e3", "d2"
      }, {
          "a2", "c3"
      }, {
          "b2", "c3"
      }, {
          "a7", "a6"
      }, {
          "c2", "b3"
      }, {
          "a6", "b7"
      }, {
          "d2", "d3"
      }, {
          "b7", "b6"
      }, {
          "d3", "e4"
      }, {
          "b6", "c7"
      }, {
          "e4", "e5"
      }, {
          "c7", "c6"
      }, {
          "e5", "f6"
      }, {
          "c6", "b5"
      }, {
          "f6", "e7"
      }, {
          "b5", "a5"
      }, {
          "f4", "f5"
      }, {
          "a5", "b5"
      }, {
          "f5", "f6"
      }
  };

  // Constructors

  /**
   * Create a new instance of this test.
   */
  public PlyGeneratorTest11() {
    super("King does an invalid move, why?");
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
      if (i == 16) {
        doPly(new CastlingPlyImpl(new PositionImpl(previousMoves[i][0]), false));
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
      if (plies[i].toString().contentEquals("b5-a4")) {
        containsMove = true;
      }
      ;
    }
    System.out.println("-----");

    assertFalse("King moves in check (b5-a4)", containsMove);
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
