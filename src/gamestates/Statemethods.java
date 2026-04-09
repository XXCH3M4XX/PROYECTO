package gamestates;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

//con esta clase crearemos los metodos que queramos implementar en las demas clases y si una clase implementa la clase, debe de tener todos
public interface Statemethods {
    public void update();
    public void draw(Graphics g);
    public void mouseClicked(MouseEvent e);
    public void mousePressed(MouseEvent e);
    public void mouseReleased(MouseEvent e);
    public void mouseMoved(MouseEvent e);
    public void keyPressed(KeyEvent e);
    public void keyReleased(KeyEvent e);
}
