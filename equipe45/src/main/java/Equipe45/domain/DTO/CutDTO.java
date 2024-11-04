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
    public float depht;
    public Tool tool;


    public CutDTO(UUID id, float depht, Tool tool) {
        this.id = id;
        this.depht = depht;
        this.tool = tool;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public float getDepht() {
        return depht;
    }

    public void setDepht(float depht) {
        this.depht = depht;
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
                ", depht=" + depht +
                ", tool=" + tool +
                '}';
    }
}
