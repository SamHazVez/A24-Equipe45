/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Equipe45.domain.DTO;

import Equipe45.domain.Tool;
import Equipe45.domain.Utils.Dimension;

import java.util.UUID;

/**
 *
 * @author mat18
 */
public class ReCutDTO extends CutDTO {

    public Dimension finaleSize;

    public ReCutDTO(UUID id, float depht, Tool tool, Dimension finaleSize) {
        super(id, depht, tool);
        this.finaleSize = finaleSize;
    }
}
