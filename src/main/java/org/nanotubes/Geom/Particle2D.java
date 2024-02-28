package org.nanotubes.Geom;

import javafx.scene.shape.Circle;

public class Particle2D {
    private final Circle circle;
    private final double TubeRadius;
    private final int number;

    private final double x,y;
    private final double radius;

    public Particle2D(Tube tube, Particle particle) {
        this.radius = particle.getRadius()*2;
        this.circle = new Circle(radius);
        this.TubeRadius = tube.getRadius();
        this.number = particle.getNumber();
        this.x = phi(particle)*TubeRadius*2;
        this.y = particle.getZ()*2;
        circle.setCenterX(x);
        circle.setCenterY(y);
        circle.setFill(particle.getColor());
    }

    private double phi (Particle particle) {
        double x = particle.getX();
        double y = particle.getY();
        double phi;
        if (y>=0) {
            phi = Math.acos(x/ TubeRadius)+Math.PI;
        } else {
            phi = Math.PI - Math.acos(x/ TubeRadius);
        }
        return phi;
    }
    public Circle getCircle() {
        return circle;
    }

    public int getNumber() {
        return number;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getRadius() {
        return radius;
    }
}
