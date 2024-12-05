/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Equipe45.domain;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import Equipe45.domain.Converter.CutConverter;
import Equipe45.domain.Converter.DimensionConverter;
import Equipe45.domain.Converter.NoCutZoneConverter;
import Equipe45.domain.Converter.PanelConverter;
import Equipe45.domain.Converter.ToolConverter;
import Equipe45.domain.DTO.CutDTO;
import Equipe45.domain.DTO.LineCutDTO;
import Equipe45.domain.DTO.NoCutZoneDTO;
import Equipe45.domain.DTO.PanelDTO;
import Equipe45.domain.DTO.ToolDTO;
import Equipe45.domain.Utils.Coordinate;
import Equipe45.domain.Utils.CutType;
import Equipe45.domain.Utils.Dimension;
import Equipe45.domain.Utils.ReferenceCoordinate;

public class Controller {
    private CNC cnc;
    private SaveManager saveManager;
    private CutConverter cutConverter;
    private ToolConverter toolConverter;
    private PanelConverter panelConverter;
    private DimensionConverter dimensionConverter;
    private NoCutZoneConverter noCutZoneConverter;
    private ReCut initialCut;
    private float gridSize = 50;
    private MeasurementUnit selectedUnit = MeasurementUnit.MILLIMETER;

    public enum Mode {
        IDLE,
        CREATE_VERTICAL_CUT,
        CREATE_HORIZONTAL_CUT,
        CREATE_L_SHAPED_CUT,
        CREATE_NO_CUT_ZONE, CREATE_RECTANGULAR_CUT
    }
    private Mode currentMode = Mode.IDLE;

    public Controller() {
        saveManager = new SaveManager();
        this.LoadProject();
        cutConverter = new CutConverter();
        toolConverter = new ToolConverter();
        dimensionConverter = new DimensionConverter();
        panelConverter = new PanelConverter(this.cutConverter, this.dimensionConverter);
        noCutZoneConverter = new NoCutZoneConverter();
        initializeCNC();
    }

    private void initializeCNC() {
        List<Tool> tools = new ArrayList<>();
        tools.add(new Tool("Scie par défault", 5.0f, 1.0f));
        for (int i = 1; i < 3; i++) {
            float toolWidth = 5.0f + i;
            float toolDepth = 1.0f + i;
            tools.add(new Tool("Scie " + i, toolWidth, toolDepth));
        }
        Dimension panelDimension = new Dimension(1500, 1500);
        Panel panel = new Panel(panelDimension, 10.0f, new ArrayList<>(), new ArrayList<>());
        cnc = new CNC(panel, tools);
        addBorderCuts(panel);
    }
    
    public UUID getInitialCutHorizontalId() {
        return initialCut.getTopHorizontalCut().getId();
    }
    
    public UUID getInitialCutVerticalId() {
        return initialCut.getLeftVerticalCut().getId();
    }
    
    public void setMode(Mode mode) {
        this.currentMode = mode;
    }

    public Mode getMode() {
        return currentMode;
    }
    
    // <editor-fold desc="GRID">    
    public float getGridSize() {
        return gridSize;
    }

    public void setGridSize(int gridSize) {
        this.gridSize = gridSize;  
    }
    // </editor-fold>

    // <editor-fold desc="TOOLS">
    public void AddTool(ToolDTO toolDTO) {
        Tool newTool = toolConverter.convertToToolFrom(toolDTO);
        cnc.AddTool(newTool);
        cnc.SetSelectedTool(newTool);
    }

    public boolean deleteSelectedTool() {
        return cnc.DeleteSelectedTool();
    }
    
    public String getSelectedCutTool(){
        return cnc.getSelectedCutTool();
    }
    
    public ToolDTO getSelectedTool() {
        Tool selectedTool = cnc.GetSelectedTool();
        return toolConverter.convertToDTOFrom(selectedTool);
    }

    public ToolConverter getToolConverter (){
        return toolConverter;
    }
    
    public void selectToolByIndex(int index) {
        cnc.SetSelectedToolByIndex(index);
    }
    
    public List<ToolDTO> getTools() {
        List<ToolDTO> toolDTOs = new ArrayList<>();
        for (Tool tool : cnc.getTools()) {
            toolDTOs.add(toolConverter.convertToDTOFrom(tool));
        }
        return toolDTOs;
    }
    // </editor-fold>
    
    // <editor-fold desc="SELECT">
    public boolean isSelectedCut(Cut cut) {
        return cnc.isSelectedCut(cut);
    }
    
    public CutType getSelectedCutType(){
        return cnc.getSelectedCutType();
    }
    
    public int getSelectedCutDistance(){
        return cnc.getSelectedCutDistance();
    }
    // </editor-fold>

    // <editor-fold desc="ADD">
    public void addNewCut(CutDTO cutDTO){
        if (cutDTO == null) {
            throw new IllegalArgumentException("CutDTO cannot be null");
        }
        cutDTO = applySelectedToolDepthToNewCut(cutDTO);
        Cut cut = cutConverter.convertToCutFrom(cutDTO,cnc);
        if (cut == null) {
            return;
        }
        cnc.addNewCut(cut);
    }
    
    public void addNoCutZone(NoCutZoneDTO noCutZone){
        cnc.addNoCutZone(noCutZoneConverter.ConvertToNoCutZoneFromDTO(noCutZone));
    }
    
    private void addBorderCuts(Panel panel)
    {
        float depth = panel.getWidth() + 0.5f;
        Tool initialTool = cnc.getTools().get(0);
        ReCut borderCut = new ReCut(depth, initialTool, this.cnc.getPanel().getDimension());
        cnc.addNewCut(borderCut.getBottomHorizontalCut());
        cnc.addNewCut(borderCut.getTopHorizontalCut());
        cnc.addNewCut(borderCut.getLeftVerticalCut());
        cnc.addNewCut(borderCut.getRightVerticalCut());
        initialCut = borderCut;
    }
    // </editor-fold>

    public CutDTO applySelectedToolDepthToNewCut(CutDTO cutDTO) {
        cutDTO.setDepth(getSelectedTool().getCutDepth());
        return cutDTO;
    }

    public CNC getCnc() {
        return cnc;
    }
    
    public void SaveProject(){}
    
    public void LoadProject(){
        this.cnc = saveManager.LoadProject();
    }

    // <editor-fold desc="PANEL">
    public PanelDTO getPanel() {
        return panelConverter.ConvertToDTO(cnc.getPanel());
    }
    
    public float getPanelWidth() {
        return cnc.getPanel().getWidth();
    }

    public float getPanelHeight() {
        return cnc.getPanel().getHeight();
    }
    
    public void setPanel(Panel panel)
    {
        cnc.setPanel(panel);
        addBorderCuts(panel);
    }
    // </editor-fold>

    // <editor-fold desc="MODIFY">
    public void ModifyReferenceCut(String text) {
        UUID cutId = UUID.fromString(text);
        cnc.ModifySelectedReferenceCut(cnc.getRegularCutById(cutId));
    }

    public void ModifyDistance(String text){
        try {
            int distance = this.selectedUnit.toMillimeters(text);

            cnc.ModifyDistance(distance);
        } catch (NumberFormatException e) {}//TODO un message d'erreur ?
    }
        
    public void ModifyReferenceCoordinate(String xString, String yString){
        try {
            float x = Float.parseFloat(xString);
            float y = Float.parseFloat(yString);
            cnc.ModifySelectedReferenceCoordinate(this.getReferenceCoordinateOfIntersection(new Coordinate(x, y)));
        } catch (NumberFormatException e) {}//TODO un message d'erreur ?
    }
        
    public void ModifyIntersection(String xString, String yString){
        try {
            float x = this.selectedUnit.toMillimetersFloat(xString);
            float y = this.selectedUnit.toMillimetersFloat(yString);
            cnc.ModifyIntersection(new Coordinate(x, y));
        } catch (NumberFormatException e) {}//TODO un message d'erreur ?
    }

    public void ModifyDimension(String xString, String yString){
        try {
            float x = this.selectedUnit.toMillimetersFloat(xString);
            float y = this.selectedUnit.toMillimetersFloat(yString);
            cnc.ModifyDimension(new Dimension(x, y));
        } catch (NumberFormatException e) {}//TODO un message d'erreur ?
    }
    
    public void ModifyCorner(String xString, String yString){
        try {
            float x = this.selectedUnit.toMillimetersFloat(xString);
            float y = this.selectedUnit.toMillimetersFloat(yString);
            cnc.ModifyCorner(new Coordinate(x, y));
        } catch (NumberFormatException e) {}//TODO un message d'erreur ?
    }
    // </editor-fold>
    
    // <editor-fold desc="DELETE">
    public void RemoveCut(){
        cnc.RemoveSelectedCut();
    }
    // </editor-fold>
    
    // <editor-fold desc="CLICK">
    public CutDTO handleCutClick(double x, double y){
        Cut cut = cnc.DetermineClickedCut(new Coordinate((float)x,(float)y));
        if(cut != null) {
            return this.cutConverter.convertToCutDTOFrom(cut);
        }
        return null;
    }

    public ReferenceCoordinate getReferenceCoordinateOfIntersection(Coordinate clickCoordinate)
    {
        return cnc.getCoordinateOfIntersectionOfCuts(clickCoordinate);
    }
    // </editor-fold>

    public List<CutDTO> getAllCuts() {
        List<CutDTO> cutDTOs = new ArrayList<>();
        for (Cut cut : cnc.getCuts()) {
            CutDTO cutDTO = cutConverter.convertToCutDTOFrom(cut);
            cutDTOs.add(cutDTO);
        }
        return cutDTOs;
    }
    
    public List<LineCutDTO> getAllDrawableCuts() {
        List<LineCutDTO> cuts = new ArrayList<>();
        for (Cut cut : cnc.getCuts()) {
            Color color = setCutColor(cut);
            List<LineCutDTO> lines = cutConverter.convertToLineCutDTOFrom(cut, color);
            cuts.addAll(lines);
        }
        return cuts;
    }
    
    private Color setCutColor(Cut cut){
        if(isSelectedCut(cut)) return Color.BLACK;
        else if(!cut.isValid()) return Color.RED;
        else if(cut.isInNoCutZone()) return Color.RED;
        else return Color.GREEN.darker();
    }
    
    public List<NoCutZone> getNoCutZones() {
        return cnc.getNoCutZones();
    }
    
    public void invalidateCutsInNoCutZones(){
        cnc.invalidateCutsInNoCutZones();
    }
    
    public void changeUnitToImperial() {
        this.selectedUnit = MeasurementUnit.INCH;
    }
    
    public void changeUnitToMetric() {
        this.selectedUnit = MeasurementUnit.MILLIMETER;
    }
}
