/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Equipe45.domain.DTO;

import Equipe45.domain.Utils.Coordinate;
import Equipe45.domain.Utils.Dimension;

/**
 *
 * @author mat18
 */
public class ParallelCutDTO extends CutDTO {

    public Coordinate origin;
    public Coordinate destination;
    public Dimension finalSize;

    public ParallelCutDTO(Coordinate origin, Coordinate destination, Dimension finalSize) {
        this.origin = origin;
        this.destination = destination;
        this.finalSize = finalSize;
    }
}
