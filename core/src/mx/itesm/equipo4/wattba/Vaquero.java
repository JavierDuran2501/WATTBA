package mx.itesm.equipo4.wattba;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Vaquero extends Objeto
{
    //Animación
    private Animation<TextureRegion> animacion;
    private float timerAnimacion;

    //Velocidad
    private final float DX_VAQUERO = 10;

    //Texturas
    // Texture texturaIdle;
    private Texture texturaMuriendo;

    // Salto
    private float yBase;        // y del piso
    private float tAire;        // tiempo de simulación < tVuelo
    private final float V0 = 100;
    private final float G = 20;
    private float tVuelo;

    private EstadosVaquero estado; // CORRIENDO, SALTANDO, MUERIENDO

    public Vaquero(Texture texturaCorriendo, Texture texturaMuriendo,float x, float y) {
        //super(texturaCorriendo, x, y);
        this.texturaMuriendo = texturaMuriendo;
        TextureRegion region = new TextureRegion(texturaCorriendo);
        TextureRegion[][] texturasFrame = region.split(32, 300);

        sprite = new Sprite(texturasFrame[0][0]);
        sprite.setPosition(x, y);

        //Animación
        TextureRegion[] arrFrames = { texturasFrame[0][1],texturasFrame[0][2],
                texturasFrame[0][3],texturasFrame[0][4],texturasFrame[0][5],
                texturasFrame[0][6],texturasFrame[0][7],texturasFrame[0][8],
                texturasFrame[0][9]};

        animacion = new Animation<TextureRegion>(0.1f, arrFrames);
        animacion.setPlayMode(Animation.PlayMode.LOOP);
        timerAnimacion = 0;

        estado = EstadosVaquero.CORRIENDO;
        //Salto
        yBase = y;

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
        timerAnimacion += delta;
        if (estado==EstadosVaquero.CORRIENDO) {
            TextureRegion frame = animacion.getKeyFrame(timerAnimacion);
            batch.draw(frame, sprite.getX(), sprite.getY());

        } else if (estado == EstadosVaquero.SALTANDO) {
            //Gdx.app.log("SALTA", "tAire: " + tAire);
            tAire += 10*delta;
            float y = yBase + V0*tAire - 0.5f*G*tAire*tAire;
            sprite.setY(y);
            super.render(batch);
            if (tAire>=tVuelo) {
                sprite.setY(yBase);
                estado = EstadosVaquero.CORRIENDO;
            }
        } else {
            // Animación de muerte
        }


    }
}
