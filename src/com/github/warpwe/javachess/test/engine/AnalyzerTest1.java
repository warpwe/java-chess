/*
 * AnalyzerTest1 - The first test for the board analyzer. Copyright (C) 2003 The Java-Chess team
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
import com.github.warpwe.javachess.ply.PlyImpl;
import com.github.warpwe.javachess.position.PositionImpl;

import junit.framework.TestCase;

/**
 * A simple test for the BitBoardAnalyzer.
 */
public class AnalyzerTest1 extends TestCase {

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
  public AnalyzerTest1() {
    super("A simple analyzer test with a attack on the black queen");
  }

  // Methods

  /**
   * Run the actual test(s).
   */
  public void runTest() {
    testanalyzer1();
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

    // Set the pieces on their initial positions.
    board.initialPosition();

    // Move the black knight to f6
    board.doPly(new PlyImpl(new PositionImpl(62), new PositionImpl(62 - 17), false));

    // Move a white pawn to the 6th row
    board.doPly(new PlyImpl(new PositionImpl(11), new PositionImpl(11 + 32), false));
  }

  /**
   * Run the actual test.
   */
  public void testanalyzer1() {

    // Do the 1st analysis
    short score1 = analyzer.analyze((IBitBoard) board, true);

    // Move the pawn and attack a black pawn
    board.doPly(new PlyImpl(new PositionImpl(43), new PositionImpl(52), true));

    // Analyze the board after the ply
    short score2 = analyzer.analyze((IBitBoard) board, false);

    // Check if the 2nd board is better for white.
    assertTrue("Analyzer did not recognize attack on black piece", score2 > score1);

    // Now attack the black queen.
    board.doPly(new PlyImpl(new PositionImpl(52), new PositionImpl(59), true));

    // Analyze the board again
    short score3 = analyzer.analyze((IBitBoard) board, false);

    assertTrue("Analyzer does not recognize attack on black queen", score3 > score2);
  }
}
