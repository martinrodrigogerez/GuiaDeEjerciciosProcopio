// Publicador.java
public interface Publicador {
    void suscribir(Suscriptor usuario);
    void desuscribir(Suscriptor usuario);
    void notificar(String mensaje);
}
