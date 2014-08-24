/*
 * AnalyzerTest2 - The second test for the board analyzer. Copyright (C) 2003 The Java-Chess team
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
 * A simple test for the BitBoardAnalyzer.
 */
public class AnalyzerTest2 extends TestCase {

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

  // Constructors
  /**
   * Create a new instance of this test.
   */
  public AnalyzerTest2() {
    super("A simple analyzer test for a opening strategy");
  }

  // Methods
  /**
   * Run the actual test(s).
   */
  public void runTest() {
    testanalyzer2();
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
    plyGenerator = new PlyGenerator(game, new PlyHashtableImpl(1000));

    // And the analyzer.
    analyzer = new BitBoardAnalyzerImpl(game, plyGenerator);
  }

  /**
   * Run the actual test.
   */
  public void testanalyzer2() {

    prepareTest();

    // Move the black pawn to e5 and analyze the board
    doPly(new PlyImpl(new PositionImpl("e7"), new PositionImpl("e5"), false));

    // Do the 1st analysis
    short score1 = analyzer.analyze((IBitBoard) board, true);

    prepareTest();

    doPly(new PlyImpl(new PositionImpl("g8"), new PositionImpl("h6"), false));

    // Analyze the board after the ply
    short score2 = analyzer.analyze((IBitBoard) board, false);

    // Check if the 2nd board is better for white.
    assertTrue("Analyzer prefers pawn move", score2 > score1);
  }

  /**
   * Prepare the opening test.
   */
  private final void prepareTest() {
    reset();
    // Move a white pawn to the 4th row
    doPly(new PlyImpl(new PositionImpl("e2"), new PositionImpl("e4"), false));
  }

  /**
   * Reset the game for a new score.
   */
  private final void reset() {
    game.reset();
    board.initialPosition();
  }

  /**
   * Perform a ply.
   *
   * @param ply
   *          The ply to perform.
   */
  private final void doPly(IPly ply) {
    game.doPly(ply);
    board.doPly(ply);
  }
}
