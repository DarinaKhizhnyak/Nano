package org.nanotubes;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.geometry.HPos;
import javafx.geometry.VPos;

import javafx.scene.*;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

import javafx.stage.Stage;

import org.nanotubes.generation.Generation;
import org.nanotubes.generation.Geom.Tube;
import org.nanotubes.generation.Mapping.TubeView;
import org.nanotubes.generation.Mapping.Mapping;
import org.nanotubes.minimization.Minimization;
import org.nanotubes.generation.Geom.Particle;
import org.nanotubes.minimization.StressMinimization;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Класс воспроизводящий программу
 */
public class NanoTube extends Application {
    /**
     * Положение "мыши" по x и y в пикселях
     */
    private double anchorX, anchorY;
    /**
     * Ширина окна в пикселях
     */
    @SuppressWarnings("FieldCanBeLocal")
    private final int WIDTH = 1400;
    /**
     * Высота окна в пикселях
     */
    @SuppressWarnings("FieldCanBeLocal")
    private final int HEIGHT = 670;
    /**
     * Радиус определяющий резкость вращения камеры
     */
    private final int RADIUS = 100;
    /**
     * Положение камеры по x при открытии окна
     */
    private final Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
    /**
     * Положение камеры по y при открытии окна
     */
    private final Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);

    /**
     * Метод содержащий элементы сцены
     * @param stage контйнер (окно)
     */
    @Override
    public void start(Stage stage) {

        var buttonEnter = new Button("Enter");
        var buttonEnergyMinimization = new Button("Minimization");
        var buttonEnergyMinimizationStress = new Button("Minimization Stress");
        var buttonDiagram = new Button("Diagram");
        var labelRadius = new Label("Cylinder's radius");
        var labelHeight = new Label("Cylinder's height");
        var labelNumber = new Label("Number Particle");
        var labelStress = new Label("Stress");
        var labelEnergy = new Label("Energy");
        var labelEnergyValue = new Label("0");
        var textFieldRadius = new TextField();
        var textFieldHeight = new TextField();
        var textNumber = new TextField();
        var textStress = new TextField();

        GridPane Top = new GridPane();
        for (int i : new int[]{120, 100, 70, 90, 60, 140, 150, 65, 1500}) {
            Top.getColumnConstraints().add(new ColumnConstraints(i));
        }
        for (int i = 0; i < 3; i++) {
            Top.getRowConstraints().add(new RowConstraints(30));
        }
        Top.getRowConstraints().add(new RowConstraints(560));

        Arrays.asList(labelRadius, labelHeight, labelNumber, labelStress, labelEnergy).forEach(label -> {
            GridPane.setHalignment(label, HPos.CENTER);
            GridPane.setValignment(label, VPos.CENTER);
        });
        Arrays.asList(buttonEnter, buttonEnergyMinimizationStress, buttonEnergyMinimization, buttonDiagram).forEach(button -> {
            GridPane.setHalignment(button, HPos.CENTER);
            GridPane.setValignment(button, VPos.CENTER);
        });

        Top.add(labelRadius, 0, 0);
        Top.add(labelHeight, 0, 1);
        Top.add(labelNumber, 0, 2);
        Top.add(textFieldRadius, 1, 0);
        Top.add(textFieldHeight, 1, 1);
        Top.add(textNumber, 1, 2);
        Top.add(buttonEnter,2,0,1,3);
        Top.add(buttonEnergyMinimization,3,0,1,3);
        Top.add(labelStress, 4, 0);
        Top.add(textStress, 5, 0);
        Top.add(labelEnergy, 4, 1,1,2);
        Top.add(labelEnergyValue, 5, 1,1,2);
        Top.add(buttonEnergyMinimizationStress,6,0,1,3);
        Top.add(buttonDiagram,7,0,1,3);

        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(10000.0);
        camera.setFieldOfView(20);
        camera.getTransforms().addAll(rotateX, rotateY, new Translate(0, 0, -500));

        Tube tube = new Tube(80,80);
        Group group3D = new Group(new TubeView(tube,Color.YELLOW).asNode());
        SubScene subScene3D = new SubScene(group3D, 750, 550, true, SceneAntialiasing.BALANCED);
        subScene3D.setFill (Color.rgb (129, 129, 129));
        subScene3D.setCamera(camera);
        var root3d = new Group(subScene3D);

        PerspectiveCamera camera2D = new PerspectiveCamera(true);
        camera2D.setNearClip(0.1);
        camera2D.setFarClip(10000.0);
        camera2D.setFieldOfView(20);
        camera2D.getTransforms().addAll(new Translate(200, 0, -500));

        Group group2D = new Group();
        SubScene subScene2D = new SubScene(group2D, 1500, 640,true, SceneAntialiasing.BALANCED);
        subScene2D.setFill (Color.rgb (245, 245, 176));
        subScene2D.setCamera(camera2D);
        var root2d = new Group(subScene2D);

        initMouseControl(subScene3D,stage,camera);
        initMouseControl2D(subScene2D,stage,camera2D);

        Top.add(root2d,8,0,1,4);
        Top.add(root3d,0,3,8,1);
        GridPane.setHalignment(root3d, HPos.CENTER);
        GridPane.setValignment(root3d, VPos.CENTER);
        GridPane.setHalignment(root2d, HPos.CENTER);
        GridPane.setValignment(root2d, VPos.CENTER);

        final ObservableList<Particle> particlesList = FXCollections.observableArrayList();

        buttonEnter.setOnAction(e -> {
            int n = Integer.parseInt(textNumber.getText());
            tube.setHeight(Double.parseDouble(textFieldHeight.getText()));
            tube.setRadius(Double.parseDouble(textFieldRadius.getText()));
            Generation generation = new Generation(tube, n, 2);
            ObservableList<Particle> particles = generation.ParticlesGeneration(particlesList);
            Mapping mapping = new Mapping(group3D,group2D,tube,particles);
            mapping.MappingParticle();
            mapping.MappingParticle2D();
            labelEnergyValue.setText(String.valueOf(generation.getEnergy(particles)));
        });

        buttonEnergyMinimization.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                Min(stage, buttonDiagram, tube, group3D, group2D, particlesList, labelEnergyValue);
            }
        });

        buttonEnergyMinimization.setOnAction(e -> {
            Min(stage, buttonDiagram, tube, group3D, group2D, particlesList, labelEnergyValue);
        });

        buttonEnergyMinimizationStress.setOnAction(e -> {
            double stress = Double.parseDouble(textStress.getText());
            new StressMinimization(particlesList,tube,stress).StressNewCoordinatesOfParticle();
            Min(stage, buttonDiagram, tube, group3D, group2D, particlesList, labelEnergyValue);
        });


        var scene = new Scene(Top, WIDTH,HEIGHT);
        stage.setScene(scene);
        stage.show();
        stage.setTitle("NanoTube Student Project");
    }

    private void Min(Stage stage, Button buttonDiagram, Tube tube, Group group3D, Group group2D, ObservableList<Particle> particlesList, Label label) {
        Minimization minimization = new Minimization(particlesList,2,tube);
        ObservableList<Particle> list = minimization.minimization();
        Mapping mapping = new Mapping(group3D, group2D ,tube,list);
        mapping.MappingParticle();
        mapping.MappingParticle2D();
//        buttonDiagram.setOnAction(actionEvent -> {
//            Chart(stage,"Step","E","Minimization Process for Energy", "Diagram for Energy",400,600, minimization.getArrayEnergy());
//            Chart(stage,"Step","k","Minimization Process for k", "Diagram for k",-300,-100, minimization.getArrayCoefficient());
//        });
        label.setText(String.valueOf(minimization.energyOfSystem(list)));
    }

    /**
     * Метод воспроизводящий сцену
     * @param args переменные
     */
    public static void main(String[] args) {
        launch();
    }


    private void initMouseControl2D(SubScene scene, Stage stage, Camera camera) {
        scene.setOnMousePressed(event -> {
            anchorX = event.getSceneX();
            anchorY = event.getSceneY();
        });
        scene.setOnMouseDragged(event -> {
            double dx = (anchorX - event.getSceneX());
            double dy = (anchorY - event.getSceneY());
            if (event.isPrimaryButtonDown()) {
                camera.getTransforms().add(new Translate(camera.getTranslateX()+dx/4,0,0));
                camera.getTransforms().add(new Translate(0,camera.getTranslateY()+dy/4,0));
            }
            anchorX = event.getSceneX();
            anchorY = event.getSceneY();
        });
        stage.addEventHandler(ScrollEvent.SCROLL, event -> {
            camera.getTransforms().add(new Translate(0,0,camera.getTranslateZ()+ event.getDeltaY()));
        });
    }

    private void initMouseControl(SubScene scene, Stage stage, Camera camera) {
        scene.setOnMousePressed(event -> {
            anchorX = event.getSceneX();
            anchorY = event.getSceneY();
        });
        scene.setOnMouseDragged(event -> {
            double dx = (anchorX - event.getSceneX());
            double dy = (anchorY - event.getSceneY());
            if (event.isPrimaryButtonDown()) {
                rotateX.setAngle(rotateX.getAngle() -
                        (dy /RADIUS  * 360) * (Math.PI / 180));
                rotateY.setAngle(rotateY.getAngle() -
                        (dx /RADIUS * -360) * (Math.PI / 180));
            }
            anchorX = event.getSceneX();
            anchorY = event.getSceneY();
        });
        stage.addEventHandler(ScrollEvent.SCROLL, event -> {
            camera.getTransforms().add(new Translate(0,0,camera.getTranslateZ()+ event.getDeltaY()));
        });
    }

    private void Chart (Stage stage, String x, String y, String title, String titleDiagram, int getX, int getY, ArrayList<Double> array) {
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel(y);
        xAxis.setLabel(x);

        final LineChart<Number,Number> lineChart = new LineChart<Number,Number>(xAxis,yAxis);
        lineChart.setTitle(title);

        XYChart.Series series = new XYChart.Series();
        series.setName(y);

        if (array.size() <= 5000) {
            for (int i = 0; i < array.size(); i++) {
                series.getData().add(new XYChart.Data(i, array.get(i)));
            }
        } else {
            for (int i = 0; i < 5000; i++) {
                series.getData().add(new XYChart.Data(i, array.get(i)));
            }
        }
        Scene scene = new Scene(lineChart, 700, 600);
        lineChart.getData().add(series);
        Stage WindowDiagram = new Stage();
        WindowDiagram.setTitle(titleDiagram);
        WindowDiagram.setScene(scene);
        WindowDiagram.setX(stage.getX() + getX);
        WindowDiagram.setY(stage.getY() + getY);
        WindowDiagram.show();
    }
}
