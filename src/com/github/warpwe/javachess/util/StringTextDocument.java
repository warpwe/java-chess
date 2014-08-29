/*
 * StringTextDocument - The document as edit template for JTextField in the dialog to setup/edit the
 * board position. Copyright (C) 2003 The Java-Chess team <info@java-chess.de> This program is free
 * software; you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either version 2 of the License, or (at
 * your option) any later version. This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received
 * a copy of the GNU General Public License along with this program; if not, write to the Free
 * Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */

package com.github.warpwe.javachess.util;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * Description: This class is used to verify validate inputs in JTextFields such as in the
 * setup/edit position dialog For further detailed information look at core Java 2 part 1 basics,
 * chapter 9.3.2 p.496ff (Sun microsystems, Horstmann/Cornell) ISBN 3-8272-6016-7, ca. 50 EUR
 * Copyright: Copyright (c) 2003 The Java-Chess team <info@java-chess.de> Organisation: The
 * Java-Chess team
 * 
 * @author Faber
 * @version 1.0
 */

public class StringTextDocument extends PlainDocument {

  // Constants what to check for
  private final int CHECK_FOR_SQUARE = 0;
  private final int CHECK_FOR_EP_SQUARE = 1;
  private final int CHECK_FOR_NUMBER = 2;
  private final int CHECK_FOR_CHAR_NUM = 3;

  /**
   * The number of allowed characters
   */
  private int iCharacterCount = 50;

  /**
   * Flag whether to check for numbers only
   */
  private boolean bCheckForNumbers = false;

  /**
   * The type what and how to check
   */
  private int iType;

  /**
   * Constructor
   *
   * @param characterCount
   *          The number of allowed character to enter in the JTextField
   */
  public StringTextDocument(int type, int characterCount) {
    super();
    iCharacterCount = characterCount;
    iType = type;
  }

  /**
   * Constructor
   *
   * @param characterCount
   *          The number of allowed character to enter in the JTextField
   * @param checkForNumbers
   *          Flag whether the string to enter has to be parsed for numbers >0 only
   */
  // public StringTextDocument(int characterCount, boolean checkForNumbers)
  // {
  // super();
  // this.iCharacterCount = characterCount;
  // this.bCheckForNumbers = checkForNumbers;
  // }

  /**
   * This method has to be overwritten for personal use. The code below is taken from the source
   * mentioned in the class description
   */
  public void insertString(int offset, String s, AttributeSet attributeSet)
      throws BadLocationException {
    if (s == null)
      return;

    String oldString = getText(0, getLength());
    String newString = oldString.substring(0, offset) + s + oldString.substring(offset);

    if (canBecomeValid(newString))
      super.insertString(offset, s, attributeSet);
  }

  /**
   * Parse the string for number with value > 0
   *
   * @param sNewString
   *          The string to parse
   * @return <code>true</code> when the given string is a number with value > 0
   */
  private boolean isValid(String sNewString) {
    try {
      int iVal = Integer.parseInt(sNewString);
      if (iVal > 0)
        return true;
      return false;
    }
    catch (NumberFormatException e) {
      return false;
    }
  }

  /**
   * Returns if the new string (after inserting a character or pasted string) is valid
   * 
   * @param s
   *          The string to check
   * @return <code>true</code> if the string is valid, based on the user's conditions given in the
   *         constructor
   */
  public boolean canBecomeValid(String s) {
    switch (iType) {
      case CHECK_FOR_CHAR_NUM: // only character count
        return (s.length() <= iCharacterCount && s.length() >= 0);
      case CHECK_FOR_NUMBER: // numbers only
        return (s.length() <= iCharacterCount && s.length() >= 0) && isValid(s);
      case CHECK_FOR_EP_SQUARE: // a3-d3 and a6-d6 are valid squares for en passant
        return (this.isValidSquare(s, true));
      case CHECK_FOR_SQUARE: // between a1 and h8
        return (this.isValidSquare(s, false));

      default:
        return false;
    }
  }

  /**
   * Checks the character for being between a and h which limit the chessboard lines
   * 
   * @param charToCheck
   *          The character to check
   * @return <code>true</code> when between a and h
   */
  private boolean isValidLine(char charToCheck) {
    if ((charToCheck >= 'a' && charToCheck <= 'h') || (charToCheck >= 'A' && charToCheck <= 'H')) {
      return true;
    }
    else {
      return false;
    }

  }

  /**
   * Checks whether the given string consists of a valid square from a chessboard between a1 and h8
   *
   * @param sString
   *          The string to parse
   * @param bEnPassantSquare
   *          Flag to check for en passant (3rd row) or not
   * @return <code>true</code> if square is valid on a chessboard
   */
  private boolean isValidSquare(String sString, boolean bEnPassantSquare) {
    if (sString.length() <= iCharacterCount && sString.length() > 0) {
      if (isValidLine(sString.charAt(0))) {
        if (sString.length() == 2) {
          if (bEnPassantSquare == false) {
            if (sString.charAt(1) >= '1' && sString.charAt(1) <= '8') {
              return true;
            }
          }
          else {
            if (sString.charAt(1) == '3' || sString.charAt(1) == '6') {
              return true;
            }
            else {
              return false;
            }
          }
        }
        else {
          return true;
        }
      }
      else {
        return false;
      }
    }
    return false;

  }
}
