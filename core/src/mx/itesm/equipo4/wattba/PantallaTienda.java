package mx.itesm.equipo4.wattba;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;

public class PantallaTienda extends Pantalla {
    private final Juego juego;
    public PantallaTienda(Juego juego) { this.juego = juego;}

    //Fondo de pantalla
    private Texture texturaFondo;

    @Override
    public void show() {
        texturaFondo = new Texture("Pantallas/Tienda.png");
    }

    @Override
    public void render(float delta) {
        borrarPantalla();
        batch.setProjectionMatrix(camara.combined);

        batch.begin();
        batch.draw(texturaFondo,0,0);
        batch.end();
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
