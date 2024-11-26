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
    public PanelDTO panel;
    public Dimension finaleSize;

    public ReCutDTO(UUID id, float depth, ToolDTO tool, Dimension finaleSize, PanelDTO panel) {
        super(id, depth, tool);
        this.finaleSize = finaleSize;
        this.panel = panel;
    }

    public Dimension getFinaleSize() {
        return finaleSize;
    }

    public void setFinaleSize(Dimension finaleSize) {
        this.finaleSize = finaleSize;
    }

    @Override
    public String toString() {
        return "ReCutDTO{" +
                "id=" + id +
                ", depth=" + depth +
                ", tool=" + toolDTO +
                ", finaleSize=" + finaleSize +
                '}';
    }
}
