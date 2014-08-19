// $ANTLR 2.7.1: "pgn.g" -> "PGNParser.java"$

package de.java_chess.javaChess.pgn;

import antlr.NoViableAltException;
import antlr.ParserSharedInputState;
import antlr.RecognitionException;
import antlr.SemanticException;
import antlr.Token;
import antlr.TokenBuffer;
import antlr.TokenStream;
import antlr.TokenStreamException;
import de.java_chess.javaChess.notation.IGameNotation;
import de.java_chess.javaChess.notation.IPlyNotation;

/**
 * A parser for PGN (Portable Game Notation) files.
 */
public class PGNParser extends antlr.LLkParser
       implements IPgnTokenTypes
 {

	/**
	 * The current move index.
         */
	private int _moveIndex;

	/**
	 * The buffer for the game notation.
	 */
	private IGameNotation _notation;

	/**
	 * Set a new buffer for the game notation.
  	 *
	 * @param notation The new notation buffer.
	 */
	public final void setNotation( IGameNotation notation) {
	    _notation = notation;
	}

	/**
	 * Get the current notation.
	 *
	 * @return The current notation.
	 */
	public final IGameNotation getNotation() {
	    return _notation;
	}

	/**
	 * A loader to follow the loaded game.
	 */
	private GameLoader _gameLoader;

	/**
	 * Get the current game loader.
	 *
	 * @return The current game loader.
	 */
	private final GameLoader getGameLoader() {
            return _gameLoader;
        }

	/**
	 * Set a new game loader.
	 * 
	 * @param loader The new game loader.
	 */
	private final void setGameLoader( GameLoader loader) {
	    _gameLoader = loader;
	}

protected PGNParser(TokenBuffer tokenBuf, int k) {
  super(tokenBuf,k);
  tokenNames = _tokenNames;
}

public PGNParser(TokenBuffer tokenBuf) {
  this(tokenBuf,2);
}

protected PGNParser(TokenStream lexer, int k) {
  super(lexer,k);
  tokenNames = _tokenNames;
}

public PGNParser(TokenStream lexer) {
  this(lexer,2);
}

public PGNParser(ParserSharedInputState state) {
  super(state,2);
  tokenNames = _tokenNames;
}

	public final void pgnGame(
		 IGameNotation notationBuffer
	) throws RecognitionException, TokenStreamException {
		
		
		setNotation( notationBuffer); 
		setGameLoader( new GameLoader());
		
		
		whiteSpaces();
		tagPairSection();
		moveTextSection();
	}
	
	public final void whiteSpaces() throws RecognitionException, TokenStreamException {
		
		
		{
		_loop29:
		do {
			if ((LA(1)==WS)) {
				match(WS);
			}
			else {
				break _loop29;
			}
			
		} while (true);
		}
	}
	
	public final void tagPairSection() throws RecognitionException, TokenStreamException {
		
		
		{
		_loop4:
		do {
			if ((LA(1)==LBRACK)) {
				tagPair();
				whiteSpaces();
			}
			else {
				break _loop4;
			}
			
		} while (true);
		}
	}
	
	public final void moveTextSection() throws RecognitionException, TokenStreamException {
		
		
		_moveIndex = 0;
		{
		_loop17:
		do {
			if ((LA(1)==MOVE_INDEX)) {
				move();
			}
			else {
				break _loop17;
			}
			
		} while (true);
		}
		match(GAME_TERMINATOR);
	}
	
	public final void tagPair() throws RecognitionException, TokenStreamException {
		
		
		match(LBRACK);
		whiteSpaces();
		{
		switch ( LA(1)) {
		case TAG_EVENT:
		{
			eventTag();
			break;
		}
		case TAG_SITE:
		{
			siteTag();
			break;
		}
		case TAG_DATE:
		{
			dateTag();
			break;
		}
		case TAG_ROUND:
		{
			roundTag();
			break;
		}
		case TAG_WHITE:
		{
			whiteTag();
			break;
		}
		case TAG_BLACK:
		{
			blackTag();
			break;
		}
		case TAG_RESULT:
		{
			resultTag();
			break;
		}
		case TAG_OPENING:
		{
			openingTag();
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		whiteSpaces();
		match(RBRACK);
	}
	
	public final void eventTag() throws RecognitionException, TokenStreamException {
		
		
		match(TAG_EVENT);
		whiteSpaces();
		match(STRING_LITERAL);
	}
	
	public final void siteTag() throws RecognitionException, TokenStreamException {
		
		
		match(TAG_SITE);
		whiteSpaces();
		match(STRING_LITERAL);
	}
	
	public final void dateTag() throws RecognitionException, TokenStreamException {
		
		
		match(TAG_DATE);
		whiteSpaces();
		match(STRING_LITERAL);
	}
	
	public final void roundTag() throws RecognitionException, TokenStreamException {
		
		
		match(TAG_ROUND);
		whiteSpaces();
		match(STRING_LITERAL);
	}
	
	public final void whiteTag() throws RecognitionException, TokenStreamException {
		
		Token  name = null;
		
		match(TAG_WHITE);
		whiteSpaces();
		name = LT(1);
		match(STRING_LITERAL);
		getNotation().setPlayerInfo( name.getText(), true);
	}
	
	public final void blackTag() throws RecognitionException, TokenStreamException {
		
		Token  name = null;
		
		match(TAG_BLACK);
		whiteSpaces();
		name = LT(1);
		match(STRING_LITERAL);
		getNotation().setPlayerInfo( name.getText(), false);
	}
	
	public final void resultTag() throws RecognitionException, TokenStreamException {
		
		
		match(TAG_RESULT);
		whiteSpaces();
		match(STRING_LITERAL);
	}
	
	public final void openingTag() throws RecognitionException, TokenStreamException {
		
		Token  name = null;
		
		match(TAG_OPENING);
		whiteSpaces();
		name = LT(1);
		match(STRING_LITERAL);
		getNotation().setOpeningInfo( name.getText());
	}
	
	public final void move() throws RecognitionException, TokenStreamException {
		
		Token  mI = null;
		IPlyNotation notation = null;
		
		mI = LT(1);
		match(MOVE_INDEX);
		if (!( ++_moveIndex == Integer.parseInt( mI.getText()) ))
		  throw new SemanticException(" ++_moveIndex == Integer.parseInt( mI.getText()) ");
		whiteSpaces();
		notation=ply();
		getNotation().addPly( notation);
		whiteSpaces();
		notation=ply();
		getNotation().addPly( notation);
		whiteSpaces();
	}
	
	public final IPlyNotation  ply() throws RecognitionException, TokenStreamException {
		IPlyNotation notation = null;
		
		Token  lc = null;
		Token  snOrg = null;
		Token  snDest = null;
		Token  lc2 = null;
		PGNPlyFragment plyFragment = new PGNPlyFragment();
		
		{
		switch ( LA(1)) {
		case FIGURINE_LETTER_CODE:
		case SQUARE_NAME:
		case PIECE_MOVE:
		case PIECE_CAPTURE:
		{
			{
			{
			switch ( LA(1)) {
			case FIGURINE_LETTER_CODE:
			{
				lc = LT(1);
				match(FIGURINE_LETTER_CODE);
				plyFragment.setPieceTypeFromLetter( lc.getText().charAt(0));
				break;
			}
			case SQUARE_NAME:
			case PIECE_MOVE:
			case PIECE_CAPTURE:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			{
			switch ( LA(1)) {
			case SQUARE_NAME:
			{
				snOrg = LT(1);
				match(SQUARE_NAME);
				plyFragment.setOrigin( snOrg.getText());
				break;
			}
			case PIECE_MOVE:
			case PIECE_CAPTURE:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			switch ( LA(1)) {
			case PIECE_MOVE:
			{
				match(PIECE_MOVE);
				plyFragment.setCapture( false);
				break;
			}
			case PIECE_CAPTURE:
			{
				match(PIECE_CAPTURE);
				plyFragment.setCapture( true);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			snDest = LT(1);
			match(SQUARE_NAME);
			plyFragment.setDestination( snDest.getText());
			}
			{
			switch ( LA(1)) {
			case PAWN_PROMOTION:
			{
				match(PAWN_PROMOTION);
				lc2 = LT(1);
				match(FIGURINE_LETTER_CODE);
				if (!( ! "P".equals( lc2.getText()) && ! "K".equals( lc2.getText())))
				  throw new SemanticException(" ! \"P\".equals( lc2.getText()) && ! \"K\".equals( lc2.getText())");
				plyFragment.setPawnPromotion( lc2.getText().charAt(0));
				break;
			}
			case GAME_TERMINATOR:
			case MOVE_INDEX:
			case FIGURINE_LETTER_CODE:
			case SQUARE_NAME:
			case PIECE_MOVE:
			case PIECE_CAPTURE:
			case LEFT_CASTLING:
			case RIGHT_CASTLING:
			case WS:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			}
			break;
		}
		case LEFT_CASTLING:
		{
			match(LEFT_CASTLING);
			plyFragment.setCastling( true);
			break;
		}
		case RIGHT_CASTLING:
		{
			match(RIGHT_CASTLING);
			plyFragment.setCastling( false);
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		// When we have all the info from the PGN file, we can try to
		// create a ply notation from it.
			     notation = getGameLoader().completePly( plyFragment);
		
		if (!(notation != null))
		  throw new SemanticException("notation != null");
		return notation;
	}
	
	
	public static final String[] _tokenNames = {
		"<0>",
		"EOF",
		"<2>",
		"NULL_TREE_LOOKAHEAD",
		"LBRACK",
		"RBRACK",
		"TAG_EVENT",
		"STRING_LITERAL",
		"TAG_SITE",
		"TAG_DATE",
		"TAG_ROUND",
		"TAG_WHITE",
		"TAG_BLACK",
		"TAG_RESULT",
		"TAG_OPENING",
		"GAME_TERMINATOR",
		"MOVE_INDEX",
		"FIGURINE_LETTER_CODE",
		"SQUARE_NAME",
		"PIECE_MOVE",
		"PIECE_CAPTURE",
		"PAWN_PROMOTION",
		"LEFT_CASTLING",
		"RIGHT_CASTLING",
		"WS",
		"DOT",
		"SL_COMMENT"
	};
	
	
	}
