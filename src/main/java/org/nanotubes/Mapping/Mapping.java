package org.nanotubes.Mapping;

import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.nanotubes.Geom.*;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.triangulate.DelaunayTriangulationBuilder;

import java.util.ArrayList;
import java.util.List;

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
     *
     * @param group3D               группа графических объектов, которые необходимо отобразить в трехмерном пространстве
     * @param group2D               группа графических объектов, которые необходимо отобразить в двухмерном пространстве
     * @param tube                  3D-объект цилиндр
     * @param list                  список 3D-объектов - частиц
     */
    public Mapping(Group group3D, Group group2D, Tube tube, ObservableList<Particle> list) {
        numberOfParticle = list.size();
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
            text.setFont(new Font("times new roman", particle2D.getRadius()*1.5));
            text.setTextAlignment(TextAlignment.CENTER);
            text.setFill(Color.RED);
            text.setFontSmoothingType(FontSmoothingType.LCD);
            text.setTranslateX(particle2D.getX()-particle2D.getRadius()/2);
            text.setTranslateY(particle2D.getY()+particle2D.getRadius()/2);
            group2D.getChildren().add(text);
        }
    }
    public void MappingTriangulation() {
        List<Coordinate> coordinates = new ArrayList<>();
        for (int i = 0; i < numberOfParticle; i++) {
            Particle2D particle2D = new Particle2D(tube, list.get(i));
            coordinates.add(new Coordinate(particle2D.getX(),particle2D.getY()));
            coordinates.add(new Coordinate(particle2D.getX(),particle2D.getY()+tube.getHeight()*2));
            coordinates.add(new Coordinate(particle2D.getX(),particle2D.getY()-tube.getHeight()*2));
            coordinates.add(new Coordinate(particle2D.getX()+tube.getRadius()*4*Math.PI,particle2D.getY()));
            coordinates.add(new Coordinate(particle2D.getX()-tube.getRadius()*4*Math.PI,particle2D.getY()));
            coordinates.add(new Coordinate(particle2D.getX()+tube.getRadius()*4*Math.PI,particle2D.getY()+tube.getHeight()*2));
            coordinates.add(new Coordinate(particle2D.getX()-tube.getRadius()*4*Math.PI,particle2D.getY()+tube.getHeight()*2));
            coordinates.add(new Coordinate(particle2D.getX()+tube.getRadius()*4*Math.PI,particle2D.getY()-tube.getHeight()*2));
            coordinates.add(new Coordinate(particle2D.getX()-tube.getRadius()*4*Math.PI,particle2D.getY()-tube.getHeight()*2));
        }
        Coordinate[] points = new Coordinate[coordinates.size()];
        coordinates.toArray(points);
        GeometryFactory geometryFactory = new GeometryFactory();
        DelaunayTriangulationBuilder delaunayBuilder = new DelaunayTriangulationBuilder();
        delaunayBuilder.setSites(geometryFactory.createMultiPointFromCoords(points));
        Geometry triangles = delaunayBuilder.getTriangles(geometryFactory);
        Coordinate[] trianglesCoordinates = triangles.getCoordinates();
        List<Double> coordinatesList = new ArrayList<>();
        for (int i = 0; i < trianglesCoordinates.length-1; i++) {
            coordinatesList.add(trianglesCoordinates[i].getX());
            coordinatesList.add(trianglesCoordinates[i].getY());
        }
        List<Double> dist = new ArrayList<>();
        for (int i = 0; i < trianglesCoordinates.length-1; i++) {
            if ((trianglesCoordinates[i].getX() != trianglesCoordinates[i+1].getX()) || (trianglesCoordinates[i].getY() != trianglesCoordinates[i+1].getY())) {
                dist.add(dist(trianglesCoordinates[i].getX(), trianglesCoordinates[i].getY(), trianglesCoordinates[i + 1].getX(), trianglesCoordinates[i + 1].getY()));
            }
        }
        double sum = 0;
        for (Double aDouble : dist) {
            sum += aDouble;
        }
        double minDist = sum/dist.size();
        List<List<Double>> coordinateArrayList = chopped(coordinatesList, 6);
        for (List<Double> doubles : coordinateArrayList) {
            double dist1 = dist(doubles.get(0), doubles.get(1), doubles.get(2), doubles.get(3));
            double dist2 = dist(doubles.get(2), doubles.get(3), doubles.get(4), doubles.get(5));
            double dist3 = dist(doubles.get(4), doubles.get(5), doubles.get(0), doubles.get(1));
            if (dist1 <= (minDist*1.5) && dist2 <= (minDist*1.5) && dist3 <= (minDist*1.5)) {
                Line line1 = new Line(doubles.get(0), doubles.get(1), doubles.get(2), doubles.get(3));
                Line line2 = new Line(doubles.get(2), doubles.get(3), doubles.get(4), doubles.get(5));
                Line line3 = new Line(doubles.get(4), doubles.get(5), doubles.get(0), doubles.get(1));
                line1.setStrokeWidth(10);
                line2.setStrokeWidth(10);
                line3.setStrokeWidth(10);
                group2D.getChildren().add(line1);
                group2D.getChildren().add(line2);
                group2D.getChildren().add(line3);
            }
        }
    }

    static <T> List<List<T>> chopped(List<T> list, final int L) {
        List<List<T>> parts = new ArrayList<>();
        final int N = list.size();
        for (int i = 0; i < N; i += L) {
            parts.add(new ArrayList<>(
                    list.subList(i, Math.min(N, i + L)))
            );
        }
        return parts;
    }

    static double dist (double a1, double b1, double a2, double b2) {
        return Math.sqrt((a1-a2)*(a1-a2)+(b1-b2)*(b1-b2));
    }
}
