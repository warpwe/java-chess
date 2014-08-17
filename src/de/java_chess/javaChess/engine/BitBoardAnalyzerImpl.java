/*
 * BitBoardAnalyzerImpl - A class to analyze the game position on a BitBoard type chessbaord.
 * Copyright (C) 2003 The Java-Chess team <info@java-chess.de> This program is free software; you
 * can redistribute it and/or modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of the License, or (at your option)
 * any later version. This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU General Public License for more details. You should have received a copy of
 * the GNU General Public License along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */

package de.java_chess.javaChess.engine;

import org.apache.log4j.Logger;

import de.java_chess.javaChess.bitboard.BitBoard;
import de.java_chess.javaChess.game.Game;
import de.java_chess.javaChess.piece.Piece;
import de.java_chess.javaChess.position.Position;
import de.java_chess.javaChess.position.PositionImpl;

/**
 * The class implements the functionality to analyze a game position, stored as a bitboard.
 */
public class BitBoardAnalyzerImpl implements IBitBoardAnalyzer {

  // Static variables

  // A minus for pieces that block the development of the own pawns.
  static final short _pawnBlocker = 10;
  
  static final Logger logger = Logger.getLogger("logfile");

  // The position value of pawns on all the squares, in the
  // opening stage and in the middle game.
  static final short[][] _pawnPositionalValue = {
      {
          0, 0, 0, 0, 0, 0, 0,
          0, // Opening stage
          0, 0, 3, 6, 6, 8, 8, 8, 0, 0, 2, 7, 7, 6, 6, 0, 0, 0, 1, 8, 8, 4, 4, 0, 0, 0, 8, 10, 10,
          2, 2, 0, 0, 0, 8, 12, 12, 0, 0, 0, 0, 0, 9, 13, 13, 0, 0, 0, 0, 0, 8, 8, 8, 0, 0, 0
      },
      {
          0, 0, 0, 0, 0, 0, 0,
          0, // Middle game
          0, 0, 0, 0, 0, 0, 0, 0, 2, 3, 4, 5, 5, 4, 3, 2, 3, 4, 5, 6, 6, 5, 4, 3, 4, 5, 6, 7, 7, 6,
          5, 4, 5, 6, 7, 8, 8, 7, 6, 5, 6, 7, 8, 9, 9, 8, 8, 7, 7, 8, 9, 10, 10, 9, 8, 7
      }
  };

  // The positional value of knights on the various squares.
  static final short[][] _knightPositionalValue = {
      {
          0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0,
          0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
          0, 0, 0, 0
      },
      {
          0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 5, 5, 1, 0, 0, 1, 3, 5, 6, 6, 5,
          3, 1, 1, 3, 5, 6, 6, 5, 3, 1, 0, 0, 1, 6, 6, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
          0, 0, 0, 0
      }
  };

  // The positional values of bishops on the various squares.
  static final short[][] _bishopPositionalValue = {
      {
          0, 0, 0, 0, 0, 0, 0,
          0, // Opening stage
          0, 4, 0, 1, 1, 0, 4, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
          0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
      },
      {
          0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 4, 0, 0, 3, 5, 3, 3, 5, 3, 0, 0, 0, 0, 6, 6, 0,
          0, 0, 0, 0, 0, 6, 6, 0, 0, 0, 0, 3, 5, 3, 3, 5, 3, 0, 0, 4, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0,
          0, 0, 0, 0
      }
  };

  // The positional value of rooks on the various squares.
  static final short[][] _rookPositionalValue = {
      {
          1, 1, 5, 5, 5, 5, 1,
          1, // Opening stage
          1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
          0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
      },
      {
          0, 0, 0, 0, 0, 0, 0,
          0, // Middle game
          0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 5, 5, 1, 0, 0, 1, 3, 5, 6, 6, 5, 3, 1, 1, 3, 5, 6, 6, 5,
          3, 1, 0, 0, 1, 6, 6, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
      }
  };

  // The positional value of queens on the various squares.
  static final short[][] _queenPositionalValue = {
      {
          0, 0, 5, 6, 6, 5, 0,
          0, // Opening stage
          0, 0, 2, 3, 3, 2, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
          0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
      },
      {
          0, 0, 0, 0, 0, 0, 0,
          0, // Middle game
          0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 5, 5, 1, 0, 0, 1, 3, 5, 6, 6, 5, 3, 1, 1, 3, 5, 6, 6, 5,
          3, 1, 0, 0, 1, 6, 6, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
      }
  };

  // The position value of the king
  static final short[] _kingPositionalValue = {
      6, 5, 2, 1, 1, 2, 5, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0
  };

  // Instance variables

  /**
   * The currently analyzed board.
   */
  private BitBoard _board;

  /**
   * The current game.
   */
  private Game _game;

  /**
   * The flag to indicate, if white moves next.
   */
  private boolean _whiteMoves;

  /**
   * A ply generator instance to simulate moves.
   */
  PlyGenerator _plyGenerator;



  // Constructors

  /**
   * Create a new bitboard analyzer.
   * 
   * @param plyGenerator
   *          A PlyGenerator instance to simulate moves.
   */
  public BitBoardAnalyzerImpl(Game game, PlyGenerator plyGenerator) {
    setGame(game);
    setPlyGenerator(plyGenerator);
  }



  // Methods

  /**
   * Get the currently analyzed board.
   * 
   * @return The currently analyzed board.
   */
  public final BitBoard getBoard() {
    return _board;
  }



  /**
   * Set a new board to be analyzed.
   * 
   * @param board
   *          The new board.
   */
  public final void setBoard(BitBoard board) {
    _board = board;
  }



  /**
   * Get the current game.
   * 
   * @return The current game.
   */
  public final Game getGame() {
    return _game;
  }



  /**
   * Set the current game.
   * 
   * @param The
   *          current game.
   */
  public final void setGame(Game game) {
    _game = game;
  }



  /**
   * Check, if white moves next.
   * 
   * @return true, if white moves next, false otherwise.
   */
  private final boolean whiteHasMoveRight() {
    return _whiteMoves;
  }



  /**
   * Set the flag, if white is about to move.
   * 
   * @param white
   *          Flag to indicate, if white has the next move.
   */
  public final void setMoveRight(boolean white) {
    _whiteMoves = white;
  }



  /**
   * Get the ply generator.
   * 
   * @return The PlyGenerator.
   */
  private final PlyGenerator getPlyGenerator() {
    return _plyGenerator;
  }



  /**
   * Set a new PlyGenerator instance.
   * 
   * @param plyGenerator
   *          The new PlyGenerator instance.
   */
  private final void setPlyGenerator(PlyGenerator plyGenerator) {
    _plyGenerator = plyGenerator;
  }



  /**
   * Analyze the current board.
   */
  public final short analyze() {

    // A check thread has a value, too.
    short checkValue = 0;

    // Start with the tests, if one of the players is in check.
    // It's important to start the test with the color that moves next!
    if (whiteHasMoveRight()) {
      if (isInCheck(true)) { // If the king of the moving player is in
        // check,
        checkValue += IBitBoardAnalyzer.BLACK_WIN; // the opponent
        // player
        // seems to win.
        // System.out.println("Weiss zieht und steht im Schach " +
        // checkValue);
      }
      if (isInCheck(false)) { // If the opponent's king is in check,
        checkValue += IBitBoardAnalyzer.WHITE_WIN; // the moving player
        // seems to win.
        // System.out.println("Weiss zieht und Schwarz steht im Schach "
        // + checkValue);
      }
    }
    else {
      if (isInCheck(false)) { // If the king of the moving player is in
        // check,
        checkValue += IBitBoardAnalyzer.WHITE_WIN; // the opponent seems
        // to win.
        // System.out.println("Schwarz zieht und steht im Schach " +
        // checkValue);
      }
      if (isInCheck(true)) { // If the opponent's king is in check,
        checkValue += IBitBoardAnalyzer.BLACK_WIN; // the moving player
        // seems to win.
        // System.out.println("Schwarz zieht und weiss steht im Schach "
        // + checkValue);
      }
    }

    // Early checks hinder the position building are punished therefore.
    if ((getGame().getNumberOfPlies() < 6) && (checkValue > (short) 0)) {
      // checkValue = (short)( checkValue / 20); // W.E. 20140729
      // System.out.println("C:" + checkValue);
      checkValue = (short) 0; // W.E. 20140729
    }

    // Now compute the position and material value of all pieces.

    short materialValue = 0; // Count the figures and their material value.
    short positionalValue = 0; // Score the position value.

    // Check the entire board.

    // Use a bitmask to speedup the test for a piece on the square
    long emptySquareMask = getBoard().getEmptySquares();

    // Get the positions of the white and black pawns.
    long[] pawnPos = new long[2];
    pawnPos[0] = getBoard().getPositionOfPieces(Piece.PAWN << 1);
    pawnPos[1] = getBoard().getPositionOfPieces(Piece.PAWN << 1 | 1);

    // I reuse the same PositionImpl object to avoid the overhead of
    // object instancing for each square.
    Position pos = new PositionImpl(0);

    for (int i = 0; i < 64; i++) {
      if ((emptySquareMask & 1) == 0) { // If there's a piece on the
        // square
        pos.setSquareIndex(i);
        Piece p = getBoard().getPiece(pos);
        // wenn emptySquareMask stimmt haben wir sicher eine Figur und
        // m�ssen das nicht mehr pr�fen
        // if( p != null) {
        short mValue = 0;
        short pValue = 0;

        // Add the value of the piece
        switch (p.getType()) {
          case Piece.PAWN:
            mValue = 10;
            pValue = _pawnPositionalValue[getGame().getNumberOfPlies() <= 12 ? 0 : 1][p.isWhite() ? i
                : ((7 - (i >>> 3)) << 3) + (i & 7)];
            // Check, if this pawn could be promoted
            if (p.isWhite()) {
              int j = i + 8;
              Position pos2 = new PositionImpl(0);
              while (j < 64) {
                pos2.setSquareIndex(j);
                Piece p2 = getBoard().getPiece(pos2);
                if (p2 != null && p2.getType() == Piece.PAWN) {
                  pValue -= 2;
                  break;
                }
                j += 8;
              }
            }
            else {
              int j = i - 8;
              Position pos2 = new PositionImpl(0);
              while (j >= 0) {
                pos2.setSquareIndex(j);
                Piece p2 = getBoard().getPiece(pos2);
                if (p2 != null && p2.getType() == Piece.PAWN) {
                  pValue -= 2;
                  break;
                }
                j += 8;
              }
            }
            break;
          case Piece.KNIGHT:
            pValue = _knightPositionalValue[getGame().getNumberOfPlies() <= 8 ? 0 : 1][p.isWhite() ? i
                : ((7 - (i >>> 3)) << 3) + (i & 7)];
            if (getGame().getNumberOfPlies() < 12) { // Check if this
              // piece blocks
              // a own pawn
              int row = i >>> 3;
              if (((p.isWhite()) && ((row == 2) || (row == 3)) && ((((1L << (i - 8)) | (1L << (i - 16))) & pawnPos[1]) != 0L))
                  || (!p.isWhite()
                  // W.E. 20140729 && ( ( row == 4) || ( row ==
                  // 5))
                      && ((row == 6) || (row == 7)) && ((((1L << (i + 8)) | (1L << (i + 16))) & pawnPos[0]) != 0L))) {
                // pValue -= _pawnBlocker;
              }
            }
            pValue = (short) ((int) 2 * pValue);
            mValue = 30;
            break;
          case Piece.BISHOP:
            pValue = _bishopPositionalValue[getGame().getNumberOfPlies() <= 8 ? 0 : 1][p.isWhite() ? i
                : ((7 - (i >>> 3)) << 3) + (i & 7)];
            if (getGame().getNumberOfPlies() <= 12) { // Check if this
              // piece blocks
              // a own pawn
              int row = i >>> 3;
              if (((p.isWhite()) && ((row == 2) || (row == 3)) && ((((1L << (i - 8)) | (1L << (i - 16))) & pawnPos[1]) != 0L))
                  || (!p.isWhite()
                  // W.E. 20140729 && ( ( row == 4) || ( row ==
                  // 5))
                      && ((row == 6) || (row == 7)) && ((((1L << (i + 8)) | (1L << (i + 16))) & pawnPos[0]) != 0L))) {
                pValue -= _pawnBlocker;
              }
            }
            mValue = 30;
            break;
          case Piece.ROOK:
            mValue = 45;
            pValue = _rookPositionalValue[getGame().getNumberOfPlies() <= 8 ? 0 : 1][p.isWhite() ? i
                : ((7 - (i >>> 3)) << 3) + (i & 7)];
            break;
          case Piece.QUEEN:
            mValue = 80;
            pValue = _queenPositionalValue[getGame().getNumberOfPlies() <= 14 ? 0 : 1][p.isWhite() ? i
                : ((7 - (i >>> 3)) << 3) + (i & 7)];
            pValue = (short) (pValue / (int) 2);
            break;
          case Piece.KING:
            pValue = _kingPositionalValue[p.isWhite() ? i : ((7 - (i >>> 3)) << 3) + (i & 7)];
            break;
        }
        if (p.isWhite()) {
          materialValue += mValue;
          positionalValue += pValue;
        }
        else {
          materialValue -= mValue;
          positionalValue -= pValue;
        }
        // }
      }
      // Shift the mask to test the next square
      emptySquareMask >>>= 1;
    }

    // Return a weighted score
    // return (short)( (short)2 * positionalValue + (short)7 * materialValue
    // + checkValue);
    
    // Logging.getLogging().addLogEntry(
    // getGame().toString() + " C:" + checkValue + " M:" + 9 * materialValue + " P:"
    //        + positionalValue);
    
    // System.out.print(getGame().toString());
    // System.out.println("P:" + positionalValue + " M:" + materialValue +
    // " C:" + checkValue);

    logger.info(getGame().toString() + " C:" + checkValue + " M:" + 9 * materialValue + " P:"
        + positionalValue);

    return (short) (positionalValue + (short) 9 * materialValue + checkValue);
  }



  /**
   * Test, if the given player is in check.
   * 
   * @param white
   *          Flag, if the white king is to test.
   * @return true, if the king is in check, false otherwise.
   */
  public final boolean isInCheck(boolean white) {

    // Get the position of the king.
    long kingPosition = getBoard().getPositionOfPieces(
        white ? Piece.KING << 1 | 1 : Piece.KING << 1);
    int kingSquare = BitUtils.getHighestBit(kingPosition);
    // System.out.println(" K�nig steht auf: " + kingSquare);

    // Get and cache the empty squares of the current board.
    long emptySquares = getBoard().getEmptySquares();
    // System.out.println(" Leere Felder: " +
    // Long.toHexString(emptySquares));

    // Now compute the moves backwards from the king's position.

    // Get all positions of bishops and queens
    long bishopPositions = getBoard().getPositionOfPieces(
        white ? Piece.BISHOP << 1 : (Piece.BISHOP << 1) | 1);
    long queenPositions = getBoard().getPositionOfPieces(
        white ? Piece.QUEEN << 1 : (Piece.QUEEN << 1) | 1);

    // The pieces, that attack diagonal
    long diagonalPositions = bishopPositions | queenPositions;
    // System.out.println(" diagonalPositions: " +
    // Long.toHexString(diagonalPositions));

    // Move the king to the upper right.
    long kingMask = kingPosition;
    while (((kingMask = ((kingMask & BitBoard._NOT_LINE_H & BitBoard._NOT_ROW_8) << 9)) & emptySquares) != 0L)
      ;
    if ((kingMask & diagonalPositions) != 0L) {
      // System.out.println("Schach erkannt: " +
      // Long.toHexString(kingMask));
      return true; // King is in check!
    }

    // Move the king to the lower right. incl. Bugfix 19.07.2014 W.E.
    kingMask = kingPosition; // Reset bitmask.
    while (((kingMask = ((kingMask & BitBoard._NOT_LINE_H & BitBoard._NOT_ROW_1) >>> 7)) & emptySquares) != 0L)
      ;
    {
      if ((kingMask & diagonalPositions) != 0L) {
        // System.out.println(" Schach erkannt: " +
        // Long.toHexString(kingMask));
        return true; // King is in check!
      }
    }
    // Move the king to the upper left.
    kingMask = kingPosition; // Reset bitmask.
    while (((kingMask = ((kingMask & BitBoard._NOT_LINE_A & BitBoard._NOT_ROW_8) << 7)) & emptySquares) != 0L)
      ;
    if ((kingMask & diagonalPositions) != 0L) {
      // System.out.println(" Schach erkannt: " +
      // Long.toHexString(kingMask));
      return true; // King is in check!
    }

    // Move the king to the lower left
    kingMask = kingPosition; // Reset bitmask.
    while (((kingMask = ((kingMask & BitBoard._NOT_LINE_A & BitBoard._NOT_ROW_1) >>> 9)) & emptySquares) != 0L)
      ;
    if ((kingMask & diagonalPositions) != 0L) {
      // System.out.println(" Schach erkannt: " +
      // Long.toHexString(kingMask));
      return true; // King is in check!
    }

    // Now we need the rooks, too.
    long rookPositions = getBoard().getPositionOfPieces(
        white ? Piece.ROOK << 1 : (Piece.ROOK << 1) | 1);

    // The pieces, that attack horizontally or vertically.
    long horVertPositions = rookPositions | queenPositions;
    // System.out.println(" horVertPositions: " +
    // Long.toHexString(horVertPositions));

    // Move the king downwards
    kingMask = kingPosition; // Reset bitmask.
    while (((kingMask = ((kingMask & BitBoard._NOT_ROW_1) >>> 8)) & emptySquares) != 0L)
      ;
    if ((kingMask & horVertPositions) != 0L) {
      // System.out.println(" Schach erkannt: " +
      // Long.toHexString(kingMask));
      return true; // King is in check!
    }

    // Move the king upwards
    kingMask = kingPosition; // Reset bitmask.
    while (((kingMask = ((kingMask & BitBoard._NOT_ROW_8) << 8)) & emptySquares) != 0L)
      ;
    if ((kingMask & horVertPositions) != 0L) {
      // System.out.println(" Schach erkannt: " +
      // Long.toHexString(kingMask));
      return true; // King is in check!
    }

    // Move the king to the left
    kingMask = kingPosition; // Reset bitmask.
    while (((kingMask = ((kingMask & BitBoard._NOT_LINE_A) >>> 1)) & emptySquares) != 0L)
      ;
    if ((kingMask & horVertPositions) != 0L) {
      // System.out.println(" Schach erkannt: " +
      // Long.toHexString(kingMask));
      return true; // King is in check!
    }

    // Move the king to the right
    kingMask = kingPosition; // Reset bitmask.
    while (((kingMask = ((kingMask & BitBoard._NOT_LINE_H) << 1)) & emptySquares) != 0L)
      ;
    if ((kingMask & horVertPositions) != 0L) {
      // System.out.println(" Schach erkannt: " +
      // Long.toHexString(kingMask));
      return true; // King is in check!
    }

    // Check, if the king is in check by a knight
    // Compute the knight moves backwards from the position of the
    // king and see, if there's a knight on this square.
    if ((getPlyGenerator().getKnightPlies(kingSquare) & getBoard().getPositionOfPieces(
        white ? Piece.KNIGHT << 1 : (Piece.KNIGHT << 1) | 1)) != 0L) {
      return true;
    }

    // Check if the king is in check by a pawn
    kingMask = kingPosition; // Reset bitmask.
    if (white) {
      // Get the positions of all black pawns and compare them with a
      // moved king.
      return ((((kingMask & BitBoard._NOT_LINE_H & BitBoard._NOT_ROW_8) << 9) | ((kingMask
          & BitBoard._NOT_LINE_A & BitBoard._NOT_ROW_8) << 7)) & getBoard().getPositionOfPieces(
          Piece.PAWN << 1)) != 0L;
    }
    else {
      // Get the positions of all white pawns and compare them with a
      // moved king.
      return ((((kingMask & BitBoard._NOT_LINE_A & BitBoard._NOT_ROW_1) >>> 9) | ((kingMask
          & BitBoard._NOT_LINE_H & BitBoard._NOT_ROW_1) >>> 7)) & getBoard().getPositionOfPieces(
          (Piece.PAWN << 1) | 1)) != 0L;
    }
  }



  /**
   * Test if a king is in check on a given board.
   * 
   * @param board
   *          The board to test.
   * @param white
   *          true, if the white king is checked, false otherwise.
   */
  public final boolean isInCheck(BitBoard board, boolean white) {
    setBoard(board);
    return isInCheck(white);
  }



  /**
   * Analyzed a new board.
   * 
   * @param board
   *          The new board to analyze.
   * @param white
   *          Flag to indicate, if white has the next move.
   */
  public final short analyze(BitBoard board, boolean white) {
    setBoard(board);
    setMoveRight(white);
    return analyze();
  }
}
