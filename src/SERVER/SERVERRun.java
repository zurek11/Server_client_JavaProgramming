package SERVER;

import Defaults.CRC;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Objects;

/**
 * Created by zurek on 23.10.2016.
 * hhh
 */

class SERVERRun extends CRC implements Runnable{

    private SERVERView serverView;
    private boolean bool=true;
    private DatagramSocket serverSocket;
    private String convertedFileType;
    private byte[] wholeSentence;
    private byte[][] sentence=null;
    private int counter=0;
    private boolean checked=false;

    SERVERRun(SERVERView serverView,String port) {
        this.serverView=serverView;
        serverSocket = null;
        try {
            serverSocket = new DatagramSocket(Integer.parseInt(port));
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        while (bool) {
            byte[] receiveData = new byte[65507];  //pole bytov kde sa ulozi poslany paket
            byte[] sendData;                        //pole bytov do ktoreho ulozime spravu ktoru server posle

            ++counter;
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            try {
                serverSocket.receive(receivePacket);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //-----ROZKUSKOVANIE ODOSLANEHO PAKETU----------------------------------------------------------------------
            int convertedLengthOfMessage = BytetoInt(Arrays.copyOfRange(receiveData, 4, 8));

            if (convertedLengthOfMessage > 0) {
                int checksum;
                int convertedChecsum=BytetoInt(Arrays.copyOfRange(receiveData, 0, 4));
                int convertedNumbOfPaket = BytetoInt(Arrays.copyOfRange(receiveData, 8, 12));
                int convertedMaxNumbOfPaket = BytetoInt(Arrays.copyOfRange(receiveData, 12, 16));
                byte[] data=Arrays.copyOfRange(receiveData, 20, convertedLengthOfMessage+20);
                if(sentence==null)sentence=new byte[convertedMaxNumbOfPaket][];

                convertedFileType=new String(Arrays.copyOfRange(receiveData, 16, 20));

                //----------CHECKSUM--------------------------------------------------------
                byte[] byteData=Arrays.copyOfRange(receiveData, 20, receiveData.length);
                checksum=CRC16(byteData,byteData.length);
                //-------------------------------------------------------------------------

                if(!checked && convertedChecsum!=checksum){
                    serverView.getArea().append("SERVER: Wrong checksum! Sending request to send packet again.\n");
                    checked=true;
                }
                if(!checked && counter!=convertedNumbOfPaket){
                    serverView.getArea().append("SERVER: Missing fragment! Sending request to send packet again.\n");
                    checked=true;
                }

                serverView.getArea().append("RECEIVED: "+convertedChecsum+" | " + convertedLengthOfMessage + " | " + convertedNumbOfPaket + " | " + convertedMaxNumbOfPaket +" | "+convertedFileType+ " |\n");
                if (convertedNumbOfPaket < convertedMaxNumbOfPaket) {   //ak stale nedosiel posledny paket pridaj do arraylistu skonvertovane data paketu na string
                    sentence[convertedNumbOfPaket-1]=data;
                    System.out.println(Arrays.toString(sentence[convertedNumbOfPaket-1]));
                }
                if (convertedNumbOfPaket == convertedMaxNumbOfPaket) {  //ak dosiel posledny paket server prida do arraylistu posledny a posle ze sprava bola uspesne prijata a vypise ju
                    sentence[convertedNumbOfPaket-1]=trim(data);
                    System.out.println(Arrays.toString(sentence[convertedNumbOfPaket-1]));

                    counter=0;
                    InetAddress IPAddress = receivePacket.getAddress();
                    int port = receivePacket.getPort();
                    if(checked) {sendData = "0000".getBytes();}
                    else{
                        sendData = "SERVER: message was successfully sent.".getBytes();
                        if(!Objects.equals(convertedFileType, "0000"))serverView.getButSave().setEnabled(true);
                    }
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                    try {
                        serverSocket.send(sendPacket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if(!checked){wholeSentence=copySmallArraysToBigArray(sentence);}
                    if(Objects.equals(convertedFileType,"0000"))serverView.getArea().append("RECEIVED TOTAL: "+new String(wholeSentence)+"\n");
                    checked=false;
                    sentence=null;
                }
            }
            else {
                serverView.getArea().append("CLIENT: pinging with 4 bytes...\n");

                InetAddress IPAddress = receivePacket.getAddress();
                int port = receivePacket.getPort();
                sendData = "SERVER: pinging with 4 bytes...".getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                try {
                    serverSocket.send(sendPacket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static int BytetoInt(byte[] bytes) {    //metoda konvertujuca pole bytov na premennu typu int
        int number = 0;

        for (int i=0; i<4 && i<bytes.length; i++) {
            number <<= 8;
            number |= (int)bytes[i] & 0xFF;
        }
        return number;
    }

    private static byte[] copySmallArraysToBigArray(byte[][] smallArrays){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
        for (byte[] smallArray : smallArrays) {
            try {outputStream.write(smallArray);}
            catch (IOException e) {e.printStackTrace();}
        }
        return outputStream.toByteArray();
    }

    private static byte[] trim(byte[] bytes) {
        int i = bytes.length - 1;
        while (i >= 0 && bytes[i] == 0) {
            --i;
        }
        return Arrays.copyOf(bytes, i + 1);
    }

    void setBool(boolean bool) {this.bool = bool;}

    boolean isBool() {
        return bool;
    }

    byte[] getWholeSentence() {return wholeSentence;}

    String getConvertedFileType() {return convertedFileType;}

    DatagramSocket getServerSocket() {
        return serverSocket;
    }
}
