package org.nanotubes.minimization;

import javafx.collections.ObservableList;
import org.nanotubes.generation.Geom.Particle;
import org.nanotubes.generation.Geom.Tube;

public class StressMinimization {
    private final ObservableList <Particle> particles;
    private final double heightTube;
    private final int numberOfParticle;
    private final double stress;

    public StressMinimization(ObservableList<Particle> particlesList, Tube tube, double stress) {
        this.particles = particlesList;
        this.stress = stress;
        numberOfParticle = particlesList.size();
        heightTube = tube.getHeight();
        tube.setHeight(heightTube-stress);
    }

    public void StressNewCoordinatesOfParticle() {
        double change = heightTube/(heightTube-stress);
        for (int i = 0; i < numberOfParticle; i++) {
            particles.get(i).setZ(particles.get(i).getZ()/change);
        }
    }
}
