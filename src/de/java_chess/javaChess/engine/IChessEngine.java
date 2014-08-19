/*
  ChessEngine - A interface to implement a engine to play chess.

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

package de.java_chess.javaChess.engine;

import javax.swing.JMenu;

import de.java_chess.javaChess.board.Board;
import de.java_chess.javaChess.game.IGame;
import de.java_chess.javaChess.ply.IPly;


/**
 * This interface defines the functionality of a engine
 * to play the game of chess.
 */
public interface IChessEngine {

    /**
     * Reset the engine for a new game.
     */
    void reset();

    /**
     * Get the current game.
     *
     * @return The current game.
     */
    IGame getGame();

    /**
     * Set the current game.
     *
     * @param The current game.
     */
    void setGame( IGame game);

    /**
     * Get the current board.
     *
     * @return The current board.
     */
    Board getBoard();
    
    /**
     * Set the board.
     *
     * @param board The new board.
     */
    void setBoard( Board board);

    /**
     * Get the color of this engine.
     *
     * @param white true, if the engine operates with the white pieces.
     */
    boolean isWhite();

    /**
     * Set the color of the engine.
     *
     * @param white flag to indicate if the engine operates on the white pieces.
     */
    void setWhite( boolean white);

    /**
     * Compute the best ply for the current position.
     *
     * @return The best known ply for the current position.
     */
    IPly computeBestPly();

    /**
     * Get all the potential plies for the human player.
     *
     * @return All the potential plies for the human player.
     */
    IPly [] getUserPlies();

    /**
     * Check if a ply made by the user is valid.
     *
     * @param ply The user ply.
     *
     * @return true, if the ply is valid. false otherwise.
     */
    boolean validateUserPly( IPly ply);

    /**
     * Request a menu from the chess engine, where the user
     * can change it's settings.
     *
     * @return A menu for the engine settings.
     */
    JMenu getMenu();

    /**
     * Get the current game state for a given color.
     *
     * @param white True, if the state of the white player is requested.
     *
     * @return The current game state.
     */
    byte getCurrentGameState( boolean white);

    /**
     * Start the computations of the permanent brain.
     */
    void startPermanentBrain();

    /**
     * Stop the computations of the permanent brain.
     */
    void stopPermanentBrain();
}
