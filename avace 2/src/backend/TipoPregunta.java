package backend;

public enum TipoPregunta {
    RESPUESTA_CORTA("Respuesta corta"),
    TEXTO_INCOMPLETO("Texto incompleto"),
    EMPAREJAMIENTO("Emparejamiento"),
    OPCION_MULTIPLE("Opción múltiple"),
    VERDADERO_FALSO("Verdadero/Falso"),
    ANALOGIAS("Analogías/Diferencias"),
    INTERPRETACION_GRAFICOS("Interpretación de gráficos");

    private String nombre;

    private TipoPregunta(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
}