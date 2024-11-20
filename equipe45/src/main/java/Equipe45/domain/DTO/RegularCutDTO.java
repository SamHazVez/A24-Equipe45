package Equipe45.domain.DTO;

import Equipe45.domain.Tool;
import Equipe45.domain.Utils.Coordinate;

import java.util.UUID;

public class RegularCutDTO extends CutDTO {

    private Coordinate origin;
    private Coordinate destination;

    public RegularCutDTO(UUID id, float depth, Tool tool, Coordinate origin, Coordinate destination) {
        super(id, depth, tool);
        this.origin = origin;
        this.destination = destination;
    }

    public Coordinate getOrigin() {
        return origin;
    }

    public void setOrigin(Coordinate origin) {
        this.origin = origin;
    }

    public Coordinate getDestination() {
        return destination;
    }

    public void setDestination(Coordinate destination) {
        this.destination = destination;
    }
}
