package MainMenu;

import Defaults.DefaultViewParent;

import javax.swing.*;
import java.awt.*;

/**
 * Created by zurek on 20.10.2016.
 * view
 */

class MainMenuView extends DefaultViewParent {
    private JButton butServer, butClient;

    private GridBagConstraints gbc;

    MainMenuView(){
        con = getContentPane();
        gbc = new GridBagConstraints();
        setLayout(new GridBagLayout());
        setLabels();
        setButtons();
        setFrame(con);
    }


    /**
     * Implemented method setLabels sets parameters.
     */
    public void setLabels(){
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor=GridBagConstraints.CENTER;
        JLabel labHeader = createLabel("Server - Client Simulator", 0, 0, con, gbc);
        labHeader.setForeground(new Color(197, 32, 25));
        labHeader.setFont(new Font("Calibri", Font.BOLD, 24));
        gbc.gridwidth = 1;
    }

    /**
     * Implemented method setButtons sets parameters.
     */
    public void setButtons(){
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        butServer=createButton("SERVER",0,2,con,gbc);

        gbc.anchor = GridBagConstraints.CENTER;
        butClient=createButton("CLIENT",0,3,con,gbc);
    }

    /**
     * Getter gets JButton
     * @return JButton butServer
     */
    JButton getButServer() {
        return butServer;
    }

    /**
     * Getter gets JButton
     * @return JButton butClient
     */
    JButton getButClient() {
        return butClient;
    }
}


