/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Equipe45.domain.Converter;

import Equipe45.domain.CNC;
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

    public Panel ConvertFromDTO(PanelDTO panelDTO, CNC cnc) {
        // Check if panelDTO or its properties are null
        if (panelDTO == null) {
            System.out.println("panelDTO is null");
            return null; // or handle appropriately
        }

        if (panelDTO.cuts == null) {
            System.out.println("panelDTO.cuts is null");
        }

        List<Cut> cuts = new ArrayList<>();
        for (CutDTO cutDTO : panelDTO.cuts) {
            if (cutDTO == null) {
                System.out.println("cutDTO is null");
            } else {
                cuts.add(cutConverter.convertToCutFrom(cutDTO, cnc));
            }
        }

        // Check if dimension is null
        if (panelDTO.dimension == null) {
            System.out.println("panelDTO.dimension is null");
        }
        // Check if dimensionConverter is null
        if (dimensionConverter == null) {
            System.out.println("dimensionConverter is null");
        }

        // Return new Panel, handling nulls as appropriate
        return new Panel(
                dimensionConverter != null ? dimensionConverter.convertToDimensionFrom(panelDTO.dimension) : null,
                panelDTO.width,
                cuts
        );
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
