import java.time.LocalDateTime;
import java.util.*;

// ===== OBSERVER =====
interface Suscriptor {
    void recibirNotificacion(OrdenBicicleta orden, String mensaje);
}

interface Publicador {
    void suscribir(Suscriptor s);
    void desuscribir(Suscriptor s);
    void notificar(String mensaje);
}

// ===== NOTIFICACIONES (Strategy) =====
interface PreferenciaNotificacion {
    void enviarMensaje(String mensaje);
}

class NotificadorMail implements PreferenciaNotificacion {
    private String email;

    public NotificadorMail(String email) {
        this.email = email;
    }

    @Override
    public void enviarMensaje(String mensaje) {
        System.out.println("📧 Email a " + email + ": " + mensaje);
    }
}

class NotificadorSMS implements PreferenciaNotificacion {
    private String telefono;

    public NotificadorSMS(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public void enviarMensaje(String mensaje) {
        System.out.println("📱 SMS a " + telefono + ": " + mensaje);
    }
}

// ===== CLIENTE =====
class Cliente implements Suscriptor {
    private int id;
    private String nombre;
    private String email;
    private String telefono;
    private List<PreferenciaNotificacion> preferenciasNotificacion = new ArrayList<>();

    public Cliente(int id, String nombre, String email, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
    }

    public void agregarNotificacion(PreferenciaNotificacion notificador) {
        preferenciasNotificacion.add(notificador);
    }

    public OrdenBicicleta comprar(OrdenBicicleta orden) {
        System.out.println(nombre + " realizó una compra.");
        return orden;
    }

    @Override
    public void recibirNotificacion(OrdenBicicleta orden, String mensaje) {
        for (PreferenciaNotificacion p : preferenciasNotificacion) {
            p.enviarMensaje("Orden " + orden.getId() + ": " + mensaje);
        }
    }
}

// ===== STATE =====
interface Estado {
    void cambiarEstado(OrdenBicicleta orden);
}

class OrdenConfirmada implements Estado {
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Override
    public void cambiarEstado(OrdenBicicleta orden) {
        orden.setEstado(new InicioDeArmado());
        orden.notificar("La orden fue confirmada el " + fechaCreacion);
    }
}

class InicioDeArmado implements Estado {
    private LocalDateTime fechaInicio = LocalDateTime.now();

    @Override
    public void cambiarEstado(OrdenBicicleta orden) {
        orden.setEstado(new Finalizado());
        orden.notificar("Se inició el armado el " + fechaInicio);
    }
}

class Finalizado implements Estado {
    private LocalDateTime fechaFinalizacion = LocalDateTime.now();

    @Override
    public void cambiarEstado(OrdenBicicleta orden) {
        orden.setEstado(new DisponibleParaEntregar());
        orden.notificar("La bicicleta fue finalizada el " + fechaFinalizacion);
    }
}

class DisponibleParaEntregar implements Estado {
    private LocalDateTime fechaEntrega = LocalDateTime.now();

    @Override
    public void cambiarEstado(OrdenBicicleta orden) {
        orden.notificar("La bicicleta está disponible para retirar desde " + fechaEntrega);
    }
}

// ===== ORDEN (Observer + State) =====
class OrdenBicicleta implements Publicador {
    private static int contador = 1;
    private int id;
    private Estado estado;
    private Cliente cliente;
    private List<Suscriptor> suscriptores = new ArrayList<>();

    public OrdenBicicleta(Cliente cliente) {
        this.id = contador++;
        this.cliente = cliente;
        this.estado = new OrdenConfirmada(); // primer estado
    }

    public int getId() { return id; }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public void avanzarEstado() {
        estado.cambiarEstado(this);
    }

    @Override
    public void suscribir(Suscriptor s) {
        suscriptores.add(s);
    }

    @Override
    public void desuscribir(Suscriptor s) {
        suscriptores.remove(s);
    }

    @Override
    public void notificar(String mensaje) {
        for (Suscriptor s : suscriptores) {
            s.recibirNotificacion(this, mensaje);
        }
    }
}

// ===== STRATEGY PARA SELECCIÓN DE COMPONENTES =====
abstract class Componente {
    String marca;
    int precio;

    public Componente(String marca, int precio) {
        this.marca = marca;
        this.precio = precio;
    }

    public int getPrecio() { return precio; }
    public String getMarca() { return marca; }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " - " + marca + " $" + precio;
    }
}

class Rueda extends Componente {
    public Rueda(String marca, int precio) { super(marca, precio); }
}

class Freno extends Componente {
    public Freno(String marca, int precio) { super(marca, precio); }
}

interface SeleccionStrategy {
    Componente seleccionar(List<Componente> componentes);
}

class SeleccionPorPrecioStrategy implements SeleccionStrategy {
    @Override
    public Componente seleccionar(List<Componente> componentes) {
        return componentes.stream().min(Comparator.comparingInt(Componente::getPrecio)).orElse(null);
    }
}

class SeleccionPorMarcaStrategy implements SeleccionStrategy {
    private String marca;

    public SeleccionPorMarcaStrategy(String marca) {
        this.marca = marca;
    }

    @Override
    public Componente seleccionar(List<Componente> componentes) {
        return componentes.stream().filter(c -> c.getMarca().equals(marca)).findFirst().orElse(null);
    }
}

// ===== BUILDER =====
interface Builder {
    void seleccionarRueda(Rueda rueda);
    void seleccionarFreno(Freno freno);
    OrdenBicicleta confirmarOrden(Cliente cliente);
}

class BicicletaBuilder implements Builder {
    private Rueda rueda;
    private Freno freno;

    @Override
    public void seleccionarRueda(Rueda rueda) {
        this.rueda = rueda;
    }

    @Override
    public void seleccionarFreno(Freno freno) {
        this.freno = freno;
    }

    @Override
    public OrdenBicicleta confirmarOrden(Cliente cliente) {
        System.out.println("✅ Bicicleta personalizada con: " + rueda + " y " + freno);
        return new OrdenBicicleta(cliente);
    }
}

// ===== MAIN DEMO =====
public class Main {
    public static void main(String[] args) {
        // Cliente
        Cliente c1 = new Cliente(1, "Martín", "martin@mail.com", "11223344");
        c1.agregarNotificacion(new NotificadorMail("martin@mail.com"));
        c1.agregarNotificacion(new NotificadorSMS("11223344"));

        // Componentes disponibles
        List<Componente> ruedas = List.of(new Rueda("Shimano", 500), new Rueda("Genérica", 300));
        List<Componente> frenos = List.of(new Freno("Shimano", 700), new Freno("Genérico", 400));

        // Estrategia de selección
        SeleccionStrategy estrategia = new SeleccionPorPrecioStrategy();
        Rueda ruedaElegida = (Rueda) estrategia.seleccionar(ruedas);
        Freno frenoElegido = (Freno) estrategia.seleccionar(frenos);

        // Construcción con Builder
        BicicletaBuilder builder = new BicicletaBuilder();
        builder.seleccionarRueda(ruedaElegida);
        builder.seleccionarFreno(frenoElegido);
        OrdenBicicleta orden = builder.confirmarOrden(c1);

        // Cliente compra la orden
        c1.comprar(orden);
        orden.suscribir(c1);

        // Flujo de estados
        orden.avanzarEstado(); // Orden confirmada
        orden.avanzarEstado(); // Inicio de armado
        orden.avanzarEstado(); // Finalizado
        orden.avanzarEstado(); // Disponible para entregar
    }
}
