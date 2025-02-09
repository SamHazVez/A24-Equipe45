/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Equipe45.domain;

import Equipe45.domain.Utils.Coordinate;
import Equipe45.domain.Utils.Dimension;
import Equipe45.domain.Utils.ReferenceCoordinate;

import java.io.Serializable;
import java.util.*;

import Equipe45.domain.Utils.CutType;

/**
 *
 * @author mat18
 */
public class CNC implements Serializable {
    private static final double CLICK_DETECTION_RANGE = 10;
    private static final long serialVersionUID = 1L;

    private Panel panel;
    private List<Tool> tools;
    private Tool selectedTool;
    private Cut selectedCut;
    private NoCutZone selectedNoCutZone;

    private ArrayDeque<Cut> undoStack = new ArrayDeque<>();
    private ArrayDeque<Cut> redoStack = new ArrayDeque<>();

    public CNC(Panel panel, List<Tool> tools) {
        this.panel = panel;
        this.tools = tools;
        if (!tools.isEmpty()) {
            selectedTool = this.tools.getFirst();
        }
    }
    
    public Panel getPanel()
    {
        return this.panel;
    }
    
    public void setPanel(Panel panel) {
        this.panel = panel;
    }
    
    public void setPanelFromPanFile(){}
    
    public void exportGCODE(){}

    // <editor-fold desc="TOOLS">
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
    
    public void SetSelectedToolByIndex(int index) {
        if (index >= 0 && index < tools.size()) {
            SetSelectedTool(tools.get(index));
        }
    }
    
    public void SetSelectedTool(Tool tool) {
        this.selectedTool = tool;
        if(selectedCut != null) {
            selectedCut.tool = selectedTool;
        }
    }

    public Tool GetSelectedTool() {
        return this.selectedTool;
    }

    public List<Tool> getTools() {
        return tools;
    }
    // </editor-fold>

    // <editor-fold desc="ADD">
    public void addNewCut(Cut cut){
        if (cut == null) {
            throw new IllegalArgumentException("Cut cannot be null");
        }
        this.panel.addCut(cut);
        redoStack.clear();
    }
        
    public void addNoCutZone(NoCutZone noCutZone){
        if (noCutZone == null) {
            throw new IllegalArgumentException("NoCutZone ne peut pas être null");
        }
        panel.addNoCutZone(noCutZone);
    }
    
        
    public void addBorderCuts(ReCut borderCut)
    {
        addNewCut(borderCut.getBottomHorizontalCut());
        addNewCut(borderCut.getTopHorizontalCut());
        addNewCut(borderCut.getLeftVerticalCut());
        addNewCut(borderCut.getRightVerticalCut());
    }
    // </editor-fold>
    
    // <editor-fold desc="MODIFY">
    public void ModifySelectedReferenceCut(RegularCut regularCut) {
        if(selectedCut.getType() == CutType.PARALLEL_HORIZONTAL || selectedCut.getType() == CutType.PARALLEL_VERTICAL) {
            this.ModifyReferenceCut(selectedCut.asParallelCut(), regularCut);
        }    
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

    public void ModifyDistance(float distance){
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
            if(cut.getType() == CutType.LSHAPED || cut.getType() == CutType.RECTANGULAR) {
                IrregularCut irregularCut = cut.asIrregularCut();
                if(irregularCut.reference.horizontalCut.id == selectedCut.id){
                    ModifyReferenceCoordinate(irregularCut, new ReferenceCoordinate(selectedCut.asRegularCut(), irregularCut.reference.verticalCut));
                } else if (irregularCut.reference.verticalCut.id == selectedCut.id) {
                    ModifyReferenceCoordinate(irregularCut, new ReferenceCoordinate(selectedCut.asRegularCut(), irregularCut.reference.horizontalCut));
                }
            }
        }
    }
    
    public void ModifySelectedReferenceCoordinate(ReferenceCoordinate referenceCoordinate){
        if (selectedCut.getType() == CutType.LSHAPED || selectedCut.getType() == CutType.RECTANGULAR) {
            ModifyReferenceCoordinate(selectedCut.asIrregularCut(), referenceCoordinate);
        }
    }
    
    public void ModifyReferenceCoordinate(IrregularCut irregularCut, ReferenceCoordinate referenceCoordinate){
        if (irregularCut.getType() == CutType.LSHAPED || irregularCut.getType() == CutType.RECTANGULAR) {
            irregularCut.asIrregularCut().setReference(referenceCoordinate);
        }
    }

    public void ModifyDimension(Dimension dimension){
        if (selectedCut.getType() == CutType.LSHAPED || selectedCut.getType() == CutType.RECTANGULAR) {
            selectedCut.asIrregularCut().modifyDimension(dimension);
        }
    }
    
    public void ModifyIntersection(Coordinate coordinate){
        if (selectedCut.getType() == CutType.LSHAPED || selectedCut.getType() == CutType.RECTANGULAR) {
            selectedCut.asIrregularCut().setIntersection(coordinate);
        }
    }
    
    public void ModifyCorner(Coordinate coordinate){
        if (selectedCut.getType() == CutType.RECTANGULAR) {
            selectedCut.asRectangularCut().setCorner(coordinate);
        }
    }
    // </editor-fold>
    
    // <editor-fold desc="DELETE">
    public void RemoveSelectedCut() {
        RemoveCut(selectedCut);
    }

    public void RemoveCut(Cut cutToRemove){
        if(cutToRemove == null) {
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
            if(cut.getType() == CutType.LSHAPED || cut.getType() == CutType.RECTANGULAR) {
                IrregularCut irregularCut = cut.asIrregularCut();
                if(irregularCut.reference.horizontalCut.id == cutToRemove.id){
                   irregularCut.setReferenceInvalid();
                }  
                if(irregularCut.reference.verticalCut.id == cutToRemove.id){
                   irregularCut.setReferenceInvalid();
                }
            }
        }
        
        cutToRemove = null;
    }
    
    public void removeBorderCuts(ReCut borderCut)
    {
        RemoveCut(borderCut.getBottomHorizontalCut());
        RemoveCut(borderCut.getTopHorizontalCut());
        RemoveCut(borderCut.getLeftVerticalCut());
        RemoveCut(borderCut.getRightVerticalCut());
    }
    // </editor-fold>
    
    // <editor-fold desc="SELECT">
    public boolean isSelectedCut(Cut cut) {
        if(selectedCut == null){
            return false;
        }
        return cut.id == selectedCut.id;
    }
    
    public CutType getSelectedCutType() {
        return this.selectedCut.getType();
    }
    
    public String getSelectedCutTool() {
        if (selectedCut != null && selectedCut.getTool() != null) {
            return selectedCut.getTool().getName();
        }
        System.out.println("CNC: Aucune coupe sélectionnée.");
        return null;
    }
    
    public float getSelectedCutDistance(){
        if(this.selectedCut.getType() == CutType.PARALLEL_HORIZONTAL || this.selectedCut.getType() == CutType.PARALLEL_VERTICAL){
            return selectedCut.asParallelCut().getDistance();
        }
        return 0;
    }
    // </editor-fold>
    
    // <editor-fold desc="CLICK">
    public Cut DetermineClickedCut(Coordinate coordinate){
        this.selectedCut = this.getCutAtCoordinate(coordinate, this.panel.getCuts());
        return this.selectedCut;
    }
    
    // Pour sélectionner une coupe // Attention l'ordre a une importance
    private Cut getCutAtCoordinate(Coordinate clickCoordinate, List<Cut> cutList){
        for (Cut cut : cutList) {
            if ((cut.getType() == CutType.PARALLEL_HORIZONTAL || cut.getType() == CutType.PARALLEL_VERTICAL) && isRegularCutAtCoordinate(clickCoordinate, cut.asRegularCut())) {
                System.out.println("Click ParallelCut");
                return cut;
            } else if (cut.getType() == CutType.LSHAPED) {
                LShapedCut lShapedCut = cut.asLShapedCut();
                if(isRegularCutAtCoordinate(clickCoordinate, lShapedCut.getHorizontalCut()) || 
                        isRegularCutAtCoordinate(clickCoordinate, lShapedCut.getVerticalCut())){
                    System.out.println("Click LShapedCut");
                    return cut;
                }
            } else if (cut.getType() == CutType.RECTANGULAR) {
                RectangularCut rectangularCut = cut.asRectangularCut();
                if(isRegularCutAtCoordinate(clickCoordinate, rectangularCut.getBottomHorizontalCut()) || 
                        isRegularCutAtCoordinate(clickCoordinate, rectangularCut.getLeftVerticalCut()) ||
                        isRegularCutAtCoordinate(clickCoordinate, rectangularCut.getRightVerticalCut()) ||
                        isRegularCutAtCoordinate(clickCoordinate, rectangularCut.getTopHorizontalCut())){
                    System.out.println("Click RectangularCut");
                    return cut;
                }
            } else if ((cut.getType() == CutType.BORDER_HORIZONTAL || cut.getType() == CutType.BORDER_VERTICAL) && isRegularCutAtCoordinate(clickCoordinate, cut.asRegularCut())) {
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
                return new ReferenceCoordinate(horizontalCut, verticalCut);
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
    // </editor-fold>
    
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

    public List<Cut> getCuts() {
        return this.panel.getCuts();
    }
    
    public List<NoCutZone> getNoCutZones() {
        return panel.getNoCutZones();
    }
    public void invalidateCutsInNoCutZones(){
        panel.invalidateCutsInNoCutZones();
    }


    public void undo() {
        List<Cut> cuts = panel.getCuts();
        if (cuts.isEmpty()) {
            return;
        }
        Cut lastCut = cuts.get(0);
        if (lastCut.getType() == CutType.BORDER_HORIZONTAL ||
                lastCut.getType() == CutType.BORDER_VERTICAL) {
            return;
        }

        cuts.remove(0);
        undoStack.push(lastCut);
    }


    public void redo() {
        if (undoStack.isEmpty()) {
            return;
        }
        Cut cutToRedo = undoStack.pop();
        panel.addCut(cutToRedo);
    }
    public boolean isSelectedNoCutZone(NoCutZone noCutZone) {
        if (selectedNoCutZone == null) {
            return false;
        }
        return selectedNoCutZone.getId().equals(noCutZone.getId());
    }

    public NoCutZone DetermineClickedNoCutZone(Coordinate coordinate) {
        for (NoCutZone zone : this.panel.getNoCutZones()) {
            if (zone.containsCoordinate(coordinate)) {
                selectedNoCutZone = zone;
                return zone;
            }
        }
        selectedNoCutZone = null;
        return null;
    }

    public void RemoveNoCutZone(NoCutZone noCutZone) {
        if (noCutZone != null && panel.getNoCutZones().contains(noCutZone)) {
            panel.getNoCutZones().remove(noCutZone);
            if (noCutZone.equals(selectedNoCutZone)) {
                selectedNoCutZone = null;
            }
        }
    }
    public void RemoveNoCutZoneByID(UUID id) {
         NoCutZone noCutZone = getNoCutZoneById(id);
        if (noCutZone != null && panel.getNoCutZones().contains(noCutZone)) {
            panel.getNoCutZones().remove(noCutZone);
            if (noCutZone.equals(selectedNoCutZone)) {
                selectedNoCutZone = null;
            }
        }
    }
    public void updateNoCutZoneCoordinate(UUID noCutZoneId, float newX, float newY) {
        NoCutZone zone = getNoCutZoneById(noCutZoneId);
        if (zone != null) {
            setSelectedNoCutZone(zone);
            zone.setCoordinate(new Coordinate(newX, newY));
            System.out.println("CNC: Updated NoCutZone ID: " + noCutZoneId + " to new coordinates: (" + newX + ", " + newY + ")");
            // Réévaluer les coupes par rapport aux nouvelles positions des zones interdites
            panel.invalidateCutsInNoCutZones();
        } else {
            System.out.println("CNC: NoCutZone ID not found: " + noCutZoneId);
        }
    }

    public NoCutZone getNoCutZoneById(UUID noCutZoneId) {
        for (NoCutZone zone : panel.getNoCutZones()) {
            if (zone.getId().equals(noCutZoneId)) {
                return zone;
            }
        }
        return null;
    }
    public void deselectCut() {
        this.selectedCut = null;
        System.out.println("CNC: Coupe désélectionnée.");
    }

    public UUID getSelectedNoCutZoneId() {
        return this.selectedNoCutZone.getId();
    }


    public void setSelectedNoCutZone(NoCutZone selectedNoCutZone) {
        this.selectedNoCutZone = selectedNoCutZone;
    }



    public void ModifySelectedReferenceCoordinateAlone(ReferenceCoordinate reference) {
        this.selectedCut.asIrregularCut().setReferenceAlone(reference);
    }

    public void ModifyDistanceFromReference(float distanceX, float distanceY) {
        if(this.selectedCut instanceof RectangularCut selectedRectangularCut) {
            selectedRectangularCut.modifyDistanceFromReference(distanceX, distanceY);
        }
    }

    public Dimension getDimensionOfSelectedCut() {
        if(this.selectedCut instanceof IrregularCut selectedIrregularCut) {
            return selectedIrregularCut.getDimension();
        }
        return null;
    }

    public Coordinate getDistanceFromReference(){
        if(this.selectedCut instanceof RectangularCut selectedRectangularCut){
            return selectedRectangularCut.getDistanceFromReference();
        }
        return null;
    }
    public void setSelectedCut(Cut cut) {
        this.selectedCut = cut;
        System.out.println("CNC: Coupe sélectionnée définie à l'ID: " + (cut != null ? cut.getId() : "null"));
    }
    public Cut getCutById(UUID cutId) {
        for (Cut cut : panel.getCuts()) {
            if (cut.getId().equals(cutId)) {
                return cut;
            }
        }
        return null;
    }
}
