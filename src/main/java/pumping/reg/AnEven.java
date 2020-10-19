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


package pumping.reg;

import pumping.LemmaMath;
import pumping.RegularPumpingLemma;

/**
 * The regular pumping lemma for <i>L</i> = {<i>a<sup>n</sup></i>
 * : <i>n</i> is even}.
 *
 * @author Jinghui Lim & Chris Morgan
 */
public class AnEven extends RegularPumpingLemma {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public String getTitle() {
        return "a^n : n is even";
    }

    public String getHTMLTitle() {
        return "<i>a<sup>n</sup></i> : <i>n</i> is even";
    }

    public void setDescription() {
        partitionIsValid = true;
        explanation = "Because this is a regular language, a valid decomposition exists.  If <i>m</i> " + GREATER_OR_EQ + " 2, " +
                "the <i>y</i> value \"aa\" will always pump the string.";
    }

    protected void chooseW() {
        w = getM() % 2 == 0 ? pumpString("a", getM()) : pumpString("a", getM() + 1);
    }

    public void chooseI() {
        i = LemmaMath.flipCoin();
    }

    protected void setRange() {
        myRange = new int[]{2, 18};
    }

    public void chooseDecomposition() {
        setDecomposition(new int[]{0, 2});
    }

    /**
     * Checks if the pumped string is in the language.
     *
     * @return <code>true</code> if it is, <code>false</code> otherwise
     */
    public boolean isInLang(String s) {
        String temp;
        char[] list = new char[]{'a'};
        if (LemmaMath.otherCharactersFound(s, list))
            return false;

        temp = s.length() == 0 ? createPumpedString() : s;
        return temp.length() % 2 == 0;
    }
}
