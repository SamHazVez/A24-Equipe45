/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Equipe45.domain;

import java.util.UUID;

import Equipe45.domain.Utils.CutType;

/**
 *
 * @author mat18
 */
public abstract class Cut {
    
    protected float depth;
    protected Tool tool;
    protected UUID id;
    protected boolean isInvalidReference;
    protected boolean isInNoCutZone;

    public Cut(float depth, Tool tool) {
        this.depth = depth;
        this.tool = tool;
        this.id = UUID.randomUUID();
        isInNoCutZone = false;
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
    
    public LShapedCut asLShapedCut() {
        throw new UnsupportedOperationException("Pas une coupe en L");
    }
    
    public RectangularCut asRectangularCut() {
        throw new UnsupportedOperationException("Pas une coupe rectangulaire");
    }
    
    public void setReferenceInvalid(){
        this.isInvalidReference = true;
    }
    
    public abstract CutType getType();
    
    public abstract void recalculate();
    

    public boolean isInNoCutZone() {
        return isInNoCutZone;
    }
    public void setInNoCutZone(boolean isInNoCutZone) {
        this.isInNoCutZone = isInNoCutZone;
    }

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
