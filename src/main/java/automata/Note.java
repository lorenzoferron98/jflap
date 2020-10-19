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


package automata;

import gui.editor.DeleteTool;
import gui.editor.EditorPane;
import gui.editor.Tool;
import gui.environment.AutomatonEnvironment;
import gui.viewer.AutomatonPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * A class that represents notes on the JFLAP canvas.
 */
public class Note extends JTextArea {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public boolean moving = false;
    public Point myViewPoint = new Point(0, 0);
    protected Point initialPointState;
    protected Point initialPointClick;
    protected AutomatonPane myView;
    private Point myAutoPoint = null;

    public Note(Point p, String message) {
        setLocationManually(p);
        this.setText(message);
    }

    /**
     * Creates an instance of <CODE>Note</CODE> with a specified message.
     */
    public Note(String message) {
        this.setText(message);
    }

    public Note(Point point) {
        setLocationManually(point);
    }

    /**
     * Initializes the note with relevant properties.
     */
    public void initializeForView(AutomatonPane view) {
        myView = view;
        setLocationManually(myAutoPoint);
        this.setDisabledTextColor(Color.BLACK);
        this.setBackground(new Color(255, 255, 150));
        this.addMouseMotionListener(new MouseMotionListener() {
            public void mouseDragged(MouseEvent e) {
                if (e.isPopupTrigger())
                    return;
                if (!((Note) e.getSource()).isEditable()) {
                    int diffX = e.getPoint().x - initialPointClick.x;
                    int diffY = e.getPoint().y - initialPointClick.y;

                    int nowAtX = initialPointState.x + diffX;
                    int nowAtY = initialPointState.y + diffY;
                    ((Note) e.getSource()).setLocationManually(new Point(nowAtX, nowAtY));
                    initialPointState = new Point(((Note) e.getSource()).getAutoPoint());
                } else {
                    //do normal select functionality
                }
                myView.repaint();
            }

            public void mouseMoved(MouseEvent e) {
                // TODO Auto-generated method stub

            }
        });
        this.addMouseListener(new MouseListener() {

            public void mouseClicked(MouseEvent e) {
                if (myView.getDrawer().getAutomaton().getEnvironmentFrame() != null)
                    ((AutomatonEnvironment) myView.getDrawer().getAutomaton().getEnvironmentFrame().getEnvironment()).saveStatus();
                e.getComponent().setEnabled(true);
                ((Note) e.getComponent()).setEditable(true);
                ((Note) e.getComponent()).setCaretColor(null);
            }

            public void mousePressed(MouseEvent e) {
                if (myView.getDrawer().getAutomaton().getEnvironmentFrame() != null) {
                    ((AutomatonEnvironment) myView.getDrawer().getAutomaton().getEnvironmentFrame().getEnvironment()).saveStatus();
                    myView.getDrawer().getAutomaton().getEnvironmentFrame().getEnvironment().setDirty();
                }

                initialPointState = new Point(((Note) e.getSource()).getAutoPoint());
                initialPointClick = new Point(e.getPoint().x, e.getPoint().y);

                //delete the text box
                EditorPane pane = myView.getCreator();
                Tool curTool = pane.getToolBar().getCurrentTool();
                if (curTool instanceof DeleteTool) {
                    myView.remove((Note) e.getSource());
                    myView.getDrawer().getAutomaton().deleteNote((Note) e.getSource());
                    myView.repaint();
                }

            }


            public void mouseReleased(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            public void mouseEntered(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            public void mouseExited(MouseEvent e) {
                // TODO Auto-generated method stub

            }

        });
        myView.add(this);
        setEnabled(true);
        setEditable(true);
        setCaretColor(null);
        this.setSelectionStart(0);
        this.setSelectionEnd(this.getColumnWidth());
        this.requestFocus();
    }

    public Point getAutoPoint() {
        return myAutoPoint;
    }

    /**
     * Gets the AutomatonPane that the Note belongs to.
     *
     * @return the AutomatonPane that the Note belongs to.
     */
    public AutomatonPane getView() {
        return myView;
    }

    /*
       For the undo part of cloning, we need a way to store the view without becoming visible / active.
     */
    public void setView(AutomatonPane view) {
        myView = view;
    }

    /**
     * Sets the Note manually to a specified Point.
     *
     * @param point
     */
    public void setLocationManually(Point point) {
        moving = true;
        myAutoPoint = point;
        if (myView != null) {
            setLocation(myView.transformFromAutomatonToView(point));
        }
    }

    public void setLocation(Point p) {
        if (moving) {
            if (myView != null) {
                myViewPoint = p;
                super.setLocation(p);
            }
        }
    }

    public void setLocation(int x, int y) {
        if (moving) {
            super.setLocation(x, y);
        }
        moving = false;
    }


    public void updateView() {
        setLocationManually(myAutoPoint);

    }

    public int specialHash() {
//        EDebug.print(myAutoPoint.hashCode() + getText().hashCode());
        return myAutoPoint == null ? -1 : myAutoPoint.hashCode() + this.getText().hashCode();
    }


}