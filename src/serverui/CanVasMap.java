package serverui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import javax.swing.JPanel;

public class CanVasMap extends JPanel {

    public static boolean TeachStatus = false;
    int[] polygonXs = {1, 133, 58, 139, 134};
    int[] polygonYs = {1, 133, 50, 2, 4};
    public static int xs2 = 1, ys2 = 1;
    public static float xl1 = 30, xl2 = 30, yl1 = 200, yl2 = 30;
    Point defaultPoint = new Point(0, 0);
    public static int StartLineIndex = -1;
    public static ArrayList<Point> PolyStartPoint = new ArrayList<>();
    public static ArrayList<Point> PolyEndPoint = new ArrayList<>();

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.BLACK);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.PINK);
        g2d.setStroke(new BasicStroke(5));
        g2d.draw(new Line2D.Float(xl1, xl2, yl1, yl2));
        for (int i = 0; i < PolyStartPoint.size(); i++) {
            g2d.draw(new Line2D.Float(PolyStartPoint.get(i), PolyEndPoint.get(i)));
            System.out.println(StartLineIndex);
            System.out.println("PolyStartPoint : " + PolyStartPoint);
        }
        if (this.TeachStatus == true) {
            g2d.drawPolyline(polygonXs, polygonYs, polygonXs.length);
        }
    }

}
