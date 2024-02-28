package org.nanotubes.Mapping;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import org.nanotubes.Geom.Tube;

/**
 * класс создающий на основе математической модели цилиндра 3D-объект
 */
public class TubeView {
    /**
     * 3D-объект цилиндр
     */
    private final Cylinder view ;

    /**
     * конструктор класса
     * @param tube 3D-объект цилиндр
     * @param color цвет цилиндра
     */
    public TubeView(Tube tube, Color color) {
        this.view = new Cylinder();
        view.setMaterial(new PhongMaterial(color));
        view.heightProperty().bind(tube.heightProperty());
        view.radiusProperty().bind(tube.radiusProperty());
    }

    /**
     * метод отображающий цилиндр в окне программы
     * @return 3D-объект цилиндр
     */
    public Node asNode() {
        return view;
    }
}
