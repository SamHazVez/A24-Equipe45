/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Equipe45.domain.DTO;

import Equipe45.domain.Utils.Coordinate;
import Equipe45.domain.Utils.Dimension;

import java.util.UUID;

/**
 *
 * @author mat18
 */
public class NoCutZoneDTO {
    private UUID id;
    public Dimension dimension;
    public Coordinate coordinate;

    public NoCutZoneDTO(UUID id,Dimension dimension, Coordinate coordinate) {
        this.id = id;
        this.dimension = dimension;
        this.coordinate = coordinate;
    }
    public NoCutZoneDTO(Dimension dimension, Coordinate coordinate) {
        this.id = UUID.randomUUID();
        this.dimension = dimension;
        this.coordinate = coordinate;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "NoCutZoneDTO{" +
                "dimension=" + dimension +
                ", coordinate=" + coordinate +
                '}';
    }
}
