package org.nanotubes.generation.HexagonalLattice;

import org.nanotubes.generation.Geom.Vector2DDouble;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class HexagonalLattice {
    private final int CoefficientI;
    private final int CoefficientJ;
    private final double a;
    private final int CoefficientN;
    private final int CoefficientM;

    public HexagonalLattice(int i, int j, double a, int coefficientN, int coefficientM) {
        this.CoefficientI = i;
        this.CoefficientJ = j;
        this.a = a;
        this.CoefficientN = coefficientN;
        this.CoefficientM = coefficientM;
    }

    private List<Vector2DDouble> CreatHexagonalLattice() {
        List<Vector2DDouble> vector2DDoubles = new LinkedList<>();
        for (int i = (CoefficientI * (-1))/2; i < CoefficientI/2; i++) {
            for (int j = (CoefficientJ * (-1))/2; j < CoefficientJ/2; j++) {
                vector2DDoubles.add(new Vector2DDouble(a * (i + (double) (j % 2) /2), j * a * Math.sqrt(3)/2));
            }
        }
        return vector2DDoubles;
    }

    public double ChiralityVector(){
        return a * Math.sqrt(CoefficientN * CoefficientN + CoefficientN * CoefficientM + CoefficientM * CoefficientM);
    }

    public double TranslationVector() {
        return Math.sqrt(3) * ChiralityVector() / GreatestCommonDivisorDr();
    }

    private int GreatestCommonDivisorDr() {
        return GreatestCommonDivisor(2*CoefficientN + CoefficientM, 2*CoefficientM + CoefficientN);
    }

    private double ChiralityAngle() {
        return Math.atan(Math.sqrt(3) * CoefficientM/ (CoefficientM + 2 * CoefficientN));
    }

    private List <Vector2DDouble> TransformationHexagonalLattice() {
        List<Vector2DDouble> transformationVector2DDoubles = new LinkedList<>();
        List<Vector2DDouble> vector2DDoubles = CreatHexagonalLattice();
        for (Vector2DDouble vector2DDouble : vector2DDoubles) {
            double x0 = vector2DDouble.getX();
            double y0 = vector2DDouble.getY();
            double x = x0 * Math.cos(ChiralityAngle()) - y0 * Math.sin(ChiralityAngle());
            double y = x0 * Math.sin(ChiralityAngle()) + y0 * Math.cos(ChiralityAngle());
            transformationVector2DDoubles.add(new Vector2DDouble(x, y));
        }
        return transformationVector2DDoubles;
    }

    public int NumberOfParticle() {
        return 2*(CoefficientM*CoefficientM+CoefficientN*CoefficientN+CoefficientN*CoefficientM)/GreatestCommonDivisorDr();
    }

    public List <Vector2DDouble> Particles() {
        List<Vector2DDouble> particles = new LinkedList<>();
        List<Vector2DDouble> transformationVector2DDoubles = TransformationHexagonalLattice();
        for (Vector2DDouble vector2DDouble : transformationVector2DDoubles) {
            double x = round(vector2DDouble.getX(),4);
            double y = round(vector2DDouble.getY(),4);
            if ((x >= 0.0) && (x < ChiralityVector()) && (y >= 0.0) && (y < TranslationVector())) {
                particles.add(vector2DDouble);
            }
        }
        return particles.stream().distinct().collect(Collectors.toList());
    }

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    private int GreatestCommonDivisor(int a, int b) {
        while (a != b) {
            if (a > b) {
                a = a - b;
            } else {
                b = b - a;
            }
        }
        return a;
    }
}
