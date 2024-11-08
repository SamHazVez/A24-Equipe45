/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Equipe45.domain.Utils;

import java.util.UUID;

/**
 *
 * @author mat18
 */
public class ReferenceCoordinate extends Coordinate {
    private UUID referenceID;

    public ReferenceCoordinate(float x, float y, UUID referenceID) {
        super(x, y);
        this.referenceID = referenceID;
    }
    
    public ReferenceCoordinate(Coordinate coordinate, UUID referenceID) {
        super(coordinate.getX(), coordinate.getY());
        this.referenceID = referenceID;
    }
    
    public UUID getReference() {
        return referenceID;
    }
}
