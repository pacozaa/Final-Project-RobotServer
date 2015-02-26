package serverui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import static java.lang.Math.abs;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JPanel;

public class Map extends JPanel {

    public static boolean TeachStatus = false;
    public static boolean LoadStatus = false;

    
    int[] polygonXs = {1, 133, 58, 139, 134};
    int[] polygonYs = {1, 133, 50, 2, 4};
    public static int xs2 = 1, ys2 = 1;
    public static float xl1 = 30, xl2 = 30, yl1 = 200, yl2 = 30;
    public static int DIndex = 0;
    public static ArrayList<Point> PolyStartPoint = new ArrayList<>();
    public static ArrayList<Point> PolyEndPoint = new ArrayList<>();
    public static ArrayList<String> CoreStep = new ArrayList<>();
    public static ArrayList<String> CoreDirection = new ArrayList<>();
    public static ArrayList<Integer> IndexOfLen = new ArrayList<>();
    private static String DirectionBuffer;
    private static String StepBuffer;
    public static String ReadMap;
    public static boolean OpStatus = false;
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.BLACK);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLUE);
        g2d.setStroke(new BasicStroke(5));
        if (Map.TeachStatus == true || Map.LoadStatus == true) {
            for (int i = 0; i < PolyStartPoint.size(); i++) {
                g2d.draw(new Line2D.Float(PolyStartPoint.get(i), PolyEndPoint.get(i)));
            }
        }

    }

    public static void AnalystPath() {
        for (int i = 0; i < PolyStartPoint.size(); i++) {
            int checkX = PolyStartPoint.get(i).x - PolyEndPoint.get(i).x;
            int checkY = PolyStartPoint.get(i).y - PolyEndPoint.get(i).y;
            if (checkX != 0 && checkY == 0) {
                if (checkX > 0) {
                    DirectionBuffer = "ReverseX";
                }
                if (checkX < 0) {

                    DirectionBuffer = "AlongX";
                }
            }
            if (checkY != 0 && checkX == 0) {
                if (checkY > 0) {
                    DirectionBuffer = "ReverseY";
                }
                if (checkY < 0) {

                    DirectionBuffer = "AlongY";
                }
            }
            CoreDirection.add(i, DirectionBuffer);
        }
        int j = 0;
        for (int i = 0; i < CoreDirection.size(); i++) {
            if (CoreDirection.size() > (i + 1)) {
                String bf = CoreDirection.get(i);
                String at = CoreDirection.get(i + 1);
                if (("AlongX".equals(bf) && "ReverseX".equals(at)) || ("AlongY".equals(bf) && "ReverseY".equals(at))) {
                    StepBuffer = "Reverse";
                }
                if (("AlongX".equals(bf) && "AlongX".equals(at)) || ("AlongY".equals(bf) && "AlongY".equals(at))) {
                    StepBuffer = "Str";
                }
                if (("AlongX".equals(bf) && "AlongY".equals(at)) || ("AlongY".equals(bf) && "ReverseX".equals(at))) {
                    StepBuffer = "Right";
                }
                if (("AlongX".equals(bf) && "ReverseY".equals(at)) || ("AlongY".equals(bf) && "AlongX".equals(at))) {
                    StepBuffer = "Left";
                }
                IndexOfLen.add(i, j);
                Double Len = getLenght(PolyStartPoint.get(i), PolyEndPoint.get(i));
                CoreStep.add(j++, Double.toString(Len));
                CoreStep.add(j++, StepBuffer); 
            }
        }
        System.out.println(CoreStep);
        System.out.println(CoreStep.toString());
    }

    public static Double getLenght(Point Start, Point End) {
        int xLen = End.x - Start.x;
        int yLen = End.y - Start.y;
        Double ActLen = null;
        Double sActLen = null;
        if (xLen == 0) {
            String selected = GuiControlNode.cbxScaleSelector.getSelectedItem().toString().replace("m", "");
            Double Scale = Double.parseDouble(selected);
            sActLen = ((double)yLen / GuiControlNode.GraphicMap.getSize().height) * Scale;
            ActLen = new BigDecimal(sActLen).setScale(3, BigDecimal.ROUND_HALF_DOWN).doubleValue();
            ActLen = abs(ActLen);
            return ActLen;
        } else {
            String selected = GuiControlNode.cbxScaleSelector.getSelectedItem().toString().replace("m", "");
            Double Scale = Double.parseDouble(selected);
            sActLen = ((double)xLen / GuiControlNode.GraphicMap.getSize().width) * Scale;
            ActLen = new BigDecimal(sActLen).setScale(3, BigDecimal.ROUND_HALF_DOWN).doubleValue();
            ActLen = abs(ActLen);
            return ActLen;
        }
    }

    public static void SaveMapToTextFile(String FileName) throws FileNotFoundException, IOException {
        if (Map.TeachStatus == true) {
            String Content;
            String StartX = "";
            String StartY = "";
            String EndX = "";
            String EndY = "";
            for (int i = 0; i < PolyStartPoint.size(); i++) {
                StartX += String.valueOf(PolyStartPoint.get(i).x) + ",";
                StartY += String.valueOf(PolyStartPoint.get(i).y) + ",";
                EndX += String.valueOf(PolyEndPoint.get(i).x) + ",";
                EndY += String.valueOf(PolyEndPoint.get(i).y) + ",";
            }
            Content = CoreStep.toString() + "#" + IndexOfLen.toString() + "#" + StartX + "#" + StartY + "#" + EndX + "#" + EndY;
            try {
                File file = new File(FileName);
                if (!file.exists()) {
                    file.createNewFile();
                }
                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(Content);
                bw.close();
                System.out.println("CoreStep : " + CoreStep.toString());
                System.out.println("Done Save File : " + FileName);
                GuiControlNode.btnTeach.doClick();
                Map.PolyStartPoint.clear();
                Map.PolyEndPoint.clear();
                Map.CoreStep.clear();
                Map.CoreDirection.clear();
                Map.IndexOfLen.clear();
                Map.DirectionBuffer = "";
                Map.StepBuffer = "";
                System.out.println("Clear Buffer");

            } catch (IOException e) {
                System.out.println("Error : " + e.toString());
            }
        }
    }

    public static boolean LoadFile(String FileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(FileName))) {
            String sCurrentLine;
            Map.ReadMap = "";
            while ((sCurrentLine = br.readLine()) != null) {
                System.out.println("Read File : " + sCurrentLine);
                Map.ReadMap += sCurrentLine;
            }
            String[] LoadBuffer = Map.ReadMap.replace("[", "").replace("]", "").split("#");//CoreStep,IndexOfLen,StartX,StartY,EndX,EndY
            if (LoadBuffer.length == 6) {
                Map.CoreStep = new ArrayList(Arrays.asList(LoadBuffer[0]));
                Map.IndexOfLen = new ArrayList(Arrays.asList(LoadBuffer[1]));
                String[] StartX = LoadBuffer[2].split(",");
                String[] StartY = LoadBuffer[3].split(",");
                String[] EndX = LoadBuffer[4].split(",");
                String[] EndY = LoadBuffer[5].split(",");
                int[] IntStartX = new int[StartX.length];
                int[] IntStartY = new int[StartY.length];
                int[] IntEndX = new int[EndX.length];
                int[] IntEndY = new int[EndY.length];
                //Create Point
                for (int i = 0; i < StartX.length; i++) {
                    IntStartX[i] = Integer.parseInt(StartX[i]);
                    IntStartY[i] = Integer.parseInt(StartY[i]);
                    IntEndX[i] = Integer.parseInt(EndX[i]);
                    IntEndY[i] = Integer.parseInt(EndY[i]);
                    PolyStartPoint.add(i, new Point(IntStartX[i], IntStartY[i]));
                    PolyEndPoint.add(i, new Point(IntEndX[i], IntEndY[i]));
                }
                System.out.println("File Format Correct");
                Map.LoadStatus = true;
                Map.OpStatus = true;
                return true;
            } else {
                System.out.println("File Format Fail");
                return false;
            }
        } catch (IOException e) {
            System.out.println("Error : " + e.toString());
            return false;
        }   
    }
    public static void PrepareData() {
        Map.AnalystPath();
            String StartX = "";
            String StartY = "";
            String EndX = "";
            String EndY = "";
            for (int i = 0; i < PolyStartPoint.size(); i++) {
                StartX += String.valueOf(PolyStartPoint.get(i).x) + ",";
                StartY += String.valueOf(PolyStartPoint.get(i).y) + ",";
                EndX += String.valueOf(PolyEndPoint.get(i).x) + ",";
                EndY += String.valueOf(PolyEndPoint.get(i).y) + ",";
            }
            GuiControlNode.PlanStream = CoreStep.toString() + "#" + IndexOfLen.toString() + "#" + StartX + "#" + StartY + "#" + EndX + "#" + EndY;      
    }
}
