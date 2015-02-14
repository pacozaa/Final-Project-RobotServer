
package serverui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class J2d1 extends JPanel{
    //public static final int CANVAS_WIDTH = 640;
    //public static final int CANVAS_HEIGHT = 480;
    public static final String TITLE = "Affine Transform Demo";
    
    int[] polygonXs = { 131, 133, 138, 139, 134};
    int[] polygonYs = { 0, -9, -3, 2, 4};
    Shape shape = new Polygon(polygonXs, polygonYs, polygonXs.length);
    double x = 50.0, y = 50.0;
    
    public J2d1(){
        //setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
    }
    
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        setBackground(Color.BLACK);
        Graphics2D g2d = (Graphics2D)g;
        AffineTransform saveTransform = g2d.getTransform();
        AffineTransform identity = new AffineTransform();
        g2d.setTransform(identity);
        
        g2d.setColor(Color.green);
        g2d.fill(shape);
        g2d.translate(x, y);
        g2d.scale(2.2, 2.2);
        g2d.fill(shape);
        
        for(int i = 0; i < 5; ++i){
            g2d.translate(50.0, 5.0);
            g2d.setColor(Color.blue);
            g2d.fill(shape);
            g2d.rotate(Math.toRadians(15.0));
            g2d.setColor(Color.RED);
            g2d.fill(shape);            
        }
        g2d.setTransform(saveTransform);
        g.drawString (Long.toString(System.currentTimeMillis()), 100, 300); 
    repaint();
        
    }
    
    
}

