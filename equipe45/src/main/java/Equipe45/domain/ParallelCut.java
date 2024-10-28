/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Equipe45.domain;

import Equipe45.domain.Utils.Coordinate;
import Equipe45.domain.Utils.Dimension;

/**
 *
 * @author mat18
 */
public class ParallelCut extends RegularCut {

    private Dimension finalSize;

    public ParallelCut(float depth, Tool tool, Coordinate origin, Coordinate destination, Dimension finalSize) {
        super(depth, tool, origin, destination);
        this.finalSize = finalSize;
    }

    @Override
    public void CutPanel(Panel panel) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public Dimension getFinalSize() {
        return finalSize;
    }
}
