package Equipe45.domain.Drawing;

import Equipe45.domain.Controller;
import Equipe45.domain.Cut;
import Equipe45.domain.DTO.DimensionDTO;
import Equipe45.domain.ParallelCut;
import Equipe45.domain.RegularCut;

import java.awt.*;
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

    private void drawCuts(Graphics2D g2d) {
        for (Cut cut : controller.getCnc().GetPanel().getCuts()) {
            if (cut instanceof RegularCut) {
                RegularCut regularCut = (RegularCut) cut;
                g2d.setColor(Color.RED);
                g2d.drawLine(
                        (int) regularCut.getOrigin().getX(),
                        (int) regularCut.getOrigin().getY(),
                        (int) regularCut.getDestination().getX(),
                        (int) regularCut.getDestination().getY()
                );
            }
        }
    }
}
