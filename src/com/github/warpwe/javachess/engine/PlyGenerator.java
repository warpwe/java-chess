/*
 * PlyGenerator - A class to generate chess moves from a game position. Copyright (C) 2003 The
 * Java-Chess team <info@java-chess.de> This program is free software; you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option) any later version. This
 * program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details. You should have received a copy of the GNU General Public
 * License along with this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 */

package com.github.warpwe.javachess.engine;

import com.github.warpwe.javachess.bitboard.IBitBoard;
import com.github.warpwe.javachess.engine.hashtable.PlyHashtable;
import com.github.warpwe.javachess.game.IGame;
import com.github.warpwe.javachess.piece.IPiece;
import com.github.warpwe.javachess.ply.AnalyzedPlyImpl;
import com.github.warpwe.javachess.ply.CastlingPlyImpl;
import com.github.warpwe.javachess.ply.EnPassantPlyImpl;
import com.github.warpwe.javachess.ply.IAnalyzedPly;
import com.github.warpwe.javachess.ply.IPly;
import com.github.warpwe.javachess.ply.PlyImpl;
import com.github.warpwe.javachess.ply.TransformationPlyImpl;
import com.github.warpwe.javachess.position.Position;
import com.github.warpwe.javachess.position.PositionImpl;

/**
 * This class generates all possible plies for a given game position.
 */
public class PlyGenerator {

  // Static variables

  // The scores for presorted plies.
  private short HASHTABLE_PLY = 101;
  private short QUEEN_TRANSFORMATION = 100;
  private short ROOK_TRANSFORMATION = 98;
  private short BISHOP_TRANSFORMATION = 96;
  private short KNIGHT_TRANSFORMATION = 94;
  private short MATERIAL_WIN = 50;
  private short REGULAR_PLY = 20;

  // The moves of a knight, of there are no limitations due to position on the board's edge,
  // opponent's pieces etc.
  private static int[][] _knightPlyOffset = {
      {
          -2, -1
      }, {
          -2, 1
      }, {
          -1, -2
      }, {
          -1, 2
      }, {
          1, -2
      }, {
          1, 2
      }, {
          2, -1
      }, {
          2, 1
      }
  };

  // The moves of a king. Also with no limitations due to board size etc.
  private static int[][] _kingPlyOffset = {
      {
          -1, 0
      }, {
          -1, 1
      }, {
          0, 1
      }, {
          1, 1
      }, {
          1, 0
      }, {
          1, -1
      }, {
          0, -1
      }, {
          -1, -1
      }
  };

  // Instance variables

  /**
   * The current game.
   */
  IGame _game;

  /**
   * A hashtable with already computed plies.
   */
  PlyHashtable _hashtable;

  /**
   * The current board.
   */
  IBitBoard _board;

  /**
   * A analyzer to check for chess (needed for castling)
   */
  IBitBoardAnalyzer _analyzer;

  /**
   * An array for the current plies to avoid the overhead of dynamic data structures.
   */
  IAnalyzedPly[] _currentPlies = new AnalyzedPlyImpl[150];

  /**
   * A counter for the currently computed plies.
   */
  private int _plyCounter = 0;

  /**
   * Caches for some important bitmasks.
   */
  long _emptySquares;  // The empty squares.
  long _attackablePieces;  // All the pieces of the opponent minus the king.

  /**
   * The possible knight plies for each square.
   */
  private long[] _knightMask = new long[64];

  /**
   * The possible king plies for each square.
   */
  private long[] _kingMask = new long[64];

  /**
   * A flag to indicate, if we compute the moves for the player with the white pieces.
   */
  private boolean _white;

  // Constructors

  /**
   * Create a new instance of a ply generator.
   *
   * @param game
   *          The current game.
   * @param hashtable
   *          A hashtable to store the best plies so far.
   */
  public PlyGenerator(IGame game, PlyHashtable hashtable) {
    setGame(game);
    setHashtable(hashtable);
    precomputeKnightPlies();
    precomputeKingPlies();
  }

  /**
   * Create a new instance of a ply generator from a given board.
   *
   * @param game
   *          The current game.
   * @param board
   *          The board to operate on.
   * @param hashtable
   *          A hashtable to store the best plies so far.
   */
  public PlyGenerator(IGame game, IBitBoard board, PlyHashtable hashtable) {
    this(game, hashtable);
    setBoard(board);
  }

  // Methods

  /**
   * Get the current game.
   *
   * @return The current game.
   */
  public final IGame getGame() {
    return _game;
  }

  /**
   * Set the current game.
   *
   * @param The
   *          current game.
   */
  public final void setGame(IGame game) {
    _game = game;
  }

  /**
   * Get the current hashtable for this ply generator.
   *
   * @return The current hashtable for this ply generator.
   */
  public final PlyHashtable getHashtable() {
    return _hashtable;
  }

  /**
   * Set a new hashtable for this ply generator.
   *
   * @param hashtable
   *          The new hashtable for this ply generator.
   */
  public final void setHashtable(PlyHashtable hashtable) {
    _hashtable = hashtable;
  }

  /**
   * Get the plies for a given board and color. Passing the last ply is suboptimal, since it's
   * slower than accessing the game history.
   *
   * @param lastPly
   *          The ply, that lead to the given board.
   * @param board
   *          The board with the game position.
   * @param white
   *          true, if white has the next move.
   */
  public final IPly[] getPliesForColor(IBitBoard board, boolean white) {
    setBoard(board);
    return getPliesForColor(white);
  }

  /**
   * Get the plies for a given game position and color.
   *
   * @param white
   *          true, if white has the next move.
   */
  public final IPly[] getPliesForColor(boolean white) {
    resetPlies();
    _white = white;  // Store the color of the current player.

    // Compute some bitmasks, so we don't have to compute them again for each piece type.
    _emptySquares = getBoard().getEmptySquares(); // Get the positions of the empty squares.
    _attackablePieces = getBoard().getAllPiecesForColor(!_white)
        & ~getBoard().getPositionOfPieces(IPiece.KING << 1 | (_white ? 0 : 1));

    // Add the possible plies for all piece types.
    // I tried to sort this list according to the probality for a check, so
    // the analyzer has a better chance to find it early.
    addPliesForKnights();
    addPliesForBishops();
    addPliesForRooks();
    addPliesForQueens();
    addPliesForPawns();
    addPliesForKing();

    // Check, if there's a good ply for this board in the hash table.
    IPly hashtablePly = getHashtable().getPly(getBoard(), _white);
    if (hashtablePly != null) {  // If so, increase the score of this ply.
      for (int index = 0; index < _plyCounter; index++) {
        if (_currentPlies[index].getPly().equals(hashtablePly)) {
          _currentPlies[index].setScore(HASHTABLE_PLY);
          break;
        }
      }
    }

    // Presort the plies
    presortPlies();

    // Convert the plies to a array of the correct size
    IPly[] plies = new IPly[_plyCounter];
    int destIndex = 0;
    for (int sourceIndex = (_plyCounter - 1); sourceIndex >= 0;) {
      plies[destIndex++] = _currentPlies[sourceIndex--].getPly();
    }

    return plies;
  }

  /**
   * Add all the plies for pawns of the current color.
   */
  private final void addPliesForPawns() {

    if (_white) {
      // Get the positions of all pawns
      long pawnPos = getBoard().getPositionOfPieces(IPiece.PAWN << 1 | 1);

      // Add all the diagonal attacks
      addRelativePliesUpward(((pawnPos & IBitBoard._NOT_LINE_H) << 9) & _attackablePieces, 17, 63,
          -9);
      addRelativePliesUpward(((pawnPos & IBitBoard._NOT_LINE_A) << 7) & _attackablePieces, 16, 62,
          -7);

      // Check for a en-passent attack
      if (getLastPly() != null) {
        Position destination = getLastPly().getDestination();
        IPiece piece = getBoard().getPiece(destination);

        // The check for the color is sorta redundant, but during analysis the
        // order of moves might be incorrect.
        if (piece != null && piece.getType() == IPiece.PAWN && piece.getColor() == IPiece.BLACK) {
          int sourceIndex = getLastPly().getSource().getSquareIndex();
          int destinationIndex = getLastPly().getDestination().getSquareIndex();

          // If the pawn moved 2 squares
          if ((sourceIndex - destinationIndex) == 16) {
            int attackableIndex = sourceIndex - 8;

            // Compute the bitmask for the pawn.
            long attackablePawnBitmask = (1L << attackableIndex);

            // Add the en passant attacks.
            if ((((pawnPos & IBitBoard._NOT_LINE_H) << 9) & attackablePawnBitmask) != 0L) {
              addPly(new EnPassantPlyImpl(new PositionImpl(attackableIndex - 9), new PositionImpl(
                  attackableIndex), new PositionImpl(destinationIndex)), MATERIAL_WIN);
            }
            // Add the en passant attacks. W.E 20140728 _NOT_LINE_A
            if ((((pawnPos & IBitBoard._NOT_LINE_A) << 7) & attackablePawnBitmask) != 0L) {
              addPly(new EnPassantPlyImpl(new PositionImpl(attackableIndex - 7), new PositionImpl(
                  attackableIndex), new PositionImpl(destinationIndex)), MATERIAL_WIN);
            }
          }
        }
      }

      // Add all the 2 square plies. Since the square in front of the pawn has to be free, I have to
      // add the bit and with the shifted empty squares.
      addRelativePliesUpward(((pawnPos & IBitBoard._ROW_2 & (_emptySquares >>> 8)) << 16)
          & _emptySquares, 24, 31, -16);

      // Add all the 1 square plies.
      long movedPawns = (pawnPos << 8) & _emptySquares;
      addRelativePliesUpward(movedPawns & IBitBoard._NOT_ROW_8, 16, 55, -8);

      // Now take care of the last row.
      movedPawns &= IBitBoard._ROW_8;
      while (movedPawns != 0L) {
        int destinationSquare = BitUtils.getHighestBit(movedPawns);
        int sourceSquare = destinationSquare - 8;

        // Add all transformation types as plies.

        boolean capture = (((1L << destinationSquare) & _attackablePieces) != 0L);

        addTransformationPly(sourceSquare, destinationSquare, IPiece.QUEEN, capture,
            QUEEN_TRANSFORMATION);
        addTransformationPly(sourceSquare, destinationSquare, IPiece.KNIGHT, capture,
            KNIGHT_TRANSFORMATION);
        addTransformationPly(sourceSquare, destinationSquare, IPiece.ROOK, capture,
            ROOK_TRANSFORMATION);
        addTransformationPly(sourceSquare, destinationSquare, IPiece.BISHOP, capture,
            BISHOP_TRANSFORMATION);

        movedPawns &= ~(1L << destinationSquare);
      }
    }
    else {
      // Get the positions of all pawns
      long pawnPos = getBoard().getPositionOfPieces(IPiece.PAWN << 1);

      // Add all the diagonal attacks
      addRelativePliesDownward(((pawnPos & IBitBoard._NOT_LINE_A) >>> 9) & _attackablePieces, 46,
          0, 9);
      addRelativePliesDownward(((pawnPos & IBitBoard._NOT_LINE_H) >>> 7) & _attackablePieces, 47,
          0, 7);

      // Check for a en-passent attack
      if (getLastPly() != null) {
        Position destination = getLastPly().getDestination();
        IPiece piece = getBoard().getPiece(destination);

        // The check for the color is sorta redundant, but during analysis the
        // order of moves might be incorrect.
        if (piece != null && piece.getType() == IPiece.PAWN && piece.getColor() == IPiece.BLACK) {
          int sourceIndex = getLastPly().getSource().getSquareIndex();
          int destinationIndex = getLastPly().getDestination().getSquareIndex();

          // If the pawn moved 2 squares
          if ((destinationIndex - sourceIndex) == 16) {
            int attackableIndex = sourceIndex + 8;

            // Compute the bitmask for the pawn.
            long attackablePawnBitmask = (1L << attackableIndex);

            // Add the en passant attacks. W.E. 20140728
            if ((((pawnPos & IBitBoard._NOT_LINE_A) >>> 9) & attackablePawnBitmask) != 0L) {
              addPly(new EnPassantPlyImpl(new PositionImpl(attackableIndex + 9), new PositionImpl(
                  attackableIndex), new PositionImpl(destinationIndex)), MATERIAL_WIN);
            }
            // Add the en passant attacks.
            if ((((pawnPos & IBitBoard._NOT_LINE_H) >>> 7) & attackablePawnBitmask) != 0L) {
              addPly(new EnPassantPlyImpl(new PositionImpl(attackableIndex + 7), new PositionImpl(
                  attackableIndex), new PositionImpl(destinationIndex)), MATERIAL_WIN);
            }
          }
        }
      }

      // Add all the 2 square plies. Since the square in front of the pawn has to be free, I have to
      // add the bit and with the shifted empty squares.
      addRelativePliesDownward(((pawnPos & IBitBoard._ROW_7 & (_emptySquares << 8)) >>> 16)
          & _emptySquares, 39, 32, 16);

      // Add all the 1 square plies.
      long movedPawns = (pawnPos >>> 8) & _emptySquares;
      addRelativePliesDownward(movedPawns & IBitBoard._NOT_ROW_1, 56, 8, 8);

      // Now take care of the last row.
      movedPawns &= IBitBoard._ROW_1;
      while (movedPawns != 0L) {
        int destinationSquare = BitUtils.getHighestBit(movedPawns);
        int sourceSquare = destinationSquare + 8;

        // Add all transformation types as plies.

        boolean capture = (((1L << destinationSquare) & _attackablePieces) != 0L);

        addTransformationPly(sourceSquare, destinationSquare, IPiece.QUEEN, capture,
            QUEEN_TRANSFORMATION);
        addTransformationPly(sourceSquare, destinationSquare, IPiece.KNIGHT, capture,
            KNIGHT_TRANSFORMATION);
        addTransformationPly(sourceSquare, destinationSquare, IPiece.ROOK, capture,
            ROOK_TRANSFORMATION);
        addTransformationPly(sourceSquare, destinationSquare, IPiece.BISHOP, capture,
            BISHOP_TRANSFORMATION);

        movedPawns &= ~(1L << destinationSquare);
      }
    }
  }

  /**
   * Compute the knight plies for each square. This is done at startup, so we can get the plies by a
   * simple array access.
   */
  private final void precomputeKnightPlies() {
    for (int i = 0; i < 64; i++) {
      long currentMask = 0L;
      int line = i & 7;
      int row = i >> 3;
      for (int currentPlyIndex = 0; currentPlyIndex < _knightPlyOffset.length; currentPlyIndex++) {
        int[] curOffsets = _knightPlyOffset[currentPlyIndex];
        int targetRow = row + curOffsets[1];
        int targetLine = line + curOffsets[0];
        if (targetRow >= 0 && targetRow < 8 && targetLine >= 0 && targetLine < 8) {
          currentMask |= 1L << ((targetRow << 3) + targetLine);
        }
      }
      _knightMask[i] = currentMask;
    }
  }

  /**
   * Compute the king moves for each square in advance.
   */
  private final void precomputeKingPlies() {
    for (int i = 0; i < 64; i++) {
      long currentMask = 0L;
      int line = i & 7;
      int row = i >> 3;
      for (int currentPlyIndex = 0; currentPlyIndex < _kingPlyOffset.length; currentPlyIndex++) {
        int[] curOffsets = _kingPlyOffset[currentPlyIndex];
        int targetRow = row + curOffsets[1];
        int targetLine = line + curOffsets[0];
        if (targetRow >= 0 && targetRow < 8 && targetLine >= 0 && targetLine < 8) {
          currentMask |= 1L << ((targetRow << 3) + targetLine);
        }
      }
      _kingMask[i] = currentMask;
      // @Testdisplay
      // System.out.println(Long.toHexString(_kingMask[i]));
    }
  }

  /**
   * Add all the plies for knights of the current color.
   */
  private final void addPliesForKnights() {
    long knightPositions = getBoard().getPositionOfPieces(
        _white ? IPiece.KNIGHT << 1 | 1 : IPiece.KNIGHT << 1);

    while (knightPositions != 0) {
      int highestBit = BitUtils.getHighestBit(knightPositions);
      long curMoves = _knightMask[highestBit] & (_emptySquares | _attackablePieces);
      int startBitRange = highestBit - 17;
      if (startBitRange < 0) {
        startBitRange = 0;
      }
      int endBitRange = highestBit + 17;
      if (endBitRange > 63) {
        endBitRange = 63;
      }
      addAbsolutePlies(curMoves, startBitRange, endBitRange, highestBit);
      knightPositions &= ~(1L << highestBit);
    }
  }

  /**
   * Add the plies for bishops.
   */
  public final void addPliesForBishops() {
    long bishopPositions = getBoard().getPositionOfPieces(
        _white ? IPiece.BISHOP << 1 | 1 : IPiece.BISHOP << 1);

    while (bishopPositions != 0) {
      int highestBit = BitUtils.getHighestBit(bishopPositions);
      addPliesForBishopPos(highestBit);
      bishopPositions &= ~(1L << highestBit);
    }
  }

  /**
   * Add the plies for a bishop position.
   *
   * @param square
   *          The square index of the bishop pos.
   */
  private final void addPliesForBishopPos(int square) {

    int orgPos = square;  // Save the original position.
    int squareRow = square >> 3;
    int squareLine = square & 7;
    long bitmask;

    if (squareRow > 0) {  // If we are not on row 1
      if (squareLine < 7) {
        // Compute plies to the lower right.
        square -= 7;
        bitmask = 1L << square;
        while (square >= 0 && ((square & 7) > 0)) {
          if ((bitmask & (_emptySquares | _attackablePieces)) != 0L) {
            boolean capture = ((bitmask & _attackablePieces) != 0L);

            addRegularPly(orgPos, square, capture, capture ? MATERIAL_WIN : REGULAR_PLY);

            // If we attacked a piece, we cannot move further
            if (capture) {
              break;
            }
          }
          else {
            break;
          }
          square -= 7;
          bitmask >>>= 7;
        }
      }

      if (squareLine > 0) {
        // Compute plies to the lower left.
        square = orgPos - 9;
        bitmask = 1L << square;
        while (square >= 0 && ((square & 7) != 7)) {
          if ((bitmask & (_emptySquares | _attackablePieces)) != 0L) {
            boolean capture = ((bitmask & _attackablePieces) != 0L);

            addRegularPly(orgPos, square, capture, capture ? MATERIAL_WIN : REGULAR_PLY);

            // If we attacked a piece, we cannot move further
            if (capture) {
              break;
            }
          }
          else {
            break;
          }
          square -= 9;
          bitmask >>>= 9;
        }
      }
    }

    if (squareRow < 7) {
      if (squareLine > 0) {
        // Compute plies to the upper left.
        square = orgPos + 7;
        bitmask = 1L << square;
        while (square < 64 && ((square & 7) != 7)) {
          if ((bitmask & (_emptySquares | _attackablePieces)) != 0L) {
            boolean capture = ((bitmask & _attackablePieces) != 0L);

            addRegularPly(orgPos, square, capture, capture ? MATERIAL_WIN : REGULAR_PLY);

            // If we attacked a piece, we cannot move further
            if (capture) {
              break;
            }
          }
          else {
            break;
          }
          square += 7;
          bitmask <<= 7;
        }
      }

      if (squareLine < 7) {
        // Compute plies to the upper right.
        square = orgPos + 9;
        bitmask = 1L << square;
        while (square < 64 && ((square & 7) > 0)) {
          if ((bitmask & (_emptySquares | _attackablePieces)) != 0L) {
            boolean capture = ((bitmask & _attackablePieces) != 0L);

            addRegularPly(orgPos, square, capture, capture ? MATERIAL_WIN : REGULAR_PLY);

            // If we attacked a piece, we cannot move further
            if (capture) {
              break;
            }
          }
          else {
            break;
          }
          square += 9;
          bitmask <<= 9;
        }
      }
    }
  }

  /**
   * Add the plies for rooks.
   */
  public final void addPliesForRooks() {
    long rookPositions = getBoard().getPositionOfPieces(
        _white ? IPiece.ROOK << 1 | 1 : IPiece.ROOK << 1);

    while (rookPositions != 0) {
      int highestBit = BitUtils.getHighestBit(rookPositions);
      addPliesForRookPos(highestBit);
      rookPositions &= ~(1L << highestBit);
    }
  }

  /**
   * Add the plies for one rook position.
   *
   * @param square
   *          The square index of the rook pos.
   */
  private final void addPliesForRookPos(int square) {

    int orgPos = square;  // Save the original position.
    long bitmask;

    // Compute plies to the left.
    bitmask = 1L << square;
    while ((square & 7) > 0) {
      square -= 1;
      bitmask >>>= 1;
      if ((bitmask & (_emptySquares | _attackablePieces)) != 0) {
        boolean capture = ((bitmask & _attackablePieces) != 0L);

        addRegularPly(orgPos, square, capture, capture ? MATERIAL_WIN : REGULAR_PLY);

        // If we attacked a piece, we cannot move further
        if (capture) {
          break;
        }
      }
      else {
        break;
      }
    }

    // Compute plies downwards.
    square = orgPos;
    bitmask = 1L << square;
    // @Bugfix Korrektur aus Sourceforge Version
    // alt: while( square > 8) {
    while (square > 7) {
      square -= 8;
      bitmask >>>= 8;
      if ((bitmask & (_emptySquares | _attackablePieces)) != 0) {
        boolean capture = ((bitmask & _attackablePieces) != 0L);

        addRegularPly(orgPos, square, capture, capture ? MATERIAL_WIN : REGULAR_PLY);

        // If we attacked a piece, we cannot move further
        if (capture) {
          break;
        }
      }
      else {
        break;
      }
    }

    // Compute plies to the right.
    square = orgPos;
    bitmask = 1L << square;
    while ((square & 7) < 7) {
      square += 1;
      bitmask <<= 1;
      if ((bitmask & (_emptySquares | _attackablePieces)) != 0) {
        boolean capture = ((bitmask & _attackablePieces) != 0L);

        addRegularPly(orgPos, square, capture, capture ? MATERIAL_WIN : REGULAR_PLY);

        // If we attacked a piece, we cannot move further
        if (capture) {
          break;
        }
      }
      else {
        break;
      }
    }

    // Compute plies upwards.
    square = orgPos;
    bitmask = 1L << square;
    while (square < 56) {
      square += 8;
      bitmask <<= 8;
      if ((bitmask & (_emptySquares | _attackablePieces)) != 0) {
        boolean capture = ((bitmask & _attackablePieces) != 0L);

        addRegularPly(orgPos, square, capture, capture ? MATERIAL_WIN : REGULAR_PLY);

        // If we attacked a piece, we cannot move further
        if (capture) {
          break;
        }
      }
      else {
        break;
      }
    }
  }

  /**
   * Add the plies for queens.
   */
  public final void addPliesForQueens() {
    long queenPositions = getBoard().getPositionOfPieces(
        _white ? IPiece.QUEEN << 1 | 1 : IPiece.QUEEN << 1);

    while (queenPositions != 0) {
      int highestBit = BitUtils.getHighestBit(queenPositions);
      addPliesForQueenPos(highestBit);
      queenPositions &= ~(1L << highestBit);
    }
  }

  /**
   * Add the plies for one queen position.
   *
   * @param square
   *          The square index of the queen position.
   */
  private final void addPliesForQueenPos(int square) {

    // Since a queen can move like a bishop or rook,
    // I simply add the plies of both for this position.
    addPliesForBishopPos(square);
    addPliesForRookPos(square);
  }

  /**
   * Add all the plies for the king of the current color.
   */
  private final void addPliesForKing() {
    long opponentKingPosition = getBoard().getPositionOfPieces(
        _white ? IPiece.KING << 1 : IPiece.KING << 1 | 1);
    int highestBit = BitUtils.getHighestBit(getBoard().getPositionOfPieces(
        _white ? (IPiece.KING << 1) + 1 : IPiece.KING << 1));
    long restrictedSquares = _kingMask[BitUtils.getHighestBit(opponentKingPosition)];
    long curMoves = _kingMask[highestBit] & (_emptySquares | _attackablePieces)
        & ~restrictedSquares;
    // @Testdisplay nur fuer den schwarzen Koenig
    // if (_white == false) {
    // GameStatus work = getLastGameStatus();
    // System.out.println(getLastPly() + " Restricted Squares: " +
    // Long.toHexString(restrictedSquares) + " curMoves: " + Long.toHexString(curMoves));
    // System.out.println("Layer[0]" + Long.toHexString(getBoard().getLayer0()));
    // System.out.println("Layer[1]" + Long.toHexString(getBoard().getLayer1()));
    // System.out.println("Layer[2]" + Long.toHexString(getBoard().getLayer2()));
    // System.out.println("Layer[3]" + Long.toHexString(getBoard().getLayer3()));
    // }
    int startBitRange = highestBit - 9;
    if (startBitRange < 0) {
      startBitRange = 0;
    }
    int endBitRange = highestBit + 9;
    if (endBitRange > 63) {
      endBitRange = 63;
    }
    addAbsolutePlies(curMoves, startBitRange, endBitRange, highestBit);

    // Check for castling
    if (_white) {
      // If the king has not been moved and is not in check
      if (!getGame().hasBeenMoved(new PositionImpl(4))
          && !getAnalyzer().isInCheck(getBoard(), true)) {
        long rookPositions = getBoard().getPositionOfPieces((IPiece.ROOK << 1) + 1);
        if (!getGame().hasBeenMoved(new PositionImpl(0))
            && ((rookPositions & 0x1L) == 0x1L)  // steht da �berhaupt ein Rook?
            && ((_emptySquares & 0xEL) == 0xEL)
            && (!getAnalyzer().isInCheck(
                (IBitBoard) (getBoard().getBoardAfterPly(new PlyImpl(new PositionImpl(4),
                    new PositionImpl(3), false))), true)
            /*
             * && !getAnalyzer().isInCheck( (BitBoard)(getBoard().getBoardAfterPly( new PlyImpl( new
             * PositionImpl(4), new PositionImpl(2)))), true)
             */)) {  // The addPly method checks for this check anyway, so it can be outcommented
                    // here...
          addCastlingPly(4, true);
        }
        if (!getGame().hasBeenMoved(new PositionImpl(7))
            && ((rookPositions & 0x80L) == 0x80L)  // steht da �berhaupt ein Rook?
            && ((_emptySquares & 0x60L) == 0x60L)
            && (!getAnalyzer().isInCheck(
                (IBitBoard) (getBoard().getBoardAfterPly(new PlyImpl(new PositionImpl(4),
                    new PositionImpl(5), false))), true)
            /*
             * && !getAnalyzer().isInCheck( (BitBoard)(getBoard().getBoardAfterPly( new PlyImpl( new
             * PositionImpl(4), new PositionImpl(6)))), true)
             */)) {
          addCastlingPly(4, false);
        }
      }
    }
    else {
      if (!getGame().hasBeenMoved(new PositionImpl(60))
          && !getAnalyzer().isInCheck(getBoard(), false)) {
        long rookPositions = getBoard().getPositionOfPieces(IPiece.ROOK << 1);
        if (!getGame().hasBeenMoved(new PositionImpl(56))
            && ((rookPositions & (0x1L << 56)) == (0x1L << 56))  // steht da �berhaupt ein Rook?
            && ((_emptySquares & (0xEL << 56)) == (0xEL << 56))
            && (!getAnalyzer().isInCheck(
                (IBitBoard) (getBoard().getBoardAfterPly(new PlyImpl(new PositionImpl(60),
                    new PositionImpl(59), false))), false)
            /*
             * && !getAnalyzer().isInCheck( (BitBoard)(getBoard().getBoardAfterPly( new PlyImpl( new
             * PositionImpl(60), new PositionImpl(58)))), false)
             */)) {
          addCastlingPly(4 + 56, true);
        }
        if (!getGame().hasBeenMoved(new PositionImpl(63))
            && ((rookPositions & (0x1L << 63)) == (0x1L << 63))  // steht da ueberhaupt ein Rook?
            && ((_emptySquares & (0x60L << 56)) == (0x60L << 56))
            && (!getAnalyzer().isInCheck(
                (IBitBoard) (getBoard().getBoardAfterPly(new PlyImpl(new PositionImpl(60),
                    new PositionImpl(61), false))), false)
            /*
             * && !getAnalyzer().isInCheck( (BitBoard)(getBoard().getBoardAfterPly( new PlyImpl( new
             * PositionImpl(60), new PositionImpl(62)))), false)
             */)) {
          addCastlingPly(4 + 56, false);
        }
      }
    }
  }

  /**
   * Convert a bitmask to a series of relative plies. We pass the destination fields here along with
   * a offset to compute the source square.
   *
   * @param destinationPos
   *          The destination positions for all the plies.
   * @param int startBit The bit to start scanning from.
   * @param int endBit The bit to end the scanning at. int offset The offset for each ply.
   */
  private final void addRelativePliesUpward(long destinationPos, int startBit, int endBit,
      int offset) {

    // Compute a bitmask to mask the bits in the destination bitfield.
    long bitmask = 1L << startBit;

    // End the loop also if the bitmask is 0, so there are no more destination fields
    // in the bitmask (so we don't have to check the entire chessboard for plies).
    for (int currentBit = startBit; currentBit <= endBit && destinationPos != 0; currentBit++) {

      // If the bit of the destination square is set, it's the destination for a ply.
      if ((destinationPos & bitmask) != 0) {

        // Add the ply to the buffer.
        boolean capture = ((bitmask & _attackablePieces) != 0L);

        addRegularPly(currentBit + offset, currentBit, capture, capture ? MATERIAL_WIN
            : REGULAR_PLY);

        // Remove the bit, so we can check if there are more plies to find.
        destinationPos &= ~bitmask;
      }
      bitmask <<= 1;
    }
  }

  /**
   * Convert a bitmask to a series of relative plies.
   *
   * @param destinationPos
   *          The destination positions for all the plies.
   * @param int startBit The bit to start scanning from.
   * @param int endBit The bit to end the scanning at. int offset The offset for each ply.
   */
  private final void addRelativePliesDownward(long destinationPos, int startBit, int endBit,
      int offset) {

    long bitmask = 1L << startBit;
    for (int currentBit = startBit; currentBit >= endBit && destinationPos != 0; currentBit--) {
      if ((destinationPos & bitmask) != 0) {
        boolean capture = ((bitmask & _attackablePieces) != 0L);

        addRegularPly(currentBit + offset, currentBit, capture, capture ? MATERIAL_WIN
            : REGULAR_PLY);
        destinationPos &= ~bitmask;
      }
      bitmask >>>= 1;
    }
  }

  /**
   * Convert a bitmask to a series of absolute plies.
   *
   * @param destinationPos
   *          The destination positions for all the plies.
   * @param int startBit The bit to start scanning from.
   * @param int endBit The bit to end the scanning at. int pos The source for each ply.
   */
  private final void addAbsolutePlies(long destinationPos, int startBit, int endBit, int pos) {

    long bitmask = 1L << startBit;
    for (int currentBit = startBit; currentBit <= endBit && destinationPos != 0; currentBit++) {
      if ((destinationPos & bitmask) != 0) {
        boolean capture = ((bitmask & _attackablePieces) != 0L);

        addRegularPly(pos, currentBit, capture, capture ? MATERIAL_WIN : REGULAR_PLY);
        destinationPos &= ~bitmask;
      }
      bitmask <<= 1;
    }
  }

  /**
   * Get the current board.
   *
   * @return The current board.
   */
  final IBitBoard getBoard() {
    return _board;
  }

  /**
   * Set a new board.
   *
   * @param board
   *          The new board.
   */
  final void setBoard(IBitBoard board) {
    _board = board;
  }

  /**
   * Get a analyzer for check test.
   *
   * @return A analyzer for chess boards.
   */
  final IBitBoardAnalyzer getAnalyzer() {
    return _analyzer;
  }

  /**
   * Get the last ply.
   *
   * @return The last ply.
   */
  final IPly getLastPly() {
    return getGame().getLastPly();
    // return _lastPly;
  }

  /**
   * Set the last ply.
   *
   * @param lastPly
   *          The last ply.
   */
  /*
   * final void setLastPly( Ply lastPly) { // _lastPly = lastPly; }
   */

  /**
   * Set a analyzer for check tests.
   *
   * @param analyzer
   *          The new analyzer to set.
   */
  public final void setAnalyzer(IBitBoardAnalyzer analyzer) {
    _analyzer = analyzer;
  }

  /**
   * Reset the ply buffer.
   */
  private final void resetPlies() {
    _plyCounter = 0;
  }

  /**
   * Add a ply to the buffer, if the own king is not in check after the ply.
   *
   * @param ply
   *          The ply to add.
   * @param score
   *          The presort score of this ply.
   */
  private final void addPly(IPly ply, short score) {

    // @Testdisplay
    // System.out.println(_plyCounter + " " + ply + " " + score);
    // Test if the own king is in check, before adding the ply
    if (!getAnalyzer().isInCheck((IBitBoard) (getBoard().getBoardAfterPly(ply)), _white)) {
      _currentPlies[_plyCounter++] = new AnalyzedPlyImpl(ply, score);
    }
  }

  /**
   * Add a regular ply.
   *
   * @param source
   *          The source square.
   * @param destination
   *          The destination square.
   * @param capture
   *          Flag to indicate if another piece is captured.
   * @param score
   *          The presort score of this ply.
   */
  private final void addRegularPly(int source, int destination, boolean capture, short score) {
    addPly(new PlyImpl(new PositionImpl(source), new PositionImpl(destination), capture), score);
  }

  /**
   * Add a castling ply.
   *
   * @param source
   *          The position square of the king.
   * @param goesLeft
   *          The flag that indicates if the castling goes to the left.
   */
  private final void addCastlingPly(int source, boolean goesLeft) {
    addPly(new CastlingPlyImpl(new PositionImpl(source), goesLeft), REGULAR_PLY);
  }

  /**
   * Add a transformation ply.
   *
   * @param source
   *          The source square of the old piece.
   * @param destination
   *          The destination square of the piece.
   * @param pieceType
   *          The new piece type after the ply.
   * @param capture
   *          Flag to indicate if another piece is captured.
   * @param score
   *          The presort score of this ply.
   */
  private final void addTransformationPly(int source, int destination, byte pieceType,
      boolean capture, short score) {
    addPly(new TransformationPlyImpl(new PositionImpl(source), new PositionImpl(destination),
        pieceType, capture), score);
  }

  /**
   * Get the knight plies for a given knight square.
   *
   * @param square
   *          The square, where the knight is located.
   * @return All the knight plies as a 64 bit bitmask.
   */
  public final long getKnightPlies(int square) {
    return _knightMask[square];
  }

  /**
   * Presort the scored plies.
   */
  private final void presortPlies() {
    if (_plyCounter > 1) {  // Check the array size, so the sort methods don't have to do it.
      quickersort(0, _plyCounter - 1);
    }
  }

  /**
   * Perform a combined quick-/shakersort on a ply partition.
   *
   * @param l
   *          The left bound of the array partition.
   * @param r
   *          The right bound of the array partition.
   */
  private final void quickersort(int l, int r) {
    int i = l, j = r;
    IAnalyzedPly x = _currentPlies[(l + r) / 2];
    do {
      while (_currentPlies[i].getScore() > x.getScore()) {
        i++;
      }
      while (x.getScore() > _currentPlies[j].getScore()) {
        j--;
      }
      if (i <= j) {
        IAnalyzedPly w = _currentPlies[i];
        _currentPlies[i++] = _currentPlies[j];
        _currentPlies[j--] = w;
      }

    }
    while (i <= j);
    if (l < j) {
      if ((l - j) > 7) {
        quickersort(l, j);
      }
      else {
        shakersort(l, j);
      }
    }
    if (i < r) {
      if ((r - i) > 7) {
        quickersort(i, r);
      }
      else {
        shakersort(i, r);
      }
    }
  }

  /**
   * Perform a shakersort on a ply partition.
   *
   * @param l
   *          The left bound of the ply partition.
   * @param r
   *          The right bound of the ply partition.
   */
  private final void shakersort(int l, int r) {
    int i = l + 1, j = r, k = r;

    do {
      for (int h = j; h >= i; h--) {
        if (_currentPlies[h - 1].getScore() < _currentPlies[h].getScore()) {
          IAnalyzedPly w = _currentPlies[h - 1];
          _currentPlies[h - 1] = _currentPlies[h];
          _currentPlies[h] = w;
          k = h;
        }
      }
      i = k + 1;
      for (int h = i; h <= j; h++) {
        if (_currentPlies[h - 1].getScore() < _currentPlies[h].getScore()) {
          IAnalyzedPly w = _currentPlies[h - 1];
          _currentPlies[h - 1] = _currentPlies[h];
          _currentPlies[h] = w;
          k = h;
        }
      }
      j = k - 1;
    }
    while (i <= j);
  }
}
