/*
        ArrayStackIterator - A iterator for the unsynchronized stack class.

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

package de.java_chess.javaChess.util;

import java.util.Iterator;


/**
 * This is a helper class to iterate over the ArrayStack class.
 */
public class ArrayStackIterator implements Iterator {

    // Instance variables

    /**
     * The items to iterate over.
     */
    private Object [] _items;

    /**
     * The number of items.
     */
    private int _nItems;

    /**
     * The current element.
     */
    private int _curElement;


    // Constructors

    /**
     * Create a new iterator instance.
     *
     * @param items The array with the items.
     * @param nItems The number of items (since the array is most likely longer).
     */
    public ArrayStackIterator( Object [] items, int nItems) {
	_items = items;
	_nItems = nItems;
	_curElement = 0;
    }
    

    // Methods

    /**
     * Check, if there are more elements to iterate over.
     *
     * @return true, if there are more elements.
     */
    public final boolean hasNext() {
	return _curElement < _nItems;
    }

    /**
     * Get the next elements from the collection.
     *
     * @return The next element from the collection.
     */
    public final Object next() {
	return _items[ _curElement++];
    }

    /**
     * Remove the returned object (just a dummy in this case).
     */
    public final void remove() {
	// Do nothing...
    }
}

