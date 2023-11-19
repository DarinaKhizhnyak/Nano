package org.nanotubes.generation.PoissonDisk;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.sqrt;

/**
 * класс использующий алгоритм выборки частиц при помощи диска Пуассона в произвольных размерах Роберта Бридсона, а также используется алгоритм авторства Herman Tulleken
 */
public class PoissonDiskIn2D {
    /**
     * максимально возможное количество создающихся точек
     */
    public final static int MAX_POINTS = 50000;
    /**
     * количество создающихся точек, в литературе k
     */
    private final int numberOfPoints;
    /**
     * точка 0 - начало координат
     */
    private final Vector2DDouble point0,
    /**
     * точка 1 - ширина и высота поля
     */
    point1;
    /**
     * расстояние между точками
     */
    private final Vector2DDouble dimensions;
    /**
     * размер ячейки, в литературе для двумерного пространства r / sqrt(2)
     */
    private final double cellSize;
    /**
     * минимальное растояние между частицами, в литературе - это радиус частицы r
     */
    private final double minDist;
    /**
     * размеры сетки: ширина сетки
     */
    private final int gridWidth,
    /**
     * размеры сетки: высота сетки
     */
    gridHeight;
    /**
     * случайное действительное число
     */
    private static final Random random = new Random();

    /**
     * конструктор класса
     * @param x0 координата двумерного пространства совпадающая с осью абцисс декартовой системы координат в левом нижнем углу поля
     * @param y0 координата двумерного пространства совпадающая с осью оридинат декартовой системы координат в левом нижнем углу поля
     * @param x1 координата двумерного пространства совпадающая с осью абцисс декартовой системы координат в правом верхнем углу поля
     * @param y1 координата двумерного пространства совпадающая с осью ординат декартовой системы координат в правом верхнем углу поля
     * @param minDist - минимальное расстояние между частицами равное радиусу точек
     * @param numberOfPoints количество точек
     */
    public PoissonDiskIn2D(double x0, double y0, double x1, double y1, double minDist, int numberOfPoints) {
        point0 = new Vector2DDouble(x0, y0);
        point1 = new Vector2DDouble(x1, y1);
        dimensions = new Vector2DDouble(x1 - x0, y1 - y0);

        this.minDist = minDist;
        this.numberOfPoints = numberOfPoints;
        cellSize = minDist/ sqrt(2);
        gridWidth = (int) (dimensions.getX()/cellSize) + 1;
        gridHeight = (int) (dimensions.getY()/cellSize) + 1;
    }

    /**
     * метод создающий коллекцию точек соответствующих распределению Пуассона, при условии что их меньше MAX_POINTS
     * @return коллекция точек
     */
    public List<Vector2DDouble> ListOfPointsPoissonDisk() {
        Vector2DDouble[][] grid = new Vector2DDouble[gridWidth][gridHeight];
        List<Vector2DDouble> activeList = new LinkedList<>();
        List<Vector2DDouble> pointList = new LinkedList<>();

        for (int i = 0; i < gridWidth; i++) {
            for (int j = 0; j < gridHeight; j++) {
                grid[i][j] = null;
            }
        }

        addFirstPoint(grid, activeList, pointList);

        while (!activeList.isEmpty() && (pointList.size() < MAX_POINTS)) {
            int listIndex = random.nextInt(activeList.size());
            Vector2DDouble vector2DDouble = activeList.get(listIndex);
            boolean found = false;

            for (int k = 0; k < numberOfPoints; k++) {
                found |= addNextPoint(grid, vector2DDouble, activeList, pointList);
            }

            if (!found) {
                activeList.remove(listIndex);
            }
        }
        return pointList;
    }

    /**
     * метод создающий первую точку в случайом месте сетки и записывающий её в списки
     * @param grid сетка
     * @param activeList еще не отобранные точки
     * @param pointList точки составляющие выборку по диску Пуассона
     */
    private void addFirstPoint(Vector2DDouble[][] grid, List<Vector2DDouble> activeList, List<Vector2DDouble> pointList){
        double d = random.nextDouble();
        double xr = point0.getX() + dimensions.getX() * d;

        d = random.nextDouble();
        double yr = point0.getY() + dimensions.getY() * d;

        Vector2DDouble vector2DDouble = new Vector2DDouble(xr, yr);
        Vector2DInt index = PoissonDiskIn2D.Vector2DDoubleToInt(vector2DDouble, point0, cellSize);

        grid[index.getX()][index.getY()] = vector2DDouble;

        activeList.add(vector2DDouble);
        pointList.add(vector2DDouble);
    }

    /**
     * метод преобразующий действительные кординаты точки в целочисленные координаты точки в сетке
     * @param pointDouble действительные кординаты точки
     * @param origin действительные координаты ливого нижнего угла сетки
     * @param cellSize размер ячейки
     * @return целочисленные координаты точки в сетке
     */
    private static Vector2DInt Vector2DDoubleToInt(Vector2DDouble pointDouble, Vector2DDouble origin, double cellSize){
        return new Vector2DInt((int) ((pointDouble.getX() - origin.getX()) / cellSize),
                (int) ((pointDouble.getY() - origin.getY()) / cellSize));
    }

    /**
     * метод добавляющий заданную точку в коллекцию при условии что она не находится слишком близко к существующей точке в коллекции
     * @param grid сетка
     * @param vector2DDouble новая точка, которая добавится в коллекцию
     * @return истина или ложь
     */
    private boolean addNextPoint (Vector2DDouble[][] grid, Vector2DDouble vector2DDouble, List<Vector2DDouble> activeList,
                                  List<Vector2DDouble> pointList) {
        boolean found = false;
        Vector2DDouble point = generateRandomAround(vector2DDouble,minDist);

        if ((point.getX() > point0.getX()) && (point.getX() < point1.getX())
                && (point.getY() > point0.getY()) && (point.getY() < point1.getY())) {

            Vector2DInt pointIndex = Vector2DDoubleToInt(point,point0,cellSize);
            boolean tooClose = false;

            for (int i = Math.max(0, pointIndex.getX() - 2);
                 i < Math.min(gridWidth, pointIndex.getX() + 3) && !tooClose; i++) {

                for (int j = Math.max(0, pointIndex.getY() - 2);
                     j < Math.min(gridHeight, pointIndex.getY() + 3) && !tooClose; j++) {

                    if (grid[i][j] != null) {

                        if (Vector2DDouble.distance(grid[i][j], point) < minDist) {
                            tooClose = true;
                        }
                    }
                }
            } if (!tooClose) {
                found = true;
                activeList.add(point);
                pointList.add(point);
                grid[pointIndex.getX()][pointIndex.getY()] = point;
            }
        }
        return found;
    }

    /**
     * метод генерирующий случайную точку в области, центром которой является заданная точка. Область генерации (кольуо) имеет минимальный внутренний радиус, внешний радиус в два раза больше минимального радиуса.
     * @param centre точка вокруг которой происходит генерация
     * @param minDist минимальное расстояие между частицами равное радиусу частицы
     * @return случайная точка принадлежащая кольцевой области вокруг исходной точки
     */
    private static Vector2DDouble generateRandomAround(Vector2DDouble centre, double minDist) {
        double d = random.nextDouble();
        double radius = minDist + minDist * d;

        d = random.nextDouble();
        double angle = 2 * Math.PI * d;

        double newX = radius * Math.sin(angle);
        double newY = radius * Math.cos(angle);

        return new Vector2DDouble(centre.getX() + newX, centre.getY() + newY);
    }
}
