package utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

//clase de utilidad para la carga de recursos y datos externos
public class CargaSprites {

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
    public static final String BARRA_SALUD = "interfaces/SaludEnergiaInterfaz.png";
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
        InputStream entrada = CargaSprites.class.getResourceAsStream("/" + fileName);
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
        //carga los niveles por nombre directamente sin listar carpetas
        // añade mas entradas si tienes mas niveles
        String[] nombresNiveles = {"Levels/1.png", "Levels/2.png", "Levels/3.png"};

        ArrayList<BufferedImage> imagenes = new ArrayList<>();
        for (String nombre : nombresNiveles) {
            InputStream is = CargaSprites.class.getResourceAsStream("/" + nombre);
            if (is != null) {
                try {
                    imagenes.add(ImageIO.read(is));
                    is.close();
                } catch (IOException e) {
                    System.out.println("Error cargando nivel: " + nombre);
                }
            }
        }
        return imagenes.toArray(new BufferedImage[0]);
    }





}