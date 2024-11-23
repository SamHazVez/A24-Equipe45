/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Equipe45.domain;

import Equipe45.domain.Converter.*;
import Equipe45.domain.DTO.CutDTO;
import Equipe45.domain.DTO.DimensionDTO;
import Equipe45.domain.DTO.NoCutZoneDTO;
import Equipe45.domain.DTO.PanelDTO;
import Equipe45.domain.DTO.ToolDTO;
import Equipe45.domain.Utils.Coordinate;
import Equipe45.domain.Utils.Dimension;
import Equipe45.domain.Utils.ReferenceCoordinate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author mat18
 */

public class Controller {
    private CNC cnc;
    private SaveManager saveManager;
    private CutConverter cutConverter;
    private ToolConverter toolConverter;
    private PanelConverter panelConverter;
    private DimensionConverter dimensionConverter;
    private NoCutZoneConverter noCutZoneConverter;
    private UUID initialCutId;

    public enum Mode {
        IDLE,
        CREATE_VERTICAL_CUT,
        CREATE_HORIZONTAL_CUT,
        CREATE_L_SHAPED_CUT
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

        for (int i = 0; i < 11; i++) {
            float toolWidth = 5.0f + i;
            tools.add(new Tool("Scie " + i, toolWidth, i));
        }

        Dimension panelDimension = new Dimension(1500, 1500);
        Panel panel = new Panel(panelDimension, 10.0f, new ArrayList<>(), new ArrayList<>());

        cnc = new CNC(new Coordinate(0, 0), panel, tools);

        addBorderCuts(panel);
    }

    public UUID getInitialCutId() {
        return initialCutId;
    }

    public ToolConverter getToolConverter (){
        return toolConverter;
    }

    public void setMode(Mode mode) {
        this.currentMode = mode;
    }

    public Mode getMode() {
        return currentMode;
    }

    public void addNewCut(CutDTO cutDTO){
        if (cutDTO == null) {
            throw new IllegalArgumentException("CutDTO cannot be null");
        }

        Cut cut = cutConverter.convertToCutFrom(cutDTO,cnc);
        if (cut == null) {
            System.err.println("Failed to convert CutDTO to Cut");
            return;
        }
        cnc.addNewCut(cut);
    }


    public void selectToolByIndex(int index) {
        List<Tool> tools = cnc.getTools();
        if (index >= 0 && index < tools.size()) {
            cnc.SetSelectedTool(tools.get(index));
        } else {
            System.out.println("Index d'outil invalide : " + index);
        }
    }

    public ToolDTO getSelectedTool() {
        Tool selectedTool = cnc.GetSelectedTool();
        return toolConverter.convertToDTOFrom(selectedTool);
    }


    public CNC getCnc() {
        return cnc;
    }
    
    public void SaveProject(){}
    
    public void LoadProject(){
        this.cnc = saveManager.LoadProject();
    }
    
    public void AddTool(ToolDTO tool){}
    
    public void SelectTool(ToolDTO tool){}
    
    public void AddNoCutZone(NoCutZoneDTO noCutZone){}

    public List<ToolDTO> getTools() {
        List<ToolDTO> toolDTOs = new ArrayList<>();
        for (Tool tool : cnc.getTools()) {
            toolDTOs.add(toolConverter.convertToDTOFrom(tool));
        }
        return toolDTOs;
    }

    public PanelDTO getPanel() {
        return panelConverter.ConvertToDTO(cnc.GetPanel());
    }

    public DimensionDTO GetPanelMaxDimension()
    {
        return dimensionConverter.convertToDimensionDTOFrom(this.cnc.GetMaxDimension());
    }

    public PanelDTO GetPanel()
    {
        return panelConverter.ConvertToDTO(cnc.GetPanel());
    }
        
    public void SetPanelFromPanFile(){}
    
    /*public void ModifyOrigin(String xString, String yString) {
        try {
            float x = Float.parseFloat(xString);
            float y = Float.parseFloat(yString);
            cnc.ModifyOrigin(new Coordinate(x, y));
        } catch (NumberFormatException e) {}//TODO un message d'erreur ?
    }

    public void ModifyDestination(String xString, String yString){
        try {
            float x = Float.parseFloat(xString);
            float y = Float.parseFloat(yString);
            cnc.ModifyDestination(new Coordinate(x, y));
        } catch (NumberFormatException e) {}//TODO un message d'erreur ?
    }
        
    public void ModifyIntersection(String xString, String yString){
        try {
            float x = Float.parseFloat(xString);
            float y = Float.parseFloat(yString);
            cnc.ModifyIntersection(new Coordinate(x, y));
        } catch (NumberFormatException e) {}//TODO un message d'erreur ?
    }*/
    
    public void RemoveCut(){
        cnc.RemoveCut();
    }
    
    public void ExportGCODE(){}
    
    public CutDTO handleCutClick(double x, double y){
        Cut cut = cnc.DetermineClickedCut(new Coordinate((float)x,(float)y));
        if(cut != null)
            return this.cutConverter.convertToCutDTOFrom(cut);
        return null;
    }

    public ReferenceCoordinate getReferenceCoordinateOfIntersection(Coordinate clickCoordinate)
    {
        return cnc.getCoordinateOfIntersectionOfCuts(clickCoordinate);
    }
    public void changeCurrentPanel(Panel panel)
    {
        cnc.changeCurrentPanel(panel);
        addBorderCuts(panel);
    }
    private void addBorderCuts(Panel panel)
    {
        float depth = panel.getWidth() + 0.5f;
        Tool initialTool = cnc.getTools().get(0);
        ReCut borderCut = new ReCut(depth, initialTool, this.cnc.GetPanel().getDimension());
        cnc.addNewCut(borderCut.getBottomHorizontalCut());
        cnc.addNewCut(borderCut.getTopHorizontalCut());
        cnc.addNewCut(borderCut.getLeftVerticalCut());
        cnc.addNewCut(borderCut.getRightVerticalCut());
        initialCutId = borderCut.getId();
    }
}
