/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Equipe45.domain;

import Equipe45.domain.Utils.Coordinate;
import Equipe45.domain.Utils.Dimension;
import Equipe45.domain.Utils.ReferenceCoordinate;

/**
 *
 * @author mat18
 */
public abstract class IrregularCut extends Cut {
    
    protected ReferenceCoordinate reference;
    protected Coordinate intersection;

    public IrregularCut(float depth, Tool tool, ReferenceCoordinate reference, Coordinate intersection) {
        super(depth, tool);
        this.reference = reference;
        this.intersection = intersection;
    }
    
    @Override
    public IrregularCut asIrregularCut() {
        return this;
    }

    public ReferenceCoordinate getReference() {
        return reference;
    }
        
    public Coordinate getIntersection() {
        return intersection;
    }

    public abstract void setReference(ReferenceCoordinate reference);

    public abstract void setIntersection(Coordinate intersection);

    public abstract void modifyDimension(Dimension dimension);
}
