/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Equipe45.domain.DTO;

import Equipe45.domain.Utils.Coordinate;
import Equipe45.domain.Utils.Dimension;

/**
 *
 * @author mat18
 */
public class NoCutZoneDTO {

    public Dimension dimension;
    public Coordinate coordinate;

    public NoCutZoneDTO(Dimension dimension, Coordinate coordinate) {
        this.dimension = dimension;
        this.coordinate = coordinate;
    }
}
