package Equipe45.domain;
import Equipe45.domain.DTO.LineCutDTO;
import Equipe45.domain.MeasurementUnit;
import Equipe45.domain.Utils.Coordinate;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GCodeGenerator {

    // Paramètres de vitesse et profondeur pour chaque système d'unités
    private static final double FEED_RATE_METRIC = 1500;      // mm/min
    private static final double FEED_RATE_IMPERIAL = 6000;    // in/min
    private static final double Z_SAFE_HEIGHT_METRIC = 5.0;   // mm
    private static final double Z_SAFE_HEIGHT_IMPERIAL = 0.2; // in
    private static final double CUT_DEPTH_METRIC = 2.0;       // mm
    private static final double CUT_DEPTH_IMPERIAL = 0.08;    // in

    public GCodeGenerator() {

    }

    public void generateGCode(List<LineCutDTO> cuts, String filePath, MeasurementUnit unit) throws IOException {
        sortCutsByProximity(cuts);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            if (unit == MeasurementUnit.MILLIMETER) {
                writer.write("G21 ; Définir les unités en millimètres\n");
            } else if (unit == MeasurementUnit.INCH) {
                writer.write("G20 ; Définir les unités en pouces\n");
            }

            writer.write("G90 ; Positionnement absolu\n");
            writer.write("M3 S1000 ; Démarrer la broche à 1000 RPM\n");
            writer.write("\n");

            double zSafeHeight = (unit == MeasurementUnit.MILLIMETER) ? 5.0 : 0.2; // Hauteur de sécurité
            double zCut = (unit == MeasurementUnit.MILLIMETER) ? -2.0 : -0.08; // Profondeur de coupe
            double feedRate = (unit == MeasurementUnit.MILLIMETER) ? 1500 : 6000; // Vitesse d'avance

            for (LineCutDTO cut : cuts) {
                // Rapid move to the start position at safe Z
                writer.write(String.format(Locale.US, "G0 X%.3f Y%.3f Z%.3f ; Positionnement rapide au début de la coupe\n",
                        cut.origin.getX(),
                        cut.origin.getY(),
                        zSafeHeight));

                // Descendre à la profondeur de coupe
                writer.write(String.format(Locale.US, "G1 Z%.3f F500 ; Descendre à la profondeur de coupe\n", zCut));

                // Mouvement de découpe
                writer.write(String.format(Locale.US, "G1 X%.3f Y%.3f F%.0f ; Découpe vers (%.3f, %.3f)\n",
                        cut.destination.getX(),
                        cut.destination.getY(),
                        feedRate,
                        cut.destination.getX(),
                        cut.destination.getY()));

                // Remonter à la hauteur de sécurité
                writer.write(String.format(Locale.US, "G0 Z%.3f ; Remonter à la hauteur de sécurité\n", zSafeHeight));
                writer.write("\n");
            }

            // Fin du programme
            writer.write("M5 ; Arrêter la broche\n");
            writer.write(String.format(Locale.US, "G0 X%.3f Y%.3f Z%.3f ; Retour à l'origine\n", 0.0, 0.0, 0.0));
            writer.write("M30 ; Fin du programme\n");
        }
    }
    private double convertMmToIn(double mm) {
        return mm / 25.4;
    }
    private void sortCutsByProximity(List<LineCutDTO> cuts) {
        if (cuts == null || cuts.isEmpty()) {
            return;
        }

        // Commencer par la première coupe
        List<LineCutDTO> sortedCuts = new ArrayList<>();
        sortedCuts.add(cuts.get(0));

        // Supprimer la première coupe de la liste originale
        List<LineCutDTO> remainingCuts = new ArrayList<>(cuts);
        remainingCuts.remove(0);

        while (!remainingCuts.isEmpty()) {
            LineCutDTO lastCut = sortedCuts.get(sortedCuts.size() - 1);
            LineCutDTO closestCut = null;
            double minDistance = Double.MAX_VALUE;

            for (LineCutDTO cut : remainingCuts) {
                double distance = calculateDistance(lastCut.destination, cut.origin);
                if (distance < minDistance) {
                    minDistance = distance;
                    closestCut = cut;
                }
            }

            if (closestCut != null) {
                sortedCuts.add(closestCut);
                remainingCuts.remove(closestCut);
            }
        }

        // Remplacer la liste originale par la liste triée
        cuts.clear();
        cuts.addAll(sortedCuts);
    }
    private double calculateDistance(Coordinate a, Coordinate b) {
        double deltaX = a.getX() - b.getX();
        double deltaY = a.getY() - b.getY();
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }
}