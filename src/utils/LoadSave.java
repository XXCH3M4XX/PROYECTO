package utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

//clase de utilidad para la carga de recursos y datos externos
public class LoadSave {

    //nombres de los archivos de imagen para los sprites y niveles
    public static final String PLAYER_ATLAS = "SpriteSheetJonathan.png";
    public static final String LEVEL_ATLAS = "sueloCampo.png";
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
    public static final String BARRA_SALUD = "SALUD_ENERGIA.png";
    public static final String NIVELCOMPLETADO_MENU = "completed_sprite.png";
    public static final String OBJETOS = "objects_sprites.png";
    public static final String POCIONES = "potions_sprites.png";
    public static final String INTRO = "hpjmGames.png";
    public static final String TRAMPA = "trap_atlas.png";
    public static final String ESQUELETO_HUESO = "esqueletoHueso.png";
    public static final String HUESO_PROYECTIL =  "huesoProyectil.png";
    public static final String PANTALLA_MUERTE =  "pantallaMuerte.png";


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
    public static BufferedImage[] getNiveles() {
        URL url = LoadSave.class.getResource("/Levels");
        File archivo = null;

        //abrimos el archivo y extraemos los niveles
        //hay un error con el tipo de excepcion
        try {
            archivo = new File(url.toURI());
        }catch(Exception e){
            e.printStackTrace();
        }
        File[] listaNiveles = archivo.listFiles();
        File[] nivelesOrdenados = new File[listaNiveles.length];

        //algoritmo sencillo pero poquito eficiente para ordenar los 3 niveles que
        //vamos a tener
        for(int i = 0; i< nivelesOrdenados.length; i++){
            for(int j = 0; j < listaNiveles.length; j++){
                if(listaNiveles[j].getName().equals("" + (i + 1) + ".png")){
                    nivelesOrdenados[i] = listaNiveles[j];
                }
            }
        }

        //recorremos los archivos para sacar los niveles
        for(File f : listaNiveles) {
            System.out.println("Archivo: " + archivo.getName());
        }
        BufferedImage[] imagenes = new BufferedImage[nivelesOrdenados.length];
        try {
            for(int i = 0; i < imagenes.length; i++){
                //el metodo read es muy importante
                imagenes[i] = ImageIO.read(nivelesOrdenados[i]);
            }
        }catch(Exception e){
            System.out.println("Error generando las imagenes.");
        }

        return imagenes;
    }





}