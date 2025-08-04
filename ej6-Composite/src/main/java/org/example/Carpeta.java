package org.example;

import java.util.ArrayList;
import java.util.List;

public class Carpeta implements ElementoSistema {
    private String nombre;
    private List<ElementoSistema> elementos = new ArrayList<>();

    public Carpeta(String nombre) {
        this.nombre = nombre;
    }

    public void agregar(ElementoSistema elemento) {
        elementos.add(elemento);
    }

    public void eliminar(ElementoSistema elemento) {
        elementos.remove(elemento);
    }

    @Override
    public void mostrar(String indentacion) {
        System.out.println(indentacion + "+ Carpeta: " + nombre);
        for (ElementoSistema e : elementos) {
            e.mostrar(indentacion + "  ");
        }
    }
}
