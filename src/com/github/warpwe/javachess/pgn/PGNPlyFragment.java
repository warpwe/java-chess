/*
 * PGNPlyFragment - A class to store information fragments on a ply. Copyright (C) 2003 The
 * Java-Chess team <info@java-chess.de> This program is free software; you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option) any later version. This
 * program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details. You should have received a copy of the GNU General Public
 * License along with this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 */

package com.github.warpwe.javachess.pgn;

import com.github.warpwe.javachess.piece.IPiece;
import com.github.warpwe.javachess.position.Position;
import com.github.warpwe.javachess.position.PositionImpl;

/**
 * This class stores information fragments on a ply, like they are found in a PGN file, where piece
 * type or originating square are sometimes missing.
 */
public class PGNPlyFragment {

  // Instance variables

  /**
   * The type of the piece.
   */
  private byte pieceType = -1;

  /**
   * Origin.
   */
  private Position origin = null;

  /**
   * Destination.
   */
  private Position destination = null;

  /**
   * Flag to indicate, if it is a capture.
   */
  boolean capture = false;

  /**
   * Flag to indicate, if this ply is a castling.
   */
  private boolean castling = false;

  /**
   * Flag to indicate, if the castling goes to the left.
   */
  private boolean leftCastling;

  /**
   * Flag to indicate, if it is a pawn promotion.
   */
  private boolean pawnPromotion = false;

  /**
   * If it is a pawn promotion, this is the type of the new piece.
   */
  private byte newPieceType = -1;

  // Constructors

  // Methods

  /**
   * Set the piece type from a figurine letter code.
   *
   * @param letter
   *          The figurine letter code.
   */
  public final void setPieceTypeFromLetter(char letter) {
    setPieceType(getPieceTypeFromLetter(letter));
  }

  /**
   * Get the current origin of this ply.
   *
   * @return The currently known origin of this ply.
   */
  public final Position getOrigin() {
    return origin;
  }

  /**
   * Set the name of the origin square.
   *
   * @param squareName
   *          The name of the origin square.
   */
  public final void setOrigin(String squareName) {

    // Create a new position implementation from the name and store it.
    origin = new PositionImpl(squareName);
  }

  /**
   * Set the name of the origin square.
   *
   * @param position
   *          The position of the origin square.
   */
  public final void setOrigin(Position position) {

    // Store the position.
    origin = position;
  }

  /**
   * Get the current destination of this ply.
   *
   * @return The currently known destination of this ply.
   */
  public final Position getDestination() {
    return destination;
  }

  /**
   * Set the name of the destination square.
   *
   * @param squareName
   *          The name of the destination square.
   */
  public final void setDestination(String squareName) {

    // Create a new position implementation from the name and store it.
    destination = new PositionImpl(squareName);
  }

  /**
   * Set the flag, if this move captures a piece.
   *
   * @param capture
   *          true, if this move capures a piece.
   */
  public final void setCapture(boolean capture) {
    this.capture = capture;
  }

  /**
   * Check, if this ply fragment is a capture.
   *
   * @return true, if this move is a capture.
   */
  public final boolean isCapture() {
    return capture;
  }

  /**
   * Make this ply a pawn promotion and set the piece type after the promotion.
   *
   * @param newPieceTypeLetter
   *          The piece type after the pawn promotion as a figurine letter code.
   */
  public final void setPawnPromotion(char newPieceTypeLetter) {
    pawnPromotion = true;
    newPieceType = getPieceTypeFromLetter(newPieceTypeLetter);
  }

  /**
   * Check, if this move is a pawn promotion.
   *
   * @return true, if this move is a pawn promotion. False otherwise.
   */
  public final boolean isPawnPromotion() {
    return pawnPromotion;
  }

  /**
   * Get the new piece type after the pawn promotion.
   *
   * @return The new piece type after the pawn promotion.
   */
  public final byte getNewPieceType() {
    return newPieceType;
  }

  /**
   * Set this ply as a castling.
   * 
   * @param isLeftCastling true, if the castling goes to the left.
   */
  public final void setCastling(boolean isLeftCastling) {
    castling = true;
    leftCastling = isLeftCastling;
  }

  /**
   * Check, if this ply fragment is a castling.
   *
   * @return true, if this ply fragment represents a castling. False otherwise.
   */
  public final boolean isCastling() {
    return castling;
  }

  /**
   * Check, if this castling goes to the left.
   *
   * @return true, if this castling goes to the left.
   */
  public final boolean isLeftCastling() {
    return leftCastling;
  }

  /**
   * Get the type of the moved piece.
   *
   * @return The type of the moved piece.
   */
  public final byte getPieceType() {
    return pieceType;
  }

  /**
   * Set the type of the moved piece.
   *
   * @param type
   *          The type of the moved piece.
   */
  private final void setPieceType(byte type) {
    pieceType = type;
  }

  /**
   * Get the piece type from a figurine letter.
   *
   * @param letter
   *          The figurine letter code.
   * @return The piece type or -1, if it was no valid figurine letter code.
   */
  private final byte getPieceTypeFromLetter(char letter) {
    if (letter == 'P') {
      return IPiece.PAWN;
    }
    if (letter == 'N') {
      return IPiece.KNIGHT;
    }
    if (letter == 'B') {
      return IPiece.BISHOP;
    }
    if (letter == 'R') {
      return IPiece.ROOK;
    }
    if (letter == 'Q') {
      return IPiece.QUEEN;
    }
    if (letter == 'K') {
      return IPiece.KING;
    }
    return (byte) -1;
  }
}
