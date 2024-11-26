/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Equipe45.domain;

import Equipe45.domain.Utils.Coordinate;
import Equipe45.domain.Utils.CutType;

/**
 *
 * @author mat18
 */
public abstract class RegularCut extends Cut {
    private Coordinate origin;
    private Coordinate destination;

    public RegularCut(float depth, Tool tool, Coordinate origin, Coordinate destination) {
        super(depth, tool);
        this.origin = origin;
        this.destination = destination;
    }
    
    @Override
    public RegularCut asRegularCut() {
        return this;
    }

    public Coordinate getDestination() {
        return destination;
    }

    public Coordinate getOrigin() {
        return origin;
    }

    public void setOrigin(Coordinate origin) {
        this.origin = origin;
    }

    public void setDestination(Coordinate destination) {
        this.destination = destination;
    }

    public boolean isHorizontal() {
        return this.origin.getY() == this.destination.getY();
    }

    public boolean isVertical() {
        return this.origin.getX() == this.destination.getX();
    }
    
    @Override
    public CutType getType() {
        return isVertical() ? CutType.VERTICAL : CutType.HORIZONTAL;
    }
}
