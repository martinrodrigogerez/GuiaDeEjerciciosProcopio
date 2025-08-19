import java.util.Comparator;
import java.util.List;

public class PopularidadStrategy implements RecomendacionStrategy {
    @Override
    public ContenidoMultimedia recomendar(List<ContenidoMultimedia> contenidos) {
        System.out.println("🔎 Recomendando por popularidad...");
        return contenidos.stream()
                .max(Comparator.comparingInt(ContenidoMultimedia::getLikes))
                .orElse(null);
    }
}
