/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Equipe45.domain.DTO;

import Equipe45.domain.Tool;
import Equipe45.domain.Utils.Coordinate;

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

    public RectangularCutDTO(float depth, Tool tool, Coordinate origin, Coordinate destination, Coordinate intersection) {
        this.depth = depth;
        this.tool = tool;
        this.origin = origin;
        this.destination = destination;
        this.intersection = intersection;
    }
}
