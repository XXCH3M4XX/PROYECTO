package utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class LoadSave {

    public static final String PLAYER_ATLAS = "Ordenado2.png";
    public static final String LEVEL_ATLAS = "prueba de escenario.png";


    public static BufferedImage GetSpriteAtlas(String fileName){
    BufferedImage imagen = null;
    InputStream entrada = LoadSave.class.getResourceAsStream("/" + fileName);
    try {
        if (entrada == null){
            System.out.println("es null");
        }
        imagen = ImageIO.read(entrada);

    }catch(IOException e){
        System.out.println("Error encontrando las imagenes.");
    } finally {
        //libera recursos y evita problemas
        try {
            entrada.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    return imagen;
}

}
