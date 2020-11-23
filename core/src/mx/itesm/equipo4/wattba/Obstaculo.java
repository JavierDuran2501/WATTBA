package mx.itesm.equipo4.wattba;

import com.badlogic.gdx.graphics.Texture;

public class Obstaculo extends Objeto
{
    private Texture textura;
    private int tipo; // 0 = Obstaculo, 1 = ItemRojo, 2 = ItemDorado, 3 = ItemAzul, 4 = ItemMalo0, 5 = ItemMalo1

    public Obstaculo(Texture textura, float x, float y, int tipo){
        super(textura, x, y);
        this.tipo = tipo;
    }

    public Obstaculo(Texture textura, float x, float y, int w, int h, int tipo) {
        super(textura, x, y, w, h);
        this.tipo = tipo;
    }

    public void moverIzquierda(int x) {

        sprite.setX(sprite.getX()-x);
    }

    public int getTipo() {
        return tipo;
    }

}
