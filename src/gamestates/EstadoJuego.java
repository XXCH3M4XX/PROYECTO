package gamestates;

public enum EstadoJuego {

    PLAYING, MENU, OPTIONS, QUIT, INTRO, STATS, NOMBRE, CONTROLES;

    public static EstadoJuego state = INTRO;
}
