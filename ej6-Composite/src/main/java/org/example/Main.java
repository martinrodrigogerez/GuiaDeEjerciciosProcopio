package org.example;

public class Main {
    public static void main(String[] args) {
        // Crear archivos
        Archivo archivo1 = new Archivo("documento.txt");
        Archivo archivo2 = new Archivo("foto.jpg");
        Archivo archivo3 = new Archivo("presentacion.pptx");

        // Crear carpetas
        Carpeta carpetaTrabajo = new Carpeta("Trabajo");
        Carpeta carpetaPersonal = new Carpeta("Personal");
        Carpeta carpetaRaiz = new Carpeta("Disco");

        // Armar estructura
        carpetaTrabajo.agregar(archivo1);
        carpetaPersonal.agregar(archivo2);
        carpetaPersonal.agregar(archivo3);

        carpetaRaiz.agregar(carpetaTrabajo);
        carpetaRaiz.agregar(carpetaPersonal);

        // Mostrar estructura completa
        carpetaRaiz.mostrar("");
    }
}
