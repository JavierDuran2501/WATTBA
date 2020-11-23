package mx.itesm.equipo4.wattba;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Vaquero extends Objeto
{
    //Velocidad
    private final float DX_VAQUERO = 10;

    //Texturas
    // Texture texturaIdle;
    private Texture texturaMuriendo;
    private Texture texturaDeslizando;
    private Texture texturaCorrer;

    // Salto
    private float yBase;        // y del piso
    private float tAire;        // tiempo de simulación < tVuelo
    private final float V0 = 135;
    private final float G = 20;
    private float tVuelo;

    private EstadosVaquero estado; // CORRIENDO, SALTANDO, MUERIENDO

    public Vaquero(Texture texturaCorriendo, Texture texturaMuriendo,float x, float y, int w, int h) {
        super(texturaCorriendo, x, y, w, h);
        this.texturaMuriendo = texturaMuriendo;
        estado = EstadosVaquero.CORRIENDO;
        //Salto
        yBase = y;
        this.texturaDeslizando = new Texture("Vaquero/Slide.png");
        this.texturaCorrer = new Texture("Vaquero/Run.png");
    }

    public EstadosVaquero getEstado() {
        return estado;
    }

    public void setEstado(EstadosVaquero estado) {
        this.estado = estado;
        switch (estado) {
            case CORRIENDO:
                //sprite.setTexture(texturaIdle);
                break;
            case MURIENDO:
                sprite.setTexture(texturaMuriendo);
                break;
        }
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
        if (estado==EstadosVaquero.CORRIENDO) {
            super.render(batch);

        } else if (estado == EstadosVaquero.SALTANDO) {
            //Gdx.app.log("SALTA", "tAire: " + tAire);
            tAire += 10*delta;
            float y = yBase + V0*tAire - 0.5f*G*tAire*tAire;
            sprite.setY(y);
            sprite.draw(batch);
            if (tAire>=tVuelo) {
                sprite.setY(yBase);
                estado = EstadosVaquero.CORRIENDO;
            }
        } else if (estado == EstadosVaquero.DESLIZANDO) {
            tAire += 10*delta;
            sprite.setTexture(texturaDeslizando);
            sprite.draw(batch);
            if (tAire>=tVuelo) {
                sprite.setY(yBase);
                estado = EstadosVaquero.CORRIENDO;
                sprite.setTexture(texturaCorrer);
            }
        }else{

        }

    }

    public void deslizar(){
        estado = EstadosVaquero.DESLIZANDO;
        tAire = 0;
        tVuelo = 2*V0/G;
    }

}

