package org.nanotubes.generation.Geom;

/**
 * класс описывающий положение точки в двумерном пространстве с помощью целочисленных чисел
 */
public class Vector2DInt {
    /**
     * координата частицы по оси x
     */
    private final int x;
    /**
     * координата частицы по оси y
     */
    private final int y;

    /**
     * коструктор класса
     * @param x координата частицы по оси x
     * @param y координата частицы по оси y
     */
    public Vector2DInt(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * метод возвращающий координату частицы по оси x
     * @return координата частицы по оси x
     */
    public int getX() {
        return x;
    }

    /**
     * метод возвращающий координату частицы по оси y
     * @return координата частицы по оси y
     */
    public int getY() {
        return y;
    }
}
