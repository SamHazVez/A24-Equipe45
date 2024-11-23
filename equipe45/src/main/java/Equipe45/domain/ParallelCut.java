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
public class ParallelCut extends RegularCut {
    private RegularCut referenceCut;
    private float distance;
    private Coordinate referenceCoordinate;
    private Coordinate intersection;

    public ParallelCut(float depth, Tool tool, RegularCut referenceCut, float distance) {
        super(depth, tool, calculateOrigin(referenceCut, distance, null,null), calculateDestination(referenceCut, distance, null ,null));
        this.referenceCut = referenceCut;
        this.distance = distance;
        this.referenceCut = null;
        this.intersection = null;
    }


    private static Coordinate calculateOrigin(RegularCut referenceCut, float distance, Coordinate referenceCoordinate, Coordinate intersection) {
        Coordinate origin = referenceCut.getOrigin();
        if (referenceCut.isVertical()) {
            return new Coordinate(origin.getX() + distance, origin.getY());
        } else if (referenceCut.isHorizontal()) {
            return new Coordinate(origin.getX(), origin.getY() + distance);
        } else {
            throw new IllegalArgumentException("Reference cut is neither vertical nor horizontal");
        }
    }


    private static Coordinate calculateDestination(RegularCut referenceCut, float distance, Coordinate referenceCoordinate, Coordinate intersection) {
        Coordinate destination = referenceCut.getDestination();
        if (referenceCut.isVertical()) {
            return new Coordinate(destination.getX() + distance,  destination.getY());
        } else if (referenceCut.isHorizontal()) {
            return new Coordinate(destination.getX(), destination.getY() + distance);
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