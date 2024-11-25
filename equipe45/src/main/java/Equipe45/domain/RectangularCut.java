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

        boolean isValidCase =
                (reference.getX() < intersection.getX() && reference.getY() > intersection.getY()) ||
                        (reference.getX() > intersection.getX() && reference.getY() > intersection.getY()) ||
                        (reference.getX() < intersection.getX() && reference.getY() < intersection.getY()) ||
                        (reference.getX() > intersection.getX() && reference.getY() < intersection.getY());

        if (!isValidCase) {
            throw new IllegalArgumentException("Invalid reference and intersection combination.");
        }

        float avgX = (intersection.getX() + corner.getX()) / 2;

        intersection .setX(avgX);
        corner.setX(avgX);

        float minX = Math.min(reference.getX(), intersection.getX());
        float maxX = Math.max(reference.getX(), intersection.getX());
        float minY = Math.min(reference.getY(), intersection.getY());
        float maxY = Math.max(reference.getY(), intersection.getY());

        Coordinate bottomLeft = new Coordinate(minX, minY);
        Coordinate topLeft = new Coordinate(minX, maxY);
        Coordinate bottomRight = new Coordinate(maxX, minY);
        Coordinate topRight = new Coordinate(maxX, maxY);

        if (reference.getX() == topLeft.getX() && reference.getY() == topLeft.getY() &&
                intersection.getX() == bottomRight.getX() && intersection.getY() == bottomRight.getY()) {
            this.otherCorner = bottomLeft;

            this.leftVerticalCut = new BorderCut(depth, tool, topLeft, bottomLeft, this);
            this.bottomHorizontalCut = new BorderCut(depth, tool, bottomLeft, bottomRight, this);
            this.rightVerticalCut = new BorderCut(depth, tool, bottomRight, topRight, this);
            this.topHorizontalCut = new BorderCut(depth, tool, topLeft, topRight, this);
        } else if (reference.getX() == topRight.getX() && reference.getY() == topRight.getY() &&
                intersection.getX() == bottomLeft.getX() && intersection.getY() == bottomLeft.getY()) {
            this.otherCorner = bottomRight;

            this.leftVerticalCut = new BorderCut(depth, tool, bottomLeft, topLeft, this);
            this.bottomHorizontalCut = new BorderCut(depth, tool, bottomLeft, bottomRight, this);
            this.rightVerticalCut = new BorderCut(depth, tool, topRight, bottomRight, this);
            this.topHorizontalCut = new BorderCut(depth, tool, topLeft, topRight, this);
        } else if (reference.getX() == bottomLeft.getX() && reference.getY() == bottomLeft.getY() &&
                intersection.getX() == topRight.getX() && intersection.getY() == topRight.getY()) {
            this.otherCorner = topLeft;

            this.leftVerticalCut = new BorderCut(depth, tool, bottomLeft, topLeft, this);
            this.bottomHorizontalCut = new BorderCut(depth, tool, bottomLeft, bottomRight, this);
            this.rightVerticalCut = new BorderCut(depth, tool, bottomRight, topRight, this);
            this.topHorizontalCut = new BorderCut(depth, tool, topLeft, topRight, this);
        } else if (reference.getX() == bottomRight.getX() && reference.getY() == bottomRight.getY() &&
                intersection.getX() == topLeft.getX() && intersection.getY() == topLeft.getY()) {
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
    public void setReference(ReferenceCoordinate reference) {}
    
    @Override
    public void setIntersection(Coordinate intersection) {}

    public void setCorner(Coordinate corner) {
        this.corner = corner;
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

        return coordinate.getX() >= minX && coordinate.getX() <= maxX &&
                coordinate.getY() >= minY && coordinate.getY() <= maxY;
    }
}
