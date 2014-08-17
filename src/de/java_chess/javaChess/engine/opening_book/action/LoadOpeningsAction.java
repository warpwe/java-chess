/*
  LoadOpeningsAction - A action to load a game.

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

package de.java_chess.javaChess.engine.opening_book.action;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JFileChooser;

import de.java_chess.javaChess.action.JavaChessAction;
import de.java_chess.javaChess.action.SaveGameAsAction;
import de.java_chess.javaChess.engine.opening_book.OpeningBook;


/**
 * This class loads openings recursively from directories and PGN files.
 */
public class LoadOpeningsAction extends JavaChessAction {

    // Instance variables

    /**
     * The opening book, we are operating on.
     */
    OpeningBook _openingBook;
    

    // Constructors

    /**
     * Create a new action instance.
     *
     * @param book The opening book, we are operating on.
     */
    public LoadOpeningsAction( OpeningBook book) {
	super( "Load openings from PGN...");
	setOpeningBook( book);
    }
    
    // Methods

    /**
     * The actual action.
     *
     * @param event The event, that caused this action.
     */
    public void actionPerformed( ActionEvent event) {

        JFileChooser chooser = new JFileChooser();

        chooser.setDialogTitle( "Load opening from pgn files");
        chooser.setFileFilter( SaveGameAsAction.getPGNFileFilter());

	// Allow the user to select directories.
	chooser.setFileSelectionMode( JFileChooser.FILES_AND_DIRECTORIES);

	int retval = chooser.showOpenDialog( null);

        if( retval == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
	    processFileOrDirectory( file);
        } 
    }

    /**
     * Process a file or directory.
     *
     * @param file The file to process.
     */
    private final void processFileOrDirectory( File file) {

	// If this file is a directory, read all the files in it.
	if( file.isDirectory()) {
	    
	    String [] files = file.list();  // Get the content of the directory

	    // Now create an absolute file from each name and process it.
	    for( int i = 0; i < files.length; i++) {
		processFileOrDirectory( new File( file, files[i]));
	    }             
	} else {  // This is a single file, so
	          // check, if it might be a PGN file.
	    if( SaveGameAsAction.getPGNFileFilter().accept( file)) {
		processPGNfile( file);
	    }
	}
    }

    /**
     * Process a single PGN file.
     *
     * @param file The PGN file to process.
     */
    private final void processPGNfile( File file) {
	getOpeningBook().addPGNopening( file);
    }

    /**
     * Get the current opening book.
     *
     * @return The current opening book.
     */
    private final OpeningBook getOpeningBook() {
	return _openingBook;
    }

    /**
     * Set the opening book, we are operating on.
     *
     * @param book The new opening book.
     */
    private final void setOpeningBook( OpeningBook book) {
	_openingBook = book;
    }
}
