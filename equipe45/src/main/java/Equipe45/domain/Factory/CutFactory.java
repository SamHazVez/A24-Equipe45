/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Equipe45.domain.Factory;

import Equipe45.domain.Tool;
import Equipe45.domain.Utils.Coordinate;
import Equipe45.domain.Utils.Dimension;

/**
 *
 * @author mat18
 */
public class CutFactory {
    
    public void CreateStraightCut(float depth, Tool tool, Coordinate origin, Coordinate destination){}
    
    public void CreateLShapedCut(float depth, Tool tool, Coordinate origin, Coordinate destination, Coordinate intersection){}
    
    public void CreateRectangularCut(float depth, Tool tool, Coordinate origin, Coordinate destination, Coordinate intersection){}

    public void CreateParallelCut(float depth, Tool tool, Coordinate origin, Coordinate destination, Dimension finalSize){}
    
    public void CreateReCut(Tool tool, Dimension finalSize){}
}
