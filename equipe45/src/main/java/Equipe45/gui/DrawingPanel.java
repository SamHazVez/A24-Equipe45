package Equipe45.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.UUID;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import Equipe45.domain.Controller;
import Equipe45.domain.DTO.BorderCutDTO;
import Equipe45.domain.DTO.CutDTO;
import Equipe45.domain.DTO.LShapedCutDTO;
import Equipe45.domain.DTO.NoCutZoneDTO;
import Equipe45.domain.DTO.ParallelCutDTO;
import Equipe45.domain.DTO.ReCutDTO;
import Equipe45.domain.DTO.RectangularCutDTO;
import Equipe45.domain.DTO.ToolDTO;
import Equipe45.domain.Drawing.PanelDrawer;
import Equipe45.domain.Utils.Coordinate;
import Equipe45.domain.Utils.ReferenceCoordinate;

public class DrawingPanel extends JPanel implements Serializable {
    private Dimension initialDimension;
    private MainWindow mainWindow;
    private double zoomFactor = 1.0;
    private AffineTransform transform = new AffineTransform();
    private ReferenceCoordinate pendingReferenceCoordinate = null;
    private Coordinate pendingSecondCoordinate = null;
    private UUID selectedCutId;

    private Point2D initZoomPoint;

    
    public DrawingPanel()
    {
    }
    
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
                if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
                    initZoomPoint = e.getPoint();
                    Point2D destZoomPoint = null;
                    try {
                    destZoomPoint = transform.inverseTransform(initZoomPoint, null);
                    } catch (NoninvertibleTransformException ex) {
                        ex.printStackTrace();
                        return;
                    }
                    
                    double scaleFactor = 1.1;
                    if (e.getPreciseWheelRotation() < 0) {
                        zoomFactor *= scaleFactor;
                    } else {
                        zoomFactor /= scaleFactor;
                    }

                    if (zoomFactor <= 0) {
                        zoomFactor = Double.MIN_VALUE;
                    }
                    
                    transform.setToIdentity();
                    transform.translate(initZoomPoint.getX(), initZoomPoint.getY());
                    transform.scale(zoomFactor, zoomFactor);
                    transform.translate(-destZoomPoint.getX(), -destZoomPoint.getY());

                    repaint();
                }

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
        //transform = new AffineTransform();

        /*int drawingAreaWidth = getWidth();
        int drawingAreaHeight = getHeight();

        PanelDTO panelDTO = mainWindow.getController().getPanel();
        float panelWidth = panelDTO.getDimension().getWidth();
        float panelHeight = panelDTO.getDimension().getHeight();

        double scaledPanelWidth = panelWidth * zoomFactor;
        double scaledPanelHeight = panelHeight * zoomFactor;

        double offsetX = (drawingAreaWidth - scaledPanelWidth) / 2.0;
        double offsetY = (drawingAreaHeight - scaledPanelHeight) / 2.0;
        transform.translate(offsetX, offsetY);
        transform.scale(zoomFactor, zoomFactor);*/
    }

    private void handleMouseClick(MouseEvent e) {
        Controller controller = mainWindow.getController();
        Point2D.Double logicalPoint = getLogicalPoint(e.getPoint());
        if (logicalPoint == null) {
            return;
        }
        
        System.out.println(controller.getMode());
        
        if (controller.getMode() == Controller.Mode.IDLE) {
            updateSelectedCut(controller.handleCutClick(logicalPoint.x, logicalPoint.y));
            repaint();
        } else if (controller.getMode() == Controller.Mode.CREATE_VERTICAL_CUT) {
            float clickX = (float) logicalPoint.getX();
            
            float distance = getSnappedValue(clickX) - controller.getSelectedCutDistance();
            
            createVerticalCut(distance);

            mainWindow.exitCreateVerticalCutMode();
            repaint();
        } else if (controller.getMode() == Controller.Mode.CREATE_HORIZONTAL_CUT) {
            float clickY = (float) logicalPoint.getY();
            
            float distance = getSnappedValue(clickY) - controller.getSelectedCutDistance();
            
            createHorizontalCut(distance);

            mainWindow.exitCreateHorizontalCutMode();
            repaint();
        } else if (controller.getMode() == Controller.Mode.CREATE_L_SHAPED_CUT) {
            float clickX = (float) logicalPoint.getX();
            float clickY = (float) logicalPoint.getY();
            Coordinate clickCoordinate = new Coordinate(getSnappedValue(clickX), getSnappedValue(clickY));

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
            Coordinate clickCoordinate = new Coordinate(getSnappedValue(clickX), getSnappedValue(clickY));

            if (pendingReferenceCoordinate == null) {
                ReferenceCoordinate referenceCoordinate = controller.getReferenceCoordinateOfIntersection(clickCoordinate);
                if (referenceCoordinate != null) {
                    pendingReferenceCoordinate = referenceCoordinate;
                    System.out.println("Reference coordinate set. Click again to specify the second point.");
                } else {
                    System.out.println("Invalid reference coordinate. Click again.");
                }
            } else if (pendingSecondCoordinate == null) {
                pendingSecondCoordinate = clickCoordinate;
                System.out.println("Second point : " + clickCoordinate.getX() + "," + clickCoordinate.getY());
                System.out.println("Second point set. Click again to specify the third point.");
            }
            else {
                createRectangularCut(pendingReferenceCoordinate, pendingSecondCoordinate, clickCoordinate);
                resetReferenceCoordinate();
                mainWindow.exitCreateLShapedCutMode();
                repaint();
            }
        }else if (controller.getMode() == Controller.Mode.CREATE_NO_CUT_ZONE) {
            System.out.println("Clic détecté en mode 'Zone Interdite'");
            float clickX = (float) logicalPoint.getX();
            float clickY = (float) logicalPoint.getY();
            Coordinate coordinate = new Coordinate(getSnappedValue(clickX), getSnappedValue(clickY));

            NoCutZoneDTO noCutZoneDTO = new NoCutZoneDTO(new Equipe45.domain.Utils.Dimension(150,150), coordinate);
            controller.addNoCutZone(noCutZoneDTO);

            mainWindow.exitCreateNoCutZoneMode();
            repaint();
        }
        else if(controller.getMode() == Controller.Mode.MODIFY_REFERENCE) {
            float clickX = (float) logicalPoint.getX();
            float clickY = (float) logicalPoint.getY();
            Coordinate clickCoordinate = new Coordinate(getSnappedValue(clickX), getSnappedValue(clickY));

   
            ReferenceCoordinate referenceCoordinate = controller.getReferenceCoordinateOfIntersection(clickCoordinate);
            if (referenceCoordinate != null) {
                this.modifyIrregularCutReference(referenceCoordinate);
                mainWindow.exitCreateLShapedCutMode();
                repaint();
                } else {
                    System.out.println("Invalid reference coordinate. Click again.");
                }
        }
        repaint();
    }

    public void resetReferenceCoordinate() {
        pendingReferenceCoordinate = null;
        pendingSecondCoordinate = null;
    }
    
    public void createDistanceVerticalCut(String text) {
        Controller controller = mainWindow.getController();
        
        UUID referenceCut = selectedCutId != null ? selectedCutId : controller.getInitialCutVerticalId();

        ToolDTO selectedToolDTO = controller.getSelectedTool();
        float defaultDepth = controller.getCnc().getPanel().getWidth() + 0.5f;
        
        try {
            float distance = controller.getSelectedUnit().toMillimetersFloat(text);
            ParallelCutDTO newCutDTO = new ParallelCutDTO(
                    UUID.randomUUID(),
                    defaultDepth,
                    selectedToolDTO,
                    referenceCut,
                    Math.round(distance)
            );
            controller.addNewCut(newCutDTO);
            mainWindow.updateCutHistoryTable();
            repaint();
        } catch (NumberFormatException e) {}
    }

    private void createVerticalCut(float distance) {
        if(selectedCutId == null) {
            System.out.println("Selectionner une bordure avant d'ajouter une coupe");
            return;
        }
        
        Controller controller = mainWindow.getController();

        ToolDTO selectedToolDTO = controller.getSelectedTool();
        float defaultDepth = controller.getCnc().getPanel().getWidth() + 0.5f;

        ParallelCutDTO newCutDTO = new ParallelCutDTO(
                UUID.randomUUID(),
                defaultDepth,
                selectedToolDTO,
                selectedCutId,
                Math.round(distance)
        );

        controller.addNewCut(newCutDTO);
        mainWindow.updateCutHistoryTable();
        repaint();
    }
    
    public void createDistanceHorizontalCut(String text) {
        Controller controller = mainWindow.getController();
        
        UUID referenceCut = selectedCutId != null ? selectedCutId : controller.getInitialCutHorizontalId();

        ToolDTO selectedToolDTO = controller.getSelectedTool();
        float defaultDepth = controller.getCnc().getPanel().getWidth() + 0.5f;
        
        try {
            float distance = controller.getSelectedUnit().toMillimetersFloat(text);
            ParallelCutDTO newCutDTO = new ParallelCutDTO(
                    UUID.randomUUID(),
                    defaultDepth,
                    selectedToolDTO,
                    referenceCut,
                    Math.round(distance)
            );
            controller.addNewCut(newCutDTO);
            mainWindow.updateCutHistoryTable();
            repaint();

        } catch (NumberFormatException e) {}
    }

    private void createHorizontalCut(float distance) {
        if(selectedCutId == null) {
            System.out.println("Selectionner une bordure avant d'ajouter une coupe");
            return;
        }
                
        Controller controller = mainWindow.getController();

        ToolDTO selectedToolDTO = controller.getSelectedTool();
        float defaultDepth = controller.getCnc().getPanel().getWidth() + 0.5f;


        ParallelCutDTO newCutDTO = new ParallelCutDTO(
                UUID.randomUUID(),
                defaultDepth,
                selectedToolDTO,
                selectedCutId,
                Math.round(distance)
        );
        controller.addNewCut(newCutDTO);

        mainWindow.updateCutHistoryTable();
        repaint();
    }

    private void createLShapedCut(ReferenceCoordinate reference, Coordinate intersection) {
        Controller controller = mainWindow.getController();

        ToolDTO selectedToolDTO = controller.getSelectedTool();
        float defaultDepth = controller.getCnc().getPanel().getWidth() + 0.5f;

        LShapedCutDTO newCutDTO = new LShapedCutDTO(
                UUID.randomUUID(),
                defaultDepth,
                selectedToolDTO,
                reference,
                intersection
        );

        controller.addNewCut(newCutDTO);

        mainWindow.updateCutHistoryTable();
        repaint();
    }
    
    private void modifyIrregularCutReference(ReferenceCoordinate reference) {
        Controller controller = mainWindow.getController();

        controller.ModifyReferenceAlone(reference);

        mainWindow.updateCutHistoryTable();
        repaint();
    }

    private void createRectangularCut(ReferenceCoordinate reference, Coordinate intersection, Coordinate corner) {
        Controller controller = mainWindow.getController();

        ToolDTO selectedToolDTO = controller.getSelectedTool();
        float defaultDepth = controller.getCnc().getPanel().getWidth() + 0.5f;

        RectangularCutDTO newCutDTO = new RectangularCutDTO(
                UUID.randomUUID(),
                defaultDepth,
                selectedToolDTO,
                reference,
                intersection,
                corner
        );

        controller.addNewCut(newCutDTO);
        mainWindow.updateCutHistoryTable();
        repaint();
    }

    public void createRecut(String dimensionx, String dimensionY) {
        Controller controller = mainWindow.getController();
        Equipe45.domain.Utils.Dimension dimension = new Equipe45.domain.Utils.Dimension(controller.getSelectedUnit().toMillimeters(dimensionx), controller.getSelectedUnit().toMillimeters(dimensionY));

        ToolDTO selectedToolDTO = controller.getSelectedTool();
        float defaultDepth = controller.getCnc().getPanel().getWidth() + 0.5f;

        ReCutDTO newCutDTO = new ReCutDTO(
                UUID.randomUUID(),
                defaultDepth,
                selectedToolDTO,
                dimension,
                controller.getPanel()
        );

        controller.addNewCut(newCutDTO);
        repaint();
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
    
    private float getSnappedValue(float value){
        Controller controller = mainWindow.getController();
        if(controller.getGridSize() == 0)
            return value;
        return Math.round(value / controller.getGridSize()) * controller.getGridSize();
    }
    
    private void updateSelectedCut(CutDTO cut){
        if(cut != null){
            selectedCutId = cut.getId();
            System.out.println("Coupe sélectionnée : " + selectedCutId);
            mainWindow.updateCutUUID(selectedCutId);
            mainWindow.updateUUIDTool(mainWindow.getController().getSelectedCutTool());
            mainWindow.displayUUID();
            if (cut instanceof ParallelCutDTO parallelCutDTO) {
                mainWindow.updateCutReferenceInformations(parallelCutDTO.referenceID);
                mainWindow.updateCutDistanceInformations(parallelCutDTO.distance);
                mainWindow.displayRegular();
            }
            else if (cut instanceof LShapedCutDTO lShapedCutDTO) {
               /* mainWindow.updateCutReferenceCoordinateInformations(lShapedCutDTO.reference.x, lShapedCutDTO.reference.y);
                mainWindow.updateCutIntersectionInformations(lShapedCutDTO.intersection.x, lShapedCutDTO.intersection.y);*/
                mainWindow.displayIrregular();
            }
            else if (cut instanceof RectangularCutDTO rectangularCutDTO) {
               /* mainWindow.updateCutReferenceCoordinateInformations(rectangularCutDTO.reference.x, rectangularCutDTO.reference.y);
                mainWindow.updateCutIntersectionInformations(rectangularCutDTO.intersection.x, rectangularCutDTO.intersection.y);
                mainWindow.updateCutCornerInformations(rectangularCutDTO.corner.x, rectangularCutDTO.corner.y);*/
                mainWindow.displayRectangular();
            }
            else if (cut instanceof BorderCutDTO) {
                mainWindow.hideAll();
            }
            mainWindow.updateCutHistoryTable();
            repaint();
        } else {
            selectedCutId = null;
            mainWindow.hideUUID();
            mainWindow.hideAll();
        }
    }
}
