package mx.itesm.equipo4.wattba;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/*
 Representa un elemento gráfico en la pantalla
 */
    public class Objeto {
    protected Sprite sprite; // Las subclases pueden acceder/modificar directamente a sprite


    //Animación
    private Animation<TextureRegion> animacion;
    private float timerAnimacion;

    public Objeto(Texture textura, float x, float y, int w, int h) {

        TextureRegion region = new TextureRegion(textura);
        TextureRegion[][] texturasFrame = region.split(w, h);
        sprite = new Sprite(texturasFrame[0][0]);
        sprite.setPosition(x, y);

        //Animación
        TextureRegion[] arrFrames = {texturasFrame[0][1], texturasFrame[0][2],
                texturasFrame[0][3], texturasFrame[0][4], texturasFrame[0][5],
                texturasFrame[0][6], texturasFrame[0][7]};

        animacion = new Animation<TextureRegion>(0.1f, arrFrames);
        animacion.setPlayMode(Animation.PlayMode.LOOP);
        timerAnimacion = 0;
    }

    public Objeto(Texture textura, float x, float y) {
        sprite = new Sprite(textura);
        sprite.setPosition(x, y);
    }

    public void render(SpriteBatch batch) {
        float delta = Gdx.graphics.getDeltaTime();  // 1/60
        timerAnimacion += delta;
        TextureRegion frame = animacion.getKeyFrame(timerAnimacion);
        batch.draw(frame, sprite.getX(), sprite.getY());
        //sprite.draw(batch);

    }

    public void renderSinAnimacion(SpriteBatch batch){

        sprite.draw(batch);
    }

}
