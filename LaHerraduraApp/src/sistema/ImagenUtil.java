package sistema;

import javax.swing.*;
import java.awt.*;

public class ImagenUtil {
    public static JLabel cargarImagen() {
     
        ImageIcon iconoOriginal = new ImageIcon("resources/download.png");

       
        Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon iconoFinal = new ImageIcon(imagenEscalada);

        JLabel etiqueta = new JLabel(iconoFinal);
        etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
        return etiqueta;
    }
}
