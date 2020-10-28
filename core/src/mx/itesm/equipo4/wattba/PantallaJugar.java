package mx.itesm.equipo4.wattba;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class PantallaJugar extends Pantalla {
    private static Juego juego;

    //Fondo de la pantalla
    private Texture texturaFondo;
    private float xFondo = 0;

    // Estado del juego
    private EstadoJuego estado = EstadoJuego.JUGANDO;

    // Pausa
    private EscenaPausa escenaPausa;

    // GameOver
    private EscenaGameOver escenaGameOver;

    // Cámara/Vista HUD/GameOver
    private OrthographicCamera camaraHUD;
    private Viewport vistaHUD;

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

    //Música
    private Music musicaFondo;

    public PantallaJugar(Juego juego) { this.juego = juego;}

    @Override
    public void show() {
        this.texturaFondo = new Texture("Pantallas/Juego.jpg");
        crearVaquero();
        //crearObstaculos();
        crearTexto();
        //cargarPuntos();
        crearArrEnemigos();
        crearTexturas();
        crearHUD();
        crearEscenaPausa();
        crearEscenaGameOver();
        crearAudio();

        Gdx.input.setInputProcessor(new ProcesadorEntrada());
    }

    private void crearAudio() {
        AssetManager manager = new AssetManager();
        manager.load("Musica/musicaMezo.mp3", Music.class);
        manager.finishLoading();
        musicaFondo = manager.get("Musica/musicaMezo.mp3");
        musicaFondo.setVolume(0.5f);
        musicaFondo.setLooping(true);
        musicaFondo.play();
    }

    private void crearEscenaGameOver() {
        escenaGameOver = new EscenaGameOver(vistaHUD, batch);
    }

    private void crearEscenaPausa() {
        escenaPausa = new EscenaPausa(vistaHUD, batch);
    }

    private void crearHUD() {
        camaraHUD = new OrthographicCamera(ANCHO, ALTO);
        camaraHUD.position.set(ANCHO/2, ALTO/2, 0);
        camaraHUD.update();
        vistaHUD = new StretchViewport(ANCHO, ALTO, camaraHUD);
    }

    private void crearTexturas() {
        texturaDino_0 = new Texture("Dinosaurios/Dino00.png");
        texturaDino_1 = new Texture("Dinosaurios/Dino00.png");
        texturaDino_2 = new Texture("Dinosaurios/Dino00.png");
        texturaDino_3 = new Texture("Dinosaurios/Dino00.png");

    }

    private void crearArrEnemigos() {
        arrObstaculos = new Array<>();
    }

    private void cargarPuntos() {
        Preferences prefs = Gdx.app.getPreferences("marcador");
        puntos = prefs.getFloat("PUNTOS", 0);
    }

    private void crearTexto() {
        texto = new Texto("Fuentes/game.fnt");
    }


    private void crearVaquero() {
      Texture texturaVaquero = new Texture("Vaquero/Correr2.png");
      Texture texturaMuriendo = new Texture("Vaquero/Dead__000.png");
      vaquero = new Vaquero(texturaVaquero, texturaMuriendo, 0, 0, 200, 244);
    }

    @Override
    public void render(float delta) {
        // Actualizar objetos
        if (estado == EstadoJuego.JUGANDO){
            actualizar();
            verificarChoques();
        }

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

        // Dibujar el menú de pausa sí esta en PAUSA
        if (estado == EstadoJuego.PAUSADO) {
            batch.setProjectionMatrix(camaraHUD.combined);
            escenaPausa.draw();
            batch.begin();
            batch.end();
        }

        //Dibujar el menú de GameOver si esta en GAME_OVER
        if (estado == EstadoJuego.GAME_OVER) {
            batch.setProjectionMatrix(camaraHUD.combined);
            escenaGameOver.draw();
            batch.begin();
            batch.end();
        }
    }

    private void dibujarEnemigos() {
        for (Obstaculo obstaculo: arrObstaculos) {
            obstaculo.render(batch);
            if (estado == EstadoJuego.JUGANDO)
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

        // Mover a los enemigos
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
            int w = 0, h = 0;
            switch (epoca){
                case MESOZOICA:
                    switch(texturaRandom){
                        case 0:
                            texturaGeneral = texturaDino_0;
                            w = 190;
                            h = 200;
                            break;
                        case 1:
                            texturaGeneral = texturaDino_1;
                            w = 190;
                            h = 200;
                            break;
                        case 2:
                            texturaGeneral = texturaDino_2;
                            w = 190;
                            h = 200;
                            break;
                        default:
                            alturaObstaculo = 30;
                            texturaGeneral = texturaDino_3;
                            w = 190;
                            h = 200;
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

            Obstaculo obstaculo = new Obstaculo(texturaGeneral, ANCHO + 50, alturaObstaculo, w, h);
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
        for (int i = arrObstaculos.size-1; i >= 0 ; i--) {
            Obstaculo obstaculo = arrObstaculos.get(i);
            if (vaquero.sprite.getBoundingRectangle().overlaps(obstaculo.sprite.getBoundingRectangle())){
                // PERDIÓ !!!!!!!!!!!!
                vaquero.sprite.setY(ALTO);
                vaquero.setEstado(EstadosVaquero.MURIENDO);
                arrObstaculos.removeIndex(i);
                estado = EstadoJuego.GAME_OVER;
                Gdx.input.setInputProcessor(escenaGameOver);
                Gdx.app.log("COLISIÓN", "El vaquero choco" + i);
                break;
            }
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
                // Poner Pausa
                if (estado == EstadoJuego.JUGANDO){
                    estado = EstadoJuego.PAUSADO;
                    // Cambiar el InputProcessor
                    Gdx.input.setInputProcessor(escenaPausa);
                    Gdx.app.log("PAUSA", "Cambia a pausado....");
                }else if (estado == EstadoJuego.PAUSADO) {
                    estado = EstadoJuego.JUGANDO;
                    Gdx.app.log("PAUSA", "Cambia a jugando....");
                }
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

    private class EscenaPausa extends Stage {

        public EscenaPausa(Viewport vista, SpriteBatch batch) {
            super(vista, batch);

           /* Texture texturaSprite = new Texture("runner/bolaFuego.png");
            sprite = new Sprite(texturaSprite);
            sprite.setPosition(ANCHO/2, ALTO*0.6f);*/

            Pixmap pixmap = new Pixmap((int)(ANCHO*0.75f), (int)(0.8f*ALTO),
                    Pixmap.Format.RGBA8888);
            pixmap.setColor(0,0,0,0.5f);
            pixmap.fillRectangle(0,0,pixmap.getWidth(), pixmap.getHeight());

            Texture textura = new Texture(pixmap);
            Image imgPausa = new Image(textura);
            imgPausa.setPosition(ANCHO/2 - pixmap.getWidth()/2,
                    ALTO/2 - pixmap.getHeight()/2);

            this.addActor(imgPausa);        // Fondo

            // Botón(es)
            //btnReanudar
            Texture texturaBtnReanudar = new Texture("btnsPausa/btnReanudar.png");
            TextureRegionDrawable trdBtnReanudar = new TextureRegionDrawable(new TextureRegion(texturaBtnReanudar));
            //Retroalimentación
            Texture texturaBtnReanudarRetro = new Texture("btnsPausa/btnReanudarRetro.png");
            TextureRegionDrawable trdBtnReanudarRetro = new TextureRegionDrawable(new TextureRegion(texturaBtnReanudarRetro));
            ImageButton btnReanudar = new ImageButton(trdBtnReanudar,trdBtnReanudarRetro);
            btnReanudar.setPosition(ANCHO/2,ALTO/2, Align.center);

            //btnSalir
            Texture texturaBtnSalir = new Texture("btnsPausa/btnSalir.png");
            TextureRegionDrawable trdBtnSalir = new TextureRegionDrawable(new TextureRegion(texturaBtnSalir));
            //Retroalimentación
            Texture texturaBtnSalirRetro = new Texture("btnsPausa/btnReanudarRetro.png");
            TextureRegionDrawable trdBtnJugarRetro = new TextureRegionDrawable(new TextureRegion(texturaBtnSalirRetro));
            ImageButton btnSalir = new ImageButton(trdBtnSalir,trdBtnJugarRetro);
            btnSalir.setPosition(ANCHO/2,ALTO/2-87, Align.center);

            // Programar listener del botón
            btnReanudar.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    // QUITAR Pausa
                    estado = EstadoJuego.JUGANDO;
                    Gdx.app.log("PAUSA", "Reanuda por el botón de la pausa");
                    Gdx.input.setInputProcessor(new ProcesadorEntrada());
                }
            });

            btnSalir.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    juego.setScreen(new PantallaMenu(juego));
                }
            });

            this.addActor(btnReanudar);
            this.addActor(btnSalir);
        }
    }

    private class EscenaGameOver extends Stage {

        public EscenaGameOver(Viewport vista, SpriteBatch batch) {
            super(vista, batch);

           /* Texture texturaSprite = new Texture("runner/bolaFuego.png");
            sprite = new Sprite(texturaSprite);
            sprite.setPosition(ANCHO/2, ALTO*0.6f);*/

            Pixmap pixmap = new Pixmap((int)(ANCHO*0.75f), (int)(0.8f*ALTO),
                    Pixmap.Format.RGBA8888);
            pixmap.setColor(0,0,0,0.5f);
            pixmap.fillRectangle(0,0,pixmap.getWidth(), pixmap.getHeight());

            Texture textura = new Texture(pixmap);
            Image imgPausa = new Image(textura);
            imgPausa.setPosition(ANCHO/2 - pixmap.getWidth()/2,
                    ALTO/2 - pixmap.getHeight()/2);

            this.addActor(imgPausa);        // Fondo

            // Botón(es)
            //btnAcercaDe
            Texture texturaBtnAcercaDe = new Texture("btnsMenu/btnAcercaDe.png");
            TextureRegionDrawable trdBtnAcercaDe = new TextureRegionDrawable(new TextureRegion(texturaBtnAcercaDe));
            //Retroalimentación
            Texture texturaBtnAcercaDeRetro = new Texture("btnsMenu/btnAcercaDeRetro.png");
            TextureRegionDrawable trdBtnAcercaDeRetro = new TextureRegionDrawable(new TextureRegion(texturaBtnAcercaDeRetro));
            ImageButton btnAcercaDe = new ImageButton(trdBtnAcercaDe,trdBtnAcercaDeRetro);
            btnAcercaDe.setPosition(ANCHO/2,ALTO/2-87, Align.center);

            //btnReiniciar
            Texture texturaBtnReiniciar = new Texture("btnsMenu/btnAcercaDe.png");
            TextureRegionDrawable trdBtnReiniciar= new TextureRegionDrawable(new TextureRegion(texturaBtnReiniciar));
            //Retroalimentación
            Texture texturaBtnReiniciarRetro = new Texture("btnsMenu/btnAcercaDeRetro.png");
            TextureRegionDrawable trdBtnReiniciarRetro = new TextureRegionDrawable(new TextureRegion(texturaBtnReiniciarRetro));
            ImageButton btnReiniciar = new ImageButton(trdBtnReiniciar,trdBtnReiniciarRetro);
            btnReiniciar.setPosition(ANCHO/2,ALTO/2, Align.center);

            // Programar listener del los botones
            //btnAcercaDe
            btnAcercaDe.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    // QUITAR Pausa
                    juego.setScreen(new PantallaMenu(juego));
                }
            });

            //btnReiniciar
            btnReiniciar.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    // QUITAR Pausa
                    juego.setScreen(new PantallaJugar(juego));
                }
            });

            this.addActor(btnAcercaDe);
            this.addActor(btnReiniciar);
        }
    }
}
