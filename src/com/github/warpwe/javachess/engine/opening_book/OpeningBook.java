/*
 * OpeningBook - Interface to define the functionality of a opening book. Copyright (C) 2003 The
 * Java-Chess team <info@java-chess.de> This program is free software; you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option) any later version. This
 * program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details. You should have received a copy of the GNU General Public
 * License along with this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 */

package com.github.warpwe.javachess.engine.opening_book;

import java.io.File;

import com.github.warpwe.javachess.ply.IAnalyzedPly;
import com.github.warpwe.javachess.ply.IPly;

/**
 * This interface defines the functionality of a opening book.
 */
public interface OpeningBook {

  // Methods

  /**
   * Advance the game by one user ply.
   *
   * @param ply
   *          The ply from the user.
   */
  void doUserPly(IPly ply);

  /**
   * Get the next ply from the opening book, if there is one available, or null if not.
   *
   * @return The next ply from the opening book, or null if there's no ply available.
   */
  IAnalyzedPly getOpeningBookPly();

  /**
   * Reset the opening book to the initial piece position.
   */
  void reset();

  /**
   * Add a opening in PGN format to the opening book and return the error status.
   *
   * @param file
   *          The File to add to the opening book.
   */
  void addPgnOpening(File file);
}
