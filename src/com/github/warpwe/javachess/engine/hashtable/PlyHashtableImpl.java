/*
 * PlyHashtableImpl - A class to store chess plies in a hashtable. Copyright (C) 2003 The Java-Chess
 * team <info@java-chess.de> This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free Software Foundation;
 * either version 2 of the License, or (at your option) any later version. This program is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details. You should have received a copy of the GNU General Public
 * License along with this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 */

package com.github.warpwe.javachess.engine.hashtable;

import java.util.Hashtable;
import java.util.LinkedList;

import com.github.warpwe.javachess.board.Board;
import com.github.warpwe.javachess.ply.IPly;

/**
 * This class implements the functionality to store chess plies in a hashtable.
 */
public class PlyHashtableImpl implements PlyHashtable {

  // Instance variables

  /**
   * The maximum number of entries.
   */
  private int maxSize;

  /**
   * A hashtable to store the plies.
   */
  Hashtable<Long, PlyHashtableEntry> hashtable;

  /**
   * A linked list to reproduce the order of the inserted plies.
   */
  LinkedList<Long> orderedList;

  // Constructors

  /**
   * Create a new hashtable instance with a given maximum size.
   *
   * @param maxSize
   *          The maximum number of entries.
   */
  public PlyHashtableImpl(int maxSize) {
    hashtable = new Hashtable<Long, PlyHashtableEntry>();
    orderedList = new LinkedList<Long>();
    setMaximumSize(maxSize);
  }

  // Methods

  /**
   * Reset the hashtables for a new game.
   */
  public void reset() {
    hashtable.clear();    // Remove all the entries from the hashtable.
    orderedList.clear();  // And the ordered list.
  }

  /**
   * Get the maximum number of entries in the hashtable.
   *
   * @return The maximum number of entries in the hashtable.
   */
  public final int getMaximumSize() {
    return maxSize;
  }

  /**
   * Set the maximum number of entries in the hashtable.
   *
   * @param maximumEntries
   *          The new maximum number of entries.
   */
  public final void setMaximumSize(int maximumEntries) {
    maxSize = maximumEntries;

    // Remove the oldest entries, if the current size
    // is bigger than the new maximum size.
    while (getSize() > getMaximumSize()) {
      removeOldestEntry();
    }
  }

  /**
   * Get the current number of entries.
   *
   * @return The current number of entries.
   */
  public final int getSize() {
    return hashtable.size();
  }

  /**
   * Try to push a new entry into the hashtable.
   *
   * @param entry
   *          The new entry, that the hashtable might store.
   */
  public final void pushEntry(PlyHashtableEntry ply) {

    // Compute and store the key for this ply.
    Long hashKey = new Long(ply.hashKey());

    // Check if this ply is not already in the hashtable
    PlyHashtableEntry oldEntry = (PlyHashtableEntry) (hashtable.get(hashKey));
    if (oldEntry != null) {  // There is a entry with the same key

      if (oldEntry.getSearchDepth() >= ply.getSearchDepth()) {
        return;  // This entry shouldn't be replaced.
      }
    }
    else {  // Now we actually add a new entry to the table.

      // Check, if the hashtable has not exceeded it's maximum capacity
      // and remove older entries, if necessary.
      while (getSize() >= getMaximumSize()) {
        removeOldestEntry();
      }
    }

    // Put the new ply into the hashtable
    hashtable.put(hashKey, ply);

    // Append the new ply at the end of the list of plies.
    // ATTENTION: this will only work, if there's only _one_
    // ply for each key!
    orderedList.add(hashKey);
  }

  /**
   * Get the stored ply for a given board and piece color.
   *
   * @param board
   *          The current board.
   * @param white
   *          true, if we want to ply with the white pieces.
   */
  public final IPly getPly(Board board, boolean white) {

    // Compute the hashkey for the board and piece color and try to get a ply.
    PlyHashtableEntry entry = (PlyHashtableEntry) (hashtable.get(new Long(PlyHashtableEntryImpl
        .hashKey(board, white))));

    // Check, if the board from the entry is identical to the passed board.
    if ((entry != null) && entry.getBoard().equals(board)) {
      return entry.getPly();
    }

    // No ply for this board found.
    return null;
  }

  /**
   * Try to remove the oldest entry. ATTENTION: this version will only work, if there's only _one_
   * entry for each hashkey!
   */
  public final void removeOldestEntry() {

    // Get and remove the oldest ply from the ordered list.
    hashtable.remove(orderedList.removeFirst());
  }
}
