/*
	AboutAction - A action to display a about box.

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

package com.github.warpwe.javachess.action;

import java.awt.event.ActionEvent;

import com.github.warpwe.javachess.dialogs.DialogAbout;
import com.github.warpwe.javachess.util.Tools;

/**
 * This class implements the action to display a about box.
 */
public class AboutAction extends JavaChessAction {

	// Instance variables
  private static final long serialVersionUID = 4444583344900126602L;

  // Constructors
  /**
	 * Create a new action instance.
	 */
	public AboutAction() {
		super("About");
	}

	// Methods

	/**
	 * The actual action.
	 * 
	 * @param event
	 *            The event, that triggered the action.
	 */
	public void actionPerformed(ActionEvent event) {
		DialogAbout dialog = DialogAbout.getInstance();

		if (dialog != null) {
			Tools.setDialogToCenter(dialog);
			dialog.show();
		}
	}
}
