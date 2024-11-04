/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Equipe45.domain.DTO;

import Equipe45.domain.Tool;

/**
 *
 * @author mat18
 */
public class ToolDTO {
    public String name;
    public float cutWidth;
    public int positionCharger;

    public ToolDTO(String name, float cutWidth, int positionCharger) {
        this.name = name;
        this.cutWidth = cutWidth;
        this.positionCharger = positionCharger;
    }

    public Tool toTool() {
        return new Tool(name, cutWidth, positionCharger);
    }

    @Override
    public String toString() {
        return name + " (Width: " + cutWidth + " mm)";
    }
}
