/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Equipe45.domain.DTO;

import java.util.UUID;

public abstract class CutDTO {
    
    public UUID id;
    public float depth;
    public ToolDTO toolDTO;


    public CutDTO(UUID id, float depth, ToolDTO tool) {
        this.id = id;
        this.depth = depth;
        this.toolDTO = tool;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public float getDepth() {
        return depth;
    }

    public void setDepth(float depth) {
        this.depth = depth;
    }

    public ToolDTO getTool() {
        return toolDTO;
    }

    public void setTool(ToolDTO tool) {
        this.toolDTO = tool;
    }

    @Override
    public String toString() {
        return "CutDTO{" +
                "id=" + id +
                ", depth=" + depth +
                ", tool=" + toolDTO +
                '}';
    }
}
