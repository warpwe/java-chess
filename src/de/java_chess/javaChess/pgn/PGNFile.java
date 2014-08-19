/*
  PGNFile - A class to read PGN files.

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

import java.io.Reader;

import antlr.RecognitionException;
import antlr.TokenStreamException;
import de.java_chess.javaChess.notation.IGameNotation;
import de.java_chess.javaChess.notation.GameNotationImpl;


/**
 * This class implements the functionality to read a PGN file.
 */
public class PGNFile {

    // Instance variables

    /**
     * The current PGN parser.
     */
    PGNParser _parser;
    
    /**
     * The reader for the current file.
     */
    Reader _reader;


    // Constructors

    /**
     * Create a new instance.
     *
     * @param reader The reader for the file.
     */
    public PGNFile( Reader reader) {
	setReader( reader);
    }


    // Methods
    
    /**
     * Read one game from the current file.
     *
     * @return The notation of the game.
     *
     * @throws RecognitionException If a parser problem occured.
     * @throws TokenStreamException If a scanner problem occured.
     */
    public final IGameNotation readGame() throws RecognitionException, TokenStreamException {
	
	// A buffer for the result.
	IGameNotation notationBuffer = new GameNotationImpl();

	// Start the parsing of the file.
	getPGNParser().pgnGame( notationBuffer);

	return notationBuffer;
    }

    /**
     * Get the reader for the current file.
     *
     * @return The current reader.
     */
    private final Reader getReader() {
	return _reader;
    }

    /**
     * Set a new reader for a file.
     *
     * @param reader The new reader.
     */
    private final void setReader( Reader reader) {
	_reader = reader;
    }

    /**
     * Get the current parser, if there's already one, or 
     * create a new parser and return it.
     *
     * @return The current PGN parser.
     */
    private final PGNParser getPGNParser() {
	if( _parser == null) {   // If there is no parser yet, create one.
	    _parser = new PGNParser( new PGNLexer( getReader()));
	}
	return _parser;                 // And return it.
    }

    /**
     * Set the current parser.
     *
     * @param parser The new PGN parser.
     */
    private final void setPGNParser( PGNParser parser) {
	_parser = parser;
    }
} /* End of class PGNFile */
