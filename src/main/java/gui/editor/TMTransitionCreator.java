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


package gui.editor;

import automata.State;
import automata.Transition;
import automata.turing.TMTransition;
import automata.turing.TuringMachine;
import gui.viewer.AutomatonPane;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * This is the creator of transitions in turing machines.
 *
 * @author Thomas Finley
 */

public class TMTransitionCreator extends TableTransitionCreator {
    /**
     * The action for the strokes for the direction field.
     */
    private static final Action CHANGE_ACTION = new AbstractAction() {
        /**
         *
         */
        private static final long serialVersionUID = 1L;

        public void actionPerformed(ActionEvent e) {
            JComboBox<?> box = (JComboBox<?>) e.getSource();
            box.setSelectedItem(e.getActionCommand().toUpperCase());
        }
    };
    /**
     * The directions.
     */
    private static String[] DIRS = new String[]{"R", "S", "L"}; //made this non-static to allow for switching option
    /**
     * The direction field combo box.
     */
    private static final JComboBox<String> BOX = new JComboBox<>(DIRS);
    /**
     * The array of keystrokes for the direction field.
     */
    private static KeyStroke[] STROKES;

    static {
        STROKES = new KeyStroke[DIRS.length];
        for (int i = 0; i < STROKES.length; i++)
            STROKES[i] = KeyStroke.getKeyStroke("shift " + DIRS[i]);
    }

    /**
     * The Turing machine.
     */
    private final TuringMachine machine;
    private boolean blockTransition = false;

    /**
     * Instantiates a new <CODE>TMTransitionCreator</CODE>.
     *
     * @param parent the parent of whatever dialogs/windows get brought up by this
     *               creator
     */
    public TMTransitionCreator(AutomatonPane parent) {
        super(parent);
        machine = (TuringMachine) parent.getDrawer().getAutomaton();
    }

    public static void setDirs(boolean allowStay) {
        if (allowStay)
            DIRS = new String[]{"R", "S", "L"}; //made this non-static to allow for switching option
        else {
//            EDebug.print("Reduction");
            DIRS = new String[]{"R", "L"}; //made this non-static to allow for switching option
        }

        STROKES = new KeyStroke[DIRS.length]; //we shall see ...arrays can't change size though
        for (int i = 0; i < STROKES.length; i++)
            STROKES[i] = KeyStroke.getKeyStroke("shift " + DIRS[i]);

        BOX.removeAllItems();
        for (int i = 0; i < DIRS.length; i++)
            BOX.addItem(DIRS[i]);
    }

    /**
     * Initializes a new empty transition.
     *
     * @param from the from state
     * @param to   to too state
     */
    protected Transition initTransition(State from, State to) {
        if (!blockTransition)
            return initTransition(from, to, "R");
        else
            return initTransition(from, to, "S");
    }

    /**
     * Initializes a new empty transition.
     *
     * @param from the from state
     * @param to   to too state
     */
    protected Transition initTransition(State from, State to,
                                        String directionString) {
        String[] read = new String[machine.tapes()];
        for (int i = 0; i < read.length; i++)
            read[i] = "";
        String[] write = read;
        if (blockTransition) {
            write = new String[machine.tapes()];
            for (int i = 0; i < write.length; i++) {
                write[i] = "~";
            }
        }
        String[] direction = new String[machine.tapes()];
        for (int i = 0; i < direction.length; i++)
            direction[i] = directionString;
        TMTransition t = new TMTransition(from, to, read, write, direction);
        t.setBlockTransition(blockTransition);
        return t;
    }

    /**
     * Given a transition, returns the arrays the editing table model needs.
     *
     * @param transition the transition to build the arrays for
     * @return the arrays for the editing table model
     */
    private String[][] arraysForTransition(TMTransition transition) {
        String[][] s = new String[machine.tapes()][3];
        for (int i = machine.tapes() - 1; i >= 0; i--) {
            s[i][0] = transition.getRead(i);
            s[i][1] = transition.getWrite(i);
            if (s[i][0].equals(TMTransition.BLANK))
                s[i][0] = "";
            if (s[i][1].equals(TMTransition.BLANK))
                s[i][1] = "";
            s[i][2] = transition.getDirection(i);
        }
        return s;
    }

    /**
     * Creates a new table model.
     *
     * @param transition the transition to create the model for
     * @return a table model for the transition
     */
    protected TableModel createModel(Transition transition) {
        final TMTransition t = (TMTransition) transition;
        return new AbstractTableModel() {
            /**
             *
             */
            private static final long serialVersionUID = 1L;
            final String[][] s = arraysForTransition(t);
            final String[] name = {"Read", "Write", "Direction"};

            public Object getValueAt(int row, int column) {
                return s[row][column];
            }

            public void setValueAt(Object o, int r, int c) {
                s[r][c] = (String) o;
            }

            public boolean isCellEditable(int r, int c) {
                if (!blockTransition)
                    return true;
                else return c == 0;
            }

            public int getRowCount() {
                return machine.tapes();
            }

            public int getColumnCount() {
                if (!blockTransition)
                    return 3;
                else
                    return 1;

            }

            public String getColumnName(int c) {
                return name[c];
            }
        };
    }

    /**
     * Creates the table.
     */
    protected JTable createTable(Transition transition) {
        JTable table = super.createTable(transition);
        if (!blockTransition) {
            TableColumn directionColumn = table.getColumnModel().getColumn(2);
            directionColumn.setCellEditor(new DefaultCellEditor(BOX) {
                /**
                 *
                 */
                private static final long serialVersionUID = 1L;

                public Component getTableCellEditorComponent(JTable table,
                                                             Object value, boolean isSelected, int row, int column) {
                    final JComboBox<?> c = (JComboBox<?>) super
                            .getTableCellEditorComponent(table, value,
                                    isSelected, row, column);
                    InputMap imap = c.getInputMap();
                    ActionMap amap = c.getActionMap();
                    Object o = new Object();
                    amap.put(o, CHANGE_ACTION);
                    for (int i = 0; i < STROKES.length; i++)
                        imap.put(STROKES[i], o);
                    return c;
                }
            });
        }
        return table;
    }

    /**
     * Modifies a transition according to what's in the table.
     */
    public Transition modifyTransition(Transition transition, TableModel model) {
        TMTransition t = (TMTransition) transition;
        try {
            String[] reads = new String[machine.tapes()];
            String[] writes = new String[machine.tapes()];
            String[] dirs = new String[machine.tapes()];
            for (int i = 0; i < machine.tapes(); i++) {
                reads[i] = (String) model.getValueAt(i, 0);
                writes[i] = (String) model.getValueAt(i, 1);
                dirs[i] = (String) model.getValueAt(i, 2);
            }
            TMTransition newTrans = new TMTransition(t.getFromState(), t
                    .getToState(), reads, writes, dirs);
            if (transition instanceof TMTransition) {
                TMTransition oldTrans = (TMTransition) transition;
                newTrans.setBlockTransition(oldTrans.isBlockTransition());
            }
            return newTrans;
        } catch (IllegalArgumentException e) {
            reportException(e);
            return null;
        }
    }

    public boolean isBlockTransition() {
        return blockTransition;
    }

    public void setBlockTransition(boolean block) {
        blockTransition = block;
    }
}