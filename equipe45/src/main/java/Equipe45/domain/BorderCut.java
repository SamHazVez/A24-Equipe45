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
public class BorderCut extends RegularCut {
    private IRectangular parent;

    public BorderCut(float depth, Tool tool, Coordinate origin, Coordinate destination, IRectangular parent) {
        super(depth, tool, origin, destination);
        this.parent = parent;
    }
    
    @Override 
    public void recalculate() {};
    
    @Override
    public CutType getType() {
        return isVertical() ? CutType.BORDER_VERTICAL : CutType.BORDER_HORIZONTAL;
    }
    
    @Override
    public boolean isValid() {
        return true;
    }

    public IRectangular getParent() {
        return parent;
    }
}