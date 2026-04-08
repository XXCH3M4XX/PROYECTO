package niveles;

import main.Juego;
import utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

public class AjusteNivel {

    private Juego juego;
    private BufferedImage[] spriteNivel;

    public AjusteNivel(Juego juego){
        this.juego = juego;
        //spriteNivel = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
        importarSpritesNivel();
    }

    private void importarSpritesNivel() {
        BufferedImage imagen = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
        spriteNivel = new BufferedImage[48];
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 12; j++) {
                int indice = i*12 + j;
                spriteNivel[indice] = imagen.getSubimage(j*32, i*32, 32, 32);
            }
        }
    }

    public void draw(Graphics g){

        g.drawImage(spriteNivel[2], 0, 0, null);
    }

    public void update(){

    }

}
