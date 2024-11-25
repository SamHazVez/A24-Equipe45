/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Equipe45.domain.Converter;

import Equipe45.domain.*;
import Equipe45.domain.DTO.*;
import Equipe45.domain.Utils.ReferenceCoordinate;

/**
 *
 * @author mat18
 */
public class CutConverter {

    public Cut convertToCutFrom(CutDTO cutDTO, CNC cnc) {
        return switch (cutDTO) {
            case ParallelCutDTO parallelCutDTO -> convertToParallelCutFromDTO(parallelCutDTO, cnc);
            case LShapedCutDTO lShapedCutDTO -> convertToLShapedCutFromDTO(lShapedCutDTO);
            case BorderCutDTO borderCutDTO -> convertToBorderCutFromDTO(borderCutDTO);
            case RectangularCutDTO rectangularCutDTO -> convertToRectangularCutFromDTO(rectangularCutDTO, cnc);
            default -> null;
        };
    }

    public CutDTO convertToCutDTOFrom(Cut cut) {
        return switch (cut) {
            case ParallelCut parallelCut -> convertToDTOFromParallelCut(parallelCut);
            case BorderCut borderCut -> convertToDTOFromBorderCut(borderCut);
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
    
    private LShapedCut convertToLShapedCutFromDTO(LShapedCutDTO cut){
        return new LShapedCut(cut.depth, cut.tool, cut.reference, cut.intersection);
    }
    
    private RectangularCut convertToRectangularCutFromDTO(RectangularCutDTO cut,CNC cnc){ // Aller chercher une référence à la vraie cut avec son ID
        RectangularCut referenceCut = cnc.getRectangularCutById(cut.getId());
        if (referenceCut == null) {
            return new RectangularCut(cut.depth, cut.tool, cut.reference, cut.intersection, cut.corner);
        }
        return new RectangularCut(cut.depth, cut.tool, referenceCut.getReference(), cut.intersection, cut.corner);
    }
    
    private BorderCut convertToBorderCutFromDTO(BorderCutDTO cutDTO) {
        return new BorderCut(cutDTO.getDepth(), cutDTO.getTool(), cutDTO.getOrigin(), cutDTO.getDestination(), cutDTO.parent);
    }
    
    //DTO

    private ParallelCutDTO convertToDTOFromParallelCut(ParallelCut cut){
        return new ParallelCutDTO(cut.getId(), cut.getDepth(), cut.getTool(), cut.getReferenceCut().getId(), cut.getDistance());
    }

    private LShapedCutDTO convertToDTOFromLShapedCut(LShapedCut cut){
        return new LShapedCutDTO(cut.getId(), cut.getDepth(), cut.getTool(), cut.getReference(), cut.getIntersection());
    }

    private RectangularCutDTO convertToDTOFromRectangularCut(RectangularCut cut){
        return new RectangularCutDTO(cut.getId(), cut.getDepth(), cut.getTool(), cut.getReference(), cut.getIntersection(), cut.getIntersection());
    }
    
    private BorderCutDTO convertToDTOFromBorderCut(BorderCut cut) {
        return new BorderCutDTO(cut.getId(), cut.getDepth(), cut.getTool(), cut.getOrigin(), cut.getDestination(), cut.getParent());
    }
}
