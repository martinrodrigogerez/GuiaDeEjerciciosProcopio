import java.util.ArrayList;
import java.util.List;

public class ContenidoMultimedia {
    private TipoContenido tipoContenido;
    private int likes = 0;
    private int visualizaciones = 0;
    private List<String> comentarios = new ArrayList<>();

    public ContenidoMultimedia(TipoContenido tipoContenido) {
        this.tipoContenido = tipoContenido;
    }

    public void agregarComentario(String comentario) {
        comentarios.add(comentario);
    }

    public void incrementarLikes() {
        likes++;
    }

    public void incrementarVisualizaciones() {
        visualizaciones++;
    }

    public TipoContenido getTipoContenido() {
        return tipoContenido;
    }

    public int getLikes() { return likes; }
    public int getVisualizaciones() { return visualizaciones; }
    public List<String> getComentarios() { return comentarios; }
}
