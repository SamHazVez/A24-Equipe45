/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Equipe45.domain;

import Equipe45.domain.Utils.Coordinate;
import Equipe45.domain.Utils.CutType;

/**
 *
 * @author mat18
 */
public class ParallelCut extends RegularCut {
    private RegularCut referenceCut;
    private int distance;

    public ParallelCut(float depth, Tool tool, RegularCut referenceCut, int distance) {
        super(depth, tool, calculateOrigin(referenceCut, distance), calculateDestination(referenceCut, distance));
        this.referenceCut = referenceCut;
        this.distance = distance;
    }
    
    @Override
    public ParallelCut asParallelCut() {
        return this;
    }

    private static Coordinate calculateOrigin(RegularCut referenceCut, float distance) {
        Coordinate origin = referenceCut.getOrigin();
        if (referenceCut.isVertical()) {
            return new Coordinate(origin.getX() + distance, origin.getY());
        } else if (referenceCut.isHorizontal()) {
            return new Coordinate(origin.getX(), origin.getY() + distance);
        } else {
            throw new IllegalArgumentException("Reference cut is neither vertical nor horizontal");
        }
    }

    private static Coordinate calculateDestination(RegularCut referenceCut, float distance) {
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

    public int getDistance() {
        return distance;
    }
    
    public void setReferenceCut(RegularCut referenceCut) {
        this.referenceCut = referenceCut;
        recalculate();
    }

    public void setDistance(int distance) {
        this.distance = distance;
        recalculate();
    }
    
    @Override
    public void recalculate() {
        super.setOrigin(calculateOrigin(referenceCut, distance));
        super.setDestination(calculateDestination(referenceCut, distance));
    }
    
    @Override
    public CutType getType() {
        return isVertical() ? CutType.PARALLEL_VERTICAL : CutType.PARALLEL_HORIZONTAL;
    }

    @Override
    public boolean isValid() {
        return referenceCut.isValid();
    }
}