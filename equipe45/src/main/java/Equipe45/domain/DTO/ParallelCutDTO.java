/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Equipe45.domain.DTO;

import Equipe45.domain.Tool;
import Equipe45.domain.Utils.Coordinate;
import Equipe45.domain.Utils.Dimension;

import java.util.UUID;

/**
 *
 * @author mat18
 */
public class ParallelCutDTO extends CutDTO {

    public Coordinate origin;
    public Coordinate destination;
    public Dimension finalSize;


    public ParallelCutDTO(UUID id, float depht, Tool tool, Coordinate origin, Coordinate destination, Dimension finalSize) {
        super(id, depht, tool);
        this.origin = origin;
        this.destination = destination;
        this.finalSize = finalSize;
    }

    public Coordinate getOrigin() {
        return origin;
    }

    public void setOrigin(Coordinate origin) {
        this.origin = origin;
    }

    public Coordinate getDestination() {
        return destination;
    }

    public void setDestination(Coordinate destination) {
        this.destination = destination;
    }

    public Dimension getFinalSize() {
        return finalSize;
    }

    public void setFinalSize(Dimension finalSize) {
        this.finalSize = finalSize;
    }

    @Override
    public String toString() {
        return "ParallelCutDTO{" +
                "origin=" + origin +
                ", destination=" + destination +
                ", finalSize=" + finalSize +
                ", id=" + id +
                ", depht=" + depht +
                ", tool=" + tool +
                '}';
    }
}
