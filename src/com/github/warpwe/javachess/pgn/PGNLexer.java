// $ANTLR 2.7.1: "pgn.g" -> "PGNLexer.java"$

package com.github.warpwe.javachess.pgn;

import java.io.InputStream;
import java.io.Reader;
import java.util.Hashtable;

import antlr.ByteBuffer;
import antlr.CharBuffer;
import antlr.CharStreamException;
import antlr.CharStreamIOException;
import antlr.InputBuffer;
import antlr.LexerSharedInputState;
import antlr.NoViableAltForCharException;
import antlr.RecognitionException;
import antlr.Token;
import antlr.TokenStream;
import antlr.TokenStreamException;
import antlr.TokenStreamIOException;
import antlr.TokenStreamRecognitionException;
import antlr.collections.impl.BitSet;

/**
 * A lexer for PGN files.
 */
public class PGNLexer extends antlr.CharScanner implements IPgnTokenTypes, TokenStream {
  public PGNLexer(InputStream in) {
    this(new ByteBuffer(in));
  }

  public PGNLexer(Reader in) {
    this(new CharBuffer(in));
  }

  public PGNLexer(InputBuffer ib) {
    this(new LexerSharedInputState(ib));
  }

  public PGNLexer(LexerSharedInputState state) {
    super(state);
    literals = new Hashtable<Object, Object>();
    caseSensitiveLiterals = true;
    setCaseSensitive(true);
  }

  public Token nextToken() throws TokenStreamException {
    Token theRetToken = null;
    tryAgain: for (;;) {
      Token _token = null;
      int _ttype = Token.INVALID_TYPE;
      resetText();
      try {   // for char stream error handling
        try {   // for lexical error handling
          switch (LA(1)) {
            case '\t':
            case '\n':
            case '\u000c':
            case '\r':
            case ' ': {
              mWS(true);
              theRetToken = _returnToken;
              break;
            }
            case '.': {
              mDOT(true);
              theRetToken = _returnToken;
              break;
            }
            case '[': {
              mLBRACK(true);
              theRetToken = _returnToken;
              break;
            }
            case ']': {
              mRBRACK(true);
              theRetToken = _returnToken;
              break;
            }
            case ';': {
              mSL_COMMENT(true);
              theRetToken = _returnToken;
              break;
            }
            case '"': {
              mSTRING_LITERAL(true);
              theRetToken = _returnToken;
              break;
            }
            case 'a':
            case 'b':
            case 'c':
            case 'd':
            case 'e':
            case 'f':
            case 'g':
            case 'h': {
              mSQUARE_NAME(true);
              theRetToken = _returnToken;
              break;
            }
            case '-': {
              mPIECE_MOVE(true);
              theRetToken = _returnToken;
              break;
            }
            case 'x': {
              mPIECE_CAPTURE(true);
              theRetToken = _returnToken;
              break;
            }
            case '=': {
              mPAWN_PROMOTION(true);
              theRetToken = _returnToken;
              break;
            }
            case 'W': {
              mTAG_WHITE(true);
              theRetToken = _returnToken;
              break;
            }
            case 'D': {
              mTAG_DATE(true);
              theRetToken = _returnToken;
              break;
            }
            case 'E': {
              mTAG_EVENT(true);
              theRetToken = _returnToken;
              break;
            }
            case 'S': {
              mTAG_SITE(true);
              theRetToken = _returnToken;
              break;
            }
            default:
              if ((LA(1) == 'O') && (LA(2) == '-')) {
                mRIGHT_CASTLING(true);
                theRetToken = _returnToken;
              }
              else if (((LA(1) >= '1' && LA(1) <= '9')) && (_tokenSet_0.member(LA(2)))) {
                mMOVE_INDEX(true);
                theRetToken = _returnToken;
              }
              else if ((LA(1) == 'B') && (LA(2) == 'l')) {
                mTAG_BLACK(true);
                theRetToken = _returnToken;
              }
              else if ((LA(1) == 'R') && (LA(2) == 'o')) {
                mTAG_ROUND(true);
                theRetToken = _returnToken;
              }
              else if ((LA(1) == 'R') && (LA(2) == 'e')) {
                mTAG_RESULT(true);
                theRetToken = _returnToken;
              }
              else if ((LA(1) == 'O') && (LA(2) == 'p')) {
                mTAG_OPENING(true);
                theRetToken = _returnToken;
              }
              else if ((_tokenSet_1.member(LA(1))) && (true)) {
                mFIGURINE_LETTER_CODE(true);
                theRetToken = _returnToken;
              }
              else if ((LA(1) == '*' || LA(1) == '0' || LA(1) == '1') && (true)) {
                mGAME_TERMINATOR(true);
                theRetToken = _returnToken;
              }
              else {
                if (LA(1) == EOF_CHAR) {
                  uponEOF();
                  _returnToken = makeToken(Token.EOF_TYPE);
                }
                else {
                  throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine());
                }
              }
          }
          if (_returnToken == null)
            continue tryAgain; // found SKIP token
          _ttype = _returnToken.getType();
          _ttype = testLiteralsTable(_ttype);
          _returnToken.setType(_ttype);
          return _returnToken;
        }
        catch (RecognitionException e) {
          throw new TokenStreamRecognitionException(e);
        }
      }
      catch (CharStreamException cse) {
        if (cse instanceof CharStreamIOException) {
          throw new TokenStreamIOException(((CharStreamIOException) cse).io);
        }
        else {
          throw new TokenStreamException(cse.getMessage());
        }
      }
    }
  }

  public final void mWS(boolean _createToken) throws RecognitionException, CharStreamException,
      TokenStreamException {
    int _ttype;
    Token _token = null;
    int _begin = text.length();
    _ttype = WS;
    int _saveIndex;

    {
      switch (LA(1)) {
        case ' ': {
          match(' ');
          break;
        }
        case '\t': {
          match('\t');
          break;
        }
        case '\u000c': {
          match('\f');
          break;
        }
        case '\n':
        case '\r': {
          {
            if ((LA(1) == '\r') && (LA(2) == '\n')) {
              match("\r\n");
            }
            else if ((LA(1) == '\r') && (true)) {
              match('\r');
            }
            else if ((LA(1) == '\n')) {
              match('\n');
            }
            else {
              throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine());
            }

          }
          newline();
          break;
        }
        default: {
          throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine());
        }
      }
    }
    /* $setType(Token.SKIP); */
    if (_createToken && _token == null && _ttype != Token.SKIP) {
      _token = makeToken(_ttype);
      _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
    }
    _returnToken = _token;
  }

  public final void mDOT(boolean _createToken) throws RecognitionException, CharStreamException,
      TokenStreamException {
    int _ttype;
    Token _token = null;
    int _begin = text.length();
    _ttype = DOT;
    int _saveIndex;

    match('.');
    if (_createToken && _token == null && _ttype != Token.SKIP) {
      _token = makeToken(_ttype);
      _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
    }
    _returnToken = _token;
  }

  public final void mLBRACK(boolean _createToken) throws RecognitionException, CharStreamException,
      TokenStreamException {
    int _ttype;
    Token _token = null;
    int _begin = text.length();
    _ttype = LBRACK;
    int _saveIndex;

    match('[');
    if (_createToken && _token == null && _ttype != Token.SKIP) {
      _token = makeToken(_ttype);
      _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
    }
    _returnToken = _token;
  }

  public final void mRBRACK(boolean _createToken) throws RecognitionException, CharStreamException,
      TokenStreamException {
    int _ttype;
    Token _token = null;
    int _begin = text.length();
    _ttype = RBRACK;
    int _saveIndex;

    match(']');
    if (_createToken && _token == null && _ttype != Token.SKIP) {
      _token = makeToken(_ttype);
      _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
    }
    _returnToken = _token;
  }

  public final void mSL_COMMENT(boolean _createToken) throws RecognitionException,
      CharStreamException, TokenStreamException {
    int _ttype;
    Token _token = null;
    int _begin = text.length();
    _ttype = SL_COMMENT;
    int _saveIndex;

    match(";");
    {
      _loop39: do {
        if ((_tokenSet_2.member(LA(1)))) {
          {
            match(_tokenSet_2);
          }
        }
        else {
          break _loop39;
        }

      }
      while (true);
    }
    {
      if ((LA(1) == '\r') && (LA(2) == '\n')) {
        match("\r\n");
      }
      else if ((LA(1) == '\n')) {
        match("\n");
      }
      else if ((LA(1) == '\r') && (true)) {
        match("\r");
      }
      else {
        throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine());
      }

    }
    _ttype = Token.SKIP;
    newline();
    if (_createToken && _token == null && _ttype != Token.SKIP) {
      _token = makeToken(_ttype);
      _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
    }
    _returnToken = _token;
  }

  public final void mSTRING_LITERAL(boolean _createToken) throws RecognitionException,
      CharStreamException, TokenStreamException {
    int _ttype;
    Token _token = null;
    int _begin = text.length();
    _ttype = STRING_LITERAL;
    int _saveIndex;

    {
      match('"');
      {
        _loop45: do {
          if ((_tokenSet_3.member(LA(1)))) {
            {
              match(_tokenSet_3);
            }
          }
          else {
            break _loop45;
          }

        }
        while (true);
      }
      match('"');
    }

    // Remove the leadind and trailing quote.
    String literal = new String(text.getBuffer(), _begin, text.length() - _begin);
    literal = literal.length() == 2 ? "" : literal.substring(1, literal.length() - 2);
    text.setLength(_begin);
    text.append(literal);

    if (_createToken && _token == null && _ttype != Token.SKIP) {
      _token = makeToken(_ttype);
      _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
    }
    _returnToken = _token;
  }

  public final void mSQUARE_NAME(boolean _createToken) throws RecognitionException,
      CharStreamException, TokenStreamException {
    int _ttype;
    Token _token = null;
    int _begin = text.length();
    _ttype = SQUARE_NAME;
    int _saveIndex;

    matchRange('a', 'h');
    matchRange('1', '8');
    if (_createToken && _token == null && _ttype != Token.SKIP) {
      _token = makeToken(_ttype);
      _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
    }
    _returnToken = _token;
  }

  public final void mPIECE_MOVE(boolean _createToken) throws RecognitionException,
      CharStreamException, TokenStreamException {
    int _ttype;
    Token _token = null;
    int _begin = text.length();
    _ttype = PIECE_MOVE;
    int _saveIndex;

    match('-');
    if (_createToken && _token == null && _ttype != Token.SKIP) {
      _token = makeToken(_ttype);
      _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
    }
    _returnToken = _token;
  }

  public final void mPIECE_CAPTURE(boolean _createToken) throws RecognitionException,
      CharStreamException, TokenStreamException {
    int _ttype;
    Token _token = null;
    int _begin = text.length();
    _ttype = PIECE_CAPTURE;
    int _saveIndex;

    match('x');
    if (_createToken && _token == null && _ttype != Token.SKIP) {
      _token = makeToken(_ttype);
      _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
    }
    _returnToken = _token;
  }

  public final void mPAWN_PROMOTION(boolean _createToken) throws RecognitionException,
      CharStreamException, TokenStreamException {
    int _ttype;
    Token _token = null;
    int _begin = text.length();
    _ttype = PAWN_PROMOTION;
    int _saveIndex;

    match('=');
    if (_createToken && _token == null && _ttype != Token.SKIP) {
      _token = makeToken(_ttype);
      _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
    }
    _returnToken = _token;
  }

  public final void mFIGURINE_LETTER_CODE(boolean _createToken) throws RecognitionException,
      CharStreamException, TokenStreamException {
    int _ttype;
    Token _token = null;
    int _begin = text.length();
    _ttype = FIGURINE_LETTER_CODE;
    int _saveIndex;

    {
      switch (LA(1)) {
        case 'P': {
          match('P');
          break;
        }
        case 'N': {
          match('N');
          break;
        }
        case 'B': {
          match('B');
          break;
        }
        case 'R': {
          match('R');
          break;
        }
        case 'Q': {
          match('Q');
          break;
        }
        case 'K': {
          match('K');
          break;
        }
        default: {
          throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine());
        }
      }
    }
    if (_createToken && _token == null && _ttype != Token.SKIP) {
      _token = makeToken(_ttype);
      _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
    }
    _returnToken = _token;
  }

  public final void mRIGHT_CASTLING(boolean _createToken) throws RecognitionException,
      CharStreamException, TokenStreamException {
    int _ttype;
    Token _token = null;
    int _begin = text.length();
    _ttype = RIGHT_CASTLING;
    int _saveIndex;

    match("O-O");
    {
      if ((LA(1) == '-')) {
        match("-O");
        _ttype = LEFT_CASTLING;
      }
      else {
      }

    }
    if (_createToken && _token == null && _ttype != Token.SKIP) {
      _token = makeToken(_ttype);
      _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
    }
    _returnToken = _token;
  }

  public final void mMOVE_INDEX(boolean _createToken) throws RecognitionException,
      CharStreamException, TokenStreamException {
    int _ttype;
    Token _token = null;
    int _begin = text.length();
    _ttype = MOVE_INDEX;
    int _saveIndex;

    {
      matchRange('1', '9');
      {
        _loop57: do {
          if (((LA(1) >= '0' && LA(1) <= '9'))) {
            matchRange('0', '9');
          }
          else {
            break _loop57;
          }

        }
        while (true);
      }
      mDOT(false);
    }

    // Remove the trailing dot.
    String index = new String(text.getBuffer(), _begin, text.length() - _begin);
    index = index.substring(0, index.length() - 1);
    text.setLength(_begin);
    text.append(index);

    if (_createToken && _token == null && _ttype != Token.SKIP) {
      _token = makeToken(_ttype);
      _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
    }
    _returnToken = _token;
  }

  public final void mTAG_WHITE(boolean _createToken) throws RecognitionException,
      CharStreamException, TokenStreamException {
    int _ttype;
    Token _token = null;
    int _begin = text.length();
    _ttype = TAG_WHITE;
    int _saveIndex;

    match("White");
    if (_createToken && _token == null && _ttype != Token.SKIP) {
      _token = makeToken(_ttype);
      _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
    }
    _returnToken = _token;
  }

  public final void mTAG_BLACK(boolean _createToken) throws RecognitionException,
      CharStreamException, TokenStreamException {
    int _ttype;
    Token _token = null;
    int _begin = text.length();
    _ttype = TAG_BLACK;
    int _saveIndex;

    match("Black");
    if (_createToken && _token == null && _ttype != Token.SKIP) {
      _token = makeToken(_ttype);
      _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
    }
    _returnToken = _token;
  }

  public final void mTAG_DATE(boolean _createToken) throws RecognitionException,
      CharStreamException, TokenStreamException {
    int _ttype;
    Token _token = null;
    int _begin = text.length();
    _ttype = TAG_DATE;
    int _saveIndex;

    match("Date");
    if (_createToken && _token == null && _ttype != Token.SKIP) {
      _token = makeToken(_ttype);
      _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
    }
    _returnToken = _token;
  }

  public final void mTAG_EVENT(boolean _createToken) throws RecognitionException,
      CharStreamException, TokenStreamException {
    int _ttype;
    Token _token = null;
    int _begin = text.length();
    _ttype = TAG_EVENT;
    int _saveIndex;

    match("Event");
    if (_createToken && _token == null && _ttype != Token.SKIP) {
      _token = makeToken(_ttype);
      _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
    }
    _returnToken = _token;
  }

  public final void mTAG_SITE(boolean _createToken) throws RecognitionException,
      CharStreamException, TokenStreamException {
    int _ttype;
    Token _token = null;
    int _begin = text.length();
    _ttype = TAG_SITE;
    int _saveIndex;

    match("Site");
    if (_createToken && _token == null && _ttype != Token.SKIP) {
      _token = makeToken(_ttype);
      _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
    }
    _returnToken = _token;
  }

  public final void mTAG_ROUND(boolean _createToken) throws RecognitionException,
      CharStreamException, TokenStreamException {
    int _ttype;
    Token _token = null;
    int _begin = text.length();
    _ttype = TAG_ROUND;
    int _saveIndex;

    match("Round");
    if (_createToken && _token == null && _ttype != Token.SKIP) {
      _token = makeToken(_ttype);
      _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
    }
    _returnToken = _token;
  }

  public final void mTAG_RESULT(boolean _createToken) throws RecognitionException,
      CharStreamException, TokenStreamException {
    int _ttype;
    Token _token = null;
    int _begin = text.length();
    _ttype = TAG_RESULT;
    int _saveIndex;

    match("Result");
    if (_createToken && _token == null && _ttype != Token.SKIP) {
      _token = makeToken(_ttype);
      _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
    }
    _returnToken = _token;
  }

  public final void mTAG_OPENING(boolean _createToken) throws RecognitionException,
      CharStreamException, TokenStreamException {
    int _ttype;
    Token _token = null;
    int _begin = text.length();
    _ttype = TAG_OPENING;
    int _saveIndex;

    match("Opening");
    if (_createToken && _token == null && _ttype != Token.SKIP) {
      _token = makeToken(_ttype);
      _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
    }
    _returnToken = _token;
  }

  public final void mGAME_TERMINATOR(boolean _createToken) throws RecognitionException,
      CharStreamException, TokenStreamException {
    int _ttype;
    Token _token = null;
    int _begin = text.length();
    _ttype = GAME_TERMINATOR;
    int _saveIndex;

    {
      switch (LA(1)) {
        case '*': {
          match("*");
          break;
        }
        case '0': {
          match("0-1");
          break;
        }
        default:
          if ((LA(1) == '1') && (LA(2) == '-')) {
            match("1-0");
          }
          else if ((LA(1) == '1') && (LA(2) == '/')) {
            match("1/2-1/2");
          }
          else {
            throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine());
          }
      }
    }
    if (_createToken && _token == null && _ttype != Token.SKIP) {
      _token = makeToken(_ttype);
      _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
    }
    _returnToken = _token;
  }

  private static final long _tokenSet_0_data_[] = {
      288019269919178752L, 0L, 0L, 0L, 0L
  };
  public static final BitSet _tokenSet_0 = new BitSet(_tokenSet_0_data_);
  private static final long _tokenSet_1_data_[] = {
      0L, 477188L, 0L, 0L, 0L
  };
  public static final BitSet _tokenSet_1 = new BitSet(_tokenSet_1_data_);
  private static final long _tokenSet_2_data_[] = {
      -9224L, -1L, -1L, -1L, 0L, 0L, 0L, 0L
  };
  public static final BitSet _tokenSet_2 = new BitSet(_tokenSet_2_data_);
  private static final long _tokenSet_3_data_[] = {
      -17179869192L, -1L, -1L, -1L, 0L, 0L, 0L, 0L
  };
  public static final BitSet _tokenSet_3 = new BitSet(_tokenSet_3_data_);

}
