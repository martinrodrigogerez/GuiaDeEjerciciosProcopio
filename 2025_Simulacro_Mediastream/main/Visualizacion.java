public class Visualizacion implements Interaccion {
    @Override
    public void ejecutar(ContenidoMultimedia contenido, CreadorContenido creador) {
        contenido.incrementarVisualizaciones();
        creador.recibirNotificacion(this, contenido);
    }

    @Override
    public String getDescripcion() {
        return "👀 Visualización registrada";
    }
}
