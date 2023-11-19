package org.nanotubes.minimization;

import javafx.collections.ObservableList;
import org.nanotubes.generation.Geom.Particle;
import org.nanotubes.generation.Geom.Tube;

public class StressMinimization {
    private final ObservableList <Particle> particles;
    private double radiusTube, heightTube;
    private double stress;

    public StressMinimization(ObservableList<Particle> particlesList, Tube tube, double stress) {
        this.particles = particlesList;
        this.stress = stress;
        radiusTube = tube.getRadius();
        heightTube = tube.getHeight() * stress / 100;
        tube.setHeight(heightTube);
    }
}
