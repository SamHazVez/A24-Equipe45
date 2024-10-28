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
public class StraightCutDTO extends CutDTO {

    public Coordinate origin;
    public Coordinate destination;

    public StraightCutDTO(Coordinate origin, Coordinate destination) {
        this.origin = origin;
        this.destination = destination;
    }
}
