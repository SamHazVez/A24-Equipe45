/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Equipe45.domain;

import Equipe45.domain.Utils.Coordinate;
import Equipe45.domain.Utils.Dimension;

import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.UUID;

/**
 *
 * @author mat18
 */
public class NoCutZone implements Serializable {
    private static final long serialVersionUID = 1L;
    private UUID id;
    private Dimension dimension;
    private Coordinate coordinate;
    private static final float DEFAULT_WIDTH = 150.0f;
    private static final float DEFAULT_HEIGHT = 150.0f;

    public NoCutZone(UUID id,Dimension dimension, Coordinate coordinate) {
        this.id = UUID.randomUUID();
        this.dimension = dimension;
        this.coordinate = coordinate;
    }

    public UUID getId() {
        return id;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public Rectangle2D.Float getBounds() {
        return new Rectangle2D.Float(coordinate.getX(), coordinate.getY(), dimension.getWidth(), dimension.getHeight());
    }


    public boolean containsCoordinate(Coordinate coordinate) {
        float x = coordinate.getX();
        float y = coordinate.getY();

        return x >= this.coordinate.getX() &&
                x <= this.coordinate.getX() + this.dimension.getWidth() &&
                y >= this.coordinate.getY() &&
                y <= this.coordinate.getY() + this.dimension.getHeight();
    }
    public boolean intersectsLine(Coordinate lineStart, Coordinate lineEnd) {
        float zoneX = this.coordinate.getX();
        float zoneY = this.coordinate.getY();
        float zoneWidth = this.dimension.getWidth();
        float zoneHeight = this.dimension.getHeight();

        Coordinate topLeft = new Coordinate(zoneX, zoneY);
        Coordinate topRight = new Coordinate(zoneX + zoneWidth, zoneY);
        Coordinate bottomLeft = new Coordinate(zoneX, zoneY + zoneHeight);
        Coordinate bottomRight = new Coordinate(zoneX + zoneWidth, zoneY + zoneHeight);

        if (containsCoordinate(lineStart) || containsCoordinate(lineEnd)) {
            return true;
        }

        return lineIntersects(lineStart, lineEnd, topLeft, topRight) ||
                lineIntersects(lineStart, lineEnd, topRight, bottomRight) ||
                lineIntersects(lineStart, lineEnd, bottomRight, bottomLeft) ||
                lineIntersects(lineStart, lineEnd, bottomLeft, topLeft);
    }

    private boolean lineIntersects(Coordinate p1, Coordinate p2, Coordinate p3, Coordinate p4) {
        float denominator = (p4.getY() - p3.getY()) * (p2.getX() - p1.getX()) - (p4.getX() - p3.getX()) * (p2.getY() - p1.getY());
        if (denominator == 0) {
            return false;
        }

        float ua = ((p4.getX() - p3.getX()) * (p1.getY() - p3.getY()) - (p4.getY() - p3.getY()) * (p1.getX() - p3.getX())) / denominator;
        float ub = ((p2.getX() - p1.getX()) * (p1.getY() - p3.getY()) - (p2.getY() - p1.getY()) * (p1.getX() - p3.getX())) / denominator;

        return (ua >= 0 && ua <= 1 && ub >= 0 && ub <= 1);
    }

}
