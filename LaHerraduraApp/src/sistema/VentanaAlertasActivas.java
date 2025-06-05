package sistema;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class VentanaAlertasActivas extends JFrame {

    private JTextArea areaAlertas;
    private JButton botonVolver;

    public VentanaAlertasActivas() {
        setTitle("Alertas Activas");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());

        // Área de texto para alertas
        areaAlertas = new JTextArea();
        areaAlertas.setEditable(false);
        areaAlertas.setFont(new Font("Monospaced", Font.PLAIN, 13));
        panel.add(new JScrollPane(areaAlertas), BorderLayout.CENTER);

        // Botón volver
        botonVolver = new JButton("Volver");
        panel.add(botonVolver, BorderLayout.SOUTH);

        add(panel);

        // Acción botón volver
        botonVolver.addActionListener(e -> {
            this.dispose();
            new VentanaBienvenida().setVisible(true);
        });

        // Mostrar alertas al abrir
        mostrarAlertas();
    }

    private void mostrarAlertas() {
        try {
            Connection conn = ConexionBD.conectar();
            String sql = "SELECT * FROM animales";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            StringBuilder alertas = new StringBuilder();

            while (rs.next()) {
                int caravana = rs.getInt("caravana");
                String sexo = rs.getString("sexo");
                String vacuna = rs.getString("vacuna");
                Date fechaVacuna = rs.getDate("fecha_vacuna");
                String preñez = rs.getString("preñez");

                boolean alerta = false;
                StringBuilder linea = new StringBuilder("ID " + caravana + " - ");

                if (vacuna == null || vacuna.trim().isEmpty()) {
                    linea.append("❌ Sin vacuna registrada | ");
                    alerta = true;
                } else if ("Aftosa".equalsIgnoreCase(vacuna) && fechaVacuna != null) {
                    long diferencia = System.currentTimeMillis() - fechaVacuna.getTime();
                    long dias = diferencia / (1000 * 60 * 60 * 24);
                    if (dias >= 180) {
                        linea.append("⏳ Requiere aftosa (última: " + fechaVacuna + ") | ");
                        alerta = true;
                    }
                }

                if ("Sí".equalsIgnoreCase(preñez)) {
                    // Falta logica de fecha de preñez
                    linea.append("🤰 Vacuna preñez activa (¡revisar!) | ");
                    alerta = true;
                }

                if (alerta) {
                    alertas.append(linea.toString()).append("\n");
                }
            }

            if (alertas.length() == 0) {
                areaAlertas.setText("✅ No hay alertas activas.");
            } else {
                areaAlertas.setText(alertas.toString());
            }

            conn.close();

        } catch (Exception ex) {
            areaAlertas.setText("❌ Error al cargar alertas: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
