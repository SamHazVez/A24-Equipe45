/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Equipe45.gui;

import Equipe45.domain.Drawing.PanelDrawer;

import java.awt.BasicStroke;
import java.awt.Dimension;
  import java.awt.Graphics;
  import java.awt.Graphics2D;
import java.io.Serializable;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
 
  public class DrawingPanel extends JPanel implements Serializable {
    
    public Dimension initialDimension;
    private MainWindow mainWindow;
    
    public DrawingPanel(){
    }
    
    public DrawingPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        setBorder(new javax.swing.border.BevelBorder(BevelBorder.LOWERED));
        int width = (int) (java.awt.Toolkit.getDefaultToolkit().getScreenSize().width);
        setPreferredSize(new Dimension(width,1));
        setVisible(true);
        int height = (int)(width*0.5);
        initialDimension = new Dimension(width,height);
    }
    @Override
    protected void paintComponent(Graphics g)
    {
        if (mainWindow != null){
            super.paintComponent(g); 
            PanelDrawer mainDrawer = new PanelDrawer(mainWindow.getController(),initialDimension);
            mainDrawer.draw(g);
        }
    }
    
    public MainWindow getMainWindow(){
        return mainWindow;
    }
    
    public void setMainWindow(MainWindow mainWindow){
        this.mainWindow = mainWindow;
    }
    
    public Dimension getInitialDimension(){
        return initialDimension;
    }
    
    public void setInitialDimension(){
        
    }
}
