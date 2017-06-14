package SERVER;

import MainMenu.MainMenu;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class SERVER{

    private Thread thread;
    private SERVERView serverView;
    private SERVERRun serverRun;

    public SERVER(){
        System.out.println("Starting Server...");
        serverView=new SERVERView();
        init();
    }

    //----METHODS-------------------------------------------------------------------------------------------------------

    private void init(){
        serverView.getButSave().setEnabled(false);
        serverView.getButBack().addActionListener(backListener);
        serverView.getButSave().addActionListener(saveListener);
        serverView.getButStart().addActionListener(startListener);
    }

    /**
     * Listener to Back button
     */
    private ActionListener backListener = new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent e) {
            serverView.dispose();
            new MainMenu();
            if(thread!=null){
                serverRun.setBool(false);
                serverRun.getServerSocket().close();
                //noinspection deprecation
                thread.stop();
            }
            System.out.println("Stopping thread...");
        }
    };

    /**
     * Listener to Save button
     */
    private ActionListener saveListener = new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent e) {
            serverView.FileChooser();
            int result = serverView.getFileChooser().showSaveDialog(serverView.getFileChooser());
            if (result == JFileChooser.APPROVE_OPTION) {
                File f = serverView.getFileChooser().getSelectedFile();
                FileOutputStream fos = null;
                serverView.getArea().append("SERVER: Saving file...\n");
                try {
                    fos = new FileOutputStream(f.getAbsolutePath()+"."+serverRun.getConvertedFileType().trim());
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }
                try {
                    if (fos != null) {fos.write(serverRun.getWholeSentence());}
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                try {
                    if (fos != null) {fos.close();}
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                serverView.getArea().append("SERVER: File has been successfully sent.\n");
            }
        }
    };

    /**
     * Listener to Start button
     */
    private ActionListener startListener = new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent e) {
            String port = serverView.getTexField().getText();
            if(!Objects.equals(port, "")){
                if (thread == null) {
                    serverView.getArea().append("SERVER: Start to waiting on port "+Integer.parseInt(port)+"...\n");
                    thread = new Thread(serverRun=new SERVERRun(serverView, port));
                    thread.start();
                }
                else{
                    if(serverRun.isBool()){
                        serverView.getArea().append("SERVER: Stop waiting...\n");
                        serverRun.setBool(false);
                        serverRun.getServerSocket().close();
                        serverView.getButSave().setEnabled(false);
                        //noinspection deprecation
                        thread.stop();
                    }
                    else{
                        serverView.getArea().append("SERVER: Start to waiting on port "+Integer.parseInt(port)+"...\n");
                        serverRun.setBool(true);
                        thread = new Thread(serverRun=new SERVERRun(serverView, port));
                        thread.start();
                    }
                }
            }
            else{
                serverView.getArea().append("SERVER: Wrong port format.\n");
            }
        }
    };

    //------------------------------------------------------------------------------------------------------------------
}