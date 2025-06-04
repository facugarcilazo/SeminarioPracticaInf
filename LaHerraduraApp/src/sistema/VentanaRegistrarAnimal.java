package sistema;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class VentanaRegistrarAnimal extends JFrame {

    private JTextField campoCaravana;
    private JComboBox<String> comboSexo;
    private JComboBox<String> comboVacuna;
    private JTextField campoFechaVacuna;
    private JComboBox<String> comboPreñez;
    private JButton botonGuardar;
    private JButton botonVolver;

    public VentanaRegistrarAnimal() {
        setTitle("Registrar Animal");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(7, 2, 5, 5));

        // Campos
        panel.add(new JLabel("ID Caravana:"));
        campoCaravana = new JTextField();
        panel.add(campoCaravana);

        panel.add(new JLabel("Sexo:"));
        comboSexo = new JComboBox<>(new String[]{"Vaca", "Toro"});
        panel.add(comboSexo);

        panel.add(new JLabel("Vacuna aplicada:"));
        comboVacuna = new JComboBox<>(new String[]{"Aftosa", "Brucelosis"});
        panel.add(comboVacuna);

        panel.add(new JLabel("Fecha de aplicación (dd-mm-aaaa):"));
        campoFechaVacuna = new JTextField();
        panel.add(campoFechaVacuna);

        panel.add(new JLabel("¿Está preñada?:"));
        comboPreñez = new JComboBox<>(new String[]{"Sí", "No"});
        panel.add(comboPreñez);

        botonGuardar = new JButton("Guardar");
        botonVolver = new JButton("Volver");
        panel.add(botonGuardar);
        panel.add(botonVolver);

        add(panel);

        // Acción volver
        botonVolver.addActionListener(e -> {
            this.dispose();
            new VentanaAnimales().setVisible(true);
        });

        // Acción guardar (por ahora, solo mostrar datos ingresados)
        botonGuardar.addActionListener(e -> {
            String caravanaStr = campoCaravana.getText().trim();
            String sexo = comboSexo.getSelectedItem().toString();
            String vacuna = comboVacuna.getSelectedItem().toString();
            String fechaStr = campoFechaVacuna.getText().trim();
            String preñez = comboPreñez.getSelectedItem().toString();

            // Validaciones mínimas
            if (caravanaStr.isEmpty() || fechaStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Completa todos los campos obligatorios.");
                return;
            }

            try {
                int caravana = Integer.parseInt(caravanaStr);
                // Convertir fecha de formato dd-mm-aaaa a yyyy-mm-dd
                String[] partes = fechaStr.split("-");
                if (partes.length != 3) throw new Exception("Formato de fecha incorrecto");
                String fechaSql = partes[2] + "-" + partes[1] + "-" + partes[0]; // yyyy-mm-dd

                Connection conn = ConexionBD.conectar();
                if (conn == null) {
                    JOptionPane.showMessageDialog(this, "No se pudo conectar a la base de datos.");
                    return;
                }

                String sql = "INSERT INTO animales (caravana, sexo, vacuna, fecha_vacuna, preñez) VALUES (?, ?, ?, ?, ?)";
                var stmt = conn.prepareStatement(sql);
                stmt.setInt(1, caravana);
                stmt.setString(2, sexo);
                stmt.setString(3, vacuna);
                stmt.setDate(4, java.sql.Date.valueOf(fechaSql));
                stmt.setString(5, preñez);
                stmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "✅ Animal registrado correctamente.");
                conn.close();
                this.dispose();
                new VentanaAnimales().setVisible(true);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El ID caravana debe ser numérico.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Error al guardar: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

    }
}
