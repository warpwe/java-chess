header {
package de.java_chess.javaChess.pgn;

import antlr.*;
import antlr.collections.*;  
import de.java_chess.javaChess.notation.*;
}

/*
  pgn.g - A Antlr grammar to parse .pgn (Portable Game Notation) files.

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

/**
 * A parser for PGN (Portable Game Notation) files.
 */
class PGNParser extends Parser;
options {
    exportVocab=PGN;
    defaultErrorHandler = false;       // Don't generate parser error handlers
    buildAST = false;
    k=2;
}
// tokens {
//        DOT;
// 	STRING_LITERAL;
//	SQUARE_NAME;
//	MOVE_INDEX;
//	FIGURINE_LETTER_CODE;
// }
{
	/**
	 * The current move index.
         */
	private int _moveIndex;

	/**
	 * The buffer for the game notation.
	 */
	private GameNotation _notation;

	/**
	 * Set a new buffer for the game notation.
  	 *
	 * @param notation The new notation buffer.
	 */
	public final void setNotation( GameNotation notation) {
	    _notation = notation;
	}

	/**
	 * Get the current notation.
	 *
	 * @return The current notation.
	 */
	public final GameNotation getNotation() {
	    return _notation;
	}

	/**
	 * A loader to follow the loaded game.
	 */
	private GameLoader _gameLoader;

	/**
	 * Get the current game loader.
	 *
	 * @return The current game loader.
	 */
	private final GameLoader getGameLoader() {
            return _gameLoader;
        }

	/**
	 * Set a new game loader.
	 * 
	 * @param loader The new game loader.
	 */
	private final void setGameLoader( GameLoader loader) {
	    _gameLoader = loader;
	}
}

// A PGN file
pgnGame[ GameNotation notationBuffer]
{ 
  setNotation( notationBuffer); 
  setGameLoader( new GameLoader());
}
	: whiteSpaces tagPairSection moveTextSection
	;

// The tag pair section
tagPairSection : ( tagPair whiteSpaces )* ;

// All the valid tag pairs
tagPair : LBRACK
          whiteSpaces 
	  ( eventTag | siteTag | dateTag | roundTag | whiteTag | blackTag | resultTag | openingTag ) 
          whiteSpaces 
	  RBRACK
	  ;

// A event tag
eventTag : TAG_EVENT whiteSpaces STRING_LITERAL ; 

// A site tag
siteTag : TAG_SITE  whiteSpaces STRING_LITERAL ;

// A date tag
dateTag : TAG_DATE  whiteSpaces STRING_LITERAL ;

// A round tag
roundTag : TAG_ROUND  whiteSpaces STRING_LITERAL ;

// Info on the player with the white pieces
whiteTag : TAG_WHITE  whiteSpaces name:STRING_LITERAL { getNotation().setPlayerInfo( name.getText(), true); } ;

// Info on the player with the black pieces
blackTag : TAG_BLACK  whiteSpaces name:STRING_LITERAL { getNotation().setPlayerInfo( name.getText(), false); } ;

// Info on the result
resultTag : TAG_RESULT  whiteSpaces STRING_LITERAL ;

// Info on the opening
openingTag : TAG_OPENING whiteSpaces name:STRING_LITERAL { getNotation().setOpeningInfo( name.getText()); } ;

// The movetext section
moveTextSection : { _moveIndex = 0; } ( move )* GAME_TERMINATOR ;

// A move
move 
{ PlyNotation notation = null;}
	: 
          mI:MOVE_INDEX { ++_moveIndex == Integer.parseInt( mI.getText()) }?
          whiteSpaces 
          notation = ply { getNotation().addPly( notation); }
          whiteSpaces 
          notation = ply { getNotation().addPly( notation); }
          whiteSpaces 
       	;

// A ply
ply returns [PlyNotation notation = null]
{ PGNPlyFragment plyFragment = new PGNPlyFragment(); }
	: (
	    (
              ( lc:FIGURINE_LETTER_CODE { plyFragment.setPieceTypeFromLetter( lc.getText().charAt(0)); } )? 
      	      ( 
                ( snOrg:SQUARE_NAME { plyFragment.setOrigin( snOrg.getText()); } )?
                ( PIECE_MOVE { plyFragment.setCapture( false); } | PIECE_CAPTURE { plyFragment.setCapture( true); } ) 
                snDest:SQUARE_NAME { plyFragment.setDestination( snDest.getText()); } 
              )
      	      ( 
                PAWN_PROMOTION 
                lc2:FIGURINE_LETTER_CODE { ! "P".equals( lc2.getText()) && ! "K".equals( lc2.getText())}? 
                { plyFragment.setPawnPromotion( lc2.getText().charAt(0)); } 
              )?
            )
            |
	    LEFT_CASTLING { plyFragment.setCastling( true); }
            |
            RIGHT_CASTLING { plyFragment.setCastling( false); }
          )
          {  // When we have all the info from the PGN file, we can try to
             // create a ply notation from it.
	     notation = getGameLoader().completePly( plyFragment);
          }
	  {notation != null}?  // Make sure, we were actually able to turn the fragment into a ply!
      	;

// A couple of whitespaces, that delimit a PGN feature
whiteSpaces : ( WS )* ;

/**
 * A lexer for PGN files.
 */
class PGNLexer extends Lexer;
options {
        exportVocab=PGN;       // call the vocabulary 'PGN'.
        k=2;                   // 2 characters of lookahead
        testLiterals=true;
        caseSensitiveLiterals = true;
        caseSensitive=true;
        charVocabulary = '\3'..'\377';  // Set the vocabulary to all characters, except the special 
                                        // characters, that Antlr uses.
}   

// ignore whitespaces
WS      :       ( ' ' | '\t' | '\f'
                | ( "\r\n" | '\r' | '\n'  ) { newline(); }  // handle different types of newlines (DOS, MAC, Unix)
                ) { /* $setType(Token.SKIP); */ }
        ;

// Basic literals
// ASTERISK	:	'*' ;  // Used as a game terminator.
DOT 		: 	'.' ;
LBRACK		:	'[';
RBRACK  	:	']';

// Single-line comments
SL_COMMENT
        :       ";" ( ~( '\n' | '\r' ) )*  ( "\n" | "\r" | "\r\n" ) { $setType(Token.SKIP); newline(); }
        ;

// A string literal (remove the quotes already here).
STRING_LITERAL :  ( '"' (~('"'))* '"' ) 
  { 
    // Remove the leadind and trailing quote.
    String literal = $getText;
    literal = literal.length() == 2 ? "" : literal.substring( 1, literal.length() - 2);
    $setText( literal);
  } 
  ;

// A square name (like 'a4' or 'h8')
SQUARE_NAME : 'a'..'h' '1'..'8' ;

// The code to move a piece.
PIECE_MOVE	: '-';

// The code to capture a piece.
PIECE_CAPTURE	: 'x';

// The code to promote a pawn.
PAWN_PROMOTION	: '=';

// The character code of a figure type
FIGURINE_LETTER_CODE : ( 'P' | 'N' | 'B' | 'R' | 'Q' | 'K' ) ;

// The castling moves
// LEFT_CASTLING 	: "O-O-O" ;
RIGHT_CASTLING 	: "O-O" ( "-O" { $setType( LEFT_CASTLING ); } )? ;

// A move index
MOVE_INDEX : ( '1'..'9' ('0'..'9')* DOT ) 
  { 
    // Remove the trailing dot.
    String index = $getText;
    index = index.substring( 0, index.length() - 1);
    $setText( index); 
  } 
  ;

// The identifiers for the tag pairs
TAG_WHITE 	: "White" ;
TAG_BLACK 	: "Black" ;
TAG_DATE 	: "Date" ;
TAG_EVENT 	: "Event" ;
TAG_SITE  	: "Site" ;
TAG_ROUND 	: "Round" ;
TAG_RESULT 	: "Result" ; 
TAG_OPENING	: "Opening" ;

// The game termination markers
GAME_TERMINATOR : ( "*" | "1-0" | "0-1" | "1/2-1/2" ) ;