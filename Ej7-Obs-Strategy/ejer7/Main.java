public class Main {
    public static void main(String[] args) {

        // Se elige inicialmente el canal de envío (EMAIL)
        iCanalStrategy canalInicial = new MailSender();

        // Crear el aula virtual con el canal elegido
        AulaVirtual aula = new AulaVirtual(canalInicial);

        // Crear alumnos con su email y teléfono
        Alumno alumno1 = new Alumno("Ana", "ana@gmail.com", "+5491112345678");
        Alumno alumno2 = new Alumno("Bruno", "bruno@hotmail.com", "+5491123456789");

        // Registrar alumnos como suscriptores del aula
        aula.registrarSuscriptor(alumno1);
        aula.registrarSuscriptor(alumno2);

        // Subir un apunte de clase
        Contenido apunte = new ApunteDeClase("Unidad 1 - Introducción a Java");
        aula.addContenido(apunte);

        // Cambiar el canal de envío a WhatsApp
        aula.setCanal(new WhatsappSender());

        // Subir un comunicado
        Contenido comunicado = new Comunicado("Recordatorio: examen parcial la próxima semana");
        aula.addContenido(comunicado);

        // Eliminar un alumno
        aula.eliminarSuscriptor(alumno1);

        // Subir nuevo contenido (solo Bruno recibirá este)
        Contenido apunte2 = new ApunteDeClase("Unidad 2 - POO");
        aula.addContenido(apunte2);
    }
}
