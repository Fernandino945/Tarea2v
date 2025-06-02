package backend;

public enum TaxonomiaBloom {
    RECORDAR("Recordar"),
    COMPRENDER("Comprender"),
    APLICAR("Aplicar"),
    ANALIZAR("Analizar"),
    EVALUAR("Evaluar"),
    CREAR("Crear");

    private String nombre;

    private TaxonomiaBloom(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
}