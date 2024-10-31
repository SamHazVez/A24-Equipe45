package Equipe45.gui;

import Equipe45.domain.Drawing.PanelDrawer;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.Serializable;

public class DrawingPanel extends JPanel implements Serializable {

    private Dimension initialDimension;
    private MainWindow mainWindow;
    private double zoomFactor = 1.0; 
    private AffineTransform transform = new AffineTransform();

    public DrawingPanel() {
    }

    public DrawingPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        setBorder(new BevelBorder(BevelBorder.LOWERED));

        int width = 500;
        int height = 400;
        setPreferredSize(new Dimension(width, height));
        setVisible(true);

        initialDimension = new Dimension(width, height);

        addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getPreciseWheelRotation() < 0) {
                    zoomFactor *= 1.1;
                } else {
                    zoomFactor /= 1.1;  
                }
                repaint(); 
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                Point logicalPoint = getLogicalPoint(e.getPoint());
                if (logicalPoint != null) {
                    System.out.println("Logical Coordinates: (" + logicalPoint.x + ", " + logicalPoint.y + ")");
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (mainWindow != null) {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g.create();

            transform = AffineTransform.getScaleInstance(zoomFactor, zoomFactor);
            g2d.setTransform(transform);

            PanelDrawer mainDrawer = new PanelDrawer(mainWindow.getController(), initialDimension);
            mainDrawer.draw(g2d);

            g2d.dispose();
        }
    }

    public MainWindow getMainWindow() {
        return mainWindow;
    }

    public void setMainWindow(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    public Dimension getInitialDimension() {
        return initialDimension;
    }

    public void setInitialDimension(Dimension initialDimension) {
        this.initialDimension = initialDimension;
    }

public Point getLogicalPoint(Point scaledPoint) {
    try {
        AffineTransform inverseTransform = transform.createInverse();
        Point2D logicalPoint2D = inverseTransform.transform(scaledPoint, null);
        return new Point((int) logicalPoint2D.getX(), (int) logicalPoint2D.getY());
    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}

}
