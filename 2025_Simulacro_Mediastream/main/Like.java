public class Like implements Interaccion {
    @Override
    public void ejecutar(ContenidoMultimedia contenido, CreadorContenido creador) {
        contenido.incrementarLikes();
        creador.recibirNotificacion(this, contenido);
    }

    @Override
    public String getDescripcion() {
        return "👍 Like recibido";
    }
}
