/*
  ResetGameAction - A action to start a new game.

  Copyright (C) 2003 The Java-Chess team <info@java-chess.de>

  This program is free software; you can redistribute it and/or
  modify it under the terms of the GNU General Public License
  as published by the Free Software Foundation; either version 2
  of the License, or (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
*/

package de.java_chess.javaChess.action;

import java.awt.event.ActionEvent;

import de.java_chess.javaChess.JavaChess;


/**
 * This class implements the action to reset a game.
 */
public class ResetGameAction extends JavaChessAction {

    // Instance variables


    // Constructors

    /**
     * Create a new action instance.
     */
    public ResetGameAction() {
	super( "Reset game");
    }


    // Methods

    /**
     * The actual action.
     *
     * @param event The event, that caused the action.
     */
    public void actionPerformed( ActionEvent event) {
	JavaChess.getInstance().reset();
    }
}


