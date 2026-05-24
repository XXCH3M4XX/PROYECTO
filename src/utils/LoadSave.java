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

    //suelos
    public static final String SUELO1 = "suelos/suelosCampo.png";
    public static final String SUELO2 = "suelos/suelosMadera.png";
    public static final String SUELO3 = "suelos/sueloAjedrezado.png";

    //fondos
    public static final String FONDO_NIVEL1 = "fondos/fondoNivel1.png";
    public static final String MONTAÑASYBOSQUES_NIVEL1 = "fondos/montañasYBosquesNivel1.png";
    public static final String NUBES_NIVEL1 = "fondos/nubesPequeñasNivel1.png";
    public static final String CASTILLO = "fondos/castilloDio.png";
    public static final String FONDO_NIVEL2 = "fondos/fondoNivel2.png";
    public static final String ARAÑA_NIVEL2 = "fondos/arañaNivel2.png";
    public static final String FONDO_NIVEL3 = "fondos/castilloDioNivel3.png";




    //spritesheets
    public static final String JUGADOR = "spritesheets/SpriteSheetJonathan.png";
    public static final String ENEMIGO1 = "spritesheets/SpriteSheetLanza.png";
    public static final String TRAMPA = "spritesheets/trampa.png";
    public static final String OBJETOS = "spritesheets/cajasBarrilesSprites.png";
    public static final String POCIONES = "spritesheets/spritesPociones.png";
    public static final String ESQUELETO_HUESO = "spritesheets/esqueletoHueso.png";
    public static final String HUESO_PROYECTIL =  "spritesheets/huesoProyectil.png";
    public static final String DIO = "spritesheets/SpriteDio.png";


    //interfaces
    public static final String FONDO_MENU = "interfaces/menuFondoInterfaz.png";
    public static final String FONDO_PANTALLA = "interfaces/fondoPantallaMenu.png";
    public static final String FONDO_PAUSA = "interfaces/MenuPausaInterfaz.png";
    public static final String BARRA_SALUD = "interfaces/saludEnergiaInterfaz.png";
    public static final String NIVELCOMPLETADO_MENU = "interfaces/nivelCompletadoInterfaz.png";
    public static final String INTRO = "interfaces/hpjmGames.png";
    public static final String PANTALLA_MUERTE =  "interfaces/pantallaMuerteInterfaz.png";
    public static final String FONDO_OPCIONES = "interfaces/MenuPausaInterfaz.png";
    public static final String BARRA_VIDA_DIO = "interfaces/SALUD_ENERGIA_DIO.png";


    //botones
    public static final String BOTONES_MENU = "botones/botonesMenuPrincipal.png";
    public static final String BOTON_NIVEL_VOLUMEN = "botones/botonVolumen.png";
    public static final String BOTONES_NAVEGACION = "botones/botonesNavegacion.png";
    public static final String BOTON_OPCIONES = "botones/botonOpciones.png";
    public static final String BOTON_CONTROLES = "botones/botonControles.png";
    public static final String BOTONES_VOLUMEN = "botones/efectosMusicaBotones.png";

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