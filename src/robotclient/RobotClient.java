
package robotclient;

import gnu.io.UnsupportedCommOperationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;


class ConnectToHost implements Runnable{
    RobotClient t = new RobotClient();
    public void run(){
        try {
            t.ConnectToHost();
        } catch (IOException ex) {
            
        } catch (InterruptedException ex) {
            Logger.getLogger(ConnectToHost.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

class RecieveStream implements Runnable{
    RobotClient t = new RobotClient();
    public void run(){
        try {
            t.RecieveStream();
        } catch (InterruptedException ex) {
            Logger.getLogger(RecieveStream.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RecieveStream.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}

class SendStream implements Runnable{
    RobotClient t = new RobotClient();
    public void run(){
        try {
            t.SendStream();
        } catch (InterruptedException ex) {
            Logger.getLogger(SendStream.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

class SerialRead implements Runnable{
    RobotClient t = new RobotClient();
    public void run(){
        try {
            t.SerialRead();
        } catch (InterruptedException ex) {
            Logger.getLogger(SerialRead.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

class SerialSend implements Runnable{
    RobotClient t = new RobotClient();
    public void run(){
        try {
            t.SerialSend();
        } catch (InterruptedException ex) {
            Logger.getLogger(SerialSend.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

class SetUpDataToHost implements Runnable{
    RobotClient t = new RobotClient();
    public void run(){
        try {
            t.SetUpDataToHost();
        } catch (InterruptedException ex) {
            Logger.getLogger(SetUpDataToHost.class.getName()).log(Level.SEVERE, null, ex);
        }
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
    public static String TCPInputLine;
    public static String DataToHost;
    public static byte counterMain=0;
    public static byte serialFire=0;
    
    public static void main(String[] args) throws IOException, UnsupportedCommOperationException{
    serialPort = new SerialPortRW();
    winframe = new GuiRobotNode();
    serialPort.initialize();
    Thread ConnectHostThread = new Thread(new ConnectToHost());
    Thread RecieveThread = new Thread(new RecieveStream());
    Thread SendStreamThread = new Thread(new SendStream());
    Thread SerialReadThread = new Thread(new SerialRead());
    Thread SerialSendThread = new Thread(new SerialSend());
    Thread SetUpDataToHostThread = new Thread(new SetUpDataToHost());
    ConnectHostThread.start();
    RecieveThread.start();
    SendStreamThread.start();
    SerialReadThread.start();
    SerialSendThread.start();
    SetUpDataToHostThread.start();
    java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                winframe.setVisible(true);
            }
        });
    }

    public void DebugLog(String message) {
        winframe.displayMessage(message);
    }

    public void ConnectToHost() throws IOException, InterruptedException {
        while(winframe.ipText.getText().length()< 10){
            Thread.sleep(1);
        }
        while(GuiRobotNode.CheckConnectBtn == false){
            winframe.ConnectBtn.setEnabled(true);
        }
        
        echosocket = new Socket(GuiRobotNode.serverHost,10007);
        outtcp = new PrintWriter(echosocket.getOutputStream(), true);
        intcp = new BufferedReader(new InputStreamReader(echosocket.getInputStream()));
        this.DebugLog("Connected to Host");
        RobotClient.ConnectStatus = "OK";
    }

    public void RecieveStream() throws InterruptedException, IOException {
        this.DebugLog("Ready to Recieve");
        while(!"OK".equals(RobotClient.ConnectStatus)){
        
            Thread.sleep(1);
        }
        this.DebugLog("Ready to TCP Chat");
        while(true){
            while((TCPInputLine = intcp.readLine()) != null) {
                this.DebugLog("Recieve Stream TCP : "+TCPInputLine);
                this.serialFire=1;
                Thread.sleep(1);
            }
            Thread.sleep(1);
        }
    }

    public void SendStream() throws InterruptedException {
       while(!"OK".equals(RobotClient.ConnectStatus)){
           Thread.sleep(1);
       }
       while(true){
           while(DataToHost != null){
               outtcp.println(DataToHost);
               this.DebugLog("SendStream : "+DataToHost);
               DataToHost = null;//---Mark
           }
           Thread.sleep(1);
       }
    }

    public void SerialRead() throws InterruptedException {
        this.DebugLog("SerialRead is Start");
        while(GuiRobotNode.CheckStartSerialBtn == false || !"OK".equals(SerialPortRW.SerialStatus)){
            Thread.sleep(1);
        }
        this.DebugLog("SerialRead is Start");
        while(true){
            while(SerialPortRW.readSerial != null && SerialPortRW.counter>this.counterMain && SerialPortRW.readSerial != "\n" && SerialPortRW.readSerial != "\r"){
                RobotClient.SerialInputLine = SerialPortRW.readSerial;
                this.DebugLog("SerialRead : "+RobotClient.SerialInputLine + " Main : "+this.counterMain+" Event : "+SerialPortRW.counter);
                this.DebugLog("SerialRead HEX : "+toHex(RobotClient.SerialInputLine));
                this.counterMain= SerialPortRW.counter;
                if(SerialPortRW.counter > 10){
                    SerialPortRW.counter=0;
                    this.counterMain=0;
                }
                Thread.sleep(1);
            }
            Thread.sleep(1);
        }
    }

    public void SerialSend() throws InterruptedException {
        this.DebugLog("Serial Send is Start");
        while(GuiRobotNode.CheckStartSerialBtn == false || !"OK".equals(SerialPortRW.SerialStatus)){ 
            Thread.sleep(1);
        }
        this.DebugLog("Serial Send is Ready");
        while(true){
            while(this.TCPInputLine != null && this.serialFire==1){
                serialPort.serialSend(this.TCPInputLine);
                this.DebugLog("SerialSend : "+this.TCPInputLine+ " Fire : "+this.serialFire);
                this.serialFire=0;
                Thread.sleep(1);
            }
            Thread.sleep(1);
        }
        
    }

    public void SetUpDataToHost() throws InterruptedException {
        while(true){
            Thread.sleep(1);
        }
    }
    
    public String toHex(String arg) {
        return String.format("%040x", new BigInteger(1, arg.getBytes(/*YOUR_CHARSET?*/)));
    }
    
    
}
