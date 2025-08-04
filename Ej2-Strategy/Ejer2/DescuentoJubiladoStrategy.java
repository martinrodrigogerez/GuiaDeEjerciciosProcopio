
import java.io.*;
import java.util.*;

public class DescuentoJubiladoStrategy implements DescuentosStrategy {
    private double porcentajeDescuento = 0.80; // 80% descuento

    public DescuentoJubiladoStrategy() {
    }

    public DescuentoJubiladoStrategy(int descuentoJubilado) {
        this.porcentajeDescuento = descuentoJubilado;
    }

    public double calcularDescuento(Curso curso) {

        System.out.println("El descuento para un jubilado es del 80%");
        //Algoritmo para hallar el 80% de un precio
        return curso.getPrecioBase() * (1 - porcentajeDescuento);
    }


}