package backend;

public class Pregunta { // Atributos privados de la clase
    private String texto;
    private TipoPregunta tipo;
    private TaxonomiaBloom nivelBloom;
    private String respuestaCorrecta;
    private String[] opciones;
    private String respuestaUsuario;
    private String enunciado;
    private Boolean bloom;

    public Pregunta(String texto, TipoPregunta tipo, TaxonomiaBloom nivelBloom,
                    String respuestaCorrecta, String[] opciones) { //constructor principal
        this.texto = texto;
        this.tipo = tipo;
        this.nivelBloom = nivelBloom;
        this.respuestaCorrecta = respuestaCorrecta;
        this.opciones = opciones;
    }
    // Métodos getter para acceder a los atributos de cada clase
    public String getTexto() {
        return texto;
    }

    public TipoPregunta getTipo() {
        return tipo;
    }

    public TaxonomiaBloom getNivelBloom() {
        return nivelBloom;
    }

    public String getRespuestaCorrecta() {
        return respuestaCorrecta;
    }

    public String[] getOpciones() {
        return opciones;
    }

    public String getRespuestaUsuario() {
        return respuestaUsuario;
    }

    public void setRespuestaUsuario(String respuesta) {
        this.respuestaUsuario = respuesta;
    }

    public boolean esCorrecta() {
        if (respuestaUsuario == null) return false;
        return respuestaUsuario.equals(respuestaCorrecta);
    }

    public String getEnunciado() {
        return enunciado;
    }

    public Boolean getBloom() {
        return bloom;
    }
}