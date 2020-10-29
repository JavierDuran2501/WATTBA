package mx.itesm.equipo4.wattba;

import com.badlogic.gdx.graphics.Texture;

public class Obstaculo extends Objeto
{
    private Texture textura;
    private int tipo; // 0 = Obstaculo, 1 = Item.

    public Obstaculo(Texture textura, float x, float y, int tipo){
        super(textura, x, y);
        this.tipo = tipo;
    }

    public Obstaculo(Texture textura, float x, float y, int w, int h, int tipo) {
        super(textura, x, y, w, h);
        this.tipo = tipo;
    }

    public void moverIzquierda() {
        sprite.setX(sprite.getX()-10);
    }

    public int getTipo() {
        return tipo;
    }

}
