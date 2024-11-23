/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Equipe45.domain;

import java.util.UUID;

/**
 *
 * @author mat18
 */
public abstract class Cut {
    
    private float depth;
    private Tool tool;
    private UUID id;

    public Cut(float depth, Tool tool) {
        this.depth = depth;
        this.tool = tool;
        this.id = UUID.randomUUID();
    }

    public float getDepth() {
        return depth;
    }

    public Tool getTool() {
        return tool;
    }

    public UUID getId() {
        return id;
    }
    
    public abstract boolean isValid();
}
