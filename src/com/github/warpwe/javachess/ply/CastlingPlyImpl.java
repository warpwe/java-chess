/*
 * CastlingPlyImpl - A class that implements a castling ply. Copyright (C) 2003 The Java-Chess team
 * <info@java-chess.de> This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free Software Foundation;
 * either version 2 of the License, or (at your option) any later version. This program is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details. You should have received a copy of the GNU General Public
 * License along with this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 */

package com.github.warpwe.javachess.ply;

import com.github.warpwe.javachess.position.Position;
import com.github.warpwe.javachess.position.PositionImpl;

/**
 * This class represents a castling.
 */
public class CastlingPlyImpl extends PlyImpl implements ICastlingPly {

  // Instance variables

  /**
   * Flag to indicate, if this ply goes to the left.
   */
  private boolean isGoingLeft;

  // Constructors

  /**
   * Construct a new castling ply from a source and a destination and the direction.
   *
   * @param source
   *          The source of the ply.
   * @param goesLeft
   *          Flag to indicate if the castling goes left.
   */
  public CastlingPlyImpl(Position source, boolean goesLeft) {
    super(source, new PositionImpl(-1), false);
    setLeftCastling(goesLeft);
  }

  // Methods

  /**
   * Check, if this castling goes to the left side of the board.
   *
   * @return true, if the castling goes to the left side, false otherwise.
   */
  public final boolean isLeftCastling() {
    return isGoingLeft;
  }

  /**
   * Set the flag, if the castling goes left.
   *
   * @param isGoingLeft
   *          Flag to indicate if the castling goes left.
   */
  public final void setLeftCastling(boolean isGoingLeft) {
    this.isGoingLeft = isGoingLeft;
  }

  /**
   * Get a string representation of this castling.
   *
   * @return A string representation of this ply.
   */
  public final String toString() {
    return isLeftCastling() ? "O-O-O" : "O-O";
  }
}
