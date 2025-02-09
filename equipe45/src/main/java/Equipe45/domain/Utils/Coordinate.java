/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Equipe45.domain.Utils;

import java.io.Serializable;

/**
 *
 * @author mat18
 */

public class Coordinate implements Serializable {
    private static final long serialVersionUID = 1L;
    public float x;
    public float y;

    public Coordinate(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }
    public void setY(float y) { this.y = y; }

    @Override
    public String toString() {
        return " " + x + ", " + y + " ";
    }
}
