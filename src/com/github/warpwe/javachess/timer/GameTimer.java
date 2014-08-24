/*
 * GameTimer - A interfac to define a game timer. Copyright (C) 2003 The Java-Chess team
 * <info@java-chess.de> This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free Software Foundation;
 * either version 2 of the License, or (at your option) any later version. This program is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details. You should have received a copy of the GNU General Public
 * License along with this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 */

package com.github.warpwe.javachess.timer;

import java.awt.event.ActionListener;

/**
 * A interface to define a game timer.
 */
public interface GameTimer {

  // Methods

  /**
   * Start the entire timer.
   */
  void start();

  /**
   * Toggle from one player to the other.
   */
  void toggle();

  /**
   * Stop the entire timer.
   */
  void stop();

  /**
   * Check if the timer is actually running.
   *
   * @return true, if the timer is active, false otherwise.
   */
  boolean isRunning();

  /**
   * Add a action listener to the timer.
   *
   * @param listener
   *          The action listener to add.
   */
  void addActionListener(ActionListener listener);
}
