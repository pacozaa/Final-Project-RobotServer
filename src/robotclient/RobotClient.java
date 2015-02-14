
package robotclient;

import gnu.io.UnsupportedCommOperationException;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;


class ConnectToHost implements Runnable{
    RobotClient t = new RobotClient();
    public void run(){
        try {
            t.DebugLog("Start: " + this.toString());
            t.ConnectToHost();
        } catch (IOException ex) {
            
        } catch (InterruptedException ex) {
            System.out.println(ex);
        }
    }
}

class RecieveStream implements Runnable{
    RobotClient t = new RobotClient();
    public void run(){
        try {
            t.DebugLog("Start: " + this.toString());
            t.RecieveStream();
        } catch (InterruptedException ex) {
            System.out.println(ex);
        } catch (IOException ex) {
            System.out.println(ex);
        }
        
    }
}

class SendStream implements Runnable{
    RobotClient t = new RobotClient();
    public void run(){
        try {
            t.DebugLog("Start: " + this.toString());
            t.SendStream();
        } catch (InterruptedException ex) {
            System.out.println(ex);
        }
    }
}

class SerialRead implements Runnable{
    RobotClient t = new RobotClient();
    public void run(){
        try {
            t.DebugLog("Start: " + this.toString());
            t.SerialRead();
        } catch (InterruptedException ex) {
            System.out.println(ex);
        }
    }
}

class SerialSend implements Runnable{
    RobotClient t = new RobotClient();
    public void run(){
        try {
            t.DebugLog("Start: " + this.toString());
            t.SerialSend();
        } catch (InterruptedException ex) {
            System.out.println(ex);
        }
    }
}

class SetUpDataToHost implements Runnable{
    RobotClient t = new RobotClient();
    public void run(){
        try {
            t.DebugLog("Start: " + this.toString());
            t.SetUpDataToHost();
        } catch (InterruptedException ex) {
            System.out.println(ex);
        }
    }
}

class StartSerialConnect implements Runnable{
    RobotClient t = new RobotClient();
    public void run(){
        try {
            t.DebugLog("Start: " + this.toString());
            t.StartSerial();
        } catch (InterruptedException ex) {
            System.out.println(ex);
        } catch (IOException ex) {
            System.out.println(ex);
        } catch (UnsupportedCommOperationException ex) {
            System.out.println(ex);
        }
    }
}

public class RobotClient {
    Date dNow = new Date( );
    SimpleDateFormat ft = new SimpleDateFormat ("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
    public static Socket echosocket = null;
    public static PrintWriter outtcp = null;
    public static BufferedReader intcp = null;
    public static GuiRobotNode winframe = null;
    public static SerialPortRW serialPort =null;
    public static int LWheelClient = 0;
    public static int RWheelClient = 0;
    public static String SerialInputLine;
    public static String TCPConnectStatus;
    public static String SerialConnectStatus;
    public static String TCPInputLine;
    public static String DataToHost;
    public static byte counterMain=0;
    public static byte serialFire=0;
    
    public static void main(String[] args) throws IOException, UnsupportedCommOperationException{
    winframe = new GuiRobotNode();
    Thread StartSerialThread = new Thread(new StartSerialConnect());
    Thread ConnectHostThread = new Thread(new ConnectToHost());
    Thread RecieveThread = new Thread(new RecieveStream());
    Thread SendStreamThread = new Thread(new SendStream());
    Thread SerialReadThread = new Thread(new SerialRead());
    Thread SerialSendThread = new Thread(new SerialSend());
    Thread SetUpDataToHostThread = new Thread(new SetUpDataToHost());
    StartSerialThread.start();
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
        String NewMessage = ft.format(dNow)+" : "+message;
        winframe.displayMessage(NewMessage);
        System.out.println(NewMessage);
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
        RobotClient.TCPConnectStatus = "OK";
        winframe.lblStatusTCP.setForeground(Color.green);
        winframe.lblStatusTCP.setText("TCP Status : OK");
        
    }

    public void RecieveStream() throws InterruptedException, IOException {
        while(!"OK".equals(RobotClient.TCPConnectStatus)){
        
            Thread.sleep(1);
        }
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
       while(!"OK".equals(RobotClient.TCPConnectStatus)){
           Thread.sleep(1);
       }
       while(true){
           while(DataToHost != null){
               outtcp.println(DataToHost);
               outtcp.flush();
               this.DebugLog("SendStream : "+DataToHost);
               DataToHost = null;//---Mark
           }
           Thread.sleep(1);
       }
    }
    
    public void StartSerial() throws InterruptedException, IOException, UnsupportedCommOperationException{
       while(GuiRobotNode.CheckStartSerialBtn == false ){
            Thread.sleep(1);
        }
       serialPort = new SerialPortRW();
       serialPort.initialize();
       this.DebugLog("Start Serial");
       while(SerialPortRW.SerialStatus == null){
           Thread.sleep(100);
       }
       winframe.lblStatusSerial.setForeground(Color.GREEN);
       winframe.lblStatusSerial.setText(SerialPortRW.SerialStatus);
       
    }
    
    public void SerialRead() throws InterruptedException {
        while(GuiRobotNode.CheckStartSerialBtn == false || !"OK".equals(SerialPortRW.SerialStatus)){
            Thread.sleep(1);
        }
        while(true){
            while(SerialPortRW.readSerial != null && SerialPortRW.counter>this.counterMain && CheckConvertInteger(SerialPortRW.readSerial) == true){
                RobotClient.SerialInputLine = SerialPortRW.readSerial;
                this.DebugLog("SerialRead : "+RobotClient.SerialInputLine);
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
        while(GuiRobotNode.CheckStartSerialBtn == false || !"OK".equals(SerialPortRW.SerialStatus)){ 
            Thread.sleep(1);
        }
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
        return String.format("%x", new BigInteger(1, arg.getBytes(/*YOUR_CHARSET?*/)));
    }
    public boolean CheckConvertInteger(String s) {
        try { 
            if(Integer.parseInt(toHex(s)) == 0){
                return false;
            } 
        } catch(NumberFormatException e) { 
            return true;
        }
        // only got here if we didn't return false
    return true;
}
    
}
