package CLIENT;

import Defaults.CRC;
import MainMenu.MainMenu;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static java.lang.Thread.State.TERMINATED;

public class CLIENT extends CRC{

    private Thread thread;
    private CLIENTView clientView;
    private String serverIP;
    private String port;
    private String length;
    private String fileType;
    private byte[] byteToSend;
    private DatagramSocket clientSocket;
    private boolean checked=false;
    private int randomNum;

    public CLIENT() {
        System.out.println("Starting Client...");
        clientView=new CLIENTView();
        init();
    }

    //----METHODS-------------------------------------------------------------------------------------------------------

    private void init(){
        clientSocket=null;
        clientView.getCheckBox().addItemListener(checkListener);
        clientView.getButBack().addActionListener(backListener);
        clientView.getButSend().addActionListener(sendListener);
        clientView.getButPing().addActionListener(pingListener);
        clientView.getButChooseFile().addActionListener(saveListener);
    }

    //---------------------ACTION LISTENERS-----------------------------------------------------------------------------
    /**
     * Listener to Save button
     */
    private ActionListener saveListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            clientView.FileChooser();
            int result = clientView.getFileChooser().showOpenDialog(clientView.getFileChooser());
            if (result == JFileChooser.APPROVE_OPTION) {
                clientView.getArea().append("CLIENT: Start sending file...\n");
                File f = clientView.getFileChooser().getSelectedFile();
                clientView.getArea().append("You chose to open this file: " + f.getName() + " " + f.getAbsolutePath()+"\n");

                FileInputStream fileinputstream = null;  //creat the incoming stream of data from the file
                try {fileinputstream = new FileInputStream(f);}
                catch (FileNotFoundException e1) {e1.printStackTrace();}

                int numberBytes = 0;  // find out how big the byte array needs to be.
                try {numberBytes = fileinputstream.available();}
                catch (IOException e1) {e1.printStackTrace();}

                byteToSend = new byte[numberBytes]; // allocate the byte array.
                try {fileinputstream.read(byteToSend);  // read the file into the byte array.
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                try {
                    fileinputstream.close();  // close the stream
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                //-------------------------------------------------------------
                boolean alive=checkAlive();
                //-------------------------------------------------------------

                port=clientView.getTexField()[0].getText();
                serverIP=clientView.getTexField()[1].getText();
                length=clientView.getTexField()[2].getText();
                fileType=getFileExtension(f);

                if(!Objects.equals(serverIP, "") && !Objects.equals(port, "") && !Objects.equals(length, "")){
                    sendingPaket(byteToSend,fileType);
                }
                else{
                    clientView.getArea().append("CLIENT: Unkown port or IP adress or length of packet\n");
                    clientView.getArea().append("CLIENT: Stopping to send a file...\n");
                }
                //------------------------------------------------------------
                if(alive){
                    clientView.getArea().append("CLIENT: Start to ping...\n");
                    thread=new Thread(new CLIENTPing(clientView));
                    thread.start();
                }
                //-----------------------------------------------------------
            }
        }
    };

    /**
     * Listener to Send button
     */
    private ActionListener sendListener = new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent e) {
            //-------------------------------------------------------------
            boolean alive=checkAlive();
            //-------------------------------------------------------------
            clientView.getArea().append("CLIENT: Start sending message...\n");
            port=clientView.getTexField()[0].getText();
            serverIP=clientView.getTexField()[1].getText();
            length=clientView.getTexField()[2].getText();
            byteToSend = clientView.getTexField()[3].getText().getBytes();             //priradenie spravy na poslanie
            fileType="0000";

            if(!Objects.equals(serverIP, "") && !Objects.equals(port, "") && !Objects.equals(length, "") && !Objects.equals(clientView.getTexField()[3].getText(),"")){
                sendingPaket(byteToSend,fileType);
            }
            else{
                clientView.getArea().append("CLIENT: Empty message or unkown port or IP adress or length of packet\n");
                clientView.getArea().append("CLIENT: Stopping to send a message...\n");
            }
            //------------------------------------------------------------
            if(alive){
                clientView.getArea().append("CLIENT: Start to ping...\n");
                thread=new Thread(new CLIENTPing(clientView));
                thread.start();
            }
            //-----------------------------------------------------------
        }
    };

    private ActionListener pingListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (thread == null || thread.getState().equals(TERMINATED)) {
                clientView.getArea().append("CLIENT: Start to ping...\n");
                thread = new Thread(new CLIENTPing(clientView));
                thread.start();
            }
            else{
                clientView.getArea().append("CLIENT: Stopping pinging...\n");
                //noinspection deprecation
                thread.stop();
            }
        }
    };

    /**
     * Listener to Back button
     */
    private ActionListener backListener = new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent e) {
            if(thread!=null){
                if(thread.isAlive())//noinspection deprecation
                    thread.stop();
            }
            clientView.dispose();
            new MainMenu();
        }
    };

    private ItemListener checkListener = e -> checked = e.getStateChange() == ItemEvent.SELECTED;

    //-------------------METHODS----------------------------------------------------------------------------------------

    private void sendingPaket(byte[] send,String fileType){
        ArrayList<byte[]> subByteToSend;        //array podstringov posielanej spravy

        try {clientSocket = new DatagramSocket();}   //vytvaranie socketu
        catch (SocketException e1) {System.out.println("Error: Can't create new DatagramSocket.");}

        InetAddress IPAddress = null;
        try {
            IPAddress = InetAddress.getByName(serverIP);
        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        }

        int maxLengthOfPacket = Integer.parseInt(length);  //priradenie maximalnej dlzky fragmetu

        if(maxLengthOfPacket<=65487) {

            //--------------------------------------
            subByteToSend = SplittedByte(send, maxLengthOfPacket);   //rozdelenie dat na fragmenty
            //-----------------------------------
            byte[] maxNumbOfPaket = ByteBuffer.allocate(4).putInt(subByteToSend.size()).array();    //pole bytov uchovavajuce pocet posielanych paketov
            byte[] byteFileTYpe = fileType.substring(0, fileType.length()).getBytes();
            byte[] lengthOfMessage = ByteBuffer.allocate(4).putInt(maxLengthOfPacket).array();              //pole bytov uchovavajuce poradie posielanej spravy
            if (checked) randomNum = (int) (Math.random() * (subByteToSend.size()-1));

            for (int i = 0; i < subByteToSend.size(); ++i) {
                //--------CHECSUM---------------------------
                byte[] array = new byte[65487];
                System.arraycopy(subByteToSend.get(i), 0, array, 0, subByteToSend.get(i).length);
                int checksum = CRC16(array, array.length);
                if (checked && randomNum == i) checksum += 1;
                //------------------------------------------
                byte[] checsumByte = ByteBuffer.allocate(4).putInt(checksum).array();
                byte[] numbOfPaket = ByteBuffer.allocate(4).putInt(i + 1).array();  //pole bytov uchovavajuce poradie aktualne posielaneho paketu
                byte[] paketToSend = new byte[20 + subByteToSend.get(i).length];    //vytvorenie pola bytov ktoremu pridame hlavicku

                System.arraycopy(checsumByte, 0, paketToSend, 0, checsumByte.length);
                System.arraycopy(lengthOfMessage, 0, paketToSend, 4, lengthOfMessage.length);       //polu bytov pridame cast hlavicky s ID spravy
                System.arraycopy(numbOfPaket, 0, paketToSend, 8, numbOfPaket.length);  //polu bytov pridame cast hlavicky s poradim posielaneho paketu
                System.arraycopy(maxNumbOfPaket, 0, paketToSend, 12, maxNumbOfPaket.length); //polu bytov pridame cast hlavicky s poctom posielanych paketov
                System.arraycopy(byteFileTYpe, 0, paketToSend, 16, byteFileTYpe.length);
                System.arraycopy(subByteToSend.get(i), 0, paketToSend, 20, subByteToSend.get(i).length); //polu bytov pridame samotne data
                subByteToSend.set(i, paketToSend);  //do arraylistu pridame modifikovany paket s hlavickou
            }
            for (byte[] aSubByteToSend : subByteToSend) {
                DatagramPacket sendPacket = new DatagramPacket(aSubByteToSend, aSubByteToSend.length, IPAddress, Integer.parseInt(clientView.getTexField()[0].getText()));
                try {
                    clientSocket.send(sendPacket);
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    clientSocket.setSoTimeout(5000);
                } catch (SocketTimeoutException e) {
                    clientView.getArea().append("CLIENT: Server is not answering.\n");
                    clientView.getArea().append("CLIENT: Stopping sending a message...\n");
                    break;
                } catch (IOException e) {
                    clientView.getArea().append("CLIENT: WTF?!\n");
                    break;
                }
            }
            receivePaket();
        }
        else{
            clientView.getArea().append("CLIENT: Max size of packet is too big.\n");
            clientView.getArea().append("CLIENT: Stopping to send a message/file...\n");
        }
    }

    private void receivePaket(){
        byte[] receiveData = new byte[1000];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        try {
            assert clientSocket != null;
            clientSocket.receive(receivePacket);
            String modifiedSentence = new String(receivePacket.getData());
            if(Objects.equals(new String(receivePacket.getData()).trim(), "0000")){
                clientView.getArea().append("CLIENT: Wrong packet sending to server again.\n");
                checked=false;
                clientView.getCheckBox().setSelected(false);
                sendingPaket(byteToSend,fileType);
            }
            else{
                clientView.getArea().append(modifiedSentence.trim()+"\n");
                clientSocket.setSoTimeout(5000);
            }
        }
        catch (SocketTimeoutException e){
            clientView.getArea().append("CLIENT: Server is not answering.\n");
            clientView.getArea().append("CLIENT: Stopping sending a message...\n");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static ArrayList<byte[]> SplittedByte(byte[] byteToSplit, int length){  //metoda kuskujuca string na rovnake kusky ktore lozi do arraylistu
        ArrayList<byte[]> subBytes = new ArrayList<>();
        int index = 0;

        while (index < byteToSplit.length) {
            subBytes.add(Arrays.copyOfRange(byteToSplit,index, index + length));
            index +=length;
        }
        return subBytes;
    }

    private boolean checkAlive(){
        boolean alive=false;
        if(thread!=null) {
            if (thread.isAlive()) {
                alive = true;
                //noinspection deprecation
                thread.stop();
                clientView.getArea().append("CLIENT: Stopping pinging...\n");
            }
        }
        return alive;
    }

    private String getFileExtension(File file){
        String extension = "";

        int i = file.getAbsolutePath().lastIndexOf('.');
        if (i >= 0) {
            extension = file.getAbsolutePath().substring(i+1);
        }
        return extension;
    }
    //--------------------------------------------------------------------------------------------------------------
}