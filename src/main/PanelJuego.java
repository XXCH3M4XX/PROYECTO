package main;

import inputs.MouseInputs;
import inputs.KeyboardInputs;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

//esto es como el lienzo del juego
public class PanelJuego extends JPanel {

    private MouseInputs inputRaton;

    //la posicion del rectangulo
    private float xDelta = 100, yDelta = 100;

    private BufferedImage imagen;



    public PanelJuego() {

        inputRaton = new MouseInputs(this);
        importarImagen();
        tamañoPantalla();
        setFocusable(true);

        addKeyListener(new KeyboardInputs(this));
        addMouseListener(inputRaton);
        addMouseMotionListener(inputRaton);

    }

    private void importarImagen() {
        InputStream entrada = getClass().getResourceAsStream("/Provisional100.png");
        try {
            imagen = ImageIO.read(entrada);
        }catch(IOException e){
            System.out.println("Error encontrando las imagenes.");
        }
    }

    private void tamañoPantalla() {
        Dimension tamaño = new Dimension(1280, 800);
        setPreferredSize(tamaño);
        setMaximumSize(tamaño);
    }

    //gracias a este metodo se podra mover el rectangulo en una direccion
    public void changeXDelta(int valor) {
        this.xDelta += valor;
    }

    //gracias a este metodo se podra mover el rectangulo en una direccion
    public void changeYDelta(int valor) {
        this.yDelta += valor;
    }

    public void setRectPos(int x, int y) {
        this.xDelta = x;
        this.yDelta = y;
    }

    //con la clase Graphics podemos pintar dentro de la pantalla del juego
    //se tiene que llamar igual que la del padre si no no funciona
    public void paintComponent(Graphics g) {

        //llamamos a jpanel (padre) para que haga lo que necesite
        //si no se hace asi puede dar errores
        super.paintComponent(g);

        g.drawImage(imagen.getSubimage(0,0,32,32), (int)xDelta, (int)yDelta,64,64, null);


    }
}
