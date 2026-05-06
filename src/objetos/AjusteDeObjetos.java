package objetos;

import entidades.Jugador;
import gamestates.Playing;
import main.Juego;
import niveles.Nivel;
import utils.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utils.Constantes.constantesObjetos.*;
import static utils.Miscelaneos.*;

public class AjusteDeObjetos {
    private Playing playing;
    private BufferedImage imagenPinchos;
    private BufferedImage[][] imagenesPociones, contenedorDeImagenes;
    private BufferedImage [] imagenCañones;
    private ArrayList<Pocion> pociones;
    private ArrayList<Pocion> porcionesOriginales;
    private ArrayList<ContenedorJuego> contenedores;
    private ArrayList<Pinchos> pinchos;
    private ArrayList<Cañon> cañones;


    public AjusteDeObjetos(Playing playing){
        this.playing = playing;
        cargarImagenes();
    }

    public void checkJugadorTocaPinchos(Jugador j){
        for(Pinchos p : pinchos){
            if (p.getHitbox().intersects(j.getHitbox())){
                j.muerte();
            }
        }
    }

    public void checkObjetoTocado(Rectangle2D.Float hitbox){
        for (Pocion p : pociones){
            if (p.isActiva()){
                if (hitbox.intersects(p.getHitbox())){
                    p.setActiva(false);
                    aplicarEfectoAlJugador(p);
                }
            }
        }

    }

    public void aplicarEfectoAlJugador(Pocion p){
        if (p.getTipoObjeto() == POCION_ROJA){
            playing.getJugador().cambiarSalud(VALOR_POCION_ROJA);
        }else {
            playing.getJugador().cambiarPoder(VALOR_POCION_AZUL);
        }
    }

    public void chekGolpeoAlObjeto(Rectangle2D.Float hitboxAtaque){
        for (ContenedorJuego cj: contenedores){
            if (cj.isActiva()){
                if (cj.getHitbox().intersects(hitboxAtaque)){
                    cj.setAnimacion(true);
                    int tipo = 0;
                    if (cj.getTipoObjeto() == BARRIL){
                        tipo = 1;
                    }
                    pociones.add(new Pocion((int)(cj.getHitbox().x + cj.getHitbox().width/2),
                            (int)(cj.getHitbox().y),
                                tipo));
                    return;
                }
            }
        }
    }

    public void cargarObjetos(Nivel nuevoNivel) {
        pociones = new ArrayList<>(nuevoNivel.getPocion());
        porcionesOriginales = new ArrayList<>(nuevoNivel.getPocion());
        contenedores = new ArrayList<>(nuevoNivel.getContenedor());
        pinchos = nuevoNivel.getPinchos();
        cañones = nuevoNivel.getCañon();
    }


    private void cargarImagenes() {
        BufferedImage spritePocion = LoadSave.GetSpriteAtlas(LoadSave.POCIONES);
        imagenesPociones = new BufferedImage[2][7];

        for (int j = 0; j < imagenesPociones.length; j++) {
            for (int i = 0; i < imagenesPociones[j].length; i++) {
                imagenesPociones[j][i] = spritePocion.getSubimage(12*i, 16*j, 12,16);

            }
        }
        BufferedImage spriteContenedor = LoadSave.GetSpriteAtlas(LoadSave.OBJETOS);
        contenedorDeImagenes = new BufferedImage[2][10];

        for (int j = 0; j < contenedorDeImagenes.length; j++) {
            for (int i = 0; i < contenedorDeImagenes[j].length; i++) {
                contenedorDeImagenes[j][i] = spriteContenedor.getSubimage(40*i, 30*j, 40,30);
            }
        }
        imagenPinchos = LoadSave.GetSpriteAtlas(LoadSave.TRAMPA);

        imagenCañones = new BufferedImage[7];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.CAÑON);

        for (int i = 0; i < imagenCañones.length; i++){
            imagenCañones[i] = temp.getSubimage(i * 40, 0, 40, 26);
        }

    }

    public void update(int [][]datosNivel, Jugador jugador){
        for (Pocion p : pociones){
            if(p.isActiva()){
                p.update();
            }
        }
        for (ContenedorJuego c : contenedores){
            if(c.isActiva()){
                c.update();
            }
        }
        actualizarCañones(datosNivel, jugador);
    }

    private void actualizarCañones(int[][] datosNivel, Jugador jugador) {
        for(Cañon c : cañones){
            if (!c.animacion){
                if (c.getDireccionY() == jugador.getDireccionY()){
                    if(jugadorEstaEnRango(c, jugador)){
                        if (estaEnfrenteDelCañon(c, jugador)){
                            if (cañonPuedeVerAlJugador(datosNivel, jugador.getHitbox(), c.getHitbox(), c.getDireccionY())){
                                disparoCañon(c);
                            }
                        }
                    }
                }
            }
            c.update();
        }
    }

    private void disparoCañon(Cañon c) {
        c.setAnimacion(true);
    }


    private boolean estaEnfrenteDelCañon(Cañon c, Jugador jugador) {
        if (c.getTipoObjeto() == CAÑON_IZQUIERDA){
            if (c.getHitbox().x < jugador.getHitbox().x){
                return true;
            }
        } else if (c.getHitbox().x > jugador.getHitbox().x) {
            return true;
        }
        return false;
    }

    private boolean jugadorEstaEnRango(Cañon c, Jugador jugador) {
        int valorAbsoluto = (int) Math.abs(jugador.getHitbox().x - c.getHitbox().x);
        return valorAbsoluto <= Juego.TILES_SIZE * 5;
    }

    public void draw(Graphics g, int xNivelOffset){
        dibujarContenedores(g, xNivelOffset);
        dibujarPociones(g, xNivelOffset);
        dibujarTrampas(g, xNivelOffset);
        dibujarCañones(g, xNivelOffset);
    }

    private void dibujarCañones(Graphics g, int xNivelOffset) {
        for (Cañon c : cañones){
            int x = (int)(c.getHitbox().x - xNivelOffset);
            int ancho = ANCHO_CAÑON;

            if (c.getTipoObjeto() == CAÑON_IZQUIERDA){
                x += ancho;
                ancho *= -1;
            }
            g.drawImage(imagenCañones[c.getAniIndice()], x, (int)(c.getHitbox().y), ancho, ALTO_CAÑON, null);
        }
    }

    private void dibujarTrampas(Graphics g, int xNivelOffset) {
        for(Pinchos p : pinchos){
            g.drawImage(imagenPinchos, (int)(p.getHitbox().x - xNivelOffset), (int)(p.getHitbox().y - p.getyDrawOffset()), PINCHO_WIDTH, PINCHO_HEIGHT, null);
        }
    }

    private void dibujarPociones(Graphics g, int xNivelOffset) {
        for(Pocion p : pociones){
            if(p.isActiva()){
                int tipo = 0;
                if(p.getTipoObjeto() == POCION_ROJA){
                    tipo = 1;
                }
                g.drawImage(imagenesPociones[tipo][p.getAniIndice()],
                        (int)(p.getHitbox().x - p.getxDrawOffset() - xNivelOffset),
                        (int)(p.getHitbox().y - p.getyDrawOffset()),
                        ANCHO_POCION,
                        ALTO_POCION,
                        null
                );
            }
        }
    }

    private void dibujarContenedores(Graphics g, int xNivelOffset) {
        for(ContenedorJuego c : contenedores){
            if(c.isActiva()){
                int tipo = 0;
                if(c.getTipoObjeto() == BARRIL){
                    tipo = 1;
                }
                g.drawImage(contenedorDeImagenes[tipo][c.getAniIndice()],
                        (int)(c.getHitbox().x - c.getxDrawOffset() - xNivelOffset),
                        (int)(c.getHitbox().y - c.getyDrawOffset()),
                        ANCHO_CONTENEDOR,
                        ALTO_CONTENEDOR,
                        null
                );
            }
        }
    }

    public void resetearTodosLosObjetos() {

        //esto hace que no se dupliquen las pociones al morir y reiniciar el nivel
        pociones = new ArrayList<>(porcionesOriginales);
        for(Pocion p : pociones){
            p.reset();
        }
        for (ContenedorJuego cj : contenedores){
            cj.reset();
        }
        for (Cañon c : cañones){
            c.reset();
        }
    }
}
