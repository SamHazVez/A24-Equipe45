package Equipe45.domain.Converter;

import Equipe45.domain.DTO.DimensionDTO;
import Equipe45.domain.Utils.Dimension;

public class DimensionConverter {

    public DimensionDTO convertToDimensionDTOFrom(Dimension dimension) {
        return new DimensionDTO(dimension.getWidth(), dimension.getHeight());
    }

    public Dimension convertToDimensionFrom(DimensionDTO dimension) {
        return new Dimension(dimension.width, dimension.height);
    }
}
