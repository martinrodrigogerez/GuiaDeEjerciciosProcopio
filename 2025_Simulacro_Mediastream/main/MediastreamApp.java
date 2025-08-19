import java.util.*;


public class MediastreamApp {
    public static void main(String[] args) {
        // Crear creador y usuarios
        CreadorContenido creador = new CreadorContenido("Alice");
        Usuario usuario1 = new Usuario("Bob");
        Usuario usuario2 = new Usuario("Charlie");

        // Ambos siguen al creador
        usuario1.seguirCreador(creador);
        usuario2.seguirCreador(creador);

        // Configurar preferencias
        usuario1.getPreferencias().setNotificarVisualizaciones(false); // Bob no quiere views
        usuario2.getPreferencias().setNotificarLikes(false);           // Charlie no quiere likes

        // Creador publica contenido
        ContenidoMultimedia video = new ContenidoMultimedia(TipoContenido.VIDEO);
        creador.crearContenido(video);

        // Interacciones
        usuario1.realizarInteraccion(video, new Like(), creador);
        usuario1.realizarInteraccion(video, new Comentario("¡Excelente video!"), creador);
        usuario1.realizarInteraccion(video, new Visualizacion(), creador);

        usuario2.realizarInteraccion(video, new Like(), creador);
        usuario2.realizarInteraccion(video, new Comentario("No está mal..."), creador);
        usuario2.realizarInteraccion(video, new Visualizacion(), creador);

        // Recomendación
        Recomendador recomendador = new Recomendador(new PopularidadStrategy());
        ContenidoMultimedia recomendado = recomendador.recomendar(creador.getContenidos());
        if (recomendado != null) {
            System.out.println("🎯 Contenido recomendado: " + recomendado.getTipoContenido());
        }
    }
}
