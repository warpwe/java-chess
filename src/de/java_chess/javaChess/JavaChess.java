/*
	JavaChess - The main class for the Java chess game.

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

package de.java_chess.javaChess;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import de.java_chess.javaChess.action.AboutAction;
import de.java_chess.javaChess.action.HelpAction;
import de.java_chess.javaChess.action.JavaChessAction;
import de.java_chess.javaChess.action.LoadGameAction;
import de.java_chess.javaChess.action.ResetGameAction;
import de.java_chess.javaChess.action.SaveGameAction;
import de.java_chess.javaChess.action.SaveGameAsAction;
import de.java_chess.javaChess.bitboard.IBitBoard;
import de.java_chess.javaChess.bitboard.BitBoardImpl;
import de.java_chess.javaChess.engine.IChessEngine;
import de.java_chess.javaChess.engine.ChessEngineImpl;
import de.java_chess.javaChess.game.IGame;
import de.java_chess.javaChess.game.GameImpl;
import de.java_chess.javaChess.listener.IEngineStatusListener;
import de.java_chess.javaChess.menu.EditMenu;
import de.java_chess.javaChess.notation.IGameNotation;
import de.java_chess.javaChess.notation.GameNotationImpl;
import de.java_chess.javaChess.renderer.ChessBoardRenderer;
import de.java_chess.javaChess.renderer2d.ChessBoardRenderer2D;
import de.java_chess.javaChess.renderer2d.EnginePanel;
import de.java_chess.javaChess.renderer2d.GameTimerPanel;
import de.java_chess.javaChess.renderer2d.NavigationPanel;
import de.java_chess.javaChess.renderer2d.NotationPanel;
import de.java_chess.javaChess.renderer2d.StatusPanel;
import de.java_chess.javaChess.util.ResourceLoader;

/**
 * The main class for the chess game, representing the GUI
 */
public class JavaChess extends JFrame implements IEngineStatusListener {
	// Static variables

	/**
	 * The game instance.
	 */
	static JavaChess _instance;

/**
 * Versionsnummer
 */
	public final static String VERSIONINFO = "0.1.1 pre-alpha 3";

	private JPanel contentPane;

/**
 * Statuszeilen-Panel
 */
	private StatusPanel jpStatus = new StatusPanel( VERSIONINFO );

/**
 * Eigenes Panel f�r das Schachbrett
 */
	private JPanel jpBrett = new JPanel();

/**
 * Own panel for game notation
 */
		private NotationPanel jpNotation;

/**
 * Own panel for engine output/debug etc.
 */
		private EnginePanel jpEngine;

/**
 * Own Panel for navigation buttons
 */
		private NavigationPanel jpNavigation;

	private Font textFont = new Font( "Serif", Font.PLAIN, 12 );

/**
 * glassPane zum Reagieren auf Mausbewegungen etc.:
 * The glass pane overlays the menu bar and content pane, so it can intercept mouse movements and such.
 */
	private JComponent glasspane = new JComponent()
	{
		public void processKeyEvent(KeyEvent e) {
			e.consume();
		}
	};

	// Instance variables

		/**
		 * The current game.
		 */
		IGame _game;

		/**
		 * The notation of the current game.
		 */
		IGameNotation _gameNotation;

		/**
		 * The chess board
		 */
		IBitBoard _board;

		/**
		 * The rendering component to display the board.
		 */
		ChessBoardRenderer _renderer;

		/**
		 * The chess engine.
		 */
		IChessEngine _engine;

		/**
		 * The menu items
		 */
		JMenuItem _exitItem;

		/**
		 * The game controller.
		 */
		GameController _controller;

		/**
		 * A timer for the game.
		 */
		GameTimerPanel _gameTimer;

/**
 * GridBagLayout for chess board Panel
 */
	GridBagLayout gridBagBrett = new GridBagLayout();

/**
 * GridBagLayout for the navigation panel
 */
	GridBagLayout gridBagNavigation = new GridBagLayout();

/**
 * The Edit menu
 */
	EditMenu editMenu;

	// Constructors

/**
 * Create a new instance of this chess game.
 */
	public JavaChess() {
		super( "JavaChess");  // Set the title of the window.
		this.enableEvents(AWTEvent.WINDOW_EVENT_MASK);

		try {
			jbInit();
			glasspane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			glasspane.addMouseListener( new MouseAdapter() {} );
			setGlassPane(glasspane);
			free();
		 }
		catch(Exception e) {
			e.printStackTrace();
		 }

		setDefaultFrameCoordinates();

		setVisible(true);
	}

/**
 * Die GUI nach Nomenklatur des JBuilder
 *
 * @throws Exception
 */
	private void jbInit() throws Exception {

	    // Add some location for resources to the resource loader.
	    ResourceLoader.getInstance().addLocation("jar:file:javaChess.jar!/de/java_chess/javaChess/renderer2d/images/");

		_game = new GameImpl();
		_gameNotation = new GameNotationImpl();
		_board = new BitBoardImpl();
		_engine = new ChessEngineImpl( _game, _gameNotation, _board, false);
		((ChessEngineImpl)_engine).addEngineStatusListener( this );

		// Create a timer with 40 min for each player.
		_gameTimer = new GameTimerPanel( 40 * 60);

		_controller = new GameController( _game, _gameNotation, _engine, _board, _gameTimer);
		_renderer = new ChessBoardRenderer2D( _controller, _board);
		_controller.setRenderer( _renderer);
		editMenu = new EditMenu();

		this.jpNotation = new NotationPanel( _gameNotation);
		((GameNotationImpl)_gameNotation).setNotationPanel( jpNotation);
		editMenu.setNotationPanel( jpNotation );

		this.jpEngine = new EnginePanel();
		((ChessEngineImpl)_engine).setEnginePanel( jpEngine );
		((ChessEngineImpl)_engine).setStatusPanel( jpStatus );

		this.jpNavigation = new NavigationPanel();

//		setIconImage(Toolkit.getDefaultToolkit().createImage(VisFrame.class.getResource("jclogo.gif")));

		contentPane = (JPanel) this.getContentPane();
		contentPane.setLayout(new GridBagLayout());

		this.setSize(new Dimension(800, 600));
		this.addWindowListener(new java.awt.event.WindowAdapter()
		{
			public void windowClosing(WindowEvent e) {
				this_windowClosing(e);
			}
		});

		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic( KeyEvent.VK_F ); // Shortcut Alt-F

		fileMenu.add( getMenuItem( new ResetGameAction()));
		fileMenu.addSeparator();
		fileMenu.add( getMenuItem( new LoadGameAction()));
		fileMenu.addSeparator();
		fileMenu.add( getMenuItem( new SaveGameAction( _gameNotation)));
		fileMenu.add( getMenuItem( new SaveGameAsAction( _gameNotation)));

		_exitItem = new JMenuItem( "Exit");
		// Shortcut f�r ALT-X zum Beenden:
		this._exitItem.setAccelerator(javax.swing.KeyStroke.
				getKeyStroke(88, java.awt.event.KeyEvent.ALT_MASK, false));
		_exitItem.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e) {
				System.exit( 0 );
			}
		});

		// Create a separated exit item
		fileMenu.addSeparator();

		this.jpBrett.setLayout(gridBagBrett);
//		this.jpBrett.setPreferredSize( new Dimension(520,520) );
//		this.jpBrett.setMaximumSize( new Dimension(520,520) );
//		this.jpBrett.setMinimumSize( new Dimension(520,520) );
		this.jpBrett.setPreferredSize( new Dimension(430,430) );
//		this.jpBrett.setMaximumSize( new Dimension(400,400) );
		this.jpBrett.setMinimumSize( new Dimension(430,430) );

		this.jpStatus.setPreferredSize( new Dimension(780, 20) );
		this.jpStatus.setMinimumSize( new Dimension(780, 20) );
		this.jpStatus.setMaximumSize( new Dimension(780, 20) );

		this.jpEngine.setPreferredSize( new Dimension(780, 150) );
		this.jpEngine.setMinimumSize( new Dimension(780, 150) );
		this.jpEngine.setMaximumSize( new Dimension(780, 150) );

		this.jpNavigation.setLayout( gridBagNavigation );

		fileMenu.add( _exitItem);

		// Add the file menu to the menu bar.
		menuBar.add( fileMenu);

		// Get the Edit menu from the according class and add to the menu bar.
		menuBar.add( editMenu.getMenu() );

		// Get the menu from the chess engine and add it to the menu bar.
		menuBar.add( _engine.getMenu());

		this.engineStatusChanged( (ChessEngineImpl)_engine );

		// Create and add a help menu
		JMenu helpMenu = new JMenu("Help");
		helpMenu.add( new HelpAction());
		helpMenu.addSeparator();
		helpMenu.add( new AboutAction());
		menuBar.add(Box.createHorizontalGlue());  // Move the help menu to the right.
		menuBar.add( helpMenu);

		// Create and set the menu.
		this.setJMenuBar( menuBar );

		contentPane.setForeground( Color.black );

		contentPane.add(jpBrett,      new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
						,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0)); // Brett
		contentPane.add(_gameTimer,   new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0
						,GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0)); // The Clock
		contentPane.add(jpEngine,     new GridBagConstraints(0, 2, 2, 1, 1.0, 1.0
						,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0)); // Engineoutput
		contentPane.add(jpStatus,     new GridBagConstraints(0, 3, 2, 1, 1.0, 0.0
						,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0)); // Statuszeile
		contentPane.add(jpNotation,   new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0
						,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		contentPane.add(jpNavigation, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
						,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0)); // Notation etc.

		jpBrett.add((ChessBoardRenderer2D)_renderer,   new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
						,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		jpBrett.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.white, Color.white, new Color(148, 145, 140), new Color(103, 101, 98)));

		this.pack();
		this.setVisible( true);
	}


		// Methods

		/**
		 * The main method.
		 *
		 * @param args The commandline arguments.
		 */
		public static void main( String [] args) {
				_instance = new JavaChess();
		}

		/**
		 * Create a return a menu item for a given action.
		 *
		 * @param action The action.
		 *
		 * @return The menu item for this action.
		 */
		private JMenuItem getMenuItem( JavaChessAction action) {
	JMenuItem item = new JMenuItem( action.getName());

	item.addActionListener( action);

	return item;
		}

		/**
		 * Get the instance of this chess app.
		 *
		 * @return The instance of this chess app.
		 */
		public static final JavaChess getInstance() {
	return _instance;
		}

		/**
		 * Reset the game.
		 */
		public void reset() {

	// Reset all the game components.

	_game.reset();
	_gameNotation.reset();
	_board.initialPosition();
	_engine.reset();

	// Reset the timer to 40 min for each player.
	_gameTimer.reset( 40 * 60);

	_controller.reset();
	_renderer.reset();
		}

	private void setDefaultFrameCoordinates() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = packFrame();

		setFrameSize(screenSize, frameSize);

		placeFrame(screenSize, frameSize);
	}

	private void placeFrame(Dimension screenSize, Dimension frameSize) {
		setLocation(
			(screenSize.width - frameSize.width) / 2,
			(screenSize.height - frameSize.height) / 2);
	}

	private Dimension packFrame() {
		boolean packFrame = true;

		if (packFrame) {
			pack();
		}
		else {
			validate();
		}

		return this.getSize();
	}

	private void setFrameSize(Dimension screenSize, Dimension frameSize) {
		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}

		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}
	}

		private void busy() {
	glasspane.requestFocus();
	glasspane.setVisible(true);
		}

		private void free() {
	glasspane.setVisible(false);
		}

		/**
		 * If Java-Chess is closed over the 'X' and not via File-Exit, a process
		 * would be still running
		 *
		 * @param e The Window event
		 */
		void this_windowClosing(WindowEvent e) {
	System.exit( 0 );
		}

		public void engineStatusChanged(ChessEngineImpl engine) {
			if ( this.jpStatus != null ) {
				this.jpStatus.setActionText( engine.getStatusDisplayString() );
			}
		}
}
