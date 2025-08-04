public class Curso {

    private String titulo;

    private String descripcion;

    private Double precioBase;


    public Curso(Double precioBase) {
        this.precioBase = precioBase;
    }

    public Curso(String titulo,String descripcion, Double precio) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.precioBase = precio;
    }

    Double getPrecioBase(){
        return this.precioBase;
    }


}