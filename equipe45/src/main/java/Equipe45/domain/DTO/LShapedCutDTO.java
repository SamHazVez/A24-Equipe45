/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Equipe45.domain.DTO;

import Equipe45.domain.Utils.Coordinate;

/**
 *
 * @author mat18
 */
public class LShapedCutDTO extends CutDTO {
    
    public Coordinate origin;
    public Coordinate destination;
    public Coordinate intersection;

    public LShapedCutDTO(Coordinate origin, Coordinate destination, Coordinate intersection) {
        this.origin = origin;
        this.destination = destination;
        this.intersection = intersection;
    }
}
