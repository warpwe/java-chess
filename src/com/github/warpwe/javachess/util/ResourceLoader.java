/*
  ResourceLoader - A utility class to load resources like images etc.

  Copyright (C) 2002,2003 Andreas Rueckert <mail@andreas-rueckert.de>

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

import java.awt.Image;
import java.awt.Toolkit;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * A utility class to load resources like images etc.
 */
public class ResourceLoader {

    // Static variables

    /**
     * The only instance of this class (singleton pattern).
     */
    private static ResourceLoader _instance = null;


    // Instance variables

    /**
     * A list of locations to load from.
     */
    private java.util.List _locations;
    

    // Constructors

    /**
     * Create a new loader instance.
     */
    private ResourceLoader() {
	_locations = new ArrayList();
    }


    // Methods

    /**
     * Get the only instance of this class.
     *
     * @return The only instance of this class.
     */
    public static final ResourceLoader getInstance() {
	if( _instance == null) {
	    _instance = new ResourceLoader();
	}
	return _instance;
    }
    
    /**
     * Add a new location to load resources from.
     *
     * @param location The location to use.
     */
    public final void addLocation( String location) {
	if( ! location.endsWith( "/")) {
	    location += "/";
	}
	_locations.add( location);
    }

    /**
     * Load an image resource.
     *
     * @return The image resource, if it was found or null otherwise.
     */
    public final Image loadImage( String imageName) {
	Image result = null;

	for( Iterator iter = _locations.iterator(); iter.hasNext(); ) {
	    try {
		result = Toolkit.getDefaultToolkit().getImage( new URL( iter.next() + imageName));
		if( result != null) {
		    return result;
		}
	    } catch( MalformedURLException ignored) {}
	}
	return null;  // Image not found.
    }
}


