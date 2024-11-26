/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Equipe45.domain.Converter;

import Equipe45.domain.*;
import Equipe45.domain.DTO.*;

/**
 *
 * @author mat18
 */
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
    
    // Domaine

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



    //DTO

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

}
