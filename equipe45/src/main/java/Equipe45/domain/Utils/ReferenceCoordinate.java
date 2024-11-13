package Equipe45.domain.Utils;

import Equipe45.domain.RegularCut;

public class ReferenceCoordinate extends Coordinate {
    public RegularCut horizontalCut;
    public RegularCut verticalCut;

    public ReferenceCoordinate(float x, float y, RegularCut reference1, RegularCut reference2) {
        super(x, y);
        if (reference1.isHorizontal()) {
            this.horizontalCut = reference1;
            this.verticalCut = reference2.isVertical() ? reference2 : null;
        } else {
            this.horizontalCut = reference2.isHorizontal() ? reference2 : null;
            this.verticalCut = reference1;
        }

        // Validate the cuts to ensure both orientations are represented
        if (this.horizontalCut == null || this.verticalCut == null) {
            throw new IllegalArgumentException("One cut must be horizontal and the other must be vertical.");
        }
    }

    public ReferenceCoordinate(Coordinate coordinate, RegularCut reference1, RegularCut reference2) {
        super(coordinate.getX(), coordinate.getY());
        if (reference1.isHorizontal()) {
            this.horizontalCut = reference1;
            this.verticalCut = reference2.isVertical() ? reference2 : null;
        } else {
            this.horizontalCut = reference2.isHorizontal() ? reference2 : null;
            this.verticalCut = reference1;
        }

        if (this.horizontalCut == null || this.verticalCut == null) {
            throw new IllegalArgumentException("One cut must be horizontal and the other must be vertical.");
        }
    }

    public enum CornerType {
        INNER_TOP_RIGHT,
        INNER_TOP_LEFT,
        INNER_BOTTOM_LEFT,
        INNER_BOTTOM_RIGHT,
        OUTER_TOP_RIGHT,
        OUTER_TOP_LEFT,
        OUTER_BOTTOM_LEFT,
        OUTER_BOTTOM_RIGHT
    }

    public CornerType determineCornerType() {
        boolean isInner = isInnerCorner();

        if (isInner) {
            if (horizontalCut.getOrigin().getX() < verticalCut.getOrigin().getX() &&
                horizontalCut.getOrigin().getY() > verticalCut.getOrigin().getY()) {
                return CornerType.INNER_TOP_RIGHT;
            } else if (horizontalCut.getOrigin().getX() > verticalCut.getOrigin().getX() &&
                       horizontalCut.getOrigin().getY() > verticalCut.getOrigin().getY()) {
                return CornerType.INNER_TOP_LEFT;
            } else if (horizontalCut.getOrigin().getX() > verticalCut.getOrigin().getX() &&
                       horizontalCut.getOrigin().getY() < verticalCut.getOrigin().getY()) {
                return CornerType.INNER_BOTTOM_LEFT;
            } else {
                return CornerType.INNER_BOTTOM_RIGHT;
            }
        } else {
            if (horizontalCut.getOrigin().getX() < verticalCut.getOrigin().getX() &&
                horizontalCut.getOrigin().getY() > verticalCut.getOrigin().getY()) {
                return CornerType.OUTER_TOP_RIGHT;
            } else if (horizontalCut.getOrigin().getX() > verticalCut.getOrigin().getX() &&
                       horizontalCut.getOrigin().getY() > verticalCut.getOrigin().getY()) {
                return CornerType.OUTER_TOP_LEFT;
            } else if (horizontalCut.getOrigin().getX() > verticalCut.getOrigin().getX() &&
                       horizontalCut.getOrigin().getY() < verticalCut.getOrigin().getY()) {
                return CornerType.OUTER_BOTTOM_LEFT;
            } else {
                return CornerType.OUTER_BOTTOM_RIGHT;
            }
        }
    }

    private boolean isInnerCorner() {
        boolean horizontalRightOfVertical = horizontalCut.getOrigin().getX() > verticalCut.getOrigin().getX();
        boolean verticalAboveHorizontal = verticalCut.getOrigin().getY() > horizontalCut.getOrigin().getY();

        return (horizontalRightOfVertical && verticalAboveHorizontal) || (!horizontalRightOfVertical && !verticalAboveHorizontal);
    }
}