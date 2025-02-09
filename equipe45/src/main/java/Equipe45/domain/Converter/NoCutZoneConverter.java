/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Equipe45.domain.Converter;

import Equipe45.domain.DTO.NoCutZoneDTO;
import Equipe45.domain.NoCutZone;

/**
 *
 * @author mat18
 */
public class NoCutZoneConverter {
    
    public NoCutZone ConvertToNoCutZoneFromDTO(NoCutZoneDTO noCutZone){
        return new NoCutZone(noCutZone.getId(),noCutZone.getDimension(),noCutZone.getCoordinate());
    }
    
    public NoCutZoneDTO ConvertToDTOFromNoCutZone(NoCutZone noCutZone){
        return new NoCutZoneDTO(noCutZone.getId(),noCutZone.getDimension(),noCutZone.getCoordinate());
    }
}
