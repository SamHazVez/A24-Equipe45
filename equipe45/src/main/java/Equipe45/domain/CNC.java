/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Equipe45.domain;

import Equipe45.domain.Utils.Coordinate;
import Equipe45.domain.Utils.CutType;
import Equipe45.domain.Utils.Dimension;
import Equipe45.domain.Utils.ReferenceCoordinate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author mat18
 */
public class CNC {
    private static final double CLICK_DETECTION_RANGE = 5;
    
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

    public boolean DeleteSelectedTool() {
        if (selectedTool != null && !tools.getFirst().equals(selectedTool)) {
            tools.remove(selectedTool);
            selectedTool = tools.getFirst();
            return true;
        } else {
            return false;
        }
    }

    public void AddTool(Tool tool) {
        if (tool == null) {
            throw new IllegalArgumentException("Tool cannot be null");
        }
        if (tools.size() >= 12) {
            throw new IllegalStateException("Cannot add more than 12 tools");
        }
        tools.add(tool);
    }

    
    public Dimension GetMaxDimension()
    {
        return this.maxDimension;
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
        this.panel.addCut(cut);
    }
    
    public Panel GetPanel()
    {
        return this.panel;
    }
    
    public void SetPanelFromPanFile(){}
    
    public void SelectTool(Tool tool){}
    
    public void AddNoCutZone(NoCutZone noCutZone){}
    
    public void ModifySelectedReferenceCut(RegularCut regularCut) {
        this.ModifyReferenceCut(selectedCut.asParallelCut(), regularCut);
    }
    
    private void ModifyReferenceCut(ParallelCut actualCut, RegularCut newCut){
        if(newCut == null || actualCut.id == newCut.id)
            return;
        
        if(actualCut.getType() == CutType.PARALLEL_HORIZONTAL || actualCut.getType() == CutType.PARALLEL_VERTICAL) {
            actualCut.setReferenceCut(newCut);
        }
        
        for (Cut cut : this.panel.getCuts()) {
            if(cut.getType() == CutType.PARALLEL_HORIZONTAL || cut.getType() == CutType.PARALLEL_VERTICAL) {
                ParallelCut parallelCut = cut.asParallelCut();
                if(parallelCut.getReferenceCut().id == actualCut.id){
                   ModifyReferenceCut(parallelCut, actualCut);
                }                    
            }
        }
    }

    public void ModifyDistance(int distance){
        if(distance < 0) 
            return;
        
        if(selectedCut.getType() == CutType.PARALLEL_HORIZONTAL || selectedCut.getType() == CutType.PARALLEL_VERTICAL) {
            selectedCut.asParallelCut().setDistance(distance);
        }
        
        for (Cut cut : this.panel.getCuts()) {
            if(cut.getType() == CutType.PARALLEL_HORIZONTAL || cut.getType() == CutType.PARALLEL_VERTICAL) {
                ParallelCut parallelCut = cut.asParallelCut();
                if(parallelCut.getReferenceCut().id == selectedCut.id){
                   ModifyReferenceCut(parallelCut, selectedCut.asRegularCut());
                }                    
            }
        }
    }
    
    public void ModifyReferenceCoordinate(ReferenceCoordinate referenceCoordinate){
        if (selectedCut instanceof IrregularCut irregularCut) {
            irregularCut.setReference(referenceCoordinate);
        }
    }
    
    public void ModifyIntersection(Coordinate coordinate){
        if (selectedCut instanceof IrregularCut irregularCut) {
            irregularCut.setIntersection(coordinate);
        }
    }
    
    public void ModifyCorner(Coordinate coordinate){
        if (selectedCut instanceof RectangularCut rectangularCut) {
            rectangularCut.setCorner(coordinate);
        }
    }
    
    public void RemoveSelectedCut() {
        RemoveCut(selectedCut);
    }

    public void RemoveCut(Cut cutToRemove){
        if(cutToRemove == null) {
            return;
        }
        if(cutToRemove.getType() == CutType.BORDER_HORIZONTAL || cutToRemove.getType() == CutType.BORDER_VERTICAL){
            return;
        }
        this.panel.getCuts().remove(cutToRemove);
        
        for (Cut cut : this.panel.getCuts()) {
            if(cut.getType() == CutType.PARALLEL_HORIZONTAL || cut.getType() == CutType.PARALLEL_VERTICAL) {
                ParallelCut parallelCut = cut.asParallelCut();
                if(parallelCut.getReferenceCut().id == cutToRemove.id){
                   parallelCut.setReferenceInvalid();
                }                    
            }
        }
        
        cutToRemove = null;
    }
    
    public void ExportGCODE(){}

    public List<Tool> getTools() {
        return tools;
    }
    
    public CutType getSelectedCutType() {
        return this.selectedCut.getType();
    }
    
    public int getSelectedCutDistance(){
        if(this.selectedCut instanceof ParallelCut parallelCut){
            return parallelCut.getDistance();
        }
        return 0;
    }
    
    public Cut DetermineClickedCut(Coordinate coordinate){
        this.selectedCut = this.getCutAtCoordinate(coordinate, this.panel.getCuts());
        return this.selectedCut;
    }
    
    // Pour sélectionner une coupe // Attention l'ordre a une importance
    private Cut getCutAtCoordinate(Coordinate clickCoordinate, List<Cut> cutList){
        for (Cut cut : cutList) {
            if (cut instanceof  ParallelCut regularCut && isRegularCutAtCoordinate(clickCoordinate, regularCut)) {
                System.out.println("Click ParallelCut");
                return cut;
            } else if (cut instanceof LShapedCut lShapedCut) {
                if(isRegularCutAtCoordinate(clickCoordinate, lShapedCut.getHorizontalCut()) || 
                        isRegularCutAtCoordinate(clickCoordinate, lShapedCut.getVerticalCut())){
                    System.out.println("Click LShapedCut");
                    return cut;
                }
            } else if (cut instanceof RectangularCut rectangularCut) {
                if(isRegularCutAtCoordinate(clickCoordinate, rectangularCut.getBottomHorizontalCut()) || 
                        isRegularCutAtCoordinate(clickCoordinate, rectangularCut.getLeftVerticalCut()) ||
                        isRegularCutAtCoordinate(clickCoordinate, rectangularCut.getRightVerticalCut()) ||
                        isRegularCutAtCoordinate(clickCoordinate, rectangularCut.getTopHorizontalCut())){
                    System.out.println("Click RectangularCut");
                    return cut;
                }
            } else if (cut instanceof  BorderCut regularCut && isRegularCutAtCoordinate(clickCoordinate, regularCut)) {
                System.out.println("Click BorderCut");
                return cut;
            } 
        }
        System.out.println("No cut at " + clickCoordinate.x + " " + clickCoordinate.y);
        return null;
    }

    
    // Pour trouver les coupes des points d'intersection
    private List<Cut> getCutsAtCoordinate(Coordinate clickCoordinate){
        List<Cut> cuts = new ArrayList<Cut>();
        for (Cut cut : this.panel.getCuts()) {
            if (cut instanceof  RegularCut regularCut && isRegularCutAtCoordinate(clickCoordinate, regularCut)) {
                cuts.add(cut);
            } else if (cut instanceof LShapedCut lShapedCut) {
                if(isRegularCutAtCoordinate(clickCoordinate, lShapedCut.getHorizontalCut()))
                {
                    System.out.println("C<est ici horizontal");
                    cuts.add(lShapedCut.getHorizontalCut());
                }
                if(isRegularCutAtCoordinate(clickCoordinate, lShapedCut.getVerticalCut()))
                {
                    System.out.println("C<est ici vert");
                    cuts.add(lShapedCut.getVerticalCut());
                }

            } else if (cut instanceof RectangularCut rectangularCut) {
                if (isRegularCutAtCoordinate(clickCoordinate, rectangularCut.getBottomHorizontalCut())) {
                    cuts.add(rectangularCut.getBottomHorizontalCut());
                    System.out.println("Bottom Horizon");
                }
                if (isRegularCutAtCoordinate(clickCoordinate, rectangularCut.getTopHorizontalCut())) {
                    cuts.add(rectangularCut.getTopHorizontalCut());
                    System.out.println("Top Horizon");
                }
                if (isRegularCutAtCoordinate(clickCoordinate, rectangularCut.getLeftVerticalCut())) {
                    cuts.add(rectangularCut.getLeftVerticalCut());
                    System.out.println("Left Vert");
                }
                if (isRegularCutAtCoordinate(clickCoordinate, rectangularCut.getRightVerticalCut())) {
                    cuts.add(rectangularCut.getRightVerticalCut());
                    System.out.println("Right Vert");
                }
            } else if(cut instanceof ReCut reCut) {
                if (isRegularCutAtCoordinate(clickCoordinate, reCut.getBottomHorizontalCut())) {
                    cuts.add(reCut.getBottomHorizontalCut());
                    System.out.println("Bottom Horizon");
                }
                if (isRegularCutAtCoordinate(clickCoordinate, reCut.getTopHorizontalCut())) {
                    cuts.add(reCut.getTopHorizontalCut());
                    System.out.println("Top Horizon");
                }
                if (isRegularCutAtCoordinate(clickCoordinate, reCut.getLeftVerticalCut())) {
                    cuts.add(reCut.getLeftVerticalCut());
                    System.out.println("Left Vert");
                }
                if (isRegularCutAtCoordinate(clickCoordinate, reCut.getRightVerticalCut())) {
                    cuts.add(reCut.getRightVerticalCut());
                    System.out.println("Right Vert");
                }
            }
        }
        return cuts;
    }

    private boolean pointIsAtIntersectionOfCuts(List<Cut> cutList) {
        if(cutList.size() >= 2) {
            return true;
        }
        return false;
    }

    private RegularCut getFirstVerticalCutInList(List<Cut> cutList)
    {
        for (Cut cut : cutList) {
            if (cut instanceof RegularCut regularCut) {
                if (regularCut.isVertical())
                {
                    return regularCut;
                }
            }
        }
        return null;
    }

    private RegularCut getFirstHorizontalCutInList(List<Cut> cutList)
    {
        for (Cut cut : cutList) {
            if (cut instanceof RegularCut regularCut) {
                if (regularCut.isHorizontal())
                {
                    return regularCut;
                }
            }
        }
        return null;
    }

    public ReferenceCoordinate getCoordinateOfIntersectionOfCuts(Coordinate clickCoordinate) {
        List<Cut> cuts = this.getCutsAtCoordinate(clickCoordinate);
        if (pointIsAtIntersectionOfCuts(cuts)) {
            RegularCut horizontalCut = getFirstHorizontalCutInList(cuts);
            RegularCut verticalCut = getFirstVerticalCutInList(cuts);
            if (horizontalCut != null && verticalCut != null) {
                return new ReferenceCoordinate(verticalCut.getOrigin().getX(), horizontalCut.getOrigin().getY(), horizontalCut, verticalCut);
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

    private boolean isRectangularCutAtCoordinate(Coordinate clickCoordinate, RectangularCut rectangularCut) {
        return isCutAtCoordinate(clickCoordinate, rectangularCut.getReference(), rectangularCut.getCorner()) ||
                isCutAtCoordinate(clickCoordinate, rectangularCut.getCorner(), rectangularCut.getIntersection()) ||
                isCutAtCoordinate(clickCoordinate, rectangularCut.getIntersection(), rectangularCut.getOtherCorner()) ||
                isCutAtCoordinate(clickCoordinate, rectangularCut.getOtherCorner(), rectangularCut.getReference());
    }

    private boolean isCutAtCoordinate(Coordinate clickCoordinate, Coordinate origin, Coordinate destination) {
        double distanceOrigin = coordinateDistance(origin, clickCoordinate);
        double distanceDestination = coordinateDistance(destination, clickCoordinate);
        double length = coordinateDistance(origin, destination);
        
        return isCoordinateOnPoint(distanceOrigin, distanceDestination, length);
    }
    
    private boolean isCoordinateOnPoint(double distanceOrigin, double distanceDestination, double length) {
        return Math.abs((distanceOrigin + distanceDestination) - length) <= CLICK_DETECTION_RANGE;
    }

    private Coordinate getExactPointOfCut(Coordinate clickCoordinate, Coordinate origin, Coordinate destination) {
        float dx = destination.getX() - origin.getX();
        float dy = destination.getY() - origin.getY();
        float cx = clickCoordinate.getX() - origin.getX();
        float cy = clickCoordinate.getY() - origin.getY();
        float dotProduct = cx * dx + cy * dy;
        float lengthSquared = dx * dx + dy * dy;
        float t = dotProduct / lengthSquared;
        t = Math.max(0, Math.min(1, t));
        float exactX = origin.getX() + t * dx;
        float exactY = origin.getY() + t * dy;
        return new Coordinate(exactX, exactY);
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
    public void changeCurrentPanel(Panel panel) {
        this.panel = panel;
    }
}
