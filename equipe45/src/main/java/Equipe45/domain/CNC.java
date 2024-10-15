/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Equipe45.domain;

import Equipe45.domain.Utils.Coordinate;
import Equipe45.domain.Utils.Dimension;
import java.util.List;

/**
 *
 * @author mat18
 */
public class CNC {
    
    private Coordinate systemOrigin;
    private Dimension maxDimension;
    private Panel panel;
    private List<Tool> tools;
    private Tool selectedTool;

    public CNC() {
    }
    
    public void CutPanel(){}
    
    public void SetPanelFromPanFile(){}
    
    public void AddTool(Tool tool){}
    
    public void SelectTool(Tool tool){}
    
    public void AddNoCutZone(NoCutZone noCutZone){}
    
    public void addNewCut(Cut cut){}
    
    public void SetPanelWithPanFile(){}
    
    public void ModifyCut(Cut cut){}
    
    public void RemoveCut(Cut cut){}
    
    public void ExportGCODE(){}
}
