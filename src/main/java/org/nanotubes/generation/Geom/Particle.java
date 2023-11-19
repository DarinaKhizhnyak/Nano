package org.nanotubes.generation.Geom;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

/**
 * класс создающий трехмерную частицу
 */
public class Particle {
    /**
     * координата частицы по оси x
     */
    private double x;
    /**
     * координата частицы по оси y
     */
    private double y;
    /**
     * координата частицы по оси z
     */
    private double z;
    /**
     * форма частицы - сфера
     */
    private final Sphere particle;
    /**
     * радиус частицы
     */
    private final double radius;
    /**
     * цвет частицы
     */
    private final Color color;

    /**
     * конструктор класса
     * @param radius радиус частицы
     * @param color цвет частицы
     * @param x координата частицы по оси x
     * @param y координата частицы по оси y
     * @param z координата частицы по оси z
     */
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

    /**
     * метод возвращающий координату частицы по оси x
     * @return координата частицы по оси x
     */
    public double getX() {
        return x;
    }

    /**
     * метод изменяющий координату частицы по оси x
     * @param x новая координата частицы по оси x
     */
    public void setX(double x) {
        this.x = x;
        particle.setTranslateX(x);
    }

    /**
     * метод возвращающий координату частицы по оси y
     * @return координата частицы по оси y
     */
    public double getY() {
        return y;
    }

    /**
     * метод изменяющий координату частицы по оси y
     * @param y новая координата частицы по оси y
     */
    public void setY(double y) {
        this.y = y;
        particle.setTranslateZ(y);
    }

    /**
     * метод возвращающий координату частицы по оси z
     * @return координата частицы по оси z
     */
    public double getZ() {
        return z;
    }

    /**
     * метод изменяющий координату частицы по оси z
     * @param z новая координата частицы по оси z
     */
    public void setZ(double z) {
        this.z = z;
        particle.setTranslateY(z);
    }

    /**
     * метод возвращающий координату частицы
     * @return частица
     */
    public Sphere getParticle() {
        return particle;
    }

    /**
     * метод возвращающий радиус частицы
     * @return радиус частицы
     */
    public double getRadius() {
        return radius;
    }

    /**
     * метод возвращающий цвет частицы
     * @return цвет частицы
     */
    public Color getColor() {
        return color;
    }

    /**
     * метод возвращающий расстояние между двумя частицами в трехмерном пространстве
     * @param x1 координата первой частицы по оси x
     * @param y1 координата первой частицы по оси y
     * @param z1 координата первой частицы по оси z
     * @param x2 координата второй частицы по оси x
     * @param y2 координата второй частицы по оси y
     * @param z2 координата второй частицы по оси z
     * @return расстояние между двумя частицами в трехмерном пространстве
     */
    private static double distance(double x1, double y1, double z1, double x2, double y2, double z2) {
        x1 -= x2;
        y1 -= y2;
        z1 -= z2;
        return Math.sqrt(x1 * x1 + y1 * y1 + z1 * z1);
    }

    /**
     * метод возвращающий расстояние между двумя частицами в трехмерном пространстве
     * @param p частица до которой расчитывается расстояние от данной частицы
     * @return расстояние между двумя частицами в трехмерном пространстве
     */
    public double distance(Particle p) {
        return distance(x, y, z, p.getX(), p.getY(), p.getZ());
    }
}
