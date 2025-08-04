
import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

public abstract class Contenido {

    public Contenido() {
    }

    private LocalDateTime fecha;

    protected String descripcion;

    public String getDescripcion() {
        return descripcion;
    }

}