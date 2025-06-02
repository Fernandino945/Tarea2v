package frontend;
import backend.Pregunta;
import backend.Prueba;
import backend.TaxonomiaBloom;
import backend.TipoPregunta;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class MainForm {//atributos privados de la clase que tambien incluyen los de la ventana .form
    private JPanel mainPanel;
    private JButton cargarArchivoButton;
    private JLabel infoLabel;
    private JButton iniciarPruebaButton;
    private JFileChooser fileChooser;
    private Prueba prueba;
    private JLabel titulo;

    public MainForm() {
        fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar archivo txt");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos txt", "txt"));
        // el boton para iniciar se mantiene desactivado hasta que se carge el archivo .txt
        iniciarPruebaButton.setEnabled(false);
        // Listeners que manejan la lógica principal
        cargarArchivoButton.addActionListener(this::cargarArchivoAction);
        iniciarPruebaButton.addActionListener(this::iniciarPruebaAction);
    }
// Maneja la carga del archivo y actualiza la interfaz
    private void cargarArchivoAction(ActionEvent e) {
        int resultado = fileChooser.showOpenDialog(mainPanel);

        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            try {
                prueba = cargarPruebaDesdetxt(archivo);
                infoLabel.setText(String.format(
                        "Archivo cargado: %s (%d preguntas)",
                        archivo.getName(),
                        prueba.getPreguntas().size()
                ));
                iniciarPruebaButton.setEnabled(true); // habilita el boton de iniciar al ingresar el archivo txt de forma exitosa
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(mainPanel,
                        "Error al cargar el archivo: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
    // Procesa el archivo .txt línea por línea para crear objetos Pregunta
    private Prueba cargarPruebaDesdetxt(File archivo) throws Exception {
        List<Pregunta> preguntas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            // Saltar cabecera si existe
            String linea = br.readLine();

            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");
                if (datos.length < 5) continue;
                // Constructor de Pregunta espera: tipo de pregunta, nivel bloom, enunciado pregunta, alternativas[], respuestaCorrecta
                try {
                    Pregunta pregunta = new Pregunta(
                            datos[2].replace("\"", ""), // Enunciado
                            TipoPregunta.valueOf(datos[0]), // Tipo
                            TaxonomiaBloom.valueOf(datos[1]), // Bloom
                            datos[4], // Respuesta correcta
                            datos[3].split(",") // Opciones
                    );
                    preguntas.add(pregunta);
                } catch (IllegalArgumentException e) {
                    System.err.println("Error al parsear línea: " + linea);
                }
            }
        }
        return new Prueba(preguntas); //retorna la prueba con las preguntas procesadas
    }
// Inicia la ventana con los datos cargadas
    private void iniciarPruebaAction(ActionEvent e) {
        if (prueba != null) {
            //configuracion para la ventana
            JFrame frame = new JFrame("Prueba");
            frame.setContentPane(new PruebaForm(prueba).getPanel());
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(mainPanel);
            frame.setVisible(true);
        }
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Sistema de Pruebas");
        frame.setContentPane(new MainForm().getMainPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}