/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Equipe45.domain;

import Equipe45.domain.Utils.Coordinate;
import Equipe45.domain.Utils.Dimension;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author mat18
 */
public class Panel implements Serializable {
    private static final long serialVersionUID = 1L;
    private Dimension dimension;
    private float thickness;
    private List<Cut> cuts;
    private List<NoCutZone> noCutZones;

    public Panel(Dimension dimension, float thickness, List<Cut> cuts, List<NoCutZone> noCutZones) {
        this.dimension = dimension;
        this.thickness = thickness;
        this.cuts = cuts;
        this.noCutZones = noCutZones;
    }

    public Panel(Dimension dimension, float thickness, List<Cut> cuts) {
        this.dimension = dimension;
        this.thickness = thickness;
        this.cuts = cuts;
    }

    public void addCut(Cut cut) {
        if (isCutWithinPanel(cut)) {
            cuts.addFirst(cut);
            System.out.println("New cut added to panel: " + cut.toString());
        } else {
            System.out.println("Warning: Cut " + cut.getId() + " is outside the panel boundaries and has not been added.");
        }
    }

    public void addNoCutZone(NoCutZone noCutZone){noCutZones.add(noCutZone);}

    public Dimension getDimension() {
        return dimension;
    }

    public float getWidth() {
        return dimension.getWidth();
    }
    
    public float getHeight() {
        return dimension.getHeight();
    }

    public List<Cut> getCuts() {
        return cuts;
    }

    public List<NoCutZone> getNoCutZones() {
        return noCutZones;
    }
    public void invalidateCutsInNoCutZones() {
        //System.out.println("Checking cuts against no-cut zones...");

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
            //System.out.println("Cut " + cut.getId() + " is in no-cut zone: " + isInNoCutZone);
        }
    }
    private boolean isCutWithinPanel(Cut cut) {
        if (cut instanceof RegularCut) {
            RegularCut regularCut = (RegularCut) cut;
            return isPointInsidePanel(regularCut.getOrigin()) && isPointInsidePanel(regularCut.getDestination());
        } else if (cut instanceof LShapedCut) {
            LShapedCut lShapedCut = (LShapedCut) cut;
            return isPointInsidePanel(lShapedCut.getHorizontalCut().getOrigin()) &&
                    isPointInsidePanel(lShapedCut.getHorizontalCut().getDestination()) &&
                    isPointInsidePanel(lShapedCut.getVerticalCut().getOrigin()) &&
                    isPointInsidePanel(lShapedCut.getVerticalCut().getDestination());
        } else if (cut instanceof RectangularCut) {
            RectangularCut rectangularCut = (RectangularCut) cut;
            return isPointInsidePanel(rectangularCut.getCorner()) &&
                    isPointInsidePanel(rectangularCut.getIntersection());
        } else if (cut instanceof ParallelCut) {
            ParallelCut parallelCut = (ParallelCut) cut;
            return isPointInsidePanel(parallelCut.getOrigin()) &&
                    isPointInsidePanel(parallelCut.getDestination());
        } else if (cut instanceof ReCut) {
            ReCut reCut = (ReCut) cut;
            return isPointInsidePanel(reCut.getTopHorizontalCut().getOrigin()) &&
                    isPointInsidePanel(reCut.getTopHorizontalCut().getDestination()) &&
                    isPointInsidePanel(reCut.getBottomHorizontalCut().getOrigin()) &&
                    isPointInsidePanel(reCut.getBottomHorizontalCut().getDestination()) &&
                    isPointInsidePanel(reCut.getLeftVerticalCut().getOrigin()) &&
                    isPointInsidePanel(reCut.getLeftVerticalCut().getDestination()) &&
                    isPointInsidePanel(reCut.getRightVerticalCut().getOrigin()) &&
                    isPointInsidePanel(reCut.getRightVerticalCut().getDestination());
        }
        return false;
    }

    private boolean isPointInsidePanel(Coordinate point) {
        return point.getX() >= 0 && point.getX() <= dimension.getWidth() &&
                point.getY() >= 0 && point.getY() <= dimension.getHeight();
    }

}
