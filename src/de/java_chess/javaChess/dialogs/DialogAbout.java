/*
  AboutDialog - A about box for JavaChess with some info on the project.

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

package de.java_chess.javaChess.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

/**
 * This class contain all informations about this programm
 */
@SuppressWarnings("serial")
public class DialogAbout extends JDialog implements ActionListener {

    /**
     * The instance of this dialog (implements the singleton pattern). 
     */
    private static DialogAbout _instance = null;

  /** This panel contain the title */
  private JPanel jPanel1 = new JPanel();
  /** The title shows the name and version of this program */
  private JLabel jLTitle = new JLabel();
  /** This JTabbedPane contain four topics with diffrend informations */
  private JTabbedPane tPAboutDialog = new JTabbedPane();
  /** About contain a short information about this program */
  private JTextArea tAAbout = new JTextArea();
  /** Authors contain the authors, who developed this program */
  private JTextArea tAAuthors = new JTextArea();
  /** This panel contain only the close Button */
  private JPanel jPanel2 = new JPanel();
  /** The CloseButton hide the dialog */
  private JButton bCloseButton = new JButton();
  /** Thanks contain all thanks to people, who help to realized this program */
  private JTextArea tAThanks = new JTextArea();
  /** License shows the license, which is use */
  private JTextArea tALicense = new JTextArea();
  @SuppressWarnings("unused")
private TitledBorder titledBorder1;

  //////////////////////////////////////////////////////////
  ////////////////////// Constructor //////////////////////
  ////////////////////////////////////////////////////////
  private DialogAbout() {
    try {
      jbInit();
      this.setTitle("About Java-Chess");
      this.setSize(400,380);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  ////////////////////////////////////////////
  //////////////// Methods //////////////////
  ///////////////////////////////////////////

    /**
     * Get the one and only instance of this class
     * (singleton pattern).
     *
     * @return The only instance of this class.
     */
    public static DialogAbout getInstance() {

	// If there's no instance yet, create one.
	if( _instance == null) {
	    _instance = new DialogAbout();
	}
	return _instance;
    }

  @SuppressWarnings("deprecation")
private void jbInit() throws Exception {
    titledBorder1 = new TitledBorder("");

    jLTitle.setBackground(Color.white);
    jLTitle.setForeground(Color.black);
    jLTitle.setText("Java-Chess 0.1.0 pre-alpha3 2014-05-26");

    tAAuthors.setBackground(Color.black);
    tAAuthors.setForeground(Color.white);
    tAAuthors.setNextFocusableComponent(tAThanks);
    tAAuthors.setEditable(false);
    tAAuthors.setMargin(new Insets(20, 70, 50, 20));
    // I use absolute path, you have to change it
    // setRegister(tAAuthors, "/home/weigo/Documents/Progs/authors");
    setRegister(tAAuthors, getDialogResource( "authors.txt"));

    tAThanks.setBackground(Color.black);
    tAThanks.setForeground(Color.white);
    tAThanks.setNextFocusableComponent(tALicense);
    tAThanks.setEditable(false);
    tAThanks.setMargin(new Insets(20, 50, 50, 20));
    // I use absolute path, you have to change it
    // setRegister(tAThanks, "/home/weigo/Documents/Progs/thanks");
    setRegister(tAThanks, getDialogResource( "thanks.txt"));

    tALicense.setBackground(Color.black);
    tALicense.setForeground(Color.white);
    tALicense.setNextFocusableComponent(bCloseButton);
    tALicense.setEditable(false);
    tALicense.setMargin(new Insets(20, 50, 50, 20));
    // I use absolute path, you have to change it
    // setRegister(tALicense, "/home/weigo/Documents/Progs/license");
    setRegister(tALicense, getDialogResource( "license.txt"));

    tAAbout.setBackground(Color.black);
    tAAbout.setForeground(Color.white);
    tAAbout.setNextFocusableComponent(tAAuthors);
    tAAbout.setEditable(false);
    tAAbout.setMargin(new Insets(20, 70, 50, 20));
    // I use absolute path, you have to change it
    // setRegister(tAAbout, "/home/weigo/Documents/Progs/about");
    setRegister(tAAbout, getDialogResource( "about.txt"));

    bCloseButton.setBackground(Color.black);
    bCloseButton.setForeground(Color.red);
    bCloseButton.setNextFocusableComponent(tAAbout);
    bCloseButton.setToolTipText("close the about dialog");
    bCloseButton.setMnemonic('0');
    bCloseButton.setText("close");
    bCloseButton.addKeyListener(new DialogAbout_CloseButton_keyAdapter(this));

    jPanel2.setBackground(Color.lightGray);
    jPanel2.setBorder(BorderFactory.createEtchedBorder());
    this.getContentPane().setBackground(Color.lightGray);

    jPanel1.setBackground(Color.lightGray);
    jPanel1.add(jLTitle, null);
    this.getContentPane().add(jPanel2, BorderLayout.SOUTH);

    jPanel2.add(bCloseButton, null);
    this.getContentPane().add(tPAboutDialog, BorderLayout.CENTER);

    tPAboutDialog.add(tAAbout,   "About");
    tPAboutDialog.add(tAAuthors,   "Authors");
    this.getContentPane().add(jPanel1, BorderLayout.NORTH);
    tPAboutDialog.add(tAThanks,  "Thanks to");
    tPAboutDialog.add(tALicense,   "License agreements");


    bCloseButton.addActionListener(this);
  }

  public void actionPerformed(ActionEvent event){
    this.setVisible(false);
  }

  @SuppressWarnings("deprecation")
void CloseButton_keyReleased(KeyEvent e) {
    if( e.getKeyCode() == KeyEvent.VK_ENTER )
      this.hide();
  }

    void setRegister(JTextArea textArea, Reader in) {
	try {
	    char[] buffer = new char[4096];
	    int len;
	    textArea.setText("");
	    
	    while(( len = in.read(buffer)) != -1) {
		String s = new String(buffer, 0, len);
		textArea.append(s);
	    }
	    textArea.setCaretPosition(0);
	} catch(IOException e) {
	    textArea.setText( e.getClass().getName() + ": " + e.getMessage());
	}
    }

    /**
     * Get a dialog resource as a reader.
     *
     * @param resourceName The name of the resource.
     *
     * @return A reader to read from.
     */
    private final Reader getDialogResource( String resourceName) {

	try {
	    // Create a URL into the jar.
//	    URL resourceURL = new URL( "jar:file:javaChess.jar!/de/java_chess/javaChess/dialogs/resources/" + resourceName);
		URL resourceURL = new URL( "file:/Java-Chess-0.1.0pre-alpha3/src/de/java_chess/javaChess/dialogs/resources/" + resourceName);
	    return new BufferedReader( new InputStreamReader( resourceURL.openStream()));
	} catch( IOException ie) {

	    // Ugly! But the same behaviour than before...
	    return new StringReader( ie.getMessage());
	}
    }
}

class DialogAbout_CloseButton_keyAdapter extends java.awt.event.KeyAdapter {
  private DialogAbout adaptee;

  DialogAbout_CloseButton_keyAdapter(DialogAbout adaptee) {
    this.adaptee = adaptee;
  }
  public void keyReleased(KeyEvent e) {
    adaptee.CloseButton_keyReleased(e);
  }
}
