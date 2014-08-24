/*
 * MinimaxTest1 - The first test for the minimax implementation. Copyright (C) 2003 The Java-Chess
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
import com.github.warpwe.javachess.engine.ChessEngineImpl;
import com.github.warpwe.javachess.engine.IChessEngine;
import com.github.warpwe.javachess.game.GameImpl;
import com.github.warpwe.javachess.game.IGame;
import com.github.warpwe.javachess.ply.IPly;
import com.github.warpwe.javachess.ply.PlyImpl;
import com.github.warpwe.javachess.position.PositionImpl;

import junit.framework.TestCase;

/**
 * A test to check, if the the boards are passed correctly to the ply generator, if we search with a
 * search depth > 1. At the time, the computer suggests e7-e5 after the opening e2-e4,g8-f6,e4-e5 ,
 * which is a illegal move.
 */
public class MinimaxTest1 extends TestCase {

  // Instance variables

  /**
   * The current game.
   */
  IGame game;

  /**
   * The chess board.
   */
  IBitBoard board;

  /**
   * The chess engine.
   */
  IChessEngine engine;

  // Constructors

  /**
   * Create a new instance of this test.
   */
  public MinimaxTest1() {
    super("A test of the minimax implementation, if boards are passed correctly over search depths");
  }

  // Methods

  /**
   * Run the actual test(s).
   */
  public void runTest() {
    testminimax1();
  }

  /**
   * Prepare the test(s).
   */
  protected void setUp() {

    // Create a new game.
    game = new GameImpl();

    // Create a new board.
    board = new BitBoardImpl();

    // Create a engine instance.
    engine = new ChessEngineImpl(game, null, board, false);

    // Set the pieces on their initial positions.
    board.initialPosition();

    // Move the white pawn from e2 - e4
    doPly(new PlyImpl(new PositionImpl(12), new PositionImpl(12 + 16), false));

    // Move the black knight to f6
    doPly(new PlyImpl(new PositionImpl(62), new PositionImpl(62 - 17), false));

    // Move the white pawn to e5
    doPly(new PlyImpl(new PositionImpl(12 + 16), new PositionImpl(12 + 16 + 8), false));
  }

  /**
   * Run the actual test.
   */
  public void testminimax1() {

    // Compute the best ply for this game position.
    IPly bestPly = engine.computeBestPly();

    // Check if e7 - e5 was delivered as the best ply.
    assertTrue(
        "Engine computed invalid ply e7-e5",
        ((bestPly.getSource().getSquareIndex() != 52) || (bestPly.getDestination().getSquareIndex() != (52 - 15))));
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
