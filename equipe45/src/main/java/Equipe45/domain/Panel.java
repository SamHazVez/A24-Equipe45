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

    public void addCut(Cut cut) {
        cuts.addFirst(cut);
        System.out.println("New cut added to panel : " + cut.toString());
    }

    public void addNoCutZone(NoCutZone noCutZone){noCutZones.add(noCutZone);}

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
    public void invalidateCutsInNoCutZones() {
        System.out.println("Checking cuts against no-cut zones...");

        for (Cut cut : cuts) {
            boolean isInNoCutZone = false;

            for (NoCutZone noCutZone : noCutZones) {
                if (cut instanceof RegularCut) {
                    RegularCut regularCut = (RegularCut) cut;
                    if (noCutZone.intersectsLine(regularCut.getOrigin(), regularCut.getDestination())) {
                        isInNoCutZone = true;
                        break;
                    }
                } else if (cut instanceof LShapedCut) {
                    LShapedCut lShapedCut = (LShapedCut) cut;

                    if (noCutZone.intersectsLine(lShapedCut.getVerticalCut().getOrigin(), lShapedCut.getVerticalCut().getDestination()) ||
                            noCutZone.intersectsLine(lShapedCut.getHorizontalCut().getOrigin(), lShapedCut.getHorizontalCut().getDestination())) {
                        isInNoCutZone = true;
                        break;
                    }
                } else if (cut instanceof RectangularCut) {
                    RectangularCut rectangularCut = (RectangularCut) cut;

                    if (noCutZone.intersectsLine(rectangularCut.getLeftVerticalCut().getOrigin(), rectangularCut.getLeftVerticalCut().getDestination()) ||
                            noCutZone.intersectsLine(rectangularCut.getRightVerticalCut().getOrigin(), rectangularCut.getRightVerticalCut().getDestination()) ||
                            noCutZone.intersectsLine(rectangularCut.getTopHorizontalCut().getOrigin(), rectangularCut.getTopHorizontalCut().getDestination()) ||
                            noCutZone.intersectsLine(rectangularCut.getBottomHorizontalCut().getOrigin(), rectangularCut.getBottomHorizontalCut().getDestination())) {
                        isInNoCutZone = true;
                        break;
                    }
                }
            }

            cut.setInNoCutZone(isInNoCutZone);
            System.out.println("Cut " + cut.getId() + " is in no-cut zone: " + isInNoCutZone);
        }
    }

}
