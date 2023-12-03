package org.nanotubes.generation.Mapping;

import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.nanotubes.generation.Geom.Particle2D;
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
    private final Group group3D;
    /**
     * цилиндр, который необходимо отобразить
     */
    private final Tube tube;

    private final Group group2D;

    /**
     * конструктор класса
     * @param numberOfParticle число частиц
     * @param group2D
     * @param group3D
     * @param tube 3D-объект цилиндр
     * @param list список 3D-объектов - частиц
     */
    public Mapping(int numberOfParticle, Group group3D, Group group2D, Tube tube, ObservableList<Particle> list) {
        this.numberOfParticle = numberOfParticle;
        this.group3D = group3D;
        this.group2D = group2D;
        this.list = list;
        this.tube = tube;
    }

    /**
     * метод отображающий частицы в окне программы
     */
    public void MappingParticle() {
        group3D.getChildren().clear();
        group3D.getChildren().add(new TubeView(tube, Color.YELLOW).asNode());
        for (int i = 0; i < numberOfParticle; i++) {
            group3D.getChildren().add(list.get(i).getParticle());
        }
    }

    public void MappingParticle2D() {
        group2D.getChildren().clear();
        for (int i = 0; i < numberOfParticle; i++) {
            Particle2D particle2D = new Particle2D(tube, list.get(i));
            group2D.getChildren().add(particle2D.getCircle());
            Text text = new Text(String.valueOf(particle2D.getNumber()));
            text.setFont(new Font("times new roman", particle2D.getRadius()));
            text.setFill(Color.YELLOW);
            text.setTranslateX(particle2D.getX()-particle2D.getRadius()/2);
            text.setTranslateY(particle2D.getY());
            group2D.getChildren().add(text);
        }
    }
}
