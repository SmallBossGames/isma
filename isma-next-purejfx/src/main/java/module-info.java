module ru.nstu.isma.ismanextpurejfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;

    opens ru.nstu.isma.ismanextpurejfx to javafx.fxml;
    exports ru.nstu.isma.ismanextpurejfx;
}