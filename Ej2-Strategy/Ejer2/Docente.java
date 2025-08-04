
import java.io.*;
import java.util.*;

public class Docente extends Persona {

    public Docente() {
        super();
        this.setDescuento(new DescuentoDocenteStrategy());
    }

    public Boolean tieneMasDeUnCurso;

}