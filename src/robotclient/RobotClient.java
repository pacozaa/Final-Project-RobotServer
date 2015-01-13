
package robotclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


class ConnectToHostThread implements Runnable{
    RobotClient t = new RobotClient();
    public void run(){
        try {
            t.ConnectToHost();
        } catch (IOException ex) {
            
        }
    }
}

class RecieveStreamThread implements Runnable{
    RobotClient t = new RobotClient();
    public void run(){
        
    }
}

class SendStreamThread implements Runnable{
    RobotClient t = new RobotClient();
    public void run(){
        
    }
}

class SerialReadThread implements Runnable{
    RobotClient t = new RobotClient();
    public void run(){
        
    }
}

class SerialSendThread implements Runnable{
    RobotClient t = new RobotClient();
    public void run(){
        
    }
}



public class RobotClient {
    public static Socket echosocket = null;
    public static PrintWriter outtcp = null;
    public static BufferedReader intcp = null;
    public static GuiRobotNode winframe = null;
    
    
    public static void main(String[] args) throws IOException{
    
    winframe = new GuiRobotNode();
    Thread ConnectHost = new Thread(new ConnectToHostThread());
    Thread RecieveThread = new Thread(new RecieveStreamThread());
    Thread SendStreamThread = new Thread(new SendStreamThread());
    Thread SerialReadThread = new Thread(new SerialReadThread());
    Thread SerialSendThread = new Thread(new SerialSendThread());
    ConnectHost.start();
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
    }
    
    
    
}
