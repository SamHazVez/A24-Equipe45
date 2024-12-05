/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Equipe45.domain;

import Equipe45.domain.Utils.Coordinate;
import Equipe45.domain.Utils.CutType;
import Equipe45.domain.Utils.Dimension;
import Equipe45.domain.Utils.ReferenceCoordinate;

/**
 *
 * @author mat18
 */
public class RectangularCut extends IrregularCut implements IRectangular {
    private Coordinate corner;
    private Coordinate otherCorner;
    private BorderCut bottomHorizontalCut;
    private BorderCut topHorizontalCut;
    private BorderCut leftVerticalCut;
    private BorderCut rightVerticalCut;

    public RectangularCut(float depth, Tool tool, ReferenceCoordinate reference, Coordinate intersection, Coordinate corner) {
        super(depth, tool, reference, intersection);
        this.corner = corner;

        recalculate();
    }
    
    @Override
    public RectangularCut asRectangularCut() {
        return this;
    }
    
    @Override
    public void recalculate() {
        float minX = Math.min(intersection.getX(), corner.getX());
        float maxX = Math.max(intersection.getX(), corner.getX());
        float minY = Math.min(intersection.getY(), corner.getY());
        float maxY = Math.max(intersection.getY(), corner.getY());

        Coordinate bottomLeft = new Coordinate(minX, minY);
        Coordinate topLeft = new Coordinate(minX, maxY);
        Coordinate bottomRight = new Coordinate(maxX, minY);
        Coordinate topRight = new Coordinate(maxX, maxY);

        if (intersection.getX() == topLeft.getX() && intersection.getY() == topLeft.getY() &&
                corner.getX() == bottomRight.getX() && corner.getY() == bottomRight.getY()) {
            this.otherCorner = bottomLeft;

            this.leftVerticalCut = new BorderCut(depth, tool, topLeft, bottomLeft, this);
            this.bottomHorizontalCut = new BorderCut(depth, tool, bottomLeft, bottomRight, this);
            this.rightVerticalCut = new BorderCut(depth, tool, bottomRight, topRight, this);
            this.topHorizontalCut = new BorderCut(depth, tool, topLeft, topRight, this);
        } else if (intersection.getX() == topRight.getX() && intersection.getY() == topRight.getY() &&
                corner.getX() == bottomLeft.getX() && corner.getY() == bottomLeft.getY()) {
            this.otherCorner = bottomRight;

            this.leftVerticalCut = new BorderCut(depth, tool, bottomLeft, topLeft, this);
            this.bottomHorizontalCut = new BorderCut(depth, tool, bottomLeft, bottomRight, this);
            this.rightVerticalCut = new BorderCut(depth, tool, topRight, bottomRight, this);
            this.topHorizontalCut = new BorderCut(depth, tool, topLeft, topRight, this);
        } else if (intersection.getX() == bottomLeft.getX() && intersection.getY() == bottomLeft.getY() &&
                corner.getX() == topRight.getX() && corner.getY() == topRight.getY()) {
            this.otherCorner = topLeft;

            this.leftVerticalCut = new BorderCut(depth, tool, bottomLeft, topLeft, this);
            this.bottomHorizontalCut = new BorderCut(depth, tool, bottomLeft, bottomRight, this);
            this.rightVerticalCut = new BorderCut(depth, tool, bottomRight, topRight, this);
            this.topHorizontalCut = new BorderCut(depth, tool, topLeft, topRight, this);
        } else if (intersection.getX() == bottomRight.getX() && intersection.getY() == bottomRight.getY() &&
                corner.getX() == topLeft.getX() && corner.getY() == topLeft.getY()) {
            this.otherCorner = topRight;

            this.leftVerticalCut = new BorderCut(depth, tool, topLeft, bottomLeft, this);
            this.bottomHorizontalCut = new BorderCut(depth, tool, bottomLeft, bottomRight, this);
            this.rightVerticalCut = new BorderCut(depth, tool, bottomRight, topRight, this);
            this.topHorizontalCut = new BorderCut(depth, tool, topLeft, topRight, this);
        }
    }

    public Coordinate getCorner() {
        return corner;
    }

    public Coordinate getOtherCorner() {
        return corner;
    }

    public BorderCut getBottomHorizontalCut() {
        return bottomHorizontalCut;
    }

    public BorderCut getTopHorizontalCut() {
        return topHorizontalCut;
    }

    public BorderCut getLeftVerticalCut() {
        return leftVerticalCut;
    }

    public BorderCut getRightVerticalCut() {
        return rightVerticalCut;
    }

    @Override
    public void setReference(ReferenceCoordinate reference) {
        this.reference = reference;
    }
    
    @Override
    public void setIntersection(Coordinate intersection) {}

    @Override
    public void modifyDimension(Dimension dimension) {
        if (dimension == null) {
            throw new IllegalArgumentException("Dimension cannot be null");
        }
        this.dimension = dimension;
    }

    public void setCorner(Coordinate corner) {
        this.corner = corner;
    }
    
    @Override
    public CutType getType(){
        return CutType.RECTANGULAR;
    }
    
    @Override
    public boolean isValid() {
        return reference.isValid() && intersection != null && corner != null;
    }

    public boolean isCoordinateInRectangle(Coordinate coordinate) {
        float minX = Math.min(this.getReference().getX(), this.getIntersection().getX());
        float maxX = Math.max(this.getReference().getX(), this.getIntersection().getX());
        float minY = Math.min(this.getReference().getY(), this.getIntersection().getY());
        float maxY = Math.max(this.getReference().getY(), this.getIntersection().getY());

        return !(coordinate.getX() >= minX && coordinate.getX() <= maxX &&
                coordinate.getY() >= minY && coordinate.getY() <= maxY);
    }
}
