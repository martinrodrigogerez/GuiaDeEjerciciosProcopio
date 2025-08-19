// ===== INTERFACES ABSTRACTAS =====

// Abstract Product - Mesa
abstract class Mesa {
    public abstract String fabricar();
    public abstract String obtenerDescripcion();
}

// Abstract Product - Silla
abstract class Silla {
    public abstract String fabricar();
    public abstract String obtenerDescripcion();
}

// Abstract Factory
abstract class FabricaMuebles {
    public abstract Mesa crearMesa();
    public abstract Silla crearSilla();
}

// ===== PRODUCTOS CONCRETOS - MADERA =====

class MesaMadera extends Mesa {
    @Override
    public String fabricar() {
        return "Fabricando mesa de madera con acabado natural";
    }

    @Override
    public String obtenerDescripcion() {
        return "Mesa de Madera - Roble macizo con barniz mate, diseño rústico elegante";
    }
}

class SillaMadera extends Silla {
    @Override
    public String fabricar() {
        return "Fabricando silla de madera con tapizado de cuero";
    }

    @Override
    public String obtenerDescripcion() {
        return "Silla de Madera - Roble con respaldo ergonómico y cojín de cuero marrón";
    }
}

// ===== PRODUCTOS CONCRETOS - METAL =====

class MesaMetal extends Mesa {
    @Override
    public String fabricar() {
        return "Fabricando mesa de metal con acabado cromado";
    }

    @Override
    public String obtenerDescripcion() {
        return "Mesa de Metal - Acero inoxidable con superficie de vidrio templado, estilo moderno";
    }
}

class SillaMetal extends Silla {
    @Override
    public String fabricar() {
        return "Fabricando silla de metal con diseño minimalista";
    }

    @Override
    public String obtenerDescripcion() {
        return "Silla de Metal - Aluminio anodizado con asiento acolchado, diseño industrial";
    }
}

// ===== FÁBRICAS CONCRETAS =====

class FabricaMueblesMadera extends FabricaMuebles {
    @Override
    public Mesa crearMesa() {
        return new MesaMadera();
    }

    @Override
    public Silla crearSilla() {
        return new SillaMadera();
    }
}

class FabricaMueblesMetal extends FabricaMuebles {
    @Override
    public Mesa crearMesa() {
        return new MesaMetal();
    }

    @Override
    public Silla crearSilla() {
        return new SillaMetal();
    }
}

// ===== CLIENTE =====

class MueblesEcommerce {
    private FabricaMuebles fabrica;
    private Mesa mesa;
    private Silla silla;

    public MueblesEcommerce(FabricaMuebles fabrica) {
        this.fabrica = fabrica;
    }

    public void fabricarConjuntoMuebles() {
        System.out.println("🏭 Iniciando proceso de fabricación...\n");

        // Crear productos usando la factory
        mesa = fabrica.crearMesa();
        silla = fabrica.crearSilla();

        // Fabricar productos
        System.out.println("📋 Proceso de fabricación:");
        System.out.println("  → " + mesa.fabricar());
        System.out.println("  → " + silla.fabricar());
        System.out.println();
    }

    public void mostrarDescripciones() {
        System.out.println("📝 Descripción de productos fabricados:");
        System.out.println("  🪑 " + silla.obtenerDescripcion());
        System.out.println("  🍽️  " + mesa.obtenerDescripcion());
        System.out.println();
    }

    public void mostrarResumen() {
        String tipoMaterial = fabrica.getClass().getSimpleName().contains("Madera") ? "MADERA" : "METAL";
        System.out.println("✅ Conjunto de muebles de " + tipoMaterial + " fabricado exitosamente!");
        System.out.println("   Coherencia de estilo garantizada ✨");
        System.out.println("" + "=".repeat(60));
    }
}

// ===== MAIN DE PRUEBA =====

public class SistemaMueblesMain {
    public static void main(String[] args) {
        System.out.println("🏠 SISTEMA DE PERSONALIZACIÓN DE MUEBLES");
        System.out.println("" + "=".repeat(60));
        System.out.println();

        // ===== ESCENARIO 1: Cliente elige muebles de MADERA =====
        System.out.println("🌳 ESCENARIO 1: Cliente selecciona muebles de MADERA");
        System.out.println("-".repeat(50));

        FabricaMuebles fabricaMadera = new FabricaMueblesMadera();
        MueblesEcommerce tiendaMadera = new MueblesEcommerce(fabricaMadera);

        tiendaMadera.fabricarConjuntoMuebles();
        tiendaMadera.mostrarDescripciones();
        tiendaMadera.mostrarResumen();
        System.out.println();

        // ===== ESCENARIO 2: Cliente elige muebles de METAL =====
        System.out.println("⚙️ ESCENARIO 2: Cliente selecciona muebles de METAL");
        System.out.println("-".repeat(50));

        FabricaMuebles fabricaMetal = new FabricaMueblesMetal();
        MueblesEcommerce tiendaMetal = new MueblesEcommerce(fabricaMetal);

        tiendaMetal.fabricarConjuntoMuebles();
        tiendaMetal.mostrarDescripciones();
        tiendaMetal.mostrarResumen();
        System.out.println();

        // ===== DEMOSTRACIÓN DE EXTENSIBILIDAD =====
        System.out.println("🔮 DEMOSTRACIÓN DE EXTENSIBILIDAD");
        System.out.println("-".repeat(50));
        demonstrarExtensibilidad();
    }

    // Método para demostrar cómo sería agregar un nuevo material
    public static void demonstrarExtensibilidad() {
        System.out.println("💡 Para agregar muebles de VIDRIO al sistema:");
        System.out.println("   1. Crear clase: FabricaMueblesVidrio extends FabricaMuebles");
        System.out.println("   2. Crear clase: MesaVidrio extends Mesa");
        System.out.println("   3. Crear clase: SillaVidrio extends Silla");
        System.out.println("   4. ¡Sin modificar código existente!");
        System.out.println();
        System.out.println("✨ El patrón Abstract Factory garantiza:");
        System.out.println("   • Coherencia entre productos de la misma familia");
        System.out.println("   • Fácil extensión para nuevos materiales");
        System.out.println("   • Separación entre creación y uso de objetos");
        System.out.println("   • Cliente desacoplado de clases concretas");
    }
}