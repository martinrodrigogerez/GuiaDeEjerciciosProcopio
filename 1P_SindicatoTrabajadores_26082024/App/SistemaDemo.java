// SistemaDemo.java
import java.time.LocalDateTime;
import java.util.*;

/**
 * Demo completo del sistema: Observer + State + Strategy
 * - Incidente es Publicador (Subject)
 * - Suscriptores (Trabajador, Sindical) pueden filtrar por TipoNotificacion
 * - Estado maneja el estado del Incidente (State pattern)
 * - AccionStrategy define medidas sindicales (Strategy pattern)
 *
 * Único public class: SistemaDemo (contiene main).
 */
public class SistemaDemo {
    public static void main(String[] args) {
        // Crear actores
        Trabajador ana = new Trabajador("Ana");
        Sindical sindicato = new Sindical("Sindicato Metalúrgico");

        // Definir preferencias de notificación
        ana.setPreferencias(EnumSet.of(TipoNotificacion.SEGUIMIENTO)); // sólo seguimiento
        sindicato.setPreferencias(EnumSet.of(TipoNotificacion.SEGUIMIENTO, TipoNotificacion.ALERTA)); // ambos

        // Crear incidente
        Incidente inc = new Incidente("Acoso verbal en planta", TipoDeViolencia.VERBAL, new EnProgreso());
        ana.addDenuncia(inc);

        // Suscribir
        inc.suscribir(ana);
        inc.suscribir(sindicato);

        // Cambio de estado inicial -> SinRespuesta (generará SEGUIMIENTO)
        inc.cambiarEstado(new SinRespuesta("Sin respuesta de la empresa"));
        // Empresa responde
        Empresa empresa = new Empresa("Acme S.A.");
        empresa.responderIncidente(inc, "Recibimos la denuncia, iniciamos investigacion interna.");

        // Sindicato decide una acción (Strategy)
        sindicato.setAccion(new ReunionDeMediacion()); // estrategia elegida
        sindicato.analizarRespuestaYActuar(inc);

        // Más cambios de estado: ConRespuesta -> Resuelto
        inc.cambiarEstado(new ConRespuesta("Empresa respondió y acordó medidas"));
        inc.cambiarEstado(new Resuelto("Caso resuelto tras mediación"));

        // Desuscribir
        inc.desuscribir(ana);
    }
}

/* ==========================
   ENUMS
   ========================== */
enum TipoNotificacion {
    SEGUIMIENTO,
    ALERTA
}

enum TipoDeViolencia {
    VERBAL,
    FISICA,
    PSICOLOGICA
}

/* ==========================
   OBSERVER
   ========================== */

interface Suscriptor {
    void actualizar(Notificacion n);
    boolean acepta(TipoNotificacion tipo);
    String getNombre();
}

interface Publicador {
    void suscribir(Suscriptor s);
    void desuscribir(Suscriptor s);
    void notificar(Notificacion n);
}

/* Notificación que lleva referencia al incidente y un detalle */
class Notificacion {
    private final TipoNotificacion tipo;
    private final Incidente incidente;
    private final String detalle;
    private final LocalDateTime fechaHora;

    public Notificacion(TipoNotificacion tipo, Incidente incidente, String detalle) {
        this.tipo = tipo;
        this.incidente = incidente;
        this.detalle = detalle;
        this.fechaHora = LocalDateTime.now();
    }

    public TipoNotificacion getTipo() { return tipo; }
    public Incidente getIncidente() { return incidente; }
    public String getDetalle() { return detalle; }
    public LocalDateTime getFechaHora() { return fechaHora; }

    @Override
    public String toString() {
        return "[" + fechaHora + "] " + tipo + " - " + detalle + " (Incidente: " + incidente.getDescripcion() + ")";
    }
}

/* ==========================
   SUSCRIPTORES CON PREFERENCIAS
   ========================== */

abstract class AbstractSuscriptor implements Suscriptor {
    protected final String nombre;
    protected Set<TipoNotificacion> preferencias = EnumSet.allOf(TipoNotificacion.class); // por defecto todo

    public AbstractSuscriptor(String nombre) {
        this.nombre = nombre;
    }

    public void setPreferencias(Set<TipoNotificacion> prefs) {
        if (prefs == null) return;
        this.preferencias = EnumSet.copyOf(prefs);
    }

    @Override
    public boolean acepta(TipoNotificacion tipo) {
        return preferencias.contains(tipo);
    }

    @Override
    public String getNombre() { return nombre; }
}

/* Trabajador recibe notificaciones de seguimiento (por ejemplo) */
class Trabajador extends AbstractSuscriptor {
    private final List<Incidente> denuncias = new ArrayList<>();

    public Trabajador(String nombre) {
        super(nombre);
    }

    public void addDenuncia(Incidente inc) {
        denuncias.add(inc);
    }

    @Override
    public void actualizar(Notificacion n) {
        // Lógica simple: imprimir por consola
        System.out.println("Trabajador '" + nombre + "' recibió: " + n);
    }
}

/* Sindical: actor que puede analizar y ejecutar estrategias */
class Sindical extends AbstractSuscriptor {
    private AccionStrategy accion;

    public Sindical(String nombre) {
        super(nombre);
    }

    public void setAccion(AccionStrategy accion) {
        this.accion = accion;
    }

    public void analizarRespuestaYActuar(Incidente inc) {
        // Ejemplo: decide ejecutar la acción configurada según gravedad/tipo
        if (accion == null) {
            System.out.println("[" + nombre + "] No hay accion configurada para ejecutar.");
            return;
        }
        System.out.println("[" + nombre + "] Ejecutando accion: " + accion.descripcion() + " sobre incidente '" + inc.getDescripcion() + "'");
        accion.ejecutar(inc);

        // Genera una ALERTA que se propaga a los suscriptores del incidente
        String detalle = "Sindicato propone: " + accion.descripcion();
        Notificacion alerta = new Notificacion(TipoNotificacion.ALERTA, inc, detalle);
        inc.notificar(alerta);
    }

    @Override
    public void actualizar(Notificacion n) {
        // El sindicato puede reaccionar al seguimiento, por ejemplo analizar
        System.out.println("Sindical '" + nombre + "' recibió: " + n);
    }
}

/* ==========================
   INCIDENTE (SUBJECT) + STATE
   ========================== */

class Incidente implements Publicador {
    private final String descripcion;
    private final LocalDateTime fechaYHoraCreacion;
    private final TipoDeViolencia tipoDeViolencia;
    private final List<String> respuestasDeEmpresa = new ArrayList<>();
    private final List<Suscriptor> suscriptores = new ArrayList<>();
    private Estado estado;

    public Incidente(String descripcion, TipoDeViolencia tipo, Estado estadoInicial) {
        this.descripcion = descripcion;
        this.tipoDeViolencia = tipo;
        this.fechaYHoraCreacion = LocalDateTime.now();
        this.estado = estadoInicial;
        if (this.estado != null) {
            // setea el contexto del estado si lo necesita (opcional)
            this.estado.setNombreOpcional(); // no hace nada por defecto, pero garantizamos inicialización
        }
    }

    public String getDescripcion() { return descripcion; }
    public TipoDeViolencia getTipoDeViolencia() { return tipoDeViolencia; }
    public Estado getEstado() { return estado; }

    /* Cambio de estado: delega al nuevo Estado su operación "cambiar" (State pattern) */
    public void cambiarEstado(Estado nuevoEstado) {
        if (nuevoEstado == null) return;
        nuevoEstado.cambiar(this); // la implementación del Estado hará setEstado(this) internamente
    }

    /* método interno para que el Estado pueda asignarse */
    void setEstadoInterno(Estado estado) {
        this.estado = estado;
        // cuando cambia el estado, notifica seguimiento automáticamente
        String detalle = "Estado cambiado a: " + (estado != null ? estado.nombre() : "N/A");
        Notificacion n = new Notificacion(TipoNotificacion.SEGUIMIENTO, this, detalle);
        notificar(n);
    }

    /* respuestas de la empresa (registro) */
    public void agregarRespuestaEmpresa(String respuesta) {
        respuestasDeEmpresa.add(respuesta);
        String detalle = "Empresa respondió: " + respuesta;
        Notificacion n = new Notificacion(TipoNotificacion.SEGUIMIENTO, this, detalle);
        notificar(n);
    }

    /* PUBLICADOR: suscribir / desuscribir / notificar (filtrado por preferencias) */
    @Override
    public void suscribir(Suscriptor s) {
        if (s == null) return;
        if (!suscriptores.contains(s)) suscriptores.add(s);
    }

    @Override
    public void desuscribir(Suscriptor s) {
        suscriptores.remove(s);
    }

    @Override
    public void notificar(Notificacion n) {
        // Recorre y notifica sólo a quienes aceptan el tipo
        for (Suscriptor s : new ArrayList<>(suscriptores)) {
            if (s.acepta(n.getTipo())) {
                s.actualizar(n);
            }
        }
    }
}

/* ==========================
   ESTADOS (State pattern)
   ========================== */

interface Estado {
    void cambiar(Incidente i);     // la implementación hará i.setEstadoInterno(this) u otra lógica
    String nombre();
    // por claridad, una implementación puede hacer tareas adicionales
    default void setNombreOpcional() { }
}

/* Implementaciones concretas de Estado */
class EnProgreso implements Estado {
    private final String contexto;
    public EnProgreso() { this.contexto = "En progreso"; }
    public EnProgreso(String detalle) { this.contexto = detalle; }

    @Override
    public void cambiar(Incidente i) {
        // al pasar a este estado se asigna a incidente y se notifica (setEstadoInterno hace notificación)
        i.setEstadoInterno(this);
    }

    @Override
    public String nombre() { return "EnProgreso: " + contexto; }
}

class SinRespuesta implements Estado {
    private final String contexto;
    public SinRespuesta() { this.contexto = "Sin respuesta"; }
    public SinRespuesta(String detalle) { this.contexto = detalle; }

    @Override
    public void cambiar(Incidente i) {
        i.setEstadoInterno(this);
    }

    @Override
    public String nombre() { return "SinRespuesta: " + contexto; }
}

class ConRespuesta implements Estado {
    private final String contexto;
    public ConRespuesta() { this.contexto = "Con respuesta"; }
    public ConRespuesta(String detalle) { this.contexto = detalle; }

    @Override
    public void cambiar(Incidente i) {
        i.setEstadoInterno(this);
    }

    @Override
    public String nombre() { return "ConRespuesta: " + contexto; }
}

class Resuelto implements Estado {
    private final String contexto;
    public Resuelto() { this.contexto = "Resuelto"; }
    public Resuelto(String detalle) { this.contexto = detalle; }

    @Override
    public void cambiar(Incidente i) {
        i.setEstadoInterno(this);
    }

    @Override
    public String nombre() { return "Resuelto: " + contexto; }
}

/* ==========================
   STRATEGY (Acciones sindicales)
   ========================== */

interface AccionStrategy {
    void ejecutar(Incidente i);
    String descripcion();
}

/* Ejemplos de estrategias concretas */
class ReunionDeMediacion implements AccionStrategy {
    @Override
    public void ejecutar(Incidente i) {
        // Acción: proponer mediación -> aquí sólo registramos una notificación de ALERTA vía Incidente
        System.out.println("[Accion] ReunionDeMediacion ejecutada para incidente: " + i.getDescripcion());
        // eventualmente podríamos cambiar el estado del incidente:
        i.cambiarEstado(new EnProgreso("Mediación iniciada"));
    }

    @Override
    public String descripcion() { return "Reunión de mediación"; }
}

class NegociarConLaEmpresa implements AccionStrategy {
    @Override
    public void ejecutar(Incidente i) {
        System.out.println("[Accion] NegociarConLaEmpresa ejecutada para incidente: " + i.getDescripcion());
        i.cambiarEstado(new EnProgreso("Negociación con empresa en curso"));
    }

    @Override
    public String descripcion() { return "Negociar con la empresa"; }
}

class Protesta implements AccionStrategy {
    @Override
    public void ejecutar(Incidente i) {
        System.out.println("[Accion] Protesta ejecutada para incidente: " + i.getDescripcion());
        // por ejemplo, no cambiamos estado de incidente, solo generamos alerta (Sindical se encarga)
    }

    @Override
    public String descripcion() { return "Protesta/Movilización"; }
}

/* ==========================
   EMPRESA
   ========================== */

class Empresa {
    private final String descripcion;

    public Empresa(String descripcion) {
        this.descripcion = descripcion;
    }

    public void responderIncidente(Incidente i, String respuesta) {
        System.out.println("[Empresa '" + descripcion + "'] responde al incidente: " + respuesta);
        i.agregarRespuestaEmpresa(respuesta);
        // opcional: cambiar estado cuando responde
        i.cambiarEstado(new ConRespuesta("Empresa envío respuesta formal"));
    }
}
