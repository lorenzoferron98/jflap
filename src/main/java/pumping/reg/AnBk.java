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
 * The regular pumping lemma for <i>L</i> =
 * {<i>a<sup>n</sup>b<sup>k</sup></i> : <i>n</i> is
 * odd or <i>k</i> is even}.
 *
 * @author Chris Morgan
 */
public class AnBk extends RegularPumpingLemma {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public String getTitle() {
        return "a^n b^k : n is odd or k is even";
    }

    public String getHTMLTitle() {
        return "<i>a<sup>n</sup>b<sup>k</sup></i> : <i>n</i> is odd"
                + " or <i>k</i> is even.";
    }

    public void setDescription() {
        partitionIsValid = true;
        explanation = "Because this is a regular language, a valid decomposition exists.  If <i>m</i> " + GREATER_OR_EQ +
                " 3, a <i>y</i> value of \"aa\" or \"bb\" will always pump the string.  At least one of those substrings " +
                "can be the <i>y</i> value.";
    }

    protected void setRange() {
        myRange = new int[]{3, 10};
    }

    public void chooseI() {
        i = LemmaMath.flipCoin();
    }

    protected void chooseW() {
        w = m % 2 == 0 ? pumpString("a", m - 2) + "bb" : "a" + pumpString("b", m);
    }

    public void chooseDecomposition() {
        int firstB = w.indexOf('b');

        //One of these should be valid
        setDecomposition(firstB == -1 || firstB >= 2 ? new int[]{0, 2} : new int[]{firstB, 2});
    }

    public boolean isInLang(String s) {
        int a, b;
        char[] list = new char[]{'a', 'b'};
        if (LemmaMath.isMixture(s, list))
            return false;

        a = LemmaMath.countInstances(s, 'a');
        b = LemmaMath.countInstances(s, 'b');
        return a % 2 == 1 || b % 2 == 0;
    }
}
