
import java.io.*;
import java.util.*;

public class Alumno implements iSuscriptorNotificacion {

    private String nombre;
    private String email;
    private String telefono;

    public Alumno(String nombre, String email, String telefono) {
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
    }

    public void recibirNotificacion(Contenido contenido, iCanalStrategy canal) {
        canal.enviar(this, contenido.getDescripcion());
    }

    public String getEmail() {
        return email;
    }

    public String getTelefono() {
        return telefono;
    }

}