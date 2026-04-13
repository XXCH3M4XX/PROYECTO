package utils;

import main.Juego;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

//clase de utilidad para la carga de recursos y datos externos
public class LoadSave {

    //nombres de los archivos de imagen para los sprites y niveles
    public static final String PLAYER_ATLAS = "Ordenado2.png";
    public static final String LEVEL_ATLAS = "suelosSprites.png";
    public static final String NIVEL1DATOS = "level_one_data.png";
    public static final String BOTONES_MENU = "botones.png";
    public static final String FONDO_MENU = "menuFondo.png";
    public static final String FONDO_PANTALLA = "fondoPantallaMenu.png";
    public static final String FONDO_PAUSA = "pause_menu.png";
    public static final String BOTONES_VOLUMEN = "sound_button.png";


    //metodo para cargar una imagen desde la carpeta de recursos
    public static BufferedImage GetSpriteAtlas(String fileName){
        BufferedImage imagen = null;
        InputStream entrada = LoadSave.class.getResourceAsStream("/" + fileName);
        try {
            //comprobacion de seguridad por si el archivo no existe
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

    //genera la matriz del nivel leyendo los valores de color de una imagen
    public static int[][] conseguirDatosNivel() {
        //inicializacion de la matriz con las dimensiones de tiles del juego
        int[][] datosNivel = new int[Juego.TILES_IN_HEIGHT][Juego.TILES_IN_WIDTH];
        BufferedImage imagen = GetSpriteAtlas(NIVEL1DATOS);
        //recorrido de cada pixel de la imagen de datos
        for(int i = 0; i < imagen.getHeight(); i++){
            for(int j = 0; j < imagen.getWidth(); j++){
                Color color = new Color(imagen.getRGB(j, i));
                //usamos el valor del canal rojo para determinar el tipo de tile
                int valor = color.getRed();
                //limite de seguridad para no exceder los indices del atlas
                if(valor >= 48) {
                    valor = 0;
                }
                datosNivel[i][j] = valor;
            }
        }
        return datosNivel;
    }

}