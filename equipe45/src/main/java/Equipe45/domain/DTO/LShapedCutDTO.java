/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Equipe45.domain.DTO;

import Equipe45.domain.Tool;
import Equipe45.domain.Utils.Coordinate;

import java.util.UUID;

/**
 *
 * @author mat18
 */
public class LShapedCutDTO extends CutDTO {
    
    public Coordinate reference;
    public Coordinate intersection;


    public LShapedCutDTO(UUID id, float depth, Tool tool, Coordinate origin, Coordinate destination, Coordinate intersection) {
        super(id, depth, tool);
        this.reference = destination;
        this.intersection = intersection;
    }

    public Coordinate getReference() {
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
