package org.nanotubes.generation.PoissonDisk;

/**
 * класс описывающий положение точки в двумерном пространстве с помощью действительных чисел
 */
public class Vector2DDouble {
    /**
     * координата частицы по оси x
     */
    private final double x;
    /**
     * координата частицы по оси y
     */
    private final double y;

    /**
     * конструктор класса создающий частицу в начале координат
     */
    public Vector2DDouble() {
        this.x = 0;
        this.y = 0;
    }

    /**
     * конструктор класса создающий частицу в заданной точке
     * @param x координата частицы по оси x
     * @param y координата частицы по оси y
     */
    public Vector2DDouble(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * метод возвращающий расстояние между двумя точками
     * @param x1 координата первой частицы по оси x
     * @param y1 координата первой частицы по оси y
     * @param x2 координата второй частицы по оси x
     * @param y2 координата второй частицы по оси y
     * @return расстояние между двумя точками
     */
    private static double distance(double x1, double y1, double x2, double y2) {
        x2 -= x1;
        y2 -= y1;
        return Math.sqrt(x2 * x2 + y2 * y2);
    }

    /**
     *  метод возвращающий расстояние между двумя частицами в двумерном пространстве
     *  @param p частица до которой расчитывается расстояние от данной частицы
     *  @return расстояние между двумя частицами в трехмерном пространстве
     */
    private double distance(Vector2DDouble p) {
        return distance(getX(), getY(), p.getX(), p.getY());
    }

    /**
     * метод возвращающий расстояние между двумя точками в двумерном пространстве
     * @param vector0 первая точка
     * @param vector1 вторая точка
     * @return расстояние между точками
     */
    public static double distance(Vector2DDouble vector0, Vector2DDouble vector1) {
        return new Vector2DDouble(vector0.getX(), vector0.getY()).distance(vector1);
    }

    /**
     * метод возвращающий координату частицы по оси x
     * @return координата частицы по оси x
     */
    public double getX() {
        return x;
    }


    /**
     * метод возвращающий координату частицы по оси y
     * @return координата частицы по оси y
     */
    public double getY() {
        return y;
    }
}
