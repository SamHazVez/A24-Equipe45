/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Equipe45.domain;

import Equipe45.domain.Utils.Coordinate;

import java.awt.*;

/**
 *
 * @author mat18
 */
public abstract class IrregularCut extends RegularCut {
    
    private Coordinate intersection;

    public void setIntersection(Coordinate intersection) {
        this.intersection = intersection;
    }

    public IrregularCut(float depth, Tool tool, Coordinate origin, Coordinate destination, Coordinate intersection) {
        super(depth, tool, origin, destination);
        this.intersection = intersection;
    }

    @Override
    abstract public void CutPanel(Panel panel);

    public Coordinate getIntersection() {
        return intersection;
    }
}
