package org.nanotubes.generation.Geom;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * класс создающий математическую модель цилиндра со списком свойств
 */
public class Tube {
    /**
     * свойство цилиндра - высота
     */
    private final DoubleProperty height = new SimpleDoubleProperty();

    /**
     * метод возвращающий свойство цилиндра "высота"
     * @return свойство цилиндра "высота"
     */
    public DoubleProperty heightProperty() {
        return height ;
    }

    /**
     * метод возвращающий численное значение свойства цилиндра "высота"
     * @return численное значение высоты цилиндра
     */
    public final double getHeight() {
        return heightProperty().get();
    }

    /**
     * метод изменяющий численное значение свойства цилиндра "высота"
     * @param height численное значение высоты цилиндра
     */
    public final void setHeight(double height) {
        heightProperty().set(height);
    }

    /**
     * свойство цилиндра - радиус
     */
    private final DoubleProperty radius = new SimpleDoubleProperty();

    /**
     * метод возвращающий свойство цилиндра "радиус"
     * @return свойство цилиндра "радиус"
     */
    public DoubleProperty radiusProperty() {
        return radius;
    }

    /**
     * метод возвращающий численное значение свойства цилиндра "радиус"
     * @return численное значение радиуса цилиндра
     */
    public final double getRadius() {
        return radiusProperty().get();
    }

    /**
     * метод изменяющий численное значение свойства цилиндра "радиус"
     * @param radius численное значение радиуса цилиндра
     */
    public final void setRadius(double radius) {
        radiusProperty().set(radius);
    }

    /**
     * конструктор класса
     * @param radius численное значение радиуса цилиндра
     * @param height численное значение высоты цилиндра
     */
    public Tube(double radius, double height) {
        setRadius(radius);
        setHeight(height);
    }
}
