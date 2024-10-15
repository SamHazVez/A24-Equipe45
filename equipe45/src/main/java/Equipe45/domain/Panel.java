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
    private float depth;
    private List<Cut> cuts;
    private List<NoCutZone> noCutZones;

    public Panel() {
    }
    
    public void AddNoCutZone(NoCutZone noCutZone){}
}
