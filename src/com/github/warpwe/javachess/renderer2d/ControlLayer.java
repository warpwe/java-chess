/*
 * ControlLayer - A component to render user interactions with the board. Copyright (C) 2002,2003
 * Andreas Rueckert <mail@andreas-rueckert.de> This program is free software; you can redistribute
 * it and/or modify it under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option) any later version. This
 * program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details. You should have received a copy of the GNU General Public
 * License along with this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 */

package com.github.warpwe.javachess.renderer2d;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import com.github.warpwe.javachess.GameController;
import com.github.warpwe.javachess.ply.PlyImpl;
import com.github.warpwe.javachess.position.PositionImpl;

/**
 * This class handles the control layer, where the user enters his moves.
 */
class ControlLayer extends JPanel {

  // Instance variables.
  /**
   * The generated ID.
   */
  private static final long serialVersionUID = -6683596603588274222L;

  /**
   * The game controller.
   */
  GameController controller;

  /**
   * The pieces layer, that holds the chess pieces.
   */
  PiecesLayer piecesLayer;

  /**
   * The size of one square as a instance variable for caching purposes.
   */
  int squareSize;

  /**
   * The size of the entire board as a instance var for caching purposes.
   */
  int boardSize;

  /**
   * The source square for the next ply.
   */
  int sourceSquare = -1;

  // Constructors

  ControlLayer(GameController controller, PiecesLayer pl) {
    setController(controller);
    squareSize = ChessBoardRenderer2D.getSquareSize();
    boardSize = 8 * squareSize;
    setLayout(null);
    setOpaque(false);
    setPreferredSize(new Dimension(boardSize, boardSize));
    setBounds(0, 0, boardSize, boardSize);
    piecesLayer = pl;
    addMouseListener(new MouseAdapter() {

      public void mouseClicked(MouseEvent e) {
        if (sourceSquare == -1) {
          sourceSquare = 8 * (7 - e.getY() / squareSize) + (e.getX() / squareSize);
          repaint();
        }
        else {
          final int destSquare = 8 * (7 - e.getY() / squareSize) + (e.getX() / squareSize);

          new Thread(new Runnable() {
            public void run() {
              // The capture flag is added by the game controller(!).
              getController().userPly(
                  new PlyImpl(new PositionImpl(sourceSquare), new PositionImpl(destSquare), false));
              sourceSquare = -1;
              repaint();
            }
          }).start();
          // getController().userPly( new PlyImpl( new PositionImpl( _sourceSquare), new
          // PositionImpl( destSquare)));
          // _sourceSquare = -1;
          // repaint();
        }
      }
    });
  }

  // Methods
  /**
   * Overridden paintComponent method to draw the marker for the source square.
   *
   * @param g
   *          The graphics context.
   */
  public void paintComponent(Graphics g) {
    if (sourceSquare != -1) {          // If a source square is selected,
      markSquare(sourceSquare, g);  // mark it.
    }
  }

  /**
   * Draw a marker at the given square.
   *
   * @param square
   *          The square to mark.
   * @param g
   *          The graphics context.
   */
  private final void markSquare(int square, Graphics g) {
    int xpos = (square & 7) * squareSize;         // Compute the location of the
    int ypos = (7 - (square >> 3)) * squareSize;  // square
    int rectSize = squareSize - 1;                 // and it's size.
    g.setColor(Color.blue);                        // Simply draw a blue frame there.
    for (int i = 0; i < 3; i++) {
      g.drawRect(xpos++, ypos++, rectSize, rectSize);
      rectSize -= 2;
    }
  }

  /**
   * Get the game controller.
   *
   * @return The game controller.
   */
  private final GameController getController() {
    return controller;
  }

  /**
   * Set a new game controller.
   *
   * @param The
   *          new game controller.
   */
  private final void setController(GameController controller) {
    this.controller = controller;
  }
}
