/*
 * PlyNotationImpl - A class to notate a ply. Copyright (C) 2003 The Java-Chess team
 * <info@java-chess.de> This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free Software Foundation;
 * either version 2 of the License, or (at your option) any later version. This program is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details. You should have received a copy of the GNU General Public
 * License along with this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 */

package com.github.warpwe.javachess.notation;

import com.github.warpwe.javachess.piece.IPiece;
import com.github.warpwe.javachess.ply.ICastlingPly;
import com.github.warpwe.javachess.ply.IPly;
import com.github.warpwe.javachess.ply.ITransformationPly;

/**
 * This class holds all the methods to render a ply as a string notation.
 */
public class PlyNotationImpl implements IPlyNotation {

  // Instance variables.

  /**
   * The ply.
   */
  private IPly ply;

  /**
   * The moved piece.
   */
  private IPiece piece;

  /**
   * Flag to indicate, if this ply sets the opponent in check.
   */
  private boolean check;

  /**
   * Flag to indicate, if the opponent is checkmate after this ply.
   */
  private boolean checkMate;

  /**
   * A cache for the string representation.
   */
  private String stringRepresentation;

  // Constructors

  /**
   * Create a new ply notation instance.
   *
   * @param ply
   *          The ply to render.
   * @param piece
   *          The moved piece.
   */
  public PlyNotationImpl(IPly ply, IPiece piece) {
    setPly(ply);
    setPiece(piece);

    // Render the string representation only once and not during
    // the toString() call.
    computeStringRepresentation();
  }

  // Methods

  /**
   * Render this notation as a string.
   *
   * @return A string representation of this ply.
   */
  public String toString() {
    return stringRepresentation;
  }

  /**
   * Render this ply as a string.
   */
  private final void computeStringRepresentation() {
    StringBuffer notation = new StringBuffer();

    if (!(getPly() instanceof ICastlingPly)) {

      // Start with the type of the piece, if it is not a pawn.
      if (getPiece().getType() != IPiece.PAWN) {
        notation.append(getPieceTypeNotation(getPiece().getType()));
      }

      // Get this ply as a string.
      String plyString = getPly().toString();

      // If it's a capture, indicate it with an 'x'
      notation.append(isCapture() ? plyString.replace('-', 'x') : plyString);

      // If it's a pawn transforming to a new piece type, append the new type.
      if (getPly() instanceof ITransformationPly) {
        notation.append("=");
        notation.append(getPieceTypeNotation(((ITransformationPly) getPly())
            .getTypeAfterTransformation()));
      }
    }
    else {
      notation.append(getPly().toString());
    }

    // Add the check(-mate) signs.
    if (isCheckMate()) {
      notation.append('#');
    }
    else {
      if (isCheck()) {
        notation.append('+');
      }
    }

    // Convert the buffer to a string and store it.
    this.stringRepresentation = notation.toString();
  }

  /**
   * Get the piece of this ply.
   *
   * @return The piece, that is moved with this ply.
   */
  public final IPiece getPiece() {
    return piece;
  }

  /**
   * Set the piece, that is moved with this ply.
   *
   * @param piece
   *          The moved ply.
   */
  public final void setPiece(IPiece piece) {
    this.piece = piece;
  }

  /**
   * Get the ply of this notation.
   *
   * @return The ply for this notation.
   */
  public final IPly getPly() {
    return ply;
  }

  /**
   * Set the ply for this notation.
   *
   * @param ply
   *          The ply for this notation.
   */
  public final void setPly(IPly ply) {
    this.ply = ply;
  }

  /**
   * Check if the piece captures another piece with this ply.
   *
   * @return true, if another piece is captureed with this ply.
   */
  public final boolean isCapture() {
    return getPly().isCapture();
  }

  /**
   * Get the flag for a check.
   *
   * @return true, if the opponent is in check.
   */
  public final boolean isCheck() {
    return check;
  }

  /**
   * Set the flag for a check.
   *
   * @param check
   *          , true if the oppenent is in check.
   */
  public final void setCheck(boolean check) {
    this.check = check;

    // If we are not in check, we are also no checkmate.
    if (check == false) {
      setCheckMate(false);
    }

    // Recompute the string representation to make sure, that
    // it is still correct.
    computeStringRepresentation();
  }

  /**
   * Get the flag for a checkmate.
   *
   * @return true, if the opponent is checkmate.
   */
  public final boolean isCheckMate() {
    return checkMate;
  }

  /**
   * Set the flag for a checkmate.
   *
   * @param checkMate
   *          true if the oppenent is checkmate.
   */
  public final void setCheckMate(boolean checkMate) {
    this.checkMate = checkMate;

    // If the opponent is checkmate, he is also in check.
    if (checkMate == true) {
      setCheck(true);
    }
  }

  /**
   * Get the notation for a piece type.
   *
   * @return The notation for a piece type.
   */
  public final String getPieceTypeNotation(byte pieceType) {

    String notation = "";  // The result;

    // Convert the piece type to a string.
    switch (pieceType) {
      case IPiece.PAWN:
        notation = "P";
        break;
      case IPiece.KNIGHT:
        notation = "N";
        break;
      case IPiece.BISHOP:
        notation = "B";
        break;
      case IPiece.ROOK:
        notation = "R";
        break;
      case IPiece.QUEEN:
        notation = "Q";
        break;
      case IPiece.KING:
        notation = "K";
        break;
    }
    return notation;
  }
}
