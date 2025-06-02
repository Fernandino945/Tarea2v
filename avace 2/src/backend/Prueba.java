package backend;

import java.util.List;

public class Prueba { // Atributos privados de la clase
    private List<Pregunta> preguntas;
    private int tiempoTotal;

    public Prueba(List<Pregunta> preguntas) {//constructor principal
        this.preguntas = preguntas;
        this.tiempoTotal = preguntas.size() * 2; // 2 minutos por pregunta
    }

    // Métodos getter para acceder a los atributos de cada clase
    public List<Pregunta> getPreguntas() {
        return preguntas;
    }

    public int getTiempoTotal() {
        return tiempoTotal;
    }

    public int getNumeroPreguntas() {
        return preguntas.size();
    }

    public int getRespuestasCorrectas() {
        int correctas = 0;
        for (Pregunta p : preguntas) {
            if (p.esCorrecta()) correctas++;
        }
        return correctas;
    }
}