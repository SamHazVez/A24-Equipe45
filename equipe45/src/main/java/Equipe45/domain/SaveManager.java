/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Equipe45.domain;

import Equipe45.domain.Utils.Coordinate;
import Equipe45.domain.Utils.Dimension;

import java.io.*;
import java.util.ArrayList;

/**
 *
 * @author mat18
 */
public class SaveManager {

    private static final String CNC_FILE_EXTENSION = ".cnc";
    private static final String PAN_FILE_EXTENSION = ".pan";
    public SaveManager() {
        this.LoadProject();
    }
    
    public void SaveProject(){}

    public CNC LoadProject(){
        //TODO: remove placeholder et lire fichiers
        return new CNC(new Panel(new Dimension(1219.2f, 914.4f), 100f, new ArrayList<Cut>()), new ArrayList<Tool>());
    }

    public void savePanel(Panel panel, String filePath) throws IOException {
        if (!filePath.toLowerCase().endsWith(PAN_FILE_EXTENSION)) {
            filePath += PAN_FILE_EXTENSION;
        }
        try (FileOutputStream fileOut = new FileOutputStream(filePath);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(panel);
            System.out.printf("Serialized data is saved in %s%n", filePath);
        }
    }

    public void saveCNC(CNC cnc, String filePath) throws IOException {
        if (!filePath.toLowerCase().endsWith(CNC_FILE_EXTENSION)) {
            filePath += CNC_FILE_EXTENSION;
        }
        try (FileOutputStream fileOut = new FileOutputStream(filePath);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(cnc);
            System.out.printf("Serialized data is saved in %s%n", filePath);
        }
    }

    public CNC loadCNC(String filePath) throws IOException, ClassNotFoundException {
        if (!filePath.toLowerCase().endsWith(CNC_FILE_EXTENSION)) {
            filePath += CNC_FILE_EXTENSION;
        }
        try (FileInputStream fileIn = new FileInputStream(filePath);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            CNC cnc = (CNC) in.readObject();
            System.out.printf("Deserialized data from %s%n", filePath);
            return cnc;
        }
    }

    public Panel loadPanel(String filePath) throws IOException, ClassNotFoundException {
        if (!filePath.toLowerCase().endsWith(PAN_FILE_EXTENSION)) {
            filePath += PAN_FILE_EXTENSION;
        }
        try (FileInputStream fileIn = new FileInputStream(filePath);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            Panel panel = (Panel) in.readObject();
            System.out.printf("Deserialized data from %s%n", filePath);
            return panel;
        }
    }
}
