module org.ifandidesignbeurau.pa {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;
    requires jdk.compiler;
    requires mysql.connector.j;

    opens org.tugaspbogaleri.pa to javafx.fxml;
    exports org.tugaspbogaleri.pa;
}