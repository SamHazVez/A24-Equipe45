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

    public Cut convertToCutFrom(CutDTO cutDTO, CNC cnc) {
        return switch (cutDTO) {
            case ParallelCutDTO parallelCutDTO -> convertToParallelCutFromDTO(parallelCutDTO, cnc);
            case RegularCutDTO regularCutDTO -> convertToRegularCutFromDTO(regularCutDTO);
            case LShapedCutDTO lShapedCutDTO -> convertToLShapedCutFromDTO(lShapedCutDTO);
            default -> null;
        };
    }

    public CutDTO convertToCutDTOFrom(Cut cut) {
        return switch (cut) {
            case ParallelCut parallelCut -> convertToDTOFromParallelCut(parallelCut);
            case RegularCut regularCut -> convertToDTOFromRegularCut(regularCut);
            default -> null;
        };
    }

    private ParallelCut convertToParallelCutFromDTO(ParallelCutDTO cutDTO, CNC cnc) {
        RegularCut referenceCut = cnc.getRegularCutById(cutDTO.referenceID);
        if (referenceCut == null) {
            throw new IllegalArgumentException("Reference cut not found");
        }
        return new ParallelCut(cutDTO.getDepth(), cutDTO.getTool(), referenceCut, cutDTO.distance);
    }
    
    private RegularCut convertToRegularCutFromDTO(RegularCutDTO cutDTO) {
        return new RegularCut(cutDTO.getDepth(), cutDTO.getTool(), cutDTO.getOrigin(), cutDTO.getDestination());
    }

    private ParallelCutDTO convertToDTOFromParallelCut(ParallelCut cut){
        return new ParallelCutDTO(cut.getId(), cut.getDepth(), cut.getTool(), cut.getReferenceCut().getId(), cut.getDistance());
    }
    
    private RegularCutDTO convertToDTOFromRegularCut(RegularCut cut) {
        return new RegularCutDTO(cut.getId(), cut.getDepth(), cut.getTool(), cut.getOrigin(), cut.getDestination());
    }

    private ReCut convertToReCutFromDTO(ReCutDTO cut){
        return new ReCut(cut.depth, cut.tool, cut.finaleSize);
    }

    private ReCutDTO convertToDTOFromReCut(ReCut cut){
        return new ReCutDTO(cut.getId(), cut.getDepth(), cut.getTool(), cut.getFinalDimension());
    }

    private LShapedCut convertToLShapedCutFromDTO(LShapedCutDTO cut){
        return new LShapedCut(cut.depth, cut.tool, cut.reference, cut.intersection);
    }

    private LShapedCutDTO convertToDTOFromLShapedCut(LShapedCut cut){
        return new LShapedCutDTO(cut.getId(), cut.getDepth(), cut.getTool(), cut.getReference(), cut.getIntersection());
    }

    private RectangularCut convertToRectangularCutFromDTO(RectangularCutDTO cut,CNC cnc){ // Aller chercher une référence à la vraie cut avec son ID
        return new RectangularCut(cut.depth, cut.tool, cnc.getRectangularCutById(cut.id).getReference(), cut.intersection, cut.corner);
    }

    private RectangularCutDTO convertToDTOFromRectangularCut(RectangularCut cut){
        return new RectangularCutDTO(cut.getId(), cut.getDepth(), cut.getTool(), cut.getReference(), cut.getIntersection(), cut.getIntersection());
    }
}
