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
public abstract class RegularCut extends Cut {
    
    private ReferenceCoordinate origin;
    private ReferenceCoordinate destination;

    public RegularCut(float depth, Tool tool, ReferenceCoordinate origin, ReferenceCoordinate destination) {
        super(depth, tool);
        this.origin = origin;
        this.destination = destination;
    }

    @Override
    abstract public void CutPanel(Panel panel);

    public ReferenceCoordinate getDestination() {
        return destination;
    }

    public ReferenceCoordinate getOrigin() {
        return origin;
    }
}
