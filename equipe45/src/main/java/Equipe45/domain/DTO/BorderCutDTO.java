package Equipe45.domain.DTO;

import Equipe45.domain.IRectangular;
import Equipe45.domain.Tool;
import Equipe45.domain.Utils.Coordinate;

import java.util.UUID;

public class BorderCutDTO extends CutDTO {

    private Coordinate origin;
    private Coordinate destination;
    public IRectangular parent;

    public BorderCutDTO(UUID id, float depth, ToolDTO tool, Coordinate origin, Coordinate destination, IRectangular parent) {
        super(id, depth, tool);
        this.origin = origin;
        this.destination = destination;
        this.parent = parent;
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
