import java.util.ArrayList;
import java.util.List;

// ====== Interfaces ======
interface Publicador {
    void suscribir(Suscriptor s);
    void desuscribir(Suscriptor s);
    void notificar(Medicion m);
}

interface Suscriptor {
    void recibirNotificacion(Medicion m);
}

// ====== Clases del dominio ======
class Medicion {
    private int valor;
    private Factor factor;

    public Medicion(Factor factor, int valor) {
        this.factor = factor;
        this.valor = valor;
    }

    public int getValor() {
        return valor;
    }

    public Factor getFactor() {
        return factor;
    }

    @Override
    public String toString() {
        return "Medición -> Factor: " + factor + ", valor: " + valor;
    }
}

enum Factor {
    TEMPERATURA,
    HUMEDAD,
    LUZ
}

class Sensor implements Publicador {
    private String descripcion;
    private Factor factor;
    private List<Suscriptor> suscriptores;

    public Sensor(String descripcion, Factor factor) {
        this.descripcion = descripcion;
        this.factor = factor;
        this.suscriptores = new ArrayList<>();
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
    public void notificar(Medicion m) {
        for (Suscriptor s : suscriptores) {
            s.recibirNotificacion(m);
        }
    }

    public void nuevaMedicion(int valor) {
        Medicion m = new Medicion(factor, valor);
        System.out.println("Sensor [" + descripcion + "] generó: " + m);
        notificar(m);
    }
}

// ====== Acciones concretas ======
class SistemaRiego {
    private String cultivoAsociado;

    public SistemaRiego(String cultivoAsociado) {
        this.cultivoAsociado = cultivoAsociado;
    }

    public void activar() {
        System.out.println(" -> Sistema de riego del cultivo [" + cultivoAsociado + "] ACTIVADO.");
    }
}

// ====== Suscriptores ======
class Cultivo implements Suscriptor {
    private String descripcion;
    private SistemaRiego riego;

    public Cultivo(String descripcion) {
        this.descripcion = descripcion;
        this.riego = new SistemaRiego(descripcion);
    }

    @Override
    public void recibirNotificacion(Medicion m) {
        System.out.println("Cultivo [" + descripcion + "] recibe notificación: " + m);
        if (m.getFactor() == Factor.HUMEDAD && m.getValor() < 30) {
            riego.activar();
        }
    }
}

class Administrador implements Suscriptor {
    private String nombre;

    public Administrador(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public void recibirNotificacion(Medicion m) {
        System.out.println("Administrador [" + nombre + "] supervisa la notificación: " + m);
        if (m.getFactor() == Factor.TEMPERATURA && m.getValor() > 35) {
            System.out.println(" -> ALERTA del Administrador [" + nombre + "]: Temperatura demasiado alta!");
        }
    }
}

// ====== Ejemplo de uso ======
public class Main {
    public static void main(String[] args) {
        Sensor sensorHumedad = new Sensor("Sensor de Humedad", Factor.HUMEDAD);

        Cultivo maiz = new Cultivo("Maíz");
        Cultivo soja = new Cultivo("Soja");
        Administrador admin = new Administrador("Carlos");

        sensorHumedad.suscribir(maiz);
        sensorHumedad.suscribir(soja);
        sensorHumedad.suscribir(admin);

        sensorHumedad.nuevaMedicion(40);  // normal
        sensorHumedad.nuevaMedicion(20);  // baja humedad -> activa riego
    }
}
