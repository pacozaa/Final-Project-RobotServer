package serverui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


class RecieveFromClientThread implements Runnable {
    RobotControl t = new RobotControl();
    public void run() {
        try {
            t.RecieveFromClient();
        } catch (IOException ex) {
            t.DebugLog(ex.getMessage());
        } catch (InterruptedException ex) {
            t.DebugLog(ex.getMessage());
        }
    }

}

class StreamDirectionThread implements Runnable {
    RobotControl t = new RobotControl();
    public void run() {
        try {
            t.StreamDirectionToClient();
        } catch (IOException ex) {
            t.DebugLog(ex.getMessage());
        } catch (InterruptedException ex) {
            t.DebugLog(ex.getMessage());
        }
    }
}

class ListeningThread implements Runnable{
    RobotControl t = new RobotControl();
    public void run(){
        try {
            t.Listening();
        } catch (IOException ex) {
            t.DebugLog(ex.getMessage());
        } catch (InterruptedException ex) {
            t.DebugLog(ex.getMessage());
        }
    }
}

public class RobotControl {

    byte s = 0;
    public static ServerSocket serverSocket = null;
    public static Socket clientSocket = null;
    public static PrintWriter outTcp = null;
    public static BufferedReader inTcp = null;
    public static boolean checklisten = false;
    public static GuiControlNode winframe = null;
    public static String ConnectionStatus = null;
    public static String DirectionStream = null;
    public static String ClientIn;
    
    
    
    public static void main(String[] args) throws IOException {

        winframe = new GuiControlNode();
        serverSocket = new ServerSocket(10007);
        Thread ListenThread = new Thread(new ListeningThread());
        Thread RecieveThread = new Thread(new RecieveFromClientThread());
        Thread DirectionStreamThread = new Thread(new StreamDirectionThread());
        ListenThread.start();
        RecieveThread.start();
        DirectionStreamThread.start();
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                winframe.setVisible(true);
            }
        });

    }
    public void Listening() throws IOException, InterruptedException{
        while(winframe.CheckListenbtn == false){ 
            Thread.sleep(1);
        }
        this.DebugLog("Listening for Connection");
        clientSocket = serverSocket.accept();
        this.DebugLog("Connect to Client");
        outTcp = new PrintWriter(clientSocket.getOutputStream(), true);
        inTcp = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.ConnectionStatus = "OK";
        this.DebugLog(this.ConnectionStatus);
    }
    public void StreamDirectionToClient() throws IOException, InterruptedException{
        while(this.ConnectionStatus != "OK"){
            Thread.sleep(1);
        }
        winframe.DirectionStream = null;
        while(true){
            this.DirectionStream = winframe.DirectionStream;
            if(this.DirectionStream != null){
                outTcp.println(this.DirectionStream);
                this.DirectionStream = null;
                winframe.DirectionStream = null;
            }
            Thread.sleep(1);
        }
    }
    public void RecieveFromClient() throws IOException, InterruptedException {
        while(this.ConnectionStatus != "OK"){
            Thread.sleep(1);
        }
        while (true) {
            while ((ClientIn = inTcp.readLine()) != null) {
                this.DebugLog("Server: " + ClientIn);
                if (ClientIn.equals("Bye.")) {
                    this.Closeconnection();
                    break;
                }
                if (winframe.CheckDisconnectbtn == true) {
                    this.Closeconnection();
                    this.DebugLog("Closing...");
                    winframe.dispose();
                    break;
                }
            }
            Thread.sleep(1);
        }
    }

    private void Closeconnection() throws IOException {
        this.DebugLog("Close Connection");
        outTcp.close();
        inTcp.close();
        clientSocket.close();
        serverSocket.close();
    }

    public void DebugLog(String x) {
        winframe.displayMessage(x);
        logtofile();
    }

    private void logtofile() {

    }

}
