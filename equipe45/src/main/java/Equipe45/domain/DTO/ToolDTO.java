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

    public ToolDTO(String name, float cutWidth) {
        this.name = name;
        this.cutWidth = cutWidth;
    }

    public Tool toTool() {
        return new Tool(name, cutWidth);
    }

    public String getName() {
        return name;
    }

    public float getCutWidth() {
        return cutWidth;
    }

    @Override
    public String toString() {
        return name + " (Width: " + cutWidth + " mm)";
    }
}
