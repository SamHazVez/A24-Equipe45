package Equipe45.gui;

import java.awt.Color;
import java.awt.Cursor;
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
import Equipe45.domain.MeasurementUnit;
import Equipe45.domain.NoCutZone;
import Equipe45.domain.ParallelCut;
import Equipe45.domain.Utils.Coordinate;
import Equipe45.domain.Utils.CutType;
import Equipe45.domain.Utils.ReferenceCoordinate;

public class DrawingPanel extends JPanel implements Serializable {
    private Dimension initialDimension;
    private MainWindow mainWindow;
    private double zoomFactor = 1.0;
    private AffineTransform transform = new AffineTransform();
    private ReferenceCoordinate pendingReferenceCoordinate = null;
    private Coordinate pendingSecondCoordinate = null;
    private UUID selectedCutId;
    private UUID selectedNoCutZoneId;
    private boolean draggingNoCutZone = false;
    private NoCutZone selectedNoCutZoneForDragging = null;
    private Point2D.Double dragOffset = null;
    private boolean draggingParallelCut = false;
    private ParallelCut selectedParallelCutForDragging = null;
    private double initialMousePosition = 0.0;
    private float initialDistance = 0;

    private Point2D initZoomPoint;

    
    public DrawingPanel()
    {
    }
    
    public DrawingPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;

        int width = 500;
        int height = 400;
        setPreferredSize(new Dimension(width, height));
        setVisible(true);

        initialDimension = new Dimension(width, height);

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

            @Override
            public void mousePressed(MouseEvent e) {
                handleMousePress(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                handleMouseRelease(e);
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                handleMouseDrag(e);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                handleMouseMove(e);
            }
        });

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
            CutDTO selectedCut = controller.handleCutClick(logicalPoint.x, logicalPoint.y);
            if (selectedCut != null) {
                updateSelectedCut(selectedCut);
                // Désélectionner la zone interdite si une coupe est sélectionnée
                updateSelectedNoCutZone(null);
            } else {
                // Tenter de sélectionner une zone interdite
                NoCutZone selectedZone = controller.handleNoCutZoneClick(logicalPoint.x, logicalPoint.y);
                if (selectedZone != null) {
                    updateSelectedNoCutZone(selectedZone);
                    // Désélectionner la coupe si une zone interdite est sélectionnée
                    updateSelectedCut(null);
                } else {
                    // Désélectionner tout si rien n'est sélectionné
                    updateSelectedCut(null);
                    updateSelectedNoCutZone(null);
                }
            }
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

    private void handleMousePress(MouseEvent e) {
        Controller controller = mainWindow.getController();
        Point2D.Double logicalPoint = getLogicalPoint(e.getPoint());
        if (logicalPoint == null) {
            return;
        }

        if (controller.getMode() == Controller.Mode.IDLE) {
            // Vérifier si une ParallelCut est sélectionnée pour commencer le drag
            CutDTO selectedCutDTO = controller.handleCutClick(logicalPoint.x, logicalPoint.y);
            if (selectedCutDTO instanceof ParallelCutDTO) {
                // Commencer le drag
                draggingParallelCut = true;
                selectedParallelCutForDragging = (ParallelCut) controller.getCutById(selectedCutDTO.getId());
                if (selectedParallelCutForDragging != null) {
                    initialDistance = selectedParallelCutForDragging.getDistance();
                    if (selectedParallelCutForDragging.getType() == CutType.PARALLEL_HORIZONTAL) {
                        initialMousePosition = logicalPoint.y;
                    } else { // PARALLEL_VERTICAL
                        initialMousePosition = logicalPoint.x;
                    }
                    System.out.println("Started dragging ParallelCut: " + selectedParallelCutForDragging.getId());
                }
            }

            // Vérifier si une zone interdite est sélectionnée pour commencer le drag
            NoCutZone selectedZone = controller.handleNoCutZoneClick(logicalPoint.x, logicalPoint.y);
            if (selectedZone != null) {
                updateSelectedNoCutZone(selectedZone);
                // Préparer pour le déplacement de la zone interdite
                draggingNoCutZone = true;
                selectedNoCutZoneForDragging = selectedZone;
                dragOffset = new Point2D.Double(
                        logicalPoint.x - selectedZone.getCoordinate().getX(),
                        logicalPoint.y - selectedZone.getCoordinate().getY()
                );
                System.out.println("Selected NoCutZone for dragging: " + selectedZone.getId());
                System.out.println("Drag Offset: X=" + dragOffset.x + ", Y=" + dragOffset.y);
                repaint();
            }
        }
    }

    private void handleMouseDrag(MouseEvent e) {
        Controller controller = mainWindow.getController();
        Point2D.Double logicalPoint = getLogicalPoint(e.getPoint());
        if (logicalPoint == null) {
            return;
        }

        // Gestion du drag pour ParallelCut
        if (draggingParallelCut && selectedParallelCutForDragging != null) {
            float newDistance = initialDistance;
            if (selectedParallelCutForDragging.getType() == CutType.PARALLEL_HORIZONTAL) {
                float deltaY = (float) (logicalPoint.y - initialMousePosition);
                newDistance = initialDistance + Math.round(deltaY);
            } else { // PARALLEL_VERTICAL
                float deltaX = (float) (logicalPoint.x - initialMousePosition);
                newDistance = initialDistance + Math.round(deltaX);
            }

            // Contrainte à des valeurs minimales et maximales
            newDistance = Math.max(0, newDistance); // La distance ne peut pas être négative
            newDistance = Math.min(newDistance, controller.getPanelWidth()); // Ne dépasse pas la largeur du panneau

            // Mettre à jour la distance via le Controller
            boolean updateSuccess = controller.updateParallelCutDistance(selectedParallelCutForDragging.getId(), (int) newDistance);

            if (updateSuccess) {
                System.out.println("Updated ParallelCut ID: " + selectedParallelCutForDragging.getId() + " to new distance: " + newDistance);
                repaint();
            } else {
                System.out.println("Failed to update ParallelCut ID: " + selectedParallelCutForDragging.getId());
            }

            return;
        }

        // Gestion du drag pour NoCutZone (existant)
        if (draggingNoCutZone && selectedNoCutZoneForDragging != null) {
            float newX = (float) (logicalPoint.x - dragOffset.x);
            float newY = (float) (logicalPoint.y - dragOffset.y);

            // Appliquer l'ancrage à la grille si nécessaire
            newX = getSnappedValue(newX);
            newY = getSnappedValue(newY);

            // Contrainte aux bords du panneau
            newX = Math.max(0, Math.min(newX, controller.getPanelWidth() - selectedNoCutZoneForDragging.getDimension().getWidth()));
            newY = Math.max(0, Math.min(newY, controller.getPanelHeight() - selectedNoCutZoneForDragging.getDimension().getHeight()));

            // Afficher les nouvelles coordonnées pour débogage
            System.out.println("Dragging NoCutZone ID: " + selectedNoCutZoneForDragging.getId());
            System.out.println("New X: " + newX + ", New Y: " + newY);

            // Mettre à jour les coordonnées de la zone interdite
            controller.updateNoCutZoneCoordinate(selectedNoCutZoneForDragging.getId(), newX, newY);
            controller.setSelectedNoCutZoneId(selectedNoCutZoneForDragging.getId());
            repaint();
        }
    }


    private void handleMouseRelease(MouseEvent e) {
        // Finalisation du drag pour ParallelCut
        if (draggingParallelCut) {
            draggingParallelCut = false;
            selectedParallelCutForDragging = null;
            initialMousePosition = 0.0;
            initialDistance = 0;
            System.out.println("Stopped dragging ParallelCut.");
            repaint();
        }

        // Finalisation du drag pour NoCutZone (existant)
        if (draggingNoCutZone) {
            draggingNoCutZone = false;
            selectedNoCutZoneForDragging = null;
            dragOffset = null;
            mainWindow.getController().setMode(Controller.Mode.IDLE);
            System.out.println("Stopped dragging NoCutZone.");
            repaint();
        }
    }

    private void handleMouseMove(MouseEvent e) {
        Point2D.Double logicalPoint = getLogicalPoint(e.getPoint());
        if (logicalPoint != null) {
            NoCutZone zone = mainWindow.getController().handleNoCutZoneClick(logicalPoint.x, logicalPoint.y);
            if (zone != null) {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
        }
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

    public void setBorderCut(String dimensionx, String dimensionY) {
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
        if(controller.getGridSize() == 0 || !controller.gribSnap)
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
                if(mainWindow.getController().getSelectedUnit() == MeasurementUnit.MILLIMETER) {
                    mainWindow.updateCutDistanceInformations(parallelCutDTO.distance);
                }
                else{
                    mainWindow.updateCutDistanceInformations(MeasurementUnit.MILLIMETER.toInchesInt(parallelCutDTO.distance));
                }
                
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
    private void updateSelectedNoCutZone(NoCutZone noCutZone) {
        if (noCutZone != null) {
            selectedNoCutZoneId = noCutZone.getId();
            System.out.println("Zone interdite sélectionnée : " + selectedNoCutZoneId);
            mainWindow.updateNoCutZoneUUID(selectedNoCutZoneId);
            mainWindow.getController().deselectCut();
            mainWindow.updateUUIDTool("Aucun outil sélectionné");
            System.out.println("Aucun outil de coupe sélectionné à mettre à jour.");
            mainWindow.displayNoCutZoneUUID();
            mainWindow.displayNoCutZoneModificationPanel();

        } else {
            selectedNoCutZoneId = null;
            mainWindow.HideNoCutZoneUUID();
            mainWindow.hideNoCutZoneModificationPanel();
        }
    }

}
