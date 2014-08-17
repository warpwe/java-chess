/*
  PermanentBrain - Implements a permanent brain, that
                   computes plies, while the human player
                   is thinking.

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

package de.java_chess.javaChess.engine.permanent_brain;

import de.java_chess.javaChess.board.Board;
import de.java_chess.javaChess.engine.ChessEngine;
import de.java_chess.javaChess.engine.ChessEngineImpl;
import de.java_chess.javaChess.ply.Ply;


/**
 * This class implements a permanent brain, that computes
 * plies, while the human player is thinking.
 */
public class PermanentBrain implements Runnable {

    // Instance variables

    /**
     * The potential next moves of the human player.
     */
    private Ply [] _userPlies;

    /**
     * The computed responses to the user plies.
     */
    private PreComputedPly [] _computedPlies = null;

    /**
     * The used chess engine.
     */
    private ChessEngine _engine;

    /**
     * The search thread for the plies.
     */
    private volatile Thread _searchThread = null;


    // Constructors

    /**
     * Create a new PermanentBrain instance.
     *
     * @param engine The used chess engine.
     */
    public PermanentBrain( ChessEngine engine) {
	setEngine( engine);
    }


    // Methods

    /**
     * Get the current chess engine.
     *
     * @return The current chess engine.
     */
    private final ChessEngine getEngine() {
	return _engine;
    }

    /**
     * Set a new chess engine.
     *
     * @param engine The new chess engine.
     */
    private final void setEngine( ChessEngine engine) {
	_engine=engine;
    }

    /**
     * Reset (remove) the user plies.
     */
    public final void resetUserPlies() {
	_userPlies=null;
    }

    /**
     * Get the potential plies for a user.
     * (Required to validate a user ply.)
     *
     * @return The potential plies for a user.
     */
    public final Ply [] getUserPlies() {

	// Lazy computation for better performance.
	if( _userPlies == null) {
	    _userPlies = getEngine().getUserPlies();
	}

	return _userPlies;  // Return the user plies.
    }

    /**
     * Get the response for a user ply.
     *
     * @param ply The ply of the human player.
     *
     * @return The precomputed response for the user ply.
     */
    public final PreComputedPly getPlyForUserPly( Ply ply) {

	if( _computedPlies != null) {

	    // Stop the computation 
	    // (but should already be done by the game controller)
	    stopComputation();

	    // Get the index of the user ply.
	    Ply [] userPlies = getUserPlies();
	    int plyCount = userPlies.length;

	    for( int i=0; i < plyCount; i++) {
		if( userPlies[i].equals( ply)) {
		    
		    // i holds the index of the user ply now, which equals the
		    // index of the computed response.
		    return _computedPlies[i];
		}
	    }
	}

	return null;
    }

    /**
     * Stop the computation of a response to the next user ply.
     */
    public final void stopComputation() {
	if( _searchThread != null) {
	    Thread savedThread = _searchThread;  // Save the thread.
	    ( (ChessEngineImpl)getEngine()).setSearchStop( true);
	    _searchThread = null;                // Signal to finish.
	    try {
	        savedThread.join();                      // Use the copy to wait 
	    } catch( InterruptedException ignored) {}    // for the thread to finish.
	}
    }

    /**
     * Start the computation of a response to the next user ply.
     */
    public final void startComputation() {

	// Stop the computation, if it was a reset.
	stopComputation();

	// Reset the permanent brain for the next user move.
	resetUserPlies();
	_computedPlies = new PreComputedPly[ getUserPlies().length];

	if( _searchThread == null) {  // Should always be null
	    _searchThread = new Thread( this);
	    _searchThread.start();
	}
    }

    /**
     * The actual search method.
     */
    public void run() {
	
	// Start with search depth 1
	( (ChessEngineImpl)getEngine()).setSearchDepth( 1);

	// Save the current board of the actual game.
	Board currentBoard = getEngine().getBoard();  // No clone() required at the moment.

	// Get the user plies as a local variable.
	Ply [] userPlies = getUserPlies();

	// Let the minimax search run through.
	( ( ChessEngineImpl)getEngine()).setSearchStop( false);

	// Reset the number of analyzed boards.
	( (ChessEngineImpl)getEngine()).setAnalyzedBoards( 0L);

	while( _searchThread == Thread.currentThread()) {
	    
	    // Go through all the user plies and compute a response.
	    for( int i=0; ( i < userPlies.length) && ( _searchThread == Thread.currentThread()); i++) {
		getEngine().setBoard( currentBoard.getBoardAfterPly( userPlies[i]));
		try {
		    _computedPlies[i] = new PreComputedPlyImpl( ( (ChessEngineImpl)getEngine()).startMinimaxAlphaBeta( getEngine().isWhite())
								, ( (ChessEngineImpl)getEngine()).getSearchDepth());
		} catch( InterruptedException ignored) {
		    _computedPlies[i] = null;
		}
	    }

	    // Increase the search depth and continue with another run.
	    ( (ChessEngineImpl)getEngine()).increaseSearchDepth();
	}

	// Restore the current board.
	getEngine().setBoard( currentBoard);
    }
}
