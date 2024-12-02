/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Equipe45.domain.DTO;

import Equipe45.domain.Utils.Coordinate;
import java.awt.Color;
import java.util.UUID;

public class LineCutDTO extends CutDTO {
    public Coordinate origin;
    public Coordinate destination;
    public Color color;

    public LineCutDTO(UUID id, float depth, ToolDTO tool, Coordinate origin, Coordinate destination, Color color) {
        super(id, depth, tool);
        this.origin = origin;
        this.destination = destination;
        this.color = color;
    }

    public boolean isHorizontal() {
        return this.origin.getY() == this.destination.getY();
    }

    public boolean isVertical() {
        return this.origin.getX() == this.destination.getX();
    }
}
