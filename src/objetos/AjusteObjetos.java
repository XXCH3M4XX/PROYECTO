package objetos;

import audio.AudioPlayer;
import entidades.EnemigoProyectil;
import entidades.Jugador;
import gamestates.Playing;
import main.Juego;
import niveles.Nivel;
import utils.CargaSprites;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import static utils.Constantes.Proyectiles.*;

import static utils.Constantes.constantesObjetos.*;
import static utils.Miscelaneos.*;

//gestiona todos los objetos del nivel: pociones, contenedores, pinchos, esqueletos y proyectiles
public class AjusteObjetos {
    //referencia al estado de juego para acceder al jugador y otros sistemas
    private Playing playing;

    //imagen estatica de los pinchos, no tiene animacion
    private BufferedImage imagenPinchos;

    //frames de animacion de pociones y contenedores, primer indice es el tipo
    private BufferedImage[][] imagenesPociones, contenedorDeImagenes;

    //frames de animacion del esqueleto, primer indice es el estado
    private BufferedImage[][] imagenEsqueletoHueso;

    //frames de animacion del proyectil de hueso, fila 0 en vuelo y fila 1 impacto
    private BufferedImage[][] huesoProyectil;

    //listas de objetos activos en el nivel actual
    private ArrayList<Pocion> pociones;

    //copia original de las pociones para restaurarlas al reiniciar
    private ArrayList<Pocion> porcionesOriginales;
    private ArrayList<ContenedorJuego> contenedores;
    private ArrayList<Pinchos> pinchos;
    private ArrayList<EnemigoProyectil> esqueletosHueso;
    private ArrayList<Proyectil> proyectiles = new ArrayList<>();

    //recibe la referencia al estado de juego y carga todas las imagenes de objetos
    public AjusteObjetos(Playing playing) {
        this.playing = playing;
        cargarImagenes();
    }

    //mata al jugador instantaneamente si su hitbox toca cualquier pincho
    public void checkJugadorTocaPinchos(Jugador j) {
        for (Pinchos p : pinchos) {
            if (p.getHitbox().intersects(j.getHitbox())) {
                j.muerte();
            }
        }
    }

    //desactiva la pocion y aplica su efecto al jugador cuando la hitbox la toca
    public void checkObjetoTocado(Rectangle2D.Float hitbox) {
        for (Pocion p : pociones) {
            if (p.isActiva()) {
                if (hitbox.intersects(p.getHitbox())) {
                    p.setActiva(false);
                    aplicarEfectoAlJugador(p);
                    playing.registrarPocion();
                    System.out.println("Pocion recogida, total: " + playing.getPorcionesRecogidas());
                }
            }
        }
    }

    //aplica el efecto de la pocion al jugador segun su tipo, roja cura y azul da poder
    public void aplicarEfectoAlJugador(Pocion p) {
        if (p.getTipoObjeto() == POCION_ROJA) {
            playing.getJugador().cambiarSalud(VALOR_POCION_ROJA);
        } else {
            playing.getJugador().cambiarPoder(VALOR_POCION_AZUL);
        }
        playing.getJuego().getAudioPlayer().playEfecto(AudioPlayer.pociones);
    }

    //rompe el contenedor golpeado y genera una pocion en su posicion, o golpea al esqueleto
    public void chekGolpeoAlObjeto(Rectangle2D.Float hitboxAtaque) {
        for (ContenedorJuego cj : contenedores) {
            if (cj.isActiva() && !cj.isYaGolpeado()) { // ← comprueba el flag
                if (cj.getHitbox().intersects(hitboxAtaque)) {
                    cj.setAnimacion(true);
                    cj.setYaGolpeado(true); // ← marca como golpeado
                    int tipo = 0;
                    if (cj.getTipoObjeto() == BARRIL) {
                        tipo = 1;
                    }
                    pociones.add(new Pocion(
                            (int)(cj.getHitbox().x + cj.getHitbox().width / 2),
                            (int)(cj.getHitbox().y),
                            tipo));
                    return;
                }
            }
        }
        for (EnemigoProyectil c : esqueletosHueso) {
            if (c.getHitbox().intersects(hitboxAtaque)) {
                c.recibirGolpe();
                return;
            }
        }
    }

    //reemplaza las listas de objetos con los del nuevo nivel y limpia los proyectiles
    public void cargarObjetos(Nivel nuevoNivel) {
        pociones = new ArrayList<>(nuevoNivel.getPocion());
        porcionesOriginales = new ArrayList<>(nuevoNivel.getPocion());
        contenedores = new ArrayList<>(nuevoNivel.getContenedor());
        pinchos = nuevoNivel.getPinchos();
        esqueletosHueso = nuevoNivel.getCañon();
        proyectiles.clear();
    }

    //carga y recorta todos los spritesheets de objetos en sus matrices de frames
    private void cargarImagenes() {
        BufferedImage spritePocion = CargaSprites.GetSpriteAtlas(CargaSprites.POCIONES);
        imagenesPociones = new BufferedImage[2][7];
        for (int j = 0; j < imagenesPociones.length; j++) {
            for (int i = 0; i < imagenesPociones[j].length; i++) {
                imagenesPociones[j][i] = spritePocion.getSubimage(12 * i, 16 * j, 12, 16);
            }
        }

        BufferedImage spriteContenedor = CargaSprites.GetSpriteAtlas(CargaSprites.OBJETOS);
        contenedorDeImagenes = new BufferedImage[2][10];
        for (int j = 0; j < contenedorDeImagenes.length; j++) {
            for (int i = 0; i < contenedorDeImagenes[j].length; i++) {
                contenedorDeImagenes[j][i] = spriteContenedor.getSubimage(40 * i, 30 * j, 40, 30);
            }
        }

        imagenPinchos = CargaSprites.GetSpriteAtlas(CargaSprites.TRAMPA);

        //cada fila del spritesheet del esqueleto corresponde a un estado distinto
        imagenEsqueletoHueso = new BufferedImage[6][];
        BufferedImage temp = CargaSprites.GetSpriteAtlas(CargaSprites.ESQUELETO_HUESO);
        for (int i = 0; i < imagenEsqueletoHueso.length; i++) {
            int frames = getFramesEsqueleto(i);
            imagenEsqueletoHueso[i] = new BufferedImage[frames];
            for (int j = 0; j < frames; j++) {
                imagenEsqueletoHueso[i][j] = temp.getSubimage(j * 72, i * 32, 72, 32);
            }
        }

        //fila 0 es el hueso en vuelo y fila 1 es la animacion de impacto
        BufferedImage temp2 = CargaSprites.GetSpriteAtlas(CargaSprites.HUESO_PROYECTIL);
        huesoProyectil = new BufferedImage[2][6];
        for (int fila = 0; fila < 2; fila++) {
            for (int col = 0; col < 6; col++) {
                huesoProyectil[fila][col] = temp2.getSubimage(col * 15, fila * 15, 15, 15);
            }
        }
    }

    //actualiza todos los objetos activos, esqueletos y proyectiles cada frame
    public void update(int[][] datosNivel, Jugador jugador) {
        for (Pocion p : pociones) {
            if (p.isActiva()) {
                p.update();
            }
        }
        for (ContenedorJuego c : contenedores) {
            if (c.isActiva()) {
                c.update();
            }
        }
        updateEsqueletosHueso(datosNivel, jugador);
        updateProyectiles(datosNivel, jugador);
    }

    //mueve los proyectiles activos y comprueba si golpean al jugador o al nivel
    private void updateProyectiles(int[][] datosNivel, Jugador jugador) {
        for (Proyectil p : proyectiles) {
            if (p.estaActivada()) {
                p.updatePosicion();
                if (p.getHitbox().intersects(jugador.getHitbox())) {
                    jugador.cambiarSalud(-25);
                    p.setActivo(false);
                    p.setImpacto(true);
                    //resetea el indice para que la animacion de impacto empiece desde el frame 0
                    p.resetAniIndice();
                } else if (huesoGolpeaNivel(p, datosNivel)) {
                    p.setActivo(false);
                    p.setImpacto(true);
                    p.resetAniIndice();
                    playing.getJuego().getAudioPlayer().playEfecto(AudioPlayer.huesoProyectil); // ← añade esto

                }
            } else if (p.impacto()) {
                //avanza la animacion de impacto hasta que termina
                p.actualizarAnimacionImpacto();
            }
        }
    }

    //decide si cada esqueleto debe disparar segun la posicion y vision del jugador
    private void updateEsqueletosHueso(int[][] datosNivel, Jugador jugador) {
        for (EnemigoProyectil c : esqueletosHueso) {
            if (!c.animacion) {
                int tileYJugador = (int)(jugador.getHitbox().y / Juego.TILES_SIZE);
                //solo dispara si el jugador esta en la misma fila de tiles que el esqueleto
                if (c.getDireccionY() == tileYJugador) {
                    if (jugadorEstaEnRango(c, jugador)) {
                        if (rangoVisionEsqueletoHueso(c, jugador)) {
                            boolean puedeVer = esqueletoPuedeVerJudator(datosNivel, jugador.getHitbox(), c.getHitbox(), c.getDireccionY());
                            if (puedeVer) {
                                c.setEstadoDisparo();
                            }
                        }
                    }
                }
            }
            // guarda el estado antes del update para detectar transiciones
            int estadoAntes = c.getEstado();

            c.update();
            // reproduce el sonido cuando empieza a descomponerse o recomponerse
            if (estadoAntes != c.getEstado()) {
                if (c.getEstado() == EnemigoProyectil.DESCOMPONE) {
                    playing.getJuego().getAudioPlayer().playEfecto(AudioPlayer.descomponerEsqueleto);
                } else if (c.getEstado() == EnemigoProyectil.REGENERA) {
                    playing.getJuego().getAudioPlayer().playEfecto(AudioPlayer.recomponerEsqueleto);
                }
            }

            //crea el proyectil en la direccion del jugador cuando la animacion lo indica
            if (c.debeDispararProyectil()) {
                c.marcarProyectilLanzado();
                int direccion = (jugador.getHitbox().x < c.getHitbox().x) ? -1 : 1;
                c.setUltimaDireccion(direccion);
                int x = (int)c.getHitbox().x;
                if (direccion == 1) x += c.getHitbox().width;
                proyectiles.add(new Proyectil(x, (int)c.getHitbox().y, direccion));
            }
        }
    }


    //devuelve true siempre porque el esqueleto ataca en ambas direcciones sin restriccion
    private boolean rangoVisionEsqueletoHueso(EnemigoProyectil c, Jugador jugador) {
        return true;
    }

    //devuelve true si el jugador esta dentro del rango de ataque horizontal del esqueleto
    private boolean jugadorEstaEnRango(EnemigoProyectil c, Jugador jugador) {
        int valorAbsoluto = (int) Math.abs(jugador.getHitbox().x - c.getHitbox().x);
        return valorAbsoluto <= Juego.TILES_SIZE * 10;
    }

    //dibuja todos los objetos del nivel en orden correcto con el offset de camara aplicado
    public void draw(Graphics g, int xNivelOffset) {
        dibujarContenedores(g, xNivelOffset);
        dibujarPociones(g, xNivelOffset);
        dibujarTrampas(g, xNivelOffset);
        dibujarEsqueletosHueso(g, xNivelOffset);
        drawProyectiles(g, xNivelOffset);
    }

    //dibuja cada proyectil usando la fila de impacto o de vuelo segun su estado
    private void drawProyectiles(Graphics g, int xNivelOffset) {
        for (Proyectil p : proyectiles) {
            if (p.estaActivada() || p.impacto()) {
                int fila = p.impacto() ? 1 : 0;

                //compensa el offset del sprite respecto a la hitbox para alinearlos visualmente
                int spriteX = (int)(p.getHitbox().x - xNivelOffset) - p.getSpriteOffsetX();
                int spriteY = (int)(p.getHitbox().y) - p.getSpriteOffsetY();

                g.drawImage(
                        huesoProyectil[fila][p.getAniIndice()],
                        spriteX, spriteY,
                        HUESO_PROYECTIL_ANCHO, HUESO_PROYECTIL_ALTO,
                        null);
            }
        }
    }

    //dibuja cada esqueleto espejando el sprite segun la ultima direccion de disparo
    private void dibujarEsqueletosHueso(Graphics g, int xNivelOffset) {
        for (EnemigoProyectil c : esqueletosHueso) {

            //ajusta estos dos valores para mover el sprite respecto a la hitbox
            int spriteOffsetX = -40;
            int spriteOffsetY = -30;

            int x = (int)(c.getHitbox().x - xNivelOffset);
            int ancho = ANCHO_EH;

            if (c.getUltimaDireccion() == -1) {
                x += ancho + spriteOffsetX;
                ancho *= -1;
            } else {
                x += spriteOffsetX;
            }

            int estadoDibujo = c.getEstado();
            int indiceDibujo = c.getAniIndice();

            if (estadoDibujo == EnemigoProyectil.EN_SUELO) {
                estadoDibujo = EnemigoProyectil.DESCOMPONE;
                indiceDibujo = imagenEsqueletoHueso[estadoDibujo].length - 1;
            }

            g.drawImage(imagenEsqueletoHueso[estadoDibujo][indiceDibujo],
                    x, (int)(c.getHitbox().y) + spriteOffsetY,
                    ancho, ALTO_EH, null);

//            //hitbox para depuracion
//            g.setColor(Color.GREEN);
//            g.drawRect(
//                    (int)(c.getHitbox().x - xNivelOffset),
//                    (int)(c.getHitbox().y),
//                    (int)c.getHitbox().width,
//                    (int)c.getHitbox().height
//            );
        }
    }

    //dibuja cada pincho en su posicion compensando el offset de camara
    private void dibujarTrampas(Graphics g, int xNivelOffset) {
        for (Pinchos p : pinchos) {
            g.drawImage(imagenPinchos,
                    (int)(p.getHitbox().x - xNivelOffset),
                    (int)(p.getHitbox().y - p.getyDrawOffset()),
                    PINCHO_WIDTH, PINCHO_HEIGHT, null);
        }
    }

    //dibuja cada pocion activa usando el tipo para seleccionar la fila correcta del atlas
    private void dibujarPociones(Graphics g, int xNivelOffset) {
        for (Pocion p : pociones) {
            if (p.isActiva()) {
                int tipo = 0;
                if (p.getTipoObjeto() == POCION_ROJA) {
                    tipo = 1;
                }
                g.drawImage(imagenesPociones[tipo][p.getAniIndice()],
                        (int)(p.getHitbox().x - p.getxDrawOffset() - xNivelOffset),
                        (int)(p.getHitbox().y - p.getyDrawOffset()),
                        ANCHO_POCION, ALTO_POCION, null);
            }
        }
    }

    //dibuja cada contenedor activo usando el tipo para seleccionar la fila correcta del atlas
    private void dibujarContenedores(Graphics g, int xNivelOffset) {
        for (ContenedorJuego c : contenedores) {
            if (c.isActiva()) {
                int tipo = 0;
                if (c.getTipoObjeto() == BARRIL) {
                    tipo = 1;
                }
                g.drawImage(contenedorDeImagenes[tipo][c.getAniIndice()],
                        (int)(c.getHitbox().x - c.getxDrawOffset() - xNivelOffset),
                        (int)(c.getHitbox().y - c.getyDrawOffset()),
                        ANCHO_CONTENEDOR, ALTO_CONTENEDOR, null);
            }
        }
    }

    //restaura todos los objetos a su estado inicial, usando la copia original para las pociones
    public void resetearTodosLosObjetos() {
        //restaura las pociones desde la copia original para evitar duplicados al reiniciar
        pociones = new ArrayList<>(porcionesOriginales);
        for (Pocion p : pociones) {
            p.reset();
        }
        for (ContenedorJuego cj : contenedores) {
            cj.reset();
        }
        for (EnemigoProyectil c : esqueletosHueso) {
            c.reset();
        }
    }
}