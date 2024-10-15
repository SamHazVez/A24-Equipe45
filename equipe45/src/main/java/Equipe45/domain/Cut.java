/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Equipe45.domain;

import java.util.UUID;

/**
 *
 * @author mat18
 */
public abstract class Cut {
    
    private float depth;
    private Tool tool;
    private UUID id;
    
    abstract public void CutPanel(Panel panel);
}
