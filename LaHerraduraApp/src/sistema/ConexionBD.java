package sistema;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    private static final String URL = "jdbc:mysql://localhost:3306/ganado_db";
    private static final String USUARIO = "root"; // ⚠️ Cambiá si usás otro usuario
    private static final String CONTRASEÑA = "2187";  // ⚠️ Si tenés contraseña, escribila

    public static Connection conectar() {
        try {
            return DriverManager.getConnection(URL, USUARIO, CONTRASEÑA);
        } catch (SQLException e) {
            System.out.println("❌ Error al conectar a MySQL: " + e.getMessage());
            return null;
        }
    }
}
