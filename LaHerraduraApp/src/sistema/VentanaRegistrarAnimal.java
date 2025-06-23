package sistema;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class VentanaRegistrarAnimal extends JFrame {

    private JTextField campoCaravana;
    private JComboBox<String> comboSexo;
    private JComboBox<String> comboVacuna;
    private JTextField campoFechaVacuna;
    private JComboBox<String> comboPreñez;
    private JTextField campoNacimiento;
    private JTextField campoFechaPreñez;
    private JButton botonGuardar;
    private JButton botonVolver;

    public VentanaRegistrarAnimal() {
        setTitle("Registrar Animal");
        setSize(450, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(10, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("ID Caravana:"));
        campoCaravana = new JTextField();
        panel.add(campoCaravana);

        panel.add(new JLabel("Sexo:"));
        comboSexo = new JComboBox<>(new String[]{"Vaca", "Toro"});
        panel.add(comboSexo);

        panel.add(new JLabel("Vacuna aplicada:"));
        comboVacuna = new JComboBox<>(new String[]{"Sin vacunar", "Aftosa", "Brucelosis"});
        panel.add(comboVacuna);

        panel.add(new JLabel("Fecha vacuna (dd-mm-aaaa):"));
        campoFechaVacuna = new JTextField();
        panel.add(campoFechaVacuna);

        // Activar/desactivar campo fecha vacuna según vacuna
        comboVacuna.addActionListener(e -> {
            boolean mostrar = !comboVacuna.getSelectedItem().toString().equals("Sin vacunar");
            campoFechaVacuna.setEnabled(mostrar);
            if (!mostrar) campoFechaVacuna.setText("");
        });

        panel.add(new JLabel("Fecha de nacimiento (dd-mm-aaaa):"));
        campoNacimiento = new JTextField();
        panel.add(campoNacimiento);

        panel.add(new JLabel("¿Está preñada?:"));
        comboPreñez = new JComboBox<>(new String[]{"Sí", "No"});
        panel.add(comboPreñez);

        JLabel labelPreñez = new JLabel("Fecha de preñez (dd-mm-aaaa):");
        campoFechaPreñez = new JTextField();
        panel.add(labelPreñez);
        panel.add(campoFechaPreñez);

        // Ocultar al inicio
        labelPreñez.setVisible(false);
        campoFechaPreñez.setVisible(false);

        comboPreñez.addActionListener(e -> {
            boolean visible = comboPreñez.getSelectedItem().equals("Sí");
            labelPreñez.setVisible(visible);
            campoFechaPreñez.setVisible(visible);
        });

        // Botones
        botonGuardar = new JButton("Guardar");
        botonVolver = new JButton("Volver");
        panel.add(botonGuardar);
        panel.add(botonVolver);

        add(panel);

        botonVolver.addActionListener(e -> {
            this.dispose();
            new VentanaAnimales().setVisible(true);
        });

        botonGuardar.addActionListener(e -> {
            try {
                String caravanaStr = campoCaravana.getText().trim();
                String sexo = comboSexo.getSelectedItem().toString();
                String vacuna = comboVacuna.getSelectedItem().toString();
                String fechaVacStr = campoFechaVacuna.getText().trim();
                String fechaNacStr = campoNacimiento.getText().trim();
                String preñez = comboPreñez.getSelectedItem().toString();
                String fechaPreñezStr = campoFechaPreñez.getText().trim();

                if (caravanaStr.isEmpty() || fechaNacStr.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "⚠️ Completá todos los campos obligatorios.");
                    return;
                }

                int caravana = Integer.parseInt(caravanaStr);

                // Convertir fecha nacimiento
                String[] fNac = fechaNacStr.split("-");
                String fechaNacimientoSql = fNac[2] + "-" + fNac[1] + "-" + fNac[0];

                // Convertir fecha vacuna si aplica
                String fechaVacunaSql = null;
                if (!vacuna.equals("Sin vacunar")) {
                    if (fechaVacStr.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "⚠️ Ingresá fecha de vacuna.");
                        return;
                    }
                    String[] fVac = fechaVacStr.split("-");
                    fechaVacunaSql = fVac[2] + "-" + fVac[1] + "-" + fVac[0];
                }

                // Convertir fecha preñez si aplica
                String fechaPreñez = null;
                if (preñez.equals("Sí")) {
                    if (fechaPreñezStr.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "⚠️ Ingresá fecha de preñez.");
                        return;
                    }
                    String[] fPre = fechaPreñezStr.split("-");
                    fechaPreñez = fPre[2] + "-" + fPre[1] + "-" + fPre[0];
                }

                // Crear objeto
                Animal animal = new AnimalVacuno(
                	    caravana, sexo, vacuna, fechaVacunaSql, preñez,
                	    fechaNacimientoSql, fechaPreñez
                	);
                
                // Guardar en base
                Connection conn = ConexionBD.conectar();
                if (conn == null) {
                    JOptionPane.showMessageDialog(this, "❌ No se pudo conectar a la base de datos.");
                    return;
                }

                String sql = "INSERT INTO animales (caravana, sexo, vacuna, fecha_vacuna, preñez, fecha_nacimiento, fecha_preñez) VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, animal.getCaravana());
                stmt.setString(2, animal.getSexo());
                stmt.setString(3, animal.getVacuna());
                if (animal.getFechaVacuna() != null) {
                    stmt.setDate(4, java.sql.Date.valueOf(animal.getFechaVacuna()));
                } else {
                    stmt.setNull(4, java.sql.Types.DATE);
                }
                stmt.setString(5, animal.getPreñez());
                stmt.setDate(6, java.sql.Date.valueOf(animal.getFechaNacimiento()));
                if (animal.getFechaPreñez() != null) {
                    stmt.setDate(7, java.sql.Date.valueOf(animal.getFechaPreñez()));
                } else {
                    stmt.setNull(7, java.sql.Types.DATE);
                }

                stmt.executeUpdate();
                conn.close();

                JOptionPane.showMessageDialog(this, "✅ Animal registrado correctamente.");
                this.dispose();
                new VentanaAnimales().setVisible(true);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "❌ El ID de caravana debe ser numérico.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Error al guardar: " + ex.getMessage());
                ex.printStackTrace();
            }
        });
    }
}
