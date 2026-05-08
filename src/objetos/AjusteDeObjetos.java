package objetos;

import entidades.EsqueletoHueso;
import entidades.Jugador;
import gamestates.Playing;
import main.Juego;
import niveles.Nivel;
import utils.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import static utils.Constantes.Proyectiles.*;

import static utils.Constantes.constantesObjetos.*;
import static utils.Miscelaneos.*;

public class AjusteDeObjetos {
    private Playing playing;
    private BufferedImage imagenPinchos;
    private BufferedImage[][] imagenesPociones, contenedorDeImagenes;
    private BufferedImage [][] imagenEsqueletoHueso;
    private BufferedImage[][] huesoProyectil;
    private ArrayList<Pocion> pociones;
    private ArrayList<Pocion> porcionesOriginales;
    private ArrayList<ContenedorJuego> contenedores;
    private ArrayList<Pinchos> pinchos;
    private ArrayList<EsqueletoHueso> esqueletosHueso;
    private ArrayList<Proyectil> proyectiles = new ArrayList<>();


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
        for (EsqueletoHueso c : esqueletosHueso) {
            if (c.getHitbox().intersects(hitboxAtaque)) {
                c.recibirGolpe();
                return;
            }
        }
    }

    public void cargarObjetos(Nivel nuevoNivel) {
        pociones = new ArrayList<>(nuevoNivel.getPocion());
        porcionesOriginales = new ArrayList<>(nuevoNivel.getPocion());
        contenedores = new ArrayList<>(nuevoNivel.getContenedor());
        pinchos = nuevoNivel.getPinchos();
        esqueletosHueso = nuevoNivel.getCañon();
        proyectiles.clear();
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

        imagenEsqueletoHueso = new BufferedImage[6][];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.ESQUELETO_HUESO);

        for (int i = 0; i < imagenEsqueletoHueso.length; i++) {
            int frames = getFramesEsqueleto(i);
            imagenEsqueletoHueso[i] = new BufferedImage[frames];
            for (int j = 0; j < frames; j++) {
                imagenEsqueletoHueso[i][j] = temp.getSubimage(j * 72, i * 32, 72, 32);
            }
        }
        //cargar el proyectil
        BufferedImage temp2 = LoadSave.GetSpriteAtlas(LoadSave.HUESO_PROYECTIL);

        huesoProyectil = new BufferedImage[2][6];

        for(int fila = 0; fila < 2; fila++){
            for(int col = 0; col < 6; col++){
                huesoProyectil[fila][col] =
                        temp2.getSubimage(col * 15, fila * 15, 15, 15);
            }
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
        updateEsqueletosHueso(datosNivel, jugador);
        updateProyectiles(datosNivel, jugador);
    }

    private void updateProyectiles(int[][] datosNivel, Jugador jugador) {
        for(Proyectil p : proyectiles){
            if(p.estaActivada()) {
                p.updatePosicion();
                if(p.getHitbox().intersects(jugador.getHitbox())){
                    jugador.cambiarSalud(-25);
                    p.setActivo(false);
                    p.setImpacto(true);
                    p.resetAniIndice(); // ← resetea para que empiece desde el frame 0
                } else if(huesoGolpeaNivel(p, datosNivel)) {
                    p.setActivo(false);
                    p.setImpacto(true);
                    p.resetAniIndice();
                }
            } else if(p.impacto()) {
                p.actualizarAnimacionImpacto(); // ← avanza la animacion de impacto
            }
        }
    }



    private void updateEsqueletosHueso(int[][] datosNivel, Jugador jugador) {
        for(EsqueletoHueso c : esqueletosHueso){
            if (!c.animacion){
                int tileYJugador = (int)(jugador.getHitbox().y / Juego.TILES_SIZE);
                if(c.getDireccionY() == tileYJugador){
                    if(jugadorEstaEnRango(c, jugador)){
                        if(rangoVisionEsqueletoHueso(c, jugador)){
                            boolean puedeVer = esqueletoPuedeVerJudator(datosNivel, jugador.getHitbox(), c.getHitbox(), c.getDireccionY());
                            System.out.println("puedeVer: " + puedeVer);
                            if(puedeVer){
                                c.setEstadoDisparo();
                            }
                        }
                    }
                }
            }

            c.update();
            if (c.debeDispararProyectil()) {
                c.marcarProyectilLanzado();
                int direccion = (jugador.getHitbox().x < c.getHitbox().x) ? -1 : 1;
                c.setUltimaDireccion(direccion); // ← guarda la direccion
                int x = (int)c.getHitbox().x;
                if (direccion == 1) x += c.getHitbox().width;
                proyectiles.add(new Proyectil(x, (int)c.getHitbox().y, direccion));
            }
        }
    }

    private void disparoEsqueletoHueso(EsqueletoHueso c) {
        c.setEstadoDisparo(); // ← antes era c.setAnimacion(true)

        int direccion = (playing.getJugador().getHitbox().x < c.getHitbox().x) ? -1 : 1;
        int x = (int)c.getHitbox().x;
        if(direccion == 1){
            x += c.getHitbox().width;
        }
        proyectiles.add(new Proyectil(x, (int)c.getHitbox().y, direccion));
    }


    private boolean rangoVisionEsqueletoHueso(EsqueletoHueso c, Jugador jugador) {
        // el esqueleto ataca en ambas direcciones
        return true;
    }

    private boolean jugadorEstaEnRango(EsqueletoHueso c, Jugador jugador) {
        int valorAbsoluto = (int) Math.abs(jugador.getHitbox().x - c.getHitbox().x);
        return valorAbsoluto <= Juego.TILES_SIZE * 10; // ← era 5, ahora 10
    }

    public void draw(Graphics g, int xNivelOffset){
        dibujarContenedores(g, xNivelOffset);
        dibujarPociones(g, xNivelOffset);
        dibujarTrampas(g, xNivelOffset);
        dibujarEsqueletosHueso(g, xNivelOffset);
        drawProyectiles(g, xNivelOffset);
    }

    private void drawProyectiles(Graphics g, int xNivelOffset) {
        for(Proyectil p : proyectiles){
            if(p.estaActivada() || p.impacto()){
                int fila = p.impacto() ? 1 : 0;

                // calcula la posicion del sprite compensando el offset de la hitbox
                int spriteX = (int)(p.getHitbox().x - xNivelOffset) - p.getSpriteOffsetX();
                int spriteY = (int)(p.getHitbox().y) - p.getSpriteOffsetY();

                g.drawImage(
                        huesoProyectil[fila][p.getAniIndice()],
                        spriteX,
                        spriteY,
                        HUESO_PROYECTIL_ANCHO,
                        HUESO_PROYECTIL_ALTO,
                        null
                );

            }
        }
    }

    private void dibujarEsqueletosHueso(Graphics g, int xNivelOffset) {
        for (EsqueletoHueso c : esqueletosHueso){
            int xOffset = -35;
            int yOffset = -30;

            int x = (int)(c.getHitbox().x - xNivelOffset);
            int ancho = ANCHO_EH;

            // voltea el sprite segun la ultima direccion de disparo
            if (c.getUltimaDireccion() == -1) {
                x += ancho + xOffset;
                ancho *= -1;
            } else {
                x += xOffset;
            }

            int estadoDibujo = c.getEstado();
            int indiceDibujo = c.getAniIndice();

            if (estadoDibujo == EsqueletoHueso.EN_SUELO) {
                estadoDibujo = EsqueletoHueso.DESCOMPONE;
                indiceDibujo = imagenEsqueletoHueso[estadoDibujo].length - 1;
            }

            g.drawImage(imagenEsqueletoHueso[estadoDibujo][indiceDibujo],
                    x, (int)(c.getHitbox().y) + yOffset,
                    ancho, ALTO_EH, null);
            g.setColor(Color.GREEN);

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
        for (EsqueletoHueso c : esqueletosHueso){
            c.reset();
        }
    }
}
