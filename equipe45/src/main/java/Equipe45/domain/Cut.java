/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Equipe45.domain;

import Equipe45.domain.Utils.CutType;
import java.util.UUID;

/**
 *
 * @author mat18
 */
public abstract class Cut {
    
    protected float depth;
    protected Tool tool;
    protected UUID id;
    protected boolean isInvalidReference;

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
    
    public RegularCut asRegularCut() {
        throw new UnsupportedOperationException("Pas une coupe régulière");
    }
    
    public ParallelCut asParallelCut() {
        throw new UnsupportedOperationException("Pas une coupe parallèle");
    }
    
    
    public IrregularCut asIrregularCut() {
        throw new UnsupportedOperationException("Pas une coupe irrégulière");
    }
    
    public void setReferenceInvalid(){
        this.isInvalidReference = true;
    }
    
    public abstract CutType getType();
    
    public abstract void recalculate();
    
    public abstract boolean isValid();

    @Override
    public String toString() {
        return "Cut{" +
                "depth=" + depth +
                ", tool=" + tool +
                ", id=" + id +
                '}';
    }
}
