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
import com.github.warpwe.javachess.ply.IPly;
import com.github.warpwe.javachess.ply.PlyImpl;
import com.github.warpwe.javachess.position.PositionImpl;

import junit.framework.TestCase;

/**
 * A simple test for the ply generator, if it generates a bishop move f1-c4.
 */
public class PlyGeneratorTest7 extends TestCase {

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
          "e7", "e6"
      }, {
          "f2", "f4"
      }, {
          "d8", "h4"
      }, {
          "g2", "g3"
      }, {
          "h4", "h6"
      }, {
          "f1", "g2"
      }, {
          "f8", "b4"
      }, {
          "c2", "c3"
      }, {
          "b4", "c5"
      }, {
          "d2", "d4"
      }, {
          "c5", "b6"
      }, {
          "f4", "f5"
      }, {
          "g7", "g5"
      }, {
          "g1", "f3"
      }, {
          "e6", "f5"
      }, {
          "c1", "g5"
      }, {
          "h6", "h5"
      }, {
          "e4", "f5"
      }, {
          "e8", "f8"
      }
  };

  // Constructors

  /**
   * Create a new instance of this test.
   */
  public PlyGeneratorTest7() {
    super("Rochadefehler warum?");
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

    // Check potential plys.
    boolean containsMove = false;

    for (int i = 0; i < plies.length; i++) {
      System.out.println(i + " " + plies[i].toString());
      if (plies[i].toString().contentEquals("O-O")) {
        containsMove = true;
      }
      ;
    }
    System.out.println("-----");

    assertTrue("Rochade erlaubt", containsMove);
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
