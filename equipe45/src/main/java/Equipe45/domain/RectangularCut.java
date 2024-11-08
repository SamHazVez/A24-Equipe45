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
    private Coordinate origin;

    public RectangularCut(float depth, Tool tool, ReferenceCoordinate reference, Coordinate intersection, Coordinate origin) {
        super(depth, tool, reference, intersection);
        this.origin = intersection;
    }

    @Override
    public void CutPanel(Panel panel) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
