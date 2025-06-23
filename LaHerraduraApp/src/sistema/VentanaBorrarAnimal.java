package sistema;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class VentanaBorrarAnimal extends JFrame {

    private JTextField campoCaravana;
    private JTextArea areaDatos;
    private JButton botonBuscar;
    private JButton botonBorrar;
    private JButton botonVolver;

    public VentanaBorrarAnimal() {
        setTitle("Borrar Animal");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());

        //Búsqueda
        JPanel panelSuperior = new JPanel(new FlowLayout());
        campoCaravana = new JTextField(10);
        botonBuscar = new JButton("Buscar");
        panelSuperior.add(new JLabel("ID Caravana:"));
        panelSuperior.add(campoCaravana);
        panelSuperior.add(botonBuscar);
        panel.add(panelSuperior, BorderLayout.NORTH);

        //Datos
        areaDatos = new JTextArea(6, 30);
        areaDatos.setEditable(false);
        JScrollPane scroll = new JScrollPane(areaDatos);
        panel.add(scroll, BorderLayout.CENTER);

        // Botones
        JPanel panelInferior = new JPanel(new FlowLayout());
        botonBorrar = new JButton("Borrar Animal");
        botonVolver = new JButton("Volver");
        panelInferior.add(botonBorrar);
        panelInferior.add(botonVolver);
        panel.add(panelInferior, BorderLayout.SOUTH);

        add(panel);

        // Volver
        botonVolver.addActionListener(e -> {
            this.dispose();
            new VentanaAnimales().setVisible(true);
        });

        // Buscar
        botonBuscar.addActionListener(e -> buscarAnimal());

        // Borrar
        botonBorrar.addActionListener(e -> confirmarBorrado());
    }

    private void buscarAnimal() {
        areaDatos.setText("");
        String caravanaStr = campoCaravana.getText().trim();

        if (caravanaStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un número de caravana.");
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

                areaDatos.setText("🐮 Caravana: " + caravana +
                        "\nSexo: " + sexo +
                        "\nVacuna: " + vacuna +
                        "\nFecha: " + fecha +
                        "\nPreñez: " + preñez);
            } else {
                areaDatos.setText("⚠️ No se encontró ningún animal con esa caravana.");
            }

            conn.close();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "La caravana debe ser numérica.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Error al buscar: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void confirmarBorrado() {
        String texto = areaDatos.getText();
        if (texto.isEmpty() || texto.startsWith("⚠️")) {
            JOptionPane.showMessageDialog(this, "Busque primero un animal válido.");
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Confirmás borrar este animal?", "Confirmar borrado",
                JOptionPane.YES_NO_OPTION);

        if (opcion == JOptionPane.YES_OPTION) {
            borrarAnimal();
        }
    }

    private void borrarAnimal() {
        try {
            int caravana = Integer.parseInt(campoCaravana.getText().trim());
            Connection conn = ConexionBD.conectar();
            String sql = "DELETE FROM animales WHERE caravana = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, caravana);
            int filas = stmt.executeUpdate();

            if (filas > 0) {
                JOptionPane.showMessageDialog(this, "✅ Animal borrado correctamente.");
                areaDatos.setText("");
                campoCaravana.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "⚠️ No se encontró el animal para borrar.");
            }

            conn.close();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Error al borrar: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}

