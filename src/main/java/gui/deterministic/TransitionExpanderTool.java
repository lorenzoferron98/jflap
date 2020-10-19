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


package gui.deterministic;

import automata.State;
import gui.editor.TransitionTool;
import gui.viewer.AutomatonDrawer;
import gui.viewer.AutomatonPane;

import javax.swing.*;
import java.awt.event.MouseEvent;

/**
 * This is a specialized transition tool that handles the expansion of a group
 * on a terminal. The user clicks on a state, and drags possibly to another
 * state if he wants the default in the input dialog to be the destination
 * state's set of NFA states, or to empty space. The user is then prompted for
 * the terminal to expand on, and then asked to specify those states which are
 * to appear.
 *
 * @author Thomas Finley
 */

public class TransitionExpanderTool extends TransitionTool {
    /**
     * The conversion to DFA controller.
     */
    private final ConversionController controller;

    /**
     * Instantiates a new <CODE>TransitionExpanderTool</CODE>.
     *
     * @param view       the view where the automaton is drawn
     * @param drawer     the object that draws the automaton
     * @param controller the NFA to DFA controller object
     */
    public TransitionExpanderTool(AutomatonPane view, AutomatonDrawer drawer,
                                  ConversionController controller) {
        super(view, drawer);
        this.controller = controller;
    }

    /**
     * When we release the mouse, a transition from the start state to this
     * released state must be formed.
     *
     * @param event the mouse event
     */
    public void mouseReleased(MouseEvent event) {
        // Did we even start at a state?
        if (first == null)
            return;
        State state = getDrawer().stateAtPoint(event.getPoint());
        controller.expandState(first, event.getPoint(), state);
        first = null;
        getView().repaint();
    }

    /**
     * Returns the tool icon.
     *
     * @return the group expander tool icon
     */
    protected Icon getIcon() {
        java.net.URL url = getClass().getResource("/ICON/expand_group.gif");
        return new ImageIcon(url);
    }

    /**
     * Gets the tool tip for this tool.
     *
     * @return the tool tip for this tool
     */
    public String getToolTip() {
        return "Expand Group on Terminal";
    }
}
