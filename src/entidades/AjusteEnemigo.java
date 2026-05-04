package entidades;

import gamestates.Playing;
import niveles.Nivel;
import utils.LoadSave;
import static utils.Constantes.constantesDelEnemigo.*;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

//gestiona todos los enemigos del nivel: carga, actualizacion, dibujo y colisiones
public class AjusteEnemigo {

    //referencia al estado de juego para poder notificar eventos como nivel completado
    Playing playing;

    //matriz de frames indexada por [estado][indice], cada fila tiene su propio numero de frames
    private BufferedImage[][] ArrayEnemigo1;

    //lista de enemigos activos en el nivel actual
    private ArrayList<PersonajeEnemigo1> enemigos = new ArrayList<>();

    public AjusteEnemigo(Playing playing) {
        this.playing = playing;
        cargarImagenesEnemigo();
    }

    //carga los enemigos del nivel recibido, se llama al iniciar o cambiar de nivel
    public void cargarEnemigos(Nivel nivel) {
        enemigos = nivel.getEnemigos();
    }

    //actualiza todos los enemigos activos y detecta si el nivel ha sido completado
    public void update(int[][] datosNivel, Jugador jugador) {
        boolean enemigosVivos = false;
        for (PersonajeEnemigo1 p : enemigos) {
            if (p.isActivo()) {
                p.update(datosNivel, jugador);
                enemigosVivos = true;
            }
        }
        //si no queda ningun enemigo vivo el nivel se da por completado
        if (!enemigosVivos) {
            playing.setNivelCompletado(true);
        }
    }

    public void draw(Graphics e, int OffsetXNivel) {
        drawEnemigos(e, OffsetXNivel);
    }

    //dibuja cada enemigo activo usando su frame y estado actual, con volteo horizontal si mira a la izquierda
    private void drawEnemigos(Graphics e, int OffsetXNivel) {
        for (PersonajeEnemigo1 p : enemigos) {
            if (p.isActivo()) {
                int estado = p.getEstadoEnemigo();
                int indice = p.getAniIndice();

                //evita dibujar si el indice esta fuera del array para ese estado
                if (indice >= ArrayEnemigo1[estado].length) continue;

                BufferedImage frame = ArrayEnemigo1[estado][indice];
                if (frame == null) {
                    System.out.println("FRAME NULL → estado=" + estado + " indice=" + indice);
                    continue;
                }

                //calcula la posicion de dibujo restando los offsets visuales y el scroll de camara
                int drawX = (int)(p.getHitbox().x - ENEMIGO1_DRAWOFFSET_X - OffsetXNivel);
                int drawY = (int)(p.getHitbox().y - ENEMIGO1_DRAWOFFSET_Y);

                //el enemigo es mas grande que el jugador, por eso se escala con un factor propio
                int w = (int)(Jugador.SPRITE_W * 1.6f);
                int h = (int)(Jugador.SPRITE_H * 1.3f);

                if (p.mirandoDerecha) {
                    //sprite normal mirando a la derecha
                    e.drawImage(frame, drawX, drawY, w, h, null);
                } else {
                    //voltear horizontalmente dibujando desde el borde derecho con ancho negativo
                    e.drawImage(frame, drawX + w, drawY, -w, h, null);
                }
            }
        }
    }

    //aplica daño al primer enemigo activo cuya hitbox intersecta con el boxAtaque del jugador
    public void golpeEnemigo(Rectangle2D.Float boxAtaque) {
        for (Enemigo e : enemigos) {
            if (e.isActivo()) {
                if (boxAtaque.intersects(e.getHitbox())) {
                    e.daño(10);
                    return;
                }
            }
        }
    }

    //cada fila tiene su propio numero de frames, se carga dinamicamente con getSpriteAmount
    private void cargarImagenesEnemigo() {
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.ENEMIGO1);
        System.out.println("Spritesheet: " + temp.getWidth() + "x" + temp.getHeight());
        ArrayEnemigo1 = new BufferedImage[7][];
        for (int i = 0; i < ArrayEnemigo1.length; i++) {
            int frames = getSpriteAmount(ENEMIGO1, i);
            ArrayEnemigo1[i] = new BufferedImage[frames];
            for (int j = 0; j < frames; j++) {
                int px = j * ENEMIGO1_WIDTH_DEFAULT;
                int py = i * ENEMIGO1_HEIGHT_DEFAULT;
                ArrayEnemigo1[i][j] = temp.getSubimage(px, py,
                        ENEMIGO1_WIDTH_DEFAULT, ENEMIGO1_HEIGHT_DEFAULT);
            }
        }
    }

    //resetea todos los enemigos a su estado inicial, se usa al reiniciar el nivel
    public void resetearTodosEnemigos() {
        for (PersonajeEnemigo1 p : enemigos) {
            p.resetearEnemigo();
        }
    }
}