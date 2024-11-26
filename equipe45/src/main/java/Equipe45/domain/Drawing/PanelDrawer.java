package Equipe45.domain.Drawing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import Equipe45.domain.Controller;
import Equipe45.domain.Cut;
import Equipe45.domain.LShapedCut;
import Equipe45.domain.NoCutZone;
import Equipe45.domain.ReCut;
import Equipe45.domain.RectangularCut;
import Equipe45.domain.RegularCut;

public class PanelDrawer {
    private final Controller controller;
    private Dimension initialDimension;

    public PanelDrawer(Controller controller, Dimension initialDimension) {
        this.controller = controller;
        this.initialDimension = initialDimension;
    }

    public void draw(Graphics2D g2d) {
        drawPanel(g2d);
        drawNoCutZones(g2d);
        drawCuts(g2d);
        controller.invalidateCutsInNoCutZones();
    }

    private void drawPanel(Graphics2D g2d) {
        g2d.setColor(Color.LIGHT_GRAY.brighter());

        float width = controller.GetPanel().dimension.width;
        float height = controller.GetPanel().dimension.height;

        Rectangle2D.Float rectangle = new Rectangle2D.Float(0f, 0f, width, height);
        g2d.fill(rectangle);
        g2d.draw(rectangle);
    }
    private Color setCutColor(Cut cut){
        if(controller.isSelectedCut(cut)) return Color.BLACK;
        else if(!cut.isValid()) return Color.RED;
        else if(cut.isInNoCutZone()) return Color.RED;
        else return Color.GREEN.darker();
    }
    private void drawCuts(Graphics2D g2d) {
        System.out.println("Draw");
        for (Cut cut : controller.getCuts()) {
            if (cut instanceof RegularCut) {
                RegularCut regularCut = (RegularCut) cut;
                g2d.setColor(setCutColor(regularCut));

                Line2D.Float line = new Line2D.Float(
                        regularCut.getOrigin().getX(),
                        regularCut.getOrigin().getY(),
                        regularCut.getDestination().getX(),
                        regularCut.getDestination().getY()
                );
                g2d.draw(line);
            } else if (cut instanceof ReCut reCut) {
                System.out.println("RECOUPE DETECTERRRRRRRR");
                g2d.setColor(setCutColor(reCut));
                Line2D.Float line1 = new Line2D.Float(
                        reCut.getLeftVerticalCut().getOrigin().getX(),
                        reCut.getLeftVerticalCut().getOrigin().getY(),
                        reCut.getLeftVerticalCut().getDestination().getX(),
                        reCut.getLeftVerticalCut().getDestination().getY()
                );
                Line2D.Float line2 = new Line2D.Float(
                        reCut.getRightVerticalCut().getOrigin().getX(),
                        reCut.getRightVerticalCut().getOrigin().getY(),
                        reCut.getRightVerticalCut().getDestination().getX(),
                        reCut.getRightVerticalCut().getDestination().getY()
                );

                Line2D.Float line3 = new Line2D.Float(
                        reCut.getTopHorizontalCut().getOrigin().getX(),
                        reCut.getTopHorizontalCut().getOrigin().getY(),
                        reCut.getTopHorizontalCut().getDestination().getX(),
                        reCut.getTopHorizontalCut().getDestination().getY()
                );
                Line2D.Float line4 = new Line2D.Float(
                        reCut.getBottomHorizontalCut().getOrigin().getX(),
                        reCut.getBottomHorizontalCut().getOrigin().getY(),
                        reCut.getBottomHorizontalCut().getDestination().getX(),
                        reCut.getBottomHorizontalCut().getDestination().getY()
                );

                g2d.draw(line1);
                g2d.draw(line2);
                g2d.draw(line3);
                g2d.draw(line4);

            } else if (cut instanceof LShapedCut) {
                LShapedCut lShapedCut = (LShapedCut) cut;
                g2d.setColor(setCutColor(lShapedCut));
                Line2D.Float line1 = new Line2D.Float(
                        lShapedCut.getVerticalCut().getOrigin().getX(),
                        lShapedCut.getVerticalCut().getOrigin().getY(),
                        lShapedCut.getVerticalCut().getDestination().getX(),
                        lShapedCut.getVerticalCut().getDestination().getY()
                );

                Line2D.Float line2 = new Line2D.Float(
                        lShapedCut.getHorizontalCut().getOrigin().getX(),
                        lShapedCut.getHorizontalCut().getOrigin().getY(),
                        lShapedCut.getHorizontalCut().getDestination().getX(),
                        lShapedCut.getHorizontalCut().getDestination().getY()
                );

                g2d.draw(line1);
                g2d.draw(line2);
           }
           else if (cut instanceof RectangularCut){
                RectangularCut rectangularCut = (RectangularCut) cut;
                g2d.setColor(setCutColor(rectangularCut));

                Line2D.Float line1 = new Line2D.Float(
                        rectangularCut.getLeftVerticalCut().getOrigin().getX(),
                        rectangularCut.getLeftVerticalCut().getOrigin().getY(),
                        rectangularCut.getLeftVerticalCut().getDestination().getX(),
                        rectangularCut.getLeftVerticalCut().getDestination().getY()
                );
                Line2D.Float line2 = new Line2D.Float(
                        rectangularCut.getRightVerticalCut().getOrigin().getX(),
                        rectangularCut.getRightVerticalCut().getOrigin().getY(),
                        rectangularCut.getRightVerticalCut().getDestination().getX(),
                        rectangularCut.getRightVerticalCut().getDestination().getY()
                );

                Line2D.Float line3 = new Line2D.Float(
                        rectangularCut.getTopHorizontalCut().getOrigin().getX(),
                        rectangularCut.getTopHorizontalCut().getOrigin().getY(),
                        rectangularCut.getTopHorizontalCut().getDestination().getX(),
                        rectangularCut.getTopHorizontalCut().getDestination().getY()
                );
                Line2D.Float line4 = new Line2D.Float(
                        rectangularCut.getBottomHorizontalCut().getOrigin().getX(),
                        rectangularCut.getBottomHorizontalCut().getOrigin().getY(),
                        rectangularCut.getBottomHorizontalCut().getDestination().getX(),
                        rectangularCut.getBottomHorizontalCut().getDestination().getY()
                );

                g2d.draw(line1);
                g2d.draw(line2);
                g2d.draw(line3);
                g2d.draw(line4);
            }
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
