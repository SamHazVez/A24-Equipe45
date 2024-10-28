/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Equipe45.domain;

import Equipe45.domain.Utils.Dimension;

/**
 *
 * @author mat18
 */
public class ReCut extends Cut {

    private Dimension finalDimension;

    public ReCut(float depth, Tool tool, Dimension finalDimension) {
        super(depth, tool);
        this.finalDimension = finalDimension;
    }

    @Override
    public void CutPanel(Panel panel) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public Dimension getFinalDimension() {
        return finalDimension;
    }
}
