
import java.io.*;
import java.util.*;

public class MailSender implements iCanalStrategy {

    public void enviar(Alumno alumno, String mensaje) {
        System.out.println("EMAIL a " + alumno.getEmail() + ": " + mensaje);
    }

}