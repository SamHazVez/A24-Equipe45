package Equipe45.domain.Drawing;

import Equipe45.domain.Controller;

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

    public void draw(Graphics g) {
        drawPanel(g);
        drawCuts(g);
    }

    private void drawPanel(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    g2d.setColor(Color.LIGHT_GRAY);

    float width = controller.GetPanel().dimension.width;
    float height = controller.GetPanel().dimension.height;

    Rectangle2D.Float rectangle = new Rectangle2D.Float(0f, 0f, width, height);
    g2d.draw(rectangle);
    g2d.fill(rectangle);
}

    private void drawCuts(Graphics g) {

    }
}
