import java.util.ArrayList;
import java.util.List;

// ===== Interfaces =====
interface Observable {
    void suscribir(Observer o);
    void desuscribir(Observer o);
    void notificar(Evento e);
}

interface Observer {
    void actualizar(Evento e);
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

class Evento {
    private Libro libro;
    public Evento(Libro libro) {
        this.libro = libro;
    }
    public Libro getLibro() { return libro; }
}

// ===== Preferencias =====
class PreferenciasNotificacion {
    private List<Genero> generos = new ArrayList<>();
    private List<String> autores = new ArrayList<>();

    public void agregarGenero(Genero g) { generos.add(g); }
    public void agregarAutor(String a) { autores.add(a); }

    public boolean cumplePreferencia(Libro libro) {
        return generos.contains(libro.getGenero()) ||
                autores.contains(libro.getAutor());
    }
}

// ===== Socio =====
class Socio implements Observer {
    private String nombre;
    private PreferenciasNotificacion preferencias;

    public Socio(String nombre) {
        this.nombre = nombre;
        this.preferencias = new PreferenciasNotificacion();
    }

    public PreferenciasNotificacion getPreferencias() {
        return preferencias;
    }

    @Override
    public void actualizar(Evento e) {
        if (preferencias.cumplePreferencia(e.getLibro())) {
            System.out.println(nombre + " recibe notificación: " +
                    e.getLibro().getTitulo() + " (" + e.getLibro().getGenero() + ")");
        }
    }
}

// ===== Biblioteca =====
class Biblioteca implements Observable {
    private List<Observer> subs = new ArrayList<>();

    @Override
    public void suscribir(Observer o) {
        subs.add(o);
    }

    @Override
    public void desuscribir(Observer o) {
        subs.remove(o);
    }

    @Override
    public void notificar(Evento e) {
        for (Observer o : subs) {
            o.actualizar(e);
        }
    }

    public void agregarLibro(Libro libro) {
        System.out.println("📚 Biblioteca agrega: " + libro.getTitulo());
        notificar(new Evento(libro));
    }
}

// ===== Main de prueba =====
public class OtraSolucion {
    public static void main(String[] args) {
        Biblioteca b = new Biblioteca();

        Socio juan = new Socio("Juan");
        juan.getPreferencias().agregarGenero(Genero.CIENCIA_FICCION);

        Socio ana = new Socio("Ana");
        ana.getPreferencias().agregarGenero(Genero.CIENCIA_FICCION);
        ana.getPreferencias().agregarGenero(Genero.NOVELA_HISTORICA);

        b.suscribir(juan);
        b.suscribir(ana);

        b.agregarLibro(new Libro("Fundación", "Isaac Asimov", Genero.CIENCIA_FICCION));
        b.agregarLibro(new Libro("Los pilares de la Tierra", "Ken Follett", Genero.NOVELA_HISTORICA));
    }
}
