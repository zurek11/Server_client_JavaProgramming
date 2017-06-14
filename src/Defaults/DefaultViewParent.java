package Defaults;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Adam Zurek on 3/22/2016.
 *
 * parent class DefaultViewParent
 * which is extended by Views from the project
 */
public class DefaultViewParent extends JFrame implements DefaultViewInterface{

    private JLabel[] labField;
    protected Container con;

    private JTextArea area = new JTextArea("",20,110);

    /**
     * Constructor which changes parent's JFrame
     * constructor (Frame's name)
     *
     */
    protected DefaultViewParent() {
        super("Server - Client simulator");
    }

    //CREATE LABELS

    /**
     * Method createLabel
     * @param text String which represents the label
     * @param x integer x coordinate
     * @param y integer y coordinate
     * @param con Container
     * @param gbc GridBagConstraints
     * @return JLabel label
     */
    protected JLabel createLabel(String text, int x, int y, Container con, GridBagConstraints gbc){
        JLabel label = new JLabel(text);
        gbc.gridx = x;
        gbc.gridy = y;
        con.add(label,gbc);
        return label;
    }

    /**
     * Overloaded method createLabel for JDialog
     * @param text String which represents the label
     * @param x integer x coordinate
     * @param y integer y coordinate
     * @param pan JPanel
     * @param gbc GridBagContraints
     * @param gbl GridBagLayout
     * @return JLabel label
     */
    protected JLabel createLabel(String text, int x, int y,JPanel pan,GridBagConstraints gbc,GridBagLayout gbl){
        JLabel label = new JLabel(text);
        gbc.gridx=x;
        gbc.gridy=y;
        gbl.setConstraints(label,gbc);
        pan.add(label,gbc);
        return label;
    }

    //CREATE TEXFIELDS

    /**
     * Method createTexField
     * @param length integer represents length of TexField
     * @param x integer x coordinate
     * @param y integer y coordinate
     * @param con Container
     * @param gbc GridBagConstraints
     * @return JTextField textField
     */
    protected JTextField createTexField(int length, int x, int y,Container con,GridBagConstraints gbc){
        JTextField textField = new JTextField(length);
        gbc.gridx=x;
        gbc.gridy=y;
        con.add(textField,gbc);
        return textField;
    }

    /**
     * Overloaded method createTexField for JDialog
     * @param length integer represents length of TexField
     * @param x integer x coordinate
     * @param y integer y coordinate
     * @param pan JPanel
     * @param gbc GridBagConstrains
     * @param gbl GridBagLayout
     * @return JTextField textField
     */
    protected JTextField createTexField(int length, int x, int y,JPanel pan,GridBagConstraints gbc,GridBagLayout gbl){
        JTextField textField = new JTextField(length);
        gbc.gridx=x;
        gbc.gridy=y;
        gbl.setConstraints(textField,gbc);
        pan.add(textField,gbc);
        return textField;
    }

    //CREATE PASSFIELD

    /**
     * Method createPassField
     * @param length integer represents length of PassField
     * @param x integer x coordinate
     * @param y integer y coordinate
     * @param con Container
     * @param gbc GridBagConstraints
     * @return JPasswordField passField
     */
    protected JPasswordField createPassField(int length,int x,int y,Container con,GridBagConstraints gbc){
        JPasswordField passField=new JPasswordField(length);
        gbc.gridx=x;
        gbc.gridy=y;
        con.add(passField,gbc);
        return passField;
    }

    //CREATE BUTTONS

    /**
     * Method createButton
     * @param text String with name of the button
     * @param x integer x coordinate
     * @param y integer y coordinate
     * @param con Container
     * @param gbc GridBagConstraints
     * @return JButton button
     */
    protected JButton createButton(String text,int x,int y,Container con,GridBagConstraints gbc){
        JButton button=new JButton(text);
        gbc.gridx = x;
        gbc.gridy = y;
        con.add(button,gbc);
        return button;
    }

    /**
     * Overloaded method createButton for JDialog
     * @param text String with name of the button
     * @param x integer x coordinate
     * @param y integer y coordinate
     * @param pan JPanel
     * @param gbc GridBagConstraints
     * @param gbl GridBagLayout
     * @return JButton button
     */
    protected JButton createButton(String text, int x, int y,JPanel pan,GridBagConstraints gbc,GridBagLayout gbl){
        JButton button = new JButton(text);
        gbc.gridx=x;
        gbc.gridy=y;
        gbl.setConstraints(button,gbc);
        pan.add(button,gbc);
        return button;
    }

    //CREATE CHECKBOXES

    /**
     * Method createCheckBox
     * @param text String with name of the JCheckBox
     * @param x integer x coordinate
     * @param y integer y coordinate
     * @param con Container
     * @param gbc GridBagConstraints
     * @return JCheckBox box
     */
    protected JCheckBox createCheckBox(String text,int x,int y,Container con,GridBagConstraints gbc){
        JCheckBox box=new JCheckBox(text);
        gbc.gridx=x;
        gbc.gridy=y;
        con.add(box,gbc);
        return box;
    }

    //CREATE COMBOBOXES


    /**
     * Method createComboBox
     * @param text array of Strings with a name of individual box
     * @param x integer x coordinate
     * @param y integer y coordinate
     * @param pan JPanel
     * @param gbc GridBagConstraints
     * @param gbl GridBagLayout
     * @return JComboBox box
     */
    protected JComboBox<String> createComboBox(String[] text, int x, int y,JPanel pan,GridBagConstraints gbc,GridBagLayout gbl){
        JComboBox<String> box= new JComboBox<>(text);
        gbc.gridx=x;
        gbc.gridy=y;
        gbl.setConstraints(box,gbc);
        pan.add(box,gbc);
        return box;
    }

    /**
     * Overloaded method createComboBox for JDialog
     * @param text array of Strings with a name of individual box
     * @param x integer x coordinate
     * @param y integer y coordinate
     * @param con Container
     * @param gbc GridBagConstraints
     * @return JComboBox box
     */
    protected JComboBox<String> createComboBox(String[] text, int x, int y, Container con, GridBagConstraints gbc){
        JComboBox<String> box=new JComboBox<>(text);
        gbc.gridx=x;
        gbc.gridy=y;
        con.add(box,gbc);
        return box;
    }

    //SETTERS

    /**
     * Method sets the basic frame
     * @param con Container
     */
    protected void setFrame(Container con) {
        con.setBackground(new Color(210, 244, 255));
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }

    /**
     * Overridden Method setLabels
     * sets JLabel in view
     */
    @Override
    public void setLabels() {}

    /**
     * Overridden Method setTextFields
     * sets JTextField in view
     */
    @Override
    public void setTextFields() {}

    /**
     * Overridden Method setButtons
     * sets JButton in view
     */
    @Override
    public void setButtons() {}

    /**
     * Overridden Method setCheckBoxes
     * sets JCheckBox in view
     */
    @Override
    public void setCheckBoxes(){}

    /**
     * Overridden Method setAreas
     * sets JArea in view
     */
    @Override
    public void setAreas(){}

    //Getters

    /**
     * Method getCon gets Container
     * @return Container
     */
    public Container getCon() {return this.con;}

    /**
     * Method getLabField gets array of LabField
     * @return array of JLabel
     */
    public JLabel[] getLabField() {return this.labField;}

    /**
     * Getter gets area.
     * @return JTextArea area.
     */
    public JTextArea getArea() {return area;}
}
