/*
 * LoadGameAction - A action to load a game. Copyright (C) 2003 The Java-Chess team
 * <info@java-chess.de> This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free Software Foundation;
 * either version 2 of the License, or (at your option) any later version. This program is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details. You should have received a copy of the GNU General Public
 * License along with this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 */

package com.github.warpwe.javachess.action;

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.github.warpwe.javachess.notation.IGameNotation;
import com.github.warpwe.javachess.pgn.PGNFile;

import antlr.RecognitionException;
import antlr.TokenStreamException;

/**
 * This class implements a action to load a game from a PGN file.
 */
public class LoadGameAction extends JavaChessAction {

  // Instance variables
  private static final long serialVersionUID = 6695396249446848955L;

  // Constructors
  /**
   * Create a new action instance.
   */
  public LoadGameAction() {
    super("Load game");
  }

  // Methods

  /**
   * The actual action.
   * 
   * @param event
   *          The event, that caused this action.
   */
  public void actionPerformed(ActionEvent event) {

    // Missing: save current project?

    JFileChooser chooser = new JFileChooser();

    chooser.setDialogTitle("Load pgn file");
    chooser.setFileFilter(SaveGameAsAction.getPGNFileFilter());

    int retval = chooser.showOpenDialog(null);
    if (retval == JFileChooser.APPROVE_OPTION) {

      File file = chooser.getSelectedFile();
      try {
        PGNFile pgnFile = new PGNFile(new BufferedReader(new FileReader(file)));
        IGameNotation notation = pgnFile.readGame();
      }
      catch (FileNotFoundException fe) {
        JOptionPane.showMessageDialog(null, "File " + file.getName() + " not found!",
            "File not found", JOptionPane.ERROR_MESSAGE);
      }
      catch (RecognitionException re) {
        throw new IllegalStateException("RecognitionExeption");
      }
      catch (TokenStreamException te) {
        throw new IllegalStateException("TokenStreamExeption");
      }
    }
  }
}
