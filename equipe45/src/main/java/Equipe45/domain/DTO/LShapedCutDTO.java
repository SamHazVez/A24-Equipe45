/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Equipe45.domain.DTO;

import Equipe45.domain.Tool;
import Equipe45.domain.Utils.Coordinate;
import Equipe45.domain.Utils.ReferenceCoordinate;

import java.util.UUID;

/**
 *
 * @author mat18
 */
public class LShapedCutDTO extends CutDTO {
    
    public ReferenceCoordinate reference;
    public Coordinate intersection;


    public LShapedCutDTO(UUID id, float depth, Tool tool, ReferenceCoordinate reference, Coordinate intersection) {
        super(id, depth, tool);
        this.reference = reference;
        this.intersection = intersection;
    }

    public ReferenceCoordinate getReference() {
        return reference;
    }

    public Coordinate getIntersection() {
        return intersection;
    }

    @Override
    public String toString() {
        return "LShapedCutDTO{" +
                ", reference=" + reference +
                ", intersection=" + intersection +
                ", id=" + id +
                ", depth=" + depth +
                ", tool=" + tool +
                '}';
    }
}
