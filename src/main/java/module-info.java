module org.nanotubes {
    requires javafx.controls;
    requires java.desktop;
    requires org.locationtech.jts;
    exports org.nanotubes;
    exports org.nanotubes.generation;
    exports org.nanotubes.generation.PoissonDisk;
    exports org.nanotubes.Geom;
    exports org.nanotubes.Mapping;
}
