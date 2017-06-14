package CLIENT;

import Defaults.DefaultViewParent;
import Defaults.HintTextFieldUI;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * Created by zurek on 20.10.2016.
 * hhh
 */
public class CLIENTView extends DefaultViewParent {

    private JLabel writing;
    private JTextField[] texField;
    private JButton butSend, butBack, butChooseFile,butPing;
    private JTextArea area;
    private JFileChooser fileChooser;
    private JCheckBox checkBox;

    private GridBagConstraints gbc;

    public CLIENTView() {
        con = getContentPane();
        gbc = new GridBagConstraints();
        fileChooser = new JFileChooser();
        checkBox=new JCheckBox();

        writing=new JLabel();
        texField = new JTextField[4];

        setLayout(new GridBagLayout());
        setLabels();
        setTextFields();
        setButtons();
        setCheckBox();
        setAreas();

        setFrame(con);
    }

    /**
     * Implemented method setLabels sets parameters.
     */
    @Override
    public void setLabels() {
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(20, 10, 0, 0);
        writing=createLabel("CLIENT",0,0,con,gbc);
        writing.setForeground(new Color(197, 32, 25));
        writing.setFont(new Font("Calibri", Font.BOLD, 36));
        gbc.insets = new Insets(10, 10, 10, 10);
    }

    /**
     * Implemented method setTexFields sets parameters.
     */
    @Override
    public void setTextFields() {
        gbc.insets = new Insets(20, 30, 10, 10);
        texField[0] = createTexField(10,1,0,con,gbc);
        texField[0].setUI(new HintTextFieldUI("Port...", true));
        texField[1] = createTexField(10,2,0,con,gbc);
        texField[1].setUI(new HintTextFieldUI("IP adress...", true));
        texField[2] = createTexField(10,3,0,con,gbc);
        texField[2].setUI(new HintTextFieldUI("Packet length...", true));
        gbc.gridwidth=4;
        gbc.insets = new Insets(10, 10, 10, 10);
        texField[3] = createTexField(50,0,1,con,gbc);
        texField[3].setUI(new HintTextFieldUI("Send message...", true));
        gbc.gridwidth=1;
    }

    /**
     * Implemented method setButtons sets parameters.
     */
    @Override
    public void setButtons() {
        gbc.insets = new Insets(10, 10, 10, 30);
        gbc.anchor=GridBagConstraints.EAST;
        butChooseFile=createButton("  Choose File  ",3,1,con,gbc);
        butBack=createButton("Back",0,3,con,gbc);
        butSend=createButton("Send",3,3,con,gbc);
        butPing=createButton("Ping",2,3,con,gbc);
        gbc.anchor=GridBagConstraints.WEST;
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
        gbc.gridwidth=4;
        area = new JTextArea("",20,100);
        area.setBackground(new Color(0xFFFFFF));
        area.setFont(new Font("Consolas", Font.PLAIN, 12));
        area.setEditable(false);
        area.setLineWrap(true);
        JScrollPane pane=new JScrollPane(area, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        con.add(pane,gbc);
        gbc.gridwidth=1;
    }

    void FileChooser() {
        JPanel panFile = new JPanel();
        panFile.add(fileChooser);
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
    }

    private void setCheckBox(){
        gbc.anchor=GridBagConstraints.EAST;
        checkBox=createCheckBox("Corrupted fragment",3,0,con,gbc);
        checkBox.setSelected(false);
        checkBox.setBackground(new Color(210, 244, 255));
    }

    JButton getButSend() {
        return butSend;
    }

    JButton getButBack() {
        return butBack;
    }

    JButton getButPing() {
        return butPing;
    }

    JButton getButChooseFile() {return butChooseFile;}

    JTextField[] getTexField() {
        return texField;
    }

    @Override
    public JTextArea getArea() {return area;}

    JCheckBox getCheckBox() {return checkBox;}

    JFileChooser getFileChooser() {return fileChooser;}
}
