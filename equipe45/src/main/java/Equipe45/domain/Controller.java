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
import Equipe45.domain.Factory.CutFactory;
import Equipe45.domain.Utils.Coordinate;
import Equipe45.domain.Utils.Dimension;

import java.util.ArrayList;
import java.util.List;

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
    private CutFactory cutFactory;
    private NoCutZoneConverter noCutZoneConverter;

    public Controller() {
        saveManager = new SaveManager();
        this.LoadProject();
        cutConverter = new CutConverter();
        toolConverter = new ToolConverter();
        dimensionConverter = new DimensionConverter();
        panelConverter = new PanelConverter(this.cutConverter, this.dimensionConverter);
        cutFactory = new CutFactory();
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
        Panel panel = new Panel(panelDimension,10.0f, new ArrayList<>(), new ArrayList<>());

        cnc = new CNC(new Coordinate(0, 0), panel, tools);
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
    
    public void CutPanel(){}
    
    public void AddNewCut(CutDTO cut){
        //cnc.addNewCut(cut);
    }
    
    public void SetPanelFromPanFile(){}
    
    public void ModifyCut(CutDTO cut){}
    
    public void RemoveCut(CutDTO cut){}
    
    public void ExportGCODE(){}
    
}
