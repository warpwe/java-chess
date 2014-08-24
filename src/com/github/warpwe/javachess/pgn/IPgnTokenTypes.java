// $ANTLR 2.7.1: "pgn.g" -> "PGNParser.java"$

package com.github.warpwe.javachess.pgn;

public interface IPgnTokenTypes {
  int EOF = 1;
  int NULL_TREE_LOOKAHEAD = 3;
  int LBRACK = 4;
  int RBRACK = 5;
  int TAG_EVENT = 6;
  int STRING_LITERAL = 7;
  int TAG_SITE = 8;
  int TAG_DATE = 9;
  int TAG_ROUND = 10;
  int TAG_WHITE = 11;
  int TAG_BLACK = 12;
  int TAG_RESULT = 13;
  int TAG_OPENING = 14;
  int GAME_TERMINATOR = 15;
  int MOVE_INDEX = 16;
  int FIGURINE_LETTER_CODE = 17;
  int SQUARE_NAME = 18;
  int PIECE_MOVE = 19;
  int PIECE_CAPTURE = 20;
  int PAWN_PROMOTION = 21;
  int LEFT_CASTLING = 22;
  int RIGHT_CASTLING = 23;
  int WS = 24;
  int DOT = 25;
  int SL_COMMENT = 26;
}
