public class PreferenciaNotificacion {
    private boolean notificarLikes = true;
    private boolean notificarComentarios = true;
    private boolean notificarVisualizaciones = true;
    private boolean notificarContenidoNuevo = true;

    public boolean isNotificarLikes() { return notificarLikes; }
    public void setNotificarLikes(boolean val) { notificarLikes = val; }

    public boolean isNotificarComentarios() { return notificarComentarios; }
    public void setNotificarComentarios(boolean val) { notificarComentarios = val; }

    public boolean isNotificarVisualizaciones() { return notificarVisualizaciones; }
    public void setNotificarVisualizaciones(boolean val) { notificarVisualizaciones = val; }

    public boolean isNotificarContenidoNuevo() { return notificarContenidoNuevo; }
    public void setNotificarContenidoNuevo(boolean val) { notificarContenidoNuevo = val; }
}
