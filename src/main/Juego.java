package main;

public class Juego {

    private PantallaJuego pantallaJuego;
    private PanelJuego panelJuego;

    public Juego() {
        panelJuego = new PanelJuego();
        pantallaJuego = new PantallaJuego(panelJuego);
        //esto va a leer el input del teclado; es decir, lee las teclas que pulsamos
        panelJuego.requestFocus();


    }
    //nigger
}
