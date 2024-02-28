package org.nanotubes.generation;

import javafx.collections.ObservableList;
import javafx.scene.paint.Color;

import org.nanotubes.Geom.Tube;
import org.nanotubes.generation.PoissonDisk.PoissonDiskIn2D;
import org.nanotubes.Geom.Vector2DDouble;
import org.nanotubes.Geom.Particle;

import java.util.Collections;
import java.util.List;

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
                Particle particleJ = new Particle(coordinates.get(j).getRadius(),coordinates.get(j).getColor(),
                        coordinates.get(j).getX(),coordinates.get(j).getY(),coordinates.get(j).getZ(),
                        coordinates.get(j).getNumber());
                if (i != j) {
                    if (Math.abs(coordinates.get(i).getZ()-particleJ.getZ()) > heightTube/2) {
                        if (particleJ.getZ() < 0.0) {
                            particleJ.setZ(particleJ.getZ() + heightTube);
                            Energy += 1/Math.pow(coordinates.get(i).distance(particleJ),degree);
                        } else if (particleJ.getZ() > 0.0) {
                            particleJ.setZ(particleJ.getZ() - heightTube);
                            Energy += 1/Math.pow(coordinates.get(i).distance(particleJ),degree);
                        }
                    } else {
                        Energy += 1/Math.pow(coordinates.get(i).distance(particleJ),degree);
                    }
                }
            }
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
