package Equipe45.gui;

import Equipe45.domain.Controller;
import Equipe45.domain.DTO.CutDTO;
import Equipe45.domain.DTO.StraightCutDTO;
import Equipe45.domain.DTO.ToolDTO;
import Equipe45.domain.Drawing.PanelDrawer;
import Equipe45.domain.Tool;
import Equipe45.domain.Utils.Coordinate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.Serializable;
import java.util.UUID;

public class DrawingPanel extends JPanel implements Serializable {
    private Dimension initialDimension;
    private MainWindow mainWindow;
    private double zoomFactor = 1.0;

    private Point startPoint = null;

    public DrawingPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        // Use a border that doesn't affect insets
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        // Or remove the border entirely
        // setBorder(null);

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
                zoomFactor = Math.max(0.1, Math.min(zoomFactor, 10.0));
                repaint();
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClick(e);
            }
        });
    }

    private void handleMouseClick(MouseEvent e) {
        Controller controller = mainWindow.getController();
        
        CutDTO clickedCut = controller.handleCutClick(e.getX(), e.getY());
        if(clickedCut != null){
            updateSelectedCut(clickedCut);
        } else {
            deselectCut();
        }
        
        if (controller.getMode() == Controller.Mode.CREATE_VERTICAL_CUT) {
            Point logicalPoint = getLogicalPoint(e.getPoint());
            if (logicalPoint == null) {
                return;
            }

            if (startPoint == null) {
                startPoint = logicalPoint;
                System.out.println("Start Point: " + startPoint);
            } else {
                Point endPoint = logicalPoint;
                System.out.println("End Point: " + endPoint);

                if (startPoint.x != endPoint.x) {
                    endPoint = new Point(startPoint.x, endPoint.y);
                    System.out.println("Adjusted End Point for verticality: " + endPoint);
                }

                createVerticalCut(startPoint, endPoint);

                startPoint = null;
                mainWindow.exitCreateVerticalCutMode();
            }

            repaint();
        }
    }
    
    private void updateSelectedCut(CutDTO cut){
        if(cut != null){
            if (cut instanceof StraightCutDTO regularCutDTO) {
                mainWindow.updateCutOriginInformations(regularCutDTO.getOrigin().getX(), regularCutDTO.getOrigin().getY());
                mainWindow.updateCutDestinationInformations(regularCutDTO.getDestination().getX(), regularCutDTO.getDestination().getY());
                mainWindow.hideIntersection();
            }
            //TODO handle les autres types de coupe
        }
    }
    
    private void deselectCut(){
        mainWindow.updateCutOriginInformations(0, 0);
        mainWindow.updateCutDestinationInformations(0, 0);
        mainWindow.hideIntersection();
    }

    private void createVerticalCut(Point start, Point end) {
        Controller controller = mainWindow.getController();

        Coordinate origin = new Coordinate(start.x, start.y);
        Coordinate destination = new Coordinate(end.x, end.y);

        ToolDTO selectedToolDTO = controller.getSelectedTool();
        Tool selectedTool = controller.getToolConverter().convertToToolFrom(selectedToolDTO);
        float defaultDepth = 10.0f + 0.5f;

        StraightCutDTO newCutDTO = new StraightCutDTO(
                UUID.randomUUID(),
                defaultDepth,
                selectedTool,
                origin,
                destination
        );

        controller.addNewCut(newCutDTO);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (mainWindow != null) {
            Graphics2D g2d = (Graphics2D) g.create();

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Apply scaling
            g2d.scale(zoomFactor, zoomFactor);

            // Now draw
            PanelDrawer mainDrawer = new PanelDrawer(mainWindow.getController(), initialDimension);
            mainDrawer.draw(g2d);

            if (startPoint != null) {
                drawStartPoint(g2d, startPoint);
            }

            g2d.dispose();
        }
    }

    /**
     * Draws a small red circle at the specified point.
     *
     * @param g2d   The Graphics2D object for drawing.
     * @param point The logical point where the circle should be drawn.
     */
    private void drawStartPoint(Graphics2D g2d, Point point) {
        float radius = 5.0f;
        float diameter = radius * 2;
        float x = point.x - radius;
        float y = point.y - radius;

        g2d.setColor(Color.RED);
        g2d.fillOval(Math.round(x), Math.round(y), Math.round(diameter), Math.round(diameter));
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

    public Point getLogicalPoint(Point screenPoint) {
        try {
            // Adjust for scaling
            double logicalX = screenPoint.x / zoomFactor;
            double logicalY = screenPoint.y / zoomFactor;

            return new Point((int) logicalX, (int) logicalY);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

