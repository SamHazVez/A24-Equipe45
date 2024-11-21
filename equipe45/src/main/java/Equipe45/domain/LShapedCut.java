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
    private ParallelCut horizontalCut;
    private ParallelCut verticalCut;

    public LShapedCut(float depth, Tool tool, ReferenceCoordinate reference, Coordinate intersection) {
        super(depth, tool, reference, intersection);
        
        ReferenceCoordinate.CornerType type = reference.determineCornerType();
        float distanceX;
        float distanceY;

        System.out.println("Origine Coupe Horizontal :" + reference.horizontalCut.getOrigin().getX() + ","+ reference.horizontalCut.getOrigin().getY());;
        System.out.println("Destination Coupe Horizontal :" + reference.horizontalCut.getDestination().getX() + ","+ reference.horizontalCut.getDestination().getY());;

        System.out.println("Origine Coupe Vertical :" + reference.verticalCut.getOrigin().getX() + ","+ reference.verticalCut.getOrigin().getY());;
        System.out.println("Destination Coupe Vertical :" + reference.verticalCut.getDestination().getX() + ","+ reference.verticalCut.getDestination().getY());

        switch (type) {
            case INNER_TOP_RIGHT:
                // Horizontal is to the left, vertical is below
                distanceX = intersection.getX() - reference.horizontalCut.getOrigin().getX();
                distanceY = reference.verticalCut.getOrigin().getY() - intersection.getY();
                break;

            case INNER_TOP_LEFT:
                // Horizontal is to the right, vertical is below
                distanceX = reference.horizontalCut.getOrigin().getX() - intersection.getX();
                distanceY = reference.verticalCut.getOrigin().getY() - intersection.getY();
                break;

            case INNER_BOTTOM_LEFT:
                // Horizontal is above, vertical is to the right
                distanceX = reference.horizontalCut.getOrigin().getX() - intersection.getX();
                distanceY = intersection.getY() - reference.verticalCut.getOrigin().getY();
                break;

            case INNER_BOTTOM_RIGHT:
                // Horizontal is above, vertical is to the left
                distanceX = intersection.getX() - reference.horizontalCut.getOrigin().getX();
                distanceY = intersection.getY() - reference.verticalCut.getOrigin().getY();
                break;

            case OUTER_TOP_RIGHT:
                // Horizontal is to the left, vertical is above
                distanceX = intersection.getX() - reference.horizontalCut.getOrigin().getX();
                distanceY = intersection.getY() - reference.verticalCut.getOrigin().getY();
                break;

            case OUTER_TOP_LEFT:
                // Horizontal is to the right, vertical is above
                distanceX = reference.horizontalCut.getOrigin().getX() - intersection.getX();
                distanceY = intersection.getY() - reference.verticalCut.getOrigin().getY();
                break;

            case OUTER_BOTTOM_LEFT:
                // Horizontal is below, vertical is to the right
                distanceX = reference.horizontalCut.getOrigin().getX() - intersection.getX();
                distanceY = intersection.getY() - reference.verticalCut.getOrigin().getY();
                break;

            case OUTER_BOTTOM_RIGHT:
                // Horizontal is below, vertical is to the left
                distanceX = intersection.getX() - reference.horizontalCut.getOrigin().getX();
                distanceY = intersection.getY() - reference.verticalCut.getOrigin().getY();
                break;

            default:
                throw new AssertionError();
        }
        
        this.horizontalCut = new ParallelCut(depth, tool, reference.horizontalCut, distanceX);
        this.verticalCut = new ParallelCut(depth, tool, reference.verticalCut, distanceY);

        /*System.out.println("Origine Coupe Horizontal :" + this.horizontalCut.getOrigin().getX() + ","+ this.horizontalCut.getOrigin().getY());;
        System.out.println("Destination Coupe Horizontal :" + this.horizontalCut.getDestination().getX() + ","+ this.horizontalCut.getDestination().getY());;

        System.out.println("Origine Coupe Vertical :" + this.verticalCut.getOrigin().getX() + ","+ this.verticalCut.getOrigin().getY());;
        System.out.println("Destination Coupe Vertical :" + this.verticalCut.getDestination().getX() + ","+ this.verticalCut.getDestination().getY());;*/
    }

    public ParallelCut getHorizontalCut() {
        return horizontalCut;
    }

    public ParallelCut getVerticalCut() {
        return verticalCut;
    }

    public ParallelCut getHorizontalCut() {
        return horizontalCut;
    }

    public ParallelCut getVerticalCut() {
        return verticalCut;
    }
}
