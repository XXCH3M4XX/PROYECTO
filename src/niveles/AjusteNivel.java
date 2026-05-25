package niveles;

import gamestates.EstadoJuego;
import main.Juego;
import utils.CargaSprites;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

//clase encargada de gestionar y dibujar los componentes de los niveles
public class AjusteNivel {

    private Juego juego;
    private BufferedImage[] spriteNivel;
    private ArrayList<Nivel> niveles;
    private int indiceNivel = 0;

    //constructor que recibe la instancia del juego e inicializa el nivel
    public AjusteNivel(Juego juego){
        this.juego = juego;
        importarSpritesNivel(0);
        niveles = new ArrayList<>();
        crearNiveles();
    }

    private void crearNiveles() {
        //permite la escalabilidad,
        //da igual cuantos niveles añadamos funcionara
        BufferedImage[] listaNiveles = CargaSprites.getNiveles();
        for(BufferedImage imagen: listaNiveles){
            niveles.add(new Nivel(imagen));
        }
    }

    //carga la imagen del atlas y la divide en un array de subimagenes
    private void importarSpritesNivel(int indice) {
        //selecciona el atlas de tiles correspondiente al nivel actual
        String atlas;
        switch (indice) {
            case 1: atlas = CargaSprites.SUELO2; break;
            case 2: atlas = CargaSprites.SUELO3; break;
            default: atlas = CargaSprites.SUELO1; break;
        }
        BufferedImage imagen = CargaSprites.GetSpriteAtlas(atlas);
        //inicializacion del array para almacenar los 48 sprites
        spriteNivel = new BufferedImage[48];
        //bucles para recorrer las filas y columnas de la hoja de sprites
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 12; j++) {
                int indiceSprite = i * 12 + j;
                //extraccion de cada tile de 32x32 pixeles
                spriteNivel[indiceSprite] = imagen.getSubimage(j * 32, i * 32, 32, 32);
            }
        }
    }

    //metodo para renderizar el nivel en pantalla usando los tiles correspondientes
    public void draw(Graphics g, int offsetXNivel){
        //recorrido de la matriz de tiles segun el alto y ancho definidos
        for(int i = 0; i< Juego.TILES_IN_HEIGHT; i++) {
            for(int j = 0; j <niveles.get(indiceNivel).getDatosNivel()[0].length; j++) {
                //obtenemos el tipo de tile que toca dibujar en esta posicion
                int indice = niveles.get(indiceNivel).getIndiceSprite(j, i);
                //dibujamos el sprite escalado al tamaño de tile del juego
                g.drawImage(spriteNivel[indice], Juego.TILES_SIZE*j - offsetXNivel, Juego.TILES_SIZE*i, Juego.TILES_SIZE, Juego.TILES_SIZE, null);

            }
        }
    }

    //metodo para actualizar logica del nivel si fuera necesario
    public  void update(){

    }
    public int getIndiceNivel() {
        return indiceNivel;
    }

    //devuelve el objeto nivel cargado actualmente
    public Nivel getNivelActual(){

        return niveles.get(indiceNivel);
    }
    //vuelve al primer nivel, se usa al morir y reiniciar
    public void resetNivel() {
        System.out.println("resetNivel llamado, indice antes: " + indiceNivel);
        indiceNivel = 0;
        importarSpritesNivel(0);
        System.out.println("resetNivel completado, atlas recargado");
    }


    public void cargarSiguienteNivel() {
        indiceNivel++;
        if(indiceNivel >= niveles.size()){
            indiceNivel = 0;
            importarSpritesNivel(0);
            juego.getPantallaIntroducirNombre().reset();
            EstadoJuego.state = EstadoJuego.NOMBRE;
            return;
        }
        importarSpritesNivel(indiceNivel); // ← recarga el atlas del nuevo nivel
        Nivel nuevoNivel = niveles.get(indiceNivel);
        juego.getPlaying().getAjusteEnemigo().cargarEnemigos(nuevoNivel);
        juego.getPlaying().getJugador().cargarDatosNivel(nuevoNivel.getDatosNivel());
        juego.getPlaying().setOffsetNivelMaximo(nuevoNivel.getOffsetNivel());
        juego.getPlaying().getAjusteDeObjetos().cargarObjetos(nuevoNivel);
    }
}