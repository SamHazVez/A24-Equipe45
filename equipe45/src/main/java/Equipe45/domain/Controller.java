/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Equipe45.domain;

import Equipe45.domain.Converter.*;
import Equipe45.domain.DTO.CutDTO;
import Equipe45.domain.DTO.NoCutZoneDTO;
import Equipe45.domain.DTO.PanelDTO;
import Equipe45.domain.DTO.ToolDTO;
import Equipe45.domain.Factory.CutFactory;
import Equipe45.domain.Utils.Coordinate;

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
    }
    
    public void SaveProject(){}
    
    public void LoadProject(){
        this.cnc = saveManager.LoadProject();
    }
    
    public void AddTool(ToolDTO tool){}
    
    public void SelectTool(ToolDTO tool){}
    
    public void AddNoCutZone(NoCutZoneDTO noCutZone){}
    
    public void GetTools(){}

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
