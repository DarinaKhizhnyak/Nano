package org.nanotubes.generation.Geom;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

public class Particle {
    private double x;
    private double y;
    private double z;
    private final Sphere particle;
    private final double radius;
    private final Color color;
    public Particle(double radius, Color color, double x, double y, double z) {
        this.particle = new Sphere(radius);
        this.radius = radius;
        this.x = x;
        this.y = y;
        this.z = z;
        this.color = color;
        particle.setMaterial(new PhongMaterial(color));
        particle.setTranslateX(x);
        particle.setTranslateY(z);
        particle.setTranslateZ(y);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
        particle.setTranslateX(x);
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
        particle.setTranslateZ(y);
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
        particle.setTranslateY(z);
    }

    public Sphere getParticle() {
        return particle;
    }

    public double getRadius() {
        return radius;
    }

    public Color getColor() {
        return color;
    }

    private static double distance(double x1, double y1, double z1, double x2, double y2, double z2) {
        x1 -= x2;
        y1 -= y2;
        z1 -= z2;
        return Math.sqrt(x1 * x1 + y1 * y1 + z1 * z1);
    }
    public double distance(Particle p) {
        return distance(x, y, z, p.getX(), p.getY(), p.getZ());
    }
}
