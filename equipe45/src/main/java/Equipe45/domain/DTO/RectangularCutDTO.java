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
public class RectangularCutDTO extends CutDTO {

    public ReferenceCoordinate reference;
    public Coordinate intersection;
    public Coordinate corner;

    public RectangularCutDTO(UUID id, float depth, ToolDTO tool, ReferenceCoordinate reference, Coordinate intersection, Coordinate corner) {
        super(id, depth, tool);
        this.reference = reference;
        this.intersection = intersection;
        this.corner = corner;
    }

    @Override
    public String toString() {
        return "RectangularCutDTO{" +
                "depth=" + depth +
                ", tool=" + toolDTO +
                ", reference=" + reference +
                ", intersection=" + intersection +
                ", corner=" + corner +
                ", id=" + id +
                ", depth=" + depth +
                ", tool=" + toolDTO +
                '}';
    }
}
