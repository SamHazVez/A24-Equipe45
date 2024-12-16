/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Equipe45.domain;

import java.io.Serializable;

/**
 *
 * @author mat18
 */
public class Tool implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private float cutWidth;
    private float cutDepth;

    public Tool(String name, float cutWidth, float cutDepth) {
        this.name = name;
        this.cutWidth = cutWidth;
        this.cutDepth = cutDepth;
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

    public float getCutDepth() {
        return cutDepth;
    }

    public void setCutDepth(float cutDepth) {
        this.cutDepth = cutDepth;
    }

    public void setCutWidth(float cutWidth) {
        this.cutWidth = cutWidth;
    }

    @Override
    public String toString() {
        return "Tool{" +
                "name='" + name + '\'' +
                ", cutWidth=" + cutWidth +
                '}';
    }
}
