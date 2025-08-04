
import java.io.*;
import java.util.*;

public interface iPublicadorAulaVirtual {

    void registrarSuscriptor( iSuscriptorNotificacion a);

    void eliminarSuscriptor(iSuscriptorNotificacion a);

    void notificarSuscriptores(Contenido c);

}