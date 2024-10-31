/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Equipe45.gui;

import Equipe45.domain.Controller;
import java.awt.Graphics2D;
import Equipe45.domain.DTO.CutDTO;
import Equipe45.domain.DTO.StraightCutDTO;

/**
 *
 * @author mat18
 */
public class MainWindow extends javax.swing.JFrame {

    private Controller controller;

    public Controller getController() {
        return controller;
    }

    public MainWindow() {
        controller = new Controller();
        initComponents();
        S_outil.setVisible(false);
        S_Coupe_I.setVisible(false);
        S_Bordure.setVisible(false);
        S_Coupe_R.setVisible(false);
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
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        S_Coupe_R = new javax.swing.JPanel();
        CR_Outil_s = new javax.swing.JLabel();
        CR_Coupe_V = new javax.swing.JToggleButton();
        CR_Coupe_H = new javax.swing.JToggleButton();
        S_CR_Titre = new javax.swing.JLabel();
        S_Coupe_I = new javax.swing.JPanel();
        CI_Outil_s = new javax.swing.JLabel();
        CI_Coupe_Rec = new javax.swing.JToggleButton();
        CI_Coupe_L = new javax.swing.JToggleButton();
        S_CI_Titre = new javax.swing.JLabel();
        S_Bordure = new javax.swing.JPanel();
        B_Longueur_L = new javax.swing.JLabel();
        B_Longueur_T = new javax.swing.JTextField();
        B_Largeur_L = new javax.swing.JLabel();
        B_Largeur_T = new javax.swing.JTextField();
        B_Ratio = new javax.swing.JToggleButton();
        B_Couper = new javax.swing.JButton();
        Historique = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        drawingPanel1 = new Equipe45.gui.DrawingPanel(this);
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
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

        jButton1.setText("jButton1");

        jButton2.setText("jButton2");

        jButton3.setText("jButton3");

        jButton4.setText("jButton4");

        jButton5.setText("jButton5");

        jButton6.setText("jButton6");

        jButton7.setText("jButton7");

        jButton8.setText("jButton8");

        jButton9.setText("jButton9");

        jButton10.setText("jButton10");

        jButton11.setText("jButton11");

        jButton12.setText("jButton12");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton10, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                    .addComponent(jButton11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton12)
                .addContainerGap(47, Short.MAX_VALUE))
        );

        jScrollPane1.setViewportView(jPanel1);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Nom :");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setText("Profondeur :");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("Largeur :");

        javax.swing.GroupLayout S_outilLayout = new javax.swing.GroupLayout(S_outil);
        S_outil.setLayout(S_outilLayout);
        S_outilLayout.setHorizontalGroup(
            S_outilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(S_outilLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(S_outilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
                    .addGroup(S_outilLayout.createSequentialGroup()
                        .addGroup(S_outilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        S_outilLayout.setVerticalGroup(
            S_outilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(S_outilLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addContainerGap(126, Short.MAX_VALUE))
        );

        CR_Outil_s.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        CR_Outil_s.setText("Outils : ");

        CR_Coupe_V.setText("Coupe verticale");

        CR_Coupe_H.setText("Coupe horizontale");
        CR_Coupe_H.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CR_Coupe_HActionPerformed(evt);
            }
        });

        S_CR_Titre.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        S_CR_Titre.setText("Coupe Régulière :");

        javax.swing.GroupLayout S_Coupe_RLayout = new javax.swing.GroupLayout(S_Coupe_R);
        S_Coupe_R.setLayout(S_Coupe_RLayout);
        S_Coupe_RLayout.setHorizontalGroup(
            S_Coupe_RLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(S_Coupe_RLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(S_Coupe_RLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(CR_Coupe_V, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(CR_Coupe_H, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
                    .addGroup(S_Coupe_RLayout.createSequentialGroup()
                        .addGroup(S_Coupe_RLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(CR_Outil_s)
                            .addComponent(S_CR_Titre))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        S_Coupe_RLayout.setVerticalGroup(
            S_Coupe_RLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(S_Coupe_RLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(CR_Outil_s)
                .addGap(15, 15, 15)
                .addComponent(S_CR_Titre)
                .addGap(18, 18, 18)
                .addComponent(CR_Coupe_V)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(CR_Coupe_H)
                .addContainerGap(175, Short.MAX_VALUE))
        );

        CI_Outil_s.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        CI_Outil_s.setText("Outils : ");

        CI_Coupe_Rec.setText("Coupe rectangulaire");

        CI_Coupe_L.setText("Coupe en L");
        CI_Coupe_L.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CI_Coupe_LActionPerformed(evt);
            }
        });

        S_CI_Titre.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        S_CI_Titre.setText("Coupe Irrégulière :");

        javax.swing.GroupLayout S_Coupe_ILayout = new javax.swing.GroupLayout(S_Coupe_I);
        S_Coupe_I.setLayout(S_Coupe_ILayout);
        S_Coupe_ILayout.setHorizontalGroup(
            S_Coupe_ILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(S_Coupe_ILayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(S_Coupe_ILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(CI_Coupe_Rec, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(CI_Coupe_L, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
                    .addGroup(S_Coupe_ILayout.createSequentialGroup()
                        .addGroup(S_Coupe_ILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(CI_Outil_s)
                            .addComponent(S_CI_Titre))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        S_Coupe_ILayout.setVerticalGroup(
            S_Coupe_ILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(S_Coupe_ILayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(CI_Outil_s)
                .addGap(15, 15, 15)
                .addComponent(S_CI_Titre)
                .addGap(18, 18, 18)
                .addComponent(CI_Coupe_Rec)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(CI_Coupe_L)
                .addContainerGap(175, Short.MAX_VALUE))
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
                    .addComponent(B_Largeur_T, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
                    .addGroup(S_BordureLayout.createSequentialGroup()
                        .addGroup(S_BordureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(B_Longueur_L)
                            .addComponent(B_Largeur_L)
                            .addComponent(B_Ratio))
                        .addGap(0, 0, Short.MAX_VALUE))
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
                .addContainerGap(140, Short.MAX_VALUE))
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
                .addComponent(S_Bordure, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Sous_OptionLayout.createSequentialGroup()
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
                .addContainerGap(212, Short.MAX_VALUE))
        );
        HistoriqueLayout.setVerticalGroup(
            HistoriqueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HistoriqueLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Historique, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(drawingPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Option, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(drawingPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Historique, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Sous_Option, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
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
        //CutDTO cutDto = new StraightCutDTO();
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
    private javax.swing.JPanel Historique;
    private javax.swing.JPanel Option;
    private javax.swing.JButton Outils;
    private javax.swing.JPanel S_Bordure;
    private javax.swing.JLabel S_CI_Titre;
    private javax.swing.JLabel S_CR_Titre;
    private javax.swing.JPanel S_Coupe_I;
    private javax.swing.JPanel S_Coupe_R;
    private javax.swing.JPanel S_outil;
    private javax.swing.JPanel Sous_Option;
    private javax.swing.JToggleButton Zone_I;
    private Equipe45.gui.DrawingPanel drawingPanel1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    // End of variables declaration//GEN-END:variables
}
