package Equipe45.domain.Drawing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import Equipe45.domain.Controller;
import Equipe45.domain.DTO.LineCutDTO;
import Equipe45.domain.NoCutZone;

public class PanelDrawer {
    private final Controller controller;
    
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
        float gridCount = controller.getGridCount();
        float width = controller.getPanelWidth();
        float height = controller.getPanelHeight();
        float gridSpace = 1;
        g2d.setColor(Color.GRAY.brighter());
        
        if(width <= height)
            gridSpace = width/gridCount;
        else if (height < width)
            gridSpace = height/gridCount;
        
        for (int i = 0; i < gridCount; i++) {
            g2d.draw(new Line2D.Float(0, gridSpace * i, width, gridSpace * i));
        }
        for (int i = 0; i < gridCount; i++) {
            g2d.draw(new Line2D.Float(gridSpace * i, 0, gridSpace * i, height));
        }
    }

    private void drawCuts(Graphics2D g2d) {
        System.out.println("Draw");
        for (LineCutDTO cut : controller.getAllDrawableCuts()) {
                g2d.setColor(cut.color);

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
}
