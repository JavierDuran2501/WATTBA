package mx.itesm.equipo4.wattba;

import com.badlogic.gdx.graphics.Texture;

public class Vaquero extends Objeto
{
    //Velocidad
    private final float DX_VAQUERO = 10;

    //Texturas
    private Texture texturaIdle;
    private Texture texturaMuriendo;

    private EstadosVaquero estado; //IDLE, SALTANDO, MUERIENDO

    public Vaquero(Texture textura, float x, float y) {
        super(textura, x, y);
    }

    public Vaquero(Texture texturaIdle, Texture texturaMuriendo, float x, float y) {
        super(texturaIdle, x, y);
        this.texturaIdle = texturaIdle;
        this.texturaMuriendo = texturaMuriendo;
        estado = EstadosVaquero.IDLE;
    }

    public EstadosVaquero getEstado() {
        return estado;
    }

    public void setEstado(EstadosVaquero estado) {
        this.estado = estado;
        switch (estado) {
            case IDLE:
                sprite.setTexture(texturaIdle);
                break;
            case MURIENDO:
                sprite.setTexture(texturaMuriendo);
                break;
        }
    }

    public void moverIzquierda() {
        sprite.setX(sprite.getX() - DX_VAQUERO);
    }

    public void moverDerecha(){
        sprite.setX(sprite.getX() + DX_VAQUERO);
    }
}
