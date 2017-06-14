package SERVER;

import Defaults.DefaultViewParent;
import Defaults.HintTextFieldUI;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * Created by zurek on 22.10.2016.
 * ggg
 */
class SERVERView extends DefaultViewParent{

    private JLabel writing;
    private JTextField texField;
    private JButton butBack, butSave, butStart;
    private JTextArea area;
    private JFileChooser fileChooser;

    private GridBagConstraints gbc;

    SERVERView() {
        con = getContentPane();
        gbc = new GridBagConstraints();
        fileChooser = new JFileChooser();

        writing=new JLabel();
        texField = new JTextField();

        setLayout(new GridBagLayout());
        setLabels();
        setTextFields();
        setAreas();
        setButtons();

        setFrame(con);
    }

    /**
     * Implemented method setLabels sets parameters.
     */
    @Override
    public void setLabels() {
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(20, 10, 0, 0);
        writing=createLabel("SERVER",0,0,con,gbc);
        writing.setForeground(new Color(197, 32, 25));
        writing.setFont(new Font("Calibri", Font.BOLD, 36));
        gbc.insets = new Insets(10, 10, 10, 10);
    }

    /**
     * Implemented method setTexFields sets parameters.
     */
    @Override
    public void setTextFields() {
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(20, 10, 10, 30);
        texField = createTexField(10,1,0,con,gbc);
        texField.setUI(new HintTextFieldUI("Port...", true));
        gbc.anchor = GridBagConstraints.WEST;
    }

    /**
     * Implemented method setAreas sets parameters.
     */
    @Override
    public void setAreas(){
        gbc.insets=new Insets(10,10,10,10);
        gbc.fill=GridBagConstraints.VERTICAL;
        gbc.gridx=0;
        gbc.gridy=2;
        gbc.weightx=5;
        gbc.weighty=1;
        gbc.gridwidth=3;
        area = new JTextArea("",20,80);
        area.setBackground(new Color(0xFFFFFF));
        area.setFont(new Font("Consolas", Font.PLAIN, 12));
        area.setEditable(false);
        area.setLineWrap(true);
        JScrollPane pane=new JScrollPane(area, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        con.add(pane,gbc);
        gbc.gridwidth=1;
    }

    /**
     * Implemented method setButtons sets parameters.
     */
    @Override
    public void setButtons() {
        gbc.insets = new Insets(10, 10, 10, 30);
        gbc.anchor=GridBagConstraints.EAST;
        butStart=createButton("Start server",2,0,con,gbc);
        gbc.anchor=GridBagConstraints.WEST;
        butBack=createButton("Back",0,3,con,gbc);
        gbc.anchor=GridBagConstraints.EAST;
        butSave=createButton("Save",2,3,con,gbc);
    }

    void FileChooser() {
        JPanel panFile = new JPanel();
        panFile.add(fileChooser);
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
    }

    //----------- GETTERS --------------------------------------


    JTextField getTexField() {
        return texField;
    }

    JButton getButBack() {
        return butBack;
    }

    JButton getButSave() {
        return butSave;
    }

    JButton getButStart() {
        return butStart;
    }

    @Override
    public JTextArea getArea() {
        return area;
    }

    JFileChooser getFileChooser() {
        return fileChooser;
    }
}
