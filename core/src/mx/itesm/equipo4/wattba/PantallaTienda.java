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

public class PantallaTienda extends Pantalla {
    private final Juego juego;
    public PantallaTienda(Juego juego) { this.juego = juego;}

    //Fondo de pantalla
    private Texture texturaFondo;

    //Escena contenedora de los botones
    private Stage escenaTnda;

    @Override
    public void show() {
        texturaFondo = new Texture("Pantallas/Tienda.png");
        tnda();
    }

    private void tnda() {
        escenaTnda = new Stage(vista);
        //btnRegresar
        Texture texturaBtnRegresar = new Texture("btnsMenu/btnRegresar.png");
        TextureRegionDrawable trdBtnRegresar = new TextureRegionDrawable(new TextureRegion(texturaBtnRegresar));
        ImageButton btnRegresar = new ImageButton(trdBtnRegresar);
        btnRegresar.setPosition(0,0);

        escenaTnda.addActor(btnRegresar);

        Gdx.input.setInputProcessor(escenaTnda);

        //Listener del boton regresar
        btnRegresar.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                juego.setScreen(new PantallaMenu(juego));
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

        escenaTnda.draw();
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
