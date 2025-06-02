package frontend;
import backend.Pregunta;
import backend.Prueba;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class PruebaForm { //atributos privados de la clase que tambien incluyen los de la ventana .form
    private JPanel panelPrincipal;
    private JLabel lblNumeroPregunta;
    private JLabel lblTipoPregunta;
    private JLabel lblNivelBloom;
    private JLabel lblPregunta;
    private JPanel panelRespuestas;
    private JButton btnAnterior;
    private JButton btnSiguiente;
    private JProgressBar progressBar;
    private JTextArea areaTextoUnico; // Se tomo la decision de hacer un unico atributos para las respuestas por escrito
    private JScrollPane scrollAreaTexto;
    private JPanel panelSuperior;
    private JPanel panelCentro;
    private JPanel panelInferior;
    private JRadioButton radioOpcion1;
    private JRadioButton radioOpcion2;
    private JRadioButton radioOpcion3;
    private JRadioButton radioOpcion4;
    private Prueba prueba;
    private int preguntaActual = 0;
    private ButtonGroup buttonGroup;

    public PruebaForm(Prueba prueba) {
        this.prueba = prueba;

        buttonGroup = new ButtonGroup();
        buttonGroup.add(radioOpcion1);
        buttonGroup.add(radioOpcion2);
        buttonGroup.add(radioOpcion3);
        buttonGroup.add(radioOpcion4);
        // Configurar el área de texto único
        areaTextoUnico = new JTextArea(5, 30);
        areaTextoUnico.setLineWrap(true);
        areaTextoUnico.setWrapStyleWord(true);
        scrollAreaTexto = new JScrollPane(areaTextoUnico);
        scrollAreaTexto.setVisible(false); // Inicialmente oculto
        // Event listeners
        btnAnterior.addActionListener(this::anteriorPregunta);
        btnSiguiente.addActionListener(this::siguientePregunta);

        mostrarPregunta();// carga las preguntas al iniciar
    }

    private void mostrarPregunta() {
        if (prueba == null || prueba.getPreguntas().isEmpty()) {
            JOptionPane.showMessageDialog(panelPrincipal, "No hay preguntas disponibles");
            return;
        }
        Pregunta p = prueba.getPreguntas().get(preguntaActual);
        // Actualizar información de la pregunta
        lblNumeroPregunta.setText(String.format("Pregunta %d de %d",
                preguntaActual + 1, prueba.getPreguntas().size()));
        lblTipoPregunta.setText("Tipo: " + p.getTipo().getNombre());
        lblNivelBloom.setText("Nivel: " + p.getNivelBloom().getNombre());
        lblPregunta.setText("<html><div style='width:350px;padding:5px;'>" + p.getTexto() + "</div></html>");
        // Configurar área de respuesta
        configurarRespuestas(p);
        // Actualizar progreso y botones
        progressBar.setValue((preguntaActual + 1) * 100 / prueba.getPreguntas().size());
        btnAnterior.setEnabled(preguntaActual > 0);
        btnSiguiente.setText(preguntaActual == prueba.getPreguntas().size() - 1 ?
                "Finalizar" : "Siguiente");
    }
    // Configura los componentes de respuesta según el tipo de pregunta
    private void configurarRespuestas(Pregunta p) {
        // Ocultar todos los componentes primero
        radioOpcion1.setVisible(false);
        radioOpcion2.setVisible(false);
        radioOpcion3.setVisible(false);
        radioOpcion4.setVisible(false);
        scrollAreaTexto.setVisible(false);
        panelRespuestas.removeAll();
        panelRespuestas.setLayout(new BorderLayout());
        buttonGroup.clearSelection();
        // Mostrar componentes según el tipo de pregunta
        switch (p.getTipo()) {
            case OPCION_MULTIPLE:
            case VERDADERO_FALSO:
                configurarOpciones(p.getOpciones());
                break;
            case RESPUESTA_CORTA:
            case TEXTO_INCOMPLETO:
                configurarAreaTexto(p);
                break;
            default:
                JLabel lblNoDisponible = new JLabel("Tipo de pregunta no soportado aún");
                panelRespuestas.add(lblNoDisponible, BorderLayout.CENTER);
                break;
        }
        // actualiza el panel
        panelRespuestas.revalidate();
        panelRespuestas.repaint();
    }
    // Configura los radio buttons con las opciones de respuesta
    private void configurarOpciones(String[] opciones) {
        JPanel opcionesPanel = new JPanel();
        opcionesPanel.setLayout(new BoxLayout(opcionesPanel, BoxLayout.Y_AXIS));
        // Mostrar y configurar cada radio button según el tipo de pregunta
        radioOpcion1.setVisible(opciones.length > 0);
        radioOpcion2.setVisible(opciones.length > 1);
        radioOpcion3.setVisible(opciones.length > 2);
        radioOpcion4.setVisible(opciones.length > 3);
        if (opciones.length > 0) radioOpcion1.setText(opciones[0]);
        if (opciones.length > 1) radioOpcion2.setText(opciones[1]);
        if (opciones.length > 2) radioOpcion3.setText(opciones[2]);
        if (opciones.length > 3) radioOpcion4.setText(opciones[3]);
        // Seleccionar respuesta si existe
        if (prueba.getPreguntas().get(preguntaActual).getRespuestaUsuario() != null) {
            // Restaurar respuesta previa del usuario si existe
            String respuesta = prueba.getPreguntas().get(preguntaActual).getRespuestaUsuario();
            if (opciones.length > 0 && opciones[0].equals(respuesta)) radioOpcion1.setSelected(true);
            if (opciones.length > 1 && opciones[1].equals(respuesta)) radioOpcion2.setSelected(true);
            if (opciones.length > 2 && opciones[2].equals(respuesta)) radioOpcion3.setSelected(true);
            if (opciones.length > 3 && opciones[3].equals(respuesta)) radioOpcion4.setSelected(true);
        }
        // Añadir las opciones al panel
        if (radioOpcion1.isVisible()) opcionesPanel.add(radioOpcion1);
        if (radioOpcion2.isVisible()) opcionesPanel.add(radioOpcion2);
        if (radioOpcion3.isVisible()) opcionesPanel.add(radioOpcion3);
        if (radioOpcion4.isVisible()) opcionesPanel.add(radioOpcion4);

        panelRespuestas.add(opcionesPanel, BorderLayout.NORTH);
    }
    // Configura el área de texto para respuestas escritas
    private void configurarAreaTexto(Pregunta p) {
        areaTextoUnico.setText(p.getRespuestaUsuario() != null ? p.getRespuestaUsuario() : "");
        scrollAreaTexto.setVisible(true);
        panelRespuestas.add(scrollAreaTexto, BorderLayout.CENTER);
    }
    // Acción para retroceder a la pregunta anterior
    private void anteriorPregunta(ActionEvent e) {
        guardarRespuestaActual();
        if (preguntaActual > 0) {
            preguntaActual--;
            mostrarPregunta();
        }
    }
    // Acción para avanzar a la pregunta anterior
    private void siguientePregunta(ActionEvent e) {
        guardarRespuestaActual();
        if (preguntaActual < prueba.getPreguntas().size() - 1) {
            preguntaActual++;
            mostrarPregunta();
        } else {
            mostrarResultados();
        }
    }
    // Guarda la respuesta del usuario a la pregunta actual
    private void guardarRespuestaActual() {
        Pregunta p = prueba.getPreguntas().get(preguntaActual);

        switch (p.getTipo()) {
            case OPCION_MULTIPLE:
            case VERDADERO_FALSO:
                if (radioOpcion1.isSelected()) p.setRespuestaUsuario(radioOpcion1.getText());
                else if (radioOpcion2.isSelected()) p.setRespuestaUsuario(radioOpcion2.getText());
                else if (radioOpcion3.isSelected()) p.setRespuestaUsuario(radioOpcion3.getText());
                else if (radioOpcion4.isSelected()) p.setRespuestaUsuario(radioOpcion4.getText());
                break;

            case RESPUESTA_CORTA:
            case TEXTO_INCOMPLETO:
                p.setRespuestaUsuario(areaTextoUnico.getText());
                break;
        }
    }
    // Muestra un cuadro de diálogo con los resultados finales de la prueba
    private void mostrarResultados() {
        int correctas = prueba.getRespuestasCorrectas();
        int total = prueba.getNumeroPreguntas();
        double porcentaje = (double) correctas / total * 100;
        String mensaje = String.format(
                "<html><h2>Resultados</h2>" +
                        "<p>Respuestas correctas: %d de %d</p>" +
                        "<p>Porcentaje: %.1f%%</p></html>",
                correctas, total, porcentaje);
        JOptionPane.showMessageDialog(panelPrincipal, mensaje, "Prueba Finalizada", JOptionPane.INFORMATION_MESSAGE);
    }
    // Método para obtener el panel principal y usarlo en una ventana JFrame
    public JPanel getPanel() {
        return panelPrincipal;
    }
}