/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Equipe45.domain.Converter;

import Equipe45.domain.Cut;
import Equipe45.domain.DTO.CutDTO;
import Equipe45.domain.DTO.DimensionDTO;
import Equipe45.domain.DTO.PanelDTO;
import Equipe45.domain.Panel;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mat18
 */
public class PanelConverter {
    private CutConverter cutConverter;
    private DimensionConverter dimensionConverter;

    public PanelConverter(CutConverter cutConverter, DimensionConverter dimensionConverter) {
        this.cutConverter = cutConverter;
        this.dimensionConverter = dimensionConverter;
    }

    public Panel ConvertFromDTO(PanelDTO panelDTO){
        List<Cut> cuts = new ArrayList<>();
        for (CutDTO cutDTO : panelDTO.cuts) {
            cuts.add(cutConverter.convertToCutFrom(cutDTO));
        }
        return new Panel(dimensionConverter.convertToDimensionFrom(panelDTO.dimension) , panelDTO.width, cuts);
    }
    

    public PanelDTO ConvertToDTO(Panel panel) {
        DimensionDTO dimensionDTO = dimensionConverter.convertToDimensionDTOFrom(panel.getDimension());
        List<CutDTO> cutDTOs = new ArrayList<>();
        for (Cut cut : panel.getCuts()) {
            cutDTOs.add(cutConverter.convertToCutDTOFrom(cut));
        }
        return new PanelDTO(dimensionDTO, panel.getWidth(), cutDTOs);
    }
}
