/*
  ChessSet - A class to provide a image of a set of chess pieces.

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

package de.java_chess.javaChess.renderer2d;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.net.URL;


/**
 * This class loads and provides a images with all the chess pieces.
 */
public class ChessSet extends BufferedImage {

    // Instance variables


    // Constructors

    /**
     * Create a new chess set component.
     *
     * @param c The parent component.
     */
    public ChessSet(Component c) {
	super( 240, 80, BufferedImage.TYPE_INT_ARGB);
//	super( 240, 80, BufferedImage.TYPE_INT_RGB);

	Image im = null;
	// try {
  	     URL url = getClass().getResource("images/ChessPieces02.gif");
	     im = Toolkit.getDefaultToolkit().getImage( url );
//	     im = Toolkit.getDefaultToolkit().getImage( new URL( "jar:file:javaChess.jar!/de/java_chess/javaChess/renderer2d/images/ChessPieces02.gif"));
//	    im = ResourceLoader.getInstance().loadImage( "file:///C:/Dokumente und Einstellungen/Wilfried/Eigene Dateien/Java-Chess-0.1.0pre-alpha3/src/de/java_chess/javaChess/renderer2d/images/ChessPieces02.gif");
	    // } catch( MalformedURLException ignored) {}
	MediaTracker mediaTracker = new MediaTracker(c);
	mediaTracker.addImage(im, 0);
	try{
	    mediaTracker.waitForID(0);
	} catch(InterruptedException ie){}
	Graphics g = getGraphics();
	g.drawImage(im, 0, 0, c);
    }


    // Methods
}
