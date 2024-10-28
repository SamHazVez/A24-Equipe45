/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Equipe45.domain.DTO;

import Equipe45.domain.Utils.Dimension;

/**
 *
 * @author mat18
 */
public class ReCutDTO extends CutDTO {

    public Dimension finaleSize;

    public ReCutDTO(Dimension finaleSize) {
        this.finaleSize = finaleSize;
    }
}
