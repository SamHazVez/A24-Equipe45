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
public class ParallelCut extends RegularCut {
    private RegularCut referenceCut;
    private float distance;
    private Coordinate intersection;

    public ParallelCut(float depth, Tool tool, RegularCut referenceCut, float distance) {
        super(depth, tool, calculateOrigin(referenceCut, distance, null), calculateDestination(referenceCut, distance, null));
        this.referenceCut = referenceCut;
        this.distance = distance;
        this.intersection = null;
    }

    public ParallelCut(float depth, Tool tool, RegularCut referenceCut, float distance, Coordinate intersection) {
        super(depth, tool, calculateOrigin(referenceCut, distance, intersection), calculateDestination(referenceCut, distance, intersection));
        this.referenceCut = referenceCut;
        this.distance = distance;
        this.intersection = intersection;
    }

    private static Coordinate calculateOrigin(RegularCut referenceCut, float distance, Coordinate intersection) {
        if (intersection != null) {
            return calculateOriginWithIntersection(referenceCut, distance, intersection);
        }
       else {
            return calculateOriginWithoutIntersection(referenceCut, distance);
         }
    }


    private static Coordinate calculateDestination(RegularCut referenceCut, float distance, Coordinate intersection) {
        if (intersection != null) {
            return calculateDestinationWithIntersection(referenceCut, distance, intersection);
        }
        else {
            return calculateDestinationWithoutIntersection(referenceCut, distance);
        }
    }

    private static Coordinate calculateOriginWithoutIntersection(RegularCut referenceCut, float distance) {
        Coordinate origin = referenceCut.getOrigin();
        if (referenceCut.isVertical()) {
            return new Coordinate(origin.getX() + distance, origin.getY());
        } else if (referenceCut.isHorizontal()) {
            return new Coordinate(origin.getX(), origin.getY() + distance);
        } else {
            throw new IllegalArgumentException("Reference cut is neither vertical nor horizontal");
        }
    }

    private static Coordinate calculateDestinationWithoutIntersection(RegularCut referenceCut, float distance) {
        Coordinate destination = referenceCut.getDestination();
        if (referenceCut.isVertical()) {
            return new Coordinate(destination.getX() + distance,  destination.getY());
        } else if (referenceCut.isHorizontal()) {
            return new Coordinate(destination.getX(), destination.getY() + distance);
        } else {
            throw new IllegalArgumentException("Reference cut is neither vertical nor horizontal");
        }
    }


    private static Coordinate calculateOriginWithIntersection(RegularCut referenceCut, float distance, Coordinate intersection) {
        Coordinate origin = referenceCut.getOrigin();
        if (referenceCut.isVertical()) {
            return new Coordinate(intersection.getX(), origin.getY());
        } else if (referenceCut.isHorizontal()) {
            return new Coordinate(origin.getX(), intersection.getY());
        } else {
            throw new IllegalArgumentException("Reference cut is neither vertical nor horizontal");
        }
    }

    private static Coordinate calculateDestinationWithIntersection(RegularCut referenceCut, float distance, Coordinate intersection) {
        Coordinate destination = referenceCut.getDestination();
        if (referenceCut.isVertical()) {
            return new Coordinate(intersection.getX(),  intersection.getY());
        } else if (referenceCut.isHorizontal()) {
            return new Coordinate(intersection.getX(), intersection.getY());
        } else {
            throw new IllegalArgumentException("Reference cut is neither vertical nor horizontal");
        }
    }


    public RegularCut getReferenceCut() {
        return referenceCut;
    }

    public float getDistance() {
        return distance;
    }

    @Override
    public boolean isValid() {
        return referenceCut != null;
    }
}