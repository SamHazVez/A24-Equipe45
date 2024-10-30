/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Equipe45.domain.DTO;

import Equipe45.domain.NoCutZone;
import Equipe45.domain.Utils.Dimension;
import java.util.List;

/**
 *
 * @author mat18
 */
public class PanelDTO {

    public DimensionDTO dimension;
    public float width;
    public List<CutDTO> cuts;

    public PanelDTO(DimensionDTO dimension, float width, List<CutDTO> cuts) {
        this.dimension = dimension;
        this.width = width;
        this.cuts = cuts;
    }

    public DimensionDTO getDimension() {
        return dimension;
    }
}
