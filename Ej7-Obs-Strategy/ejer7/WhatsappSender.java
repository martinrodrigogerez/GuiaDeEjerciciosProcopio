
import java.io.*;
import java.util.*;

public class WhatsappSender implements iCanalStrategy {
    public void enviar(Alumno alumno, String mensaje) {
        System.out.println("WHATSAPP a " + alumno.getTelefono() + ": " + mensaje);
    }

}