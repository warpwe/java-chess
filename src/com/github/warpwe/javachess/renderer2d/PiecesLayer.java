/*
 * PiecesLayer - A component to hold the pieces of a chess game. Copyright (C) 2003 The Java-Chess
 * team <info@java-chess.de> This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free Software Foundation;
 * either version 2 of the License, or (at your option) any later version. This program is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details. You should have received a copy of the GNU General Public
 * License along with this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 */

package com.github.warpwe.javachess.renderer2d;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import com.github.warpwe.javachess.board.Board;
import com.github.warpwe.javachess.piece.IPiece;
import com.github.warpwe.javachess.ply.ICastlingPly;
import com.github.warpwe.javachess.ply.IEnPassantPly;
import com.github.warpwe.javachess.ply.IPly;
import com.github.warpwe.javachess.ply.ITransformationPly;
import com.github.warpwe.javachess.position.Position;
import com.github.warpwe.javachess.position.PositionImpl;

/**
 * This class implements the functionality to hold the pieces of a chessboard.
 */
class PiecesLayer extends JPanel {

  // Instance variables
  /**
   * The generated ID.
   */
  private static final long serialVersionUID = 2192775027916243715L;

  private Board board;

  private ChessSet set;

  /**
   * Flag to indicate of the moves are animated.
   */
  private boolean animatedMoves = false;

  /**
   * The layer to animate the moved pieces.
   */
  private AnimationLayer animationLayer;

  /**
   * The labels for all the squares.
   */
  private PositionRenderer[] square = new PositionRenderer[64];

  PiecesLayer(Board board) {
    super(new GridLayout(8, 8));
    set = new ChessSet(this);

    animationLayer = new AnimationLayer(this);

    this.board = board;
    int boardSize = 8 * ChessBoardRenderer2D.getSquareSize();
    setPreferredSize(new Dimension(boardSize, boardSize));
    setBounds(0, 0, boardSize, boardSize);
    setOpaque(false);
    repaintBoard();
  }

  final void repaintBoard() {
    // removeAll();

    // Create a renderer for each chessboard square

    Position pos = new PositionImpl(0);  // Avoid to create new position instances for each square

    for (int s = 0; s < 64; s++) {
      pos.setSquareIndex(s);
      IPiece p = board.getPiece(pos);
      square[s] = (p != null ? new PositionRenderer(new PieceRenderer(p.getColor(), p.getType(),
          set, this)) : new PositionRenderer());
    }

    // Remove all the squares, in case this is a reset and the pieces layer already contains the
    // squares of an existing board.
    removeAll();

    // The order of the component adding is different then the order of the chessboard squares.
    // _square[0] = square a1 of the chessboard, but the components are added from the left
    // upper square (= a8)
    for (int s = 63; s >= 0; s--) {
      add(square[(s & ~7) + (7 - (s & 7))]);
    }
    repaint();
  }

  /**
   * Render a ply.
   *
   * @param ply
   *          The ply to render.
   */
  public final void doPly(IPly ply) {
    if (animatedMoves) {
      getAnimationLayer().animatePly(ply);
      getAnimationLayer().start();
      repaint();
    }
    else {
      // Check, if it was a castling
      if (ply instanceof ICastlingPly) {
        int source = ply.getSource().getSquareIndex();
        if (((ICastlingPly) ply).isLeftCastling()) {
          square[source - 2].getPieceFrom(square[source]);
          square[source - 1].getPieceFrom(square[source - 4]);  // Move the rook to the right
        }
        else {
          square[source + 2].getPieceFrom(square[source]);
          square[source + 1].getPieceFrom(square[source + 3]);  // Move the rook to the left
        }
      }
      else {
        // If a pawn has just reached the last row
        if (ply instanceof ITransformationPly) {

          // Copy the piece from source square to destination square.
          square[ply.getDestination().getSquareIndex()].getPieceFrom(square[ply.getSource()
              .getSquareIndex()]);

          // Now change the rendering to the new piece type.
          square[ply.getDestination().getSquareIndex()].setIcon(new ImageIcon(new PieceRenderer(
              ply.getDestination().getSquareIndex() < 8 ? IPiece.BLACK : IPiece.WHITE,
              ((ITransformationPly) ply).getTypeAfterTransformation(), set, this)));
        }
        else {
          // Copy the piece from source square to destination square.
          square[ply.getDestination().getSquareIndex()].getPieceFrom(square[ply.getSource()
              .getSquareIndex()]);

          // If it's a en passant ply, remove the attacked pawn.
          if (ply instanceof IEnPassantPly) {
            square[((IEnPassantPly) ply).getAttackedPosition().getSquareIndex()].setIcon(null);
          }
        }
      }
      repaint();
    }
  }

  /**
   * Get the current animation layer.
   *
   * @return The current animation layer.
   */
  final AnimationLayer getAnimationLayer() {
    return animationLayer;
  }

  /**
   * Get the position renderer for a given square.
   *
   * @param squareIndex
   *          The index of the square.
   * @return The position renderer for the given square.
   */
  final PositionRenderer getPositionRenderer(int squareIndex) {
    return square[squareIndex];
  }

  final void clearBoard() {

    // Create a renderer for each chessboard square
    Position pos = new PositionImpl(0);  // Avoid to create new position instances for each square

    for (int s = 0; s < 64; s++) {
      pos.setSquareIndex(s);
      square[s] = new PositionRenderer();
    }

    // Remove all the squares, in case this is a reset and the pieces layer already contains the
    // squares of an existing board.
    removeAll();

    // The order of the component adding is different then the order of the chessboard squares.
    // _square[0] = square a1 of the chessboard, but the components are added from the left
    // upper square (= a8)
    for (int s = 63; s >= 0; s--) {
      add(square[(s & ~7) + (7 - (s & 7))]);
    }
    repaint();
  }
}
