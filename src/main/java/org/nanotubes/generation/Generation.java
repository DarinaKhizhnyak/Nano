package org.nanotubes.generation;

import javafx.collections.ObservableList;
import javafx.scene.paint.Color;

import org.nanotubes.generation.Geom.Tube;
import org.nanotubes.generation.PoissonDisk.PoissonDiskIn2D;
import org.nanotubes.generation.PoissonDisk.Vector2DDouble;
import org.nanotubes.generation.Geom.Particle;

import java.util.Collections;
import java.util.List;

import static java.lang.Math.pow;
import static javafx.scene.paint.Color.*;

/**
 * класс генерирующий частицы расположенные случайным образом
 */
public class Generation {
    /**
     * цвета частиц
     */
    private static final Color[] COLORS = new Color[] {DARKBLUE};
    /**
     * число частиц
     */
    private final int numberOfParticle,
    /**
     * степень (из теории)
     */
    degree;
    /**
     * радиус частицы
     */
    private final double radius,
    /**
     * высота цилиндра
     */
    heightTube,
    /**
     * радиус цилиндра
     */
    radiusTube;

    /**
     * конструктор класса
     * @param tube параметры цилиндра
     * @param numberOfParticle количество частиц
     */
    public Generation(Tube tube, int numberOfParticle, int degree) {
        this.degree = degree;
        this.numberOfParticle = numberOfParticle;
        radius = Math.sqrt(tube.getRadius()*tube.getHeight()/(2*numberOfParticle));
        heightTube = tube.getHeight();
        radiusTube = tube.getRadius();
    }

    /**
     * метод создающий псевдослучайным образом (расспределение Пуассона) заданное количество частиц рассположенных на боковой поверхности цилиндра
     * @return список частиц
     */
    public ObservableList<Particle> ParticlesGeneration(ObservableList<Particle> particlesList) {
        particlesList.clear();
        List <Vector2DDouble> list = new PoissonDiskIn2D(0,0,radiusTube*2*Math.PI-2*radius,
                heightTube,radius*2, numberOfParticle).ListOfPointsPoissonDisk();
        Collections.shuffle(list);
        for (int i = 0; i < numberOfParticle; i++) {
            Particle particle3D = new Particle(radius, COLORS[i % COLORS.length],
                    radiusTube*Math.cos(list.get(i).getX()/radiusTube),
                    radiusTube*Math.sin(list.get(i).getX()/radiusTube),
                    list.get(i).getY()-heightTube/2,
                    i+1);
            particlesList.add(particle3D);
        }
        return particlesList;
    }

    /**
     * Метод возвращающий энергию системы
     * @param coordinates список частиц
     * @param numberOfParticle количетво частиц
     * @param degree степень (из теории)
     * @param heightTube высота цилиндра
     * @return энергия системы частиц
     */
    public static double energy(ObservableList<Particle> coordinates, int numberOfParticle, int degree, double heightTube) {
        double Energy = 0;
        for (int i = 0; i < numberOfParticle; i++) {
            for (int j = 0; j < numberOfParticle; j++) {
                if (i != j) {
                    Energy += 1/pow(coordinates.get(i).distance(coordinates.get(j)), degree);
                }
            }
            Energy += 1 / pow(coordinates.get(i).getZ() - heightTube / 2, degree) +
                    1 / pow(coordinates.get(i).getZ() + heightTube / 2, degree);
        }
        return Energy;
    }

    /**
     * метод возвращающий энергию системы частиц
     * @param coordinates частицы
     * @return энергия системы частиц
     */
    public double getEnergy(ObservableList<Particle> coordinates) {
        return energy(coordinates, numberOfParticle, degree, heightTube);
    }
}
