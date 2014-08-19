/*
  GameNotationImpl - This class implements the functionality to
                     notate an entire game.

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

package de.java_chess.javaChess.notation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.java_chess.javaChess.renderer2d.NotationPanel;


/**
 * This class implements the functionality to notate an 
 * entire game.
 */
public class GameNotationImpl implements IGameNotation {

    // Instance variables

    /**
     * The info on the players.
     */
    private String [] _playerInfo = new String[2];

    /**
     * The name of the used opening.
     */
    private String _openingName;

    /**
     * The plies of this game.
     */
    private ArrayList _plies;

    /**
     * A string representation of the game notation.
     */
    private StringBuffer _notation;

    /**
     * A panel to display the notation.
     */
    private NotationPanel _notationPanel;


    // Constructors

    /**
     * Create a new game notation instance.
     */
    public GameNotationImpl() {
	_plies = new ArrayList();
	_notation = new StringBuffer();
    }


    // Methods

    /**
     * Reset the game notation for a new game.
     */
    public void reset() {
	_plies.clear();                  // Remove all the plies.
	_notation = new StringBuffer();  // Remove the text.

	// Remove the text from the panel.
	getNotationPanel().setText( "");

	// Remove the player info.
	setPlayerInfo( null, true);
	setPlayerInfo( null, false);

	// Remove the opening name.
	setOpeningInfo( null);
    }

    /**
     * Get the notation for a given move and piece color.
     *
     * @param moveIndex The index of the move.
     * @param boolean white Flag to indicate if the color is white.
     *
     * @return The notation for a given move and color.
     */
    public final String getMove( int moveIndex, boolean white) {
	return getPly( white ? ( moveIndex << 1) : ( moveIndex << 1) + 1);
    }

    /**
     * Get the ply with the given index.
     *
     * @param plyIndex The index of the ply.
     *
     * @return The notation for the ply as a string.
     */
    private final String getPly( int plyIndex) {
	return _plies.get( plyIndex).toString();
    }

    /**
     * Add a new ply with it's notation.
     *
     * @param plyNotation The notation of the new ply.
     */
    public void addPly( IPlyNotation plyNotation) {
	_plies.add( plyNotation);
	
	int nPlies = _plies.size();  // Get the number of plies.
	if( ( nPlies & 1) != 0) {
	    if( nPlies > 1) {  // Start a new line
		_notation.append( "\n");
	    }
	    _notation.append( "" + ( ( nPlies >> 1) + 1) + ". ");
	} else {
	    _notation.append( " ");
	}
	_notation.append( plyNotation.toString());

	// If there's a panel, display the current notation.
	if( null != getNotationPanel()) {
	    getNotationPanel().setText( toString());
	}
    }
    /**
     * Get a ply with a given index.
     *
     * @param index The index of the ply.
     */
    public IPlyNotation getPlyNotation( int index) {
	return (IPlyNotation)( _plies.get( index));
    }

    /**
     * Get the number of plies in this notation.
     *
     * @return The number of plies.
     */
    public int size() {
	return _plies.size();
    }

    /**
     * Get the entire game as a string.
     *
     * @return The entire game as a string.
     */
    public final String toString() {
	return _notation.toString();
    }

    /**
     * Get some extended PGN compatible info on the game.
     *
     * @return Extended PGN compatible info on the game.
     */
    public final String getPGNheader() {

	// Create a buffer for the result.
	StringBuffer resultBuffer = new StringBuffer();

	// Add a empty event to the header
	resultBuffer.append( "[Event \"?\"]\n");

	// Info on the site
	resultBuffer.append( "[Site \"?\"]\n");

	// Add the current date to the header
	resultBuffer.append( "[Date \"").append( ( new SimpleDateFormat( "yyyy.MM.dd")).format( new Date())).append( "\"]\n");

	// Add the round to the header
	resultBuffer.append( "[Round \"?\"]\n");

	// Add the name of the white player, if it is available.
	if( ! "".equals( getPlayerInfo( true))) {
	    resultBuffer.append( "[White \"").append( getPlayerInfo( true)).append( "\"]\n");
	}

	// Add the name of the black player, if it is available.
	if( ! "".equals( getPlayerInfo( false))) {
	    resultBuffer.append( "[Black \"").append( getPlayerInfo( false)).append( "\"]\n");
	}

	// Info on the result
	resultBuffer.append( "[Result \"?\"]\n");

	// Convert the header to a String and return it.
	return resultBuffer.toString();
    }

    /**
     * Get some info on a player.
     *
     * @return Some info on a player.
     */
    public final String getPlayerInfo( boolean white) {
	return _playerInfo[ white ? 1 : 0];
    }

    /**
     * Set the info on a player.
     *
     * @param playerInfo The player info.
     * @param white Flag to indicate, if it's the player 
     *              with the white pieces.
     */
    public final void setPlayerInfo( String playerInfo, boolean white) {
	_playerInfo[ white ? 1 : 0] = playerInfo;
    }

    /**
     * Get the info on the used opening.
     *
     * @return The name of the used opening.
     */
    public final String getOpeningInfo() {
	return _openingName;
    }

    /**
     * Set the info on the used opening.
     *
     * @param name The name of the opening.
     */
    public final void setOpeningInfo( String name) {
	_openingName = name;
    }

    /**
     * Get the panel for the output.
     *
     * @return The panel for the output.
     */
    public final NotationPanel getNotationPanel() {
	return _notationPanel;
    }

    /**
     * Set the panel for the notation output.
     *
     * @param notationPanel The panel for the output.
     */
    public final void setNotationPanel( NotationPanel notationPanel) {
	_notationPanel = notationPanel;
    }
}




