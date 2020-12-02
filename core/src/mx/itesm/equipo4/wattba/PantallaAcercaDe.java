package mx.itesm.equipo4.wattba;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

public class PantallaAcercaDe extends Pantalla {
    private final Juego juego;

    //Fondo de la pantalla
    private Texture texturaFondo;

    //Escena contenedora del boton regresar
    private Stage escenaAcrkd;

    public PantallaAcercaDe(Juego juego) {this.juego = juego;}

    @Override
    public void show() {
        texturaFondo = new Texture("Pantallas/Acercade.png");
        acrkde();
    }

    private void acrkde(){
        escenaAcrkd = new Stage(vista);
        //btnRegresar
        Texture texturaBtnRegresar = new Texture("btnsMenu/btnRegresar.png");
        TextureRegionDrawable trdBtnRegresar = new TextureRegionDrawable(new TextureRegion(texturaBtnRegresar));
        ImageButton btnRegresar = new ImageButton(trdBtnRegresar);
        btnRegresar.setPosition(0,0);

        escenaAcrkd.addActor(btnRegresar);

        Gdx.input.setInputProcessor(escenaAcrkd);

        //Listener del boton regresar
        btnRegresar.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                juego.setScreen(new PantallaCargando(juego, Pantallas.MENU));
            }
        });
    }

    @Override
    public void render(float delta) {
        borrarPantalla();
        batch.setProjectionMatrix(camara.combined);

        batch.begin();
        batch.draw(texturaFondo,0,0);

        batch.end();

        escenaAcrkd.draw();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
