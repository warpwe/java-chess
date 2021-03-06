/*
 * PieceRenderer - A component to render a chess piece with a given image. Copyright (C) 2003 The
 * Java-Chess team <info@java-chess.de> This program is free software; you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option) any later version. This
 * program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details. You should have received a copy of the GNU General Public
 * License along with this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 */

package com.github.warpwe.javachess.renderer2d;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * This class renders a chess piece as a image.
 */
public class PieceRenderer extends BufferedImage {

  // Instance variables

  /**
   * The color of the piece.
   */
  byte color;

  /**
   * The type of the piece (pawn, bishop, etc).
   */
  byte piece;

  /**
   * A provider for the piece images.
   */
  ChessSet set;

  // Constructors
  /**
   * Create a new piece renderer instance.
   *
   * @param color
   *          The color of the piece.
   * @param piece
   *          The type of the piece.
   * @param set
   *          The set of chess pieces.
   * @param c The parent component.
   */
  public PieceRenderer(byte color, byte piece, ChessSet set, Component c) {
    super(40, 40, BufferedImage.TYPE_INT_ARGB);
    this.piece = piece;
    this.color = color;
    this.set = set;
    Graphics g = getGraphics();
    g.drawImage(set.getSubimage((piece - 1) * 40, color * 40, 40, 40), 0, 0, c);
  }

  // Methods
}
