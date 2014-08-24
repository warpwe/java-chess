/*
        UnsychronizedArrayStack - A unsynchronized stack class (based on an array)
	                          for better performance with small stack sizes.

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

package com.github.warpwe.javachess.util;

import java.util.Iterator;


/**
 * This class implements a stack data structure with no synchronization
 * for better performance (compared to the standard J2 Stack implementation).
 * It uses a array, instead of a linked list.
 */
public class UnsynchronizedArrayStack {

    // Instance variables

    /**
     * An array to store the items on the stack.
     */
    private Object [] _items;

    /**
     * The current number of items on the stack.
     */
    private int _nItems;

    /**
     * The size for the stack increment, if it's currently too small.
     */
    private int _incSize = 30;


    // Constructors
    
    /**
     * Create a new stack instance.
     */
    public UnsynchronizedArrayStack() {
	this( 50);
    }

    /**
     * Create a new stack instance with a given initial
     * capacity.
     *
     * @param capacity The initial capacity.
     */
    public UnsynchronizedArrayStack( int capacity) {
	_nItems = 0;
	_items = new Object[ capacity];
    }


    // Methods

    /**
     * Test, if this stack is empty.
     *
     * @return true, if the stack is empty. False otherwise.
     */
    public final boolean empty() {
	return _nItems == 0;
    }

    /**
     * Look at the object, that is at the top of the stack without removing it.
     *
     * @return The object at the top of the stack.
     */
    public final Object peek() {
	return empty() ? null : _items[ _nItems - 1];
    }

    /**
     * Remove the object at the top of the stack and return it.
     *
     * @return The object at the top of the stack.
     */
    public final Object pop() {
	if( ! empty()) {
	    Object item = _items[ --_nItems];
	    _items[ _nItems] = null;
	    return item;
	}
	return null;
    }

    /**
     * Push an item on the stack.
     *
     * @param item The item to push on the stack.
     */
    public final void push( Object item) {
	try {
	    _items[ _nItems] = item;
	    _nItems++;
	} catch( ArrayIndexOutOfBoundsException ae) {
	    increaseCapacity();
	    push( item);
	}
    }

    /**
     * Return the position of a object on the stack, or -1 if it
     * is not found.
     *
     * @return The position of the object on the stack.
     */
    public final int search( Object o) {
	for( int i = 0; i < _nItems; i++) {
	    if( _items[i].equals( o)) {
		return i;
	    }
	}
	return -1;
    }

    /**
     * Remove all the elements of the stack.
     */
    public final void clear() {
	for( int i = 0; i < _nItems; i++) {
	    _items[ i] = null;
	}
	_nItems = 0;
    }
    
    /**
     * Get the current number of elements on the stack.
     *
     * @param The current size of the stack.
     */
    public final int size() {
	return _nItems;
    }

    /**
     * Get a iterator for the elements of this stack.
     *
     * @return A iterator for the elements of this stack.
     */
    public final Iterator iterator() {
	return new ArrayStackIterator( _items, _nItems);
    }

    /**
     * Increase the capacity of the stack.
     */
    private void increaseCapacity() {

	// Create a new bigger array.
	Object [] newStack = new Object[ _items.length + _incSize];

	// Copy the content of the old array to the new one.
	System.arraycopy( _items, 0, newStack, 0, _nItems);

	// Set the new array.
	_items = newStack;
    }
}
