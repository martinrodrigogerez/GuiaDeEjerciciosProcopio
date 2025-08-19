import java.util.*;

// Interfaz base para todos los postres
interface Postre {
    void preparar();
}

// Productos abstractos - Interfaces especializadas
interface PostreRefrescante extends Postre {
}

interface PostreCaliente extends Postre {
}

// Productos concretos para VERANO (solo refrescantes)
class Helado implements PostreRefrescante {
    @Override
    public void preparar() {
        System.out.println("🍦 Preparando helado: mezclando ingredientes, congelando y decorando con frutas frescas");
    }
}

class TartaFria implements PostreRefrescante {
    @Override
    public void preparar() {
        System.out.println("🍰 Preparando tarta fría: base de galleta, relleno cremoso y refrigeración por 4 horas");
    }
}

// Productos concretos para INVIERNO (solo calientes)
class Brownie implements PostreCaliente {
    @Override
    public void preparar() {
        System.out.println("🍫 Preparando brownie: horneando chocolate fundido a 180°C por 25 minutos");
    }
}

class TartaDeManzana implements PostreCaliente {
    @Override
    public void preparar() {
        System.out.println("🥧 Preparando tarta de manzana: masa casera, manzanas caramelizadas y horneado a 200°C");
    }
}

// Factory abstracta
abstract class PostreFactory {
    public abstract List<Postre> crearMenuTemporada();

    // Método de conveniencia para preparar menú completo
    public void prepararMenuCompleto() {
        System.out.println("\n=== Preparando menú de temporada ===");
        List<Postre> menu = crearMenuTemporada();

        for (Postre postre : menu) {
            postre.preparar();
        }
        System.out.println("¡Menú listo para servir!\n");
    }
}

// Factory concreta para VERANO - Solo crea postres refrescantes
class PostreVeranoFactory extends PostreFactory {
    @Override
    public List<Postre> crearMenuTemporada() {
        List<Postre> menuVerano = new ArrayList<>();
        menuVerano.add(new Helado());
        menuVerano.add(new TartaFria());
        return menuVerano;
    }

    // Métodos específicos para postres de verano
    public PostreRefrescante crearHelado() {
        return new Helado();
    }

    public PostreRefrescante crearTartaFria() {
        return new TartaFria();
    }
}

// Factory concreta para INVIERNO - Solo crea postres calientes
class PostreInviernoFactory extends PostreFactory {
    @Override
    public List<Postre> crearMenuTemporada() {
        List<Postre> menuInvierno = new ArrayList<>();
        menuInvierno.add(new Brownie());
        menuInvierno.add(new TartaDeManzana());
        return menuInvierno;
    }

    // Métodos específicos para postres de invierno
    public PostreCaliente crearBrownie() {
        return new Brownie();
    }

    public PostreCaliente crearTartaDeManzana() {
        return new TartaDeManzana();
    }
}

// Context/Cliente que usa las factories
class Reposteria {
    private PostreFactory factory;

    public void establecerTemporada(PostreFactory factory) {
        this.factory = factory;
        System.out.println("🏪 Temporada actualizada en la repostería");
    }

    public void prepararMenuDelDia() {
        if (factory == null) {
            System.out.println("⚠️ Primero debe establecerse la temporada");
            return;
        }
        factory.prepararMenuCompleto();
    }

    public List<Postre> obtenerMenuTemporada() {
        if (factory == null) {
            System.out.println("⚠️ Primero debe establecerse la temporada");
            return new ArrayList<>();
        }
        return factory.crearMenuTemporada();
    }

    // Métodos específicos según el tipo de factory
    public PostreRefrescante pedirHelado() {
        if (factory instanceof PostreVeranoFactory) {
            return ((PostreVeranoFactory) factory).crearHelado();
        }
        throw new UnsupportedOperationException("Helado no disponible en esta temporada");
    }

    public PostreRefrescante pedirTartaFria() {
        if (factory instanceof PostreVeranoFactory) {
            return ((PostreVeranoFactory) factory).crearTartaFria();
        }
        throw new UnsupportedOperationException("Tarta fría no disponible en esta temporada");
    }

    public PostreCaliente pedirBrownie() {
        if (factory instanceof PostreInviernoFactory) {
            return ((PostreInviernoFactory) factory).crearBrownie();
        }
        throw new UnsupportedOperationException("Brownie no disponible en esta temporada");
    }

    public PostreCaliente pedirTartaDeManzana() {
        if (factory instanceof PostreInviernoFactory) {
            return ((PostreInviernoFactory) factory).crearTartaDeManzana();
        }
        throw new UnsupportedOperationException("Tarta de manzana no disponible en esta temporada");
    }
}

// Programa principal - Demostración del patrón
public class SistemaPostres {
    public static void main(String[] args) {
        Reposteria reposteria = new Reposteria();

        System.out.println("🌟 Sistema de Postres Personalizados 🌟");

        // Temporada de VERANO
        System.out.println("\n☀️ CAMBIO A TEMPORADA DE VERANO");
        reposteria.establecerTemporada(new PostreVeranoFactory());
        reposteria.prepararMenuDelDia();

        // Pedidos específicos de verano
        System.out.println("\n--- Pedidos individuales de verano ---");
        try {
            PostreRefrescante helado = reposteria.pedirHelado();
            helado.preparar();

            PostreRefrescante tartaFria = reposteria.pedirTartaFria();
            tartaFria.preparar();
        } catch (UnsupportedOperationException e) {
            System.out.println("❌ " + e.getMessage());
        }

        // Temporada de INVIERNO
        System.out.println("\n❄️ CAMBIO A TEMPORADA DE INVIERNO");
        reposteria.establecerTemporada(new PostreInviernoFactory());
        reposteria.prepararMenuDelDia();

        // Pedidos específicos de invierno
        System.out.println("\n--- Pedidos individuales de invierno ---");
        try {
            PostreCaliente brownie = reposteria.pedirBrownie();
            brownie.preparar();

            PostreCaliente tartaManzana = reposteria.pedirTartaDeManzana();
            tartaManzana.preparar();
        } catch (UnsupportedOperationException e) {
            System.out.println("❌ " + e.getMessage());
        }

        // Intento de pedir helado en invierno (debe fallar)
        System.out.println("\n--- Prueba de restricción por temporada ---");
        try {
            PostreRefrescante helado = reposteria.pedirHelado();
            helado.preparar();
        } catch (UnsupportedOperationException e) {
            System.out.println("❌ " + e.getMessage());
        }

        System.out.println("\n✨ Cada factory ahora maneja SOLO su familia de productos:");
        System.out.println("   - PostreVeranoFactory: SOLO postres refrescantes (Helado, Tarta Fría)");
        System.out.println("   - PostreInviernoFactory: SOLO postres calientes (Brownie, Tarta Manzana)");
        System.out.println("   - Sin responsabilidades cruzadas 🎯");
        System.out.println("   - Fácil extensión: agregar PostrePrimaveraFactory, PostreOtoñoFactory...");
    }
}