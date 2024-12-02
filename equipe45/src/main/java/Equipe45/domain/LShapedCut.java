package Equipe45.domain;

import Equipe45.domain.Utils.Coordinate;
import Equipe45.domain.Utils.CutType;
import Equipe45.domain.Utils.Dimension;
import Equipe45.domain.Utils.ReferenceCoordinate;

/**
 *
 * @author mat18
 */
public class LShapedCut extends IrregularCut implements IRectangular {
    private StraightCutForL horizontalCut;
    private StraightCutForL verticalCut;

    public LShapedCut(float depth, Tool tool, ReferenceCoordinate reference, Coordinate intersection) {
        super(depth, tool, reference, intersection);
        initializeLShapedCut();
    }



    private void initializeLShapedCut() {
        if (reference.horizontalCut instanceof  StraightCutForL straightCutForLHorizontal && reference.verticalCut instanceof  StraightCutForL straightCutForLVert)
        {
            if(!straightCutForLHorizontal.getParent().isCoordinateInRectangleFromLWhenHorizontalIsParent(intersection, straightCutForLHorizontal.getParent()) && !straightCutForLVert.getParent().isCoordinateInRectangleFromLWhenVerticalIsParent(intersection, straightCutForLVert.getParent())){
                throw new IllegalArgumentException("Erreur 2 L.");
            }
        }

        else if(reference.horizontalCut instanceof StraightCutForL straightCutForL){

            if (reference.getX() == straightCutForL.getParent().getIntersection().getX() &&
                    reference.getY() == straightCutForL.getParent().getIntersection().getY()) {

                StraightCutForL parentHorizontalCut = straightCutForL.getParent().getHorizontalCut();
                StraightCutForL parentVerticalCut = straightCutForL.getParent().getVerticalCut();

                float minX = Math.min(parentHorizontalCut.getOrigin().getX(), parentHorizontalCut.getDestination().getX());
                float maxX = Math.max(parentHorizontalCut.getOrigin().getX(), parentHorizontalCut.getDestination().getX());

                float minY = Math.min(parentVerticalCut.getOrigin().getY(), parentVerticalCut.getDestination().getY());
                float maxY = Math.max(parentVerticalCut.getOrigin().getY(), parentVerticalCut.getDestination().getY());

                if (intersection.getX() < minX || intersection.getX() > maxX ||
                        intersection.getY() < minY || intersection.getY() > maxY) {
                    throw new IllegalArgumentException("The intersection must lie within the parent's bounds.");
                }
            }

            if(!straightCutForL.getParent().isCoordinateInRectangleFromLWhenHorizontalIsParent(intersection, straightCutForL.getParent())){
                throw new IllegalArgumentException("Erreur parent horizontal.");
            }
        }

        else if(reference.verticalCut instanceof StraightCutForL straightCutForL){
            if (reference.getX() == straightCutForL.getParent().getIntersection().getX() &&
                    reference.getY() == straightCutForL.getParent().getIntersection().getY()) {

                StraightCutForL parentHorizontalCut = straightCutForL.getParent().getHorizontalCut();
                StraightCutForL parentVerticalCut = straightCutForL.getParent().getVerticalCut();

                float minX = Math.min(parentHorizontalCut.getOrigin().getX(), parentHorizontalCut.getDestination().getX());
                float maxX = Math.max(parentHorizontalCut.getOrigin().getX(), parentHorizontalCut.getDestination().getX());

                float minY = Math.min(parentVerticalCut.getOrigin().getY(), parentVerticalCut.getDestination().getY());
                float maxY = Math.max(parentVerticalCut.getOrigin().getY(), parentVerticalCut.getDestination().getY());

                if (intersection.getX() < minX || intersection.getX() > maxX ||
                        intersection.getY() < minY || intersection.getY() > maxY) {
                    throw new IllegalArgumentException("The intersection must lie within the parent's bounds.");
                }
            }


            if(!straightCutForL.getParent().isCoordinateInRectangleFromLWhenVerticalIsParent(intersection, straightCutForL.getParent())){
                throw new IllegalArgumentException("Erreur parent vertical.");
            }
        }

        if(reference.horizontalCut instanceof BorderCut borderCut)
        {
            if(!borderCut.getParent().isCoordinateInRectangle(intersection)){
                throw new IllegalArgumentException("L'intersection doit être dans le rectangle.");
            }
        }
        if (reference.verticalCut instanceof BorderCut borderCut) {
            if (!borderCut.getParent().isCoordinateInRectangle(intersection)) {
                throw new IllegalArgumentException("L'intersection doit être dans le rectangle.");
            }
        }
        if (reference.horizontalCut instanceof StraightCutForL straightCutForL) {
            if (!straightCutForL.getParent().isCoordinateInRectangle(intersection)) {
                throw new IllegalArgumentException("L'intersection doit être dans le rectangle.");
            }
        }
        if (reference.verticalCut instanceof StraightCutForL straightCutForL) {
            if (!straightCutForL.getParent().isCoordinateInRectangle(intersection)) {
                throw new IllegalArgumentException("L'intersection doit être dans le rectangle.");
            }
        }
        this.horizontalCut = new StraightCutForL(depth, tool, reference.horizontalCut, new Coordinate(reference.getX(), reference.getY()), intersection, this);
        this.verticalCut = new StraightCutForL(depth, tool, reference.verticalCut, new Coordinate(reference.getX(), reference.getY()), intersection, this);
    }


    @Override
    public LShapedCut asLShapedCut(){
        return this;
    }
    
    @Override
    public void recalculate() {
        this.initializeLShapedCut();
        this.horizontalCut.recalculate();
        this.verticalCut.recalculate();
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
        recalculate();
    }

    @Override
    public void setIntersection(Coordinate intersection) {
        this.intersection = intersection;
        recalculate();
    }

    @Override
    public void modifyDimension(Dimension dimension) {
        if (dimension == null) {
            throw new IllegalArgumentException("Dimension cannot be null");
        }

        float relativeX = this.intersection.getX() - this.reference.getX();
        float relativeY = this.intersection.getY() - this.reference.getY();

        float newIntersectionX = this.reference.getX();
        if (relativeX > 0) {
            newIntersectionX += dimension.getWidth();
        } else if (relativeX < 0) {
            newIntersectionX -= dimension.getWidth();
        }

        float newIntersectionY = this.reference.getY();
        if (relativeY > 0) {
            newIntersectionY += dimension.getHeight();
        } else if (relativeY < 0) {
            newIntersectionY -= dimension.getHeight();
        }

        this.intersection.setX(newIntersectionX);
        this.intersection.setY(newIntersectionY);

        recalculate();
    }

    @Override
    public boolean isCoordinateInRectangle(Coordinate coordinate) {
        float minX = Math.min(this.getReference().getX(), this.getIntersection().getX());
        float maxX = Math.max(this.getReference().getX(), this.getIntersection().getX());
        float minY = Math.min(this.getReference().getY(), this.getIntersection().getY());
        float maxY = Math.max(this.getReference().getY(), this.getIntersection().getY());

        return coordinate.getX() >= minX && coordinate.getX() <= maxX &&
                coordinate.getY() >= minY && coordinate.getY() <= maxY;
    }

    private boolean isCoordinateInRectangleFromLWhenHorizontalIsParent(Coordinate coordinate, LShapedCut reference) {
        float minX = Math.min(reference.horizontalCut.getOrigin().x, reference.horizontalCut.getDestination().x);
        float maxX = Math.max(reference.horizontalCut.getOrigin().x, reference.horizontalCut.getDestination().x);

        return coordinate.getX() >= minX && coordinate.getX() <= maxX;
    }

    private boolean isCoordinateInRectangleFromLWhenVerticalIsParent(Coordinate coordinate, LShapedCut reference) {
        float minY = Math.min(reference.verticalCut.getOrigin().y, reference.verticalCut.getDestination().y);
        float maxY = Math.max(reference.verticalCut.getOrigin().y, reference.verticalCut.getDestination().y);

        System.out.println();

        if (coordinate.getY() < minY) {
            System.out.println("Condition failed: coordinate.getY() < minY");
        } else if (coordinate.getY() > maxY) {
            System.out.println("Condition failed: coordinate.getY() > maxY");
        } else {
            System.out.println("Condition passed: coordinate.getY() is within range");
        }

        return coordinate.getY() >= minY && coordinate.getY() <= maxY;
    }

    @Override
    public CutType getType() {
        return CutType.LSHAPED;
    }

    @Override
    public boolean isValid() {
        return reference.isValid() && intersection != null;
    }
}
