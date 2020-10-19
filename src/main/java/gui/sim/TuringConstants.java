/*
 *  JFLAP - Formal Languages and Automata Package
 *
 *
 *  Susan H. Rodger
 *  Computer Science Department
 *  Duke University
 *  August 27, 2009

 *  Copyright (c) 2002-2009
 *  All rights reserved.

 *  JFLAP is open source software. Please see the LICENSE for terms.
 *
 */


package gui.sim;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * This interface holds constants necessary for the drawing of configuration
 * icons for Turing machines.
 *
 * @author Thomas Finley
 */

interface TuringConstants {
    /**
     * A simple prefix/postfix string for tape.
     */
    String FIX = FixCreator.getFix();

    /**
     * The size of the tape head.
     */
    int SIZE_HEAD = 4;

    class FixCreator {
        public static String getFix() {
            char c = automata.turing.Tape.BLANK;
            return IntStream.range(0, 20).mapToObj(i -> String.valueOf(c)).collect(Collectors.joining());
        }
    }
}
