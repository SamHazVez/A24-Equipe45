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
public class Dimension implements Serializable {
    private static final long serialVersionUID = 1L;
    private float width;
    private float height;
    
    public Dimension(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
