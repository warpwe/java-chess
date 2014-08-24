/*
 * AnimationLayer - A pane to animate moved chess pieces. Copyright (C) 2003 The Java-Chess team
 * <info@java-chess.de> This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free Software Foundation;
 * either version 2 of the License, or (at your option) any later version. This program is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details. You should have received a copy of the GNU General Public
 * License along with this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 */

package com.github.warpwe.javachess.renderer2d;

import java.awt.Point;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.github.warpwe.javachess.ply.IPly;

/**
 * This class animates moved chess pieces in a separate thread.
 */
class AnimationLayer extends JPanel implements Runnable {

  // Static variables

  // Instance variables

  /**
   * The generated ID.
   */
  private static final long serialVersionUID = -605845361052812415L;

  /**
   * The pieces layer.
   */
  private PiecesLayer piecesLayer;

  /**
   * The current ply to animate.
   */
  private IPly ply = null;

  /**
   * The renderer for the piece, that we are animate.
   */
  // private PieceRenderer _piece;

  /**
   * The current point of the animated piece.
   */
  private Point currentPoint;

  /**
   * The animation thread.
   */
  private volatile Thread animationThread;

  // Constructors

  /**
   * Create a new AnimationLayer instance.
   *
   * @param piecesLayer
   *          The layer with the pieces.
   */
  AnimationLayer(PiecesLayer piecesLayer) {
    setPiecesLayer(piecesLayer);
  }

  // Methods

  /**
   * Animate the move of a piece.
   */
  void animatePly(IPly ply) {
    this.ply = ply;
  }

  /**
   * The method to start the thread.
   */
  public void start() {
    if (animationThread == null) {
      animationThread = new Thread(this);
      animationThread.start();
    }
  }

  /**
   * The run method to start the animation thread.
   */
  public void run() {
    ImageIcon icon = (ImageIcon) getPiecesLayer().getPositionRenderer(
        ply.getSource().getSquareIndex()).getIcon();
    JLabel movingPiece = new JLabel(icon);
    // _piece = (PieceRenderer)icon.getImage();
    getPiecesLayer().getPositionRenderer(ply.getSource().getSquareIndex()).setIcon(null);
    getPiecesLayer().repaint();
    int xOffset = (ChessBoardRenderer2D.getSquareSize() - icon.getIconWidth()) / 2;
    int yOffset = (ChessBoardRenderer2D.getSquareSize() - icon.getIconHeight()) / 2;
    currentPoint = new Point((ply.getSource().getSquareIndex() & 7)
        * ChessBoardRenderer2D.getSquareSize() + xOffset,
        (7 - (ply.getSource().getSquareIndex() >> 3)) * ChessBoardRenderer2D.getSquareSize()
            + yOffset);
    Point endPoint = new Point((ply.getDestination().getSquareIndex() & 7)
        * ChessBoardRenderer2D.getSquareSize() + xOffset, (7 - (ply.getDestination()
        .getSquareIndex() >> 3)) * ChessBoardRenderer2D.getSquareSize() + yOffset);
    int xMoveOffset = currentPoint.x < endPoint.x ? 1 : currentPoint.x > endPoint.x ? -1 : 0;
    int yMoveOffset = currentPoint.y < endPoint.y ? 1 : currentPoint.y > endPoint.y ? -1 : 0;

    movingPiece.setLocation(currentPoint);

    while (!currentPoint.equals(endPoint)) {
      if (currentPoint.x == endPoint.x) {
        xMoveOffset = 0;
      }
      if (currentPoint.y == endPoint.y) {
        yMoveOffset = 0;
      }
      currentPoint.x += xMoveOffset;
      currentPoint.y += yMoveOffset;
      // invalidate();
      // repaint();
      // paint( getGraphics());
      // getGraphics().drawImage( _piece, _currentPoint.x, _currentPoint.y, this);
      movingPiece.setLocation(currentPoint);
      paintImmediately(currentPoint.x - 1, currentPoint.y - 1,
          ChessBoardRenderer2D.getSquareSize(), ChessBoardRenderer2D.getSquareSize());
      try {
        Thread.sleep(5);
      }
      catch (InterruptedException ignored) {
      }
      // getGraphics().clearRect( _currentPoint.x - 1, _currentPoint.y - 1,
      // ChessBoardRenderer2D.getSquareSize() + 1, ChessBoardRenderer2D.getSquareSize() + 1);
    }
    movingPiece.setVisible(false);
    getPiecesLayer().getPositionRenderer(ply.getDestination().getSquareIndex()).setIcon(icon);
    ply = null;
    animationThread = null;
  }

  /**
   * Paint the animated piece.
   *
   * @param g
   *          The graphics context.
   */
  /*
   * public void paint( Graphics g) { System.out.println( "Repaint piece at " + _currentPoint.x +
   * "," + _currentPoint.y); // g.drawImage( _piece, _currentPoint.x, _currentPoint.y, this); }
   */

  /**
   * Get the current pieces layer.
   *
   * @return The current pieces layer.
   */
  PiecesLayer getPiecesLayer() {
    return piecesLayer;
  }

  /**
   * Set a new pieces layer.
   *
   * @param piecesLayer
   *          The new pieces layer.
   */
  void setPiecesLayer(PiecesLayer piecesLayer) {
    this.piecesLayer = piecesLayer;
  }
}
