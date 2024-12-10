package Equipe45.domain.Drawing;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import Equipe45.domain.Controller;
import Equipe45.domain.DTO.LineCutDTO;
import Equipe45.domain.NoCutZone;

public class PanelDrawer {
    private final Controller controller;
    private float gridSize = 1;
    
    public PanelDrawer(Controller controller, Dimension initialDimension) {
        this.controller = controller;
    }

    public void draw(Graphics2D g2d) {
        drawPanel(g2d);
        drawGrid(g2d);
        drawNoCutZones(g2d);
        drawCuts(g2d);
        controller.invalidateCutsInNoCutZones();
    }

    private void drawPanel(Graphics2D g2d) {
        g2d.setColor(Color.LIGHT_GRAY.brighter());

        float width = controller.getPanelWidth();
        float height = controller.getPanelHeight();

        Rectangle2D.Float rectangle = new Rectangle2D.Float(0f, 0f, width, height);
        g2d.fill(rectangle);
        g2d.draw(rectangle);
    }
    
    private void drawGrid(Graphics2D g2d) {
        this.gridSize = controller.getGridSize();
        if(gridSize == 0)
            return;
        float width = controller.getPanelWidth();
        System.out.println(width);
        float height = controller.getPanelHeight();
        System.out.println(height);
        g2d.setColor(Color.GRAY.brighter());
        
        for (int i = 0; i * gridSize < height; i++) {
            g2d.draw(new Line2D.Float(0, gridSize * i, width, gridSize * i));
        }
        for (int j = 0; j * gridSize < width; j++) {
            g2d.draw(new Line2D.Float(gridSize * j, 0, gridSize * j, height));
        }
    }

    private void drawCuts(Graphics2D g2d) {
        System.out.println("Draw");
        for (LineCutDTO cut : controller.getAllDrawableCuts()) {
            g2d.setColor(cut.color);


            float toolWidth = cut.toolDTO.getCutWidth();

            //Changez ça si vous voulez faire que les cuts sont plus larges/minces
            float displayWidth = toolWidth * 0.6f;

            g2d.setStroke(new BasicStroke(displayWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            Line2D.Float line = new Line2D.Float(
                    cut.origin.getX(),
                    cut.origin.getY(),
                    cut.destination.getX(),
                    cut.destination.getY()
            );
            g2d.draw(line);
        }
    }


    private void drawNoCutZones(Graphics2D g2d) {
        g2d.setColor(new Color(255, 0, 0, 100)); // Rouge mais transparent
        for (NoCutZone noCutZone : controller.getNoCutZones()) {
            Rectangle2D.Float rectangle = new Rectangle2D.Float(
                    noCutZone.getCoordinate().getX(),
                    noCutZone.getCoordinate().getY(),
                    noCutZone.getDimension().getWidth(),
                    noCutZone.getDimension().getHeight()
            );
            g2d.fill(rectangle);
            g2d.draw(rectangle);
        }
    }
    
    /*private float getSnappedValue(float value){
        if(controller.getGridSize() == 0)
            return value;
        return Math.round(value / gridSize) * gridSize;
    }*/
}
