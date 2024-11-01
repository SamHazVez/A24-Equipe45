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


    public Tool() {
    }

    public Tool(String name, float cutWidth, int positionCharger) {
        this.name = name;
        this.cutWidth = cutWidth;
        this.positionCharger = positionCharger;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getCutWidth() {
        return cutWidth;
    }

    public void setCutWidth(float cutWidth) {
        if (cutWidth < 0) {
            throw new IllegalArgumentException("Cut width cannot be negative.");
        }
        this.cutWidth = cutWidth;
    }

    public int getPositionCharger() {
        return positionCharger;
    }

    public void setPositionCharger(int positionCharger) {
        if (positionCharger < 0 || positionCharger > 11) {
            throw new IllegalArgumentException("Position in charger must be between 0 and 11.");
        }
        this.positionCharger = positionCharger;
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