package main;

import javax.swing.*;

//esto es como el marco del juego
public class PantallaJuego extends JFrame {

    //vamos a utilizar la clase JFrame
    //es como un lienzo en el que puedes poner lo que quieras
    public PantallaJuego(PanelJuego panelJuego) {
        //tamaño de la pantalla
        this.setSize(400, 400);


        //esto hace que cuando cierres la ventana se acabe la ejecucion
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //tenemos que añadir "la pintura al lienzo"
        this.add(panelJuego);

        //esto hace que el recuadro aparezca en el centro de la pantalla
        this.setLocationRelativeTo(null);

        //tiene que estar al fondo porque si no se buggea con el tamaño y no se nada
        this.setVisible(true);
    }
    //nigger
}
