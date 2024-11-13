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

    public Cut convertToCutFrom(CutDTO cutDTO) {
        return switch (cutDTO){
            case ParallelCutDTO parallelCutDTO -> convertToParallelCutFromDTO(parallelCutDTO);
            case ReCutDTO reCutDTO -> convertToReCutFromDTO(reCutDTO);
            case LShapedCutDTO lShapedCutDTO -> convertToLShapedCutFromDTO(lShapedCutDTO);
            case RectangularCutDTO rectangularCutDTO -> convertToRectangularCutFromDTO(rectangularCutDTO);
            default -> null; //ajouter exception
        };
    }

    public CutDTO convertToCutDTOFrom(Cut cut) {
        return switch (cut){
            case ParallelCut parallelCut -> convertToDTOFromParallelCut(parallelCut);
            case ReCut reCut -> convertToDTOFromReCut(reCut);
            case LShapedCut lShapedCut -> convertToDTOFromLShapedCut(lShapedCut);
            case RectangularCut rectangularCut -> convertToDTOFromRectangularCut(rectangularCut);
            default -> null;
        };
    }

    private ParallelCut convertToParallelCutFromDTO(ParallelCutDTO cut){
        return new ParallelCut(cut.depth, cut.tool, new ReferenceCoordinate(cut.getOrigin(), cut.getId()), new ReferenceCoordinate(cut.getDestination(), cut.getId()));
    }

    private ParallelCutDTO convertToDTOFromParallelCut(ParallelCut cut){
        return new ParallelCutDTO(cut.getId(), cut.getDepth(), cut.getTool(), cut.getOrigin(), cut.getDestination());
    }

    private ReCut convertToReCutFromDTO(ReCutDTO cut){
        return new ReCut(cut.depth, cut.tool, cut.finaleSize);
    }

    private ReCutDTO convertToDTOFromReCut(ReCut cut){
        return new ReCutDTO(cut.getId(), cut.getDepth(), cut.getTool(), cut.getFinalDimension());
    }

    private LShapedCut convertToLShapedCutFromDTO(LShapedCutDTO cut){
        return new LShapedCut(cut.depth, cut.tool, new ReferenceCoordinate(cut.reference, cut.getId()), cut.intersection);
    }

    private LShapedCutDTO convertToDTOFromLShapedCut(LShapedCut cut){
        return new LShapedCutDTO(cut.getId(), cut.getDepth(), cut.getTool(), cut.getReference(), cut.getIntersection(), cut.getIntersection());
    }

    private RectangularCut convertToRectangularCutFromDTO(RectangularCutDTO cut){
        return new RectangularCut(cut.depth, cut.tool, new ReferenceCoordinate(cut.reference, cut.getId()), cut.intersection, cut.corner);
    }

    private RectangularCutDTO convertToDTOFromRectangularCut(RectangularCut cut){
        return new RectangularCutDTO(cut.getId(), cut.getDepth(), cut.getTool(), cut.getReference(), cut.getIntersection(), cut.getIntersection());
    }
}
