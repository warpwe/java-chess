/*
 * PlyGeneratorTest5 - A test for a queen capture by a bishop. Copyright (C) 2003 The Java-Chess
 * team <info@java-chess.de> This program is free software; you can redistribute it and/or modify it
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
import com.github.warpwe.javachess.ply.IPly;
import com.github.warpwe.javachess.ply.PlyImpl;
import com.github.warpwe.javachess.position.PositionImpl;

import junit.framework.TestCase;

/**
 * A simple test for the ply generator, if it generates a queen capture on h7 by a bishop.
 */
public class PlyGeneratorTest5 extends TestCase {

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
          "e2", "e4"
      }, {
          "g8", "f6"
      }, {
          "e4", "e5"
      }, {
          "c7", "c6"
      }, {
          "e5", "f6"
      }, {
          "d7", "d5"
      }, {
          "d2", "d4"
      }, {
          "g7", "f6"
      }, {
          "f1", "d3"
      }, {
          "e7", "e6"
      }, {
          "d1", "h5"
      }, {
          "h8", "g8"
      }, {
          "h5", "h7"
      }, {
          "g8", "g2"
      }, {
          "h7", "h8"
      }, {
          "e8", "d7"
      }, {
          "c1", "f4"
      }, {
          "f8", "b4"
      }, {
          "c2", "c3"
      }, {
          "d8", "h8"
      }, {
          "c3", "b4"
      }, {
          "h8", "h4"
      }, {
          "f4", "e3"
      }, {
          "g2", "g1"
      }, {
          "h1", "g1"
      }, {
          "h4", "h2"
      }, {
          "g1", "g8"
      }, {
          "h2", "h7"
      }
  };

  // Constructors

  /**
   * Create a new instance of this test.
   */
  public PlyGeneratorTest5() {
    super("A simple ply generator test for a queen capture on h7 by a bishop");
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
      doPly(new PlyImpl(new PositionImpl(previousMoves[i][0]),
          new PositionImpl(previousMoves[i][1]), false));
    }
  }

  /**
   * Run the actual test.
   */
  public void testgenerator() {

    // Get the plies for white
    IPly[] plies = plyGenerator.getPliesForColor(true);

    // Check if d3 x h7 was delivered as a potential ply.
    boolean containsCapture = false;

    for (int i = 0; i < plies.length; i++) {
      if (19 == plies[i].getSource().getSquareIndex()
          && 55 == plies[i].getDestination().getSquareIndex()) {
        containsCapture = true;
        break;
      }
    }

    assertTrue("PlyGenerator computes capture d3 x h7", containsCapture);
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
