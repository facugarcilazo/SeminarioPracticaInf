package sistema;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class VentanaLogin extends JFrame {

    private JTextField campoUsuario;
    private JPasswordField campoContraseña;
    private JButton botonIngresar;
    private JButton botonSalir;
    private JLabel mensaje;

    public static String usuarioActivo;
    public static String rolActivo;

    public VentanaLogin() {
        setTitle("Inicio de Sesión - La Herradura");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(7, 1));
        
        
        panel.add(ImagenUtil.cargarImagen());

        // Título
        panel.add(new JLabel("¡Arranquemos el día!", SwingConstants.CENTER));
        panel.add(new JLabel("La Herradura - Sistema de Gestión de Vacunos", SwingConstants.CENTER));

        // Campo usuario
        campoUsuario = new JTextField();
        campoUsuario.setBorder(BorderFactory.createTitledBorder("Usuario"));
        panel.add(campoUsuario);

        // Campo contraseña
        campoContraseña = new JPasswordField();
        campoContraseña.setBorder(BorderFactory.createTitledBorder("Contraseña"));
        panel.add(campoContraseña);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        botonIngresar = new JButton("Iniciar Sesión");
        botonSalir = new JButton("Salir");
        panelBotones.add(botonIngresar);
        panelBotones.add(botonSalir);
        panel.add(panelBotones);

        // Mensaje de estado
        mensaje = new JLabel("", SwingConstants.CENTER);
        mensaje.setForeground(Color.RED);
        panel.add(mensaje);

        add(panel);

        // salir
        botonSalir.addActionListener(e -> System.exit(0));

        // iniciar sesión
        botonIngresar.addActionListener(e -> validarLogin());
    }

    private void validarLogin() {
        String usuario = campoUsuario.getText().toLowerCase().trim();
        String contraseña = new String(campoContraseña.getPassword()).trim();

        try (Connection conn = ConexionBD.conectar()) {
            if (conn == null) {
                mensaje.setForeground(Color.RED);
                mensaje.setText("❌ No se pudo conectar a la base.");
                return;
            }

            String sql = "SELECT * FROM usuarios WHERE nombre = ? AND contraseña = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, usuario);
            stmt.setString(2, contraseña);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                rolActivo = rs.getString("rol");
                usuarioActivo = usuario;
                mensaje.setForeground(new Color(0, 128, 0));
                mensaje.setText("✅ Bienvenido " + usuario + " (" + rolActivo + ")");
                this.dispose();
                new VentanaBienvenida().setVisible(true);
            } else {
                mensaje.setForeground(Color.RED);
                mensaje.setText("❌ Usuario o contraseña incorrectos");
            }

        } catch (Exception e) {
            mensaje.setForeground(Color.RED);
            mensaje.setText("❌ Error al iniciar sesión.");
            e.printStackTrace();
        }
    }
}

