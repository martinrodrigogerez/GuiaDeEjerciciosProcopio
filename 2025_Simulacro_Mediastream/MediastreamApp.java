import java.util.*;

// ====== OBSERVER ======

interface Suscriptor {
    void recibirNotificacion(String mensaje);
}

interface Publicador {
    void suscribir(Suscriptor usuario);
    void desuscribir(Suscriptor usuario);
    void notificar(String mensaje);
}

interface Observador {
    void recibirNotificacion(Interaccion interaccion, ContenidoMultimedia contenido);
}

// ====== MODELO PRINCIPAL ======

class CreadorContenido implements Publicador, Observador {
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
}

class Usuario implements Suscriptor {
    private String nombreUsuario;

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
        System.out.println("🔔 " + nombreUsuario + " recibió notificación: " + mensaje);
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }
}

// ====== CONTENIDO ======

enum TipoContenido {
    ARTICULO, VIDEO, PODCAST
}

class ContenidoMultimedia {
    private TipoContenido tipoContenido;
    private int likes = 0;
    private int visualizaciones = 0;
    private List<String> comentarios = new ArrayList<>();

    public ContenidoMultimedia(TipoContenido tipoContenido) {
        this.tipoContenido = tipoContenido;
    }

    public void agregarComentario(String comentario) {
        comentarios.add(comentario);
    }

    public void incrementarLikes() {
        likes++;
    }

    public void incrementarVisualizaciones() {
        visualizaciones++;
    }

    public TipoContenido getTipoContenido() {
        return tipoContenido;
    }
}

// ====== INTERACCIONES ======

interface Interaccion {
    void ejecutar(ContenidoMultimedia contenido, CreadorContenido creador);
    String getDescripcion();
}

class Like implements Interaccion {
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

class Comentario implements Interaccion {
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

class Visualizacion implements Interaccion {
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

// ====== STRATEGY ======

interface RecomendacionStrategy {
    ContenidoMultimedia recomendar(List<ContenidoMultimedia> contenidos);
}

class PopularidadStrategy implements RecomendacionStrategy {
    @Override
    public ContenidoMultimedia recomendar(List<ContenidoMultimedia> contenidos) {
        System.out.println("🔎 Recomendando por popularidad...");
        return contenidos.isEmpty() ? null : contenidos.get(0); // dummy
    }
}

class Recomendador {
    private RecomendacionStrategy estrategia;

    public Recomendador(RecomendacionStrategy estrategia) {
        this.estrategia = estrategia;
    }

    public void setEstrategia(RecomendacionStrategy estrategia) {
        this.estrategia = estrategia;
    }

    public ContenidoMultimedia recomendar(List<ContenidoMultimedia> contenidos) {
        return estrategia.recomendar(contenidos);
    }
}

// ====== MAIN ======

public class MediastreamApp {
    public static void main(String[] args) {
        // 1. Crear creador y usuario
        CreadorContenido creador = new CreadorContenido("Alice");
        Usuario usuario1 = new Usuario("Bob");
        Usuario usuario2 = new Usuario("Charlie");

        // 2. Usuario sigue al creador
        usuario1.seguirCreador(creador);

        // 3. Creador publica contenido
        ContenidoMultimedia video = new ContenidoMultimedia(TipoContenido.VIDEO);
        creador.crearContenido(video);

        // 4. Usuario interactúa con el contenido
        usuario1.realizarInteraccion(video, new Like(), creador);
        usuario1.realizarInteraccion(video, new Comentario("Muy bueno!"), creador);
        usuario1.realizarInteraccion(video, new Visualizacion(), creador);

        // 5. Sistema de recomendación
        Recomendador recomendador = new Recomendador(new PopularidadStrategy());
        ContenidoMultimedia recomendado = recomendador.recomendar(List.of(video));
        if (recomendado != null) {
            System.out.println("🎯 Contenido recomendado: " + recomendado.getTipoContenido());
        }


    }
}
