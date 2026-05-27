package gamestates;

import audio.AudioPlayer;
import main.Juego;
import utils.CargaSprites;

import java.awt.*;
import java.awt.image.BufferedImage;

//pantalla de introduccion con fade in, imagen visible y fade out antes del menu
public class CreditosIniciales extends Estado implements MetodosEstadoJuego {

    private BufferedImage imagen;

    //opacidad del rectangulo negro encima de la imagen, 1.0 = negro total, 0.0 = imagen visible
    private float alpha = 1.0f;

    //velocidad a la que cambia el alpha cada update
    private final float velocidadFade = 0.0010f;

    //ticks que la imagen permanece visible entre los dos fades
    private int ticksVisible = 0;
    private final int MAX_TICKS_VISIBLE = 600;

    //fases de la animacion de intro
    private enum Fase { FADE_IN, VISIBLE, FADE_OUT, TERMINADO }
    private Fase faseActual = Fase.FADE_IN;

    public CreditosIniciales(Juego juego) {
        super(juego);
        imagen = CargaSprites.GetSpriteAtlas(CargaSprites.INTRO);
        juego.getAudioPlayer().playCancion(AudioPlayer.creditosIniciales); // ← arranca los creditos
    }

    @Override
    public void update() {
        switch (faseActual) {

            //la imagen aparece poco a poco bajando el alpha del negro
            case FADE_IN:
                alpha -= velocidadFade;
                if (alpha <= 0f) {
                    alpha = 0f;
                    faseActual = Fase.VISIBLE;
                }
                break;

            //la imagen se queda visible durante MAX_TICKS_VISIBLE updates
            case VISIBLE:
                ticksVisible++;
                if (ticksVisible >= MAX_TICKS_VISIBLE) {
                    faseActual = Fase.FADE_OUT;
                }
                break;

            //la imagen desaparece poco a poco subiendo el alpha del negro
            case FADE_OUT:
                alpha += velocidadFade;
                if (alpha >= 1f) {
                    alpha = 1f;
                    faseActual = Fase.TERMINADO;
                }
                break;

            //cuando termina el fade out pasamos al menu
            case TERMINADO:
                EstadoJuego.state = EstadoJuego.MENU;
                juego.getAudioPlayer().playCancion(AudioPlayer.menu);
                break;
        }
    }

    @Override
    public void draw(Graphics g) {
        //dibuja la imagen a pantalla completa
        g.drawImage(imagen, 0, 0, Juego.GAME_WIDTH, Juego.GAME_HEIGHT, null);

        //dibuja el rectangulo negro encima con la opacidad actual para el efecto de fade
        Graphics2D g2d = (Graphics2D) g;
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, Juego.GAME_WIDTH, Juego.GAME_HEIGHT);

        //restaura el composite para no afectar al resto del dibujo
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }

    //permite saltar la intro pulsando cualquier tecla
    @Override
    public void keyPressed(java.awt.event.KeyEvent e) {
        if (e.getKeyCode() == java.awt.event.KeyEvent.VK_E) {
            juego.getAudioPlayer().playCancion(AudioPlayer.menu);
            EstadoJuego.state = EstadoJuego.MENU;
        }
    }

    @Override public void keyReleased(java.awt.event.KeyEvent e) {}
    @Override public void mouseClicked(java.awt.event.MouseEvent e) {}
    @Override public void mousePressed(java.awt.event.MouseEvent e) {}
    @Override public void mouseReleased(java.awt.event.MouseEvent e) {}
    @Override public void mouseMoved(java.awt.event.MouseEvent e) {}
}