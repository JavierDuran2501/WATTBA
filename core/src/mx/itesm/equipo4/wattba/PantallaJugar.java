package mx.itesm.equipo4.wattba;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class PantallaJugar extends Pantalla {
    private static Juego juego;

    //Fondo de la pantalla
    private Texture texturaFondo;

    //Vaquero
    private Vaquero vaquero;
    private float timerAnimaVaquero;
    private final float TIEMPO_FRAME_VAQUERO = 1;

    //Obstaculos
    private Obstaculo tronco;

    public PantallaJugar(Juego juego) { this.juego = juego;}

    @Override
    public void show() {
        this.texturaFondo = new Texture("Pantallas/Juego.jpg");
        crearVaquero();
        crearObstaculos();

        Gdx.input.setInputProcessor(new ProcesadorEntrada());
    }

    private void crearObstaculos() {
        Texture texturaTronco = new Texture("Obstaculos/Tronco.png");
        tronco = new Obstaculo(texturaTronco, ANCHO/2, 0);
    }

    private void crearVaquero() {
      Texture texturaVaquero = new Texture("Vaquero/Idle__000.png");
      Texture texturaMuriendo = new Texture("Vaquero/Dead__000.png");
      vaquero = new Vaquero(texturaVaquero, texturaMuriendo, 0, 0);
    }

    @Override
    public void render(float delta) {
        // Actualizar objetos
        verificarChoques();
        //actualizarVaquero(delta);



        borrarPantalla();
        batch.setProjectionMatrix(camara.combined);

        batch.begin();
        //Fondo
        batch.draw(texturaFondo,0,0);

        //Vaquero
        vaquero.render(batch);


        //Obstaculos
        tronco.render(batch);

        batch.end();
    }

    private void actualizarVaquero(float delta) {
        timerAnimaVaquero += delta;
        if(timerAnimaVaquero >= TIEMPO_FRAME_VAQUERO){
            // Checamos cambios de estado

            timerAnimaVaquero = 0;
        }
    }

    private void verificarChoques() {
        Rectangle rectVaquero = vaquero.sprite.getBoundingRectangle();
        Rectangle rectTronco = tronco.sprite.getBoundingRectangle();
        if(rectVaquero.overlaps(rectTronco)){
            // COLISIÓN!!!
            vaquero.setEstado(EstadosVaquero.MURIENDO);
        }
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

    private class ProcesadorEntrada implements InputProcessor
    {
        @Override
        public boolean keyDown(int keycode) {
            return false;
        }

        @Override
        public boolean keyUp(int keycode) {
            return false;
        }

        @Override
        public boolean keyTyped(char character) {
            return false;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            Vector3 v = new Vector3(screenX, screenY, 0);
            camara.unproject(v);

            //Mover al vaquero
            if(v.x<= ANCHO/2){
                // Izq
                vaquero.moverIzquierda();
            }else{
                // derecha
                vaquero.moverDerecha();
            }
            return true;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            return false;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return false;
        }

        @Override
        public boolean scrolled(int amount) {
            return false;
        }
    }
}
