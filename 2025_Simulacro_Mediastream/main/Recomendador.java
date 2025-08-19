import java.util.List;

public class Recomendador {
    private RecomendacionStrategy estrategia;

    public Recomendador(RecomendacionStrategy estrategia) {
        this.estrategia = estrategia;
    }

    public void setEstrategia(RecomendacionStrategy estrategia) {
        this.estrategia = estrategia;
    }

    public ContenidoMultimedia recomendar(List<ContenidoMultimedia> contenidos) {
        return estrategia.recomendar(contenidos);
    }
}
