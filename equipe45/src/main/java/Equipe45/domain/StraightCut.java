/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Equipe45.domain;

import Equipe45.domain.Utils.Coordinate;

/**
 *
 * @author mat18
 */
public class StraightCut extends RegularCut {

    public StraightCut(float depth, Tool tool, Coordinate origin, Coordinate destination) {
        super(depth, tool, origin, destination);
    }

    @Override
    public void CutPanel(Panel panel) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
