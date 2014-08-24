/*
 * PGNOutputStream - A class to write PGN files. Copyright (C) 2003 The Java-Chess team
 * <info@java-chess.de> This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free Software Foundation;
 * either version 2 of the License, or (at your option) any later version. This program is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details. You should have received a copy of the GNU General Public
 * License along with this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 */

package com.github.warpwe.javachess.pgn;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.github.warpwe.javachess.notation.IGameNotation;

/**
 * This class handes PGN output streams.
 */
public class PGNOutputStream extends FileOutputStream {

  // Instance variables

  // Constructors

  /**
   * Create a new PGNOutputStream instance.
   *
   * @param file
   *          The file to write into.
   */
  public PGNOutputStream(File file) throws IOException {
    super(file);
  }

  // Methods

  /**
   * Write the notation of a game.
   *
   * @param gameNotation
   *          The notation of the game.
   */
  public final void write(IGameNotation gameNotation) throws IOException {

    // Check, if there is some header available.
    String header = gameNotation.getPGNheader();

    if (!"".equals(header)) {
      header += "\n";
      write(header.getBytes());
    }

    write(gameNotation.toString().getBytes());
  }
}
