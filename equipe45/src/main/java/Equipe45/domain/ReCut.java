/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Equipe45.domain;

import Equipe45.domain.Utils.Coordinate;
import Equipe45.domain.Utils.CutType;
import Equipe45.domain.Utils.Dimension;

/**
 *
 * @author mat18
 */
public class ReCut extends Cut implements IRectangular{

    private Dimension finalDimension;
    private BorderCut topHorizontalCut;
    private BorderCut bottomHorizontalCut;
    private BorderCut leftVerticalCut;
    private BorderCut rightVerticalCut;
    private Panel panel;

    public ReCut(float depth, Tool tool, Dimension finalDimension) {
        super(depth, tool);
        this.finalDimension = finalDimension;
        this.recalculate();
    }

    public ReCut(float depth, Tool tool, Dimension finalDimension, Panel panel) {
        super(depth, tool);
        this.finalDimension = finalDimension;
        this.panel = panel;
        this.calculateCutsFromPanel();
    }
    
    @Override
    public ReCut asReCut() {
        return this;
    }
    
    @Override
    public void recalculate() {
        this.topHorizontalCut = new BorderCut(
                depth,
                tool,
                new Coordinate(0, 0),  // Top-left
                new Coordinate(finalDimension.getWidth(), 0),
                this
        );

        this.bottomHorizontalCut = new BorderCut(
                depth,
                tool,
                new Coordinate(0, finalDimension.getHeight()),  // Bottom-left
                new Coordinate(finalDimension.getWidth(), finalDimension.getHeight()),
                this
        );

        this.leftVerticalCut = new BorderCut(
                depth,
                tool,
                new Coordinate(0, 0),  // Top-left
                new Coordinate(0, finalDimension.getHeight()),
                this// Bottom-left
        );

        // Right vertical cut (origin at top-right corner, along the right edge)
        this.rightVerticalCut = new BorderCut(
                depth,
                tool,
                new Coordinate(finalDimension.getWidth(), 0),  // Top-right
                new Coordinate(finalDimension.getWidth(), finalDimension.getHeight()),
                this// Bottom-right
        );
    }

    private void calculateCutsFromPanel() {
        Dimension panelDimension = panel.getDimension();

        float panelCenterX = panelDimension.getWidth() / 2;
        float panelCenterY = panelDimension.getHeight() / 2;


        float finalHalfWidth = finalDimension.getWidth() / 2;
        float finalHalfHeight = finalDimension.getHeight() / 2;

        Coordinate bottomLeft = new Coordinate(panelCenterX - finalHalfWidth, panelCenterY - finalHalfHeight);
        Coordinate topRight = new Coordinate(panelCenterX + finalHalfWidth, panelCenterY + finalHalfHeight);

        bottomHorizontalCut = new BorderCut(
                0,
                tool,
                bottomLeft,
                new Coordinate(topRight.getX(), bottomLeft.getY()),
                this
        );

        topHorizontalCut = new BorderCut(
                0,
                tool,
                new Coordinate(bottomLeft.getX(), topRight.getY()), // origin
                topRight,                                          // destination
                this
        );

        leftVerticalCut = new BorderCut(
                0,
                tool,
                bottomLeft, // origin
                new Coordinate(bottomLeft.getX(), topRight.getY()), // destination
                this
        );

        rightVerticalCut = new BorderCut(
                0,
                tool,
                new Coordinate(topRight.getX(), bottomLeft.getY()), // origin
                topRight,                                          // destination
                this
        );
    }

    public Dimension getFinalDimension() {
        return finalDimension;
    }

    
    @Override
    public boolean isValid() {
        return true;
    }
    

    public BorderCut getTopHorizontalCut() {
        return topHorizontalCut;
    }

    public BorderCut getBottomHorizontalCut() {
        return bottomHorizontalCut;
    }

    public BorderCut getLeftVerticalCut() {
        return leftVerticalCut;
    }

    public BorderCut getRightVerticalCut() {
        return rightVerticalCut;
    }
    
    @Override
    public CutType getType() {
        return CutType.RECUT;
    }

    @Override
    public boolean isCoordinateInRectangle(Coordinate coordinate) {
        float minX = Math.min(topHorizontalCut.getOrigin().getX(), topHorizontalCut.getDestination().getX());
        float maxX = Math.max(topHorizontalCut.getOrigin().getX(), topHorizontalCut.getDestination().getX());
        float minY = Math.min(leftVerticalCut.getOrigin().getY(), leftVerticalCut.getDestination().getY());
        float maxY = Math.max(leftVerticalCut.getOrigin().getY(), leftVerticalCut.getDestination().getY());

        return coordinate.getX() >= minX && coordinate.getX() <= maxX &&
                coordinate.getY() >= minY && coordinate.getY() <= maxY;
    }

}
