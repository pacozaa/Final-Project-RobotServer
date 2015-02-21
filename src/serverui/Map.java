package serverui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JPanel;

public class Map extends JPanel {

    public static boolean TeachStatus = false;
    int[] polygonXs = {1, 133, 58, 139, 134};
    int[] polygonYs = {1, 133, 50, 2, 4};
    public static int xs2 = 1, ys2 = 1;
    public static float xl1 = 30, xl2 = 30, yl1 = 200, yl2 = 30;
    Point defaultPoint = new Point(0, 0);
    public static int DIndex = 0;
    public static ArrayList<Point> PolyStartPoint = new ArrayList<>();
    public static ArrayList<Point> PolyEndPoint = new ArrayList<>();
    public static ArrayList<String> CoreStep = new ArrayList<>();
    public static ArrayList<String> CoreDirection = new ArrayList<>();
    public static ArrayList<String> CoreStepLength = new ArrayList<>();
    public static ArrayList<Integer> IndexOfLen = new ArrayList<>();
    private static String DirectionBuffer;
    private static String StepBuffer;

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.BLACK);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.PINK);
        g2d.setStroke(new BasicStroke(5));
        if (Map.TeachStatus == true) {
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
                int Len = getLenght(PolyStartPoint.get(i), PolyEndPoint.get(i));
                CoreStep.add(j++, Integer.toString(Len));
                CoreStep.add(j++, StepBuffer);
            }
        }
        System.out.println(CoreStep);
        System.out.println(CoreStep.toString());
    }

    public static int getLenght(Point Start, Point End) {
        int xLen = End.x - Start.x;
        int yLen = End.y - Start.y;
        if (xLen == 0) {
            return yLen;
        } else {
            return xLen;
        }
    }

    public static void SaveMapToXMLFile(String FileName) throws FileNotFoundException, IOException {
        String Content = CoreStep.toString() + "#" + IndexOfLen.toString() + "#" + PolyStartPoint.toString() + "#" + PolyEndPoint.toString();
        File file = new File(FileName);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream fop = new FileOutputStream(FileName);
        byte[] contentInBytes = Content.getBytes();
        fop.write(contentInBytes);
        fop.flush();
        fop.close();
        System.out.println("CoreStep : "+CoreStep.toString());
        System.out.println("Done Save File : "+FileName);   
    }

}
