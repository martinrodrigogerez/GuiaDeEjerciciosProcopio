import java.time.LocalDateTime;
import java.util.*;

// ENUMERACIONES
enum TipoViolencia {
    VERBAL, FISICA, PSICOLOGICA
}

enum GravedadIncidente {
    LEVE, MODERADA, GRAVE, CRITICA
}

enum TipoNotificacion {
    SEGUIMIENTO_CASO, ALERTA_ACCION_SINDICAL
}

enum TipoAlerta {
    REUNION_EMERGENCIA, PROTESTA, NEGOCIACION, ACCION_LEGAL
}

// PATRÓN OBSERVER - Interfaces
interface Observer {
    void update(Incidencia incidencia, TipoNotificacion tipo);
}

// SISTEMA DE NOTIFICACIONES
class SistemaNotificaciones {
    private List<Observer> suscriptores = new ArrayList<>();

    public void addSuscriptor(Observer observer) {
        suscriptores.add(observer);
        System.out.println("Nuevo suscriptor agregado al sistema");
    }

    public void removeSuscriptor(Observer observer) {
        suscriptores.remove(observer);
    }

    public void notificarCambioEstado(Incidencia incidencia) {
        System.out.println("📢 Notificando cambio de estado para incidencia: " + incidencia.getId());
        for (Observer observer : suscriptores) {
            observer.update(incidencia, TipoNotificacion.SEGUIMIENTO_CASO);
        }
    }

    public void notificarAlertaAccion(Incidencia incidencia, TipoAlerta tipoAlerta) {
        System.out.println("🚨 ALERTA DE ACCIÓN SINDICAL: " + tipoAlerta + " para incidencia: " + incidencia.getId());
        for (Observer observer : suscriptores) {
            observer.update(incidencia, TipoNotificacion.ALERTA_ACCION_SINDICAL);
        }
    }
}

// USUARIOS
abstract class Usuario implements Observer {
    protected String nombre;
    protected String email;

    public Usuario(String nombre, String email) {
        this.nombre = nombre;
        this.email = email;
    }

    public String getNombre() { return nombre; }
}

class Trabajador extends Usuario {
    public Trabajador(String nombre, String email) {
        super(nombre, email);
    }

    @Override
    public void update(Incidencia incidencia, TipoNotificacion tipo) {
        System.out.println("👷 Trabajador " + nombre + " recibió notificación: " + tipo +
                " para caso " + incidencia.getId());
    }

    public Incidencia crearDenuncia(String descripcion, TipoViolencia tipoViolencia, GravedadIncidente gravedad) {
        System.out.println("📝 " + nombre + " está creando una denuncia por " + tipoViolencia);
        return new Incidencia(descripcion, tipoViolencia, gravedad, this);
    }
}

class RepresentanteSindical extends Usuario {
    public RepresentanteSindical(String nombre, String email) {
        super(nombre, email);
    }

    @Override
    public void update(Incidencia incidencia, TipoNotificacion tipo) {
        System.out.println("🏛️ Representante " + nombre + " recibió " + tipo +
                " para caso " + incidencia.getId());

        if (tipo == TipoNotificacion.ALERTA_ACCION_SINDICAL) {
            System.out.println("   → Analizando necesidad de acción inmediata...");
        }
    }

    public boolean analizarRespuestaEmpresa(Incidencia incidencia, String respuesta) {
        System.out.println("🔍 " + nombre + " analizando respuesta de empresa para caso " + incidencia.getId());
        // Lógica simplificada: respuesta es inadecuada si es muy corta o contiene palabras clave negativas
        boolean esAdecuada = respuesta.length() > 20 && !respuesta.toLowerCase().contains("rechaza");
        System.out.println("   → Respuesta " + (esAdecuada ? "ADECUADA" : "INADECUADA"));
        return esAdecuada;
    }
}

// PATRÓN STATE - Estados de Incidencia
interface EstadoIncidencia {
    void procesar(Incidencia incidencia);
    boolean puedeTransicionarA(EstadoIncidencia nuevoEstado);
    String getNombre();
}

class EstadoPendiente implements EstadoIncidencia {
    @Override
    public void procesar(Incidencia incidencia) {
        System.out.println("⏳ Procesando incidencia pendiente: " + incidencia.getId());
    }

    @Override
    public boolean puedeTransicionarA(EstadoIncidencia nuevoEstado) {
        return nuevoEstado instanceof EstadoEnProceso || nuevoEstado instanceof EstadoEscalado;
    }

    @Override
    public String getNombre() { return "PENDIENTE"; }
}

class EstadoEnProceso implements EstadoIncidencia {
    @Override
    public void procesar(Incidencia incidencia) {
        System.out.println("⚙️ Procesando incidencia en curso: " + incidencia.getId());
    }

    @Override
    public boolean puedeTransicionarA(EstadoIncidencia nuevoEstado) {
        return nuevoEstado instanceof EstadoResuelto || nuevoEstado instanceof EstadoEscalado;
    }

    @Override
    public String getNombre() { return "EN PROCESO"; }
}

class EstadoResuelto implements EstadoIncidencia {
    @Override
    public void procesar(Incidencia incidencia) {
        System.out.println("✅ Incidencia resuelta: " + incidencia.getId());
    }

    @Override
    public boolean puedeTransicionarA(EstadoIncidencia nuevoEstado) {
        return false; // Una vez resuelto, no puede cambiar
    }

    @Override
    public String getNombre() { return "RESUELTO"; }
}

class EstadoEscalado implements EstadoIncidencia {
    @Override
    public void procesar(Incidencia incidencia) {
        System.out.println("🔥 Procesando incidencia escalada: " + incidencia.getId());
    }

    @Override
    public boolean puedeTransicionarA(EstadoIncidencia nuevoEstado) {
        return nuevoEstado instanceof EstadoResuelto;
    }

    @Override
    public String getNombre() { return "ESCALADO"; }
}

// INCIDENCIA
class Incidencia {
    private static int contadorId = 1;
    private String id;
    private String descripcion;
    private LocalDateTime fechaCreacion;
    private EstadoIncidencia estado;
    private TipoViolencia tipoViolencia;
    private GravedadIncidente gravedad;
    private List<RespuestaEmpresa> historialRespuestas;
    private Trabajador trabajadorDenunciante;

    public Incidencia(String descripcion, TipoViolencia tipoViolencia, GravedadIncidente gravedad, Trabajador denunciante) {
        this.id = "INC-" + String.format("%03d", contadorId++);
        this.descripcion = descripcion;
        this.tipoViolencia = tipoViolencia;
        this.gravedad = gravedad;
        this.trabajadorDenunciante = denunciante;
        this.fechaCreacion = LocalDateTime.now();
        this.estado = new EstadoPendiente();
        this.historialRespuestas = new ArrayList<>();
    }

    public void cambiarEstado(EstadoIncidencia nuevoEstado) {
        if (estado.puedeTransicionarA(nuevoEstado)) {
            EstadoIncidencia estadoAnterior = this.estado;
            this.estado = nuevoEstado;
            System.out.println("🔄 Estado cambiado de " + estadoAnterior.getNombre() +
                    " a " + nuevoEstado.getNombre() + " para caso " + id);
        } else {
            System.out.println("❌ No se puede cambiar de " + estado.getNombre() +
                    " a " + nuevoEstado.getNombre());
        }
    }

    public void agregarRespuesta(RespuestaEmpresa respuesta) {
        historialRespuestas.add(respuesta);
        System.out.println("📄 Nueva respuesta agregada al caso " + id);
    }

    // Getters
    public String getId() { return id; }
    public String getDescripcion() { return descripcion; }
    public TipoViolencia getTipoViolencia() { return tipoViolencia; }
    public GravedadIncidente getGravedad() { return gravedad; }
    public EstadoIncidencia getEstado() { return estado; }
    public Trabajador getTrabajadorDenunciante() { return trabajadorDenunciante; }
}

// EMPRESA Y RESPUESTA
class Empresa {
    private String nombre;
    private String cuit;

    public Empresa(String nombre, String cuit) {
        this.nombre = nombre;
        this.cuit = cuit;
    }

    public RespuestaEmpresa responderIncidencia(Incidencia incidencia, String respuesta) {
        System.out.println("🏢 Empresa " + nombre + " respondiendo a incidencia " + incidencia.getId());
        return new RespuestaEmpresa(respuesta, this);
    }

    public String getNombre() { return nombre; }
}

class RespuestaEmpresa {
    private LocalDateTime fecha;
    private String respuesta;
    private Empresa empresa;
    private boolean esAdecuada;

    public RespuestaEmpresa(String respuesta, Empresa empresa) {
        this.fecha = LocalDateTime.now();
        this.respuesta = respuesta;
        this.empresa = empresa;
        this.esAdecuada = false; // Se evaluará posteriormente
    }

    public String getRespuesta() { return respuesta; }
    public void setEsAdecuada(boolean esAdecuada) { this.esAdecuada = esAdecuada; }
    public boolean isEsAdecuada() { return esAdecuada; }
}

// PATRÓN STRATEGY - Manejo de Casos
interface EstrategiaManejoCaso {
    TipoAlerta manejar(Incidencia incidencia);
    boolean esAplicable(TipoViolencia tipo, GravedadIncidente gravedad);
}

class EstrategiaIncidenteLeve implements EstrategiaManejoCaso {
    @Override
    public TipoAlerta manejar(Incidencia incidencia) {
        System.out.println("📋 Aplicando estrategia para incidente LEVE");
        System.out.println("   → Priorizando mediación rápida");
        return TipoAlerta.REUNION_EMERGENCIA;
    }

    @Override
    public boolean esAplicable(TipoViolencia tipo, GravedadIncidente gravedad) {
        return gravedad == GravedadIncidente.LEVE || gravedad == GravedadIncidente.MODERADA;
    }
}

class EstrategiaIncidenteGrave implements EstrategiaManejoCaso {
    @Override
    public TipoAlerta manejar(Incidencia incidencia) {
        System.out.println("⚡ Aplicando estrategia para incidente GRAVE");
        System.out.println("   → Requiere intervención legal y medidas disciplinarias");

        if (incidencia.getGravedad() == GravedadIncidente.CRITICA) {
            return TipoAlerta.ACCION_LEGAL;
        } else {
            return TipoAlerta.PROTESTA;
        }
    }

    @Override
    public boolean esAplicable(TipoViolencia tipo, GravedadIncidente gravedad) {
        return gravedad == GravedadIncidente.GRAVE || gravedad == GravedadIncidente.CRITICA;
    }
}

class GestorCasos {
    private EstrategiaManejoCaso estrategia;
    private SistemaNotificaciones sistemaNotificaciones;

    public GestorCasos(SistemaNotificaciones sistema) {
        this.sistemaNotificaciones = sistema;
    }

    public void setEstrategia(EstrategiaManejoCaso estrategia) {
        this.estrategia = estrategia;
    }

    public void manejarCaso(Incidencia incidencia) {
        System.out.println("\n🎯 GESTIONANDO CASO: " + incidencia.getId());

        // Evaluar y establecer estrategia automáticamente
        evaluarYEstablecerEstrategia(incidencia);

        // Aplicar estrategia
        TipoAlerta accionRequerida = estrategia.manejar(incidencia);

        // Cambiar estado
        incidencia.cambiarEstado(new EstadoEnProceso());
        sistemaNotificaciones.notificarCambioEstado(incidencia);

        // Notificar alerta de acción si es necesario
        if (incidencia.getGravedad() != GravedadIncidente.LEVE) {
            sistemaNotificaciones.notificarAlertaAccion(incidencia, accionRequerida);
        }
    }

    private void evaluarYEstablecerEstrategia(Incidencia incidencia) {
        EstrategiaIncidenteLeve estrategiaLeve = new EstrategiaIncidenteLeve();
        EstrategiaIncidenteGrave estrategiaGrave = new EstrategiaIncidenteGrave();

        if (estrategiaLeve.esAplicable(incidencia.getTipoViolencia(), incidencia.getGravedad())) {
            setEstrategia(estrategiaLeve);
        } else {
            setEstrategia(estrategiaGrave);
        }
    }

    public void procesarRespuestaEmpresa(Incidencia incidencia, RespuestaEmpresa respuesta, RepresentanteSindical representante) {
        System.out.println("\n📨 PROCESANDO RESPUESTA DE EMPRESA");

        incidencia.agregarRespuesta(respuesta);
        boolean esAdecuada = representante.analizarRespuestaEmpresa(incidencia, respuesta.getRespuesta());
        respuesta.setEsAdecuada(esAdecuada);

        if (esAdecuada) {
            incidencia.cambiarEstado(new EstadoResuelto());
            System.out.println("✅ Caso resuelto satisfactoriamente");
        } else {
            incidencia.cambiarEstado(new EstadoEscalado());
            sistemaNotificaciones.notificarAlertaAccion(incidencia, TipoAlerta.PROTESTA);
        }

        sistemaNotificaciones.notificarCambioEstado(incidencia);
    }
}

// PATRÓN FACTORY - Acciones Sindicales
interface AccionSindical {
    void ejecutar();
    String getDescripcion();
}

class ReunionEmergencia implements AccionSindical {
    private LocalDateTime fecha;
    private List<Usuario> participantes;

    public ReunionEmergencia() {
        this.fecha = LocalDateTime.now().plusDays(1);
        this.participantes = new ArrayList<>();
    }

    @Override
    public void ejecutar() {
        System.out.println("🤝 Ejecutando Reunión de Emergencia programada para: " + fecha);
        System.out.println("   → Convocando a mediación entre partes");
    }

    @Override
    public String getDescripcion() {
        return "Reunión de mediación urgente";
    }
}

class Protesta implements AccionSindical {
    private String lugar;
    private LocalDateTime fecha;
    private int numeroParticipantes;

    public Protesta(String lugar, int participantes) {
        this.lugar = lugar;
        this.numeroParticipantes = participantes;
        this.fecha = LocalDateTime.now().plusDays(3);
    }

    @Override
    public void ejecutar() {
        System.out.println("✊ Ejecutando Protesta en: " + lugar);
        System.out.println("   → Participantes estimados: " + numeroParticipantes);
        System.out.println("   → Fecha programada: " + fecha);
    }

    @Override
    public String getDescripcion() {
        return "Protesta sindical en " + lugar;
    }
}

class AccionLegal implements AccionSindical {
    private String tipoAccion;
    private String abogadoAsignado;

    public AccionLegal(String tipoAccion) {
        this.tipoAccion = tipoAccion;
        this.abogadoAsignado = "Dr. Martinez (Abogado Laboralista)";
    }

    @Override
    public void ejecutar() {
        System.out.println("⚖️ Ejecutando Acción Legal: " + tipoAccion);
        System.out.println("   → Abogado asignado: " + abogadoAsignado);
        System.out.println("   → Iniciando procedimientos legales");
    }

    @Override
    public String getDescripcion() {
        return "Acción legal: " + tipoAccion;
    }
}

class AccionFactory {
    public static AccionSindical crearAccion(TipoAlerta tipo, Incidencia incidencia) {
        switch (tipo) {
            case REUNION_EMERGENCIA:
                return new ReunionEmergencia();
            case PROTESTA:
                return new Protesta("Frente a empresa " + "TechCorp", 50);
            case ACCION_LEGAL:
                return new AccionLegal("Demanda por acoso laboral");
            default:
                return new ReunionEmergencia();
        }
    }
}

// CLASE PRINCIPAL CON CASOS DE USO
public class MainSistemaIncidencias {

    public static void main(String[] args) {
        System.out.println("=".repeat(80));
        System.out.println("🏭 SISTEMA DE GESTIÓN DE INCIDENCIAS LABORALES");
        System.out.println("=".repeat(80));

        // Inicializar sistema
        SistemaNotificaciones sistemaNotificaciones = new SistemaNotificaciones();
        GestorCasos gestor = new GestorCasos(sistemaNotificaciones);

        // Crear usuarios
        Trabajador juan = new Trabajador("Juan Pérez", "juan@email.com");
        Trabajador maria = new Trabajador("María González", "maria@email.com");
        RepresentanteSindical carlos = new RepresentanteSindical("Carlos Rodríguez", "carlos@sindicato.org");
        RepresentanteSindical ana = new RepresentanteSindical("Ana López", "ana@sindicato.org");

        // Crear empresa
        Empresa techCorp = new Empresa("TechCorp S.A.", "20-12345678-9");

        // Suscribir usuarios al sistema de notificaciones
        sistemaNotificaciones.addSuscriptor(juan);
        sistemaNotificaciones.addSuscriptor(maria);
        sistemaNotificaciones.addSuscriptor(carlos);
        sistemaNotificaciones.addSuscriptor(ana);

        System.out.println("\n" + "=".repeat(60));
        System.out.println("📋 CASO DE USO 1: INCIDENTE LEVE - VIOLENCIA VERBAL");
        System.out.println("=".repeat(60));

        // Caso 1: Incidente leve
        Incidencia incidencia1 = juan.crearDenuncia(
                "El supervisor me gritó delante de otros compañeros y me trató de incompetente",
                TipoViolencia.VERBAL,
                GravedadIncidente.LEVE
        );

        gestor.manejarCaso(incidencia1);

        // Empresa responde de manera adecuada
        RespuestaEmpresa respuesta1 = techCorp.responderIncidencia(incidencia1,
                "Hemos hablado con el supervisor y se ha comprometido a disculparse. " +
                        "Implementaremos un programa de capacitación en comunicación efectiva para todo el personal de supervisión.");

        gestor.procesarRespuestaEmpresa(incidencia1, respuesta1, carlos);

        System.out.println("\n" + "=".repeat(60));
        System.out.println("📋 CASO DE USO 2: INCIDENTE GRAVE - VIOLENCIA FÍSICA");
        System.out.println("=".repeat(60));

        // Caso 2: Incidente grave
        Incidencia incidencia2 = maria.crearDenuncia(
                "Un compañero me empujó violentamente durante una discusión, causándome lesiones en el brazo",
                TipoViolencia.FISICA,
                GravedadIncidente.GRAVE
        );

        gestor.manejarCaso(incidencia2);

        // Crear y ejecutar acción sindical
        AccionSindical protesta = AccionFactory.crearAccion(TipoAlerta.PROTESTA, incidencia2);
        protesta.ejecutar();

        // Empresa responde de manera inadecuada
        RespuestaEmpresa respuesta2 = techCorp.responderIncidencia(incidencia2,
                "Rechazamos la denuncia");

        gestor.procesarRespuestaEmpresa(incidencia2, respuesta2, ana);

        System.out.println("\n" + "=".repeat(60));
        System.out.println("📋 CASO DE USO 3: INCIDENTE CRÍTICO - ACOSO PSICOLÓGICO");
        System.out.println("=".repeat(60));

        // Caso 3: Incidente crítico
        Incidencia incidencia3 = juan.crearDenuncia(
                "Durante meses he sufrido acoso psicológico sistemático por parte de mi jefe inmediato. " +
                        "Me asigna tareas imposibles, me humilla públicamente y amenaza constantemente con despedirme",
                TipoViolencia.PSICOLOGICA,
                GravedadIncidente.CRITICA
        );

        gestor.manejarCaso(incidencia3);

        // Crear y ejecutar acción legal
        AccionSindical accionLegal = AccionFactory.crearAccion(TipoAlerta.ACCION_LEGAL, incidencia3);
        accionLegal.ejecutar();

        System.out.println("\n" + "=".repeat(60));
        System.out.println("📊 DEMOSTRACIÓN DE CAMBIOS DE ESTADO");
        System.out.println("=".repeat(60));

        // Demostrar transiciones de estado
        Incidencia incidencia4 = maria.crearDenuncia(
                "Discriminación por género en asignación de proyectos",
                TipoViolencia.PSICOLOGICA,
                GravedadIncidente.MODERADA
        );

        System.out.println("Estado inicial: " + incidencia4.getEstado().getNombre());

        incidencia4.cambiarEstado(new EstadoEnProceso());
        incidencia4.cambiarEstado(new EstadoEscalado());
        incidencia4.cambiarEstado(new EstadoResuelto());

        // Intentar cambio inválido
        incidencia4.cambiarEstado(new EstadoPendiente());

        System.out.println("\n" + "=".repeat(60));
        System.out.println("📈 RESUMEN DEL SISTEMA");
        System.out.println("=".repeat(60));

        System.out.println("✅ Patrones implementados:");
        System.out.println("   • Observer: Sistema de notificaciones");
        System.out.println("   • Strategy: Manejo dinámico de casos según gravedad");
        System.out.println("   • State: Estados de incidencia con transiciones validadas");
        System.out.println("   • Factory: Creación de acciones sindicales");

        System.out.println("\n✅ Casos de uso cubiertos:");
        System.out.println("   • Notificaciones automáticas a trabajadores y representantes");
        System.out.println("   • Manejo diferenciado según tipo y gravedad de violencia");
        System.out.println("   • Alertas de acción sindical para casos graves");
        System.out.println("   • Seguimiento del progreso de casos");
        System.out.println("   • Respuesta dinámica según comportamiento de la empresa");

        System.out.println("\n" + "=".repeat(80));
        System.out.println("🎯 SISTEMA FUNCIONANDO CORRECTAMENTE");
        System.out.println("=".repeat(80));
    }
}