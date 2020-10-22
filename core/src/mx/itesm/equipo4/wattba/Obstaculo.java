package mx.itesm.equipo4.wattba;

import com.badlogic.gdx.graphics.Texture;

public class Obstaculo extends Objeto
{
    private Texture textura;

    public Obstaculo(Texture textura, float x, float y) {
        super(textura, x, y);
    }

    public void moverIzquierda() {
        sprite.setX(sprite.getX()-10);
    }
}
