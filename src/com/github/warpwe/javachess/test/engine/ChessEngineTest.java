/*
 * ChessEngineTest - Test the chess engine. Copyright (C) 2003 The Java-Chess team
 * <info@java-chess.de> This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free Software Foundation;
 * either version 2 of the License, or (at your option) any later version. This program is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details. You should have received a copy of the GNU General Public
 * License along with this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 */

package com.github.warpwe.javachess.test.engine;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * This class generates the testsuite for the engine tests.
 */
public class ChessEngineTest {

  /**
   * Create the engine test suite.
   *
   * @return The engine test suite.
   */
  public static Test suite() {

    // Create a new test suite
    TestSuite suite = new TestSuite();

    // Test the ply generator
    suite.addTest(new PlyGeneratorTest1());
    suite.addTest(new PlyGeneratorTest2());
    suite.addTest(new PlyGeneratorTest3());
    suite.addTest(new PlyGeneratorTest4());
    suite.addTest(new PlyGeneratorTest5());
    suite.addTest(new PlyGeneratorTest6());
    suite.addTest(new PlyGeneratorTest7());
    suite.addTest(new PlyGeneratorTest8());
    suite.addTest(new PlyGeneratorTest9());
    suite.addTest(new PlyGeneratorTest10());
    suite.addTest(new PlyGeneratorTest11());
    suite.addTest(new PlyGeneratorTest12());
    suite.addTest(new PlyGeneratorTest13());
    suite.addTest(new PlyGeneratorTest16());
    suite.addTest(new PlyGeneratorTest17());

    // Add the actual engine tests.
    suite.addTest(new KingInKnightCheckTest());

    // Test the analyzer
    suite.addTest(new AnalyzerTest1());
    suite.addTest(new AnalyzerTest2());

    // Test the complete engine
    suite.addTest(new MinimaxTest1());
    suite.addTest(new MinimaxTest2());
    suite.addTest(new BoardConsistency1());

    return suite;
  }
}
