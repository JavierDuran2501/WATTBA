package mx.itesm.equipo4.wattba;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Vaquero extends Objeto
{
    //Velocidad
    private final float DX_VAQUERO = 10;

    //Texturas
    private Texture texturaIdle;
    private Texture texturaMuriendo;

    // Salto
    private float yBase;        // y del piso
    private float tAire;        // tiempo de simulación < tVuelo
    private final float V0 = 100;
    private final float G = 20;
    private float tVuelo;

    private EstadosVaquero estado; //IDLE, SALTANDO, MUERIENDO

    public Vaquero(Texture textura, float x, float y) {
        super(textura, x, y);
    }

    public Vaquero(Texture texturaIdle, Texture texturaMuriendo, float x, float y) {
        super(texturaIdle, x, y);
        this.texturaIdle = texturaIdle;
        this.texturaMuriendo = texturaMuriendo;
        estado = EstadosVaquero.CAMINANDO;
        //Salto
        yBase = y;
    }

    public EstadosVaquero getEstado() {
        return estado;
    }

    public void setEstado(EstadosVaquero estado) {
        this.estado = estado;
        switch (estado) {
            case CAMINANDO:
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

    public void saltar() {
        estado = EstadosVaquero.SALTANDO;
        tAire = 0;
        tVuelo = 2*V0/G;        // Permanece en el aire
    }

    @Override
    public void render(SpriteBatch batch) {
        //actualizar();
        float delta = Gdx.graphics.getDeltaTime();  // 1/60
        if (estado==EstadosVaquero.CAMINANDO) {

        } else {
            //Gdx.app.log("SALTA", "tAire: " + tAire);
            tAire += 10*delta;
            float y = yBase + V0*tAire - 0.5f*G*tAire*tAire;
            sprite.setY(y);
            super.render(batch);
            if (tAire>=tVuelo) {
                sprite.setY(yBase);
                estado = EstadosVaquero.CAMINANDO;
            }

        }


    }
}
