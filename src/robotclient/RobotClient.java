
package robotclient;

import gnu.io.UnsupportedCommOperationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


class ConnectToHost implements Runnable{
    RobotClient t = new RobotClient();
    public void run(){
        try {
            t.ConnectToHost();
        } catch (IOException ex) {
            
        }
    }
}

class RecieveStream implements Runnable{
    RobotClient t = new RobotClient();
    public void run(){
        t.RecieveStream();
        
    }
}

class SendStream implements Runnable{
    RobotClient t = new RobotClient();
    public void run(){
        t.SendStream();
    }
}

class SerialRead implements Runnable{
    RobotClient t = new RobotClient();
    public void run(){
        t.SerialRead();
    }
}

class SerialSend implements Runnable{
    RobotClient t = new RobotClient();
    public void run(){
        t.SerialSend();
    }
}



public class RobotClient {
    public static Socket echosocket = null;
    public static PrintWriter outtcp = null;
    public static BufferedReader intcp = null;
    public static GuiRobotNode winframe = null;
    public static SerialPortRW serialPort =null;
    public static int LWheelClient = 0;
    public static int RWheelClient = 0;
    public static String SerialInputLine;
    public static String ConnectStatus;
    
    public static void main(String[] args) throws IOException, UnsupportedCommOperationException{
    serialPort = new SerialPortRW();
    winframe = new GuiRobotNode();
    serialPort.initialize();
    Thread ConnectHostThread = new Thread(new ConnectToHost());
    Thread RecieveThread = new Thread(new RecieveStream());
    Thread SendStreamThread = new Thread(new SendStream());
    Thread SerialReadThread = new Thread(new SerialRead());
    Thread SerialSendThread = new Thread(new SerialSend());
    ConnectHostThread.start();
    RecieveThread.start();
    SendStreamThread.start();
    SerialReadThread.start();
    SerialSendThread.start();
    java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                winframe.setVisible(true);
            }
        });
    }

    public void DebugLog(String message) {
        winframe.displayMessage(message);
    }

    public void ConnectToHost() throws IOException {
        while(GuiRobotNode.CheckConnectBtn == false){}
        echosocket = new Socket(GuiRobotNode.serverHost,10007);
        outtcp = new PrintWriter(echosocket.getOutputStream(), true);
        intcp = new BufferedReader(new InputStreamReader(echosocket.getInputStream()));
        this.DebugLog("Connected to Host");
        RobotClient.ConnectStatus = "OK";
    }

    public void RecieveStream() {
        while(!"OK".equals(RobotClient.ConnectStatus)){
           
       }
    }

    public void SendStream() {
       while(!"OK".equals(RobotClient.ConnectStatus)){
           
       }
    }

    public void SerialRead() {
        while(GuiRobotNode.CheckStartSerialBtn == false || !"OK".equals(SerialPortRW.SerialStatus)){
            
        }
    }

    public void SerialSend() {
        while(GuiRobotNode.CheckStartSerialBtn == false || !"OK".equals(SerialPortRW.SerialStatus)){
            
        }
    }
    
    
    
}
