/*
 * PlyGeneratorTest4 - A test for a rook capture by a bishop. Copyright (C) 2003 The Java-Chess team
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
 * A simple test for the ply generator, if it generates a rook capture on h8 by a bishop.
 */
public class PlyGeneratorTest4 extends TestCase {

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
  public PlyGeneratorTest4() {
    super("A simple ply generator test for a rook capture on h8 by a bishop");
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

    // Move the white pawn from b2 - b3
    doPly(new PlyImpl(new PositionImpl(9), new PositionImpl(17), false));

    // Move the black pawn from g7 - g6
    doPly(new PlyImpl(new PositionImpl(54), new PositionImpl(46), false));

    // Move the bishop from c1 - b2
    doPly(new PlyImpl(new PositionImpl(2), new PositionImpl(9), false));

    // Move the black pawn from g6 - g5 (doesn't matter anyway. Just keep
    // the attack on h8 open.
    doPly(new PlyImpl(new PositionImpl(66), new PositionImpl(66 - 8), false));
  }

  /**
   * Run the actual test.
   */
  public void testgenerator() {

    // Get the plies for white
    IPly[] plies = plyGenerator.getPliesForColor(true);

    // Check if b2 x h8 was delivered as a potential ply.
    boolean containsCapture = false;

    for (int i = 0; i < plies.length; i++) {
      if (plies[i].getSource().getSquareIndex() == 9
          && plies[i].getDestination().getSquareIndex() == 63) {
        containsCapture = true;
        break;
      }
    }

    assertTrue("PlyGenerator computes capture b2 x h8", containsCapture);
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
