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
public class RectangularCutDTO extends CutDTO {

    public float depth;
    public Tool tool;
    public Coordinate origin;
    public Coordinate destination;
    public Coordinate intersection;

    public RectangularCutDTO(UUID id, float depth, Tool tool, Coordinate origin, Coordinate destination, Coordinate intersection) {
        super(id, depth, tool);
        this.origin = origin;
        this.destination = destination;
        this.intersection = intersection;
    }

    public float getDepth() {
        return depth;
    }

    public void setDepth(float depth) {
        this.depth = depth;
    }

    @Override
    public Tool getTool() {
        return tool;
    }

    @Override
    public void setTool(Tool tool) {
        this.tool = tool;
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

    public Coordinate getIntersection() {
        return intersection;
    }

    public void setIntersection(Coordinate intersection) {
        this.intersection = intersection;
    }

    @Override
    public String toString() {
        return "RectangularCutDTO{" +
                "depth=" + depth +
                ", tool=" + tool +
                ", origin=" + origin +
                ", destination=" + destination +
                ", intersection=" + intersection +
                ", id=" + id +
                ", depht=" + depht +
                ", tool=" + tool +
                '}';
    }
}
