/*
  BitBoard - A interface to implement a chess board data structure as
             layered bitmaps.

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

package de.java_chess.javaChess.bitboard;

import de.java_chess.javaChess.board.Board;


/**
 * This interface defines the methods to be implemented by any 
 * bitboard implementation.
 */
public interface IBitBoard extends Board {
    
    // Static constants.

    long _LINE_A = 0x0101010101010101L;
    long _LINE_B = 0x0202020202020202L;
    long _LINE_C = 0x0404040404040404L;
    long _LINE_D = 0x0808080808080808L;
    long _LINE_E = 0x1010101010101010L;
    long _LINE_F = 0x2020202020202020L;
    long _LINE_G = 0x4040404040404040L;
    long _LINE_H = 0x8080808080808080L;
    long _NOT_LINE_A = ~_LINE_A;
    long _NOT_LINE_B = ~_LINE_B;
    long _NOT_LINE_C = ~_LINE_C;
    long _NOT_LINE_D = ~_LINE_D;
    long _NOT_LINE_E = ~_LINE_E;
    long _NOT_LINE_F = ~_LINE_F;
    long _NOT_LINE_G = ~_LINE_G;
    long _NOT_LINE_H = ~_LINE_H;
    long _ROW_1 = 0x00000000000000FFL;
    long _ROW_2 = 0x000000000000FF00L;
    long _ROW_3 = 0x0000000000FF0000L;
    long _ROW_4 = 0x00000000FF000000L;
    long _ROW_5 = 0x000000FF00000000L;
    long _ROW_6 = 0x0000FF0000000000L;
    long _ROW_7 = 0x00FF000000000000L;
    long _ROW_8 = 0xFF00000000000000L;
    long _NOT_ROW_1 = ~_ROW_1;
    long _NOT_ROW_2 = ~_ROW_2;
    long _NOT_ROW_3 = ~_ROW_3;
    long _NOT_ROW_4 = ~_ROW_4;
    long _NOT_ROW_5 = ~_ROW_5;
    long _NOT_ROW_6 = ~_ROW_6;
    long _NOT_ROW_7 = ~_ROW_7;
    long _NOT_ROW_8 = ~_ROW_8;


    // Methods

    /**
     * Get the position of some pieces as a long (64 bit wide) bitmask.
     *
     * @param pieceTypeColor The color and type of the pieces.
     *
     * @return A bitmask with the positions of these pieces.
     */
    long getPositionOfPieces(int pieceTypeColor);

    /**
     * Get the position of all empty squares.
     *
     * @return The position of all empty squares.
     */
    long getEmptySquares();

    /**
     * Get all white or black pieces.
     *
     * @param white true, if the white pieces are requested, 
     *              false for the black pieces.
     * @return A bitmask with the colors of these pieces.
     */
    long getAllPiecesForColor(boolean white);

    /**
     * Get the board as a byte stream.
     *
     * @return The board as a array of bytes.
     */
    byte [] getBytes();

    long getLayer0();
    long getLayer1();
    long getLayer2();
    long getLayer3();
}
