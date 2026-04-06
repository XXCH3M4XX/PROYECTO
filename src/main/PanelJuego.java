package main;

import inputs.MouseInputs;
import inputs.KeyboardInputs;
import utils.Constantes;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static main.Juego.GAME_HEIGHT;
import static main.Juego.GAME_WIDTH;
import static utils.Constantes.ConstantesJugador.*;
import static utils.Constantes.Direcciones.*;

//esto es como el lienzo del juego
public class PanelJuego extends JPanel {
    private MouseInputs inputRaton;
    private Juego juego;
    public PanelJuego(Juego juego) {
        inputRaton = new MouseInputs(this);
        this.juego = juego;
        tamañoPantalla();
        setFocusable(true);

        //escuchador de teclado y de raton que sabe cuando lo presionamos, liberamos o mantenemos
        addKeyListener(new KeyboardInputs(this));
        addMouseListener(inputRaton);
        addMouseMotionListener(inputRaton);

    }



    //metodo para ajustar el tamaño de la pantalla, 800p es ideal por el pixelart
    private void tamañoPantalla() {
        Dimension tamaño = new Dimension(GAME_WIDTH, GAME_HEIGHT);
        setPreferredSize(tamaño);
        setMaximumSize(tamaño);
        System.out.println("size: " + GAME_WIDTH + " height: " + GAME_HEIGHT);
    }

    public void updateGame(){

    }


    //con la clase Graphics podemos pintar dentro de la pantalla del juego
    //se tiene que llamar igual que la del padre si no no funciona
    public void paintComponent(Graphics g) {

        //llamamos a jpanel (padre) para que haga lo que necesite
        //si no se hace asi puede dar errores
        super.paintComponent(g);
        //pintamos la imagen
        //el primer parentesis es la columna y fila de la imagen de sprites
        //el resto de valores es la posicion de la imagen en la pantalla y su tamaño
        //hacemos que se pueda mover con xdelta e ydelta
        juego.render(g);

    }



    public Juego getJuego(){
        return juego;
    }

}