/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Equipe45.domain.DTO;

import java.util.UUID;

public class ParallelCutDTO extends CutDTO {

    public UUID referenceID;
    public float distance;

    public ParallelCutDTO(UUID id, float depth, ToolDTO tool, UUID referenceID, float distance) {
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
