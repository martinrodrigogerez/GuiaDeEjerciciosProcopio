# Primer Parcial de Diseño de Sistemas

**Nombre:** _________________ **Legajo:** _________  
**Fecha:** 26-8-2024

---

## Ejercicio 1

### Contexto:

Un sindicato de trabajadores está desarrollando un sistema para gestionar y analizar denuncias sobre incidentes de violencia laboral en distintas empresas. El objetivo del sistema es registrar las denuncias realizadas por los trabajadores y permitir que los representantes sindicales sigan el progreso de cada caso, brindando apoyo y ofreciendo alertas cuando sea necesario tomar medidas, como reuniones de mediación o protestas.

El sistema debe ser capaz de gestionar dos tipos de notificaciones:
1. Notificaciones de seguimiento de casos, que informan a los trabajadores y representantes sindicales sobre el estado de las denuncias presentadas.
2. Alertas de acción sindical, que notifica a los representantes sindicales y a los trabajadores cuando es necesario intervenir, como convocar reuniones de emergencia, realizar protestas o coordinar negociaciones con la empresa.

El sistema debe ser flexible y permitir la implementación de diferentes métodos de manejo de casos basadas en la gravedad del incidente, el tipo de violencia (verbal, física, psicológica) y la respuesta de la empresa.

### Requisitos:

1. **Usuarios y notificaciones:**
    - El sistema debe permitir que los trabajadores y los representantes sindicales se suscriban a notificaciones sobre el progreso de los casos.
    - Cada vez que se actualice un caso (nueva evidencia, reuniones programadas, cambios en la negociación), los usuarios suscritos deben ser notificados automáticamente.
    - Además, los representantes sindicales deben poder recibir alertas de acción sindical cuando es necesario movilizar recursos o tomar medidas urgentes.

2. **Métodos de manejo de casos:**
    - El sistema debe permitir cambiar dinámicamente la estrategia de manejo de un caso. Por ejemplo, un incidente de violencia verbal puede requerir una de gestión distinta de acuerdo un caso de acoso físico o psicológico prolongado.
    - Implementar al menos dos estrategias de manejo: una para incidentes leves (que prioricen la mediación rápida) y otra para incidentes graves (que requieran intervención legal y medidas disciplinarias).

### Consignas:

1. Modelar las clases necesarias para representar a los Usuarios, el Sistema de notificaciones y el Manejo de casos.
2. Utilizar los patrones de diseño que considere adecuados.

---

```mermaid

classDiagram
    

    class Trabajador{
        -nombre: String 
        -denuncias:List~Incidente~
        Trabajador(String nombre)
        
        +denunciar(Incidente nuevoIncidente)
     }
 

    class Incidente{
        -fechayHoraIncidente:LocalDateTime
        -tipoDeViolencia:TipoDeViolencia
        -estado:Estado
        -respuestaDeLaEmpresa: List ~String~
        -listaSuscriptores: List ~Suscriptor~
        %% por defecto el estado es 
        Incidente(String descripcion, TipoDeViolencia tipo, Estado estado)
        cambiar(Estado estado)
    }
    %%  Estado o Progreso
    
    class Sindical{
        -accion:AccionStrategy
        -nombre:String
        Sindical(String nombre)
        analizarRespuesta(Incidente i) Medida
        setMedidaDeFuerza(Medida m)
        cambiarEstadoIncidente(Incidente incidente)
        ejecutar(Medida m)

    }
    
    class EnProgreso{
        cambiar(Estado estado)

    }
    
    class SinRespuesta{
        cambiar(Estado estado)

    }
    
    class ConRespuesta{
        cambiar(Estado estado)

    }
    
    class Resuelto{
        cambiar(Estado estado)

    }
    
    class Estado{
        <<interface>>
        cambiar(Estado estado)
    }
    
    class Suscriptor{
        recibirNotificacion(Incidente incidente)
    }
    
    
    
    
    Sindical --> Incidente: analizar
    Sindical --|> Suscriptor:recibir notificacion
    Incidente ..|> Publicador:comunica eventos
    Incidente --> Estado: control de estado
    Estado <|..ConRespuesta:ejecutar
    Estado <|..EnProgreso: progreso
    Estado <|..SinRespuesta: no respondido
    Estado <|..Resuelto: resuelto
    Incidente <--o Trabajador
    AccionStrategy <|.. Sindical 
    ReunionDeMediacion ..|>AccionStrategy
    NegociarConLaEmpresa ..|>AccionStrategy
    Protesta ..|>AccionStrategy
    Incidente --> TipoDeViolencia
    Medida --> Gravedad
    Empresa ..> Incidente:responder
    Medida <|-- ReunionDeMediacion
    Medida <|-- NegociarConLaEmpresa
    Medida <|-- Protesta

    class AccionStrategy{
        <<interface>>
        ejecutar(Medida)
    }
    
    class TipoDeViolencia{
        <<enumeration>>
        VERBAL
        FISICA
        PSICOLOGICA
    }
    
    class Empresa{
        -descripcion: String
        Empresa(String descripcion)
        responderIncidente(Incidente incidente)
    }


    class Notificacion{
        tipo:TipoNotificacion
    }
    
    class TipoNotificacion{
        <<enumeration>>
        SEGUIMIENTO
        ALERTA
    }
    
    class Medida{
        <<abstract>>
        -fechaInicio:LocalDate
        -descripcion:String
        -gravedad:Gravedad
        +setGravedad(Gravedad gravedadIncidente)
    }
    
    class Publicador{
        suscribir(Suscriptor suscriptor)
        desuscribir(Suscriptor suscriptor)
        notificar()
    }
    
    class Gravedad{
        <<enumeration>>
        LEVE
        MEDIA
        GRAVE
    }
    
    class ReunionDeMediacion{
        ejecutar(Medida medidaDeFuerza)

    }
    
    class NegociarConLaEmpresa{
        ejecutar(Medida medidaDeFuerza)

    }
    
    class Protesta{
        ejecutar(Medida medidaDeFuerza)

    }

    
    
```

## Ejercicio 2

Mencione tres atributos de calidad que considere los más importantes, basándose en la norma ISO/IEC 25010 (que reemplaza a la norma ISO/IEC 9126). Justifique su elección en el contexto del sistema planteado en el Ejercicio 1. Justifique la elección de cada atributo.