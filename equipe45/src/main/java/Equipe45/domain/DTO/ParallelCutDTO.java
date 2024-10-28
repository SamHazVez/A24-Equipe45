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
}
