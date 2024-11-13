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
    RegularCut referenceCut;
    float distance;

    public ParallelCut(float depth, Tool tool, RegularCut referenceCut, float distance) {
        super(depth, tool, initOrigin(referenceCut, distance), initDestination(referenceCut, distance));
        this.referenceCut = referenceCut;
        this.distance = distance;
    }
    
    private static Coordinate initOrigin(RegularCut referenceCut, float distance) {
        Coordinate origin = referenceCut.getOrigin();
        Coordinate destination = referenceCut.getDestination();
        
        if(origin.getX() == destination.getX()){ // vertical
            return new Coordinate(origin.getX() + distance, origin.getY());
        }
        else if(origin.getY() == destination.getY()){ // horizontal
            return new Coordinate(origin.getX(), origin.getY() + distance);
        }
        else
            throw new IllegalArgumentException();
    }
    
    private static Coordinate initDestination(RegularCut referenceCut, float distance) { // TODO fusionner en de quoi de plus beau
        Coordinate origin = referenceCut.getOrigin();
        Coordinate destination = referenceCut.getDestination();
        
        if(origin.getX() == destination.getX()){ // vertical
            return new Coordinate(destination.getX() + distance, destination.getY());
        }
        else if(origin.getY() == destination.getY()){ // horizontal
            return new Coordinate(destination.getX(), destination.getY() + distance);
        }
        else
            throw new IllegalArgumentException();
    }

    public RegularCut getReferenceCut() {
        return referenceCut;
    }

    public float getDistance() {
        return distance;
    }
}
