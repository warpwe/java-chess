/*
  PGNPlyFragment - A class to store information fragments on a ply.

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

package de.java_chess.javaChess.pgn;

import de.java_chess.javaChess.piece.Piece;
import de.java_chess.javaChess.position.Position;
import de.java_chess.javaChess.position.PositionImpl;


/**
 * This class stores information fragments on a ply, like they
 * are found in a PGN file, where piece type or originating square
 * are sometimes missing.
 */
public class PGNPlyFragment {

    // Instance variables

    /**
     * The type of the piece.
     */
    private byte _pieceType = -1;

    /**
     * Origin.
     */
    private Position _origin = null;

    /**
     * Destination.
     */
    private Position _destination = null;

    /**
     * Flag to indicate, if it is a capture.
     */
    boolean _capture = false;

    /**
     * Flag to indicate, if this ply is a castling.
     */
    private boolean _castling = false;

    /**
     * Flag to indicate, if the castling goes to the left.
     */
    private boolean _leftCastling;

    /**
     * Flag to indicate, if it is a pawn promotion.
     */
    private boolean _pawnPromotion = false;

    /**
     * If it is a pawn promotion, this is the type of the new piece.
     */
    private byte _newPieceType = -1;

    
    // Constructors


    // Methods

    /**
     * Set the piece type from a figurine letter code.
     *
     * @param letter The figurine letter code.
     */
    public final void setPieceTypeFromLetter( char letter) {
	setPieceType( getPieceTypeFromLetter( letter));
    }

    /**
     * Get the current origin of this ply.
     *
     * @return The currently known origin of this ply.
     */
    public final Position getOrigin() {
	return _origin;
    }

    /**
     * Set the name of the origin square.
     *
     * @param squareName The name of the origin square.
     */
    public final void setOrigin( String squareName) {

	// Create a new position implementation from the name and store it.
	_origin = new PositionImpl( squareName);
    }

    /**
     * Set the name of the origin square.
     *
     * @param position The position of the origin square.
     */
    public final void setOrigin( Position position) {

	// Store the position.
	_origin = position;
    }

    /**
     * Get the current destination of this ply.
     *
     * @return The currently known destination of this ply.
     */
    public final Position getDestination() {
	return _destination;
    }

    /**
     * Set the name of the destination square.
     *
     * @param squareName The name of the destination square.
     */
    public final void setDestination( String squareName) {

	// Create a new position implementation from the name and store it.
	_destination = new PositionImpl( squareName);
    }

    /**
     * Set the flag, if this move captures a piece.
     *
     * @param capture true, if this move capures a piece.
     */
    public final void setCapture( boolean capture) {
	_capture = capture;
    }
    
    /**
     * Check, if this ply fragment is a capture.
     *
     * @return true, if this move is a capture.
     */
    public final boolean isCapture() {
	return _capture;
    }

    /**
     * Make this ply a pawn promotion and set the piece type after the promotion.
     *
     * @param newPieceTypeLetter The piece type after the pawn promotion as a figurine letter code.
     */
    public final void setPawnPromotion( char newPieceTypeLetter) {
	_pawnPromotion = true;
	_newPieceType = getPieceTypeFromLetter( newPieceTypeLetter);
    }

    /**
     * Check, if this move is a pawn promotion.
     *
     * @return true, if this move is a pawn promotion. False otherwise.
     */
    public final boolean isPawnPromotion() {
	return _pawnPromotion;
    }
    
    /**
     * Get the new piece type after the pawn promotion.
     *
     * @return The new piece type after the pawn promotion.
     */
    public final byte getNewPieceType() {
	return _newPieceType;
    }

    /**
     * Set this ply as a castling.
     *
     * @param goesLeft true, if the castling goes to the left.
     */
    public final void setCastling( boolean left) {
	_castling = true;
	_leftCastling = left;
    }

    /**
     * Check, if this ply fragment is a castling.
     *
     * @return true, if this ply fragment represents a castling. False otherwise.
     */
    public final boolean isCastling() {
	return _castling;
    }

    /**
     * Check, if this castling goes to the left.
     *
     * @return true, if this castling goes to the left.
     */
    public final boolean isLeftCastling() {
	return _leftCastling;
    }

    /**
     * Get the type of the moved piece.
     *
     * @return The type of the moved piece.
     */
    public final byte getPieceType() {
	return _pieceType;
    }

    /**
     * Set the type of the moved piece.
     *
     * @param type The type of the moved piece.
     */
    private final void setPieceType( byte type) {
	_pieceType = type;
    }

    /**
     * Get the piece type from a figurine letter.
     *
     * @param letter The figurine letter code.
     *
     * @return The piece type or -1, if it was no valid figurine letter code.
     */
    private final byte getPieceTypeFromLetter(  char letter) {
	if( letter == 'P') { return Piece.PAWN; }
	if( letter == 'N') { return Piece.KNIGHT; }
	if( letter == 'B') { return Piece.BISHOP; }
	if( letter == 'R') { return Piece.ROOK; }
	if( letter == 'Q') { return Piece.QUEEN; }
	if( letter == 'K') { return Piece.KING; }
	return (byte)-1;
    }
}
