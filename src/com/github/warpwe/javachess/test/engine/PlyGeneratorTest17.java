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
public class PlyGeneratorTest17 extends TestCase {

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
  String[][] _prevMoves = {
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
          "f1", "e1"
      }, {
          "d8", "b6"
      }, {
          "g1", "h1"
      }, {
          "f6", "f5"
      }, {
          "f3", "h4"
      }, {
          "g7", "g6"
      }, {
          "c1", "e3"
      }, {
          "f8", "e7"
      }, {
          "b1", "d2"
      }, {
          "c8", "b7"
      }, {
          "h4", "f3"
      }, {
          "a7", "a5"
      }, {
          "f3", "g5"
      }, {
          "e7", "g5"
      }, {
          "f4", "g5"
      }, {
          "b6", "c7"
      }, {
          "d2", "b3"
      }, {
          "b8", "d7"
      }, {
          "e3", "f4"
      }, {
          "a5", "a4"
      }, {
          "b3", "c5"
      }, {
          "d7", "c5"
      }, {
          "d4", "c5"
      }, {
          "a8", "a5"
      }, {
          "d1", "d4"
      }, {
          "a5", "a8"
      }, {
          "b2", "b4"
      }, {
          "e8", "f8"
      }, {
          "a2", "a3"
      }, {
          "a8", "d8"
      }, {
          "e1", "f1"
      }, {
          "g8", "e7"
      }, {
          "a1", "e1"
      }, {
          "f8", "g8"
      }, {
          "g2", "h3"
      }, {
          "d8", "e8"
      }, {
          "g3", "g4"
      }, {
          "e8", "a8"
      }, {
          "g4", "f5"
      }, {
          "g6", "f5"
      }, {
          "d4", "f2"
      }, {
          "a8", "f8"
      }, {
          "f2", "h4"
      }, {
          "d5", "d4"
      }, {
          "c3", "d4"
      }, {
          "c7", "d8"
      }, {
          "f4", "d2"
      }, {
          "e7", "g6"
      }, {
          "h4", "f2"
      }, {
          "h7", "h6"
      }, {
          "f1", "g1"
      }, {
          "d8", "d5"
      }, {
          "h3", "g2"
      }, {
          "d5", "a2"
      }, {
          "f2", "e3"
      }, {
          "h6", "g5"
      }, {
          "e3", "g5"
      }, {
          "h8", "h2"
      }
  };

  // Constructors

  /**
   * Create a new instance of this test.
   */
  public PlyGeneratorTest17() {
    super("Computer_behauptet_mattzusetzen.pgn");
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
    for (int i = 0; i < _prevMoves.length; i++) {
      // castling
      if (i == 16) {
        doPly(new CastlingPlyImpl(new PositionImpl(_prevMoves[i][0]), false));
        continue;
      }

      doPly(new PlyImpl(new PositionImpl(_prevMoves[i][0]), new PositionImpl(_prevMoves[i][1]),
          false));
    }
  }

  /**
   * Run the actual test.
   */
  public void testgenerator() {

    // Get the plies for white
    IPly[] plies = plyGenerator.getPliesForColor(true);

    // Check potential plys.
    boolean containsMove = false;

    for (int i = 0; i < plies.length; i++) {
      System.out.println(i + " " + plies[i].toString());
      containsMove = true;
    }
    System.out.println("-----");

    assertTrue("Computer_behauptet_mattzusetzen", containsMove);
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
