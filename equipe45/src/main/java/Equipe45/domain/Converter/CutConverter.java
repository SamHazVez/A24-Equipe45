/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Equipe45.domain.Converter;

import Equipe45.domain.BorderCut;
import Equipe45.domain.CNC;
import Equipe45.domain.Cut;
import Equipe45.domain.DTO.BorderCutDTO;
import Equipe45.domain.DTO.CutDTO;
import Equipe45.domain.DTO.LShapedCutDTO;
import Equipe45.domain.DTO.LineCutDTO;
import Equipe45.domain.DTO.ParallelCutDTO;
import Equipe45.domain.DTO.ReCutDTO;
import Equipe45.domain.DTO.RectangularCutDTO;
import Equipe45.domain.LShapedCut;
import Equipe45.domain.ParallelCut;
import Equipe45.domain.ReCut;
import Equipe45.domain.RectangularCut;
import Equipe45.domain.RegularCut;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class CutConverter {
    private final ToolConverter toolConverter = new ToolConverter();
    private final PanelConverter panelConverter;

    public CutConverter() {
        this.panelConverter = new PanelConverter(this, new DimensionConverter());
    }

    public Cut convertToCutFrom(CutDTO cutDTO, CNC cnc) {
        return switch (cutDTO) {
            case ParallelCutDTO parallelCutDTO -> convertToParallelCutFromDTO(parallelCutDTO, cnc);
            case LShapedCutDTO lShapedCutDTO -> convertToLShapedCutFromDTO(lShapedCutDTO);
            case BorderCutDTO borderCutDTO -> convertToBorderCutFromDTO(borderCutDTO);
            case RectangularCutDTO rectangularCutDTO -> convertToRectangularCutFromDTO(rectangularCutDTO, cnc);
            case ReCutDTO reCutDTO -> convertToRecutFromDTO(reCutDTO, cnc);
            default -> null;
        };
    }

    public CutDTO convertToCutDTOFrom(Cut cut) {
        return switch (cut) {
            case ParallelCut parallelCut -> convertToDTOFromParallelCut(parallelCut);
            case LShapedCut lShapedCut -> convertToDTOFromLShapedCut(lShapedCut);
            case BorderCut borderCut -> convertToDTOFromBorderCut(borderCut);
            case RectangularCut rectangularCut -> convertToDTOFromRectangularCut(rectangularCut);
            default -> null;
        };
    }
    
    public List<LineCutDTO> convertToLineCutDTOFrom(Cut cut, Color color) {
        List<LineCutDTO> lines = new ArrayList<>();
        lines.addAll(switch (cut) {
            case ParallelCut parallelCut -> convertToLineDTOFromParallelCut(parallelCut);
            case LShapedCut lShapedCut -> convertToLineDTOFromLShapedCut(lShapedCut);
            case BorderCut borderCut -> convertToLineDTOFromBorderCut(borderCut);
            case RectangularCut rectangularCut -> convertToLineDTOFromRectangularCut(rectangularCut);
            default -> {
                System.err.println("Warning: Unhandled cut type: " + cut.getClass().getName());
                yield new ArrayList<LineCutDTO>(); // Retourner une liste vide
            }
        });
        for (LineCutDTO line : lines) {
            line.color = color;
        }
        return lines;
    }
    
    // <editor-fold desc="DOMAIN">
    private ParallelCut convertToParallelCutFromDTO(ParallelCutDTO cutDTO, CNC cnc) {
        RegularCut referenceCut = cnc.getRegularCutById(cutDTO.referenceID);
        if (referenceCut == null) {
            throw new IllegalArgumentException("Reference cut not found");
        }
        return new ParallelCut(cutDTO.depth, toolConverter.convertToToolFrom(cutDTO.toolDTO), referenceCut, cutDTO.distance);
    }
    
    private LShapedCut convertToLShapedCutFromDTO(LShapedCutDTO cutDTO){
        return new LShapedCut(cutDTO.depth, toolConverter.convertToToolFrom(cutDTO.toolDTO), cutDTO.reference, cutDTO.intersection);
    }
    
    private RectangularCut convertToRectangularCutFromDTO(RectangularCutDTO cutDTO,CNC cnc){ // Aller chercher une référence à la vraie cut avec son ID
        RectangularCut referenceCut = cnc.getRectangularCutById(cutDTO.getId());
        if (referenceCut == null) {
            return new RectangularCut(cutDTO.depth, toolConverter.convertToToolFrom(cutDTO.toolDTO), cutDTO.reference, cutDTO.intersection, cutDTO.corner);
        }
        return new RectangularCut(cutDTO.depth, toolConverter.convertToToolFrom(cutDTO.toolDTO), referenceCut.getReference(), cutDTO.intersection, cutDTO.corner);
    }
    
    private BorderCut convertToBorderCutFromDTO(BorderCutDTO cutDTO) {
        return new BorderCut(cutDTO.getDepth(), toolConverter.convertToToolFrom(cutDTO.getTool()), cutDTO.getOrigin(), cutDTO.getDestination(), cutDTO.parent);
    }

    private ReCut convertToRecutFromDTO(ReCutDTO cutDTO, CNC cnc) {

        if (cutDTO.panel == null) {
            return new ReCut(cutDTO.getDepth(), toolConverter.convertToToolFrom(cutDTO.getTool()), cutDTO.finaleSize);
        }

        try {
            return new ReCut(cutDTO.getDepth(), toolConverter.convertToToolFrom(cutDTO.getTool()), cutDTO.finaleSize, panelConverter.ConvertFromDTO(cutDTO.panel, cnc));
        } catch (Exception e) {
            System.out.println("Error during panel conversion: " + e.getMessage());
            throw e;
        }
    }
    // </editor-fold>

    // <editor-fold desc="DTO">
    private ParallelCutDTO convertToDTOFromParallelCut(ParallelCut cut){
        return new ParallelCutDTO(cut.getId(), cut.getDepth(), toolConverter.convertToDTOFrom(cut.getTool()), cut.getReferenceCut().getId(), cut.getDistance());
    }

    private LShapedCutDTO convertToDTOFromLShapedCut(LShapedCut cut){
        return new LShapedCutDTO(cut.getId(), cut.getDepth(), toolConverter.convertToDTOFrom(cut.getTool()), cut.getReference(), cut.getIntersection());
    }

    private RectangularCutDTO convertToDTOFromRectangularCut(RectangularCut cut){
        return new RectangularCutDTO(cut.getId(), cut.getDepth(), toolConverter.convertToDTOFrom(cut.getTool()), cut.getReference(), cut.getIntersection(), cut.getIntersection());
    }
    
    private BorderCutDTO convertToDTOFromBorderCut(BorderCut cut) {
        return new BorderCutDTO(cut.getId(), cut.getDepth(), toolConverter.convertToDTOFrom(cut.getTool()), cut.getOrigin(), cut.getDestination(), cut.getParent());
    }
    // </editor-fold>

    // <editor-fold desc="LINE">
    public List<LineCutDTO> convertToLineDTOFromParallelCut(ParallelCut cut) {
        return new ArrayList<LineCutDTO>() {{
            add(new LineCutDTO(cut.getId(), cut.getDepth(), toolConverter.convertToDTOFrom(cut.getTool()), cut.getOrigin(), cut.getDestination(), null));
        }};
    }
    
    public List<LineCutDTO> convertToLineDTOFromLShapedCut(LShapedCut cut) {
        return new ArrayList<LineCutDTO>() {{
            add(new LineCutDTO(cut.getId(), cut.getDepth(), toolConverter.convertToDTOFrom(cut.getTool()), cut.getHorizontalCut().getOrigin(), cut.getHorizontalCut().getDestination(), null));
            add(new LineCutDTO(cut.getId(), cut.getDepth(), toolConverter.convertToDTOFrom(cut.getTool()), cut.getVerticalCut().getOrigin(), cut.getVerticalCut().getDestination(), null));
        }};
    }
    
    public List<LineCutDTO> convertToLineDTOFromBorderCut(BorderCut cut) {
        return new ArrayList<LineCutDTO>() {{
            add(new LineCutDTO(cut.getId(), cut.getDepth(), toolConverter.convertToDTOFrom(cut.getTool()), cut.getOrigin(), cut.getDestination(), null));
        }};
    }
    
    public List<LineCutDTO> convertToLineDTOFromRectangularCut(RectangularCut cut) {
        return new ArrayList<LineCutDTO>() {{
            add(new LineCutDTO(cut.getId(), cut.getDepth(), toolConverter.convertToDTOFrom(cut.getTool()), cut.getLeftVerticalCut().getOrigin(), cut.getLeftVerticalCut().getDestination(), null));
            add(new LineCutDTO(cut.getId(), cut.getDepth(), toolConverter.convertToDTOFrom(cut.getTool()), cut.getRightVerticalCut().getOrigin(), cut.getRightVerticalCut().getDestination(), null));
            add(new LineCutDTO(cut.getId(), cut.getDepth(), toolConverter.convertToDTOFrom(cut.getTool()), cut.getBottomHorizontalCut().getOrigin(), cut.getBottomHorizontalCut().getDestination(), null));
            add(new LineCutDTO(cut.getId(), cut.getDepth(), toolConverter.convertToDTOFrom(cut.getTool()), cut.getTopHorizontalCut().getOrigin(), cut.getTopHorizontalCut().getDestination(), null));

        }};
    }

    // </editor-fold>
}
