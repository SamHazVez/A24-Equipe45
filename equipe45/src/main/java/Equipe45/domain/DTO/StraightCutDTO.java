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
public class StraightCutDTO extends CutDTO {

    public Coordinate origin;
    public Coordinate destination;

    public StraightCutDTO(UUID id, float depth, Tool tool, Coordinate origin, Coordinate destination) {
        super(id, depth, tool);
        this.origin = origin;
        this.destination = destination;
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

    @Override
    public String toString() {
        return "StraightCutDTO{" +
                "origin=" + origin +
                ", destination=" + destination +
                ", id=" + id +
                ", depth=" + depth +
                ", tool=" + tool +
                '}';
    }
}
