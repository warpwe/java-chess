/*
	Tools - An abstract class for (static) utility- and convenience methods.

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
package de.java_chess.javaChess.util;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JDialog;

/**
 * An abstract class for (static) utility- and convenience methods
 */
public abstract class Tools
{

	public Tools()
	{
	}

/**
 * Sets the Dialog to the center of the screen
 *
 * @param dialog The dialog which is to be centered on the screen
 */
	public static void setDialogToCenter(JDialog dialog)
	{
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension dialogSize = dialog.getSize();

		dialog.setLocation( (screenSize.width - dialogSize.width) / 2,
												(screenSize.height - dialogSize.height) / 2);

	}

}
