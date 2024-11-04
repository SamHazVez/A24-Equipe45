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
    private Dimension maxDimension = new Dimension(1500,1500);
    private Panel panel;
    private List<Tool> tools;
    private Tool selectedTool;

    public CNC(Coordinate systemOrigin, Panel panel, List<Tool> tools) {
        this.systemOrigin = systemOrigin;
        this.panel = panel;
        this.tools = tools;
        if (!tools.isEmpty()) {
            selectedTool = this.tools.getFirst();
        }
    }

    public void SetSelectedTool(Tool tool) {
        this.selectedTool = tool;
    }

    public Tool GetSelectedTool() {
        return this.selectedTool;
    }


    public Dimension GetMaxDimension()
    {
        return this.maxDimension;
    }

    public Panel GetPanel()
    {
        return this.panel;
    }

    public void CutPanel(){}
    
    public void SetPanelFromPanFile(){}
    
    public void AddTool(Tool tool){}
    
    public void SelectTool(Tool tool){}
    
    public void AddNoCutZone(NoCutZone noCutZone){}
    
    public void addNewCut(Cut cut){}
    
    public void ModifyCut(Cut cut){}
    
    public void RemoveCut(Cut cut){}
    
    public void ExportGCODE(){}

    public List<Tool> getTools() {
        return tools;
    }

}
