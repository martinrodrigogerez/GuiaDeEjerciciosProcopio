import java.util.ArrayList;
import java.util.List;

public class CreadorContenido implements Publicador, Observador {
    private String nombreUsuario;
    private List<ContenidoMultimedia> contenidos = new ArrayList<>();
    private List<Suscriptor> suscriptores = new ArrayList<>();

    public CreadorContenido(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public void crearContenido(ContenidoMultimedia material) {
        contenidos.add(material);
        notificar("Nuevo contenido publicado por " + nombreUsuario + ": " + material.getTipoContenido());
    }

    @Override
    public void suscribir(Suscriptor usuario) {
        suscriptores.add(usuario);
    }

    @Override
    public void desuscribir(Suscriptor usuario) {
        suscriptores.remove(usuario);
    }

    @Override
    public void notificar(String mensaje) {
        for (Suscriptor s : suscriptores) {
            s.recibirNotificacion(mensaje);
        }
    }

    @Override
    public void recibirNotificacion(Interaccion interaccion, ContenidoMultimedia contenido) {
        System.out.println("📢 " + nombreUsuario + " fue notificado: "
                + interaccion.getDescripcion() + " en su contenido " + contenido.getTipoContenido());
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public List<ContenidoMultimedia> getContenidos() {
        return contenidos;
    }
}
