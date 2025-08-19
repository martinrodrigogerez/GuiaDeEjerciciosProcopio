public class Usuario implements Suscriptor {
    private String nombreUsuario;
    private PreferenciaNotificacion preferencias = new PreferenciaNotificacion();

    public Usuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public void seguirCreador(CreadorContenido creador) {
        creador.suscribir(this);
    }

    public void realizarInteraccion(ContenidoMultimedia contenido, Interaccion interaccion, CreadorContenido creador) {
        interaccion.ejecutar(contenido, creador);
    }

    @Override
    public void recibirNotificacion(String mensaje) {
        // Filtrado por preferencias
        if ((mensaje.contains("Like") && !preferencias.isNotificarLikes()) ||
                (mensaje.contains("comentario") && !preferencias.isNotificarComentarios()) ||
                (mensaje.contains("Visualización") && !preferencias.isNotificarVisualizaciones()) ||
                (mensaje.contains("Nuevo contenido") && !preferencias.isNotificarContenidoNuevo())) {
            return; // No mostrar
        }

        System.out.println("🔔 " + nombreUsuario + " recibió notificación: " + mensaje);
    }

    public PreferenciaNotificacion getPreferencias() {
        return preferencias;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }
}
