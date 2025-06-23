package sistema;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class VentanaAnimales extends JFrame {

    private JTextField campoCaravana;
    private JButton botonBuscar;
    private JButton botonRegistrar;
    private JButton botonBorrar;
    private JButton botonVolver;
    private JButton botonExportar; // ✅ nuevo botón

    public VentanaAnimales() {
        setTitle("Gestión de Animales - La Herradura");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(8, 1, 10, 10));

        panel.add(ImagenUtil.cargarImagen());

        // Buscar
        JPanel panelBuscar = new JPanel(new FlowLayout());
        campoCaravana = new JTextField(10);
        botonBuscar = new JButton("Ir");
        panelBuscar.add(new JLabel("Buscar por ID Caravana:"));
        panelBuscar.add(campoCaravana);
        panelBuscar.add(botonBuscar);
        panel.add(panelBuscar);

        // Botón registrar
        botonRegistrar = new JButton("Registrar animal");
        panel.add(botonRegistrar);

        // Botón borrar
        botonBorrar = new JButton("Borrar animal");
        panel.add(botonBorrar);

        // Botón exportar PDF
        botonExportar = new JButton("Exportar a PDF");
        panel.add(botonExportar);

        // Botón volver
        botonVolver = new JButton("Volver");
        panel.add(botonVolver);

        add(panel);

        // Acción volver
        botonVolver.addActionListener(e -> {
            this.dispose();
            new VentanaBienvenida().setVisible(true);
        });

        // Buscar
        botonBuscar.addActionListener(e -> {
            String caravanaStr = campoCaravana.getText().trim();
            if (caravanaStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, ingrese un ID de caravana.");
                return;
            }

            try {
                int caravana = Integer.parseInt(caravanaStr);
                Connection conn = ConexionBD.conectar();
                String sql = "SELECT * FROM animales WHERE caravana = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, caravana);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    String sexo = rs.getString("sexo");
                    String vacuna = rs.getString("vacuna");
                    String fecha = rs.getString("fecha_vacuna");
                    String preñez = rs.getString("preñez");

                    JOptionPane.showMessageDialog(this,
                            "🐮 Caravana: " + caravana +
                            "\nSexo: " + sexo +
                            "\nVacuna: " + vacuna +
                            "\nFecha: " + fecha +
                            "\nPreñez: " + preñez,
                            "Animal Encontrado",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "⚠️ No se encontró un animal con esa caravana.");
                }

                conn.close();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "La caravana debe ser un número.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Error al buscar: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        // Registrar
        botonRegistrar.addActionListener(e -> {
            this.dispose();
            new VentanaRegistrarAnimal().setVisible(true);
        });

        // Borrar
        botonBorrar.addActionListener(e -> {
            this.dispose();
            new VentanaBorrarAnimal().setVisible(true);
        });

        // ✅ Exportar PDF
        botonExportar.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new java.io.File("vacunos.pdf"));
            int opcion = fileChooser.showSaveDialog(this);
            if (opcion == JFileChooser.APPROVE_OPTION) {
                String ruta = fileChooser.getSelectedFile().getAbsolutePath();
                PDFExporter.exportarAnimales(ruta);
                JOptionPane.showMessageDialog(this, "✅ PDF generado:\n" + ruta);
            }
        });
    }
}
