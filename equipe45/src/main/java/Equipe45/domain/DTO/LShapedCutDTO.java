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
    
    public Coordinate origin;
    public Coordinate destination;
    public Coordinate intersection;


    public LShapedCutDTO(UUID id, float depht, Tool tool, Coordinate origin, Coordinate destination, Coordinate intersection) {
        super(id, depht, tool);
        this.origin = origin;
        this.destination = destination;
        this.intersection = intersection;
    }
}
