/*
 * EditMenu - A class to create the Edit menu. Copyright (C) 2002,2003 Harald Faber
 * <info@java-chess.de> This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free Software Foundation;
 * either version 2 of the License, or (at your option) any later version. This program is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details. You should have received a copy of the GNU General Public
 * License along with this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 */

package com.github.warpwe.javachess.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.github.warpwe.javachess.dialogs.PlayerDialog;
import com.github.warpwe.javachess.dialogs.SetupBoardDialog;
import com.github.warpwe.javachess.renderer2d.NotationPanel;
import com.github.warpwe.javachess.util.Tools;

/**
 * This class constructs the Edit menu.
 */
public class EditMenu implements ActionListener {

  /**
   * Holds the NotationPanel, e.g. for editing the players names.
   */
  private NotationPanel notationPanel;

  /**
   * The menu item to edit the white player name
   */
  JMenuItem jmiWhitePlayerName = new JMenuItem();

  /**
   * The menu item to edit the black player name
   */
  JMenuItem jmiBlackPlayerName = new JMenuItem();

  /**
   * The menu item to edit the black player name
   */
  JMenuItem jmiEditPosition = new JMenuItem();

  /**
   * Standardconstructor
   */
  public EditMenu() {
    this.notationPanel = null;
  }

  /**
   * Interface method. Currently displays input dialogs to edit white and black player names
   * 
   * @param e
   *          The action event
   */
  public void actionPerformed(ActionEvent e) {
    if (e.getSource().equals(jmiEditPosition)) {
      SetupBoardDialog setupBoardDialog = new SetupBoardDialog();
      Tools.setDialogToCenter(setupBoardDialog);
      setupBoardDialog.setModal(true);
      setupBoardDialog.show();
    }
    else {
      if (notationPanel != null) {
        PlayerDialog playerDialog = null;
        boolean bWhiteOrBlack = true;

        if (e.getSource().equals(jmiWhitePlayerName)) {
          playerDialog = new PlayerDialog(1, notationPanel.getWhitePlayerName());
          bWhiteOrBlack = true;
        }
        else if (e.getSource().equals(jmiBlackPlayerName)) {
          playerDialog = new PlayerDialog(2, notationPanel.getBlackPlayerName());
          bWhiteOrBlack = false;
        }

        if (playerDialog != null) {
          Tools.setDialogToCenter(playerDialog);
          playerDialog.show();

          String sNewName = playerDialog.getNewName();
          if (bWhiteOrBlack == true) {
            notationPanel.setWhitePlayerName(sNewName);
          }
          else {
            notationPanel.setBlackPlayerName(sNewName);
          }
        }
      }
    }
  }

  /**
   * Return a menu from the chess engine, where the user can change the settings.
   *
   * @return A menu for the engine settings.
   */
  public final JMenu getMenu() {
    // Create a new menu.
    JMenu jmEditMenu = new JMenu("Edit");

    // Add menu items
    jmiBlackPlayerName.setText("Black player name");
    jmiWhitePlayerName.setText("White player name");
    jmiEditPosition.setText("Edit/Setup position");
    jmiEditPosition.setMnemonic('S');

    jmiBlackPlayerName.addActionListener(this);
    jmiWhitePlayerName.addActionListener(this);
    jmiEditPosition.addActionListener(this);

    jmEditMenu.add(jmiWhitePlayerName);
    jmEditMenu.add(jmiBlackPlayerName);
    jmEditMenu.addSeparator();
    jmEditMenu.add(jmiEditPosition);

    jmEditMenu.setMnemonic(KeyEvent.VK_E);
    return (jmEditMenu);
  }

  /**
   * Sets the NotationPanel to be able to perform some actions there
   * 
   * @param panel
   *          The NotationPanel object
   */
  public final void setNotationPanel(NotationPanel panel) {
    this.notationPanel = panel;
  }

}
