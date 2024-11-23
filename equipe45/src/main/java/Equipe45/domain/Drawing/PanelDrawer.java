package Equipe45.domain.Drawing;

import Equipe45.domain.*;
import Equipe45.domain.DTO.DimensionDTO;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

public class PanelDrawer {
    private final Controller controller;
    private Dimension initialDimension;

    public PanelDrawer(Controller controller, Dimension initialDimension) {
        this.controller = controller;
        this.initialDimension = initialDimension;
    }

    public void draw(Graphics2D g2d) {
        drawPanel(g2d);
        drawCuts(g2d);
    }

    private void drawPanel(Graphics2D g2d) {
        g2d.setColor(Color.LIGHT_GRAY);

        float width = controller.GetPanel().dimension.width;
        float height = controller.GetPanel().dimension.height;

        Rectangle2D.Float rectangle = new Rectangle2D.Float(0f, 0f, width, height);
        g2d.fill(rectangle);
        g2d.setColor(Color.BLACK);
        g2d.draw(rectangle);
    }
    private Color setCutColor(Cut cut){
        if(cut.isValid()) return Color.GREEN.darker().darker();
        else return Color.RED;
    }
    private void drawCuts(Graphics2D g2d) {
        for (Cut cut : controller.getCnc().GetPanel().getCuts()) {
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
            }

           else if (cut instanceof LShapedCut) {
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

           else if (cut instanceof ReCut) {
                ReCut reCut = (ReCut) cut;
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
            }
        }
    }

}
