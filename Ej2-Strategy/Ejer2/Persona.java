
import java.util.*;


public abstract class Persona {

    public Persona() {
        this.listaDeCursos = new HashSet<>();
    }


    private int edad;

    private DescuentosStrategy descuentoEstrategy;

    private Curso curso;

    private Set <Curso> listaDeCursos;

    public void setCurso(Curso curso){
        this.curso = curso;
    }
    public void agregarCurso(Curso curso) {
        this.listaDeCursos.add(curso);

    }

    public Set<Curso> getCursos() {
        return listaDeCursos;
    }

    public void setDescuento(DescuentosStrategy estrategia) {
        this.descuentoEstrategy = estrategia;
    }


    //En caso de tener una lista de cursos
    public double calcularDescuentoTotal() {
        double precioTotal = 0;
        //recorrer el set de cursos y hallar el precio final para todos en funcion del descuento.
        for (Curso curso : listaDeCursos) {
            if(descuentoEstrategy != null) {
                precioTotal += descuentoEstrategy.calcularDescuento(curso);
            }else {
                precioTotal += curso.getPrecioBase();
            }
        }
        return precioTotal;
    }

    public double calcularPrecioFinal() {
        return descuentoEstrategy.calcularDescuento(curso);
    }


}