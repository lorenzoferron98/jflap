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


package gui.environment;

import file.xml.DOMPrettier;
import gui.editor.TMTransitionCreator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Map;

public class Profile {
    /**
     * The tag name for the root of a structure.
     */
    public static final String STRUCTURE_NAME = "structure";
    /**
     * The tag name for the type of structure this is.
     */
    public static final String STRUCTURE_TYPE_NAME = "type";
    /**
     * The tag name for the out from final state preference.
     */
    public static final String TURING_FINAL_NAME = "turing_final";
    /**
     * The tag name for the Undo amount preference.
     */
    public static final String UNDO_AMOUNT_NAME = "undo_amount";
    /**
     * The tag name for accept by final state preference
     */
    public static final String ACCEPT_FINAL_STATE = "turing_accept_by_final_state";
    /**
     * The tag name for accept by halting preference
     */
    public static final String ACCEPT_HALT = "turing_accept_by_halt";
    /**
     * The tag name for allow-stay preference.
     */
    public static final String ALLOW_STAY = "turing_allow_stay_on_transition";
    public static String LAMBDA = "\u03BB";     // Jinghui Lim added stuff
    public static String EPSILON = "\u03B5";    // see MultipleSimulateAction
    /**
     * A JCheckBoxMenuItem that displays and allows one to change transTuringFinal.
     */
    private final JCheckBoxMenuItem transTuringFinalCheckBox;
    private final JCheckBoxMenuItem turingAcceptByFinalStateCheckBox;
    private final JCheckBoxMenuItem turingAcceptByHaltingCheckBox;
    private final JCheckBoxMenuItem turingAllowStayCheckBox;
    private final JCheckBoxMenuItem turingSudkampConvenctionsCheckBox;
    public String lambda = "\u03BB";
    public String epsilon = "\u03B5";
    public String lambdaText = "u03BB";
    public String epsilonText = "u03B5";
    public String Color = "Original";
    public int undo_num = 50;
    /**
     * The tag bane for the empty string preference.
     */
    public String EMPTY_STRING_NAME = "empty_string";
    public String pathToFile = "";
    private String emptyString = lambda;
    /**
     * Determines whether transitions can be issued from the final
     * state of a Turing machine.
     *
     * @author Chris Morgan
     */
    private boolean transTuringFinal;
    //default to acceptByFinalState, since that was how it used to be
    private boolean turingAcceptByFinalState; //I would rather have it a better way, but I'm short on Time - ~Henry
    private boolean turingAcceptByHalting; //I would rather have it a better way, but I'm short on Time - ~Henry
    private boolean turingAllowStay; //default to true since that was the old implementation
    private boolean turingSudkampConvenctions;

    public Profile() {
        emptyString = lambda;
        transTuringFinal = false;
        transTuringFinalCheckBox = new JCheckBoxMenuItem("Enable Transitions From Turing Machine Final States");
        transTuringFinalCheckBox.setSelected(transTuringFinal);
        transTuringFinalCheckBox.addActionListener(e -> {
            setTransitionsFromTuringFinalStateAllowed(transTuringFinalCheckBox.isSelected());
            savePreferences();
        });

        turingAcceptByFinalState = true; //default to true, since that was the status before;
        turingAcceptByFinalStateCheckBox = new JCheckBoxMenuItem("Accept by Final State");
        turingAcceptByFinalStateCheckBox.setSelected(turingAcceptByFinalState);
        turingAcceptByFinalStateCheckBox.addActionListener(e -> {
            setAcceptByFinalState(turingAcceptByFinalStateCheckBox.isSelected());
            savePreferences();
        });

        turingAcceptByHalting = false; //defaults to false, since it was not in previous JFLAP
        turingAcceptByHaltingCheckBox = new JCheckBoxMenuItem("Accept by Halting");
        turingAcceptByHaltingCheckBox.setSelected(turingAcceptByHalting);
        turingAcceptByHaltingCheckBox.addActionListener(e -> {
            setAcceptByHalting(turingAcceptByHaltingCheckBox.isSelected());
            savePreferences();
        });

        turingAllowStay = false; //defaults to false temporarily since that's how it was before
        turingAllowStayCheckBox = new JCheckBoxMenuItem("Allow stay for tape head on transition");
        turingAllowStayCheckBox.setSelected(turingAllowStay);
        turingAllowStayCheckBox.addActionListener(e -> {
            setAllowStay(turingAllowStayCheckBox.isSelected());
            savePreferences();
        });

        turingSudkampConvenctions = true;
        turingSudkampConvenctionsCheckBox = new JCheckBoxMenuItem("Allow Sudkamp's conventions");
        turingSudkampConvenctionsCheckBox.setSelected(turingSudkampConvenctions);
        turingSudkampConvenctionsCheckBox.addActionListener(e -> {
            setSudkampConventions(turingSudkampConvenctionsCheckBox.isSelected());
            savePreferences();
        });
    }

    protected static Element createElement(Document document, String tagname,
                                           Map<?, ?> attributes, String text) {
        // Create the new element.
        Element element = document.createElement(tagname);

        // Add the text element.
        if (text != null)
            element.appendChild(document.createTextNode(text));
        return element;
    }

    public void setNumUndo(int nn) {
        undo_num = nn;
    }

    /**
     * Returns the empty string.
     *
     * @return the empty string
     */
    public String getEmptyString() {
        return emptyString;
    }

    /**
     * Sets the empty string.
     *
     * @param empty the empty string
     */
    public void setEmptyString(String empty) {
        emptyString = empty;
    }

    /**
     * Returns the current color.
     *
     * @return the current color
     */
    public String getColor() {
        return Color;
    }

    /**
     * Sets the color.
     *
     * @param new color
     */
    public void setColor(String color) {
        Color = color;
    }

    /**
     * Sets whether transitions leading from Turing machine final states are allowed.
     *
     * @param t whether the transitions are allowed
     */
    public void setTransitionsFromTuringFinalStateAllowed(boolean t) {
        transTuringFinal = t;
        transTuringFinalCheckBox.setSelected(t);
    }

    /**
     * Sets whether Turing machines will accept by halting.
     *
     * @param t yes or no
     */
    public void setAllowStay(boolean t) {
        turingAllowStay = t;
        turingAllowStayCheckBox.setSelected(t);
        TMTransitionCreator.setDirs(t);
    }

    /**
     * Sets whether Turing machines will use Sudkamp's conventions.
     *
     * @param t yes or no
     */
    public void setSudkampConventions(boolean t) {
        turingSudkampConvenctions = t;
        turingSudkampConvenctionsCheckBox.setSelected(t);
    }

    /**
     * Returns whether transitions from Turing machine final states are allowed.
     *
     * @return whether the transitions are allowed from final states
     */
    public boolean transitionsFromTuringFinalStateAllowed() {
        return transTuringFinal;
    }

    public boolean getAcceptByFinalState() {
        return turingAcceptByFinalState;
    }

    /**
     * Sets whether Turing machines will accept by final state.
     *
     * @param t yes or no
     */
    public void setAcceptByFinalState(boolean t) {
        turingAcceptByFinalState = t;
        turingAcceptByFinalStateCheckBox.setSelected(t);
    }

    public boolean getAcceptByHalting() {
        return turingAcceptByHalting;
    }

    public boolean getSudkampConvenctionsEnabled() {
        return turingSudkampConvenctions;
    }

    /**
     * Sets whether Turing machines will accept by halting.
     *
     * @param t yes or no
     */
    public void setAcceptByHalting(boolean t) {
        turingAcceptByHalting = t;
        turingAcceptByHaltingCheckBox.setSelected(t);
    }

    /**
     * Returns the JCheckBoxMenuItem that can allow the user to change whether
     * Turing machine final states are allowed.
     */
    public JCheckBoxMenuItem getTuringFinalCheckBox() {
        return transTuringFinalCheckBox;
    }

    public JCheckBoxMenuItem getAcceptByFinalStateCheckBox() {
        return turingAcceptByFinalStateCheckBox;
    }

    public JCheckBoxMenuItem getAcceptByHaltingCheckBox() {
        return turingAcceptByHaltingCheckBox;
    }

    public JCheckBoxMenuItem getAllowStayCheckBox() {
        return turingAllowStayCheckBox;
    }

    public JCheckBoxMenuItem getSudkampConvenctions() {
        return turingSudkampConvenctionsCheckBox;
    }

    /**
     * Saves the preferences stored in this profile in jflapPreferences.xml.
     */
    public void savePreferences() {
        String empty = "";
        if (emptyString.equals(lambda)) empty = lambdaText;
        else if (emptyString.equals(epsilon)) empty = epsilonText;

        DocumentBuilderFactory factory = DocumentBuilderFactory
                .newInstance();
        DocumentBuilder builder;
        try {
            File file = new File(pathToFile);

            builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();
            doc.appendChild(doc.createComment("Created with JFLAP "
                    + gui.AboutBox.VERSION + "."));
            // Create and add the <structure> element.
            Element structureElement = createElement(doc, STRUCTURE_NAME, null,
                    null);
            doc.appendChild(structureElement);
            Element se = doc.getDocumentElement();
            Element element = createElement(doc, EMPTY_STRING_NAME, null, "" + empty);
            se.appendChild(element);
            element = createElement(doc, TURING_FINAL_NAME, null, "" + transTuringFinal);
            se.appendChild(element);
            element = createElement(doc, UNDO_AMOUNT_NAME, null, "" + undo_num);
            se.appendChild(element);
            element = createElement(doc, ACCEPT_FINAL_STATE, null, "" + turingAcceptByFinalState);
            se.appendChild(element);
            element = createElement(doc, ACCEPT_HALT, null, "" + turingAcceptByHalting);
            se.appendChild(element);
            element = createElement(doc, ALLOW_STAY, null, "" + turingAllowStay);
            se.appendChild(element);

            DOMPrettier.makePretty(doc);
            Source s = new DOMSource(doc);
            Result r = new StreamResult(file);
            Transformer t;
            try {
                t = TransformerFactory.newInstance().newTransformer();
                try {
                    t.transform(s, r);
                } catch (TransformerException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } catch (TransformerConfigurationException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (TransformerFactoryConfigurationError e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }


        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}