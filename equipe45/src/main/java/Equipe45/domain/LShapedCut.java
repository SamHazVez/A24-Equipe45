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
    private StraightCutForL horizontalCut;
    private StraightCutForL verticalCut;

    public LShapedCut(float depth, Tool tool, ReferenceCoordinate reference, Coordinate intersection) {
        super(depth, tool, reference, intersection);

        this.horizontalCut = new StraightCutForL(depth, tool, reference.horizontalCut, new Coordinate(reference.getX(), reference.getY()),intersection);
        this.verticalCut = new StraightCutForL(depth, tool, reference.verticalCut, new Coordinate(reference.getX(), reference.getY()),intersection);
    }

    public StraightCutForL getHorizontalCut() {
        return horizontalCut;
    }

    public StraightCutForL getVerticalCut() {
        return verticalCut;
    }
    
    @Override
    public void setReference(ReferenceCoordinate reference) {
        this.reference = reference;
        this.horizontalCut = new StraightCutForL(depth, tool, reference.horizontalCut, new Coordinate(reference.getX(), reference.getY()),intersection);
        this.verticalCut = new StraightCutForL(depth, tool, reference.verticalCut, new Coordinate(reference.getX(), reference.getY()),intersection);
    }

    @Override
    public void setIntersection(Coordinate intersection) {
        this.intersection = intersection;
        this.horizontalCut = new StraightCutForL(depth, tool, reference.horizontalCut, new Coordinate(reference.getX(), reference.getY()),intersection);
        this.verticalCut = new StraightCutForL(depth, tool, reference.verticalCut, new Coordinate(reference.getX(), reference.getY()),intersection);
    }
}
