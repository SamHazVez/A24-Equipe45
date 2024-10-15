/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Equipe45.domain;

import Equipe45.domain.Converter.CutConverter;
import Equipe45.domain.Converter.NoCutZoneConverter;
import Equipe45.domain.Converter.ToolConverter;
import Equipe45.domain.DTO.CutDTO;
import Equipe45.domain.DTO.NoCutZoneDTO;
import Equipe45.domain.DTO.ToolDTO;
import Equipe45.domain.Factory.CutFactory;

/**
 *
 * @author mat18
 */
public class Controller {
    private CNC cnc;
    private SaveManager saveManager;
    private CutConverter cutConverter;
    private ToolConverter toolConverter;
    private CutFactory cutFactory;
    private NoCutZoneConverter noCutZoneConverter;

    public Controller() {
    }
    
    public void SaveProject(){}
    
    public void LoadProject(){}
    
    public void AddTool(ToolDTO tool){}
    
    public void SelectTool(ToolDTO tool){}
    
    public void AddNoCutZone(NoCutZoneDTO noCutZone){}
    
    public void GetTools(){}
    
    public void CutPanel(){}
    
    public void AddNewCut(CutDTO cut){}
    
    public void SetPanelFromPanFile(){}
    
    public void ModifyCut(CutDTO cut){}
    
    public void RemoveCut(CutDTO cut){}
    
    public void ExportGCODE(){}
    
}
