package Equipe45.gui;

import Equipe45.domain.Controller;
import Equipe45.domain.DTO.*;
import Equipe45.domain.Drawing.PanelDrawer;
import Equipe45.domain.RectangularCut;
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

import java.awt.event.*;

public class DrawingPanel extends JPanel implements Serializable {
    private Dimension initialDimension;
    private MainWindow mainWindow;
    private double zoomFactor = 1.0;
    private AffineTransform transform = new AffineTransform();
    private ReferenceCoordinate pendingReferenceCoordinate = null;
    private Coordinate pendingSecondCoordinate = null;
    private UUID selectedCutId;

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
        
        if (controller.getMode() == Controller.Mode.IDLE) {
            CutDTO clickedCut = controller.handleCutClick(logicalPoint.x, logicalPoint.y);
            if(clickedCut != null){
                selectedCutId = clickedCut.getId();
                updateSelectedCut(clickedCut);
                System.out.println("Coupe sélectionnée : " + selectedCutId);
            } else {
                mainWindow.deselectCut();
            }
        } else if (controller.getMode() == Controller.Mode.CREATE_VERTICAL_CUT) {
            float clickX = (float) logicalPoint.getX();
            createVerticalCut(clickX);

            mainWindow.exitCreateVerticalCutMode();
            repaint();
        } else if (controller.getMode() == Controller.Mode.CREATE_HORIZONTAL_CUT) {
            float clickY = (float) logicalPoint.getY();
            createHorizontalCut(clickY);

            mainWindow.exitCreateHorizontalCutMode();
            repaint();
        } else if (controller.getMode() == Controller.Mode.CREATE_L_SHAPED_CUT) {
            float clickX = (float) logicalPoint.getX();
            float clickY = (float) logicalPoint.getY();
            Coordinate clickCoordinate = new Coordinate(clickX, clickY);

            if (pendingReferenceCoordinate == null) {
                ReferenceCoordinate referenceCoordinate = controller.getReferenceCoordinateOfIntersection(clickCoordinate);
                if (referenceCoordinate != null) {
                    pendingReferenceCoordinate = referenceCoordinate;
                    System.out.println("Reference coordinate set. Click again to specify the second point.");
                } else {
                    System.out.println("Invalid reference coordinate. Click again.");
                }
            } else {
                createLShapedCut(pendingReferenceCoordinate, clickCoordinate);
                resetReferenceCoordinate();
                mainWindow.exitCreateLShapedCutMode();
                repaint();
            }
        } else if(controller.getMode() == Controller.Mode.CREATE_RECTANGULAR_CUT) {
            float clickX = (float) logicalPoint.getX();
            float clickY = (float) logicalPoint.getY();
            Coordinate clickCoordinate = new Coordinate(clickX, clickY);

            if (pendingReferenceCoordinate == null) {
                ReferenceCoordinate referenceCoordinate = controller.getReferenceCoordinateOfIntersection(clickCoordinate);
                if(referenceCoordinate == null){
                    referenceCoordinate = new ReferenceCoordinate(clickX, clickY, null, null);
                }
                if (referenceCoordinate != null) {
                    pendingReferenceCoordinate = referenceCoordinate;
                    System.out.println("Reference coordinate set. Click again to specify the second point.");
                } else {
                    System.out.println("Invalid reference coordinate. Click again.");
                }
            } else if (pendingSecondCoordinate == null) {
                pendingSecondCoordinate = clickCoordinate;
                System.out.println("Second point set. Click again to specify the third point.");
            }
            else {
                createRectangularCut(pendingReferenceCoordinate, pendingSecondCoordinate, clickCoordinate);
                resetReferenceCoordinate();
                mainWindow.exitCreateLShapedCutMode();
                repaint();
            }
        }
    }

    public void resetReferenceCoordinate() {
        pendingReferenceCoordinate = null;
        pendingSecondCoordinate = null;
    }

    private void createVerticalCut(float distance) {
        if(selectedCutId == null) {
            System.out.println("Selectionner une bordure avant d'ajouter une coupe");
            return;
        }
        
        Controller controller = mainWindow.getController();

        ToolDTO selectedToolDTO = controller.getSelectedTool();
        Tool selectedTool = controller.getToolConverter().convertToToolFrom(selectedToolDTO);
        float defaultDepth = controller.getCnc().GetPanel().getWidth() + 0.5f;

        ParallelCutDTO newCutDTO = new ParallelCutDTO(
                UUID.randomUUID(),
                defaultDepth,
                selectedTool,
                selectedCutId,
                distance
        );

        controller.addNewCut(newCutDTO);
    }

    private void createHorizontalCut(float distance) {
        if(selectedCutId == null) {
            System.out.println("Selectionner une bordure avant d'ajouter une coupe");
            return;
        }
                
        Controller controller = mainWindow.getController();

        ToolDTO selectedToolDTO = controller.getSelectedTool();
        Tool selectedTool = controller.getToolConverter().convertToToolFrom(selectedToolDTO);
        float defaultDepth = controller.getCnc().GetPanel().getWidth() + 0.5f;


        ParallelCutDTO newCutDTO = new ParallelCutDTO(
                UUID.randomUUID(),
                defaultDepth,
                selectedTool,
                selectedCutId,
                distance
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

    private void createRectangularCut(ReferenceCoordinate reference, Coordinate intersection, Coordinate corner) {
        Controller controller = mainWindow.getController();

        ToolDTO selectedToolDTO = controller.getSelectedTool();
        Tool selectedTool = controller.getToolConverter().convertToToolFrom(selectedToolDTO);
        float defaultDepth = controller.getCnc().GetPanel().getWidth() + 0.5f;

        RectangularCutDTO newCutDTO = new RectangularCutDTO(
                UUID.randomUUID(),
                defaultDepth,
                selectedTool,
                reference,
                intersection,
                corner
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
        System.out.println("Click");
        if(cut != null){
            if (cut instanceof ParallelCutDTO parallelCutDTO) {
                System.out.println("ClickIn");
                //mainWindow.updateCutOriginInformations(parallelCutDTO.referenceID);
                //mainWindow.updateCutDestinationInformations(10f,10f);
                mainWindow.hideIntersection();
            }
            else if (cut instanceof LShapedCutDTO lShapedCutDTO) {
                System.out.println("ClickIn");
                //mainWindow.updateCutOriginInformations(10f,10f);
                //mainWindow.updateCutDestinationInformations(10f,10f);
            }
            /*else if (cut instanceof RectangularCutDTO rectangularCutDTO) {
                System.out.println("ClickIn");
                mainWindow.updateCutOriginInformations(10f,10f);
                mainWindow.updateCutDestinationInformations(10f,10f);
                mainWindow.hideIntersection();
            }*/
            else if (cut instanceof BorderCutDTO) {
                System.out.println("ClickIn");
                //mainWindow.hideIntersection();
            }
        }
    }
}
