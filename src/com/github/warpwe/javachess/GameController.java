package com.github.warpwe.javachess;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.github.warpwe.javachess.board.Board;
import com.github.warpwe.javachess.dialogs.TransformationDialog;
import com.github.warpwe.javachess.engine.IChessEngine;
import com.github.warpwe.javachess.game.IGame;
import com.github.warpwe.javachess.notation.IGameNotation;
import com.github.warpwe.javachess.notation.IPlyNotation;
import com.github.warpwe.javachess.notation.PlyNotationImpl;
import com.github.warpwe.javachess.piece.IPiece;
import com.github.warpwe.javachess.ply.CastlingPlyImpl;
import com.github.warpwe.javachess.ply.EnPassantPlyImpl;
import com.github.warpwe.javachess.ply.IPly;
import com.github.warpwe.javachess.ply.PlyImpl;
import com.github.warpwe.javachess.ply.TransformationPlyImpl;
import com.github.warpwe.javachess.position.Position;
import com.github.warpwe.javachess.position.PositionImpl;
import com.github.warpwe.javachess.renderer.ChessBoardRenderer;
import com.github.warpwe.javachess.timer.GameTimer;

/**
 * GameController - A class to control the game of chess. Copyright (C) 2003 The Java-Chess team
 * <info@java-chess.de> This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free Software Foundation;
 * either version 2 of the License, or (at your option) any later version. This program is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details. You should have received a copy of the GNU General Public
 * License along with this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 */
public class GameController implements ActionListener {

  // Instance variables

  /**
   * The current game.
   */
  private IGame currentGame;

  /**
   * The game notation.
   */
  private IGameNotation gameNotation;

  /**
   * A flag to indicate, if white has the next move.
   */
  private boolean whiteHasNextMove = true;

  /**
   * A flag to indicate, if the computer play with white pieces.
   */
  private boolean isComputerWhite = false;

  /**
   * The chess engine.
   */
  private IChessEngine engine;

  /**
   * The current board.
   */
  private Board board;

  /**
   * The renderer.
   */
  ChessBoardRenderer renderer;

  /**
   * The timer for the game.
   */
  GameTimer gameTimer;

  /**
   * The current game state.
   */
  byte gameState;

  /**
   * The log4j instance.
   */
  static final Logger logger = Logger.getLogger("logfile");

  // Constructors

  /**
   * Create a new controller instance.
   *
   * @param game
   *          The current game.
   * @param gameNotation
   *          The notation of the game.
   * @param engine
   *          The current engine.
   * @param board
   *          The current board.
   * @param timer
   *          The game timer.
   */
  public GameController(IGame game, IGameNotation gameNotation, IChessEngine engine, Board board,
      GameTimer timer) {
    setGame(game);
    setGameNotation(gameNotation);
    setEngine(engine);
    setBoard(board);
    setGameTimer(timer);
  }

  // Methods

  /**
   * Get the current game.
   *
   * @return The current game.
   */
  public final IGame getGame() {
    return currentGame;
  }

  /**
   * Set the current game.
   *
   * @param The
   *          current game.
   */
  public final void setGame(IGame game) {
    currentGame = game;
  }

  /**
   * Get the current game notation.
   *
   * @return The current game notation.
   */
  public final IGameNotation getGameNotation() {
    return gameNotation;
  }

  /**
   * Set the current game notation.
   *
   * @param gameNotation
   *          The new game notation.
   */
  public final void setGameNotation(IGameNotation gameNotation) {
    this.gameNotation = gameNotation;
  }

  /**
   * Get the current chess engine.
   *
   * @return The current chess engine.
   */
  final IChessEngine getEngine() {
    return this.engine;
  }

  /**
   * Set a new chess engine.
   *
   * @param engine
   *          The new engine.
   */
  final void setEngine(IChessEngine engine) {
    this.engine = engine;
  }

  /**
   * Get the current board.
   *
   * @return The current board.
   */
  final Board getBoard() {
    return board;
  }

  /**
   * Set a new board.
   *
   * @param board
   *          The new board.
   */
  final void setBoard(Board board) {
    this.board = board;
  }

  /**
   * Get the renderer.
   *
   * @return The current renderer.
   */
  final ChessBoardRenderer getRenderer() {
    return renderer;
  }

  /**
   * Set a new renderer.
   *
   * @param renderer
   *          The new renderer.
   */
  final void setRenderer(ChessBoardRenderer renderer) {
    this.renderer = renderer;
  }

  /**
   * Get the current game timer.
   *
   * @return The current game timer.
   */
  final GameTimer getGameTimer() {
    return gameTimer;
  }

  /**
   * Set a new game timer.
   *
   * @param timer
   *          The new game timer.
   */
  final void setGameTimer(GameTimer timer) {
    gameTimer = timer;
    timer.addActionListener(this);
  }

  /**
   * Reset the game controller.
   */
  public final void reset() {
    // White has the first move.
    whiteHasNextMove = true;

    // The game is stopped until white moves.
    gameState = GameState.STOPPED;

  }

  /**
   * Let the computer make a move.
   */
  public final boolean computerPly() {
    IPly nextPly = getEngine().computeBestPly();
    if (nextPly == null) {
      logger.info("No computer move returned");
      return false;
    }
    else {
      doPly(nextPly);
      getEngine().startPermanentBrain();
      return true;
    }
  }

  /**
   * The user moved a piece.
   *
   * @param ply
   *          The ply of the user.
   */
  public final void userPly(IPly ply) {

    // Before any user ply can be applied, we have to stop
    // the permanent brain, so the engine is free to process
    // the user ply.
    // if (getEngine() == null) {
    // System.out.println("getEngine returns null");
    // }
    getEngine().stopPermanentBrain();

    // plyInterpretation
    ply = convertUserPly(ply);

    // Check if the user has the right to move a piece
    // and made a valid move.
    if (whiteHasNextMove != isComputerWhite) {
      if ((gameState != GameState.CHECKMATE) && (gameState != GameState.DRAW)
          && (gameState != GameState.TIMEOUT)) {
        if (getEngine().validateUserPly(ply)) {
          doPly(ply);    // Ok => move the piece.
        }
        else {  // The user had to right to move or made an invalid move.
          signalUserInputError("invalid move");
        }
      }
      else {
        signalUserInputError("game is already over");
      }
    }
    else {
      signalUserInputError("user is not about to move");
    }
  }

  /**
   * Try to interpretate a user ply, if it's a castling or so.
   *
   * @param ply
   *          The user ply.
   * @return The converted ply.
   */
  @SuppressWarnings("deprecation")
  private final IPly convertUserPly(IPly ply) {

    Position source = ply.getSource();
    Position destination = ply.getDestination();
    IPiece piece = getBoard().getPiece(source);

    if (piece != null) {
      // Check, if the user wanted a castling.
      if (piece.getType() == IPiece.KING) {
        int sourceLine = source.getSquareIndex() & 7;
        int destinationLine = destination.getSquareIndex() & 7;

        // If the castling goes to the left.
        if ((sourceLine - 2) == destinationLine) {
          return new CastlingPlyImpl(source, true);
        }

        // If the castling goes to the right.
        if ((sourceLine + 2) == destinationLine) {
          return new CastlingPlyImpl(source, false);
        }
      }
      else {
        // Check if this is a transformation ply
        if (piece.getType() == IPiece.PAWN) {
          int destinationRow = destination.getSquareIndex() >> 3;

          // If the pawn reached the last row
          if (destinationRow == 7) {
            TransformationDialog.getInstance().show();
            byte pieceType = TransformationDialog.getInstance().getPieceType();

            // Create and return a new transformation ply.
            return new TransformationPlyImpl(source, destination, pieceType, getBoard().getPiece(
                destination) != null);
          }
          else {
            int sourceLine = source.getSquareIndex() & 7;
            int sourceRow = source.getSquareIndex() >> 3;
            int destinationLine = destination.getSquareIndex() & 7;

            if ((sourceRow == 4) && (destinationRow == 5)
                && (1 == Math.abs(sourceLine - destinationLine))) {

              IPly lastPly = currentGame.getLastPly();

              if ((lastPly != null)
                  && (lastPly.getSource().getSquareIndex() == (destination.getSquareIndex() + 8))
                  && (lastPly.getDestination().getSquareIndex() == (destination.getSquareIndex() - 8))
                  && (getBoard().getPiece(lastPly.getDestination()).getType() == IPiece.PAWN)) {
                return new EnPassantPlyImpl(source, destination, new PositionImpl(
                    destination.getSquareIndex() - 8));
              }
            }
          }
        }
      }
    }

    // Set the capture flag properly.
    ((PlyImpl) ply).setCapture(getBoard().getPiece(destination) != null);

    return ply;  // Return the original ply
  }

  /**
   * Perform a ply.
   *
   * @param ply
   *          The ply to do.
   */
  private final void doPly(IPly ply) {

    // If the game is over, stop here.
    if (gameState == GameState.TIMEOUT) {
      return;
    }

    // Store the ply notation to set the check flags a bit later, since they are computed in the
    // gameOver()
    // call in the move right toggle.
    IPlyNotation plyNotation = new PlyNotationImpl(ply, getBoard().getPiece(ply.getSource()));
    getGame().doPly(ply);
    getBoard().doPly(ply);
    getRenderer().doPly(ply);

    // Now try to get some info on the current game state.
    boolean gameOver = gameOver(!whiteHasNextMove);

    // Set check info for this ply.
    if (gameState == GameState.CHECKMATE) {
      plyNotation.setCheckMate(true);
    }
    else {
      if (gameState == GameState.CHECK) {
        plyNotation.setCheck(true);
      }
    }

    // Add the ply to the notation.
    getGameNotation().addPly(plyNotation);

    if (gameOver) {

      getGameTimer().stop();  // The game has ended.

      if (gameState == GameState.DRAW) {
        signalGameOver("Draw! Use File -> Reset to play again!");
      }
      else {
        signalGameOver(whiteHasNextMove == isComputerWhite ? "Checkmate! I win! :-)"
            : "Checkmate! I lose... :-(");
      }
    }
    else {
      toggleMoveRight();
    }
  }

  /**
   * Turn the right to move from one player to the other.
   */
  public void toggleMoveRight() {
    whiteHasNextMove = !whiteHasNextMove;

    if (getGameTimer().isRunning()) {
      getGameTimer().toggle();
    }
    else {
      getGameTimer().start();

      gameState = GameState.ONGOING;  // Change the game state to running.
    }

    // Check if the computer has the next move.
    if (whiteHasNextMove == isComputerWhite) {
      if (!computerPly()) {
        System.out.println("Computer cannot move!");
      }
    }
  }

  /**
   * Check if the game is over.
   *
   * @param white
   *          True, if white is about to move.
   * @return true, if the game is over.
   */
  private final boolean gameOver(boolean white) {
    gameState = getEngine().getCurrentGameState(white);

    return (gameState == GameState.CHECKMATE) || (gameState == GameState.DRAW);
  }

  /**
   * Process a event, that we are listening to.
   *
   * @param event
   *          The event.
   */
  public void actionPerformed(ActionEvent event) {

    System.out.println("Got event " + event.getActionCommand());

    // If the timer fired the event, check for timeout.
    if (event.getSource() == getGameTimer()) {
      String command = event.getActionCommand();

      if ("timeout white".equals(command)) {
        gameState = GameState.TIMEOUT;
        signalGameOver("Timeout for you! I win!");
      }

      if ("timeout black".equals(command)) {
        System.out.println("Black timeout!");
        gameState = GameState.TIMEOUT;
        signalGameOver("Timeout for me! You win!");
      }
    }
  }

  /**
   * Signal a input error to the user.
   *
   * @param errorMessage
   *          More information on the error.
   */
  public final void signalUserInputError(String errorMessage) {
    System.out.print((char) 7);
    JOptionPane.showMessageDialog(null, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
  }

  /**
   * Signal the end if the game.
   *
   * @param gameOverMessage
   *          Some info how and why the game has ended.
   */
  public final void signalGameOver(String gameOverMessage) {
    System.out.print((char) 7);
    JOptionPane.showMessageDialog(null, gameOverMessage, "Game over",
        JOptionPane.INFORMATION_MESSAGE);
  }
}
