/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Equipe45.domain;

import Equipe45.domain.Utils.Dimension;
import java.util.List;

/**
 *
 * @author mat18
 */
public class Panel {
    
    private Dimension dimension;
    private float width;
    private List<Cut> cuts;
    private List<NoCutZone> noCutZones;

    public Panel(Dimension dimension, float width, List<Cut> cuts, List<NoCutZone> noCutZones) {
        this.dimension = dimension;
        this.width = width;
        this.cuts = cuts;
        this.noCutZones = noCutZones;
    }

    public Panel(Dimension dimension, float width, List<Cut> cuts) {
        this.dimension = dimension;
        this.width = width;
        this.cuts = cuts;
    }


    public void AddNoCutZone(NoCutZone noCutZone){}

    public Dimension getDimension() {
        return dimension;
    }

    public float getWidth() {
        return width;
    }

    public List<Cut> getCuts() {
        return cuts;
    }

    public List<NoCutZone> getNoCutZones() {
        return noCutZones;
    }
}
