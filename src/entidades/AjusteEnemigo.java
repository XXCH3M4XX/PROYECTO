package entidades;

import gamestates.Playing;
import main.Juego;
import utils.LoadSave;
import static utils.Constantes.constantesDelEnemigo.*;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class AjusteEnemigo {
    Playing playing;
    private BufferedImage[][]ArrayEnemigo1;
    private ArrayList<PersonajeEnemigo1> enemigos = new ArrayList<>();
    public AjusteEnemigo(Playing playing){
        this.playing = playing;
        cargarImagenesEnemigo();
        añadirEnemigos();
    }

    private void añadirEnemigos() {
        enemigos = LoadSave.getPersonajeEnemigo1();
        System.out.println("tamaño del enemigo: " + enemigos.size());
    }

    public void update(int [][] datosNivel, Jugador jugador){
        for (PersonajeEnemigo1 p : enemigos) {
            if (p.isActivo()){
                p.update(datosNivel, jugador);
            }
        }
    }
    public void draw(Graphics e, int OffsetXNivel){
        drawEnemigos(e, OffsetXNivel);
    }

    private void drawEnemigos(Graphics e, int OffsetXNivel) {
        for (PersonajeEnemigo1 p : enemigos) {
            if (p.isActivo()) {
                int estado = p.getEstadoEnemigo();
                int indice = p.getAniIndice();

                // Evita dibujar si el indice está fuera del array
                if (indice >= ArrayEnemigo1[estado].length) continue;

                BufferedImage frame = ArrayEnemigo1[estado][indice];
                if (frame == null){
                    System.out.println("FRAME NULL → estado=" + estado + " indice=" + indice);
                    continue;
                }

                int drawX = (int)(p.getHitbox().x - ENEMIGO1_DRAWOFFSET_X - OffsetXNivel);
                int drawY = (int)(p.getHitbox().y - ENEMIGO1_DRAWOFFSET_Y);
                int w = (int)(Jugador.SPRITE_W * 1.6f);
                int h = (int)(Jugador.SPRITE_H * 1.3f);


                if (p.mirandoDerecha) {
                    //sprite normal mirando a la derecha
                    e.drawImage(frame, drawX, drawY, w, h, null);
                } else {
                    //voltear horizontalmente dibujando desde el borde derecho con ancho negativo
                    e.drawImage(frame, drawX + w, drawY, -w, h, null);
                }
                p.drawBoxAtaque(e,OffsetXNivel);
            }
        }
    }

    public void golpeEnemigo(Rectangle2D.Float boxAtaque){
        for(Enemigo e : enemigos){
            if (e.isActivo()){
                if(boxAtaque.intersects(e.getHitbox())){
                    e.daño(10);
                    return;
                }
            }
        }
    }


    //cada fila tiene su propio número de frames
    private void cargarImagenesEnemigo() {
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.ENEMIGO1);
        System.out.println("Spritesheet: " + temp.getWidth() + "x" + temp.getHeight());
        ArrayEnemigo1 = new BufferedImage[5][];
        for (int i = 0; i < ArrayEnemigo1.length; i++) {
            int frames = getSpriteAmount(ENEMIGO1, i);
            ArrayEnemigo1[i] = new BufferedImage[frames];
            for (int j = 0; j < frames; j++) {
                int px = j * ENEMIGO1_WIDTH_DEFAULT;
                int py = i * ENEMIGO1_HEIGHT_DEFAULT;
                System.out.println("Fila " + i + " frame " + j + " -> x:" + px + " y:" + py);
                ArrayEnemigo1[i][j] = temp.getSubimage(px, py,
                        ENEMIGO1_WIDTH_DEFAULT, ENEMIGO1_HEIGHT_DEFAULT);
            }
        }
    }

    public void resetearTodosEnemigos(){
        for(PersonajeEnemigo1 p : enemigos){
            p.resetearEnemigo();
        }
    }

}
