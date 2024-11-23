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
public class LShapedCut extends IrregularCut {
    private StraightCUt horizontalCut;
    private StraightCUt verticalCut;

    public LShapedCut(float depth, Tool tool, ReferenceCoordinate reference, Coordinate intersection) {
        super(depth, tool, reference, intersection);

        this.horizontalCut = new StraightCUt(depth, tool, reference.horizontalCut, new Coordinate(reference.getX(), reference.getY()),intersection);
        this.verticalCut = new StraightCUt(depth, tool, reference.verticalCut, new Coordinate(reference.getX(), reference.getY()),intersection);
    }

    public StraightCUt getHorizontalCut() {
        return horizontalCut;
    }

    public StraightCUt getVerticalCut() {
        return verticalCut;
    }
}
