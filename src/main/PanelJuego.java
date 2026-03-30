package main;

import inputs.MouseInputs;
import inputs.KeyboardInputs;
import inputs.MouseInputs;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

//esto es como el lienzo del juego
public class PanelJuego extends JPanel {

    private MouseInputs inputRaton;

    //la posicion del rectangulo
    private float xDelta = 100, yDelta = 100;

    //direccion del rectangulo
    private float xDir = 1f, yDir = 1f;
    private Color color;
    private Random random = new Random();

    public PanelJuego() {

        inputRaton = new MouseInputs(this);
        addKeyListener(new KeyboardInputs(this));
        addMouseListener(inputRaton);
        addMouseMotionListener(inputRaton);

    }

    //gracias a este metodo se podra mover el rectangulo en una direccion
    public void changeXDelta(int valor){
        this.xDelta += valor;
    }

    //gracias a este metodo se podra mover el rectangulo en una direccion
    public void changeYDelta(int valor){
        this.yDelta += valor;
    }

    public void setRectPos(int x, int y){
        this.xDelta = x;
        this.yDelta = y;
    }

    //con la clase Graphics podemos pintar dentro de la pantalla del juego
    //se tiene que llamar igual que la del padre si no no funciona
    public void paintComponent(Graphics g) {

        //llamamos a jpanel (padre) para que haga lo que necesite
        //si no se hace asi puede dar errores
        super.paintComponent(g);
        actualizarRectangulo();
        g.setColor(color);

        //esto dibuja el cuadrado tambien por dentro, no solo los bordes
        g.fillRect((int)xDelta, (int)yDelta, 200, 50);

    }

    //Por el momento el movimiento depende de los fps
    private void actualizarRectangulo() {

        //cuando el panel golpea los bordes su direccion se invierte
        //y su color cambia
        xDelta+= xDir;
        if((xDelta >= 400) || (xDelta <0)) {
            xDir*=-1;
            color = colorRandom();

        }
        yDelta+=yDir;
        if((yDelta >= 400) || (yDelta <0)) {
            yDir*=-1;
            color = colorRandom();
        }
    }
    //este metodo crea un color random y se llama cada vez que
    //el rectangulo golpea un borde
    private Color colorRandom() {
        int rojo = random.nextInt(255);
        int verde = random.nextInt(255);
        int azul = random.nextInt(255);

        return new Color(rojo, verde, azul);
    }
    //nigger
}
