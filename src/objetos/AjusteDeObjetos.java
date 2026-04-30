package objetos;

import gamestates.Playing;
import niveles.Nivel;
import utils.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utils.Constantes.constantesObjetos.*;

public class AjusteDeObjetos {
    private Playing playing;
    private BufferedImage[][] imagenesPociones, contenedorDeImagenes;
    private ArrayList<Pocion> pociones;
    private ArrayList<ContenedorJuego> contenedores;

    public AjusteDeObjetos(Playing playing){
        this.playing = playing;
        cargarImagenes();
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
        pociones = nuevoNivel.getPocion();
        contenedores = nuevoNivel.getContenedor();
        System.out.println("Pociones: " + pociones.size());
        System.out.println("Contenedores: " + contenedores.size());
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
    }

    public void update(){
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
    }

    public void draw(Graphics g, int xNivelOffset){
        dibujarContenedores(g, xNivelOffset);
        dibujarPociones(g, xNivelOffset);
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
        for(Pocion p : pociones){
            p.reset();
        }
        for (ContenedorJuego cj : contenedores){
            cj.reset();
        }
    }
}
