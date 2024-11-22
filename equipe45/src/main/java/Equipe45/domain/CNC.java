/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Equipe45.domain;

import Equipe45.domain.Utils.Coordinate;
import Equipe45.domain.Utils.Dimension;
import Equipe45.domain.Utils.ReferenceCoordinate;
import java.util.List;
import java.util.UUID;

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
    private Cut selectedCut;

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

    public void addNewCut(Cut cut){
        if (cut == null) {
            throw new IllegalArgumentException("Cut cannot be null");
        }
        this.panel.getCuts().add(cut);
    }

    public Dimension GetMaxDimension()
    {
        return this.maxDimension;
    }
    
    public Panel GetPanel()
    {
        return this.panel;
    }
    
    public void SetPanelFromPanFile(){}
    
    public void AddTool(Tool tool){}
    
    public void SelectTool(Tool tool){}
    
    public void AddNoCutZone(NoCutZone noCutZone){}

    
    /*public void ModifyOrigin(Coordinate coordinate){
        if (selectedCut instanceof RegularCut regularCut) {
            regularCut.setOrigin(new ReferenceCoordinate(coordinate, selectedCut.getId()));
        }
    }
    
    public void ModifyDestination(Coordinate coordinate){
        if (selectedCut instanceof RegularCut regularCut) {
            regularCut.setDestination(new ReferenceCoordinate(coordinate, selectedCut.getId()));
        }
    }
    
    public void ModifyIntersection(Coordinate coordinate){
        if (selectedCut instanceof IrregularCut irregularCut) {
            irregularCut.setIntersection(coordinate);
        }
    }*/
    
    public void RemoveCut(){
        if(selectedCut != null) {
            this.panel.getCuts().remove(this.selectedCut);
            this.selectedCut = null;
        }
    }
    
    public void ExportGCODE(){}

    public List<Tool> getTools() {
        return tools;
    }
    
    public Cut DetermineClickedCut(Coordinate coordinate){
        Cut cut = this.getCutAtCoordinate(coordinate, this.panel.getCuts());
        if(cut != null){
            this.selectedCut = cut;
        }
        return this.selectedCut;
    }
    
    private Cut getCutAtCoordinate(Coordinate clickCoordinate, List<Cut> cutList){
        for (Cut cut : cutList) {            
            if (cut instanceof  RegularCut regularCut && isRegularCutAtCoordinate(clickCoordinate, regularCut)) {
                return cut;
            } else if (cut instanceof IrregularCut irregularCut && isIrregularCutAtCoordinate(clickCoordinate, irregularCut)) {
                return cut;
            }
        }
        return null;
    }
    
    private boolean isRegularCutAtCoordinate(Coordinate clickCoordinate, RegularCut regularCut) {
        return isCutAtCoordinate(clickCoordinate, regularCut.getOrigin(), regularCut.getDestination());
    }
    
    private boolean isIrregularCutAtCoordinate(Coordinate clickCoordinate, IrregularCut irregularCut) {
        return isCutAtCoordinate(clickCoordinate, irregularCut.getReference(), irregularCut.getIntersection())||
                isCutAtCoordinate(clickCoordinate, irregularCut.getIntersection(), irregularCut.getReference());
    }
    
    private boolean isCutAtCoordinate(Coordinate clickCoordinate, Coordinate origin, Coordinate destination) {
        double distanceOrigin = coordinateDistance(origin, clickCoordinate);
        double distanceDestination = coordinateDistance(destination, clickCoordinate);
        double length = coordinateDistance(origin, destination);
        
        return isCoordinateOnPoint(distanceOrigin, distanceDestination, length);
    }
    
    private boolean isCoordinateOnPoint(double distanceOrigin, double distanceDestination, double length) {
        if(Math.abs((distanceOrigin + distanceDestination) - length) <= 1) {
            return true;
        }
        return false;
    }
    
    private double coordinateDistance(Coordinate c1, Coordinate c2) {
        return Math.sqrt(Math.pow(c2.getX() - c1.getX(), 2) + Math.pow(c2.getY() - c1.getY(), 2));
    }
    public RegularCut getRegularCutById(UUID cutId) {
        for (Cut cut : panel.getCuts()) {
            if (cut.getId().equals(cutId) && cut instanceof RegularCut) {
                return (RegularCut) cut;
            }
        }
        return null;
    }

    public RectangularCut getRectangularCutById(UUID cutId) {
        for (Cut cut : panel.getCuts()) {
            if (cut.getId().equals(cutId) && cut instanceof RectangularCut) {
                return (RectangularCut) cut;
            }
        }
        return null;
    }

    public void updateValidReferences () {
        for (Tool tool : tools) {
            
        }
    }
}
