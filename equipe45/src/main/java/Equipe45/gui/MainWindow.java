/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Equipe45.gui;

import Equipe45.domain.Controller;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import Equipe45.domain.DTO.ToolDTO;
import Equipe45.domain.Panel;
import Equipe45.domain.Utils.Dimension;

import javax.swing.*;

/**
 *
 * @author mat18
 */
public class MainWindow extends javax.swing.JFrame {

    private Controller controller;

    public Controller getController() {
        return controller;
    }

    private JButton[] toolButtons;

    public MainWindow() {
        controller = new Controller();
        initComponents();
        initializeToolButtons();
        initializeSelectedToolLabels();
        addVerticalCutEvent();
        addHorizontalCutEvent();
        addLShapedCutEvent();
        addDeleteToolButtonEvent();
        addNewToolButtonEvent();
        S_outil.setVisible(false);
        S_Coupe_I.setVisible(false);
        S_Bordure.setVisible(false);
        S_Coupe_R.setVisible(false);
    }

    private void addVerticalCutEvent(){
        CR_Coupe_V.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if (CR_Coupe_V.isSelected()) {
                    controller.setMode(Controller.Mode.CREATE_VERTICAL_CUT);
                    drawingPanel1.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                    System.out.println("Mode: Create Vertical Cut");
                } else {
                    controller.setMode(Controller.Mode.IDLE);
                    drawingPanel1.setCursor(Cursor.getDefaultCursor());
                    System.out.println("Mode: Idle");
                }
            }
        });
    }

    private void addNewToolButtonEvent() {
        addNewToolButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String toolName = addNewToolNameTextField.getText();
                String toolWidthStr = addNewToolWidthTextField.getText();
                String errorMessage = "";

                if (toolName == null || toolName.trim().isEmpty()) {
                    errorMessage += "Le nom ne peut pas être vide ou seulement des espaces.\n";
                } else if (toolName.length() > 12) {
                    errorMessage += "Le nom ne peut pas dépasser 12 caractères.\n";
                } else if (!toolName.matches("[A-Za-z0-9 ]+")) {
                    errorMessage += "Le nom ne peut contenir que des lettres, des chiffres et des espaces.\n";
                }

                float toolWidth = 0;
                try {
                    toolWidth = Float.parseFloat(toolWidthStr);
                    if (toolWidth <= 0 || toolWidth > 20) {
                        errorMessage += "La largeur est trop grande.\n";
                    }
                } catch (NumberFormatException ex) {
                    errorMessage += "La largeur doit être un nombre valide.\n";
                }

                if (controller.getTools().size() >= 12) {
                    errorMessage += "Le nombre maximum d'outils (12) a été atteint.\n";
                }

                if (!errorMessage.isEmpty()) {
                    JOptionPane.showMessageDialog(MainWindow.this,
                            errorMessage,
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    ToolDTO newTool = new ToolDTO(toolName, toolWidth);
                    controller.AddTool(newTool);
                    initializeToolButtons();
                    initializeSelectedToolLabels();
                    addNewToolNameTextField.setText("");
                    addNewToolWidthTextField.setText("");
                }
            }
        });
    }


    private void addHorizontalCutEvent(){
        CR_Coupe_H.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if (CR_Coupe_H.isSelected()) {
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



    private void addLShapedCutEvent(){
        CI_Coupe_L.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if (CI_Coupe_L.isSelected()) {
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


    private void initializeSelectedToolLabels() {
        ToolDTO selectedTool = controller.getSelectedTool();
        if (selectedTool != null) {
            selectedTool1.setText(selectedTool.getName());
            selectedTool2.setText(selectedTool.getName());
            selectedToolName.setText(selectedTool.getName());
            selectedToolWidth.setText(String.valueOf(selectedTool.getCutWidth()));
        } else {
            selectedTool1.setText("Aucun outil sélectionné");
            selectedTool2.setText("Aucun outil sélectionné");
            selectedToolName.setText("");
            selectedToolWidth.setText("");
        }
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
                    selectedToolWidth.setText(String.valueOf(tools.get(index).getCutWidth()));
                });
            } else {
                toolButtons[i].setVisible(false);
            }
        }
        S_outil.revalidate();
        S_outil.repaint();
    }

    
    public void updateCutOriginInformations(Float x, Float y) {
        OriginX.setText((x == null) ? "" : String.valueOf(x));
        OriginY.setText((y == null) ? "" : String.valueOf(y));
    }
    
    public void updateCutDestinationInformations(Float x, Float y) {
        DesinationX.setText((x == null) ? "" : String.valueOf(x));
        DestinationY.setText((y == null) ? "" : String.valueOf(y));
    }
    
    public void updateCutIntersectionInformations(Float x, Float y) {
        IntersectionX.setText((x == null) ? "" : String.valueOf(x));
        IntersectionY.setText((y == null) ? "" : String.valueOf(y));
    }
    
    public void showIntersection(){
        IntersectionPanel.setVisible(true);
    }
    
    public void hideIntersection(){
        IntersectionPanel.setVisible(false);
    }
    
    public void deselectCut(){
        updateCutOriginInformations(null, null);
        updateCutDestinationInformations(null, null);
        hideIntersection();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Option = new javax.swing.JPanel();
        Outils = new javax.swing.JButton();
        Coupe_R = new javax.swing.JButton();
        Coupe_I = new javax.swing.JButton();
        Bordure = new javax.swing.JButton();
        Zone_I = new javax.swing.JToggleButton();
        Sous_Option = new javax.swing.JPanel();
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
        addNewToolLabel = new javax.swing.JLabel();
        addNewToolNameLabel = new javax.swing.JLabel();
        addNewToolNameTextField = new javax.swing.JTextField();
        addNewToolWidthLabel = new javax.swing.JLabel();
        addNewToolWidthTextField = new javax.swing.JTextField();
        addNewToolButton = new javax.swing.JButton();
        S_Coupe_R = new javax.swing.JPanel();
        CR_Outil_s = new javax.swing.JLabel();
        CR_Coupe_V = new javax.swing.JToggleButton();
        CR_Coupe_H = new javax.swing.JToggleButton();
        S_CR_Titre = new javax.swing.JLabel();
        selectedTool1 = new javax.swing.JLabel();
        S_Coupe_I = new javax.swing.JPanel();
        CI_Outil_s = new javax.swing.JLabel();
        CI_Coupe_Rec = new javax.swing.JToggleButton();
        CI_Coupe_L = new javax.swing.JToggleButton();
        S_CI_Titre = new javax.swing.JLabel();
        selectedTool2 = new javax.swing.JLabel();
        S_Bordure = new javax.swing.JPanel();
        B_Longueur_L = new javax.swing.JLabel();
        B_Longueur_T = new javax.swing.JTextField();
        B_Largeur_L = new javax.swing.JLabel();
        B_Largeur_T = new javax.swing.JTextField();
        B_Ratio = new javax.swing.JToggleButton();
        B_Couper = new javax.swing.JButton();
        Historique = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        Informations = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        OriginPanel = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        OriginX = new javax.swing.JTextField();
        OriginY = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        OriginButton = new javax.swing.JButton();
        DestinationPanel = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        DesinationX = new javax.swing.JTextField();
        DestinationY = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        DestinationButton = new javax.swing.JButton();
        IntersectionPanel = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        IntersectionX = new javax.swing.JTextField();
        IntersectionY = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        IntersectionButton = new javax.swing.JButton();
        DeleteCutButton = new javax.swing.JButton();
        drawingPanel1 = new Equipe45.gui.DrawingPanel(this);
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        CreatePanelMenuButton = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Equipe45");
        setBackground(new java.awt.Color(0, 0, 0));
        setResizable(false);

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
                .addComponent(Zone_I, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
                .addContainerGap())
        );

        Sous_Option.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

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
        jLabel4.setText("Largeur :");

        selectedToolName.setText("selectedToolName");

        selectedToolWidth.setText("selectedToolWidth");

        deleteSelectedTool.setText("Supprimer l'outil");
        deleteSelectedTool.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteSelectedToolActionPerformed(evt);
            }
        });

        addNewToolLabel.setText("Ajouter un outil");

        addNewToolNameLabel.setText("Nom du nouvel outil:");

        addNewToolNameTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addNewToolNameTextFieldActionPerformed(evt);
            }
        });

        addNewToolWidthLabel.setText("Largeur du nouveul outil: ");

        addNewToolWidthTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addNewToolWidthTextFieldActionPerformed(evt);
            }
        });

        addNewToolButton.setText("Ajouter à la liste");

        javax.swing.GroupLayout S_outilLayout = new javax.swing.GroupLayout(S_outil);
        S_outil.setLayout(S_outilLayout);
        S_outilLayout.setHorizontalGroup(
            S_outilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(S_outilLayout.createSequentialGroup()
                .addGroup(S_outilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(S_outilLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(S_outilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(S_outilLayout.createSequentialGroup()
                                .addComponent(addNewToolNameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(30, 30, 30)
                                .addComponent(addNewToolNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE)
                            .addGroup(S_outilLayout.createSequentialGroup()
                                .addGroup(S_outilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(S_outilLayout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addGap(18, 18, 18)
                                        .addComponent(selectedToolWidth, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(deleteSelectedTool, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(S_outilLayout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addGap(18, 18, 18)
                                        .addComponent(selectedToolName, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(S_outilLayout.createSequentialGroup()
                        .addGap(83, 83, 83)
                        .addComponent(addNewToolLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(S_outilLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(addNewToolWidthLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(addNewToolWidthTextField)))
                .addContainerGap())
            .addGroup(S_outilLayout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addComponent(addNewToolButton, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        S_outilLayout.setVerticalGroup(
            S_outilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(S_outilLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(S_outilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(selectedToolName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(S_outilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(selectedToolWidth))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(deleteSelectedTool, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addNewToolLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(S_outilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addNewToolNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addNewToolNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(S_outilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addNewToolWidthLabel)
                    .addComponent(addNewToolWidthTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addNewToolButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        CR_Outil_s.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        CR_Outil_s.setText("Outil sélectionné : ");

        CR_Coupe_V.setText("Coupe verticale");

        CR_Coupe_H.setText("Coupe horizontale");
        CR_Coupe_H.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CR_Coupe_HActionPerformed(evt);
            }
        });

        S_CR_Titre.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        S_CR_Titre.setText("Coupe Régulière :");

        selectedTool1.setText("selectedTool1");

        javax.swing.GroupLayout S_Coupe_RLayout = new javax.swing.GroupLayout(S_Coupe_R);
        S_Coupe_R.setLayout(S_Coupe_RLayout);
        S_Coupe_RLayout.setHorizontalGroup(
            S_Coupe_RLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(S_Coupe_RLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(S_Coupe_RLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(CR_Coupe_V, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(CR_Coupe_H, javax.swing.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE)
                    .addComponent(S_CR_Titre)
                    .addGroup(S_Coupe_RLayout.createSequentialGroup()
                        .addComponent(CR_Outil_s)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(selectedTool1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        S_Coupe_RLayout.setVerticalGroup(
            S_Coupe_RLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(S_Coupe_RLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(S_Coupe_RLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CR_Outil_s)
                    .addComponent(selectedTool1))
                .addGap(40, 40, 40)
                .addComponent(S_CR_Titre)
                .addGap(18, 18, 18)
                .addComponent(CR_Coupe_V)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(CR_Coupe_H)
                .addContainerGap(162, Short.MAX_VALUE))
        );

        CI_Outil_s.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        CI_Outil_s.setText("Outil sélectionné : ");

        CI_Coupe_Rec.setText("Coupe rectangulaire");

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
                        .addComponent(S_CI_Titre)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(S_Coupe_ILayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(CI_Outil_s)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(selectedTool2, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        S_Coupe_ILayout.setVerticalGroup(
            S_Coupe_ILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(S_Coupe_ILayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(S_Coupe_ILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(CI_Outil_s)
                    .addComponent(selectedTool2))
                .addGap(15, 15, 15)
                .addComponent(S_CI_Titre)
                .addGap(18, 18, 18)
                .addComponent(CI_Coupe_Rec)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(CI_Coupe_L)
                .addContainerGap(187, Short.MAX_VALUE))
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
                        .addGap(0, 97, Short.MAX_VALUE))
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
                .addContainerGap(156, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout Sous_OptionLayout = new javax.swing.GroupLayout(Sous_Option);
        Sous_Option.setLayout(Sous_OptionLayout);
        Sous_OptionLayout.setHorizontalGroup(
            Sous_OptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(S_outil, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(Sous_OptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(S_Coupe_R, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(Sous_OptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(S_Coupe_I, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(Sous_OptionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(Sous_OptionLayout.createSequentialGroup()
                    .addComponent(S_Bordure, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
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
        );

        Historique.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel1.setText("Historique : ");

        javax.swing.GroupLayout HistoriqueLayout = new javax.swing.GroupLayout(Historique);
        Historique.setLayout(HistoriqueLayout);
        HistoriqueLayout.setHorizontalGroup(
            HistoriqueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HistoriqueLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(365, Short.MAX_VALUE))
        );
        HistoriqueLayout.setVerticalGroup(
            HistoriqueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HistoriqueLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        Informations.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Informations.setName("Informations"); // NOI18N

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setLabelFor(Informations);
        jLabel5.setText("Coupe");

        OriginPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel9.setText("Origine");

        jLabel10.setText("X");

        jLabel11.setText("Y");

        OriginButton.setText("Repositionner");
        OriginButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OriginButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout OriginPanelLayout = new javax.swing.GroupLayout(OriginPanel);
        OriginPanel.setLayout(OriginPanelLayout);
        OriginPanelLayout.setHorizontalGroup(
            OriginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(OriginPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(OriginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(OriginPanelLayout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(OriginPanelLayout.createSequentialGroup()
                        .addGroup(OriginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(OriginPanelLayout.createSequentialGroup()
                                .addGroup(OriginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(OriginX)
                                    .addComponent(jLabel10))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(OriginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel11)
                                    .addComponent(OriginY)))
                            .addComponent(OriginButton))
                        .addGap(0, 12, Short.MAX_VALUE))))
        );
        OriginPanelLayout.setVerticalGroup(
            OriginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(OriginPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(OriginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(OriginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(OriginX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(OriginY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(OriginButton)
                .addContainerGap())
        );

        DestinationPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel12.setText("Destination");

        jLabel13.setText("X");

        jLabel14.setText("Y");

        DestinationButton.setText("Repositionner");
        DestinationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DestinationButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout DestinationPanelLayout = new javax.swing.GroupLayout(DestinationPanel);
        DestinationPanel.setLayout(DestinationPanelLayout);
        DestinationPanelLayout.setHorizontalGroup(
            DestinationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DestinationPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(DestinationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(DestinationPanelLayout.createSequentialGroup()
                        .addGroup(DestinationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(DesinationX)
                            .addComponent(jLabel13))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(DestinationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(DestinationY)))
                    .addGroup(DestinationPanelLayout.createSequentialGroup()
                        .addGroup(DestinationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12)
                            .addComponent(DestinationButton))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        DestinationPanelLayout.setVerticalGroup(
            DestinationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DestinationPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(DestinationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(DestinationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(DesinationX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(DestinationY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(DestinationButton))
        );

        IntersectionPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel6.setText("Intersection");

        IntersectionY.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IntersectionYActionPerformed(evt);
            }
        });

        jLabel7.setText("X");

        jLabel8.setText("Y");

        IntersectionButton.setText("Redimensionner");
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
                .addGroup(IntersectionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(IntersectionPanelLayout.createSequentialGroup()
                        .addGroup(IntersectionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(IntersectionX)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(IntersectionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(IntersectionY)))
                    .addGroup(IntersectionPanelLayout.createSequentialGroup()
                        .addGroup(IntersectionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(IntersectionButton))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        IntersectionPanelLayout.setVerticalGroup(
            IntersectionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(IntersectionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(IntersectionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(IntersectionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(IntersectionX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(IntersectionY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(IntersectionButton)
                .addContainerGap())
        );

        DeleteCutButton.setText("Supprimer la coupe");
        DeleteCutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeleteCutButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout InformationsLayout = new javax.swing.GroupLayout(Informations);
        Informations.setLayout(InformationsLayout);
        InformationsLayout.setHorizontalGroup(
            InformationsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(InformationsLayout.createSequentialGroup()
                .addGroup(InformationsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(InformationsLayout.createSequentialGroup()
                        .addGroup(InformationsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(InformationsLayout.createSequentialGroup()
                                .addGap(7, 7, 7)
                                .addComponent(jLabel5))
                            .addGroup(InformationsLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(InformationsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(DeleteCutButton)
                                    .addComponent(OriginPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 49, Short.MAX_VALUE))
                    .addGroup(InformationsLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(InformationsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(DestinationPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(IntersectionPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        InformationsLayout.setVerticalGroup(
            InformationsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(InformationsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(OriginPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(DestinationPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(IntersectionPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(DeleteCutButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout drawingPanel1Layout = new javax.swing.GroupLayout(drawingPanel1);
        drawingPanel1.setLayout(drawingPanel1Layout);
        drawingPanel1Layout.setHorizontalGroup(
            drawingPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        drawingPanel1Layout.setVerticalGroup(
            drawingPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jMenu1.setText("File");

        CreatePanelMenuButton.setText("Create new panel");
        CreatePanelMenuButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CreatePanelMenuButtonActionPerformed(evt);
            }
        });
        jMenu1.add(CreatePanelMenuButton);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(Sous_Option, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Option, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Historique, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(drawingPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Informations, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(9, 9, 9))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Informations, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(Option, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 7, Short.MAX_VALUE))
                            .addComponent(drawingPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(Historique, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Sous_Option, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(26, 26, 26))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void Coupe_RActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Coupe_RActionPerformed
        // TODO add your handling code here:
        S_outil.setVisible(false);
        S_Coupe_I.setVisible(false);
        S_Bordure.setVisible(false);
        S_Coupe_R.setVisible(true);
    }//GEN-LAST:event_Coupe_RActionPerformed

    private void Coupe_IActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Coupe_IActionPerformed
        S_Coupe_R.setVisible(false);
        S_outil.setVisible(false);
        S_Bordure.setVisible(false);
        S_Coupe_I.setVisible(true);
    }//GEN-LAST:event_Coupe_IActionPerformed

    private void BordureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BordureActionPerformed
        S_Coupe_R.setVisible(false);
        S_outil.setVisible(false);
        S_Coupe_I.setVisible(false);
        S_Bordure.setVisible(true);
    }//GEN-LAST:event_BordureActionPerformed

    private void CR_Coupe_HActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CR_Coupe_HActionPerformed
        // aller chercher attributes de la coupe
        // Créer nouvelle coupe(dto)
        //CutDTO cutDto = new ParallelCutDTO();
        //this.controller.AddNewCut(cutDto);
    }//GEN-LAST:event_CR_Coupe_HActionPerformed

    private void OutilsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OutilsActionPerformed
        S_Coupe_R.setVisible(false);
        S_Coupe_I.setVisible(false);
        S_Bordure.setVisible(false);
        S_outil.setVisible(true);
    }//GEN-LAST:event_OutilsActionPerformed

    private void CI_Coupe_LActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CI_Coupe_LActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CI_Coupe_LActionPerformed

    private void Zone_IActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Zone_IActionPerformed
        // TODO add your handling code here:
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
        //controller.ModifyIntersection(IntersectionX.getText(), IntersectionY.getText());
        repaint();
    }//GEN-LAST:event_IntersectionButtonActionPerformed

    private void IntersectionYActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IntersectionYActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_IntersectionYActionPerformed

    private void OriginButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OriginButtonActionPerformed
        //controller.ModifyOrigin(OriginX.getText(), OriginY.getText());
        repaint();
    }//GEN-LAST:event_OriginButtonActionPerformed

    private void DestinationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DestinationButtonActionPerformed
        //controller.ModifyDestination(DesinationX.getText(), DestinationY.getText());
        repaint();
    }//GEN-LAST:event_DestinationButtonActionPerformed

    private void DeleteCutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeleteCutButtonActionPerformed
        controller.RemoveCut();
        deselectCut();
        repaint();
    }//GEN-LAST:event_DeleteCutButtonActionPerformed

    private void CreatePanelMenuButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CreatePanelMenuButtonActionPerformed
        Dimension dimension = new Dimension(1000,1000);
        Panel panel = new Panel (dimension, 10.0f, new ArrayList<>(), new ArrayList<>());
        controller.changeCurrentPanel(panel);
        deselectCut();
        drawingPanel1.repaint();
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
    private javax.swing.JToggleButton CR_Coupe_H;
    private javax.swing.JToggleButton CR_Coupe_V;
    private javax.swing.JLabel CR_Outil_s;
    private javax.swing.JButton Coupe_I;
    private javax.swing.JButton Coupe_R;
    private javax.swing.JMenuItem CreatePanelMenuButton;
    private javax.swing.JButton DeleteCutButton;
    private javax.swing.JTextField DesinationX;
    private javax.swing.JButton DestinationButton;
    private javax.swing.JPanel DestinationPanel;
    private javax.swing.JTextField DestinationY;
    private javax.swing.JPanel Historique;
    private javax.swing.JPanel Informations;
    private javax.swing.JButton IntersectionButton;
    private javax.swing.JPanel IntersectionPanel;
    private javax.swing.JTextField IntersectionX;
    private javax.swing.JTextField IntersectionY;
    private javax.swing.JPanel Option;
    private javax.swing.JButton OriginButton;
    private javax.swing.JPanel OriginPanel;
    private javax.swing.JTextField OriginX;
    private javax.swing.JTextField OriginY;
    private javax.swing.JButton Outils;
    private javax.swing.JPanel S_Bordure;
    private javax.swing.JLabel S_CI_Titre;
    private javax.swing.JLabel S_CR_Titre;
    private javax.swing.JPanel S_Coupe_I;
    private javax.swing.JPanel S_Coupe_R;
    private javax.swing.JPanel S_outil;
    private javax.swing.JPanel Sous_Option;
    private javax.swing.JToggleButton Zone_I;
    private javax.swing.JButton addNewToolButton;
    private javax.swing.JLabel addNewToolLabel;
    private javax.swing.JLabel addNewToolNameLabel;
    private javax.swing.JTextField addNewToolNameTextField;
    private javax.swing.JLabel addNewToolWidthLabel;
    private javax.swing.JTextField addNewToolWidthTextField;
    private javax.swing.JButton deleteSelectedTool;
    private Equipe45.gui.DrawingPanel drawingPanel1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel selectedTool1;
    private javax.swing.JLabel selectedTool2;
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
    // End of variables declaration//GEN-END:variables
}
