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
public class LShapedCut extends IrregularCut implements IRectangular {
    private StraightCutForL horizontalCut;
    private StraightCutForL verticalCut;

    public LShapedCut(float depth, Tool tool, ReferenceCoordinate reference, Coordinate intersection) {
        super(depth, tool, reference, intersection);

        if(reference.horizontalCut instanceof BorderCut borderCut)
        {
            if(!borderCut.getParent().isCoordinateInRectangle(intersection)){
                throw new IllegalArgumentException("L'intersection doit être dans le rectangle.");
            }
        }
        if(reference.verticalCut instanceof BorderCut borderCut){
            if(!borderCut.getParent().isCoordinateInRectangle(intersection)){
                throw new IllegalArgumentException("L'intersection doit être dans le rectangle.");
            }
        }

        if(reference.horizontalCut instanceof StraightCutForL straightCutForL){
            if(!straightCutForL.getParent().isCoordinateInRectangle(intersection)){
                throw new IllegalArgumentException("L'intersection doit être dans le rectangle.");
            }
        }

        if(reference.verticalCut instanceof StraightCutForL straightCutForL){
            if(!straightCutForL.getParent().isCoordinateInRectangle(intersection)){
                throw new IllegalArgumentException("L'intersection doit être dans le rectangle.");
            }
        }

        this.horizontalCut = new StraightCutForL(depth, tool, reference.horizontalCut, new Coordinate(reference.getX(), reference.getY()),intersection, this);
        this.verticalCut = new StraightCutForL(depth, tool, reference.verticalCut, new Coordinate(reference.getX(), reference.getY()),intersection, this);
    }

    public StraightCutForL getHorizontalCut() {
        return horizontalCut;
    }

    public StraightCutForL getVerticalCut() {
        return verticalCut;
    }
    
    @Override
    public void setReference(ReferenceCoordinate reference) {
        this.reference = reference;
        this.horizontalCut = new StraightCutForL(depth, tool, reference.horizontalCut, new Coordinate(reference.getX(), reference.getY()),intersection, this);
        this.verticalCut = new StraightCutForL(depth, tool, reference.verticalCut, new Coordinate(reference.getX(), reference.getY()),intersection, this);
    }

    @Override

    public void setIntersection(Coordinate intersection) {
        this.intersection = intersection;
        this.horizontalCut = new StraightCutForL(depth, tool, reference.horizontalCut, new Coordinate(reference.getX(), reference.getY()), intersection, this);
        this.verticalCut = new StraightCutForL(depth, tool, reference.verticalCut, new Coordinate(reference.getX(), reference.getY()), intersection, this);
    }

    @Override
    public boolean isCoordinateInRectangle(Coordinate coordinate) {
        float minX = Math.min(this.getReference().getX(), this.getIntersection().getX());
        float maxX = Math.max(this.getReference().getX(), this.getIntersection().getX());
        float minY = Math.min(this.getReference().getY(), this.getIntersection().getY());
        float maxY = Math.max(this.getReference().getY(), this.getIntersection().getY());

        return coordinate.getX() >= minX && coordinate.getX() <= maxX &&
                coordinate.getY() >= minY && coordinate.getY() <= maxY;
    }
    
    @Override
    public boolean isValid() {
        return reference.isValid() && intersection != null;
    }
}
