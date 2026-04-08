package niveles;

import main.Juego;
import utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

//clase encargada de gestionar y dibujar los componentes de los niveles
public class AjusteNivel {

    private Juego juego;
    private BufferedImage[] spriteNivel;
    private Nivel nivel1;

    //constructor que recibe la instancia del juego e inicializa el nivel
    public AjusteNivel(Juego juego){
        this.juego = juego;
        importarSpritesNivel();
        nivel1 = new Nivel(LoadSave.conseguirDatosNivel());

    }

    //carga la imagen del atlas y la divide en un array de subimagenes
    private void importarSpritesNivel() {
        BufferedImage imagen = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
        //inicializacion del array para almacenar los 48 sprites
        spriteNivel = new BufferedImage[48];
        //bucles para recorrer las filas y columnas de la hoja de sprites
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 12; j++) {
                int indice = i*12 + j;
                //extraccion de cada tile de 32x32 pixeles
                spriteNivel[indice] = imagen.getSubimage(j*32, i*32, 32, 32);
            }
        }
    }

    //metodo para renderizar el nivel en pantalla usando los tiles correspondientes
    public void draw(Graphics g){
        //recorrido de la matriz de tiles segun el alto y ancho definidos
        for(int i = 0; i< Juego.TILES_IN_HEIGHT; i++) {
            for(int j = 0; j < Juego.TILES_IN_WIDTH; j++) {
                //obtenemos el tipo de tile que toca dibujar en esta posicion
                int indice = nivel1.getIndiceSprite(j, i);
                //dibujamos el sprite escalado al tamaño de tile del juego
                g.drawImage(spriteNivel[indice], Juego.TILES_SIZE*j, Juego.TILES_SIZE*i, Juego.TILES_SIZE, Juego.TILES_SIZE, null);

            }
        }
    }

    //metodo para actualizar logica del nivel si fuera necesario
    public void update(){

    }

    //devuelve el objeto nivel cargado actualmente
    public Nivel getNivelActual(){
        return nivel1;
    }

}