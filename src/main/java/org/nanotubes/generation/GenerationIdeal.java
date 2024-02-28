package org.nanotubes.generation;

import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import org.nanotubes.Geom.Particle;
import org.nanotubes.Geom.Tube;
import org.nanotubes.Geom.Vector2DDouble;
import org.nanotubes.generation.HexagonalLattice.HexagonalLattice;

import java.util.List;

import static javafx.scene.paint.Color.*;

public class GenerationIdeal {
    private final HexagonalLattice hexagonalLattice;
    private static final Color[] COLORS = new Color[] {DARKBLUE};
    private final double heightTube;
    private final double radiusTube;

    public GenerationIdeal(int coefficientI, int coefficientJ, double a, int coefficientN, int coefficientM, Tube tube) {
        this.hexagonalLattice = new HexagonalLattice(coefficientI,coefficientJ,a,coefficientN,coefficientM);
        heightTube = hexagonalLattice.TranslationVector();
        radiusTube = hexagonalLattice.ChiralityVector() / (2 * Math.PI);
        tube.setHeight(heightTube);
        tube.setRadius(radiusTube);
    }

    public ObservableList<Particle> ParticlesGeneration(ObservableList<Particle> particlesList) {
        particlesList.clear();
        List <Vector2DDouble> particles2D = hexagonalLattice.Particles();
        for (int i = 0; i < particles2D.size(); i++) {
            Particle particle3D = new Particle(20, COLORS[i % COLORS.length],
                    radiusTube * Math.cos(particles2D.get(i).getX() / radiusTube),
                    radiusTube * Math.sin(particles2D.get(i).getX() / radiusTube),
                    particles2D.get(i).getY() - heightTube / 2, i + 1);
            particlesList.add(particle3D);
        }
        return particlesList;
    }
    public int Number() {
        return hexagonalLattice.NumberOfParticle();
    }

}
