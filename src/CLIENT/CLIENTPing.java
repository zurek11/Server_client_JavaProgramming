package CLIENT;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.Objects;

/**
 * Created by zurek on 22.10.2016.
 * hhh
 */

class CLIENTPing implements Runnable {

    private CLIENTView clientView;
    private String port;
    private String serverIP;

    CLIENTPing(CLIENTView clientView) {
        this.clientView=clientView;
        port=clientView.getTexField()[0].getText();
        serverIP=clientView.getTexField()[1].getText();
    }


    @Override
    public void run() {
        while(true){
            if(Objects.equals(port, "")) {
                clientView.getArea().append("CLIENT: Unkown port...\n");
                clientView.getArea().append("CLIENT: Stopping pinging...\n");
                break;
            }
            if(Objects.equals(serverIP, "")){
                clientView.getArea().append("CLIENT: Unkown IP adress...\n");
                clientView.getArea().append("CLIENT: Stopping pinging...\n");
                break;
            }
            InetAddress IPAddress;
            byte[] byteFragment = ByteBuffer.allocate(4).putInt(0).array();
            byte[] receiveData = new byte[100];

            try {IPAddress = InetAddress.getByName(serverIP);}
            catch (UnknownHostException e1) {
                clientView.getArea().append("CLIENT: Unknown server.\n");
                clientView.getArea().append("CLIENT: Stopping pinging...\n");
                break;
            }
            DatagramSocket clientSocket;
            try {
                clientSocket =new DatagramSocket();
            }
            catch (SocketException e) {
                System.out.println("Nepodarilo sa vytvorit datagramSocket");
                break;
            }
            DatagramPacket sendPacket = new DatagramPacket(byteFragment, byteFragment.length, IPAddress, Integer.parseInt(port));
            try {
                clientSocket.send(sendPacket);
                clientSocket.setSoTimeout(5000);
            }
            catch (SocketTimeoutException e){
                clientView.getArea().append("CLIENT: Unknown server.\n");
                clientView.getArea().append("CLIENT: Stopping pinging...\n");
                break;
            }
            catch (IOException e) {
                System.out.println("Neposlalo..");
                break;
            }

            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            try {
                clientSocket.receive(receivePacket);
                clientSocket.setSoTimeout(5000);
            }
            catch (SocketTimeoutException e){
                clientView.getArea().append("CLIENT: Server is not answering.\n");
                clientView.getArea().append("CLIENT: Stopping pinging...\n");
                break;
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            String modifiedSentence = new String(receivePacket.getData());
            clientView.getArea().append(modifiedSentence);

            try {Thread.sleep(2000);}
            catch (InterruptedException e) {e.printStackTrace();}
        }
    }
}
