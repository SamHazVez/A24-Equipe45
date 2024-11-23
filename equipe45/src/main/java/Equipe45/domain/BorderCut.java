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
public class BorderCut extends RegularCut {

    public BorderCut(float depth, Tool tool, Coordinate origin, Coordinate destination) {
        super(depth, tool, origin, destination);
    }
    @Override
    public boolean isValid() {
        return true;
    }
}