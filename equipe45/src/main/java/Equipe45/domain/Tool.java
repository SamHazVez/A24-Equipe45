/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Equipe45.domain;

/**
 *
 * @author mat18
 */
public class Tool {
    private String name;
    private float cutWidth;
    private int positionCharger;

    public Tool(String name, float cutWidth, int positionCharger) {
        this.name = name;
        this.cutWidth = cutWidth;
        this.positionCharger = positionCharger;
    }

    public String getName() {
        return name;
    }

    public float getCutWidth() {
        return cutWidth;
    }

    public int getPositionCharger() {
        return positionCharger;
    }

    @Override
    public String toString() {
        return "Tool{" +
                "name='" + name + '\'' +
                ", cutWidth=" + cutWidth +
                ", positionCharger=" + positionCharger +
                '}';
    }
}
