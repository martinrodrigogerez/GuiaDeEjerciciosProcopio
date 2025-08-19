import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// ======== Infraestructura de eventos (constantes de texto) ========
final class EventoTipo {
    private EventoTipo() {}
    public static final String INSCRIPCION = "INSCRIPCION";
    public static final String BAJA = "BAJA";
}

// ======== Observer Pattern: Publicador / Suscriptor ========
interface Publicador {
    void suscribir(Suscriptor s);
    void remove(Suscriptor s);
    void notificar(Alumno alumno, String evento);
}

interface Suscriptor {
    void recibirNotificacion(Alumno alumno, String evento);
}

// ======== Dominio ========
class Alumno {
    private final String nombre;

    public Alumno(String nombre) {
        this.nombre = Objects.requireNonNull(nombre);
    }

    public String getNombre() { return nombre; }

    public void inscribirse(ClaseGrupal clase) {
        clase.agregarAlumno(this);
    }

    public void darseBaja(ClaseGrupal clase) {
        clase.removerAlumno(this);
    }

    @Override
    public String toString() {
        return nombre;
    }
}

class ClaseGrupal implements Publicador {
    private final String descripcion;
    private int cuposDisponibles;
    private final List<Suscriptor> suscriptores = new ArrayList<>();
    private final List<Alumno> alumnos = new ArrayList<>();

    public ClaseGrupal(String descripcion, int cupos) {
        this.descripcion = Objects.requireNonNull(descripcion);
        if (cupos < 0) throw new IllegalArgumentException("Cupos no puede ser negativo");
        this.cuposDisponibles = cupos;
    }

    @Override
    public void suscribir(Suscriptor s) {
        if (s != null && !suscriptores.contains(s)) {
            suscriptores.add(s);
        }
    }

    @Override
    public void remove(Suscriptor s) {
        suscriptores.remove(s);
    }

    @Override
    public void notificar(Alumno alumno, String evento) {
        for (Suscriptor s : suscriptores) {
            s.recibirNotificacion(alumno, evento);
        }
    }

    public void agregarAlumno(Alumno alumno) {
        if (alumnos.contains(alumno)) {
            System.out.println("⚠️  " + alumno.getNombre() + " ya está inscripto en " + descripcion);
            return;
        }
        if (cuposDisponibles <= 0) {
            System.out.println("❌ Sin cupos en " + descripcion + " para " + alumno.getNombre());
            return;
        }
        alumnos.add(alumno);
        cuposDisponibles--;
        System.out.println("✅ " + alumno.getNombre() + " se inscribió en " + descripcion +
                " (cupos restantes: " + cuposDisponibles + ")");
        notificar(alumno, EventoTipo.INSCRIPCION);
    }

    public void removerAlumno(Alumno alumno) {
        if (!alumnos.contains(alumno)) {
            System.out.println("⚠️  " + alumno.getNombre() + " no estaba inscripto en " + descripcion);
            return;
        }
        alumnos.remove(alumno);
        cuposDisponibles++;
        System.out.println("🔁 " + alumno.getNombre() + " se dio de baja de " + descripcion +
                " (cupos restantes: " + cuposDisponibles + ")");
        notificar(alumno, EventoTipo.BAJA);
    }

    @Override
    public String toString() {
        return "ClaseGrupal{" + descripcion + "}";
    }
}

class Entrenador implements Suscriptor {
    private final String nombre;
    private final List<Alumno> listaDeAlumnos = new ArrayList<>();

    public Entrenador(String nombre) {
        this.nombre = Objects.requireNonNull(nombre);
    }

    @Override
    public void recibirNotificacion(Alumno alumno, String evento) {
        if (EventoTipo.INSCRIPCION.equals(evento)) {
            if (!listaDeAlumnos.contains(alumno)) listaDeAlumnos.add(alumno);
            System.out.println("👟 Entrenador " + nombre + " actualizado: + " + alumno.getNombre());
        } else if (EventoTipo.BAJA.equals(evento)) {
            listaDeAlumnos.remove(alumno);
            System.out.println("👟 Entrenador " + nombre + " actualizado: - " + alumno.getNombre());
        } else {
            System.out.println("👟 Entrenador " + nombre + " recibió evento desconocido: " + evento);
        }
    }

    public void imprimirAlumnos() {
        System.out.println("📋 Alumnos a cargo de " + nombre + ": " + listaDeAlumnos);
    }
}

class Supervisor implements Suscriptor {
    private final String nombre;
    private int totalInscripciones = 0;
    private int totalBajas = 0;

    public Supervisor(String nombre) {
        this.nombre = Objects.requireNonNull(nombre);
    }

    @Override
    public void recibirNotificacion(Alumno alumno, String evento) {
        if (EventoTipo.INSCRIPCION.equals(evento)) {
            totalInscripciones++;
            System.out.println("🧭 Supervisor " + nombre + ": inscripcion de " + alumno.getNombre() +
                    " (total inscripciones: " + totalInscripciones + ")");
        } else if (EventoTipo.BAJA.equals(evento)) {
            totalBajas++;
            System.out.println("🧭 Supervisor " + nombre + ": baja de " + alumno.getNombre() +
                    " (total bajas: " + totalBajas + ")");
        }
    }
}

// ======== Demo ========
public class MainGimnasio {
    public static void main(String[] args) {
        ClaseGrupal yoga = new ClaseGrupal("Yoga", 2);
        Entrenador entrenadorLucas = new Entrenador("Lucas");
        Supervisor supervisorMaria = new Supervisor("María");

        // Suscriptores a la clase
        yoga.suscribir(entrenadorLucas);
        yoga.suscribir(supervisorMaria);

        // Alumnos
        Alumno juan = new Alumno("Juan");
        Alumno ana  = new Alumno("Ana");
        Alumno pedro = new Alumno("Pedro");

        // Flujo
        juan.inscribirse(yoga);    // OK
        ana.inscribirse(yoga);     // OK
        pedro.inscribirse(yoga);   // Sin cupos
        ana.darseBaja(yoga);       // Libera cupo
        pedro.inscribirse(yoga);   // Ahora entra

        // Estado del entrenador
        entrenadorLucas.imprimirAlumnos();
    }
}


