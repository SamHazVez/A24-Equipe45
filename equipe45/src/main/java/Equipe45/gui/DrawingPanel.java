package Equipe45.gui;

import Equipe45.domain.Controller;
import Equipe45.domain.DTO.*;
import Equipe45.domain.Drawing.PanelDrawer;
import Equipe45.domain.IrregularCut;
import Equipe45.domain.Tool;
import Equipe45.domain.Utils.Coordinate;
import Equipe45.domain.Utils.ReferenceCoordinate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.UUID;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.Serializable;
import javax.swing.*;

public class DrawingPanel extends JPanel implements Serializable {
    private Dimension initialDimension;
    private MainWindow mainWindow;
    private double zoomFactor = 1.0;
    private AffineTransform transform = new AffineTransform();

    public DrawingPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        int width = 500;
        int height = 400;
        setPreferredSize(new Dimension(width, height));
        setVisible(true);

        initialDimension = new Dimension(width, height);

        addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                double scaleFactor = 1.1;
                if (e.getPreciseWheelRotation() < 0) {
                    zoomFactor *= scaleFactor;
                } else {
                    zoomFactor /= scaleFactor;
                }

                if (zoomFactor <= 0) {
                    zoomFactor = Double.MIN_VALUE;
                }

                updateTransform();

                repaint();
            }
        });


        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateTransform();
                repaint();
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClick(e);
            }
        });

        updateTransform();
    }

    private void updateTransform() {
        transform = new AffineTransform();

        int drawingAreaWidth = getWidth();
        int drawingAreaHeight = getHeight();

        PanelDTO panelDTO = mainWindow.getController().getPanel();
        float panelWidth = panelDTO.getDimension().getWidth();
        float panelHeight = panelDTO.getDimension().getHeight();

        double scaledPanelWidth = panelWidth * zoomFactor;
        double scaledPanelHeight = panelHeight * zoomFactor;

        double offsetX = (drawingAreaWidth - scaledPanelWidth) / 2.0;
        double offsetY = (drawingAreaHeight - scaledPanelHeight) / 2.0;
        transform.translate(offsetX, offsetY);
        transform.scale(zoomFactor, zoomFactor);
    }

    private void handleMouseClick(MouseEvent e) {
        Controller controller = mainWindow.getController();
        Point2D.Double logicalPoint = getLogicalPoint(e.getPoint());
        if (logicalPoint == null) {
            return;
        }
        
        CutDTO clickedCut = controller.handleCutClick(logicalPoint.x, logicalPoint.y);
        if(clickedCut != null){
            updateSelectedCut(clickedCut);
        } else {
            mainWindow.deselectCut();
        }

        if (controller.getMode() == Controller.Mode.CREATE_VERTICAL_CUT) {
            float clickX = (float) logicalPoint.getX();
            createVerticalCut(clickX);

            mainWindow.exitCreateVerticalCutMode();
            repaint();
        } else if (controller.getMode() == Controller.Mode.CREATE_HORIZONTAL_CUT) {
            float clickY = (float) logicalPoint.getY();
            createHorizontalCut(clickY);

            mainWindow.exitCreateHorizontalCutMode();
            repaint();
        }
        else if(controller.getMode() == Controller.Mode.CREATE_L_SHAPED_CUT) {

            repaint();
        }

    }



    private void createVerticalCut(float x) {
        Controller controller = mainWindow.getController();

        ToolDTO selectedToolDTO = controller.getSelectedTool();
        Tool selectedTool = controller.getToolConverter().convertToToolFrom(selectedToolDTO);
        float defaultDepth = controller.getCnc().GetPanel().getWidth() + 0.5f;

        PanelDTO panelDTO = controller.getPanel();
        float panelHeight = panelDTO.getDimension().getHeight();

        Coordinate origin = new Coordinate(x, 0f);
        Coordinate destination = new Coordinate(x, panelHeight);

        RegularCutDTO newCutDTO = new RegularCutDTO(
                UUID.randomUUID(),
                defaultDepth,
                selectedTool,
                origin,
                destination
        );

        controller.addNewCut(newCutDTO);
    }


        private void createHorizontalCut(float y) {
            Controller controller = mainWindow.getController();

            ToolDTO selectedToolDTO = controller.getSelectedTool();
            Tool selectedTool = controller.getToolConverter().convertToToolFrom(selectedToolDTO);
            float defaultDepth = controller.getCnc().GetPanel().getWidth() + 0.5f;


            PanelDTO panelDTO = controller.getPanel();
            float panelWidth = panelDTO.getDimension().getWidth();

            Coordinate origin = new Coordinate(0f, y);
            Coordinate destination = new Coordinate(panelWidth, y);

            RegularCutDTO newCutDTO = new RegularCutDTO(
                    UUID.randomUUID(),
                    defaultDepth,
                    selectedTool,
                    origin,
                    destination
            );

            controller.addNewCut(newCutDTO);
        }


    private void createLShapedCut(ReferenceCoordinate reference, Coordinate intersection) {
        Controller controller = mainWindow.getController();

        ToolDTO selectedToolDTO = controller.getSelectedTool();
        Tool selectedTool = controller.getToolConverter().convertToToolFrom(selectedToolDTO);
        float defaultDepth = controller.getCnc().GetPanel().getWidth() + 0.5f;

        LShapedCutDTO newCutDTO = new LShapedCutDTO(
                UUID.randomUUID(),
                defaultDepth,
                selectedTool,
                reference,
                intersection
        );

        controller.addNewCut(newCutDTO);
    }



    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (mainWindow != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.transform(transform);

            PanelDrawer mainDrawer = new PanelDrawer(mainWindow.getController(), initialDimension);
            mainDrawer.draw(g2d);

            g2d.dispose();
        }
    }

    public Point2D.Double getLogicalPoint(Point screenPoint) {
        try {
            AffineTransform inverseTransform = transform.createInverse();
            Point2D logicalPoint2D = inverseTransform.transform(screenPoint, null);
            return new Point2D.Double(logicalPoint2D.getX(), logicalPoint2D.getY());
        } catch (NoninvertibleTransformException e) {
            e.printStackTrace();
            return null;
        }
    }
    
        private void updateSelectedCut(CutDTO cut){
        if(cut != null){
            if (cut instanceof RegularCutDTO regularCutDTO) {
                mainWindow.updateCutOriginInformations(regularCutDTO.getOrigin().getX(), regularCutDTO.getOrigin().getY());
                mainWindow.updateCutDestinationInformations(regularCutDTO.getDestination().getX(), regularCutDTO.getDestination().getY());
                mainWindow.hideIntersection();
            }
            //TODO handle les autres types de coupe
        }
    }
}
