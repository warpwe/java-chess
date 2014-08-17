/*
	ChessEngineImpl - A class to implement a engine to play chess.

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

package de.java_chess.javaChess.engine;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;

import de.java_chess.javaChess.GameState;
import de.java_chess.javaChess.bitboard.BitBoard;
import de.java_chess.javaChess.board.Board;
import de.java_chess.javaChess.engine.hashtable.PlyHashtable;
import de.java_chess.javaChess.engine.hashtable.PlyHashtableEntryImpl;
import de.java_chess.javaChess.engine.hashtable.PlyHashtableImpl;
import de.java_chess.javaChess.engine.opening_book.OpeningBook;
import de.java_chess.javaChess.engine.opening_book.OpeningBookImpl;
import de.java_chess.javaChess.engine.opening_book.action.LoadOpeningsAction;
import de.java_chess.javaChess.engine.permanent_brain.PermanentBrain;
import de.java_chess.javaChess.engine.permanent_brain.PreComputedPly;
import de.java_chess.javaChess.game.Game;
import de.java_chess.javaChess.listener.EngineStatusListener;
import de.java_chess.javaChess.notation.GameNotation;
import de.java_chess.javaChess.ply.AnalyzedPly;
import de.java_chess.javaChess.ply.AnalyzedPlyImpl;
import de.java_chess.javaChess.ply.Ply;
import de.java_chess.javaChess.ply.TransformationPly;
import de.java_chess.javaChess.renderer2d.EnginePanel;
import de.java_chess.javaChess.renderer2d.StatusPanel;

/**
 * This class implements the functionality to play the actual game of chess
 */
public class ChessEngineImpl implements ChessEngine, Runnable, ActionListener {

	// Instance variables

	/**
	 * The current game.
	 */
	private Game _game;

	/**
	 * The board to operate on.
	 */
	private Board _board;

	/**
	 * A analyzer for the boards.
	 */
	private IBitBoardAnalyzer _analyzer;

	/**
	 * The maximum search depth.
	 */
	private int _maxSearchTime = 15000;

	/**
	 * Flag to indicate if the engine operates on the white pieces.
	 */
	private boolean _white;

	/**
	 * The generator for the plies.
	 */
	private PlyGenerator _plyGenerator;

	/**
	 * Flag to indicate, if the permanent brain function should be used.
	 */
	private boolean _usePermanentBrain = false;

	/**
	 * The permanent brain.
	 */
	private PermanentBrain _permanentBrain;

	/**
	 * The menu item to toggle the permanent brain.
	 */
	private JCheckBoxMenuItem _permanentBrainMenuItem;

	/**
	 * A hashtable for computed plies.
	 */
	private PlyHashtable _hashtable;

	/**
	 * The opening book.
	 */
	private OpeningBook _openingBook;

	/**
	 * Flag to indicate, if we are still in the opening book lines.
	 */
	private boolean _inOpeningBook;

	/**
	 * The currently used search depth.
	 */
	private int _searchDepth;

	/**
	 * A counter for the analyzed boards.
	 */
	private long _analyzedBoards;

	/**
	 * A thread to search for the best move.
	 */
	private volatile Thread _searchThread;

	/**
	 * Flag to stop the search.
	 */
	private boolean _stopSearch;

	/**
	 * The best computed ply so far.
	 */
	private AnalyzedPly _bestPly = null;

	/**
	 * The menu items for the various fix search times.
	 */
	private JMenuItem[] _fixSearchTimeMenuItem;

	/**
	 * The menu items for the various average search times.
	 */
	private JMenuItem[] _avSearchTimeMenuItem;

	/**
	 * The predefined search times (in seconds).
	 */
	private int[] _searchTime = { 3, 5, 10, 15, 30, 45, 60 };

	/**
	 * The menu items for the various hashtable sizes.
	 */
	private JMenuItem[] _hashtableSizeMenuItem;

	/**
	 * The predefined hashtable sizes.
	 */
	private int[] _hashtableSizes = { 5000, 10000, 20000, 50000, 100000 };

	/**
	 * The menu items for the various search times.
	 */
	private EnginePanel _enginePanel = null;

	/**
	 * The menu items for the various search times.
	 */
	private StatusPanel _statusPanel = null;

	/**
	 * The last ply from the user.
	 */
	Ply _lastUserPly = null;

	/**
	 * Flag that indicates if the chosen time control is fixed or average time
	 */
	private boolean bFixedTime = false;

	/**
	 * The ButtonGroup for the time controls
	 */
	ButtonGroup buttonGroupSearchTime;

	/**
	 * The ButtonGroup for the hash sizes
	 */
	ButtonGroup buttonGroupHashSize;

	/**
	 * The list of listeners
	 */
	private List listeners;

	// Constructors

	/**
	 * Create a new engine instance with a given board.
	 * 
	 * @param game
	 *            The current game.
	 * @param notation
	 *            The current notation.
	 * @param board
	 *            The new board.
	 * @param white
	 *            Flag, to indicate if the engine operates on the white pieces.
	 */
	public ChessEngineImpl(Game game, GameNotation notation, Board board,
			boolean white) {
		listeners = new ArrayList();
		setGame(game);
		setBoard(board);
		setWhite(white);
		_hashtable = new PlyHashtableImpl(100000);
		_plyGenerator = new PlyGenerator(getGame(), _hashtable);
		_analyzer = new BitBoardAnalyzerImpl(getGame(), _plyGenerator);
		_plyGenerator.setAnalyzer(_analyzer);
		setPermanentBrain(new PermanentBrain(this));
		startPermanentBrain();
		setOpeningBook(new OpeningBookImpl(notation));
		setInOpeningBook(true);
	}

	// Methods

	/**
	 * Reset the engine for a new game.
	 */
	public void reset() {
		setInOpeningBook(true); // Default is, that we are in the opening book.
		getHashtable().reset(); // Reset the hash tables.

		// Remove the engine status messages.
		if (_enginePanel != null) {
			_enginePanel.setText("");
		}

		// After a reset, the user is about to move.
		if (this._statusPanel != null) {
			this._statusPanel.setStatusText("Your turn...");
		}

		// Start the permanent brain, if the user wants it to be used.
		startPermanentBrain();

		// Reset the last user ply.
		_lastUserPly = null;
	}

	/**
	 * Get the current game.
	 * 
	 * @return The current game.
	 */
	public final Game getGame() {
		return _game;
	}

	/**
	 * Set the current game.
	 * 
	 * @param The
	 *            current game.
	 */
	public final void setGame(Game game) {
		_game = game;
	}

	/**
	 * Get the current board.
	 * 
	 * @return The current board.
	 */
	public Board getBoard() {
		return _board;
	}

	/**
	 * Set the board.
	 * 
	 * @param board
	 *            The new board.
	 */
	public void setBoard(Board board) {
		_board = board;
	}

	/**
	 * Get the current hashtable for this ply generator.
	 * 
	 * @return The current hashtable for this ply generator.
	 */
	public final PlyHashtable getHashtable() {
		return _hashtable;
	}

	/**
	 * Set a new hashtable for this ply generator.
	 * 
	 * @param hashtable
	 *            The new hashtable for this ply generator.
	 */
	public final void setHashtable(PlyHashtable hashtable) {
		_hashtable = hashtable;
	}

	/**
	 * Get the permanent brain.
	 * 
	 * @return The permanent brain.
	 */
	public final PermanentBrain getPermanentBrain() {
		return _permanentBrain;
	}

	/**
	 * Set a new permanent brain.
	 * 
	 * @param permanentBrain
	 *            The new permanent brain.
	 */
	private final void setPermanentBrain(PermanentBrain permanentBrain) {
		_permanentBrain = permanentBrain;
	}

	/**
	 * Check, if the permanent brain should be used.
	 * 
	 * @return A flag, that indicates, if the permanent brain should be used.
	 */
	private final boolean usePermanentBrain() {
		return _usePermanentBrain;
	}

	/**
	 * (De-)activate the permanent brain function.
	 * 
	 * @param active
	 *            Flag to indicate, if the permanent brain function should be
	 *            used.
	 */
	private final void activatePermanentBrain(boolean active) {
		_usePermanentBrain = active;

		// Since the permanent brain is eventually active already, stop it now.
		if (!usePermanentBrain()) {
			stopPermanentBrain();
		}

		// If the permanent brain is activated, it will be use for the next(!)
		// move.
	}

	/**
	 * Get the maximum search time.
	 * 
	 * @return The maximum search time.
	 */
	public final int getMaximumSearchTime() {
		return _maxSearchTime;
	}

	/**
	 * Set the maximum search time.
	 * 
	 * @param depth
	 *            The new search time.
	 */
	public final void setMaximumSearchTime(int time) {
		_maxSearchTime = time;
	}

	/**
	 * Get the color of this engine.
	 * 
	 * @param white
	 *            true, if the engine operates with the white pieces.
	 */
	public boolean isWhite() {
		return _white;
	}

	/**
	 * Set the color of the engine.
	 * 
	 * @param white
	 *            flag to indicate if the engine operates on the white pieces.
	 */
	public void setWhite(boolean white) {
		_white = white;
	}

	/**
	 * Get the current opening book.
	 * 
	 * @return The current opening book.
	 */
	private final OpeningBook getOpeningBook() {
		return _openingBook;
	}

	/**
	 * Set a new opening book.
	 * 
	 * @param book
	 *            The new opening book.
	 */
	private final void setOpeningBook(OpeningBook book) {
		_openingBook = book;
	}

	/**
	 * Get the flag to indicate, if we are still in the opening book.
	 * 
	 * @return true, if we are still in the opening book. False otherwise.
	 */
	private final boolean inOpeningBook() {
		return _inOpeningBook;
	}

	/**
	 * Set the flag to indicate, if we are still in the opening book.
	 * 
	 * @param inBook
	 *            true, if we are still in the opening book. False otherwise.
	 */
	private final void setInOpeningBook(boolean inBook) {
		_inOpeningBook = inBook;
	}

	/**
	 * Start a new thread to search for a ply.
	 */
	public void start() {
		if (_searchThread == null) {
			setSearchStop(false);
			_searchThread = new Thread(this);
			_searchThread.start();
		}
	}

	/**
	 * Compute the best ply for the current position.
	 * 
	 * @return The best known ply for the current position.
	 */
	public Ply computeBestPly() {

		_bestPly = null; // Remove ply from last computation.
		long startTime = System.currentTimeMillis();

		if (inOpeningBook()) {
			_bestPly = getOpeningBook().getOpeningBookPly();

			if (_bestPly == null) { // If there's no ply in the opening book.
				setInOpeningBook(false);
			}
		}

		if (_bestPly == null) { // If we don't have a ply yet
			start();
			try {
				Thread.sleep(getMaximumSearchTime());
				setSearchStop(true);
				// if( this.bFixedTime == false) {
				_searchThread.join(); // Wait for the search thread to end the
										// search at this search depth.
				// }

				_searchThread = null; // Remove the thread, so it can be
										// recreated for the next move.
			} catch (InterruptedException ignored) {
			}
		}

		long usedTime = System.currentTimeMillis() - startTime;
		if (_bestPly != null) {
			if (this._enginePanel != null) {
				StringBuffer sOut = new StringBuffer();
				sOut.append("Best ply: ");
				sOut.append(_bestPly.getPly().toString());
				sOut.append(" with score ");
				sOut.append(_bestPly.getScore());
				sOut.append(" and search depth ");
				sOut.append(getSearchDepth());
				this._enginePanel.modifyText(sOut.toString());
				sOut = new StringBuffer();
				sOut.append("Analyzed boards: ");
				sOut.append(getAnalyzedBoards());
				sOut.append(" in ");
				sOut.append(usedTime);
				sOut.append(" ms");
				this._enginePanel.modifyText(sOut.toString());
			}
			if (this._statusPanel != null)
				this._statusPanel.setStatusText("Your turn...");

			return _bestPly.getPly();
		}
		return null;
	}

	/**
	 * The main method of the search thread.
	 */
	public void run() {
		// setAnalyzedBoards( 0L); // Is done in the permanent brain now.
		setSearchDepth(0);

		// Try to get a move from the permanent brain.
		PreComputedPly permanentBrainPly = usePermanentBrain() ? getPermanentBrain()
				.getPlyForUserPly(_lastUserPly) : null;

		// If we actually have a precomputed ply, adjust the search parameters.
		if (permanentBrainPly != null) {
			_bestPly = permanentBrainPly.getPly();
			setSearchDepth(permanentBrainPly.getSearchDepth()); // depth is
																// increased
																// before next
																// search!

			if (this._enginePanel != null) {
				StringBuffer sOutput = new StringBuffer();
				sOutput.append("Best ply for search depth ");
				sOutput.append(getSearchDepth());
				sOutput.append(" is ");
				sOutput.append(_bestPly.getPly().toString());
				sOutput.append(" with score ");
				sOutput.append(_bestPly.getScore());
				this._enginePanel.modifyText(sOutput.toString());
				// @Testdisplay
				// System.out.println(getSearchDepth() + "-------------" +
				// _bestPly.getPly().toString());

			}
		}

		// The following search is rather inefficent at the moment, since we
		// should try to get a principal variant
		// from a search, so we can presort the plies for the next search.
		do {
			increaseSearchDepth();

			AnalyzedPly searchDepthResult = null;
			try {
				searchDepthResult = startMinimaxAlphaBeta(isWhite());
			} catch (InterruptedException ignored) { // The search was just
														// interrupted here, so
														// we don't have to
														// throw this
														// exception...
				decreaseSearchDepth(); // But the search depth is 1 too high.
			}

			if (searchDepthResult != null) { // The exception might not be the
												// only case, where a null is
												// returned, so keep
				// this additional test.

				if (this._statusPanel != null)
					this._statusPanel.setStatusText("Thinking...");

				_bestPly = searchDepthResult;

				if (this._enginePanel != null) {
					StringBuffer sOutput = new StringBuffer();
					sOutput.append("Best ply for search depth ");
					sOutput.append(getSearchDepth());
					sOutput.append(" is ");
					sOutput.append(_bestPly.getPly().toString());
					sOutput.append(" with score ");
					sOutput.append(_bestPly.getScore());
					this._enginePanel.modifyText(sOutput.toString());
					// System.out.println(getSearchDepth() + "-------------" +
					// _bestPly.getPly().toString() + " " +
					// _bestPly.getScore());
				}
			}
			// If search depth 1 was completed and no valid ply was found,
			// it seems that the computer is checkmate and the search can be
			// aborted.
		} while (!isSearchStop() && (_bestPly != null));
	}

	/**
	 * Start a complete Minimax-Alpha-Beta search. This is the search level 1,
	 * where we have to store the analyzed ply, so it gets a special method.
	 * 
	 * @param white
	 *            Flag to indicate, if white is about to move.
	 * 
	 * @throws InterruptedException
	 *             if the search was interrupted because of a timeout.
	 */
	public final AnalyzedPly startMinimaxAlphaBeta(boolean white)
			throws InterruptedException {
		short curAlpha = AnalyzedPly.MIN_SCORE;
		short curBeta = AnalyzedPly.MAX_SCORE;
		int bestPlyIndex = -1;

		Ply[] plies = _plyGenerator.getPliesForColor((BitBoard) getBoard(),
				white);
		if (white) {
			for (int i = 0; i < plies.length; i++) {
				if (isSearchStop() && (getSearchDepth() > 1)) { // If the search
																// time is over
																// and at least
																// depth 1 was
																// completed
					throw new InterruptedException(
							"Search interrupted at depth " + getSearchDepth()); // abort
																				// the
																				// search.
				}
				getGame().doPly(plies[i]);
				short val;
				try {
					val = minimaxAlphaBeta(plies[i], getBoard()
							.getBoardAfterPly(plies[i]), false, 1, curAlpha,
							curBeta);
					// @Testdisplay
					// System.out.println(i + " " + plies[i] + " " + val);
				} catch (InterruptedException ie) {
					getGame().undoLastPly(); // Undo the last move
					throw ie; // and pass the exception.
				}
				if (val > curAlpha) {
					curAlpha = val;
					bestPlyIndex = i;
				}
				if (curAlpha >= curBeta) {
					getGame().undoLastPly(); // Take the last ply back, before
												// the loop is aborted.
					break;
				}
				getGame().undoLastPly();
			}

			if (bestPlyIndex != -1) {

				// Since this is the best ply so far, we store it in the
				// hashtable. This makes sense,
				// since the minimax algorithm is started several times, before
				// a move is selected.
				// So this move is not necessarily applied immediately!
				getHashtable().pushEntry(
						new PlyHashtableEntryImpl(getBoard(),
								plies[bestPlyIndex], getSearchDepth()));

				return new AnalyzedPlyImpl(plies[bestPlyIndex], curAlpha);
			} else {
				return null;
			}
		} else {
			for (int i = 0; i < plies.length; i++) {
				if (isSearchStop() && (getSearchDepth() > 1)) { // If the search
																// time is over
																// and at least
																// depth 1 was
																// completed
					throw new InterruptedException(
							"Search interrupted at depth " + getSearchDepth()); // abort
																				// the
																				// search.
				}
				getGame().doPly(plies[i]);
				short val;
				try {
					val = minimaxAlphaBeta(plies[i], getBoard()
							.getBoardAfterPly(plies[i]), true, 1, curAlpha,
							curBeta);
					// @Testdisplay
					// System.out.println(i + " " + plies[i] + " " + val + " " +
					// curAlpha + " " + curBeta);
				} catch (InterruptedException ie) {
					getGame().undoLastPly(); // Undo the last move
					throw ie; // and pass the exception.
				}
				if (val < curBeta) {
					curBeta = val;
					bestPlyIndex = i;
				}
				if (curBeta <= curAlpha) {
					getGame().undoLastPly(); // Take the last ply back, before
												// the loop is aborted.
					break;
				}
				getGame().undoLastPly();
			}

			if (bestPlyIndex != -1) {

				// Since this is the best ply so far, we store it in the
				// hashtable. This makes sense,
				// since the minimax algorithm is started several times, before
				// a move is selected.
				// So this move is not necessarily applied immediately!
				getHashtable().pushEntry(
						new PlyHashtableEntryImpl(getBoard(),
								plies[bestPlyIndex], getSearchDepth()));

				return new AnalyzedPlyImpl(plies[bestPlyIndex], curBeta);
			} else {
				return null;
			}
		}
	}

	/**
	 * Perform a alpha-beta minimax search on the board.
	 * 
	 * @param lastPly
	 *            The ply, that created this board.
	 * @param board
	 *            The board to analyze.
	 * @param white
	 *            true, if white has the next move.
	 * @param byte searchLevel The level to search for.
	 * @param alpha
	 *            The current maximum.
	 * @param beta
	 *            The current minimum.
	 * 
	 * @throws InterruptedException
	 *             if the search was interrupted because of a timeout.
	 */
	private final short minimaxAlphaBeta(Ply lastPly, Board board,
			boolean white, int searchLevel, short alpha, short beta)
			throws InterruptedException {
		if ((searchLevel >= getSearchDepth())
				&& (!lastPly.isCapture()
						|| !(lastPly instanceof TransformationPly) || !_analyzer
							.isInCheck((BitBoard) board, !white))) {
			increaseAnalyzedBoards();
			return analyzeBoard(board);
		} else {
			short curAlpha = alpha;
			short curBeta = beta;
			int bestPlyIndex = -1;

			Ply[] plies = _plyGenerator.getPliesForColor((BitBoard) board,
					white);
			if (white) {
				for (int i = 0; i < plies.length; i++) {
					// W.E. 20140729
					// if( isSearchStop() && ( getSearchDepth() > 1)) { // If
					// the search time is over and at least depth 1 was
					// completed
					if (isSearchStop() && (getSearchDepth() > 3)) { // If the
																	// search
																	// time is
																	// over and
																	// at least
																	// depth 1
																	// was
																	// completed
						throw new InterruptedException(
								"Search interrupted at depth "
										+ getSearchDepth()); // abort the
																// search.
					}
					getGame().doPly(plies[i]);
					short val;
					try {
						val = minimaxAlphaBeta(plies[i],
								board.getBoardAfterPly(plies[i]), false,
								searchLevel + 1, curAlpha, curBeta);
					} catch (InterruptedException ie) {
						getGame().undoLastPly(); // Undo the last move
						throw ie; // and pass the exception.
					}
					if (val > curAlpha) {
						curAlpha = val;
						bestPlyIndex = i; // Store the index of this ply, so we
											// can access it later.
					}
					if (curAlpha >= curBeta) {
						getGame().undoLastPly(); // Take the last ply back,
													// before the loop is
													// aborted.
						break;
					}
					getGame().undoLastPly();
				}

				if (bestPlyIndex != -1) {
					// Since this is the best ply for this search level, we
					// store it in the hashtable
					getHashtable().pushEntry(
							new PlyHashtableEntryImpl(board,
									plies[bestPlyIndex], getSearchDepth()
											- searchLevel));
				} else {
					if (plies.length == 0) { // There are no legal moves
												// available?
						if (_analyzer.isInCheck((BitBoard) board, white)) { // Is
																			// this
																			// a
																			// checkmate?
							return IBitBoardAnalyzer.BLACK_HAS_WON;
						} else { // Looks like a draw?
							return IBitBoardAnalyzer.DRAW;
						}
					}
				}

				return curAlpha;
			} else {
				for (int i = 0; i < plies.length; i++) {
					if (isSearchStop() && (getSearchDepth() > 1)) { // If the
																	// search
																	// time is
																	// over and
																	// at least
																	// depth 1
																	// was
																	// completed
						throw new InterruptedException(
								"Search interrupted at depth "
										+ getSearchDepth()); // abort the
																// search.
					}
					getGame().doPly(plies[i]);
					short val;
					try {
						val = minimaxAlphaBeta(plies[i],
								board.getBoardAfterPly(plies[i]), true,
								searchLevel + 1, curAlpha, curBeta);
					} catch (InterruptedException ie) {
						getGame().undoLastPly(); // Undo the last move
						throw ie; // and pass the exception.
					}
					if (val < curBeta) {
						curBeta = val;
						bestPlyIndex = i; // Store the index of this ply, so we
											// can access it later.
					}
					if (curBeta <= curAlpha) {
						getGame().undoLastPly(); // Take the last ply back,
													// before the loop is
													// aborted.
						break;
					}
					getGame().undoLastPly();
				}

				if (bestPlyIndex != -1) {
					// Since this is the best ply for this search level, we
					// store it in the hashtable
					getHashtable().pushEntry(
							new PlyHashtableEntryImpl(board,
									plies[bestPlyIndex], getSearchDepth()
											- searchLevel));
				} else {
					if (plies.length == 0) { // There are no legal moves
												// available?
						if (_analyzer.isInCheck((BitBoard) board, white)) { // Is
																			// this
																			// a
																			// checkmate?
							return IBitBoardAnalyzer.WHITE_HAS_WON;
						} else { // Looks like a draw?
							return IBitBoardAnalyzer.DRAW;
						}
					}
				}

				return curBeta;
			}
		}
	}

	/**
	 * Compute a score for a game position.
	 * 
	 * @return A score for the current game position.
	 */
	public final short analyzeBoard(Board board) {
		return _analyzer.analyze((BitBoard) board, isWhite());
	}

	/**
	 * Get all the potential plies for the human player.
	 * 
	 * @return All the potential plies for the human player.
	 */
	public final Ply[] getUserPlies() {
		return _plyGenerator
				.getPliesForColor((BitBoard) getBoard(), !isWhite());
	}

	/**
	 * Check if a ply made by the user is valid.
	 * 
	 * @param ply
	 *            The user ply.
	 * 
	 * @return true, if the ply is valid. false otherwise.
	 */
	public final boolean validateUserPly(Ply ply) {

		// Get the user plies from the permanent brain, where they
		// were hopefully already computed (if the PB is actually active).
		Ply[] plies = getPermanentBrain().getUserPlies();
		// If the permanent brain is not activated at the moment, remove the
		// computed plies immediately, so they are recomputed for the next move!
		if (!usePermanentBrain()) {
			getPermanentBrain().resetUserPlies();
		}

		for (int p = 0; p < plies.length; p++) { // For each ply
			if (plies[p].equals(ply)) { // if the user ply equals this computed

				// Perform this ply in the opening book
				getOpeningBook().doUserPly(ply);

				// Store the last user ply in a instance variable.
				_lastUserPly = ply;

				return true; // ply, it seems to be valid.
			}
		}

		// The computer could not compute the ply, the user has made,
		// so we assume, that it is not valid.
		System.out.println("Invalid move " + ply.toString());
		System.out.println("Piecetype on source square "
				+ (getBoard().getPiece(ply.getSource()) == null ? "null" : ""
						+ getBoard().getPiece(ply.getSource()).getType()));
		System.out.println("Piecetype on destination square "
				+ (getBoard().getPiece(ply.getDestination()) == null ? "null"
						: ""
								+ getBoard().getPiece(ply.getDestination())
										.getType()));
		System.out.println("Valid moves are:");
		for (int p = 0; p < plies.length; p++) { // For each ply
			System.out.print(plies[p].toString() + " ");
		}
		System.out.println();
		return false;
	}

	/**
	 * Start the computations of the permanent brain.
	 */
	public final void startPermanentBrain() {
		// If the permanent brain is active.
		if (usePermanentBrain()) {
			getPermanentBrain().startComputation();
		} else {
			// Since the permanent brain cannot reset the counter,
			// I do it here.
			setAnalyzedBoards(0L);
		}
	}

	/**
	 * Stop the computations of the permanent brain.
	 */
	public final void stopPermanentBrain() {
		getPermanentBrain().stopComputation();
	}

	/**
	 * Set the the stop flag for the search.
	 * 
	 * @param stopFlag
	 *            The flag to stop the search engine.
	 */
	public final void setSearchStop(boolean stopFlag) {
		_stopSearch = stopFlag;
	}

	/**
	 * Check, if a search should be stopped.
	 * 
	 * @return true, if the search should be stopped.
	 */
	public final boolean isSearchStop() {
		return _stopSearch;
	}

	/**
	 * Get the number of analyzed boards.
	 * 
	 * @return The number of analyzed boards.
	 */
	public final long getAnalyzedBoards() {
		return _analyzedBoards;
	}

	/**
	 * Set the number of analyzed boards.
	 * 
	 * @param analyzedBoards
	 *            The new number of analyzed boards.
	 */
	public final void setAnalyzedBoards(long analyzedBoards) {
		_analyzedBoards = analyzedBoards;
	}

	/**
	 * Increase the number of analyzed boards by 1.
	 */
	public final void increaseAnalyzedBoards() {
		_analyzedBoards++;
	}

	/**
	 * Get the current search depth.
	 * 
	 * @return The current search depth.
	 */
	public final int getSearchDepth() {
		return _searchDepth;
	}

	/**
	 * Set a new search depth.
	 * 
	 * @param searchDepth
	 *            The new search depth.
	 */
	public final void setSearchDepth(int searchDepth) {
		_searchDepth = searchDepth;
	}

	/**
	 * Increase the search depth by 1.
	 */
	public final void increaseSearchDepth() {
		_searchDepth++;
	}

	/**
	 * Decrease the search depth by 1.
	 */
	public final void decreaseSearchDepth() {
		_searchDepth--;
	}

	/**
	 * Return a menu from the chess engine, where the user can change the
	 * settings.
	 * 
	 * @return A menu for the engine settings.
	 */
	public final JMenu getMenu() {

		// Create a new menu.
		JMenu engineMenu = new JMenu("Engine");

		// Add a toggle item for to the permanent brain.
		engineMenu.add(_permanentBrainMenuItem = new JCheckBoxMenuItem(
				"Use permanent brain", usePermanentBrain()));
		_permanentBrainMenuItem.addActionListener(this);

		// Add a menu for the maximum search time
		JMenu searchTimeMenu = new JMenu("Search time");

		// Add a sub-menu for fixed time.
		JMenu searchTimeSubMenuFix = new JMenu("Fixed time");

		// Add a sub-menu for average time.
		JMenu searchTimeSubMenuAv = new JMenu("Average time");

		// Add various options for the fixed search time
		// (maybe a user defined search time should be added, too).
		buttonGroupSearchTime = new ButtonGroup();
		_fixSearchTimeMenuItem = new JRadioButtonMenuItem[_searchTime.length];
		for (int st = 0; st < _searchTime.length; st++) {
			_fixSearchTimeMenuItem[st] = new JRadioButtonMenuItem(""
					+ _searchTime[st] + " seconds");
			_fixSearchTimeMenuItem[st].addActionListener(this);
			if (_searchTime[st] == 15) { // set initially 5 sec max search time:
				_fixSearchTimeMenuItem[st].setSelected(true);
				this.setMaximumSearchTime(15000);
			}

			buttonGroupSearchTime.add(_fixSearchTimeMenuItem[st]);

			// Add the current search time menu item to it's menu.
			searchTimeSubMenuFix.add(_fixSearchTimeMenuItem[st]);
		}

		// Add various options for the average search time
		_avSearchTimeMenuItem = new JRadioButtonMenuItem[_searchTime.length];
		for (int st = 0; st < _searchTime.length; st++) {
			_avSearchTimeMenuItem[st] = new JRadioButtonMenuItem(""
					+ _searchTime[st] + " seconds");
			_avSearchTimeMenuItem[st].addActionListener(this);

			// Add the current search time menu item to it's menu.
			searchTimeSubMenuAv.add(_avSearchTimeMenuItem[st]);
			buttonGroupSearchTime.add(_avSearchTimeMenuItem[st]);
		}

		searchTimeMenu.add(searchTimeSubMenuFix);
		searchTimeMenu.add(searchTimeSubMenuAv);

		// Add the search time menu to the main engine menu.
		engineMenu.add(searchTimeMenu);

		// Add a menu for the hashtable size.
		JMenu hashtableSizeMenu = new JMenu("Hashtable size");

		// Add various options for the hashtable size.
		this.buttonGroupHashSize = new ButtonGroup();
		_hashtableSizeMenuItem = new JRadioButtonMenuItem[_hashtableSizes.length];
		for (int hts = 0; hts < _hashtableSizes.length; hts++) {
			_hashtableSizeMenuItem[hts] = new JRadioButtonMenuItem(""
					+ _hashtableSizes[hts] + " entries");
			_hashtableSizeMenuItem[hts].addActionListener(this);
			if (_hashtableSizes[hts] == 100000) {
				_hashtableSizeMenuItem[hts].setSelected(true);
				getHashtable().setMaximumSize(_hashtableSizes[hts]);
			}

			// Add the current search time menu item to it's menu.
			hashtableSizeMenu.add(_hashtableSizeMenuItem[hts]);
			this.buttonGroupHashSize.add(_hashtableSizeMenuItem[hts]);
		}

		// Add the search time menu to the main engine menu.
		engineMenu.add(hashtableSizeMenu);

		// Add a menu item to read openings from PGN files.
		engineMenu.addSeparator();
		engineMenu.add(new LoadOpeningsAction(getOpeningBook()));

		// Return the engine menu.
		return engineMenu;
	}

	/**
	 * Perform a action (could be a menu related action).
	 * 
	 * @param actionEvent
	 *            The event.
	 */
	public final void actionPerformed(ActionEvent actionEvent) {

		// Check, if the user (de-)activated the permanent brain.
		if (actionEvent.getSource().equals(_permanentBrainMenuItem)) {
			activatePermanentBrain(_permanentBrainMenuItem.isSelected());
			this.notifyListeners();
			return;
		}

		// Check if the user has requested a new search time
		for (int st = 0; st < _searchTime.length; st++) {
			if (actionEvent.getSource().equals(_avSearchTimeMenuItem[st])) {
				setMaximumSearchTime(_searchTime[st] * 1000);
				this.bFixedTime = false;
			}

			if (actionEvent.getSource().equals(_fixSearchTimeMenuItem[st])) {
				setMaximumSearchTime(_searchTime[st] * 1000);
				this.bFixedTime = true;
			}
		}

		// Check, if the user has requested a different hashtable size.
		for (int hts = 0; hts < _hashtableSizes.length; hts++) {
			if (actionEvent.getSource().equals(_hashtableSizeMenuItem[hts])) {
				getHashtable().setMaximumSize(_hashtableSizes[hts]);
			}
		}

		this.notifyListeners();
	}

	/**
	 * Sets the EnginePanel to be able to output in the panel and not only with
	 * System.out.println(...)
	 * 
	 * @param panel
	 *            The EnginePanel to set
	 */
	public void setEnginePanel(EnginePanel panel) {
		this._enginePanel = panel;
	}

	/**
	 * Get the current game state.
	 * 
	 * @param white
	 *            True, if the state of the white player is requested.
	 * 
	 * @return The current game state.
	 */
	public final byte getCurrentGameState(boolean white) {

		// Test if the given player is in check.
		boolean inCheck = _analyzer.isInCheck((BitBoard) getBoard(), white);
		// @Testdisplay
		// System.out.println(white + " " + inCheck);
		// Test if the player has valid plies available.
		// The following computation of the available plies is not really
		// efficient,
		// since they are done anyway (either to compute the next computer ply
		// or to
		// check if a user ply is valid). So maybe the plygenerator or the
		// engine should
		// cache the computed plies.
		boolean validPliesAvailable = (_plyGenerator.getPliesForColor(
				(BitBoard) getBoard(), white).length > 0);

		if (inCheck) {
			return validPliesAvailable ? GameState.CHECK : GameState.CHECKMATE;
		} else {
			return validPliesAvailable ? GameState.ONGOING : GameState.DRAW;
		}
	}

	public void setStatusPanel(StatusPanel myPanel) {
		if (this._statusPanel != myPanel)
			this._statusPanel = myPanel;
	}

	/**
	 * Returns whether the search time is fixed or not.
	 * 
	 * @return Is the search time fixed?
	 */
	public boolean isFixedTime() {
		return (this.bFixedTime);
	}

	/**
	 * Returns the hash size
	 * 
	 * @return The hashsize
	 */
	public int getHashSize() {
		return (this.getHashtable().getMaximumSize());
	}

	/**
	 * Returns the status string to display in status bar
	 */
	public String getStatusDisplayString() {
		String retString = "HashSize: " + this.getHashSize() + " entries; "
				+ "Search time: " + this.getMaximumSearchTime() / 1000 + "sec";
		if (this.isFixedTime()) {
			retString += " fix";
		} else {
			retString += " average";
		}

		return (retString);

	}

	/**
	 * Registers an object for notification.
	 * 
	 * @param listener
	 *            The object to be registered.
	 */
	public void addEngineStatusListener(EngineStatusListener listener) {
		listeners.add(listener);
	}

	/**
	 * Notifies all registered listeners that the engine has taken a change.
	 */
	public void notifyListeners() {
		Iterator iterator = listeners.iterator();
		while (iterator.hasNext()) {
			EngineStatusListener listener = (EngineStatusListener) iterator
					.next();
			listener.engineStatusChanged(this);
		}
	}
}
