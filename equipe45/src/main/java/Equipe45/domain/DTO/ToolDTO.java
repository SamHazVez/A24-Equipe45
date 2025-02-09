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
    public float cutDepth;

    public ToolDTO(String name, float cutWidth, float cutDepth) {
        this.name = name;
        this.cutWidth = cutWidth;
        this.cutDepth = cutDepth;
    }

    public Tool toTool() {
        return new Tool(name, cutWidth, cutDepth);
    }

    public String getName() {
        return name;
    }

    public float getCutWidth() {
        return cutWidth;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCutWidth(float cutWidth) {
        this.cutWidth = cutWidth;
    }

    public float getCutDepth() {
        return cutDepth;
    }

    public void setCutDepth(float cutDepth) {
        this.cutDepth = cutDepth;
    }

    @Override
    public String toString() {
        return name + " (Width: " + cutWidth + " mm)";
    }
}
