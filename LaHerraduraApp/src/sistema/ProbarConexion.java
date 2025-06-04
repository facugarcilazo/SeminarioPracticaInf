package sistema;

import java.sql.Connection;

public class ProbarConexion {
    public static void main(String[] args) {
        Connection conn = ConexionBD.conectar();

        if (conn != null) {
            System.out.println("✅ Conexión exitosa a MySQL.");
        } else {
            System.out.println("❌ No se pudo conectar.");
        }
    }
}
