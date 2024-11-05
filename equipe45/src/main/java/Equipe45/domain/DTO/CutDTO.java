/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Equipe45.domain.DTO;

import Equipe45.domain.Tool;
import java.util.UUID;

/**
 *
 * @author mat18
 */
public abstract class CutDTO {
    
    public UUID id;
    public float depth;
    public Tool tool;


    public CutDTO(UUID id, float depth, Tool tool) {
        this.id = id;
        this.depth = depth;
        this.tool = tool;
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

    public Tool getTool() {
        return tool;
    }

    public void setTool(Tool tool) {
        this.tool = tool;
    }

    @Override
    public String toString() {
        return "CutDTO{" +
                "id=" + id +
                ", depth=" + depth +
                ", tool=" + tool +
                '}';
    }
}
