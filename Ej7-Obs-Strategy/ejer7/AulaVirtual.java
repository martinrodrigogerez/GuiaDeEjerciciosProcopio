import java.util.ArrayList;
import java.util.List;

public class AulaVirtual implements iContenido, iPublicadorAulaVirtual {
    private List<Contenido> contenidos = new ArrayList<>();
    private List<iSuscriptorNotificacion> alumnos = new ArrayList<>();
    private iCanalStrategy canal; // canal de comunicación elegido por el aula

    public AulaVirtual(iCanalStrategy canalInicial) {
        this.canal = canalInicial;
    }

    public void addContenido(Contenido c) {
        contenidos.add(c);
        notificarSuscriptores(c);
    }

    public void removeContenido(Contenido c) {
        contenidos.remove(c);
    }


    public void registrarSuscriptor(iSuscriptorNotificacion s) {
        alumnos.add(s);
    }

    public void eliminarSuscriptor(iSuscriptorNotificacion s) {
        alumnos.remove(s);
    }

    public void notificarSuscriptores(Contenido contenido) {
        for (iSuscriptorNotificacion alumno : alumnos) {
            alumno.recibirNotificacion(contenido, canal);
        }
    }

    public void setCanal(iCanalStrategy canal) {
        this.canal = canal;
    }
}
