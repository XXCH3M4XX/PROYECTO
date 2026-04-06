package niveles;

import main.Juego;
import utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

public class AjusteNivel {

    private Juego juego;
    private BufferedImage SpriteNivel;

    public AjusteNivel(Juego juego){
        this.juego = juego;
        SpriteNivel = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
    }

    public void draw(Graphics g){
        g.drawImage(SpriteNivel, 0, 0, null);
    }

    public void update(){

    }

}
