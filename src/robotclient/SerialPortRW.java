
package robotclient;


import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.TooManyListenersException;





public class SerialPortRW implements SerialPortEventListener {
    public static byte counter=0;
    SerialPort serialPort;
    private static final String PORT_NAMES[]={
        "/dev/ttyACM0",//for Ubuntu
        "/dev/tty.usbserial-A9007UX1",//Mac OS X
        "/dev/ttyUSB0",//linux
        "COM3",//Windows
        "COM4",
        "COM5",
        "COM6",
        "COM7",
        "COM8",
        "COM9",
    };
    private BufferedReader inputSerial;
    private OutputStream outputStream;
    private static PrintStream outputSerial;
    private static final int TIME_OUT = 2000;
    private static final int DATA_RATE = 9600;
    public static String readSerial;
    public static String SerialStatus;
    public void initialize() throws IOException, UnsupportedCommOperationException{
        CommPortIdentifier portId = null;
        Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
        while(portEnum.hasMoreElements()){
            CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
            for(String portName : PORT_NAMES){
                if(currPortId.getName().equals(portName)){
                    portId = currPortId;
                    break;
                }
            }
        }
        if(portId == null){
            System.out.println(this.toString()+" : Could not find COM port.");
            SerialPortRW.SerialStatus = "Could not find COM port.";
        }
        else{
            try{
                serialPort = (SerialPort) portId.open(this.getClass().getName(), TIME_OUT);
                serialPort.setSerialPortParams(DATA_RATE, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
                //input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
                inputSerial = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
                outputStream = serialPort.getOutputStream(); 
                outputSerial = new PrintStream(outputStream);
                serialPort.addEventListener(this);
                serialPort.notifyOnDataAvailable(true);
                //outputSerial.println("fucker");
                SerialPortRW.SerialStatus = "OK";
            }catch(PortInUseException | UnsupportedCommOperationException | IOException | TooManyListenersException e){
                System.err.println(e.toString());
                SerialPortRW.SerialStatus = e.toString();
            }
        }
    }
    
    public synchronized void close(){
        if(serialPort != null){
            serialPort.removeEventListener();
            serialPort.close();
        }
    }
    
    public synchronized void serialSend(String message){
        outputSerial.println(message);
    }
    
    public synchronized void serialEvent(SerialPortEvent oEvent){
        if(oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE){
            try{
                SerialPortRW.readSerial = inputSerial.readLine();
                this.counter++;
                //outputSerial.println("fucker");
                System.out.println(SerialPortRW.readSerial);
            }catch(Exception e){
                System.err.println(e.toString());
            }
        }
    }
    
 
//    public static void main(String[] args) throws Exception{
//        SimpleRead main = new SimpleRead();
//        main.initialize();
//        Thread t=new Thread(){
//            public void run(){
//                try {
//                    while(true){
//                    outx.println("fucker");
//                    Thread.sleep(50000);
//                    }
//                } catch (InterruptedException ex) {
//                    Logger.getLogger(SimpleRead.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        };
//        t.start();
//    }  
}

