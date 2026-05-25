package entidades;

import audio.AudioPlayer;
import gamestates.Playing;
import main.Juego;
import niveles.Nivel;
import utils.CargaSprites;
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
    private BufferedImage[][] ArrayDio;

    //lista de enemigos activos en el nivel actual
    private ArrayList<PersonajeEnemigo1> enemigos = new ArrayList<>();
    private ArrayList<BossFinal> enemigos_dio = new ArrayList<>();

    //imagen de la barra de vida de dio
    private BufferedImage imagenBarraVidaDio;

    //posicion y tamaño de la barra de estado de dio en pantalla ya escalados
    //se coloca arriba a la derecha simetrica a la del jugador
    private final int anchoBarraEstadoDio = (int)(304 * Juego.ESCALA);
    private final int altoBarraEstadoDio  = (int)(104  * Juego.ESCALA);
    private final int xBarraEstadoDio = 900;
    private final int yBarraEstadoDio     = (int)(8 * Juego.ESCALA);

    //dimensiones y posicion del segmento rojo que representa la vida actual de dio
    //ajusta xInicio e yInicio segun donde este el hueco en tu imagen
    private final int anchoBarraVidaDio   = (int)(152 * Juego.ESCALA);
    private final int altoBarraVidaDio    = (int)(7   * Juego.ESCALA);
    private final int xInicioBarraVidaDio = (int)(58  * Juego.ESCALA);
    private final int yInicioBarraVidaDio = (int)(45  * Juego.ESCALA);

    //vida maxima de dio, igual que la del jugador
    private final int vidaMaxDio = utils.Constantes.constantesDelEnemigo.getVidaMax(DIO);

    public AjusteEnemigo(Playing playing) {
        this.playing = playing;
        cargarImagenesEnemigo();
        cargarImagenesDio();
        imagenBarraVidaDio = CargaSprites.GetSpriteAtlas(CargaSprites.BARRA_VIDA_DIO);
    }

    //carga los enemigos del nivel recibido, se llama al iniciar o cambiar de nivel
    public void cargarEnemigos(Nivel nivel) {
        enemigos = nivel.getEnemigos();
        enemigos_dio = nivel.getDio();

            for (BossFinal d : enemigos_dio) {
            d.setPlaying(playing);
        }
    }

    //actualiza todos los enemigos activos y detecta si el nivel ha sido completado
    public void update(int[][] datosNivel, Jugador jugador) {
        ArrayList<Enemigo> todosEnemigos = new ArrayList<>();
        todosEnemigos.addAll(enemigos);
        todosEnemigos.addAll(enemigos_dio);

        boolean enemigosVivos = false;
        for (PersonajeEnemigo1 p : enemigos) {
            if (p.isActivo()) {
                p.update(datosNivel, jugador, todosEnemigos);
                enemigosVivos = true;
            }
        }
        for (BossFinal d : enemigos_dio) {
            if (d.isActivo()) {
                d.update(datosNivel, jugador, todosEnemigos);
                enemigosVivos = true;
            }
        }
        if (!enemigosVivos) {
            playing.setNivelCompletado(true);
        }
    }

    public void draw(Graphics e, int OffsetXNivel) {
        drawEnemigos(e, OffsetXNivel);
        drawBarraVidaDio(e);
    }
    public ArrayList<PersonajeEnemigo1> getEnemigos() {
        return enemigos;
    }

    //dibuja la barra de vida de dio solo si hay algun dio en el nivel
    private void drawBarraVidaDio(Graphics g) {
        //solo dibuja si hay algun dio en el nivel
        if (enemigos_dio.isEmpty()) return;

        //usa la vida del primer dio activo, si todos han muerto no dibuja nada
        BossFinal dio = null;
        for (BossFinal d : enemigos_dio) {
            if (d.isActivo()) {
                dio = d;
                break;
            }
        }
        if (dio == null) return;

        //dibuja la imagen de fondo de la barra
        g.drawImage(imagenBarraVidaDio, xBarraEstadoDio, yBarraEstadoDio,
                anchoBarraEstadoDio, altoBarraEstadoDio, null);

        //calcula el ancho del segmento rojo en proporcion a la vida actual
        int anchoVidaActual = (int)((dio.getVidaActual() / (float) vidaMaxDio) * anchoBarraVidaDio);

        g.setColor(new Color(0x5daf03));
        g.fillRect(xInicioBarraVidaDio + xBarraEstadoDio,
                yInicioBarraVidaDio + yBarraEstadoDio,
                anchoVidaActual,
                altoBarraVidaDio);
    }

    //dibuja cada enemigo activo usando su frame y estado actual, con volteo horizontal si mira a la izquierda
    private void drawEnemigos(Graphics e, int OffsetXNivel) {
        for (PersonajeEnemigo1 p : enemigos) {
            if (p.isActivo()) {
                int estado = p.getEstadoEnemigo();
                int indice = p.getAniIndice();

                if (indice >= ArrayEnemigo1[estado].length) continue;

                BufferedImage frame = ArrayEnemigo1[estado][indice];
                if (frame == null) {
                    System.out.println("FRAME NULL → estado=" + estado + " indice=" + indice);
                    continue;
                }

                int drawX = (int)(p.getHitbox().x - ENEMIGO1_DRAWOFFSET_X - OffsetXNivel);
                int drawY = (int)(p.getHitbox().y - ENEMIGO1_DRAWOFFSET_Y);

                int w = (int)(Jugador.SPRITE_W * 1.6f);
                int h = (int)(Jugador.SPRITE_H * 1.3f);

                if (p.mirandoDerecha) {
                    e.drawImage(frame, drawX, drawY, w, h, null);
                } else {
                    e.drawImage(frame, drawX + w, drawY, -w, h, null);
                }
            }
        }

        for (BossFinal d : enemigos_dio) {
            if (d.isActivo()) {
                int estado = d.getEstadoEnemigo();
                int indice = d.getAniIndice();

                if (estado >= ArrayDio.length || indice >= ArrayDio[estado].length) continue;

                BufferedImage frame = ArrayDio[estado][indice];
                if (frame == null) {
                    System.out.println("FRAME NULL DIO → estado=" + estado + " indice=" + indice);
                    continue;
                }

                int drawX = (int)(d.getHitbox().x - DIO_DRAWOFFSET_X - OffsetXNivel);
                int drawY = (int)(d.getHitbox().y - DIO_DRAWOFFSET_Y);

                int w = (int)(DIO_WIDTH_DEFAULT * Juego.ESCALA_JUGADOR);
                int h = (int)(DIO_HEIGHT_DEFAULT * Juego.ESCALA_JUGADOR);


                if (d.mirandoDerecha) {
                    e.drawImage(frame, drawX, drawY, w, h, null);
                } else {
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
                    e.daño(5);
                    if (e.getEstadoEnemigo() == MUERTE) {
                        playing.getJuego().getAudioPlayer().playEfecto(AudioPlayer.enemigoMuere);
                        playing.getJugador().cambiarPoder(50);
                    }
                    return;
                }
            }
        }
        for (BossFinal d : enemigos_dio) {
            if (d.isActivo()) {
                if (boxAtaque.intersects(d.getHitbox())) {
                    d.daño(10);
                    if (d.getEstadoEnemigo() == utils.Constantes.ConstantesDio.MUERTEDIO) {
                        playing.getJuego().getAudioPlayer().playEfecto(AudioPlayer.enemigoMuere);
                        playing.getJugador().cambiarPoder(50);
                    }
                    return;
                }
            }
        }
    }

    //cada fila tiene su propio numero de frames, se carga dinamicamente con getSpriteAmount
    private void cargarImagenesEnemigo() {
        BufferedImage temp = CargaSprites.GetSpriteAtlas(CargaSprites.ENEMIGO1);
        System.out.println("Spritesheet: " + temp.getWidth() + "x" + temp.getHeight());
        ArrayEnemigo1 = new BufferedImage[6][];
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

    //carga el spritesheet de Dio usando GetCantidadSpriteDio para saber los frames de cada fila
    private void cargarImagenesDio() {
        BufferedImage temp = CargaSprites.GetSpriteAtlas(CargaSprites.DIO);
        System.out.println("Spritesheet Dio: " + temp.getWidth() + "x" + temp.getHeight());
        int maxEstado = 9;
        ArrayDio = new BufferedImage[maxEstado][];
        for (int i = 0; i < maxEstado; i++) {
            int frames = utils.Constantes.ConstantesDio.GetCantidadSpriteDio(i);
            if (frames == 0) {
                ArrayDio[i] = new BufferedImage[1];
                continue;
            }
            ArrayDio[i] = new BufferedImage[frames];
            for (int j = 0; j < frames; j++) {
                ArrayDio[i][j] = temp.getSubimage(
                        j * DIO_WIDTH_DEFAULT,
                        i * DIO_HEIGHT_DEFAULT,
                        DIO_WIDTH_DEFAULT,
                        DIO_HEIGHT_DEFAULT
                );
            }
        }
    }

    //resetea todos los enemigos a su estado inicial, se usa al reiniciar el nivel
    public void resetearTodosEnemigos() {
        for (PersonajeEnemigo1 p : enemigos) {
            p.resetearEnemigo();
        }
        for (BossFinal d : enemigos_dio) {
            d.resetearEnemigo();
        }
    }
}