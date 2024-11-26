package Equipe45.domain;

import Equipe45.domain.Utils.Coordinate;
import Equipe45.domain.Utils.CutType;

public class StraightCutForL extends RegularCut{
    private RegularCut referenceCut;
    private Coordinate referenceCoordinate;
    private Coordinate intersection;
    private boolean isValid;
    private LShapedCut parent;

    public StraightCutForL(float depth, Tool tool, RegularCut referenceCut, Coordinate referenceCoordinate, Coordinate intersection, LShapedCut parent) {
        super(depth, tool, calculateOrigin(referenceCut, referenceCoordinate, intersection), calculateDestination(referenceCut, referenceCoordinate, intersection));
        this.referenceCut = referenceCut;
        this.intersection = intersection;
        this.referenceCoordinate = referenceCoordinate;
        this.parent = parent;
    }

    private static Coordinate calculateOrigin(RegularCut referenceCut, Coordinate referenceCoordinate, Coordinate intersection) {
        Coordinate origin = referenceCut.getOrigin();
        if (referenceCut.isVertical()) {
            return new Coordinate(intersection.getX(), referenceCoordinate.getY());
        } else if (referenceCut.isHorizontal()) {
            return new Coordinate(referenceCoordinate.getX(), intersection.getY());
        } else {
            throw new IllegalArgumentException("Reference cut is neither vertical nor horizontal");
        }
    }

    private static Coordinate calculateDestination(RegularCut referenceCut, Coordinate referenceCoordinate, Coordinate intersection) {
        Coordinate destination = referenceCut.getDestination();
        if (referenceCut.isVertical()) {
            return new Coordinate(intersection.getX(),  intersection.getY());
        } else if (referenceCut.isHorizontal()) {
            return new Coordinate(intersection.getX(), intersection.getY());
        } else {
            throw new IllegalArgumentException("Reference cut is neither vertical nor horizontal");
        }
    }
    
    @Override
    public void recalculate() {
        super.setOrigin(calculateOrigin(referenceCut, referenceCoordinate, intersection));
        super.setDestination(calculateDestination(referenceCut, referenceCoordinate, intersection));
    }
    
    @Override
    public CutType getType(){
        return CutType.LSHAPED;
    }

    @Override
    public boolean isValid() {
        return isValid;
    }

    public LShapedCut getParent() {
        return parent;
    }
}
