package org.nanotubes.minimization;

import javafx.collections.ObservableList;
import org.nanotubes.generation.Geom.Particle;
import org.nanotubes.generation.Geom.Tube;

import java.util.ArrayList;

import static java.lang.Math.pow;


/**
 * Класс, который минимизирует энергию системы частиц
 */
public class Minimization {
    private double COEFFICIENT = 500;

    @SuppressWarnings("FieldCanBeLocal")
    private final double ACCEPTABLE_COEFFICIENT = 0;
    /**
     * Допустимое значение коэффициента разницы энергий
     */
    @SuppressWarnings("FieldCanBeLocal")
    private final double ACCEPTABLE_VALUE_OF_ENERGY_DIFFERENCE = 0;
    /**
     * Максимальное значение для типа double
     */
    @SuppressWarnings("FieldCanBeLocal")
    private static final double MAX_VALUE = 1.7976931348623157E308;

    /**
     * Исходные координаты частиц, выбранные с помощью распределения Пуассона
     */
    private final ObservableList<Particle> particles;
    /**
     * Количество частиц
     */
    private final int numberOfParticle;
    /**
     * Степень (из теории)
     */
    private final int degree;
    /**
     * Параметры цилиндра
     */
    private final double heightTube;
    private final double radiusTube;

    /**
     * Массив для записи значений энергий системы на каждом шаге минимизации
     */
    private final ArrayList<Double> arrayEnergy = new ArrayList<>();
    /**
     * Массив для записи значений коэффициента координаты на каждом шаге системы
     */
    private final ArrayList<Double> arrayCoefficient = new ArrayList<>();


    /**
     * Конструктор класса Minimization создающий обект Minimization
     * @param particleList исходные координаты частиц, выбранные с помощью распределения Пуассона
     * @param degree степень (из теории)
     * @param tube параметры цилиндра
     */
    public Minimization(ObservableList<Particle> particleList, int degree, Tube tube) {
        this.particles = particleList;
        this.degree = degree;
        numberOfParticle = particleList.size();
        heightTube = tube.getHeight();
        radiusTube = tube.getRadius();
    }

    public ObservableList<Particle> minimization () {
        ObservableList<Particle> list = particles;
        double energyOld = MAX_VALUE;
        double energyNew = energyOfSystem(list);

        arrayEnergy.add(energyNew);
        arrayCoefficient.add(COEFFICIENT);
        while (COEFFICIENT > ACCEPTABLE_COEFFICIENT && energyOld >= energyNew) {
            if (energyOld - energyNew < ACCEPTABLE_VALUE_OF_ENERGY_DIFFERENCE) {
                COEFFICIENT = COEFFICIENT/2;
            }
            energyOld = energyNew;
            stepOfMinimization(list);
            energyNew = energyOfSystem(list);

            arrayEnergy.add(energyNew);
            arrayCoefficient.add(COEFFICIENT);
        }
        return list;
    }

    /**
     * Метод возвращающий чатицы после одного шага минимизации
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
     * Метод возвращающий частицу с новыми координатами, учитывая взаимодейтвие выбранной частицы с другими частицами
     * @param coordinates частицы
     * @param i номер выбранной частицы
     * @return выбранную частицу с новыми координатами
     */
    private Particle newParticle(ObservableList<Particle> coordinates, int i) {
        Particle particle3D = new Particle(coordinates.get(i).getRadius(),coordinates.get(i).getColor(),coordinates.get(i).getX(),coordinates.get(i).getY(),coordinates.get(i).getZ());
        double ForceX = 0.0;
        double ForceY = 0.0;
        double ForceZ = 0.0;
        for (int j = 0; j < numberOfParticle; j++) {
            Particle particleJ = new Particle(coordinates.get(j).getRadius(),coordinates.get(j).getColor(),coordinates.get(j).getX(),coordinates.get(j).getY(),coordinates.get(j).getZ());
            if (i != j) {
                ForceX += (degree * (particle3D.getX()-particleJ.getX()))/ pow(particle3D.distance(particleJ),degree+2);
                ForceY += (degree * (particle3D.getY()-particleJ.getY()))/ pow(particle3D.distance(particleJ),degree+2);
                ForceZ += (degree * (particle3D.getZ()-particleJ.getZ()))/ pow(particle3D.distance(particleJ),degree+2);
            }
        }
        ForceZ += degree / pow(particle3D.getZ() - heightTube / 2, degree + 1) +
                degree / pow(particle3D.getZ() + heightTube / 2, degree + 1);

        double x = particle3D.getX() + COEFFICIENT * ForceX;
        double y = particle3D.getY() + COEFFICIENT * ForceY;
        double z = particle3D.getZ() + COEFFICIENT * ForceZ;

        double rho = Math.sqrt(x * x + y * y);

        particle3D.setX(radiusTube * x/rho);
        particle3D.setY(radiusTube * y/rho);

        if (z < heightTube/2 && z > -heightTube/2) {
            particle3D.setZ(z);
        }

        return particle3D;
    }



    /**
     * Метод возвращающий энергию системы в двумерном пространстве развертки боковой стороны цилиндра
     * @param coordinates частицы
     * @return энергию системы частиц
     */
    private double energyOfSystem (ObservableList<Particle> coordinates) {
        double Energy = 0;
        for (int i = 0; i < numberOfParticle; i++) {
            for (int j = 0; j < numberOfParticle; j++) {
                if (i != j) {
                    Energy += 1/pow(coordinates.get(i).distance(coordinates.get(j)),degree);
                }
            }
            Energy += 1 / pow(coordinates.get(i).getZ() - heightTube / 2, degree) +
                    1 / pow(coordinates.get(i).getZ() + heightTube / 2, degree);
        }
        return Energy;
    }

    /**
     * Метод возвращающий значение энергии системы при изменении координат одного элемета системы
     * @param coordinates частицы
     * @param particle измененная частица
     * @return значение энергии системы при изменении координат одного элемета системы
     */
    private double energyOfPartial (ObservableList<Particle> coordinates, Particle particle, int i) {
        double Energy = 0;
        for (int j = 0; j < numberOfParticle; j++) {
            if (i != j) {
                Energy += 1/Math.pow(particle.distance(coordinates.get(i)),degree);
            }
        }
        Energy += 1 / pow(particle.getZ() - heightTube / 2, degree) +
                1 / pow(particle.getZ() + heightTube / 2, degree);
        return Energy;
    }


    /**
     * Метод возвращающий значения энергии системы на каждом шаге минимизации
     * @return массив значений энергии системы на каждом шаге минимизации
     */
    public ArrayList<Double> getArrayEnergy() {
        return arrayEnergy;
    }

    /**
     * Метод возвращающий значения коэффициента для координаты на каждом шаге минимизации
     * @return массив значений коэффициента на каждом шаге минимизации
     */
    public ArrayList<Double> getArrayCoefficient() {
        return arrayCoefficient;
    }

}
