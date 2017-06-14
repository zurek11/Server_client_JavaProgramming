package MainMenu;

import CLIENT.CLIENT;
import SERVER.SERVER;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by zurek on 20.10.2016.
 * hahaha
 */
public class MainMenu{
    private MainMenuView mainView;

    /**
     * Constructor RegMenuControl.
     * Create objects model, view and calls method init.
     */
    public MainMenu(){
        mainView=new MainMenuView();
        init();
    }
    private void init(){
        mainView.getButServer().addActionListener(serverListener);
        mainView.getButClient().addActionListener(clientListener);
    }


    /**
     * Listener to Server button
     */
    private ActionListener serverListener = new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent e) {
            mainView.dispose();
            new SERVER();
        }
    };

    /**
     * Listener to Client button
     */
    private ActionListener clientListener = new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent e) {
            mainView.dispose();
            new CLIENT();
        }
    };
}
