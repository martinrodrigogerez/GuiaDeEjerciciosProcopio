
import java.io.*;
import java.util.*;

public class Jubilado extends Persona {

    public Jubilado() {
        super();
        this.setDescuento(new DescuentoJubiladoStrategy());
    }

    public Boolean esEgresadoDeLaCasa;

}