/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Equipe45.domain;

import Equipe45.domain.Utils.Coordinate;
import Equipe45.domain.Utils.ReferenceCoordinate;

/**
 *
 * @author mat18
 */
public abstract class IrregularCut extends Cut {
    
    private ReferenceCoordinate reference;
    private Coordinate intersection;

    public IrregularCut(float depth, Tool tool, ReferenceCoordinate reference, Coordinate intersection) {
        super(depth, tool);
        this.reference = reference;
        this.intersection = intersection;
    }

    public ReferenceCoordinate getReference() {
        return reference;
    }
        
    public Coordinate getIntersection() {
        return intersection;
    }

    public void setReference(ReferenceCoordinate reference) {
        this.reference = reference;
    }

    public void setIntersection(Coordinate intersection) {
        this.intersection = intersection;
    }
}
