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
public abstract class RegularCut extends Cut {
    
    private Coordinate origin;
    private Coordinate destination;
    
    @Override
    abstract public void CutPanel(Panel panel);

}
