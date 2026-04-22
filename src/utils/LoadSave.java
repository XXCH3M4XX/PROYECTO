package utils;

import entidades.PersonajeEnemigo1;
import main.Juego;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import static utils.Constantes.constantesDelEnemigo.ENEMIGO1;

//clase de utilidad para la carga de recursos y datos externos
public class LoadSave {

    //nombres de los archivos de imagen para los sprites y niveles
    public static final String PLAYER_ATLAS = "Ordenado2.png";
    public static final String LEVEL_ATLAS = "sueloCampo.png";
    //public static final String NIVEL1DATOS = "level_one_data.png";
    public static final String NIVEL1DATOS = "level_one_data_long.png";
    public static final String BOTONES_MENU = "botones.png";
    public static final String FONDO_MENU = "menuFondo.png";
    public static final String FONDO_PANTALLA = "fondoPantallaMenu.png";
    public static final String FONDO_PAUSA = "MenuPausa.png";
    public static final String BOTONES_VOLUMEN = "sound_button.png";
    public static final String BOTONES_URM = "urm_buttons.png";
    public static final String BOTON_NIVEL_VOLUMEN = "volume_buttons.png";
    public static final String FONDO_JUGANDO = "fondoJugando.png";
    public static final String MONTAÑAS_YBOSQUES = "montañasYBosques.png";
    public static final String nubes = "nubesPequeñas.png";
    public static final String ENEMIGO1 = "SpriteSheetLanza.png";

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

    public static ArrayList<PersonajeEnemigo1>getPersonajeEnemigo1() {
        BufferedImage imagen = GetSpriteAtlas(NIVEL1DATOS);
        ArrayList<PersonajeEnemigo1> lista = new ArrayList<>();
        for (int i = 0; i < imagen.getHeight(); i++) {
            for (int j = 0; j < imagen.getWidth(); j++) {
                Color color = new Color(imagen.getRGB(j, i));
                //usamos el valor del canal rojo para determinar el tipo de tile
                int valor = color.getGreen();
                //limite de seguridad para no exceder los indices del atlas
                if (valor == Constantes.constantesDelEnemigo.ENEMIGO1) {
                    lista.add(new PersonajeEnemigo1(j * Juego.TILES_SIZE, i * Juego.TILES_SIZE));

                }

            }
        }
        return lista;
    }

    //genera la matriz del nivel leyendo los valores de color de una imagen
    public static int[][] conseguirDatosNivel() {
        BufferedImage imagen = GetSpriteAtlas(NIVEL1DATOS);
        //inicializacion de la matriz con las dimensiones de tiles del juego
        int[][] datosNivel = new int[imagen.getHeight()][imagen.getWidth()];
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