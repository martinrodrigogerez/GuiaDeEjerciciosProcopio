import java.util.ArrayList;
import java.util.List;

// ===== Interfaces =====
interface Publicador {
    void suscribir(Subscriber socio);
    void desuscribir(Subscriber socio);
    void notificar(Libro libro);
}

interface Subscriber {
    void recibirNotificacion(Libro libro);
}

// ===== Clases del dominio =====
enum Genero {
    CIENCIA_FICCION,
    NOVELA_HISTORICA,
    INGENIERIA,
    INFANTIL
}

class Libro {
    private String titulo;
    private String autor;
    private Genero genero;

    public Libro(String titulo, String autor, Genero genero) {
        this.titulo = titulo;
        this.autor = autor;
        this.genero = genero;
    }

    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public Genero getGenero() { return genero; }
}

// ===== Socio =====
class Socio implements Subscriber {
    private String nombre;
    private List<Genero> generosFavoritos = new ArrayList<>();
    private List<String> autoresFavoritos = new ArrayList<>();

    public Socio(String nombre) {
        this.nombre = nombre;
    }

    public void agregarGeneroFavorito(Genero genero) {
        generosFavoritos.add(genero);
    }

    public void agregarAutorFavorito(String autor) {
        autoresFavoritos.add(autor);
    }

    public boolean leInteresa(Libro libro) {
        return generosFavoritos.contains(libro.getGenero()) ||
                autoresFavoritos.contains(libro.getAutor());
    }

    @Override
    public void recibirNotificacion(Libro libro) {
        System.out.println(nombre + " recibe notificación: " +
                libro.getTitulo() + " (" + libro.getGenero() + ")");
    }
}

// ===== Biblioteca =====
class Biblioteca implements Publicador {
    private List<Subscriber> suscriptores = new ArrayList<>();

    @Override
    public void suscribir(Subscriber socio) {
        suscriptores.add(socio);
    }

    @Override
    public void desuscribir(Subscriber socio) {
        suscriptores.remove(socio);
    }

    @Override
    public void notificar(Libro libro) {
        for (Subscriber s : suscriptores) {
            if (s instanceof Socio socio) {
                if (socio.leInteresa(libro)) {
                    socio.recibirNotificacion(libro);
                }
            }
        }
    }

    public void agregarLibro(Libro libro) {
        System.out.println("📚 Biblioteca agrega: " + libro.getTitulo());
        notificar(libro);
    }
}

// ===== Main de prueba =====
public class Main {
    public static void main(String[] args) {
        Biblioteca b = new Biblioteca();

        Socio juan = new Socio("Juan");
        juan.agregarGeneroFavorito(Genero.CIENCIA_FICCION);

        Socio ana = new Socio("Ana");
        ana.agregarGeneroFavorito(Genero.CIENCIA_FICCION);
        ana.agregarGeneroFavorito(Genero.NOVELA_HISTORICA);

        b.suscribir(juan);
        b.suscribir(ana);

        b.agregarLibro(new Libro("Fundación", "Isaac Asimov", Genero.CIENCIA_FICCION));
        b.agregarLibro(new Libro("Los pilares de la Tierra", "Ken Follett", Genero.NOVELA_HISTORICA));
    }
}
