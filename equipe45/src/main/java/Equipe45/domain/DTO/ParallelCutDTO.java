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
public class ParallelCutDTO extends CutDTO {

    public UUID referenceID;
    public int distance;

    public ParallelCutDTO(UUID id, float depth, ToolDTO tool, UUID referenceID, int distance) {
        super(id, depth, tool);
        this.referenceID = referenceID;
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "ParallelCutDTO{" +
                "referenceID=" + referenceID +
                ", distance=" + distance +
                ", id=" + id +
                ", depth=" + depth +
                ", tool=" + toolDTO +
                '}';
    }
}
