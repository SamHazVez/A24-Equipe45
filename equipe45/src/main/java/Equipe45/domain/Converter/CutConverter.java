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

    public Cut convertToCutFrom(CutDTO cutDTO) {
        return switch (cutDTO){
            case StraightCutDTO straightCutDTO -> convertToStraightCutFromDTO(straightCutDTO);
            case ReCutDTO reCutDTO -> convertToReCutFromDTO(reCutDTO);
            case ParallelCutDTO parallelCutDTO -> convertToParallelCutFromDTO(parallelCutDTO);
            case LShapedCutDTO lShapedCutDTO -> convertToLShapedCutFromDTO(lShapedCutDTO);
            case RectangularCutDTO rectangularCutDTO -> convertToRectangularCutFromDTO(rectangularCutDTO);
            default -> null; //ajouter exception
        };
    }

    public CutDTO convertToCutDTOFrom(Cut cut) {
        return switch (cut){
            case StraightCut straightCut -> convertToDTOFromStraightCut(straightCut);
            case ReCut reCut -> convertToDTOFromReCut(reCut);
            case ParallelCut parallelCut -> convertToDTOFromParallelCut(parallelCut);
            case LShapedCut lShapedCut -> convertToDTOFromLShapedCut(lShapedCut);
            case RectangularCut rectangularCut -> convertToDTOFromRectangularCut(rectangularCut);
            default -> null;
        };
    }

    private StraightCut convertToStraightCutFromDTO(StraightCutDTO cut){
        return new StraightCut(cut.)
    }

    private StraightCutDTO convertToDTOFromStraightCut(StraightCut cut){
        return null;
    }

    private ReCut convertToReCutFromDTO(ReCutDTO cut){
        return null;
    }

    private ReCutDTO convertToDTOFromReCut(ReCut cut){
        return null;
    }

    private ParallelCut convertToParallelCutFromDTO(ParallelCutDTO cut){
        return null;
    }

    private ParallelCutDTO convertToDTOFromParallelCut(ParallelCut cut){
        return null;
    }

    private LShapedCut convertToLShapedCutFromDTO(LShapedCutDTO cut){
        return null;
    }

    private LShapedCutDTO convertToDTOFromLShapedCut(LShapedCut cut){
        return null;
    }

    private RectangularCut convertToRectangularCutFromDTO(RectangularCutDTO cut){
        return null;
    }

    private RectangularCutDTO convertToDTOFromRectangularCut(RectangularCut cut){
        return null;
    }
}
