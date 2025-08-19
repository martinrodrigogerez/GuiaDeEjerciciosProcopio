public class Comentario implements Interaccion {
    private String texto;

    public Comentario(String texto) {
        this.texto = texto;
    }

    @Override
    public void ejecutar(ContenidoMultimedia contenido, CreadorContenido creador) {
        contenido.agregarComentario(texto);
        creador.recibirNotificacion(this, contenido);
    }

    @Override
    public String getDescripcion() {
        return "💬 Nuevo comentario: " + texto;
    }
}
