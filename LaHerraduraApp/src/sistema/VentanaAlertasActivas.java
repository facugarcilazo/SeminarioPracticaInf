package sistema;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class VentanaAlertasActivas extends JFrame {

    private JTextArea areaAlertas;
    private JButton botonVolver;
    private JButton botonExportar;


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
            Date hoy = new Date(System.currentTimeMillis());
            while (rs.next()) {
                int caravana = rs.getInt("caravana");
                String sexo = rs.getString("sexo");
                String vacuna = rs.getString("vacuna");
                Date fechaVacuna = rs.getDate("fecha_vacuna");
                String preñez = rs.getString("preñez");

                
                Date fechaNacimiento = rs.getDate("fecha_nacimiento");
                Date fechaPreñez = rs.getDate("fecha_preñez");

                boolean alerta = false;
                StringBuilder linea = new StringBuilder("ID " + caravana + " - ");

                // 🟠 Aftosa vencida, más de 180 días desde vacunación
                if ("Aftosa".equalsIgnoreCase(vacuna) && fechaVacuna != null) {
                    long dias = (hoy.getTime() - fechaVacuna.getTime()) / (1000 * 60 * 60 * 24);
                    if (dias > 180) {
                        linea.append("🟠 Aftosa vencida hace ").append(dias).append(" días | ");
                        alerta = true;
                    }
                }

                // 🔴 Brucelosis fuera de tiempo, aplicada después de 4 meses (120 días de vida)
                if ("Brucelosis".equalsIgnoreCase(vacuna) && fechaNacimiento != null && fechaVacuna != null) {
                    long edadDias = (fechaVacuna.getTime() - fechaNacimiento.getTime()) / (1000 * 60 * 60 * 24);
                    if (edadDias > 120) {
                        linea.append("🔴 Brucelosis aplicada tarde (").append(edadDias).append(" días de vida) | ");
                        alerta = true;
                    }
                }

                // ⏰ Preñez próxima a parto, faltan 10 días o menos para 280 días
                if ("Sí".equalsIgnoreCase(preñez) && fechaPreñez != null) {
                    long diasPreñez = (hoy.getTime() - fechaPreñez.getTime()) / (1000 * 60 * 60 * 24);
                    long diasGestacion = 280;
                    long diasFaltantes = diasGestacion - diasPreñez;

                    if (diasFaltantes <= 10 && diasFaltantes > 0) {
                        linea.append("⏰ Vaca por parir en ").append(diasFaltantes).append(" días | ");
                        alerta = true;
                    }
                }

                // Si por lo menos una alerta, se muestra
                if (alerta) {
                    alertas.append(linea.toString()).append("\n");
                }
            }

            // Mostrar alertas o mensaje si no hay ninguna alerta
            if (alertas.length() == 0) {
                areaAlertas.setText("✅ No hay alertas activas por el momento.");
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
