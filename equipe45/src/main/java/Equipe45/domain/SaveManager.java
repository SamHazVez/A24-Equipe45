/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Equipe45.domain;

import Equipe45.domain.Utils.Coordinate;
import Equipe45.domain.Utils.Dimension;

import java.util.ArrayList;

/**
 *
 * @author mat18
 */
public class SaveManager {

    public SaveManager() {
        this.LoadProject();
    }
    
    public void SaveProject(){}

    public CNC LoadProject(){
        //TODO: remove placeholder et lire fichiers
        return new CNC(new Panel(new Dimension(1219.2f, 914.4f), 100f, new ArrayList<Cut>()), new ArrayList<Tool>());
    }

    private void SavePanel(){}

    private void SaveCNC(){}

    private void LoadCNC(){}

    private void LoadPanel(){}
}
