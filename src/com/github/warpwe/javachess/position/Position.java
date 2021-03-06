/*
 * Position - A interface to implement the position of one piece of a chess game. Copyright (C) 2003
 * The Java-Chess team <info@java-chess.de> This program is free software; you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option) any later version. This
 * program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details. You should have received a copy of the GNU General Public
 * License along with this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 */

package com.github.warpwe.javachess.position;

/**
 * This interface has to be implemented by any chess position.
 */
public interface Position {

  // Methods

  /**
   * Get the square index of this position instance.
   *
   * @return The square index of this piece position (0-63).
   */
  int getSquareIndex();

  /**
   * The square index of this position instance.
   *
   * @param index
   *          The square index of this position (0-63).
   */
  void setSquareIndex(int index);

  /**
   * Get the row index of this position.
   *
   * @return The row index of this position (0-7).
   */
  int getRowIndex();

  /**
   * Get the line index of this position.
   *
   * @return The line index of this position (0-7).
   */
  int getLineIndex();

  /**
   * Get the name of this square.
   *
   * @return The suare name of this position (i.e. a4).
   */
  String toSquareName();

  /**
   * Test if 2 positions are equal.
   *
   * @param pos Another
   *          position.
   * @return true, if the positions are equal, false otherwise.
   */
  boolean equals(Position pos);
}
