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


package automata.event;

import automata.Automaton;
import automata.Transition;

import java.util.EventObject;

/**
 * This event is given to listeners of an automaton interested in events when a
 * transition on an automaton is added, removed, or changed.
 *
 * @author Thomas Finley
 * @see Automaton
 * @see Transition
 * @see Automaton#addTransition
 * @see Automaton#removeTransition
 * @see AutomataTransitionListener
 */

public class AutomataTransitionEvent extends EventObject {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /**
     * Was this an add?
     */
    private final boolean myAdd;
    /**
     * Which transition did we add/remove?
     */
    private final Transition myTransition;
    /**
     * Is this a change in property?
     */
    private final boolean myChange;

    /**
     * Instantiates a new <CODE>AutomataStateEvent</CODE>.
     *
     * @param auto       the <CODE>Automaton</CODE> that generated the event
     * @param transition the <CODE>Transition</CODE> that was added or removed
     * @param add        <CODE>true</CODE> if the transition is added, <CODE>false</CODE>
     *                   if removed
     * @param change     <CODE>true</CODE> if some property of the transition was
     *                   changed, <CODE>false</CODE> if this is not a simple change
     */
    public AutomataTransitionEvent(Automaton auto, Transition transition,
                                   boolean add, boolean change) {
        super(auto);
        myTransition = transition;
        myAdd = add;
        myChange = change;
    }

    /**
     * Returns the <CODE>Automaton</CODE> that generated this event.
     *
     * @return the <CODE>Automaton</CODE> that generated this event
     */
    public Automaton getAutomaton() {
        return (Automaton) getSource();
    }

    /**
     * Returns the <CODE>Transition</CODE> that was added/removed.
     *
     * @return the <CODE>Transition</CODE> that was added/removed
     */
    public Transition getTransition() {
        return myTransition;
    }

    /**
     * Returns if this was an add.
     *
     * @return <CODE>true</CODE> if this event indicates the addition of a
     * transition, <CODE>false</CODE> otherwise
     */
    public boolean isAdd() {
        return myAdd;
    }

    /**
     * Returns if this was a delete.
     *
     * @return <CODE>true</CODE> if this event indicates the removal of a
     * transition, <CODE>false</CODE> otherwise
     */
    public boolean isDelete() {
        return !(myAdd || myChange);
    }

    /**
     * Returns if this was a simple change in a property of the transition.
     *
     * @return <CODE>true</CODE> if the properties of this transition were
     * changed, <CODE>false</CODE> otherwise
     */
    public boolean isChange() {
        return myChange;
    }
}
