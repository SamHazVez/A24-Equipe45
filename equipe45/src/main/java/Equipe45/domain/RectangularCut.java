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
public class RectangularCut extends IrregularCut {
    private Coordinate corner;
    private BorderCut bottomHorizontalCut;
    private BorderCut topHorizontalCut;
    private BorderCut leftVerticalCut;
    private BorderCut rightVerticalCut;

    public RectangularCut(float depth, Tool tool, ReferenceCoordinate reference, Coordinate intersection, Coordinate corner) {
        super(depth, tool, reference, intersection);

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
            System.out.println("Reference top left. Intersection bottom right");
            this.leftVerticalCut = new BorderCut(depth, tool, topLeft, bottomLeft);
            this.bottomHorizontalCut = new BorderCut(depth, tool, bottomLeft, bottomRight);
            this.rightVerticalCut = new BorderCut(depth, tool, bottomRight, topRight);
            this.topHorizontalCut = new BorderCut(depth, tool, topLeft, topRight);
        } else if (reference.getX() == topRight.getX() && reference.getY() == topRight.getY() &&
                intersection.getX() == bottomLeft.getX() && intersection.getY() == bottomLeft.getY()) {
            System.out.println("Reference top right. Intersection bottom left");
            this.leftVerticalCut = new BorderCut(depth, tool, bottomLeft, topLeft);
            this.bottomHorizontalCut = new BorderCut(depth, tool, bottomLeft, bottomRight);
            this.rightVerticalCut = new BorderCut(depth, tool, topRight, bottomRight);
            this.topHorizontalCut = new BorderCut(depth, tool, topLeft, topRight);
        } else if (reference.getX() == bottomLeft.getX() && reference.getY() == bottomLeft.getY() &&
                intersection.getX() == topRight.getX() && intersection.getY() == topRight.getY()) {
            System.out.println("Reference bottom left. Intersection top right");
            this.leftVerticalCut = new BorderCut(depth, tool, bottomLeft, topLeft);
            this.bottomHorizontalCut = new BorderCut(depth, tool, bottomLeft, bottomRight);
            this.rightVerticalCut = new BorderCut(depth, tool, bottomRight, topRight);
            this.topHorizontalCut = new BorderCut(depth, tool, topLeft, topRight);
        } else if (reference.getX() == bottomRight.getX() && reference.getY() == bottomRight.getY() &&
                intersection.getX() == topLeft.getX() && intersection.getY() == topLeft.getY()) {
            System.out.println("Reference bottom right. Intersection top left");
            this.leftVerticalCut = new BorderCut(depth, tool, topLeft, bottomLeft);
            this.bottomHorizontalCut = new BorderCut(depth, tool, bottomLeft, bottomRight);
            this.rightVerticalCut = new BorderCut(depth, tool, bottomRight, topRight);
            this.topHorizontalCut = new BorderCut(depth, tool, topLeft, topRight);
        }
    }


    public Coordinate getCorner() {
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
}
