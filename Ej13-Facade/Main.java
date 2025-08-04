public class Main {
    public static void main(String[] args) {
        CineFacade cine = new CineFacade();

        System.out.println("=== INICIANDO SESIÓN DE CINE ===");
        cine.encenderCine();

        System.out.println("\n=== FIN DE LA SESIÓN ===");
        cine.apagarCine();
    }
}
