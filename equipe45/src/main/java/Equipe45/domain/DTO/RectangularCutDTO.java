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

    public Coordinate reference;
    public Coordinate intersection;
    public Coordinate corner;

    public RectangularCutDTO(UUID id, float depth, Tool tool, Coordinate destination, Coordinate intersection, Coordinate corner) {
        super(id, depth, tool);
        this.reference = destination;
        this.intersection = intersection;
        this.corner = corner;
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

    @Override
    public String toString() {
        return "RectangularCutDTO{" +
                "depth=" + depth +
                ", tool=" + tool +
                ", reference=" + reference +
                ", intersection=" + intersection +
                ", corner=" + corner +
                ", id=" + id +
                ", depth=" + depth +
                ", tool=" + tool +
                '}';
    }
}
