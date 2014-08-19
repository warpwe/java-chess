/*
  OpeningBookImpl - A implementation of the OpeningBook interface.

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

package de.java_chess.javaChess.engine.opening_book;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import antlr.RecognitionException;
import antlr.TokenStreamException;
import de.java_chess.javaChess.notation.IGameNotation;
import de.java_chess.javaChess.pgn.PGNFile;
import de.java_chess.javaChess.ply.IAnalyzedPly;
import de.java_chess.javaChess.ply.AnalyzedPlyImpl;
import de.java_chess.javaChess.ply.IPly;


/**
 * This class implements a opening book.
 */
public class OpeningBookImpl implements OpeningBook {

    // Instance variables

    /**
     * The current game.
     */
    private IGameNotation _notation = null;

    /**
     * All the known openings with their notations.
     */
    private ArrayList _openings;

    /**
     * The index of the currently used opening.
     */
    private int _currentOpening;

    
    // Constructors
    
    /**
     * Create a new instance of this class.
     *
     * @param notation The notation of the current game.
     */
    public OpeningBookImpl( IGameNotation notation) {
	setNotation( notation);

	// Create a new dynamic array for the openings.
	_openings = new ArrayList();  

	// Reset the opening book.
	reset();
    }


    // Methods

    /**
     * The user just did a ply.
     *
     * @param ply The user ply.
     */
    public final void doUserPly( IPly ply) {

	// If we are already in a opening, check if the current user
	// move doesn't lead us out of the opening line.
	if( _currentOpening != -1) {
	    if( ! isInOpening( _currentOpening)) {
		_currentOpening = -1;
	    }
	}
    }

    /**
     * Get the next ply from the opening book, if there is
     * one available, or null if not.
     *
     * @return The next ply from the opening book, or null 
     *         if there's no ply available.
     */
    public final IAnalyzedPly getOpeningBookPly() {

	// If we have no opening yet, try to find one.
	if( _currentOpening == -1) {
	    _currentOpening = findOpening();
	}
	
	// Check, if we have found a opening.
	if( _currentOpening != -1) {
	    // Check, if we have another ply in the opening.
	    int pliesMade = _notation.size();

	    IGameNotation currentOpening = (IGameNotation)( _openings.get( _currentOpening));

	    if( currentOpening.size() > pliesMade) {
		return new AnalyzedPlyImpl( currentOpening.getPlyNotation( pliesMade).getPly(), (short)0);
	    }
	}

	return null; // No ply found in the available opening books.
    }

    /**
     * Reset the opening book to the initial piece position.
     */
    public final void reset() {
	_currentOpening = -1;
    }

    /**
     * Add a opening in PGN format to the opening book and return
     * the error status.
     *
     * @param file The File to add to the opening book.
     */
    public final void addPGNopening( File file) {
	try {
	    PGNFile pgnFile = new PGNFile( new BufferedReader( new FileReader( file)));
	    IGameNotation notation = pgnFile.readGame();
	    _openings.add( notation);

	    // System.out.println( "DEBUG: added opening \n" + notation.toString());

	} catch( FileNotFoundException fe) {
	    JOptionPane.showMessageDialog( null, "File " + file.getName() + " not found!", "File not found", JOptionPane.ERROR_MESSAGE);
	} catch( IOException ioe) {
	    JOptionPane.showMessageDialog( null, "IO error while parsing file " + file.getName() + "!", "IO error", JOptionPane.ERROR_MESSAGE);
	} catch( RecognitionException re) {
	    JOptionPane.showMessageDialog( null, "Error while parsing file " + file.getName() + "!", "Error in PGN file", JOptionPane.ERROR_MESSAGE);
	} catch( TokenStreamException te) { 
	    System.err.print("Tokenstream exception"); 
	}
    }

    /**
     * Try to find a opening for the current game notation.
     *
     * @return The index of the found opening.
     */
    private final int findOpening() {
	for( int i = 0; i < _openings.size(); i++) {
	    if( isInOpening( i)) {
		return i;
	    }
	}
	return -1;  // No opening found.
    }

    /**
     * Check, if the current game notation is part of a given opening.
     *
     * @param openingIndex The index of the opening to test.
     */
    private final boolean isInOpening( int openingIndex) {
	IGameNotation notation = (IGameNotation)_openings.get( openingIndex);

	// If the current game has more plies, than the opening, we can return
	// false.
	if( _notation.size() > notation.size()) {
	    return false;
	}

	// Compare the notation ply by ply, which is rather slow, but should work
	// for now.
	for( int i = 0; i < _notation.size(); i++) {
	    if( ! _notation.getPlyNotation( i).getPly().equals( notation.getPlyNotation( i).getPly())) {
		return false;
	    }
	}

	// The current game uses the given opening.
	return true;
    }

    /**
     * Get the notation of the current game.
     *
     * @return The notation of the current game.
     */
    private final IGameNotation getNotation() {
	return _notation;
    }

    /**
     * Set the notation of the current game.
     *
     * @param notation The notation of the current game.
     */
    private final void setNotation( IGameNotation notation) {
	_notation = notation;
    }
}
