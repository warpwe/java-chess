/*
 * SetupBoardDialog - The dialog to setup/edit the board position. Copyright (C) 2003 The Java-Chess
 * team <info@java-chess.de> This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free Software Foundation;
 * either version 2 of the License, or (at your option) any later version. This program is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details. You should have received a copy of the GNU General Public
 * License along with this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 */

package com.github.warpwe.javachess.dialogs;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

import com.github.warpwe.javachess.GameController;
import com.github.warpwe.javachess.bitboard.BitBoardImpl;
import com.github.warpwe.javachess.renderer2d.ChessBoardRenderer2D;
import com.github.warpwe.javachess.util.StringTextDocument;

/**
 * Copyright: Copyright (c) 2003 The Java-Chess team <info@java-chess.de> Organisation: The
 * Java-Chess team
 * 
 * @author Faber
 * @version 1.0
 */

public class SetupBoardDialog extends JDialog {
  private GridBagLayout gblContentPane = new GridBagLayout();
  private GridBagLayout gblBoard = new GridBagLayout();

  private BitBoardImpl bitBoard;
  private GameController gameController;
  private ChessBoardRenderer2D boardRenderer;
  private JPanel jpBoard = new JPanel();
  private JPanel jpPieces = new PieceSelectionPanel();
  private JPanel jpButtons = new JPanel();
  private JPanel jpMoveRight = new JPanel();
  private GridBagLayout gblButtons = new GridBagLayout();
  private GridBagLayout gblMoveRight = new GridBagLayout();
  private ButtonGroup bgMoveRight = new ButtonGroup();
  private JRadioButton jrbWhiteToMove = new JRadioButton();
  private JRadioButton jrbBlackToMove = new JRadioButton();
  private JButton jbOk = new JButton();
  private JButton jbCancel = new JButton();
  private JButton jbStartPosition = new JButton();
  private JButton jbClearBoard = new JButton();
  private JLabel jlMoveRight = new JLabel();
  private JPanel jpCastling = new JPanel();
  private GridBagLayout gblCastling = new GridBagLayout();
  private JCheckBox checkBoxWhiteShort = new JCheckBox();
  private JCheckBox checkBoxWhiteLong = new JCheckBox();
  private JCheckBox checkBoxBlackShort = new JCheckBox();
  private JCheckBox checkBoxBlackLong = new JCheckBox();
  private JLabel jlCastling = new JLabel();
  private JPanel jpEnPassant = new JPanel();
  private GridBagLayout gridBagLayout1 = new GridBagLayout();
  private JLabel jlEnPassant = new JLabel();
  private JTextField jtEnPassant = new JTextField();
  private JPanel jpMoveNumber = new JPanel();
  private GridBagLayout gblMoveNumber = new GridBagLayout();
  private JLabel jlMoveNumber = new JLabel();
  private JTextField jtMoveNumber = new JTextField();
  private Cursor oldCursor;

  /**
   * Constructor
   */
  public SetupBoardDialog() {
    super();
    String sMessage = "This dialog is under construction.\n\n";
    sMessage += "You can try some buttons and play around a bit,\n";
    sMessage += "but it is not yet possible to place\n";
    sMessage += "one or more pieces on the board.\n";
    sMessage += "This point has very high priority\n";
    sMessage += "on our ToDo-list for the next release.";
    String sTitle = "Important note!";

    JOptionPane.showMessageDialog(null, sMessage, sTitle, JOptionPane.INFORMATION_MESSAGE);

    try {
      jbInit();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Method for constructing the GUI
   */
  private void jbInit() throws Exception {

    this.setTitle("JavaChess - Setup position");
    this.getContentPane().setLayout(gblContentPane);
    this.setSize(new Dimension(700, 650));
    this.addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        this_windowClosing(e);
      }
    });

    bitBoard = new BitBoardImpl();
    // Hm:
    // gameController = new GameController( _game, _gameNotation, _engine, _board, _gameTimer);
    // boardRenderer = new ChessBoardRenderer2D( gameController, bitBoard );
    boardRenderer = new ChessBoardRenderer2D(null, bitBoard);
    // gameController.setRenderer( boardRenderer );

    this.jpBoard.setLayout(gblBoard);
    this.jpBoard.setPreferredSize(new Dimension(430, 430));
    jpBoard.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseEntered(MouseEvent e) {
        jpBoard_mouseEntered(e);
      }

      public void mouseExited(MouseEvent e) {
        jpBoard_mouseExited(e);
      }
    });
    this.jpBoard.setMinimumSize(new Dimension(430, 430));

    jpCastling.setLayout(gblCastling);
    jpCastling.setBorder(BorderFactory.createLineBorder(Color.black));
    jlCastling.setText("Castling rights:");
    checkBoxWhiteShort.setText("White short (O-O)");
    checkBoxWhiteLong.setText("White long (O-O-O)");
    checkBoxBlackShort.setText("Black short (O-O)");
    checkBoxBlackLong.setText("Black long (O-O-O)");

    jpMoveRight.setLayout(gblMoveRight);
    jpMoveRight.setBorder(BorderFactory.createLineBorder(Color.black));
    jpMoveRight.setMinimumSize(new Dimension(135, 73));
    jpMoveRight.setPreferredSize(new Dimension(135, 73));
    jlMoveRight.setText("Move right:");
    jrbWhiteToMove.setText("White to move");
    jrbWhiteToMove.setSelected(true);
    jrbBlackToMove.setText("Black to move");
    jbClearBoard.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jbClearBoard_actionPerformed(e);
      }
    });
    jbStartPosition.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jbStartPosition_actionPerformed(e);
      }
    });
    bgMoveRight.add(jrbWhiteToMove);
    bgMoveRight.add(jrbBlackToMove);

    jpEnPassant.setLayout(gridBagLayout1);
    jlEnPassant.setText("EnPassant-Square:");
    jtEnPassant.setMinimumSize(new Dimension(108, 21));
    // Restrict the user input to 2 valid characters:
    jtEnPassant.setDocument(new StringTextDocument(1, 2));
    jtEnPassant.setPreferredSize(new Dimension(108, 21));
    jpEnPassant.setBorder(BorderFactory.createLineBorder(Color.black));
    jpEnPassant.setMinimumSize(new Dimension(135, 48));
    jpEnPassant.setPreferredSize(new Dimension(135, 48));

    jpMoveNumber.setLayout(gblMoveNumber);
    jlMoveNumber.setText("Move number:");
    jpMoveNumber.setBorder(BorderFactory.createLineBorder(Color.black));
    jpMoveNumber.setMinimumSize(new Dimension(135, 56));
    jpMoveNumber.setPreferredSize(new Dimension(135, 56));
    jtMoveNumber.setMinimumSize(new Dimension(108, 21));
    jtMoveNumber.setPreferredSize(new Dimension(108, 21));
    // Restrict the user input to 3 numbers:
    jtMoveNumber.setDocument(new StringTextDocument(2, 3));

    jpButtons.setLayout(gblButtons);

    jbOk.setToolTipText("Close dialog with current position");
    jbOk.setMnemonic(KeyEvent.VK_O);
    jbOk.setText("OK");
    jbOk.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jbOk_actionPerformed(e);
      }
    });

    jbCancel.setToolTipText("Abort and back to former position/game");
    jbCancel.setText("Cancel");
    jbCancel.setMnemonic(KeyEvent.VK_C);
    jbCancel.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jbCancel_actionPerformed(e);
      }
    });

    jbClearBoard.setToolTipText("Clears the complete board");
    jbClearBoard.setText("Clear board");
    jbClearBoard.setMnemonic(KeyEvent.VK_L);

    jbStartPosition.setToolTipText("Startup/Initial position");
    jbStartPosition.setText("Start position");
    jbStartPosition.setMnemonic(KeyEvent.VK_S);

    jpBoard.add(boardRenderer, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
        GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    this.getContentPane().add(
        jpPieces,
        new GridBagConstraints(0, 5, 2, 1, 1.0, 1.0, GridBagConstraints.WEST,
            GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jpBoard.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.white,
        Color.white, new Color(148, 145, 140), new Color(103, 101, 98)));

    this.getContentPane().add(
        jpBoard,
        new GridBagConstraints(0, 0, 1, 4, 1.0, 1.0, GridBagConstraints.NORTHWEST,
            GridBagConstraints.NONE, new Insets(20, 20, 2, 2), 0, 0));
    this.getContentPane().add(
        jpButtons,
        new GridBagConstraints(0, 6, 2, 1, 1.0, 1.0, GridBagConstraints.WEST,
            GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.getContentPane().add(
        jpMoveRight,
        new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
            GridBagConstraints.NONE, new Insets(20, 20, 0, 20), 0, 0));
    jpMoveRight.add(jrbWhiteToMove, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
        GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 4, 0, 0), 0, 0));
    jpMoveRight.add(jrbBlackToMove, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
        GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 4, 0, 0), 0, 0));
    jpMoveRight.add(jlMoveRight, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
        GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(4, 4, 0, 0), 0, 0));
    jpButtons.add(jbOk, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
        GridBagConstraints.NONE, new Insets(0, 0, 0, 20), 0, 0));
    jpButtons.add(jbCancel, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
        GridBagConstraints.NONE, new Insets(0, 0, 0, 20), 0, 0));
    jpButtons.add(jbStartPosition, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0,
        GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 20, 0, 20), 0, 0));
    jpButtons.add(jbClearBoard, new GridBagConstraints(0, 0, 1, 1, 0.0, 1.0,
        GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 20, 0, 20), 0, 0));
    this.getContentPane().add(
        jpCastling,
        new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
            GridBagConstraints.NONE, new Insets(20, 20, 0, 20), 0, 0));
    jpCastling.add(checkBoxBlackLong, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
        GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 4, 0, 0), 0, 0));
    jpCastling.add(checkBoxWhiteShort, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
        GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 4, 0, 0), 0, 0));
    jpCastling.add(checkBoxWhiteLong, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
        GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 4, 0, 0), 0, 0));
    jpCastling.add(checkBoxBlackShort, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
        GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 4, 0, 0), 0, 0));
    jpCastling.add(jlCastling, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
        GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(4, 4, 10, 0), 0, 0));
    this.getContentPane().add(
        jpEnPassant,
        new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
            GridBagConstraints.NONE, new Insets(20, 20, 0, 0), 0, 0));
    jpEnPassant.add(jtEnPassant, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
        GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 4, 4, 4), 0, 0));
    jpEnPassant.add(jlEnPassant, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
        GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(4, 4, 0, 0), 0, 0));
    this.getContentPane().add(
        jpMoveNumber,
        new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
            GridBagConstraints.NONE, new Insets(20, 20, 0, 0), 0, 0));
    jpMoveNumber.add(jtMoveNumber, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
        GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
    jpMoveNumber.add(jlMoveNumber, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
        GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));// Brett

  }

  /**
   * If this dialog is closed over the 'X' and not via File-Exit, a process would be still running
   *
   * @param e
   *          The Window event
   */
  private void this_windowClosing(WindowEvent e) {
    System.exit(0);
  }

  /**
   * Button Cancel pressed
   * 
   * @param e
   *          The event
   */
  private void jbCancel_actionPerformed(ActionEvent e) {
    this.dispose();
  }

  /**
   * Button Ok pressed
   * 
   * @param e
   *          The event
   */
  private void jbOk_actionPerformed(ActionEvent e) {
    this.hide();
  }

  /**
   * Mouse cursor entered the board: Use piece as mouse pointer over the board
   * 
   * @param e
   *          The event
   */
  void jpBoard_mouseEntered(MouseEvent e) {
    Image imageCursor;
    oldCursor = this.getCursor();
    // byte typeAndColor = ((Piece)jpPieces).getTypeAndColor();
    // URL url = getClass().getResource("BP.gif");
    // Image image = Toolkit.getDefaultToolkit().getImage( url );
    // Cursor myCursor =
    // Toolkit.getDefaultToolkit().createCustomCursor(((Piece)jpPieces).getSelectedPieceImage(), new
    // Point(20, 20), "Piece");
    imageCursor = ((PieceSelectionPanel) jpPieces).getSelectedPieceImage();
    ((PieceSelectionPanel) jpPieces).initializeSelectedIconLabel();
    if (imageCursor != null) {
      Cursor myCursor = Toolkit.getDefaultToolkit().createCustomCursor(imageCursor,
          new Point(20, 20), "Piece");
      jpBoard.setCursor(myCursor);
    }
  }

  /**
   * Mouse cursor left the board: Use normal mouse pointer again
   * 
   * @param e
   *          The event
   */
  void jpBoard_mouseExited(MouseEvent e) {
    jpBoard.setCursor(oldCursor);
  }

  /**
   * Button Clear board pressed; self-explaining
   * 
   * @param e
   *          The event
   */
  void jbClearBoard_actionPerformed(ActionEvent e) {
    this.boardRenderer.clearBoard();
  }

  /**
   * Button Start position pressed; self-explaining
   * 
   * @param e
   *          The event
   */
  void jbStartPosition_actionPerformed(ActionEvent e) {
    bitBoard.initialPosition();
    this.boardRenderer.reset();
    this.validate();
  }

}
