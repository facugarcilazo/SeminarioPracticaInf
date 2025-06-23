package sistema;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.FileOutputStream;
import java.sql.*;

public class PDFExporter {

    public static void exportarAnimales(String ruta) {
        try {
            Document doc = new Document();
            PdfWriter.getInstance(doc, new FileOutputStream(ruta));
            doc.open();

            doc.add(new Paragraph("Listado de Animales - La Herradura"));
            doc.add(new Paragraph(" "));

            Connection conn = ConexionBD.conectar();
            String sql = "SELECT * FROM animales";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Animal animal = new AnimalVacuno(
                    rs.getInt("caravana"),
                    rs.getString("sexo"),
                    rs.getString("vacuna"),
                    rs.getString("fecha_vacuna"),
                    rs.getString("preñez"),
                    rs.getString("fecha_nacimiento"),
                    rs.getString("fecha_preñez")
                );

                doc.add(new Paragraph(animal.resumen()));
                doc.add(new Paragraph("------------------------"));
            }

            doc.close();
            conn.close();
            System.out.println("✅ PDF generado correctamente.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
