public class CineFacade {
    private SistemaDeSonido sonido;
    private Proyector proyector;
    private Luces luces;
    private Pantalla pantalla;

    public CineFacade() {
        this.sonido = new SistemaDeSonido();
        this.proyector = new Proyector();
        this.luces = new Luces();
        this.pantalla = new Pantalla();
    }

    public void encenderCine() {
        System.out.println("Preparando el cine para la película...");
        luces.atenuar();
        pantalla.bajar();
        proyector.encender();
        proyector.seleccionarEntrada();
        sonido.encender();
        sonido.configurarSurround();
        System.out.println("Todo listo para disfrutar la película.");
    }

    public void apagarCine() {
        System.out.println("Finalizando la sesión de cine...");
        sonido.apagar();
        proyector.apagar();
        pantalla.subir();
        luces.encender();
        System.out.println("Cine apagado correctamente.");
    }
}
