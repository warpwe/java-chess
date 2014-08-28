/*
 * PlyImpl - A class to implement the functionality of one ply of a chess game. Copyright (C) 2003
 * The Java-Chess team <info@java-chess.de> This program is free software; you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option) any later version. This
 * program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details. You should have received a copy of the GNU General Public
 * License along with this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 */

package com.github.warpwe.javachess.ply;

import com.github.warpwe.javachess.position.Position;

/**
 * This class implements the functionality of a ply.
 */
public class PlyImpl implements IPly {

	// Instance variables

	/**
	 * The source of the ply.
	 */
	protected Position source;

	/**
	 * The destination of the ply.
	 */
	protected Position destination;

	/**
	 * Flag to indicate, if this ply captures another piece.
	 */
	protected boolean capture;

	private short score = 0;

	// Constructors

	/**
	 * Construct a new ply from a source and a destination.
	 * 
	 * @param source
	 *            The source of the ply.
	 * @param destination
	 *            The destination of the ply.
	 * @param capture
	 *            Flag to indicate, if this ply captures another piece.
	 */
	public PlyImpl(Position source, Position destination, boolean capture) {
		setSource(source);
		setDestination(destination);
		setCapture(capture);
	}

	// Methods

	/**
	 * Get the source of the ply.
	 * 
	 * @return The source of the piece.
	 */
	public final Position getSource() {
		return source;
	}

	/**
	 * Set the source of the ply.
	 * 
	 * @param The
	 *            new source of the piece.
	 */
	public final void setSource(Position source) {
		this.source = source;
	}

	/**
	 * Get the destination of the piece.
	 * 
	 * @return The destination of the piece.
	 */
	public final Position getDestination() {
		return destination;
	}

	/**
	 * Set the destination of the piece.
	 * 
	 * @param destination
	 *            The new destination of the piece.
	 */
	public final void setDestination(Position destination) {
		this.destination = destination;
	}

	/**
	 * Check, if this ply captures another piece.
	 * 
	 * @return true, if another piece is captured with this ply.
	 */
	public final boolean isCapture() {
		return capture;
	}

	/**
	 * Set a flag, if this ply captures another piece.
	 * 
	 * @param capture
	 *            Flag to indicate if this ply captures another piece.
	 */
	public final void setCapture(boolean capture) {
		this.capture = capture;
	}

	/**
	 * Convert the ply into something human readable. It's not exactly chess
	 * notation, since we don't have a board to check, if it is a move or a
	 * attack.
	 * 
	 * @return The ply as a string.
	 */
	public String toString() {
		return getSource().toSquareName() + "-"
				+ getDestination().toSquareName();
	}

	/**
	 * Test, if this ply is equal to another ply.
	 * 
	 * @param ply
	 *            The other ply.
	 * @return true, if the 2 plies are equal.
	 */
	public final boolean equals(IPly ply) {
		return ply.getSource().equals(getSource())
				&& ply.getDestination().equals(getDestination());
	}

	@Override
	public void setScore(short score) {
		this.score = score;

	}

	@Override
	public short getScore() {
		return score;
	}
}
