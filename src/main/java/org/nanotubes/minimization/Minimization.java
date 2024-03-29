package org.nanotubes.minimization;

import javafx.collections.ObservableList;
import org.nanotubes.Geom.Particle;
import org.nanotubes.Geom.Tube;

import static java.lang.Math.pow;
import static org.nanotubes.generation.Generation.energy;

/**
 * класс минимизирующий энергию системы частиц
 */
public class Minimization {
    /**
     * коэффициент (из теории)
     */
    private final double COEFFICIENT;
    /**
     * допустимое значение коэффициента
     */
    @SuppressWarnings("FieldCanBeLocal")
    private final double ACCEPTABLE_COEFFICIENT = 0.0000000000001;
    /**
     * допустимое значение коэффициента разницы энергий
     */
    @SuppressWarnings("FieldCanBeLocal")
    private final double ACCEPTABLE_VALUE_OF_ENERGY_DIFFERENCE = 0.0000000000000000001;
    /**
     * максимальное значение для типа double
     */
    @SuppressWarnings("FieldCanBeLocal")
    private static final double MAX_VALUE = 1.7976931348623157E308;
    /**
     * исходные координаты частиц, выбранные с помощью распределения Пуассона
     */
    private final ObservableList<Particle> particles;
    /**
     * количество частиц
     */
    private final int numberOfParticle,
    /**
     * степень (из теории)
     */
    degree;
    /**
     * высота цилиндра
     */
    private final double heightTube,
    /**
     *  радиус цилиндра
     */
    radiusTube;

    /**
     * конструктор класса
     * @param particleList исходные координаты частиц, выбранные с помощью распределения Пуассона
     * @param degree степень (из теории)
     * @param tube параметры цилиндра
     */
    public Minimization(ObservableList<Particle> particleList, int degree, Tube tube, double coefficient) {
        this.particles = particleList;
        this.degree = degree;
        this.COEFFICIENT = coefficient;
        numberOfParticle = particleList.size();
        heightTube = tube.getHeight();
        radiusTube = tube.getRadius();
    }

    /**
     * метод возвращающий чатицы после минимизации энергии системы
     * @return список частиц
     */
    public ObservableList<Particle> minimization () {
        double energyOld = MAX_VALUE;
        double energyNew = energyOfSystem(particles);

        int iter = 5000;
        double k = COEFFICIENT();
        System.out.println(k);
        while (k > ACCEPTABLE_COEFFICIENT && energyOld > energyNew && iter > 0) {
            iter--;
            System.out.println(iter);
            if (energyOld - energyNew < ACCEPTABLE_VALUE_OF_ENERGY_DIFFERENCE) {
                k = k/2;
            }
            energyOld = energyNew;
            stepOfMinimization(particles);
            energyNew = energyOfSystem(particles);
        }
        return particles;
    }

    private double COEFFICIENT() {
        return COEFFICIENT*radiusTube*heightTube/(2*numberOfParticle);
    }

    /**
     * метод возвращающий чатицы после одного шага минимизации
     * @param list частицы до минимизации
     */
    private void stepOfMinimization (ObservableList<Particle> list) {
        for (int i = 0; i < numberOfParticle; i++) {
            Particle particle3D = newParticle(list, i);
            if (energyOfPartial(list,list.get(i),i) > energyOfPartial(list,particle3D,i)) {
                list.set(i, particle3D);
            }
        }
    }

    /**
     * метод возвращающий частицу с новыми координатами, учитывая взаимодейтвие выбранной частицы с другими частицами
     * @param coordinates частицы
     * @param i номер выбранной частицы
     * @return выбранная частица с новыми координатами
     */
    private Particle newParticle(ObservableList<Particle> coordinates, int i) {
        Particle particle3D = new Particle(coordinates.get(i).getRadius(),coordinates.get(i).getColor(),
                coordinates.get(i).getX(),coordinates.get(i).getY(),coordinates.get(i).getZ(),
                coordinates.get(i).getNumber());
        double ForceX = 0.0;
        double ForceY = 0.0;
        double ForceZ = 0.0;
        for (int j = 0; j < numberOfParticle; j++) {
            Particle particleJ = new Particle(coordinates.get(j).getRadius(),coordinates.get(j).getColor(),
                    coordinates.get(j).getX(),coordinates.get(j).getY(),coordinates.get(j).getZ(),
                    coordinates.get(j).getNumber());
            if (i != j) {
                ForceX += (degree * (particle3D.getX()-particleJ.getX()))/ pow(particle3D.distance(particleJ),degree+2);
                ForceY += (degree * (particle3D.getY()-particleJ.getY()))/ pow(particle3D.distance(particleJ),degree+2);
                if (Math.abs(particle3D.getZ()-particleJ.getZ()) > heightTube/2) {
                    if (particleJ.getZ() < 0.0) {
                        particleJ.setZ(particleJ.getZ() + heightTube);
                        ForceZ += (degree * (particle3D.getZ()-particleJ.getZ()))/ pow(particle3D.distance(particleJ),degree+2);
                    } else if (particleJ.getZ() > 0.0) {
                        particleJ.setZ(particleJ.getZ() - heightTube);
                        ForceZ += (degree * (particle3D.getZ()-particleJ.getZ()))/ pow(particle3D.distance(particleJ),degree+2);
                    }
                } else {
                    ForceZ += (degree * (particle3D.getZ()-particleJ.getZ()))/ pow(particle3D.distance(particleJ),degree+2);
                }
            }
        }
        double x = particle3D.getX() + COEFFICIENT * ForceX;
        double y = particle3D.getY() + COEFFICIENT * ForceY;
        double z = particle3D.getZ() + COEFFICIENT * ForceZ;

        double rho = Math.sqrt(x * x + y * y);

        particle3D.setX(radiusTube * x/rho);
        particle3D.setY(radiusTube * y/rho);

        if (z > heightTube/2) {
            particle3D.setZ(z - heightTube);
        } else if (z < (-(1)*heightTube/2)) {
            particle3D.setZ(z + heightTube);
        } else {
            particle3D.setZ(z);
        }

        return particle3D;
    }

    /**
     * метод возвращающий энергию системы
     * @param coordinates частицы
     * @return энергия системы частиц
     */
    public double energyOfSystem(ObservableList<Particle> coordinates) {
        return energy(coordinates, numberOfParticle, degree, heightTube);
    }

    /**
     * метод возвращающий значение энергии системы при изменении координат одного элемета системы
     * @param coordinates частицы
     * @param particle измененная частица
     * @return значение энергии системы при изменении координат одного элемета системы
     */
    private double energyOfPartial (ObservableList<Particle> coordinates, Particle particle, int i) {
        double Energy = 0;
        for (int j = 0; j < numberOfParticle; j++) {
            Particle particleJ = new Particle(coordinates.get(j).getRadius(),coordinates.get(j).getColor(),
                    coordinates.get(j).getX(),coordinates.get(j).getY(),coordinates.get(j).getZ(),
                    coordinates.get(j).getNumber());
            if (i != j) {
                if (Math.abs(particle.getZ()-particleJ.getZ()) > heightTube/2) {
                    if (particleJ.getZ() <= 0.0) {
                        particleJ.setZ(particleJ.getZ() + heightTube);
                        Energy += 1/Math.pow(particle.distance(particleJ),degree);
                    } else if (particleJ.getZ() >= 0.0) {
                        particleJ.setZ(particleJ.getZ() - heightTube);
                        Energy += 1/Math.pow(particle.distance(particleJ),degree);
                    }
                } else {
                    Energy += 1/Math.pow(particle.distance(particleJ),degree);
                }
            }
        }
        return Energy;
    }
}
