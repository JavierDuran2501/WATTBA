package mx.itesm.equipo4.wattba;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class PantallaJugar extends Pantalla {
    private static Juego juego;

    //Fondo de la pantalla
    private Texture texturaFondo;
    private float xFondo = 0;

    //Vaquero
    private Vaquero vaquero;
    private float timerAnimaVaquero;
    private final float TIEMPO_FRAME_VAQUERO = 1;

    // Texto
    private Texto texto;        // Dibuja textos en la pantalla
    private float puntos = 0;

    //Epoca
    private Epocas epoca = Epocas.MESOZOICA;

    private Texture texturaGeneral;

    //Obstaculos
    private Obstaculo obstaculo_0;
    private int alturaObstaculo = 0;

    // Textura Dinosaurios
    private Texture texturaDino_0;
    private Texture texturaDino_1;
    private Texture texturaDino_2;
    private Texture texturaDino_3;

    // Arreglo Enemigos
    private Array<Obstaculo> arrObstaculos;
    private float timerCrearObstaculos;
    private float TIEMPO_CREA_OBSTACULOS = 1;
    private float tiempoBase = 1;

    public PantallaJugar(Juego juego) { this.juego = juego;}

    @Override
    public void show() {
        this.texturaFondo = new Texture("Pantallas/Juego.jpg");
        crearVaquero();
        crearObstaculos();
        crearTexto();
        //cargarPuntos();
        crearEnemigos();
        crearTexturas();

        Gdx.input.setInputProcessor(new ProcesadorEntrada());
    }

    private void crearTexturas() {
        texturaDino_0 = new Texture("Obstaculos/tronco.png");
        texturaDino_1 = new Texture("Obstaculos/tronco.png");
        texturaDino_2 = new Texture("Obstaculos/tronco.png");
        texturaDino_3 = new Texture("Obstaculos/tronco.png");

    }

    private void crearEnemigos() {
        //texturaGoomba = new Texture("runner/goomba.png");
        arrObstaculos = new Array<>();
    }

    private void cargarPuntos() {
        Preferences prefs = Gdx.app.getPreferences("marcador");
        puntos = prefs.getFloat("PUNTOS", 0);
    }

    private void crearTexto() {
        texto = new Texto("Fuentes/game.fnt");
    }

    private void crearObstaculos() {
        Texture texturaTronco = new Texture("Obstaculos/Tronco.png");
        obstaculo_0 = new Obstaculo(texturaTronco, ANCHO/2, 0);
    }

    private void crearVaquero() {
      Texture texturaVaquero = new Texture("Vaquero/Correr.png");
      Texture texturaMuriendo = new Texture("Vaquero/Dead__000.png");
      vaquero = new Vaquero(texturaVaquero, texturaMuriendo, 0, 0);
    }

    @Override
    public void render(float delta) {
        // Actualizar objetos
        actualizar();
        verificarChoques();
        //actualizarVaquero(delta);




        borrarPantalla();
        batch.setProjectionMatrix(camara.combined);

        batch.begin();

        //Fondo
        batch.draw(texturaFondo, xFondo,0);
        batch.draw(texturaFondo, xFondo + texturaFondo.getWidth(), 0);

        //Dibujar Enemigos
        dibujarEnemigos();

        //Vaquero
        vaquero.render(batch);

        // Texto
        dibujarTexto();

        //Obstaculos
        //obstaculo_0.render(batch);



        batch.end();
    }

    private void dibujarEnemigos() {
        for (Obstaculo obstaculo: arrObstaculos) {
            obstaculo.render(batch);
            obstaculo.moverIzquierda();
        }
    }

    private void dibujarTexto() {
        //texto.mostrarMensaje(batch, "What a time to be alive", ANCHO/2, 0.9f*ALTO);
        //puntos += Gdx.graphics.getDeltaTime();
        int puntosInt = (int)puntos;
        texto.mostrarMensaje(batch, "" + puntosInt, ANCHO*0.9f, 0.9f*ALTO);
    }

    private void actualizar() {
        // Movimiento del fondo
        xFondo-=5;
        if (xFondo==-texturaFondo.getWidth()) {
            xFondo = 0;
        }

        actualizarObstaculos();

        //Actulizar puntos
        puntos += Gdx.graphics.getDeltaTime();

    }

    private void actualizarObstaculos() {
        timerCrearObstaculos += Gdx.graphics.getDeltaTime();
        if(timerCrearObstaculos >= TIEMPO_CREA_OBSTACULOS){
            timerCrearObstaculos = 0;
            TIEMPO_CREA_OBSTACULOS = tiempoBase + MathUtils.random()*2;
            if(tiempoBase>0){
                tiempoBase -= -0.01f;
            }
            int texturaRandom = MathUtils.random(0,4);
            alturaObstaculo = 0;
            switch (epoca){
                case MESOZOICA:
                    switch(texturaRandom){
                        case 0:
                            texturaGeneral = texturaDino_0;
                            break;
                        case 1:
                            texturaGeneral = texturaDino_1;
                            break;
                        case 2:
                            texturaGeneral = texturaDino_2;
                            break;
                        default:
                            alturaObstaculo = 30;
                            texturaGeneral = texturaDino_3;
                            break;
                    }
                    break;
                case PREHISTORIA:
                    break;
                case EDAD_ANTIGUA:
                    break;
                case EDAD_MEDIA:
                    break;
                case EDAD_MODERNA:
                    break;
                case EDAD_CONTEMPORANEA:
                    break;
                case EDAD_FUTURA:
                    break;
            }

            Obstaculo obstaculo = new Obstaculo(texturaGeneral, ANCHO + 50, alturaObstaculo);
            arrObstaculos.add(obstaculo);
        }

        for (int i = arrObstaculos.size-1; i >= 0; i--) {
            Obstaculo obstaculo = arrObstaculos.get(i);
            if (obstaculo.sprite.getX() < 0 - obstaculo.sprite.getWidth()){
                arrObstaculos.removeIndex(i);
            }
        }
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
        Rectangle rectTronco = obstaculo_0.sprite.getBoundingRectangle();
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
                //vaquero.moverIzquierda();
            }else{
                // derecha
                // vaquero.moverDerecha(); //
                if (vaquero.getEstado() != EstadosVaquero.SALTANDO)
                {
                    vaquero.saltar();
                }

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
