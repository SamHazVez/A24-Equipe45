/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Equipe45.domain;

import Equipe45.domain.Utils.Coordinate;
import Equipe45.domain.Utils.Dimension;

/**
 *
 * @author mat18
 */
public class ReCut extends Cut {

    private Dimension finalDimension;
    private RegularCut topHorizontalCut;
    private RegularCut bottomHorizontalCut;
    private RegularCut leftVerticalCut;
    private RegularCut rightVerticalCut;

    public ReCut(float depth, Tool tool, Dimension finalDimension) {
        super(depth, tool);
        this.finalDimension = finalDimension;

        // Top horizontal cut (origin at top-left corner, along the top edge)
        this.topHorizontalCut = new RegularCut(
                depth,
                tool,
                new Coordinate(0, 0),  // Top-left
                new Coordinate(finalDimension.getWidth(), 0) // Top-right
        );

        // Bottom horizontal cut (origin at bottom-left corner, along the bottom edge)
        this.bottomHorizontalCut = new RegularCut(
                depth,
                tool,
                new Coordinate(0, finalDimension.getHeight()),  // Bottom-left
                new Coordinate(finalDimension.getWidth(), finalDimension.getHeight()) // Bottom-right
        );

        // Left vertical cut (origin at top-left corner, along the left edge)
        this.leftVerticalCut = new RegularCut(
                depth,
                tool,
                new Coordinate(0, 0),  // Top-left
                new Coordinate(0, finalDimension.getHeight()) // Bottom-left
        );

        // Right vertical cut (origin at top-right corner, along the right edge)
        this.rightVerticalCut = new RegularCut(
                depth,
                tool,
                new Coordinate(finalDimension.getWidth(), 0),  // Top-right
                new Coordinate(finalDimension.getWidth(), finalDimension.getHeight()) // Bottom-right
        );
    }


    public Dimension getFinalDimension() {
        return finalDimension;
    }
    
    @Override
    public boolean isValid() {
        return true;
    }
    
    public RegularCut getTopHorizontalCut() {
        return topHorizontalCut;
    }

    public RegularCut getBottomHorizontalCut() {
        return bottomHorizontalCut;
    }

    public RegularCut getLeftVerticalCut() {
        return leftVerticalCut;
    }

    public RegularCut getRightVerticalCut() {
        return rightVerticalCut;
    }
    
    @Override
    public boolean isValid() {
        return true;
    }
}
