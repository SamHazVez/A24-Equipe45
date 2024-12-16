/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Equipe45.gui;

import Equipe45.domain.Controller;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Equipe45.domain.DTO.*;
import Equipe45.domain.MeasurementUnit;
import Equipe45.domain.NoCutZone;
import Equipe45.domain.Panel;
import Equipe45.domain.Utils.CutType;
import Equipe45.domain.Utils.Dimension;
import java.util.UUID;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *
 * @author mat18
 */
public class MainWindow extends javax.swing.JFrame {
    private JTable cutHistoryTable;
    private DefaultTableModel cutHistoryTableModel;

    private Controller controller;
    
    private static final String IMPERIAL_UNITE = "mm";
    private static final String METRIQUE_UNITE = "po";

    public Controller getController() {
        return controller;
    }

    private JButton[] toolButtons;

    public MainWindow() {
        controller = new Controller();
        initComponents();
        InitializeTools();
        initializeToolButtons();
        initializeSelectedToolLabels();
        initializeCutHistoryTable();
        InitializeCustomEvents();
        closeAllSousMenu();
        S_outil.setVisible(true);
        newDrawingPanel.setVisible(false);
        deselectCut();
        Zone_I.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if (Zone_I.isSelected()) {
                    controller.setMode(Controller.Mode.CREATE_NO_CUT_ZONE);
                    drawingPanel1.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                    System.out.println("Mode: Create No-Cut Zone");
                } else {
                    controller.setMode(Controller.Mode.IDLE);
                    drawingPanel1.setCursor(Cursor.getDefaultCursor());
                    System.out.println("Mode: Idle");
                }
            }
        });
        newDrawingPanel.addHierarchyListener(new java.awt.event.HierarchyListener() {
            public void hierarchyChanged(java.awt.event.HierarchyEvent evt) {
                if ((evt.getChangeFlags() & java.awt.event.HierarchyEvent.SHOWING_CHANGED) != 0) {
                    if (newDrawingPanel.isVisible()) {
                        updateRadioButtonSelection();
                    }
                }
            }
        });
    }

    private void updateAllLabel() {
        if (controller.getSelectedUnit() == MeasurementUnit.INCH) {
            CR_Label_cm.setText(IMPERIAL_UNITE);
        }
        else {
            CR_Label_cm.setText(METRIQUE_UNITE);
        }
    }
    

    private void updateRadioButtonSelection() {
        ndp_metrique_radio.setSelected(false);
        ndp_imperial_radio.setSelected(false);

        if (controller.getSelectedUnit() == MeasurementUnit.MILLIMETER) {
            ndp_metrique_radio.setSelected(true);
        } else if (controller.getSelectedUnit() == MeasurementUnit.INCH) {
            ndp_imperial_radio.setSelected(true);
        }
    }


    private void InitializeTools() {
        initializeToolButtons();
        initializeSelectedToolLabels();
    }

    private void InitializeCustomEvents()
    {
        addManualCutEvent();
        addLShapedCutEvent();
        addRectangularCutEvent();
        addDeleteToolButtonEvent();
        addNewToolButtonEvent();
        addUndoRedoEventsButtons();
    }
    
    private void addManualCutEvent(){
        CR_Coupe_M.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if (CR_Coupe_M.isSelected() && (controller.getSelectedCutType() == CutType.BORDER_VERTICAL||(controller.getSelectedCutType() == CutType.PARALLEL_VERTICAL) )) {
                    controller.setMode(Controller.Mode.CREATE_VERTICAL_CUT);
                    drawingPanel1.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                    System.out.println("Mode: Create Vertical Cut");
                } else if (CR_Coupe_M.isSelected() && (controller.getSelectedCutType() == CutType.BORDER_HORIZONTAL||controller.getSelectedCutType() == CutType.PARALLEL_HORIZONTAL)) {
                    controller.setMode(Controller.Mode.CREATE_HORIZONTAL_CUT);
                    drawingPanel1.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                    System.out.println("Mode: Create Horizontal Cut");
                } else {
                    controller.setMode(Controller.Mode.IDLE);
                    drawingPanel1.setCursor(Cursor.getDefaultCursor());
                    System.out.println("Mode: Idle");
                }
            }
        });
    }



    private String validateToolName(String toolName) {
        if (toolName == null || toolName.trim().isEmpty()) {
            return "Le nom ne peut pas être vide ou seulement des espaces.";
        } else if (toolName.length() > 12) {
            return "Le nom ne peut pas dépasser 12 caractères.";
        } else if (!toolName.matches("[A-Za-z0-9 ]+")) {
            return "Le nom ne peut contenir que des lettres, des chiffres et des espaces.";
        }
        return "";
    }

    private String validateToolWidth(float toolWidth) {
        try {
            if (toolWidth <= 0 || toolWidth > 20) {
                return "La largeur doit être un nombre positif inférieur ou égal à 20mm ou 0,787402 pouces.";
            }
        } catch (NumberFormatException ex) {
            return "La largeur doit être un nombre valide.";
        }
        return "";
    }

    private String validateToolDepth(float toolDepth) {
        try {
            if (toolDepth <= 0 || toolDepth > 20) {
                return "La profondeur doit être un nombre positif inférieur ou égal à 20mm ou 0,787402 pouces.";
            }
        } catch (NumberFormatException ex) {
            return "La profondeur doit être un nombre valide.";
        }
        return "";
    }


    private void addNewToolButtonEvent() {
        addNewToolButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String toolName = addNewToolNameTextField.getText();
                String toolWidthStr = addNewToolWidthTextField.getText();
                String toolDepthStr = addNewToolDepthTextField.getText();
                StringBuilder errorMessage = new StringBuilder();

                String nameError = validateToolName(toolName);
                if (!nameError.isEmpty()) {
                    errorMessage.append(nameError).append("\n");
                }

                String widthError = validateToolWidth(controller.getSelectedUnit().toMillimetersFloat(toolWidthStr));
                if (!widthError.isEmpty()) {
                    errorMessage.append(widthError).append("\n");
                }

                // Validate tool depth
                String depthError = validateToolDepth(controller.getSelectedUnit().toMillimetersFloat(toolDepthStr));
                if (!depthError.isEmpty()) {
                    errorMessage.append(depthError).append("\n");
                }

                if (controller.getTools().size() >= 30) {
                    errorMessage.append("Le nombre maximum d'outils (30) a été atteint.\n");
                }

                if (errorMessage.length() > 0) {
                    JOptionPane.showMessageDialog(MainWindow.this,
                            errorMessage.toString(),
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    float toolWidth = controller.getSelectedUnit().toMillimetersFloat(toolWidthStr);
                    float toolDepth = controller.getSelectedUnit().toMillimetersFloat(toolDepthStr);

                    ToolDTO newTool = new ToolDTO(toolName, toolWidth, toolDepth);
                    controller.AddTool(newTool);

                    initializeToolButtons();
                    initializeSelectedToolLabels();

                    addNewToolNameTextField.setText("");
                    addNewToolWidthTextField.setText("");
                    addNewToolDepthTextField.setText("");
                }
            }
        });
    }

    private void addDeleteToolButtonEvent(){
        deleteSelectedTool.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean success = controller.deleteSelectedTool();
                if (!success) {
                    JOptionPane.showMessageDialog(MainWindow.this,
                            "Vous ne pouvez pas supprimer l'outil par défaut.",
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                }
                initializeToolButtons();
                initializeSelectedToolLabels();
            }
        });

    }


    private void initializeSelectedToolLabels() {
        ToolDTO selectedTool = controller.getSelectedTool();
        if (selectedTool != null) {
            selectedTool1.setText(selectedTool.getName());
            selectedTool2.setText(selectedTool.getName());
            selectedToolName.setText(selectedTool.getName());
            if (controller.getSelectedUnit() == MeasurementUnit.INCH) {
                selectedToolWidth.setText(
                        BigDecimal.valueOf(controller.getSelectedUnit().toInches(selectedTool.getCutWidth()))
                                .setScale(2, RoundingMode.HALF_UP)
                                .toPlainString()
                );

                selectedToolDepthLabel.setText(
                        BigDecimal.valueOf(controller.getSelectedUnit().toInches(selectedTool.getCutDepth()))
                                .setScale(2, RoundingMode.HALF_UP)
                                .toPlainString()
                );
            }
            else {
                selectedToolWidth.setText(String.valueOf(selectedTool.getCutWidth()));
                selectedToolDepthLabel.setText(String.valueOf(selectedTool.getCutDepth()));
            }

        } else {
            selectedTool1.setText("Aucun outil sélectionné");
            selectedTool2.setText("Aucun outil sélectionné");
            selectedToolName.setText("");
            selectedToolWidth.setText("");
            selectedToolDepthLabel.setText("");
        }
    }


    private void addLShapedCutEvent(){
        CI_Coupe_L.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if (CI_Coupe_L.isSelected()) {
                    drawingPanel1.resetReferenceCoordinate();
                    controller.setMode(Controller.Mode.CREATE_L_SHAPED_CUT);
                    drawingPanel1.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                    System.out.println("Mode: Create L SHAPED Cut");
                } else {
                    controller.setMode(Controller.Mode.IDLE);
                    drawingPanel1.setCursor(Cursor.getDefaultCursor());
                    System.out.println("Mode: Idle");
                }
            }
        });
    }

    private void addRectangularCutEvent(){
        CI_Coupe_Rec.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if (CI_Coupe_Rec.isSelected()) {
                    drawingPanel1.resetReferenceCoordinate();
                    controller.setMode(Controller.Mode.CREATE_RECTANGULAR_CUT);
                    drawingPanel1.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                    System.out.println("Mode: Create Rectangular Cut");
                } else {
                    controller.setMode(Controller.Mode.IDLE);
                    drawingPanel1.setCursor(Cursor.getDefaultCursor());
                    System.out.println("Mode: Idle");
                }
            }
        });
    }


    public void exitCreateVerticalCutMode() {
        controller.setMode(Controller.Mode.IDLE);
        drawingPanel1.setCursor(Cursor.getDefaultCursor());
        CR_Coupe_V.setSelected(false);
        System.out.println("Mode: Idle");
    }

    public void exitCreateHorizontalCutMode() {
        controller.setMode(Controller.Mode.IDLE);
        drawingPanel1.setCursor(Cursor.getDefaultCursor());
        CR_Coupe_H.setSelected(false);
        System.out.println("Mode: Idle");
    }

    public void exitCreateLShapedCutMode() {
        controller.setMode(Controller.Mode.IDLE);
        drawingPanel1.setCursor(Cursor.getDefaultCursor());
        CI_Coupe_L.setSelected(false);
        System.out.println("Mode: Idle");
    }
    public void exitCreateNoCutZoneMode(){
        Zone_I.setSelected(false);
        controller.setMode(Controller.Mode.IDLE);
        drawingPanel1.setCursor(Cursor.getDefaultCursor());
        System.out.println("Mode: Idle");
    }
    public void displayNoCutZoneInfo(NoCutZone noCutZone) {
        if (noCutZone != null) {
            NoCutZoneModificationPanel.setVisible(true);
            // Remplir les champs avec les dimensions actuelles
            ModificationNoCutZoneXField.setText(String.valueOf(noCutZone.getDimension().getWidth()));
            ModificationNoCutZoneYField.setText(String.valueOf(noCutZone.getDimension().getHeight()));
        } else {
            NoCutZoneModificationPanel.setVisible(false);
            ModificationNoCutZoneXField.setText("");
            ModificationNoCutZoneYField.setText("");
        }
        revalidate();
        repaint();
    }



    private void initializeToolButtons() {
        toolButtons = new JButton[]{toolButton1, toolButton2, toolButton3, toolButton4, toolButton5,
                toolButton6, toolButton7, toolButton8, toolButton9, toolButton10, toolButton11, toolButton12};
        List<ToolDTO> tools = controller.getTools();

        for (JButton button : toolButtons) {
            for (ActionListener al : button.getActionListeners()) {
                button.removeActionListener(al);
            }
        }

        for (int i = 0; i < toolButtons.length; i++) {
            if (i < tools.size()) {
                toolButtons[i].setText(tools.get(i).getName());
                toolButtons[i].setVisible(true);
                final int index = i;
                toolButtons[i].addActionListener(e -> {
                    controller.selectToolByIndex(index);
                    System.out.println("Selected Tool: " + tools.get(index).getName());
                    selectedTool1.setText(tools.get(index).getName());
                    selectedTool2.setText(tools.get(index).getName());
                    selectedToolName.setText(tools.get(index).getName());
                    if(this.controller.getSelectedUnit() == MeasurementUnit.INCH)
                    {
                        selectedToolWidth.setText(
                                BigDecimal.valueOf(controller.getSelectedUnit().toInches(tools.get(index).getCutWidth()))
                                        .setScale(2, RoundingMode.HALF_UP)
                                        .toPlainString()
                        );

                        selectedToolDepthLabel.setText(
                                BigDecimal.valueOf(controller.getSelectedUnit().toInches(tools.get(index).getCutDepth()))
                                        .setScale(2, RoundingMode.HALF_UP)
                                        .toPlainString()
                        );

                    }
                    else {
                        selectedToolWidth.setText(String.valueOf(tools.get(index).getCutWidth()));
                        selectedToolDepthLabel.setText(String.valueOf(tools.get(index).getCutDepth()));
                    }
                });
            } else {
                toolButtons[i].setVisible(false);
            }
        }
        S_outil.revalidate();
        S_outil.repaint();
    }

    public void updateCutUUID(UUID uuid) {
        CutSelfUUID.setText((uuid == null) ? "" : String.valueOf(uuid));
    }
    
    public void updateUUIDTool(String name) {
        OutilUUID.setText((name == null) ? "" : String.valueOf(name));
    }
    
    public void updateCutReferenceInformations(UUID uuid) {
        UUIDText.setText((uuid == null) ? "" : String.valueOf(uuid));
    }
    
    public void updateCutDistanceInformations(Integer distance) {
        DistanceText.setText((distance == null) ? "" : String.valueOf(distance));
    }

    public void displayUUID(){
        CutSelfUUID.setVisible(true);
        CutSelfUUIDLabel.setVisible(true);
    }
    
    public void displayRegular(){
        hideAll();
        ReferencePanel.setVisible(true);
        DistancePanel.setVisible(true);
        DeleteCutButton.setVisible(true);
    }
    
    public void displayIrregular(){
        hideAll();
        //ReferenceCoordinatePanel.setVisible(true);
        IntersectionPanel.setVisible(true);
        DeleteCutButton.setVisible(true);
        this.ModifyDimensionPanel.setVisible(true);
    }

    public void displayRectangular() {
        displayIrregular();
        ModifyDistanceFromRef.setVisible(true);
    }
    
    public void displayCorner(){
        hideAll();
        //ReferenceCoordinatePanel.setVisible(true);
        IntersectionPanel.setVisible(true);
        CornerPanel.setVisible(true);
        DeleteCutButton.setVisible(true);
    }
    
    public void displayBorder(){
        hideAll();
    }
    
    public void hideUUID(){
        CutSelfUUID.setText("Aucune");
        //CutSelfUUID.setVisible(false);
        //CutSelfUUIDLabel.setVisible(false);
    }
    public void HideNoCutZoneUUID(){
        SelectedZoneUUID.setText("Aucune");
    }

    public void hideAll(){
        ReferencePanel.setVisible(false);
        DistancePanel.setVisible(false);
        ReferenceCoordinatePanel.setVisible(false);
        IntersectionPanel.setVisible(false);
        CornerPanel.setVisible(false);
        DeleteCutButton.setVisible(false);
        HideNoCutZoneUUID();
        ModifyDistanceFromRef.setVisible(false);
        this.ModifyDimensionPanel.setVisible(false);
        NoCutZoneModificationPanel.setVisible(false);
    }
    
    private void deselectCut() {
        hideUUID();
        hideAll();
    }


    private void initializeCutHistoryTable() {
        String[] columnNames = {"Type", "ID", "Attributes"};
        cutHistoryTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells non-editable
            }
        };
        cutHistoryTable = new JTable(cutHistoryTableModel);
        cutHistoryTable.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(cutHistoryTable);
        Historique_panel.setLayout(new BorderLayout());
        Historique_panel.add(scrollPane, BorderLayout.CENTER);
        updateCutHistoryTable();
    }

    public void updateCutHistoryTable() {
        cutHistoryTableModel.setRowCount(0);
        List<CutDTO> cuts = controller.getAllCuts();
        for (CutDTO cut : cuts) {
            String type = getCutType(cut);
            String id = cut.getId().toString();
            String attributes = getCutAttributesAsString(cut);
            cutHistoryTableModel.addRow(new Object[]{type, id, attributes});
        }
        Historique_panel.repaint();
    }

    private String getCutType(CutDTO cut) {
        if (cut instanceof ParallelCutDTO) {
            return "Parallel Cut";
        } else if (cut instanceof LShapedCutDTO) {
            return "L-Shaped Cut";
        } else if (cut instanceof RectangularCutDTO) {
            return "Rectangular Cut";
        } else if (cut instanceof BorderCutDTO) {
            return "Border Cut";
        } else {
            return "Unknown Cut";
        }
    }

    private String getCutAttributesAsString(CutDTO cut) {
        StringBuilder attributes = new StringBuilder();

        attributes.append("Depth: ").append(cut.getDepth()).append(", ");
        attributes.append("Tool: ").append(cut.getTool().getName()).append(", ");

        if (cut instanceof ParallelCutDTO pc) {
            attributes.append("Distance: ").append(pc.distance).append(", ");
            attributes.append("Reference ID: ").append(pc.referenceID);
        } else if (cut instanceof LShapedCutDTO lc) {
            attributes.append("Reference: ").append(lc.reference).append(", ");
            attributes.append("Intersection: ").append(lc.intersection);
        } else if (cut instanceof RectangularCutDTO rc) {
            attributes.append("Reference: ").append(rc.reference).append(", ");
            attributes.append("Corner: ").append(rc.corner);
        } else if (cut instanceof BorderCutDTO bc) {
            attributes.append("Origin: ").append(bc.getOrigin()).append(", ");
            attributes.append("Destination: ").append(bc.getDestination());
        }

        return attributes.toString();
    }

    private void addUndoRedoEventsButtons(){
        undoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.undo();
                updateCutHistoryTable();
                drawingPanel1.repaint();
            }
        });

        redoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.redo();
                updateCutHistoryTable();
                drawingPanel1.repaint();
            }
        });
    }

    public void updateNoCutZoneUUID(UUID uuid) {
        SelectedZoneUUID.setText(uuid.toString());
    }

    public void displayNoCutZoneUUID() {
        SelectedZoneUUID.setVisible(true);
    }
    public void displayNoCutZoneModificationPanel() {
        NoCutZoneModificationPanel.setVisible(true);
    }
    public void hideNoCutZoneModificationPanel() {
        NoCutZoneModificationPanel.setVisible(false);
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ndp_radio_groupe = new javax.swing.ButtonGroup();
        Sous_Option = new javax.swing.JPanel();
        S_Coupe_R = new javax.swing.JPanel();
        CR_Outil_s = new javax.swing.JLabel();
        S_CR_Titre = new javax.swing.JLabel();
        selectedTool1 = new javax.swing.JLabel();
        CR_Label_Distance = new javax.swing.JLabel();
        CR_Distance = new javax.swing.JTextField();
        CR_Label_cm = new javax.swing.JLabel();
        CR_Coupe_M = new javax.swing.JToggleButton();
        CR_Coupe_V = new javax.swing.JButton();
        CR_Coupe_H = new javax.swing.JButton();
        S_outil = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        toolButton1 = new javax.swing.JButton();
        toolButton2 = new javax.swing.JButton();
        toolButton3 = new javax.swing.JButton();
        toolButton4 = new javax.swing.JButton();
        toolButton5 = new javax.swing.JButton();
        toolButton6 = new javax.swing.JButton();
        toolButton7 = new javax.swing.JButton();
        toolButton8 = new javax.swing.JButton();
        toolButton9 = new javax.swing.JButton();
        toolButton10 = new javax.swing.JButton();
        toolButton11 = new javax.swing.JButton();
        toolButton12 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        selectedToolName = new javax.swing.JLabel();
        selectedToolWidth = new javax.swing.JLabel();
        deleteSelectedTool = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        addNewToolNameLabel = new javax.swing.JLabel();
        addNewToolNameTextField = new javax.swing.JTextField();
        addNewToolWidthLabel = new javax.swing.JLabel();
        addNewToolWidthTextField = new javax.swing.JTextField();
        addNewToolButton = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        selectedToolDepthLabel = new javax.swing.JLabel();
        addNewToolDepthLabel = new javax.swing.JLabel();
        addNewToolDepthTextField = new javax.swing.JTextField();
        S_Coupe_I = new javax.swing.JPanel();
        CI_Outil_s = new javax.swing.JLabel();
        CI_Coupe_Rec = new javax.swing.JToggleButton();
        CI_Coupe_L = new javax.swing.JToggleButton();
        S_CI_Titre = new javax.swing.JLabel();
        selectedTool2 = new javax.swing.JLabel();
        S_Grille = new javax.swing.JPanel();
        S_G_Titre = new javax.swing.JLabel();
        S_G_size = new javax.swing.JTextField();
        S_G_size_label = new javax.swing.JLabel();
        S_G_Confirmer = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JSeparator();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        S_Bordure = new javax.swing.JPanel();
        B_Longueur_L = new javax.swing.JLabel();
        B_Longueur_T = new javax.swing.JTextField();
        B_Largeur_L = new javax.swing.JLabel();
        B_Largeur_T = new javax.swing.JTextField();
        B_Ratio = new javax.swing.JToggleButton();
        B_Couper = new javax.swing.JButton();
        Option = new javax.swing.JPanel();
        Outils = new javax.swing.JButton();
        Coupe_R = new javax.swing.JButton();
        Coupe_I = new javax.swing.JButton();
        Bordure = new javax.swing.JButton();
        Zone_I = new javax.swing.JToggleButton();
        Historique_panel = new javax.swing.JPanel();
        Historique = new javax.swing.JPanel();
        Informations = new javax.swing.JPanel();
        ReferencePanel1 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        ReferencePanel = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        UUIDText = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        UUIDButton = new javax.swing.JButton();
        DistancePanel = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        DistanceText = new javax.swing.JTextField();
        DsitanceButton = new javax.swing.JButton();
        IntersectionPanel = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        IntersectionButton = new javax.swing.JButton();
        DeleteCutButton = new javax.swing.JButton();
        CornerPanel = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        CornerX = new javax.swing.JTextField();
        CornerY = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        CornerButton = new javax.swing.JButton();
        ReferenceCoordinatePanel = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        RefCoX = new javax.swing.JTextField();
        RefCoY = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        RefCoButton = new javax.swing.JButton();
        CutSelfUUID = new javax.swing.JTextField();
        CutSelfUUIDLabel = new javax.swing.JLabel();
        OutilUUID = new javax.swing.JLabel();
        SelectedZoneUUIDLabel = new javax.swing.JLabel();
        SelectedZoneUUID = new javax.swing.JTextField();
        ModifyDimensionPanel = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        DimensionCoX = new javax.swing.JTextField();
        DimensionCoY = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        RefCoButton1 = new javax.swing.JButton();
        ModifyDistanceFromRef = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        DistanceRefX1 = new javax.swing.JTextField();
        DistanceRefY1 = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        ModifyRefPosButton = new javax.swing.JButton();
        NoCutZoneModificationPanel = new javax.swing.JPanel();
        ModificationNoCutsZoneLabel = new javax.swing.JLabel();
        ModificationNoCutsZoneXLabel = new javax.swing.JLabel();
        ModificationNoCutZoneXField = new javax.swing.JTextField();
        RedimensionButtonNoCutZone = new javax.swing.JButton();
        ModificationNoCutsZoneYLabel = new javax.swing.JLabel();
        ModificationNoCutZoneYField = new javax.swing.JTextField();
        DeleteButtonNoCutZone = new javax.swing.JButton();
        jdrawingPanel = new javax.swing.JPanel();
        drawingPanel1 = new Equipe45.gui.DrawingPanel(this);
        newDrawingPanel = new javax.swing.JPanel();
        ndp_titre = new javax.swing.JLabel();
        ndp_longueur_label = new javax.swing.JLabel();
        ndp_largeur_label = new javax.swing.JLabel();
        ndp_longueur_input = new javax.swing.JTextField();
        ndp_largeur_input = new javax.swing.JTextField();
        ndp_metrique_radio = new javax.swing.JRadioButton();
        ndp_imperial_radio = new javax.swing.JRadioButton();
        ndp_confirmer = new javax.swing.JButton();
        ndp_largeur_unite = new javax.swing.JLabel();
        ndp_longueur_unite = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        CreatePanelMenuButton = new javax.swing.JMenuItem();
        SaveMenuButton = new javax.swing.JMenuItem();
        LoadMenuButton = new javax.swing.JMenuItem();
        ExportGCodeButton = new javax.swing.JMenuItem();
        EditMenu = new javax.swing.JMenu();
        CreateGrille = new javax.swing.JMenuItem();
        undoButton = new javax.swing.JMenuItem();
        redoButton = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Equipe45");
        setBackground(new java.awt.Color(0, 0, 0));

        Sous_Option.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        CR_Outil_s.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        CR_Outil_s.setText("Outil sélectionné : ");

        S_CR_Titre.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        S_CR_Titre.setText("Coupe Régulière :");

        selectedTool1.setText("selectedTool1");

        CR_Label_Distance.setText("Distance :");

        CR_Label_cm.setText("mm");

        CR_Coupe_M.setText("Ajouter manuellement");
        CR_Coupe_M.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CR_Coupe_MActionPerformed(evt);
            }
        });

        CR_Coupe_V.setText("Coupe Verticale");
        CR_Coupe_V.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CR_Coupe_VActionPerformed(evt);
            }
        });

        CR_Coupe_H.setText("Coupe Horizontale");
        CR_Coupe_H.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CR_Coupe_HActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout S_Coupe_RLayout = new javax.swing.GroupLayout(S_Coupe_R);
        S_Coupe_R.setLayout(S_Coupe_RLayout);
        S_Coupe_RLayout.setHorizontalGroup(
            S_Coupe_RLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(S_Coupe_RLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(S_Coupe_RLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(CR_Coupe_V, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(S_Coupe_RLayout.createSequentialGroup()
                        .addComponent(CR_Outil_s)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(selectedTool1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(CR_Coupe_M, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(S_Coupe_RLayout.createSequentialGroup()
                        .addGroup(S_Coupe_RLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(S_Coupe_RLayout.createSequentialGroup()
                                .addComponent(CR_Label_Distance, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(CR_Distance, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(CR_Label_cm, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(S_CR_Titre))
                        .addGap(0, 33, Short.MAX_VALUE))
                    .addComponent(CR_Coupe_H, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        S_Coupe_RLayout.setVerticalGroup(
            S_Coupe_RLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(S_Coupe_RLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(S_Coupe_RLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CR_Outil_s)
                    .addComponent(selectedTool1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(S_CR_Titre)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(S_Coupe_RLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CR_Label_Distance, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CR_Distance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CR_Label_cm))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(CR_Coupe_V)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(CR_Coupe_H)
                .addGap(18, 18, 18)
                .addComponent(CR_Coupe_M)
                .addContainerGap(144, Short.MAX_VALUE))
        );

        toolButton1.setText("Tool1");
        toolButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toolButton1ActionPerformed(evt);
            }
        });

        toolButton2.setText("Tool2");
        toolButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toolButton2ActionPerformed(evt);
            }
        });

        toolButton3.setText("Tool3");

        toolButton4.setText("Tool4");

        toolButton5.setText("Tool5");

        toolButton6.setText("Tool6");

        toolButton7.setText("Tool7");

        toolButton8.setText("Tool8");

        toolButton9.setText("Tool9");

        toolButton10.setText("Tool10");

        toolButton11.setText("Tool11");

        toolButton12.setText("Tool12");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(toolButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                    .addComponent(toolButton2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(toolButton3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(toolButton4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(toolButton5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(toolButton6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(toolButton7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(toolButton8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(toolButton9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(toolButton10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(toolButton11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(toolButton12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(toolButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(toolButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(toolButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(toolButton4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(toolButton5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(toolButton6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(toolButton7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(toolButton8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(toolButton9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(toolButton10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(toolButton11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(toolButton12)
                .addContainerGap(632, Short.MAX_VALUE))
        );

        jScrollPane1.setViewportView(jPanel1);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Nom :");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("Profondeur: ");

        selectedToolName.setText("selectedToolName");

        selectedToolWidth.setText("selectedToolWidth");

        deleteSelectedTool.setText("Supprimer");
        deleteSelectedTool.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteSelectedToolActionPerformed(evt);
            }
        });

        addNewToolNameLabel.setText("Nom du nouvel outil:");

        addNewToolNameTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addNewToolNameTextFieldActionPerformed(evt);
            }
        });

        addNewToolWidthLabel.setText("Largeur du nouvel outil: ");

        addNewToolWidthTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addNewToolWidthTextFieldActionPerformed(evt);
            }
        });

        addNewToolButton.setText("Ajouter");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setText("Largeur :");

        selectedToolDepthLabel.setText("selectedToolWidth");

        addNewToolDepthLabel.setText("Profondeur du nouvel outil: ");

        addNewToolDepthTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addNewToolDepthTextFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout S_outilLayout = new javax.swing.GroupLayout(S_outil);
        S_outil.setLayout(S_outilLayout);
        S_outilLayout.setHorizontalGroup(
            S_outilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(S_outilLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(S_outilLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(S_outilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(S_outilLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(9, 9, 9)
                        .addComponent(selectedToolName))
                    .addGroup(S_outilLayout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(10, 10, 10)
                        .addComponent(selectedToolWidth, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addComponent(deleteSelectedTool, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(S_outilLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel4)
                .addGap(4, 4, 4)
                .addComponent(selectedToolDepthLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(S_outilLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(addNewToolNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(addNewToolNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(S_outilLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(addNewToolWidthLabel)
                .addGap(22, 22, 22)
                .addComponent(addNewToolWidthTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(S_outilLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(addNewToolDepthLabel)
                .addGap(3, 3, 3)
                .addComponent(addNewToolDepthTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(S_outilLayout.createSequentialGroup()
                .addGap(160, 160, 160)
                .addComponent(addNewToolButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        S_outilLayout.setVerticalGroup(
            S_outilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(S_outilLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addGroup(S_outilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(S_outilLayout.createSequentialGroup()
                        .addGroup(S_outilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(selectedToolName, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(S_outilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(selectedToolWidth, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(deleteSelectedTool, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(S_outilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(selectedToolDepthLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addGroup(S_outilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(addNewToolNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addNewToolNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(S_outilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(addNewToolWidthLabel)
                    .addComponent(addNewToolWidthTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(S_outilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(addNewToolDepthLabel)
                    .addComponent(addNewToolDepthTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addComponent(addNewToolButton)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        CI_Outil_s.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        CI_Outil_s.setText("Outil sélectionné : ");

        CI_Coupe_Rec.setText("Coupe rectangulaire");
        CI_Coupe_Rec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CI_Coupe_RecActionPerformed(evt);
            }
        });

        CI_Coupe_L.setText("Coupe en L");
        CI_Coupe_L.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CI_Coupe_LActionPerformed(evt);
            }
        });

        S_CI_Titre.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        S_CI_Titre.setText("Coupe Irrégulière :");

        selectedTool2.setText("selectedTool2");

        javax.swing.GroupLayout S_Coupe_ILayout = new javax.swing.GroupLayout(S_Coupe_I);
        S_Coupe_I.setLayout(S_Coupe_ILayout);
        S_Coupe_ILayout.setHorizontalGroup(
            S_Coupe_ILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(S_Coupe_ILayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(S_Coupe_ILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(CI_Coupe_Rec, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(CI_Coupe_L, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(S_Coupe_ILayout.createSequentialGroup()
                        .addGroup(S_Coupe_ILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(S_CI_Titre)
                            .addGroup(S_Coupe_ILayout.createSequentialGroup()
                                .addComponent(CI_Outil_s)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(selectedTool2, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 35, Short.MAX_VALUE)))
                .addContainerGap())
        );
        S_Coupe_ILayout.setVerticalGroup(
            S_Coupe_ILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(S_Coupe_ILayout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(S_Coupe_ILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(CI_Outil_s)
                    .addComponent(selectedTool2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(S_CI_Titre)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(CI_Coupe_Rec)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(CI_Coupe_L)
                .addContainerGap(210, Short.MAX_VALUE))
        );

        S_G_Titre.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        S_G_Titre.setText("Grille :");

        S_G_size.setText("0");
        S_G_size.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                S_G_sizeActionPerformed(evt);
            }
        });

        S_G_size_label.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        S_G_size_label.setText("Grandeur :");

        S_G_Confirmer.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        S_G_Confirmer.setText("Confirmer");
        S_G_Confirmer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                S_G_ConfirmerActionPerformed(evt);
            }
        });

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setText("La grandeur donné, équiveau à une case.\n\nune Grandeur de 0 ==> désectiver Grille.");
        jScrollPane2.setViewportView(jTextArea1);

        javax.swing.GroupLayout S_GrilleLayout = new javax.swing.GroupLayout(S_Grille);
        S_Grille.setLayout(S_GrilleLayout);
        S_GrilleLayout.setHorizontalGroup(
            S_GrilleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator3)
            .addGroup(S_GrilleLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(S_GrilleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, S_GrilleLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(S_G_Confirmer))
                    .addComponent(S_G_size)
                    .addGroup(S_GrilleLayout.createSequentialGroup()
                        .addGroup(S_GrilleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(S_G_Titre)
                            .addComponent(S_G_size_label, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE))
                .addContainerGap())
        );
        S_GrilleLayout.setVerticalGroup(
            S_GrilleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(S_GrilleLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(S_G_Titre)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(S_G_size_label)
                .addGap(3, 3, 3)
                .addComponent(S_G_size, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(S_G_Confirmer)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(93, Short.MAX_VALUE))
        );

        B_Longueur_L.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        B_Longueur_L.setText("Longueur :");

        B_Longueur_T.setText("500");
        B_Longueur_T.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                B_Longueur_TActionPerformed(evt);
            }
        });

        B_Largeur_L.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        B_Largeur_L.setText("Largeur :");

        B_Largeur_T.setText("400");
        B_Largeur_T.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                B_Largeur_TActionPerformed(evt);
            }
        });

        B_Ratio.setText("Conserver ratio");

        B_Couper.setText("Couper");
        B_Couper.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                B_CouperActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout S_BordureLayout = new javax.swing.GroupLayout(S_Bordure);
        S_Bordure.setLayout(S_BordureLayout);
        S_BordureLayout.setHorizontalGroup(
            S_BordureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(S_BordureLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(S_BordureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(B_Longueur_T)
                    .addComponent(B_Largeur_T)
                    .addGroup(S_BordureLayout.createSequentialGroup()
                        .addGroup(S_BordureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(B_Longueur_L)
                            .addComponent(B_Largeur_L)
                            .addComponent(B_Ratio))
                        .addGap(0, 160, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, S_BordureLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(B_Couper)))
                .addContainerGap())
        );
        S_BordureLayout.setVerticalGroup(
            S_BordureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(S_BordureLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(B_Longueur_L)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(B_Longueur_T, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(B_Largeur_L)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(B_Largeur_T, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(B_Ratio)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(B_Couper)
                .addContainerGap(157, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout Sous_OptionLayout = new javax.swing.GroupLayout(Sous_Option);
        Sous_Option.setLayout(Sous_OptionLayout);
        Sous_OptionLayout.setHorizontalGroup(
            Sous_OptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(S_outil, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(Sous_OptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(S_Coupe_R, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(Sous_OptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(S_Coupe_I, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(Sous_OptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(S_Bordure, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(Sous_OptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(S_Grille, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        Sous_OptionLayout.setVerticalGroup(
            Sous_OptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Sous_OptionLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(S_outil, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(Sous_OptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(Sous_OptionLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(S_Coupe_R, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
            .addGroup(Sous_OptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(Sous_OptionLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(S_Coupe_I, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
            .addGroup(Sous_OptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(Sous_OptionLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(S_Bordure, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
            .addGroup(Sous_OptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(Sous_OptionLayout.createSequentialGroup()
                    .addGap(12, 12, 12)
                    .addComponent(S_Grille, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGap(12, 12, 12)))
        );

        Option.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        Outils.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        Outils.setText("Boite a outils");
        Outils.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        Outils.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OutilsActionPerformed(evt);
            }
        });

        Coupe_R.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        Coupe_R.setText("Coupe Régulière");
        Coupe_R.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        Coupe_R.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Coupe_RActionPerformed(evt);
            }
        });

        Coupe_I.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        Coupe_I.setText("Coupe Irrégulière");
        Coupe_I.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        Coupe_I.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Coupe_IActionPerformed(evt);
            }
        });

        Bordure.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        Bordure.setText("Ajuster Bordure");
        Bordure.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        Bordure.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BordureActionPerformed(evt);
            }
        });

        Zone_I.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        Zone_I.setText("Zone Interdite");
        Zone_I.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Zone_IActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout OptionLayout = new javax.swing.GroupLayout(Option);
        Option.setLayout(OptionLayout);
        OptionLayout.setHorizontalGroup(
            OptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, OptionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(OptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(Outils, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Coupe_R, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Coupe_I, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Bordure, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Zone_I, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        OptionLayout.setVerticalGroup(
            OptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(OptionLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Outils, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Coupe_R, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Coupe_I, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Bordure, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Zone_I)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        Historique_panel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Historique_panel.setDoubleBuffered(false);
        Historique_panel.setFocusable(false);

        javax.swing.GroupLayout HistoriqueLayout = new javax.swing.GroupLayout(Historique);
        Historique.setLayout(HistoriqueLayout);
        HistoriqueLayout.setHorizontalGroup(
            HistoriqueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        HistoriqueLayout.setVerticalGroup(
            HistoriqueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 111, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout Historique_panelLayout = new javax.swing.GroupLayout(Historique_panel);
        Historique_panel.setLayout(Historique_panelLayout);
        Historique_panelLayout.setHorizontalGroup(
            Historique_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Historique_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Historique, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        Historique_panelLayout.setVerticalGroup(
            Historique_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Historique_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Historique, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        Informations.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Informations.setName("Informations"); // NOI18N

        ReferencePanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel18.setText("Unité de mesure");

        jRadioButton1.setSelected(true);
        jRadioButton1.setText("Métrique");
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });

        jRadioButton2.setText("Impérial");
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ReferencePanel1Layout = new javax.swing.GroupLayout(ReferencePanel1);
        ReferencePanel1.setLayout(ReferencePanel1Layout);
        ReferencePanel1Layout.setHorizontalGroup(
            ReferencePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ReferencePanel1Layout.createSequentialGroup()
                .addGroup(ReferencePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ReferencePanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel18))
                    .addGroup(ReferencePanel1Layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(jRadioButton1)
                        .addGap(30, 30, 30)
                        .addComponent(jRadioButton2)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        ReferencePanel1Layout.setVerticalGroup(
            ReferencePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ReferencePanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel18)
                .addGap(18, 18, 18)
                .addGroup(ReferencePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton1)
                    .addComponent(jRadioButton2))
                .addGap(32, 32, 32))
        );

        ReferencePanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel9.setText("Référence de la coupe");

        jLabel10.setText("UUID");

        UUIDButton.setLabel("Changer la référence");
        UUIDButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UUIDButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ReferencePanelLayout = new javax.swing.GroupLayout(ReferencePanel);
        ReferencePanel.setLayout(ReferencePanelLayout);
        ReferencePanelLayout.setHorizontalGroup(
            ReferencePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ReferencePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ReferencePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ReferencePanelLayout.createSequentialGroup()
                        .addComponent(UUIDButton)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(ReferencePanelLayout.createSequentialGroup()
                        .addGroup(ReferencePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(UUIDText)
                            .addGroup(ReferencePanelLayout.createSequentialGroup()
                                .addGroup(ReferencePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel10))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())))
        );
        ReferencePanelLayout.setVerticalGroup(
            ReferencePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ReferencePanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(UUIDText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(UUIDButton)
                .addContainerGap())
        );

        DistancePanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel12.setText("Distance");

        DistanceText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DistanceTextActionPerformed(evt);
            }
        });

        DsitanceButton.setText("Redimensionner");
        DsitanceButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DsitanceButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout DistancePanelLayout = new javax.swing.GroupLayout(DistancePanel);
        DistancePanel.setLayout(DistancePanelLayout);
        DistancePanelLayout.setHorizontalGroup(
            DistancePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DistancePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(DistancePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(DistancePanelLayout.createSequentialGroup()
                        .addComponent(DsitanceButton)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(DistancePanelLayout.createSequentialGroup()
                        .addGroup(DistancePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(DistanceText)
                            .addGroup(DistancePanelLayout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())))
        );
        DistancePanelLayout.setVerticalGroup(
            DistancePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DistancePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(DistanceText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(DsitanceButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        IntersectionPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel6.setText("Modifier la référence");

        IntersectionButton.setText("Repositionner");
        IntersectionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IntersectionButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout IntersectionPanelLayout = new javax.swing.GroupLayout(IntersectionPanel);
        IntersectionPanel.setLayout(IntersectionPanelLayout);
        IntersectionPanelLayout.setHorizontalGroup(
            IntersectionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(IntersectionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, IntersectionPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(IntersectionButton)
                .addGap(113, 113, 113))
        );
        IntersectionPanelLayout.setVerticalGroup(
            IntersectionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(IntersectionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addGap(18, 18, 18)
                .addComponent(IntersectionButton)
                .addContainerGap(48, Short.MAX_VALUE))
        );

        DeleteCutButton.setText("Supprimer la coupe");
        DeleteCutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeleteCutButtonActionPerformed(evt);
            }
        });

        CornerPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel11.setText("Coin interieur");

        CornerY.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CornerYActionPerformed(evt);
            }
        });

        jLabel13.setText("X");

        jLabel14.setText("Y");

        CornerButton.setText("Repositionner");
        CornerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CornerButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout CornerPanelLayout = new javax.swing.GroupLayout(CornerPanel);
        CornerPanel.setLayout(CornerPanelLayout);
        CornerPanelLayout.setHorizontalGroup(
            CornerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CornerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(CornerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(CornerPanelLayout.createSequentialGroup()
                        .addGroup(CornerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(CornerX)
                            .addComponent(jLabel13))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(CornerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(CornerY))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(CornerPanelLayout.createSequentialGroup()
                        .addGroup(CornerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addComponent(CornerButton))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        CornerPanelLayout.setVerticalGroup(
            CornerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CornerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(CornerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(CornerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CornerX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CornerY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(CornerButton)
                .addContainerGap())
        );

        ReferenceCoordinatePanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel15.setText("Coordonnée de l'intersection de référence");

        RefCoY.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RefCoYActionPerformed(evt);
            }
        });

        jLabel16.setText("X");

        jLabel17.setText("Y");

        RefCoButton.setText("Repositionner");
        RefCoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RefCoButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ReferenceCoordinatePanelLayout = new javax.swing.GroupLayout(ReferenceCoordinatePanel);
        ReferenceCoordinatePanel.setLayout(ReferenceCoordinatePanelLayout);
        ReferenceCoordinatePanelLayout.setHorizontalGroup(
            ReferenceCoordinatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ReferenceCoordinatePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ReferenceCoordinatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ReferenceCoordinatePanelLayout.createSequentialGroup()
                        .addGroup(ReferenceCoordinatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(RefCoX)
                            .addComponent(jLabel16))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(ReferenceCoordinatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel17)
                            .addComponent(RefCoY))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(ReferenceCoordinatePanelLayout.createSequentialGroup()
                        .addGroup(ReferenceCoordinatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15)
                            .addComponent(RefCoButton))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        ReferenceCoordinatePanelLayout.setVerticalGroup(
            ReferenceCoordinatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ReferenceCoordinatePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(ReferenceCoordinatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jLabel17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ReferenceCoordinatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(RefCoX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(RefCoY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(RefCoButton)
                .addContainerGap())
        );

        CutSelfUUID.setEditable(false);
        CutSelfUUID.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        CutSelfUUID.setText("UUID");

        CutSelfUUIDLabel.setText("Coupe sélectionnée : ");

        SelectedZoneUUIDLabel.setText("Zone sélectionnée : ");

        SelectedZoneUUID.setEditable(false);
        SelectedZoneUUID.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        SelectedZoneUUID.setText("UUID");

        ModifyDimensionPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel19.setText("Modifier la dimension");

        DimensionCoY.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DimensionCoYActionPerformed(evt);
            }
        });

        jLabel20.setText("X");

        jLabel21.setText("Y");

        RefCoButton1.setText("Redimensionner");
        RefCoButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DimensionCoButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ModifyDimensionPanelLayout = new javax.swing.GroupLayout(ModifyDimensionPanel);
        ModifyDimensionPanel.setLayout(ModifyDimensionPanelLayout);
        ModifyDimensionPanelLayout.setHorizontalGroup(
            ModifyDimensionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ModifyDimensionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ModifyDimensionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ModifyDimensionPanelLayout.createSequentialGroup()
                        .addGroup(ModifyDimensionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(DimensionCoX)
                            .addComponent(jLabel20))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(ModifyDimensionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel21)
                            .addComponent(DimensionCoY))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(ModifyDimensionPanelLayout.createSequentialGroup()
                        .addGroup(ModifyDimensionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel19)
                            .addComponent(RefCoButton1))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        ModifyDimensionPanelLayout.setVerticalGroup(
            ModifyDimensionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ModifyDimensionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(ModifyDimensionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(jLabel21))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ModifyDimensionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(DimensionCoX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(DimensionCoY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(RefCoButton1)
                .addContainerGap())
        );

        ModifyDistanceFromRef.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel22.setText("Modifier la distance par rapport à la référence");

        DistanceRefY1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DistanceRefY1ActionPerformed(evt);
            }
        });

        jLabel23.setText("X");

        jLabel24.setText("Y");

        ModifyRefPosButton.setText("Repositionner");
        ModifyRefPosButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ModifyRefPosButtonDimensionCoButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ModifyDistanceFromRefLayout = new javax.swing.GroupLayout(ModifyDistanceFromRef);
        ModifyDistanceFromRef.setLayout(ModifyDistanceFromRefLayout);
        ModifyDistanceFromRefLayout.setHorizontalGroup(
            ModifyDistanceFromRefLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ModifyDistanceFromRefLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ModifyDistanceFromRefLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ModifyDistanceFromRefLayout.createSequentialGroup()
                        .addGroup(ModifyDistanceFromRefLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(DistanceRefX1)
                            .addComponent(jLabel23))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(ModifyDistanceFromRefLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel24)
                            .addComponent(DistanceRefY1))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(ModifyDistanceFromRefLayout.createSequentialGroup()
                        .addGroup(ModifyDistanceFromRefLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel22)
                            .addComponent(ModifyRefPosButton))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        ModifyDistanceFromRefLayout.setVerticalGroup(
            ModifyDistanceFromRefLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ModifyDistanceFromRefLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel22)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(ModifyDistanceFromRefLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(jLabel24))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ModifyDistanceFromRefLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(DistanceRefX1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(DistanceRefY1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ModifyRefPosButton)
                .addContainerGap())
        );

        NoCutZoneModificationPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        ModificationNoCutsZoneLabel.setText("Modifier Dimension Zone Interdite");

        ModificationNoCutsZoneXLabel.setText("X");

        ModificationNoCutZoneXField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ModificationNoCutZoneXFieldActionPerformed(evt);
            }
        });

        RedimensionButtonNoCutZone.setText("Redimensionner");
        RedimensionButtonNoCutZone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RedimensionButtonNoCutZoneActionPerformed(evt);
            }
        });

        ModificationNoCutsZoneYLabel.setText("Y");

        ModificationNoCutZoneYField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ModificationNoCutZoneYFieldActionPerformed(evt);
            }
        });

        DeleteButtonNoCutZone.setText("SupprimerZone");
        DeleteButtonNoCutZone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeleteButtonNoCutZoneActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout NoCutZoneModificationPanelLayout = new javax.swing.GroupLayout(NoCutZoneModificationPanel);
        NoCutZoneModificationPanel.setLayout(NoCutZoneModificationPanelLayout);
        NoCutZoneModificationPanelLayout.setHorizontalGroup(
            NoCutZoneModificationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(NoCutZoneModificationPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(NoCutZoneModificationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ModificationNoCutsZoneLabel)
                    .addGroup(NoCutZoneModificationPanelLayout.createSequentialGroup()
                        .addComponent(ModificationNoCutsZoneXLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(NoCutZoneModificationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(NoCutZoneModificationPanelLayout.createSequentialGroup()
                                .addComponent(RedimensionButtonNoCutZone)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(DeleteButtonNoCutZone))
                            .addGroup(NoCutZoneModificationPanelLayout.createSequentialGroup()
                                .addComponent(ModificationNoCutZoneXField, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ModificationNoCutsZoneYLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ModificationNoCutZoneYField, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        NoCutZoneModificationPanelLayout.setVerticalGroup(
            NoCutZoneModificationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(NoCutZoneModificationPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ModificationNoCutsZoneLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(NoCutZoneModificationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ModificationNoCutsZoneXLabel)
                    .addComponent(ModificationNoCutZoneXField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ModificationNoCutsZoneYLabel)
                    .addComponent(ModificationNoCutZoneYField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(NoCutZoneModificationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(RedimensionButtonNoCutZone)
                    .addComponent(DeleteButtonNoCutZone))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout InformationsLayout = new javax.swing.GroupLayout(Informations);
        Informations.setLayout(InformationsLayout);
        InformationsLayout.setHorizontalGroup(
            InformationsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(InformationsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(InformationsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(CornerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(DistancePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(IntersectionPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ReferenceCoordinatePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ReferencePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(InformationsLayout.createSequentialGroup()
                        .addGroup(InformationsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(InformationsLayout.createSequentialGroup()
                                .addGap(112, 112, 112)
                                .addComponent(OutilUUID, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(InformationsLayout.createSequentialGroup()
                                .addGroup(InformationsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(CutSelfUUIDLabel)
                                    .addComponent(SelectedZoneUUIDLabel))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(InformationsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(CutSelfUUID, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(SelectedZoneUUID, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(ReferencePanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ModifyDimensionPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ModifyDistanceFromRef, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(NoCutZoneModificationPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, InformationsLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(DeleteCutButton)
                .addGap(17, 17, 17))
        );
        InformationsLayout.setVerticalGroup(
            InformationsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(InformationsLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(InformationsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(InformationsLayout.createSequentialGroup()
                        .addGroup(InformationsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(CutSelfUUIDLabel)
                            .addComponent(CutSelfUUID, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(34, 34, 34))
                    .addGroup(InformationsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(SelectedZoneUUID, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(SelectedZoneUUIDLabel)))
                .addGap(22, 22, 22)
                .addComponent(OutilUUID, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ReferencePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ReferencePanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(DistancePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(NoCutZoneModificationPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ReferenceCoordinatePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ModifyDimensionPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ModifyDistanceFromRef, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(IntersectionPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(CornerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(DeleteCutButton)
                .addContainerGap(221, Short.MAX_VALUE))
        );

        jdrawingPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout drawingPanel1Layout = new javax.swing.GroupLayout(drawingPanel1);
        drawingPanel1.setLayout(drawingPanel1Layout);
        drawingPanel1Layout.setHorizontalGroup(
            drawingPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 614, Short.MAX_VALUE)
        );
        drawingPanel1Layout.setVerticalGroup(
            drawingPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        ndp_titre.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        ndp_titre.setText("Nouveau Paneau");

        ndp_longueur_label.setFont(new java.awt.Font("Segoe UI", 2, 14)); // NOI18N
        ndp_longueur_label.setText("Longueur : ");

        ndp_largeur_label.setFont(new java.awt.Font("Segoe UI", 2, 14)); // NOI18N
        ndp_largeur_label.setText("Largeur : ");

        ndp_longueur_input.setText("1000");

        ndp_largeur_input.setText("1000");
        ndp_largeur_input.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ndp_largeur_inputActionPerformed(evt);
            }
        });

        ndp_radio_groupe.add(ndp_metrique_radio);
        ndp_metrique_radio.setText("Métrique");
        ndp_metrique_radio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ndp_metrique_radioActionPerformed(evt);
            }
        });

        ndp_radio_groupe.add(ndp_imperial_radio);
        ndp_imperial_radio.setSelected(true);
        ndp_imperial_radio.setText("Impérial");
        ndp_imperial_radio.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        ndp_imperial_radio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ndp_imperial_radioActionPerformed(evt);
            }
        });

        ndp_confirmer.setText("Confirmer");
        ndp_confirmer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ndp_confirmerActionPerformed(evt);
            }
        });

        ndp_largeur_unite.setText("mm");

        ndp_longueur_unite.setText("mm");

        javax.swing.GroupLayout newDrawingPanelLayout = new javax.swing.GroupLayout(newDrawingPanel);
        newDrawingPanel.setLayout(newDrawingPanelLayout);
        newDrawingPanelLayout.setHorizontalGroup(
            newDrawingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(newDrawingPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(newDrawingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ndp_titre, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(newDrawingPanelLayout.createSequentialGroup()
                        .addGroup(newDrawingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, newDrawingPanelLayout.createSequentialGroup()
                                .addComponent(ndp_longueur_input, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ndp_longueur_unite, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(ndp_longueur_label, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, newDrawingPanelLayout.createSequentialGroup()
                                .addGroup(newDrawingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, newDrawingPanelLayout.createSequentialGroup()
                                        .addComponent(ndp_largeur_label)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(ndp_metrique_radio, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(ndp_largeur_input, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(newDrawingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(ndp_imperial_radio, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(ndp_largeur_unite, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 308, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, newDrawingPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(ndp_confirmer, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        newDrawingPanelLayout.setVerticalGroup(
            newDrawingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(newDrawingPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ndp_titre, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(newDrawingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ndp_largeur_label)
                    .addComponent(ndp_metrique_radio)
                    .addComponent(ndp_imperial_radio))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(newDrawingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ndp_largeur_input, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ndp_largeur_unite))
                .addGap(18, 18, 18)
                .addComponent(ndp_longueur_label)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(newDrawingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ndp_longueur_input, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ndp_longueur_unite))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ndp_confirmer)
                .addContainerGap(354, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jdrawingPanelLayout = new javax.swing.GroupLayout(jdrawingPanel);
        jdrawingPanel.setLayout(jdrawingPanelLayout);
        jdrawingPanelLayout.setHorizontalGroup(
            jdrawingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jdrawingPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(drawingPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jdrawingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jdrawingPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(newDrawingPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jdrawingPanelLayout.setVerticalGroup(
            jdrawingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jdrawingPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(drawingPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jdrawingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jdrawingPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(newDrawingPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        jMenu1.setText("File");

        CreatePanelMenuButton.setText("Create new panel");
        CreatePanelMenuButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CreatePanelMenuButtonActionPerformed(evt);
            }
        });
        jMenu1.add(CreatePanelMenuButton);

        SaveMenuButton.setText("Save project");
        SaveMenuButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaveMenuButtonActionPerformed(evt);
            }
        });
        jMenu1.add(SaveMenuButton);

        LoadMenuButton.setText("Load project");
        LoadMenuButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LoadMenuButtonActionPerformed(evt);
            }
        });
        jMenu1.add(LoadMenuButton);

        ExportGCodeButton.setText("Export GCode");
        ExportGCodeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExportGCodeButtonActionPerformed(evt);
            }
        });
        jMenu1.add(ExportGCodeButton);

        jMenuBar1.add(jMenu1);

        EditMenu.setText("Edit");

        CreateGrille.setText("Grille");
        CreateGrille.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CreateGrilleActionPerformed(evt);
            }
        });
        EditMenu.add(CreateGrille);

        undoButton.setText("Undo");
        undoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                undoButtonActionPerformed(evt);
            }
        });
        EditMenu.add(undoButton);

        redoButton.setText("Redo");
        redoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                redoButtonActionPerformed(evt);
            }
        });
        EditMenu.add(redoButton);

        jMenuBar1.add(EditMenu);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(Historique_panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(Option, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Sous_Option, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jdrawingPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Informations, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(9, 9, 9))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jdrawingPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Option, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Sous_Option, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(Informations, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Historique_panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void Coupe_RActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Coupe_RActionPerformed
        closeAllSousMenu();
        S_Coupe_R.setVisible(true);
    }//GEN-LAST:event_Coupe_RActionPerformed

    private void Coupe_IActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Coupe_IActionPerformed
        closeAllSousMenu();
        S_Coupe_I.setVisible(true);
    }//GEN-LAST:event_Coupe_IActionPerformed

    private void BordureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BordureActionPerformed
        closeAllSousMenu();
        S_Bordure.setVisible(true);
    }//GEN-LAST:event_BordureActionPerformed

    private void OutilsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OutilsActionPerformed
        closeAllSousMenu();
        S_outil.setVisible(true);
    }//GEN-LAST:event_OutilsActionPerformed

    private void CI_Coupe_LActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CI_Coupe_LActionPerformed
        CI_Coupe_Rec.setSelected(false);
    }//GEN-LAST:event_CI_Coupe_LActionPerformed

    private void Zone_IActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Zone_IActionPerformed
        if (Zone_I.isSelected()) {
            controller.setMode(Controller.Mode.CREATE_NO_CUT_ZONE);
            drawingPanel1.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
            System.out.println("Mode: Create No-Cut Zone");
        } else {
            controller.setMode(Controller.Mode.IDLE);
            drawingPanel1.setCursor(Cursor.getDefaultCursor());
            System.out.println("Mode: Idle");
        }
    }//GEN-LAST:event_Zone_IActionPerformed

    private void B_Longueur_TActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_B_Longueur_TActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_B_Longueur_TActionPerformed

    private void B_Largeur_TActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_B_Largeur_TActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_B_Largeur_TActionPerformed

    private void toolButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toolButton1ActionPerformed
     // TODO add your handling code here:
    }//GEN-LAST:event_toolButton1ActionPerformed

    private void toolButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toolButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_toolButton2ActionPerformed
    private void IntersectionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IntersectionButtonActionPerformed

            controller.setMode(Controller.Mode.MODIFY_REFERENCE);
            drawingPanel1.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
            System.out.println("Mode: Modify Reference");
            repaint();
    }//GEN-LAST:event_IntersectionButtonActionPerformed

    private void DsitanceButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DsitanceButtonActionPerformed
        controller.ModifyDistance(DistanceText.getText());
        repaint();
    }//GEN-LAST:event_DsitanceButtonActionPerformed

    private void DeleteCutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeleteCutButtonActionPerformed
        controller.RemoveCut();
        updateCutHistoryTable();
        deselectCut();
        repaint();
    }//GEN-LAST:event_DeleteCutButtonActionPerformed

    private void CreatePanelMenuButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CreatePanelMenuButtonActionPerformed
        drawingPanel1.setVisible(false);
        newDrawingPanel.setVisible(true);
    }//GEN-LAST:event_CreatePanelMenuButtonActionPerformed

    private void deleteSelectedToolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteSelectedToolActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_deleteSelectedToolActionPerformed

    private void addNewToolNameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addNewToolNameTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addNewToolNameTextFieldActionPerformed

    private void addNewToolWidthTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addNewToolWidthTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addNewToolWidthTextFieldActionPerformed

    private void DistanceTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DistanceTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_DistanceTextActionPerformed

    private void CornerYActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CornerYActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CornerYActionPerformed

    private void CornerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CornerButtonActionPerformed
        controller.ModifyCorner(CornerX.getText(), CornerY.getText());
        repaint();
    }//GEN-LAST:event_CornerButtonActionPerformed

    private void RefCoYActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RefCoYActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_RefCoYActionPerformed

    private void RefCoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RefCoButtonActionPerformed
        controller.ModifyReferenceCoordinate(RefCoX.getText(), RefCoY.getText());
        repaint();
    }//GEN-LAST:event_RefCoButtonActionPerformed

    private void UUIDButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UUIDButtonActionPerformed
        controller.ModifyReferenceCut(UUIDText.getText());
        repaint();
    }//GEN-LAST:event_UUIDButtonActionPerformed

    private void CR_Coupe_MActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CR_Coupe_MActionPerformed

    }//GEN-LAST:event_CR_Coupe_MActionPerformed

    private void addNewToolDepthTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addNewToolDepthTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addNewToolDepthTextFieldActionPerformed

    private void B_CouperActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_B_CouperActionPerformed
        drawingPanel1.createRecut(B_Longueur_T.getText(), (B_Largeur_T.getText()));
    }//GEN-LAST:event_B_CouperActionPerformed

    private void ndp_metrique_radioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ndp_metrique_radioActionPerformed
        this.jRadioButton1.setSelected(false);
        this.jRadioButton2.setSelected(false);

        ndp_largeur_unite.setText(IMPERIAL_UNITE);
        ndp_longueur_unite.setText(IMPERIAL_UNITE);
        controller.changeUnitToMetric();
        if(ndp_metrique_radio.isSelected()){
            this.jRadioButton1.setSelected(true);
            this.jRadioButton2.setSelected(false);
        }

    }//GEN-LAST:event_ndp_metrique_radioActionPerformed

    private void ndp_confirmerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ndp_confirmerActionPerformed
        int largeur = Integer.parseInt(ndp_largeur_input.getText());
        int longueur = Integer.parseInt(ndp_longueur_input.getText());
        
        Dimension dimension = new Dimension(largeur,longueur);
        Panel panel = new Panel (dimension, 10.0f, new ArrayList<>(), new ArrayList<>());
        controller.setPanel(panel);
        deselectCut();
        drawingPanel1.repaint();
        
        drawingPanel1.setVisible(true);
        newDrawingPanel.setVisible(false);
    }//GEN-LAST:event_ndp_confirmerActionPerformed

    private void ndp_largeur_inputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ndp_largeur_inputActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ndp_largeur_inputActionPerformed

    private void ndp_imperial_radioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ndp_imperial_radioActionPerformed
        ndp_largeur_unite.setText(METRIQUE_UNITE);
        ndp_longueur_unite.setText(METRIQUE_UNITE);
        controller.changeUnitToImperial();
        if(ndp_imperial_radio.isSelected()){
            this.jRadioButton1.setSelected(false);
            this.jRadioButton2.setSelected(true);
        }
    }//GEN-LAST:event_ndp_imperial_radioActionPerformed

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed

        this.jRadioButton2.setSelected(false);
        if(this.jRadioButton1.isSelected()) {
            updateAllLabel();
            controller.changeUnitToMetric();
        }
    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
       this.jRadioButton1.setSelected(false);
       if(this.jRadioButton2.isSelected()) {
           updateAllLabel();
           controller.changeUnitToImperial();
        }
    }//GEN-LAST:event_jRadioButton2ActionPerformed

    private void CreateGrilleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CreateGrilleActionPerformed
        closeAllSousMenu();
        S_Grille.setVisible(true);
    }//GEN-LAST:event_CreateGrilleActionPerformed

    private void S_G_sizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_S_G_sizeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_S_G_sizeActionPerformed

    private void S_G_ConfirmerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_S_G_ConfirmerActionPerformed
        controller.setGridSize(Integer.parseInt(S_G_size.getText()));
        drawingPanel1.repaint();
    }//GEN-LAST:event_S_G_ConfirmerActionPerformed

    private void CR_Coupe_VActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CR_Coupe_VActionPerformed
        drawingPanel1.createDistanceVerticalCut(CR_Distance.getText());
        repaint();
    }//GEN-LAST:event_CR_Coupe_VActionPerformed

    private void CR_Coupe_HActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CR_Coupe_HActionPerformed
        drawingPanel1.createDistanceHorizontalCut(CR_Distance.getText());
        repaint();
    }//GEN-LAST:event_CR_Coupe_HActionPerformed

    private void CI_Coupe_RecActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CI_Coupe_RecActionPerformed
        CI_Coupe_L.setSelected(false);
    }//GEN-LAST:event_CI_Coupe_RecActionPerformed

    private void undoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_undoButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_undoButtonActionPerformed

    private void redoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_redoButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_redoButtonActionPerformed

    private void ModificationNoCutZoneXFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ModificationNoCutZoneXFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ModificationNoCutZoneXFieldActionPerformed

    private void RedimensionButtonNoCutZoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RedimensionButtonNoCutZoneActionPerformed
        UUID selectedZoneId = controller.getSelectedNoCutZoneId();

        if (selectedZoneId == null) {
            JOptionPane.showMessageDialog(this, "Aucune zone interdite sélectionnée.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Récupérer et valider les nouvelles dimensions
            float newWidth = Float.parseFloat(ModificationNoCutZoneXField.getText());
            float newHeight = Float.parseFloat(ModificationNoCutZoneYField.getText());

            if (newWidth <= 0 || newHeight <= 0) {
                JOptionPane.showMessageDialog(this, "Les dimensions doivent être positives.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (newWidth >= controller.getPanelWidth() || newHeight >= controller.getPanelHeight()) {
                JOptionPane.showMessageDialog(this, "Les dimensions doivent être plus petite que le panneau.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Mettre à jour les dimensions via le contrôleur
            controller.updateNoCutZoneDimension(selectedZoneId, newWidth, newHeight);

            // Rafraîchir l'affichage
            drawingPanel1.repaint();
            updateCutHistoryTable(); // Si vous souhaitez mettre à jour l'historique des coupes

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer des nombres valides pour les dimensions.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_RedimensionButtonNoCutZoneActionPerformed

    private void ModificationNoCutZoneYFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ModificationNoCutZoneYFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ModificationNoCutZoneYFieldActionPerformed

    private void DeleteButtonNoCutZoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeleteButtonNoCutZoneActionPerformed
        // Récupérer l'UUID de la zone interdite sélectionnée
        UUID selectedZoneId = controller.getSelectedNoCutZoneId();

        if (selectedZoneId == null) {
            JOptionPane.showMessageDialog(this, "Aucune zone interdite sélectionnée.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
            // Supprimer la zone interdite via le contrôleur
            controller.removeNoCutZone(selectedZoneId);

            // Rafraîchir l'affichage
            drawingPanel1.repaint();
            updateCutHistoryTable(); // Si vous souhaitez mettre à jour l'historique des coupes


    }//GEN-LAST:event_DeleteButtonNoCutZoneActionPerformed

    private void SaveMenuButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveMenuButtonActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Sauvegarder le Projet");

        // Filtre pour les fichiers .cnc
        FileNameExtensionFilter cncFilter = new FileNameExtensionFilter("Fichiers CNC (*.cnc)", "cnc");
        fileChooser.addChoosableFileFilter(cncFilter);
        fileChooser.setFileFilter(cncFilter); // Sélectionner le filtre par défaut

        // Sauvegarder le fichier .cnc
        fileChooser.setSelectedFile(new java.io.File("project.cnc"));
        int userSelectionCNC = fileChooser.showSaveDialog(this);
        String cncFilePath = null;
        if (userSelectionCNC == JFileChooser.APPROVE_OPTION) {
            cncFilePath = fileChooser.getSelectedFile().getAbsolutePath();
        } else {
            return; // Annulé
        }

        // Changer le filtre pour les fichiers .pan
        FileNameExtensionFilter panFilter = new FileNameExtensionFilter("Fichiers PAN (*.pan)", "pan");
        fileChooser.resetChoosableFileFilters();
        fileChooser.addChoosableFileFilter(panFilter);
        fileChooser.setFileFilter(panFilter); // Sélectionner le filtre

        // Sauvegarder le fichier .pan
        fileChooser.setSelectedFile(new java.io.File("project.pan"));
        int userSelectionPAN = fileChooser.showSaveDialog(this);
        String panFilePath = null;
        if (userSelectionPAN == JFileChooser.APPROVE_OPTION) {
            panFilePath = fileChooser.getSelectedFile().getAbsolutePath();
        } else {
            return; // Annulé
        }

        try {
            controller.saveProject(cncFilePath, panFilePath);
            JOptionPane.showMessageDialog(this, "Projet sauvegardé avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la sauvegarde du projet: " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_SaveMenuButtonActionPerformed

    private void LoadMenuButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LoadMenuButtonActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Charger le Projet");

        // Filtre pour les fichiers .cnc
        FileNameExtensionFilter cncFilter = new FileNameExtensionFilter("Fichiers CNC (*.cnc)", "cnc");
        fileChooser.addChoosableFileFilter(cncFilter);
        fileChooser.setFileFilter(cncFilter); // Sélectionner le filtre par défaut

        // Charger le fichier .cnc
        int userSelectionCNC = fileChooser.showOpenDialog(this);
        String cncFilePath = null;
        if (userSelectionCNC == JFileChooser.APPROVE_OPTION) {
            cncFilePath = fileChooser.getSelectedFile().getAbsolutePath();
        } else {
            return; // Annulé
        }

        // Changer le filtre pour les fichiers .pan
        FileNameExtensionFilter panFilter = new FileNameExtensionFilter("Fichiers PAN (*.pan)", "pan");
        fileChooser.resetChoosableFileFilters();
        fileChooser.addChoosableFileFilter(panFilter);
        fileChooser.setFileFilter(panFilter); // Sélectionner le filtre

        // Charger le fichier .pan
        int userSelectionPAN = fileChooser.showOpenDialog(this);
        String panFilePath = null;
        if (userSelectionPAN == JFileChooser.APPROVE_OPTION) {
            panFilePath = fileChooser.getSelectedFile().getAbsolutePath();
        } else {
            return; // Annulé
        }

        try {
            controller.loadProject(cncFilePath, panFilePath);
            drawingPanel1.repaint();
            JOptionPane.showMessageDialog(this, "Projet chargé avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement du projet: " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_LoadMenuButtonActionPerformed

    private void ExportGCodeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExportGCodeButtonActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Exporter G-code");
        fileChooser.setSelectedFile(new java.io.File("output.gcode"));
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            String gcodeFilePath = fileChooser.getSelectedFile().getAbsolutePath();

            // Ajouter l'extension .gcode si nécessaire
            if (!gcodeFilePath.toLowerCase().endsWith(".gcode")) {
                gcodeFilePath += ".gcode";
            }

            try {
                controller.exportGCode(gcodeFilePath);
                JOptionPane.showMessageDialog(this, "G-code exporté avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'exportation du G-code : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_ExportGCodeButtonActionPerformed

    private void DimensionCoYActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DimensionCoYActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_DimensionCoYActionPerformed

    private void DimensionCoButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DimensionCoButton1ActionPerformed
        this.controller.ModifyDimension(this.DimensionCoX.getText(), this.DimensionCoY.getText());
        repaint();
    }//GEN-LAST:event_DimensionCoButton1ActionPerformed

    private void DistanceRefY1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DistanceRefY1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_DistanceRefY1ActionPerformed

    private void ModifyRefPosButtonDimensionCoButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ModifyRefPosButtonDimensionCoButton1ActionPerformed
        this.controller.ModifyDistanceFromReference(this.DistanceRefX1.getText(), this.DistanceRefY1.getText());
        repaint();
    }//GEN-LAST:event_ModifyRefPosButtonDimensionCoButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainWindow().setVisible(true);
            }
        });
    }
    
    private void closeAllSousMenu(){
        S_outil.setVisible(false);
        S_Coupe_I.setVisible(false);
        S_Bordure.setVisible(false);
        S_Coupe_R.setVisible(false);
        S_Grille.setVisible(false);
    }



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton B_Couper;
    private javax.swing.JLabel B_Largeur_L;
    private javax.swing.JTextField B_Largeur_T;
    private javax.swing.JLabel B_Longueur_L;
    private javax.swing.JTextField B_Longueur_T;
    private javax.swing.JToggleButton B_Ratio;
    private javax.swing.JButton Bordure;
    private javax.swing.JToggleButton CI_Coupe_L;
    private javax.swing.JToggleButton CI_Coupe_Rec;
    private javax.swing.JLabel CI_Outil_s;
    private javax.swing.JButton CR_Coupe_H;
    private javax.swing.JToggleButton CR_Coupe_M;
    private javax.swing.JButton CR_Coupe_V;
    private javax.swing.JTextField CR_Distance;
    private javax.swing.JLabel CR_Label_Distance;
    private javax.swing.JLabel CR_Label_cm;
    private javax.swing.JLabel CR_Outil_s;
    private javax.swing.JButton CornerButton;
    private javax.swing.JPanel CornerPanel;
    private javax.swing.JTextField CornerX;
    private javax.swing.JTextField CornerY;
    private javax.swing.JButton Coupe_I;
    private javax.swing.JButton Coupe_R;
    private javax.swing.JMenuItem CreateGrille;
    private javax.swing.JMenuItem CreatePanelMenuButton;
    private javax.swing.JTextField CutSelfUUID;
    private javax.swing.JLabel CutSelfUUIDLabel;
    private javax.swing.JButton DeleteButtonNoCutZone;
    private javax.swing.JButton DeleteCutButton;
    private javax.swing.JTextField DimensionCoX;
    private javax.swing.JTextField DimensionCoY;
    private javax.swing.JPanel DistancePanel;
    private javax.swing.JTextField DistanceRefX1;
    private javax.swing.JTextField DistanceRefY1;
    private javax.swing.JTextField DistanceText;
    private javax.swing.JButton DsitanceButton;
    private javax.swing.JMenu EditMenu;
    private javax.swing.JMenuItem ExportGCodeButton;
    private javax.swing.JPanel Historique;
    private javax.swing.JPanel Historique_panel;
    private javax.swing.JPanel Informations;
    private javax.swing.JButton IntersectionButton;
    private javax.swing.JPanel IntersectionPanel;
    private javax.swing.JMenuItem LoadMenuButton;
    private javax.swing.JTextField ModificationNoCutZoneXField;
    private javax.swing.JTextField ModificationNoCutZoneYField;
    private javax.swing.JLabel ModificationNoCutsZoneLabel;
    private javax.swing.JLabel ModificationNoCutsZoneXLabel;
    private javax.swing.JLabel ModificationNoCutsZoneYLabel;
    private javax.swing.JPanel ModifyDimensionPanel;
    private javax.swing.JPanel ModifyDistanceFromRef;
    private javax.swing.JButton ModifyRefPosButton;
    private javax.swing.JPanel NoCutZoneModificationPanel;
    private javax.swing.JPanel Option;
    private javax.swing.JLabel OutilUUID;
    private javax.swing.JButton Outils;
    private javax.swing.JButton RedimensionButtonNoCutZone;
    private javax.swing.JButton RefCoButton;
    private javax.swing.JButton RefCoButton1;
    private javax.swing.JTextField RefCoX;
    private javax.swing.JTextField RefCoY;
    private javax.swing.JPanel ReferenceCoordinatePanel;
    private javax.swing.JPanel ReferencePanel;
    private javax.swing.JPanel ReferencePanel1;
    private javax.swing.JPanel S_Bordure;
    private javax.swing.JLabel S_CI_Titre;
    private javax.swing.JLabel S_CR_Titre;
    private javax.swing.JPanel S_Coupe_I;
    private javax.swing.JPanel S_Coupe_R;
    private javax.swing.JButton S_G_Confirmer;
    private javax.swing.JLabel S_G_Titre;
    private javax.swing.JTextField S_G_size;
    private javax.swing.JLabel S_G_size_label;
    private javax.swing.JPanel S_Grille;
    private javax.swing.JPanel S_outil;
    private javax.swing.JMenuItem SaveMenuButton;
    private javax.swing.JTextField SelectedZoneUUID;
    private javax.swing.JLabel SelectedZoneUUIDLabel;
    private javax.swing.JPanel Sous_Option;
    private javax.swing.JButton UUIDButton;
    private javax.swing.JTextField UUIDText;
    private javax.swing.JToggleButton Zone_I;
    private javax.swing.JButton addNewToolButton;
    private javax.swing.JLabel addNewToolDepthLabel;
    private javax.swing.JTextField addNewToolDepthTextField;
    private javax.swing.JLabel addNewToolNameLabel;
    private javax.swing.JTextField addNewToolNameTextField;
    private javax.swing.JLabel addNewToolWidthLabel;
    private javax.swing.JTextField addNewToolWidthTextField;
    private javax.swing.JButton deleteSelectedTool;
    private Equipe45.gui.DrawingPanel drawingPanel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JPanel jdrawingPanel;
    private javax.swing.JButton ndp_confirmer;
    private javax.swing.JRadioButton ndp_imperial_radio;
    private javax.swing.JTextField ndp_largeur_input;
    private javax.swing.JLabel ndp_largeur_label;
    private javax.swing.JLabel ndp_largeur_unite;
    private javax.swing.JTextField ndp_longueur_input;
    private javax.swing.JLabel ndp_longueur_label;
    private javax.swing.JLabel ndp_longueur_unite;
    private javax.swing.JRadioButton ndp_metrique_radio;
    private javax.swing.ButtonGroup ndp_radio_groupe;
    private javax.swing.JLabel ndp_titre;
    private javax.swing.JPanel newDrawingPanel;
    private javax.swing.JMenuItem redoButton;
    private javax.swing.JLabel selectedTool1;
    private javax.swing.JLabel selectedTool2;
    private javax.swing.JLabel selectedToolDepthLabel;
    private javax.swing.JLabel selectedToolName;
    private javax.swing.JLabel selectedToolWidth;
    private javax.swing.JButton toolButton1;
    private javax.swing.JButton toolButton10;
    private javax.swing.JButton toolButton11;
    private javax.swing.JButton toolButton12;
    private javax.swing.JButton toolButton2;
    private javax.swing.JButton toolButton3;
    private javax.swing.JButton toolButton4;
    private javax.swing.JButton toolButton5;
    private javax.swing.JButton toolButton6;
    private javax.swing.JButton toolButton7;
    private javax.swing.JButton toolButton8;
    private javax.swing.JButton toolButton9;
    private javax.swing.JMenuItem undoButton;
    // End of variables declaration//GEN-END:variables
}
