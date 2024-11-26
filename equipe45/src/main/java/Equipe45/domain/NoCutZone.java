/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Equipe45.domain;

import Equipe45.domain.Utils.Coordinate;
import Equipe45.domain.Utils.Dimension;

import java.awt.geom.Rectangle2D;
import java.util.UUID;

/**
 *
 * @author mat18
 */
public class NoCutZone {

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

    // Getter et Setter pour les coordonnées
    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public Rectangle2D.Float getBounds() {
        return new Rectangle2D.Float(coordinate.getX(), coordinate.getY(), dimension.getWidth(), dimension.getHeight());
    }

    public boolean contains(Coordinate point) {
        return point.getX() >= coordinate.getX()
                && point.getX() <= coordinate.getX() + dimension.getWidth()
                && point.getY() >= coordinate.getY()
                && point.getY() <= coordinate.getY() + dimension.getHeight();
    }

    public boolean intersects(Coordinate origin, Coordinate destination) {
        Rectangle2D.Float bounds = getBounds();

        return bounds.intersectsLine(
                origin.getX(), origin.getY(),
                destination.getX(), destination.getY()
        );
    }
}
