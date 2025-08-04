
import java.io.*;
import java.util.*;

public class DescuentoDocenteStrategy implements DescuentosStrategy {
    //Por defecto
    private double porcentajeDescuento = 0.20; // 15% descuento


    public DescuentoDocenteStrategy() {

    }

    public DescuentoDocenteStrategy(int porcentajeDescuentoDocente) {
        this.porcentajeDescuento = porcentajeDescuentoDocente;
    }

    public double calcularDescuento(Curso curso) {
        // TODO implement here
        System.out.println("Aplicando descuento del 20% para docente");
        return curso.getPrecioBase() * (1 - porcentajeDescuento);

    }


}