/*
 * PlyHashtableEntryImpl - Implements a hashtable entry for plies. Copyright (C) 2003 The Java-Chess
 * team <info@java-chess.de> This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free Software Foundation;
 * either version 2 of the License, or (at your option) any later version. This program is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details. You should have received a copy of the GNU General Public
 * License along with this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 */

package com.github.warpwe.javachess.engine.hashtable;

import com.github.warpwe.javachess.bitboard.IBitBoard;
import com.github.warpwe.javachess.board.Board;
import com.github.warpwe.javachess.piece.IPiece;
import com.github.warpwe.javachess.ply.IPly;

/**
 * The class implements the functionality to store chess plies in a hashtable.
 */
public class PlyHashtableEntryImpl implements PlyHashtableEntry {

  // Instance variables

  /**
   * The board before this ply.
   */
  private Board _board;

  /**
   * The ply to store.
   */
  private IPly _ply;

  /**
   * The search depth, that was used to compute this ply.
   */
  private int _searchDepth;

  // Constructors

  /**
   * Create a new hashtable entry.
   *
   * @param board
   *          The board before the ply.
   * @param ply
   *          The ply to store.
   * @param searchDepth
   *          The search depth, that was used to compute the ply.
   */
  public PlyHashtableEntryImpl(Board board, IPly ply, int searchDepth) {
    setBoard(board);
    setPly(ply);
    setSearchDepth(searchDepth);
  }

  // Methods

  /**
   * Get the board of this entry.
   *
   * @return The board of this entry.
   */
  public final Board getBoard() {
    return _board;
  }

  /**
   * Set a new board for this entry.
   *
   * @param board
   *          The new board.
   */
  public final void setBoard(Board board) {
    _board = board;
  }

  /**
   * Get the ply.
   *
   * @return The ply.
   */
  public final IPly getPly() {
    return _ply;
  }

  /**
   * Set the ply.
   *
   * @param ply
   *          The ply to set.
   */
  public final void setPly(IPly ply) {
    _ply = ply;
  }

  /**
   * Get the search depth for the computed ply.
   *
   * @return The search depth for the computed ply.
   */
  public final int getSearchDepth() {
    return _searchDepth;
  }

  /**
   * Set the search depth for the computed ply.
   *
   * @param searchDepth
   *          The search depth.
   */
  public final void setSearchDepth(int searchDepth) {
    _searchDepth = searchDepth;
  }

  /**
   * Check, if it's a move with white pieces.
   *
   * @return true, if it's a move with white pieces.
   */
  public final boolean isWhiteMove() {
    return getBoard().getPiece(getPly().getSource()).getColor() == IPiece.WHITE;
  }

  /**
   * Get the hashcode for this ply.
   *
   * @return A hashcode for this ply.
   */
  public final long hashKey() {
    return hashKey(getBoard(), isWhiteMove());
  }

  /**
   * Get a hashcode for a given board and piece color.
   *
   * @param board
   *          The current board.
   * @param white
   *          true, if it's a ply with the white pieces.
   * @return A hashcode for the given board and color.
   */
  public static long hashKey(Board board, boolean white) {
    return ZobristKeyImpl.getInstance().computeKey((IBitBoard) board, white);
  }
}
