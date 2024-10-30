package Equipe45.domain.Drawing;

import Equipe45.domain.Controller;

import java.awt.*;
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

    }

    private void drawCuts(Graphics g) {

    }
}
