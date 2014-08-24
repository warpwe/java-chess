/*
 * ChessBoardRenderer2D - A class to render a 2D representation of a chessboard. Copyright (C) 2003
 * The Java-Chess team <info@java-chess.de> This program is free software; you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option) any later version. This
 * program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details. You should have received a copy of the GNU General Public
 * License along with this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 */

package com.github.warpwe.javachess.renderer2d;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import com.github.warpwe.javachess.GameController;
import com.github.warpwe.javachess.board.Board;
import com.github.warpwe.javachess.ply.IPly;
import com.github.warpwe.javachess.renderer.ChessBoardRenderer;

/**
 * This class renders a chessboard as a 2D graphics.
 */
public class ChessBoardRenderer2D extends JPanel implements ChessBoardRenderer {

  // Static variables
  /**
   * The generated ID.
   */
  private static final long serialVersionUID = 6175164814825572312L;

  /**
   * The size of each square.
   */
  private static final int squareSize = 50;

  // Instance variables
  /**
   * The current board.
   */
  private Board board;

  /**
   * The control layer, where the user does his moves.
   */
  ControlLayer controlLayer;

  /**
   * The pieces layer, that holds all the pieces of the current board.
   */
  PiecesLayer piecesLayer;

  // Constructors

  /**
   * Create a new renderer instance.
   *
   * @param controller
   *          The game controller.
   * @param board
   *          The current board.
   */
  public ChessBoardRenderer2D(GameController controller, Board board) {
    setBoard(board);  // Store the board in a class variable.

    // Use a boarder layout for the entire component, since it holds
    // more that the chessboard itself.
    setLayout(new BorderLayout());

    // Add the row numbers to the board.
    JPanel rowNumbers = new JPanel();
    rowNumbers.setLayout(new GridLayout(8, 1));
    rowNumbers.setPreferredSize(new Dimension(squareSize / 2, 8 * squareSize));
    for (int i = 8; i > 0; i--) {
      rowNumbers.add(new JLabel("" + i, JLabel.CENTER));
    }
    add(rowNumbers, BorderLayout.WEST);

    // Add the board itself.
    JLayeredPane boardPane = new JLayeredPane();
    boardPane.setPreferredSize(new Dimension(8 * squareSize, 8 * squareSize));
    boardPane.setOpaque(false);

    // The squares of the board are drawn on the board layer.
    boardPane.add(new BoardLayer(), JLayeredPane.DEFAULT_LAYER);

    // The next layer holds the pieces.
    boardPane.add(piecesLayer = new PiecesLayer(getBoard()), JLayeredPane.PALETTE_LAYER);

    // The next layer holds the control markers, when the user moves a piece.
    if (controller != null)
      boardPane.add(controlLayer = new ControlLayer(controller, piecesLayer),
          JLayeredPane.MODAL_LAYER);

    // The next layer shows the animated pieces.
    boardPane.add(piecesLayer.getAnimationLayer(), JLayeredPane.DRAG_LAYER);

    add(boardPane);

    // Add the line names to the board
    JPanel lineNames = new JPanel();
    lineNames.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
    lineNames.setPreferredSize(new Dimension(8 * squareSize + squareSize / 2, squareSize / 2));
    JLabel placeHolder = new JLabel();
    placeHolder.setPreferredSize(new Dimension(squareSize / 2, squareSize / 2));
    lineNames.add(placeHolder);
    byte[] name = new byte[1];
    for (int i = 0; i < 8; i++) {
      name[0] = (byte) ('a' + i);
      JLabel nameLabel = new JLabel(new String(name), JLabel.CENTER);
      nameLabel.setPreferredSize(new Dimension(squareSize, squareSize / 2));
      lineNames.add(nameLabel);
    }
    add(lineNames, BorderLayout.SOUTH);
  }

  /**
   * Reset the renderer for a new game.
   */
  public void reset() {
    repaintBoard();
  }

  /**
   * Repaint the board after a game position change.
   */
  public final void repaintBoard() {
    piecesLayer.repaintBoard();
    repaint();
  }

  /**
   * Repaint the board after a game position change.
   */
  public final void clearBoard() {
    piecesLayer.clearBoard();
    repaint();
  }

  /**
   * Render a ply (the move of a piece).
   *
   * @param ply
   *          The ply to render.
   */
  public final void doPly(IPly ply) {
    piecesLayer.doPly(ply);
    repaint();
  }

  /**
   * Get the current board.
   *
   * @return The current board.
   */
  public final Board getBoard() {
    return board;
  }

  /**
   * Set a new board.
   *
   * @param board
   *          The new board.
   */
  public final void setBoard(Board board) {
    this.board = board;
  }

  /**
   * Get the size of a square.
   *
   * @return The size of a square as a int (since height and width are the same).
   */
  public static final int getSquareSize() {
    return squareSize;
  }

  /**
   * Get the preferred size of the board.
   *
   * @return The preferred size of the board.
   */
  public final Dimension getPreferredSize() {
    return getMinimumSize();
  }

  /**
   * Get the maximum size of the board.
   *
   * @return The maximum size of the board.
   */
  public final Dimension getMaximumSize() {
    return getMinimumSize();
  }

  /**
   * Get the minimum size of the board.
   *
   * @return The minimum size of the board.
   */
  public final Dimension getMinimumSize() {
    return new Dimension(8 * getSquareSize() + getSquareSize() / 2, 8 * getSquareSize()
        + getSquareSize() / 2);
  }
}
