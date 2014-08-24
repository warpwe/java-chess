/*
 * GameLoader - A class to follow a chess game, while it is loaded from a PGN file. Copyright (C)
 * 2003 The Java-Chess team <info@java-chess.de> This program is free software; you can redistribute
 * it and/or modify it under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option) any later version. This
 * program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details. You should have received a copy of the GNU General Public
 * License along with this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 */

package com.github.warpwe.javachess.pgn;

import com.github.warpwe.javachess.bitboard.BitBoardImpl;
import com.github.warpwe.javachess.bitboard.IBitBoard;
import com.github.warpwe.javachess.board.Board;
import com.github.warpwe.javachess.engine.BitBoardAnalyzerImpl;
import com.github.warpwe.javachess.engine.IBitBoardAnalyzer;
import com.github.warpwe.javachess.engine.PlyGenerator;
import com.github.warpwe.javachess.engine.hashtable.PlyHashtableImpl;
import com.github.warpwe.javachess.game.GameImpl;
import com.github.warpwe.javachess.game.IGame;
import com.github.warpwe.javachess.notation.IPlyNotation;
import com.github.warpwe.javachess.notation.PlyNotationImpl;
import com.github.warpwe.javachess.ply.CastlingPlyImpl;
import com.github.warpwe.javachess.ply.IPly;
import com.github.warpwe.javachess.ply.PlyImpl;
import com.github.warpwe.javachess.ply.TransformationPlyImpl;
import com.github.warpwe.javachess.position.PositionImpl;

/**
 * This class follows a chess game, while it is loaded from a PGN file.
 */
public class GameLoader {

  // Instance variables

  /**
   * A game for the generator.
   */
  IGame game;

  /**
   * The current board of the game.
   */
  Board board;

  /**
   * A ply generator to check for valid moves.
   */
  PlyGenerator plyGenerator;

  /**
   * The analyzer, that is required by the ply generator.
   */
  IBitBoardAnalyzer analyzer;

  /**
   * Flag to indicate, what color moves.
   */
  boolean whiteMoves = true;

  // Constructors

  /**
   * Create a new game loader instance.
   */
  public GameLoader() {

    // Create a new game.
    game = new GameImpl();

    // Create a new board.
    board = new BitBoardImpl();

    // Create the ply generator.
    plyGenerator = new PlyGenerator(game, (IBitBoard) board, new PlyHashtableImpl(10));

    // And the analyzer.
    analyzer = new BitBoardAnalyzerImpl(game, plyGenerator);

    plyGenerator.setAnalyzer(analyzer);

    // Set the pieces on their initial positions.
    board.initialPosition();
  }

  // Methods

  /**
   * Turn a loaded ply fragment from a PGN file into a notated ply.
   *
   * @param plyFragment
   *          The info from the PGN file.
   * @return The notation for the ply, if we could recognize the ply, or null.
   */
  public final IPlyNotation completePly(PGNPlyFragment plyFragment) {

    // Create a ply from the ply fragment.
    IPly ply = null;
    IPlyNotation notation = null;

    // If the origin of the move is missing, compute it from the destination
    // ...not necessary yet...

    if (plyFragment.isCastling()) {
      ply = new CastlingPlyImpl(new PositionImpl(whiteMoves ? 4 : 60), plyFragment.isLeftCastling());
      notation = new PlyNotationImpl(ply, board.getPiece(ply.getSource()));
    }
    else {

      // Check, how complete our fragment is so far.
      if (plyFragment.getOrigin() == null) {  // If there was no origin given, try to figure it from
                                             // the currently available moves

        // Compute all the available moves for this board.
        IPly[] plies = plyGenerator.getPliesForColor(whiteMoves);

        for (int i = 0; i < plies.length; i++) {
          if ((plies[i].getDestination() == plyFragment.getDestination())
              && (board.getPiece(plies[i].getSource()).getType() == plyFragment.getPieceType())) {

            // Found the right ply!
            plyFragment.setOrigin(plies[i].getSource());

            break;  // Stop the search for the ply origin.
          }
        }
      }

      if (plyFragment.isPawnPromotion()) {
        ply = new TransformationPlyImpl(plyFragment.getOrigin(), plyFragment.getDestination(),
            plyFragment.getNewPieceType(), board.getPiece(plyFragment.getDestination()) == null);
        notation = new PlyNotationImpl(ply, board.getPiece(ply.getSource()));
      }
      else {
        ply = new PlyImpl(plyFragment.getOrigin(), plyFragment.getDestination(),
            board.getPiece(plyFragment.getDestination()) == null);
        notation = new PlyNotationImpl(ply, board.getPiece(ply.getSource()));
      }

      // Verifiy, if the notation doesn't show a capture on an empty sqare.
      if (plyFragment.isCapture() && board.getPiece(plyFragment.getDestination()) == null) {
        return null;
      }
    }

    // No perform the ply on our simulated board.
    doPly(ply);

    // And return the notation for the ply.
    return notation;
  }

  /**
   * Perform a ply, when it is completely loaded. param ply The ply to perform.
   */
  private final void doPly(IPly ply) {
    game.doPly(ply);
    board.doPly(ply);

    // The other color is about to move now.
    whiteMoves = !whiteMoves;
  }
}
