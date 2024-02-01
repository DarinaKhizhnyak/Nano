package org.nanotubes;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.VPos;

import javafx.scene.*;
import javafx.scene.control.*;
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
import org.nanotubes.generation.GenerationIdeal;
import org.nanotubes.generation.Geom.Tube;
import org.nanotubes.generation.Geom.Vector2DDouble;
import org.nanotubes.generation.HexagonalLattice.HexagonalLattice;
import org.nanotubes.generation.Mapping.TubeView;
import org.nanotubes.generation.Mapping.Mapping;
import org.nanotubes.minimization.Minimization;
import org.nanotubes.generation.Geom.Particle;
import org.nanotubes.minimization.StressMinimization;

import java.util.Arrays;
import java.util.List;

import static org.nanotubes.generation.Generation.energy;

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
    private final int HEIGHT = 700;
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
     *
     * @param stage контйнер (окно)
     */
    @Override
    public void start(Stage stage) {
        var menu = new TabPane();

        var buttonEnterRandom = new Button("Enter");
        var buttonEnterIdeal = new Button("Enter");
        var buttonEnergyMinimizationIdeal = new Button("Minimization");
        var buttonEnergyMinimizationRandom = new Button("Minimization");
        var buttonEnergyMinimizationStressRandom = new Button("Minimization Stress");
        var buttonEnergyMinimizationStressIdeal = new Button("Minimization Stress");
        var buttonDiagramIdeal = new Button("Diagram");
        var buttonDiagramRandom = new Button("Diagram");
        var labelParameterM = new Label("Parameter m");
        var labelParameterN = new Label("Parameter n");
        var labelCoefficientIdeal = new Label("Coefficient");
        var labelCoefficientRandom = new Label("Coefficient");
        var labelRadius = new Label("Cylinder's radius");
        var labelHeight = new Label("Cylinder's height");
        var labelNumberRandom = new Label("Number Particle");
        var labelNumberIdeal = new Label("Number Particle");
        var labelNumberChange = new Label("0");
        var labelStressRandom = new Label("Stress");
        var labelStressIdeal = new Label("Stress");
        var labelEnergyRandom = new Label("Energy");
        var labelEnergyIdeal = new Label("Energy");
        var labelEnergyValueRandom = new Label("0");
        var labelEnergyValueIdeal = new Label("0");
        var textFieldRadius = new TextField();
        var textFieldHeight = new TextField();
        var textFieldParameterM = new TextField();
        var textFieldParameterN = new TextField();
        var textFieldCoefficientIdeal = new TextField();
        var textFieldCoefficientRandom = new TextField();
        var textNumber = new TextField();
        var textStressIdeal = new TextField();
        var textStressRandom = new TextField();

        Arrays.asList(labelRadius, labelHeight, labelNumberRandom, labelStressRandom, labelEnergyRandom,
                labelCoefficientRandom, labelParameterM, labelParameterN, labelNumberIdeal, labelCoefficientIdeal,
                labelEnergyIdeal, labelStressIdeal).forEach(label -> {
            GridPane.setHalignment(label, HPos.CENTER);
            GridPane.setValignment(label, VPos.CENTER);
        });
        Arrays.asList(buttonEnterRandom, buttonEnergyMinimizationStressRandom, buttonEnergyMinimizationRandom,
                buttonDiagramRandom, buttonEnterIdeal, buttonEnergyMinimizationIdeal,
                buttonEnergyMinimizationStressIdeal, buttonDiagramIdeal).forEach(button -> {
            GridPane.setHalignment(button, HPos.CENTER);
            GridPane.setValignment(button, VPos.CENTER);
        });


        var randomTab = new Tab("Random Tube");
        randomTab.setClosable(false);
        var idealTab = new Tab("Ideal Tube");
        idealTab.setClosable(false);
        menu.getTabs().add(randomTab);
        menu.getTabs().add(idealTab);

        GridPane gridPaneRandom = new GridPane();
        randomTab.setContent(gridPaneRandom);
        for (int i : new int[]{120, 100, 140, 80, 140, 80, 1500}) {
            gridPaneRandom.getColumnConstraints().add(new ColumnConstraints(i));
        }
        for (int i = 0; i < 3; i++) {
            gridPaneRandom.getRowConstraints().add(new RowConstraints(30));
        }
        gridPaneRandom.getRowConstraints().add(new RowConstraints(560));

        gridPaneRandom.add(labelRadius, 0, 0);
        gridPaneRandom.add(labelHeight, 0, 1);
        gridPaneRandom.add(labelNumberRandom, 0, 2);
        gridPaneRandom.add(textFieldRadius, 1, 0);
        gridPaneRandom.add(textFieldHeight, 1, 1);
        gridPaneRandom.add(textNumber, 1, 2);
        gridPaneRandom.add(buttonEnterRandom, 2, 0);
        gridPaneRandom.add(buttonEnergyMinimizationRandom, 2, 1);
        gridPaneRandom.add(buttonEnergyMinimizationStressRandom, 2, 2);
        gridPaneRandom.add(labelStressRandom, 3, 0);
        gridPaneRandom.add(textStressRandom, 4, 0);
        gridPaneRandom.add(labelCoefficientRandom, 3, 1);
        gridPaneRandom.add(textFieldCoefficientRandom,4,1);
        gridPaneRandom.add(labelEnergyRandom, 3, 2);
        gridPaneRandom.add(labelEnergyValueRandom, 4, 2);
        gridPaneRandom.add(buttonDiagramRandom, 5, 0, 1, 3);

        PerspectiveCamera camera3DRandom = new PerspectiveCamera(true);
        camera3DRandom.setNearClip(0.1);
        camera3DRandom.setFarClip(100000.0);
        camera3DRandom.setFieldOfView(20);
        camera3DRandom.getTransforms().addAll(rotateX, rotateY, new Translate(0, 0, -3000));

        Tube tube = new Tube(100, 1000);
        Group group3DRandom = new Group(new TubeView(tube, Color.YELLOW).asNode());
        SubScene subScene3DRandom = new SubScene(group3DRandom, 650, 550, true, SceneAntialiasing.BALANCED);
        subScene3DRandom.setFill(Color.rgb(129, 129, 129));
        subScene3DRandom.setCamera(camera3DRandom);
        var root3dRandom = new Group(subScene3DRandom);

        PerspectiveCamera camera2DRandom = new PerspectiveCamera(true);
        camera2DRandom.setNearClip(0.1);
        camera2DRandom.setFarClip(10000.0);
        camera2DRandom.setFieldOfView(20);
        camera2DRandom.getTransforms().addAll(new Translate(1900, 0, -6000));

        Group group2DRandom = new Group();
        SubScene subScene2DRandom = new SubScene(group2DRandom, 1500, 640, true, SceneAntialiasing.BALANCED);
        subScene2DRandom.setFill(Color.rgb(245, 245, 176));
        subScene2DRandom.setCamera(camera2DRandom);
        var root2dRandom = new Group(subScene2DRandom);

        initMouseControl(subScene3DRandom, stage, camera3DRandom);
        initMouseControl2D(subScene2DRandom, stage, camera2DRandom);

        gridPaneRandom.add(root2dRandom, 6, 0, 1, 4);
        gridPaneRandom.add(root3dRandom, 0, 3, 6, 1);
        GridPane.setHalignment(root3dRandom, HPos.CENTER);
        GridPane.setValignment(root3dRandom, VPos.CENTER);
        GridPane.setHalignment(root2dRandom, HPos.CENTER);
        GridPane.setValignment(root2dRandom, VPos.CENTER);

        final ObservableList<Particle> particlesList = FXCollections.observableArrayList();

        buttonEnterRandom.setOnAction(e -> {
            int n = Integer.parseInt(textNumber.getText());
            tube.setHeight(Double.parseDouble(textFieldHeight.getText()));
            tube.setRadius(Double.parseDouble(textFieldRadius.getText()));
            Generation generation = new Generation(tube, n, 2);
            ObservableList<Particle> particles = generation.ParticlesGeneration(particlesList);
            Mapping mapping = new Mapping(group3DRandom, group2DRandom, tube, particles);
            mapping.MappingParticle();
            mapping.MappingParticle2D();
            labelEnergyValueRandom.setText(String.valueOf(generation.getEnergy(particles)));
        });

        buttonEnergyMinimizationRandom.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                double coefficient = Double.parseDouble(textFieldCoefficientRandom.getText());
                Min(tube, group3DRandom, group2DRandom, particlesList, labelEnergyValueRandom, coefficient);
            }
        });

        buttonEnergyMinimizationRandom.setOnAction(event -> {
            double coefficient = Double.parseDouble(textFieldCoefficientRandom.getText());
            Min(tube, group3DRandom, group2DRandom, particlesList, labelEnergyValueRandom,coefficient);
        });

        buttonEnergyMinimizationStressRandom.setOnAction(e -> {
            double stress = Double.parseDouble(textStressRandom.getText());
            double coefficient = Double.parseDouble(textFieldCoefficientRandom.getText());
            new StressMinimization(particlesList, tube, stress).StressNewCoordinatesOfParticle();
            Min(tube, group3DRandom, group2DRandom, particlesList, labelEnergyValueRandom, coefficient);
        });

        GridPane gridPaneIdeal = new GridPane();
        idealTab.setContent(gridPaneIdeal);
        for (int i : new int[]{120, 100, 140, 80, 140, 80, 4500}) {
            gridPaneIdeal.getColumnConstraints().add(new ColumnConstraints(i));
        }
        for (int i = 0; i < 3; i++) {
            gridPaneIdeal.getRowConstraints().add(new RowConstraints(30));
        }
        gridPaneIdeal.getRowConstraints().add(new RowConstraints(560));

        gridPaneIdeal.add(labelParameterN, 0, 0);
        gridPaneIdeal.add(labelParameterM, 0, 1);
        gridPaneIdeal.add(labelNumberIdeal,0,2);
        gridPaneIdeal.add(textFieldParameterN, 1, 0);
        gridPaneIdeal.add(textFieldParameterM, 1, 1);
        gridPaneIdeal.add(labelNumberChange,1,2);
        gridPaneIdeal.add(buttonEnterIdeal, 2, 0);
        gridPaneIdeal.add(buttonEnergyMinimizationIdeal, 2, 1);
        gridPaneIdeal.add(buttonEnergyMinimizationStressIdeal, 2, 2);
        gridPaneIdeal.add(labelStressIdeal, 3, 0);
        gridPaneIdeal.add(textStressIdeal, 4, 0);
        gridPaneIdeal.add(labelCoefficientIdeal, 3, 1);
        gridPaneIdeal.add(textFieldCoefficientIdeal,4,1);
        gridPaneIdeal.add(labelEnergyIdeal, 3, 2);
        gridPaneIdeal.add(labelEnergyValueIdeal, 4, 2);
        gridPaneIdeal.add(buttonDiagramIdeal, 5, 0, 1, 3);

        PerspectiveCamera camera3DIdeal = new PerspectiveCamera(true);
        camera3DIdeal.setNearClip(0.1);
        camera3DIdeal.setFarClip(100000.0);
        camera3DIdeal.setFieldOfView(20);
        camera3DIdeal.getTransforms().addAll(rotateX, rotateY, new Translate(0, 0, -3000));

        Group group3DIdeal = new Group(new TubeView(tube, Color.YELLOW).asNode());
        SubScene subScene3DIdeal = new SubScene(group3DIdeal, 650, 550, true, SceneAntialiasing.BALANCED);
        subScene3DIdeal.setFill(Color.rgb(129, 129, 129));
        subScene3DIdeal.setCamera(camera3DIdeal);
        var root3dIdeal = new Group(subScene3DIdeal);

        PerspectiveCamera camera2DIdeal = new PerspectiveCamera(true);
        camera2DIdeal.setNearClip(0.1);
        camera2DIdeal.setFarClip(10000.0);
        camera2DIdeal.setFieldOfView(20);
        camera2DIdeal.getTransforms().addAll(new Translate(6700, 0, -6000));

        Group group2DIdeal = new Group();
        SubScene subScene2DIdeal = new SubScene(group2DIdeal, 4500, 640, true, SceneAntialiasing.BALANCED);
        subScene2DIdeal.setFill(Color.rgb(245, 245, 176));
        subScene2DIdeal.setCamera(camera2DIdeal);
        var root2dIdeal = new Group(subScene2DIdeal);

        initMouseControl(subScene3DIdeal, stage, camera3DIdeal);
        initMouseControl2D(subScene2DIdeal, stage, camera2DIdeal);

        gridPaneIdeal.add(root2dIdeal, 6, 0, 1, 4);
        gridPaneIdeal.add(root3dIdeal, 0, 3, 6, 1);
        GridPane.setHalignment(root3dIdeal, HPos.CENTER);
        GridPane.setValignment(root3dIdeal, VPos.CENTER);
        GridPane.setHalignment(root2dIdeal, HPos.CENTER);
        GridPane.setValignment(root2dIdeal, VPos.CENTER);

        buttonEnterIdeal.setOnAction(e -> {
            int n = Integer.parseInt(textFieldParameterN.getText());
            int m = Integer.parseInt(textFieldParameterM.getText());
            GenerationIdeal generationIdeal = new GenerationIdeal(60,60,70,n,m,tube);
            ObservableList<Particle> particles = generationIdeal.ParticlesGeneration(particlesList);
            Mapping mapping = new Mapping(group3DIdeal, group2DIdeal, tube, particles);
            mapping.MappingParticle();
            mapping.MappingParticle2D();
            labelNumberChange.setText(String.valueOf(particles.size()));
            labelEnergyValueIdeal.setText(String.valueOf(energy(particles,particles.size(),3,tube.getHeight())));
        });

        buttonEnergyMinimizationIdeal.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                double coefficient = Double.parseDouble(textFieldCoefficientIdeal.getText());
                Min(tube, group3DIdeal, group2DIdeal, particlesList, labelEnergyValueRandom, coefficient);
            }
        });

        buttonEnergyMinimizationIdeal.setOnAction(e -> {
            double coefficient = Double.parseDouble(textFieldCoefficientIdeal.getText());
            Min(tube, group3DIdeal, group2DIdeal, particlesList, labelEnergyValueRandom, coefficient);
        });

        buttonEnergyMinimizationStressIdeal.setOnAction(e -> {
            double coefficient = Double.parseDouble(textFieldCoefficientIdeal.getText());
            double stress = Double.parseDouble(textStressIdeal.getText());
            new StressMinimization(particlesList, tube, stress).StressNewCoordinatesOfParticle();
            Min(tube, group3DIdeal, group2DIdeal, particlesList, labelEnergyValueRandom,coefficient);
        });

        var scene = new Scene(menu, WIDTH, HEIGHT);
        stage.setScene(scene);
        stage.show();
        stage.setTitle("NanoTube Student Project");
    }

    private void Min(Tube tube, Group group3D, Group group2D, ObservableList<Particle> particlesList,
                     Label label, double coefficient) {
        Minimization minimization = new Minimization(particlesList, 2, tube, coefficient);
        ObservableList<Particle> list = minimization.minimization();
        Mapping mapping = new Mapping(group3D, group2D, tube, list);
        mapping.MappingParticle();
        mapping.MappingParticle2D();
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
                camera.getTransforms().add(new Translate(camera.getTranslateX() + dx / 4, 0, 0));
                camera.getTransforms().add(new Translate(0, camera.getTranslateY() + dy / 4, 0));
            }
            anchorX = event.getSceneX();
            anchorY = event.getSceneY();
        });
        stage.addEventHandler(ScrollEvent.SCROLL,
                event -> camera.getTransforms().add(
                        new Translate(0, 0, camera.getTranslateZ() + event.getDeltaY())));
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
                        (dy / RADIUS * 360) * (Math.PI / 180));
                rotateY.setAngle(rotateY.getAngle() -
                        (dx / RADIUS * -360) * (Math.PI / 180));
            }
            anchorX = event.getSceneX();
            anchorY = event.getSceneY();
        });
        stage.addEventHandler(ScrollEvent.SCROLL, event -> camera.getTransforms().add(new Translate(0, 0, camera.getTranslateZ() + event.getDeltaY())));
    }
}
