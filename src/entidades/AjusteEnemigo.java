package entidades;

import gamestates.Playing;
import main.Juego;
import utils.LoadSave;
import static utils.Constantes.constantesDelEnemigo.*;

import java.awt.*;
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

    public void update(int [][] datosNivel){
        for (PersonajeEnemigo1 p : enemigos) {
            p.update(datosNivel);
        }
    }
    public void draw(Graphics e, int OffsetXNivel){
        drawEnemigos(e, OffsetXNivel);
    }

    private void drawEnemigos(Graphics e, int OffsetXNivel) {
        for (PersonajeEnemigo1 p : enemigos) {
            int estado = p.getEstadoEnemigo();
            int indice = p.getAniIndice();

            // Evita dibujar si el indice está fuera del array
            if (indice >= ArrayEnemigo1[estado].length) continue;

            BufferedImage frame = ArrayEnemigo1[estado][indice];
            if (frame == null){
                System.out.println("FRAME NULL → estado=" + estado + " indice=" + indice);
                continue;
            }

            e.drawImage(frame,
                    (int)(p.getHitbox().x - ENEMIGO1_DRAWOFFSET_X - OffsetXNivel),
                    (int)(p.getHitbox().y - ENEMIGO1_DRAWOFFSET_Y),
                    (int)(Jugador.SPRITE_W * 1.5f),
                    (int)(Jugador.SPRITE_H * 1.3f),  // mismo tamaño visual que el jugador
                    null);
                    p.pintarHitbox(e, OffsetXNivel);

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

}
