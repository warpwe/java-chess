/*
  PreComputedPlyImpl - Implements a ply, that was computed by 
                       the permanent brain function.

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

import de.java_chess.javaChess.ply.IAnalyzedPly;


/**
 * This class implements a ply, that was computed by the permanent brain
 * function.
 */
public class PreComputedPlyImpl implements PreComputedPly {

    // Instance variables
   
    /**
     * The computed ply.
     */
    IAnalyzedPly _ply;

    /**
     * The search depth, that was used to compute this ply.
     */
    int _searchDepth;


    // Constructors

    /**
     * Create a new instance of a computed ply.
     *
     * @param ply The computed ply.
     * @param searchDepth The used search depth.
     */
    public PreComputedPlyImpl( IAnalyzedPly ply, int searchDepth) {
	setPly( ply);
	setSearchDepth( searchDepth);
    }


    // Methods

    /**
     * Get the computed ply.
     *
     * @return The computed ply.
     */
    public final IAnalyzedPly getPly() {
	return _ply;
    }

    /**
     * Set the computed ply.
     *
     * @param ply The computed ply.
     */
    private final void setPly( IAnalyzedPly ply) {
	_ply = ply;
    }

    /**
     * Get the used search depth.
     *
     * @return The used search depth.
     */
    public final int getSearchDepth() {
	return _searchDepth;
    }

    /**
     * Set the used search depth.
     *
     * @param searchDepth The used search depth.
     */
    private final void setSearchDepth( int searchDepth) {
	_searchDepth = searchDepth;
    }
}
