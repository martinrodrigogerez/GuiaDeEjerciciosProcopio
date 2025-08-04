public class Main {
    public static void main(String[] args) {
        Curso cursoPython = new Curso(10000.0);

        Persona jubilado = new Jubilado();
        jubilado.setCurso(cursoPython);
//        jubilado.agregarCurso(cursoPython);

        Persona docente = new Docente();
        docente.setCurso(cursoPython);
//        docente.agregarCurso(cursoPython);

        System.out.println("Precio del curso de: $"+ cursoPython.getPrecioBase());
        System.out.println("Precio para jubilado: $" + jubilado.calcularPrecioFinal());
        System.out.println("Precio para docente: $" + docente.calcularPrecioFinal());
    }
}
