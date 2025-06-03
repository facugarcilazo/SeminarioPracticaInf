package sistema;

import javax.swing.*;
import java.awt.*;

public class VentanaBienvenida extends JFrame {

    private JLabel mensajeBienvenida;
    private JButton botonAnimales;
    private JButton botonAlertas;
    private JButton botonSalir;

    public VentanaBienvenida() {
        setTitle("Bienvenida - La Herradura");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel principal
        JPanel panel = new JPanel(new GridLayout(6, 1, 10, 10));

        // 🐄 Imagen de vaca
        panel.add(ImagenUtil.cargarImagen());

        // Mensaje superior
        panel.add(new JLabel("La Herradura", SwingConstants.CENTER));

        // Bienvenida personalizada
        mensajeBienvenida = new JLabel("Bienvenido " + VentanaLogin.usuarioActivo, SwingConstants.CENTER);
        mensajeBienvenida.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(mensajeBienvenida);

        // Botón Animales
        botonAnimales = new JButton("Animales");
        panel.add(botonAnimales);

        // Botón Alertas
        botonAlertas = new JButton("Alertas activas");
        panel.add(botonAlertas);

        // Botón Salir
        botonSalir = new JButton("Salir");
        panel.add(botonSalir);

        add(panel);

        // Acciones
        botonSalir.addActionListener(e -> System.exit(0));

        botonAnimales.addActionListener(e -> {
            this.dispose();
            new VentanaAnimales().setVisible(true);
        });

        botonAlertas.addActionListener(e -> {
            this.dispose();
            new VentanaAlertasActivas().setVisible(true);
        });
    }
}
