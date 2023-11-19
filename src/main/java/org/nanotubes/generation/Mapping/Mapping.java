package org.nanotubes.generation.Mapping;

import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import org.nanotubes.generation.Geom.Tube;
import org.nanotubes.generation.Geom.Particle;

/**
 * класс отображающий частицы в окне программы
 */
public class Mapping {
    /**
     * частицы, которые необходимо отобразить
     */
    private final ObservableList<Particle> list;
    /**
     * колличество частиц
     */
    private final int numberOfParticle;
    /**
     * группа графических объектов, которые необходимо отобразить
     */
    private final Group group;
    /**
     * цилиндр, который необходимо отобразить
     */
    private final Tube tube;

    /**
     * конструктор класса
     * @param numberOfParticle число частиц
     * @param group группа графических объектов
     * @param tube 3D-объект цилиндр
     * @param list список 3D-объектов - частиц
     */
    public Mapping(int numberOfParticle, Group group, Tube tube, ObservableList<Particle> list) {
        this.numberOfParticle = numberOfParticle;
        this.group = group;
        this.list = list;
        this.tube = tube;
    }

    /**
     * метод отображающий частицы в окне программы
     */
    public void MappingParticle() {
        group.getChildren().clear();
        group.getChildren().add(new TubeView(tube, Color.YELLOW).asNode());
        for (int i = 0; i < numberOfParticle; i++) {
            group.getChildren().add(list.get(i).getParticle());
        }
    }
}
