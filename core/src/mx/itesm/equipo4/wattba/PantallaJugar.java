package mx.itesm.equipo4.wattba;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
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

    // Texturas de epoca
    private Texture texturaMezesoica;
    private Texture texturaEdadAntigua;
    private Texture texturaPrehistoria;
    private Texture texturaEdadMedieval;

    //Botón de pausa
    private Texture texturaBtnPausa;

    // Estado del juego
    private EstadoJuego estado = EstadoJuego.JUGANDO;

    private Stage escenaJuego;

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

    //Poderes
    private boolean vidaExtra = false;

    // Texto
    private Texto texto;        // Dibuja textos en la pantalla
    private float puntos = 0;

    //Epoca
    private Epocas epoca = Epocas.MESOZOICA;

    //Obstaculos
    private int alturaObstaculo = 0;
    private Texture texturaGeneral;

    //Items
    private Texture texturaItemRojo;
    private Texture texturaItemAzul;
    private Texture texturaItemDorado;
    private Texture texturaItemMalo0;
    private Texture texturaItemMalo1;

    // Puntos Extra
    private float puntosExtra = 0;

    // Textura Dinosaurios
    private Texture texturaDino_0;
    private Texture texturaDino_1;
    private Texture texturaDino_2;
    private Texture texturaDino_3;

    // Velocidad Enemigos
    private int velocidadObstaculos = 10;

    // Arreglo Enemigos
    private Array<Obstaculo> arrObstaculos;
    private float timerCrearObstaculos;
    private float TIEMPO_CREA_OBSTACULOS = 1;
    private float tiempoBase = 1.5f;

    //Música y sonidos
    private Music musicaGeneral;
    private Music musicaMezo;
    private Music musicaPrehistoria;
    private Music musicaAntigua;
    private Music musicaMedieval;
    private Sound sonidoSalto;
    private Sound sonidoSlide;
    private Sound sonidoItem;
    private Sound sonidoItemMalo;
    private Sound sonidoCambioEpoca;
    private Sound sonidoPausa;

    // AUmento puntos/velocidad enemigos
    private boolean aumento = false;


    public PantallaJugar(Juego juego) { this.juego = juego;}

    @Override
    public void show() {
        crearBtnPausa();
        crearFondos();
        crearVaquero();
        crearTexto();
        crearArrEnemigos();
        crearTexturas();
        crearHUD();
        crearEscenaPausa();
        crearEscenaGameOver();
        crearAudio();
        controlMusica();

        Gdx.input.setInputProcessor(new ProcesadorEntrada());
    }

    private void crearBtnPausa() {
        texturaBtnPausa = new Texture("btnsPausa/btnPausa.png");
    }

    private void crearFondos() {
        this.texturaMezesoica = juego.getManager().get("Pantallas/Juego.jpg");
        texturaEdadAntigua = juego.getManager().get("Pantallas/edadAntiguaFondo.jpg");
        texturaPrehistoria = juego.getManager().get("Pantallas/Prehistoria.jpg");
        texturaEdadMedieval = juego.getManager().get("Pantallas/EdadMedieval.jpg");
        texturaFondo = texturaMezesoica;
    }

    private void crearAudio() {
        musicaMezo = juego.getManager().get("Musica/musicaMezo.mp3");
        musicaPrehistoria = juego.getManager().get("Musica/musicaPrehistoria.mp3");
        musicaAntigua = juego.getManager().get("Musica/musicaAntigua.mp3");
        musicaMedieval = juego.getManager().get("Musica/musicaMedieval.mp3");

        musicaPrehistoria.setVolume(0.1f);
        musicaPrehistoria.setLooping(true);

        musicaAntigua.setVolume(0.1f);
        musicaAntigua.setLooping(true);

        musicaMedieval.setVolume(0.1f);
        musicaMedieval.setLooping(true);

        musicaGeneral = musicaMezo;
        musicaGeneral.setVolume(0.1f);
        musicaGeneral.setLooping(true);

        sonidoSalto = juego.getManager().get("Sonidos/sonidoSalto.mp3");
        sonidoSlide = juego.getManager().get("Sonidos/sonidoSlide.mp3");
        sonidoItem  = juego.getManager().get("Sonidos/sonidoItem.mp3");
        sonidoItemMalo = juego.getManager().get("Sonidos/sonidoItemMalo.mp3");
        sonidoPausa = juego.getManager().get("Sonidos/pausa.mp3");
        sonidoCambioEpoca = juego.getManager().get("Sonidos/sonidoCambioEpoca.mp3");

        /*if (this.epoca == Epocas.PREHISTORIA){
            musicaGeneral = musicaPrehistoria;
        }*/

    }

    private void controlMusica() {
        Preferences pref = Gdx.app.getPreferences("sonido");
        if (pref.getBoolean("PLAY")){
            musicaGeneral.play();
        }else {
            musicaGeneral.stop();
        }
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
        // Dinosaurios
        texturaDino_0 = juego.getManager().get("Dinosaurios/Volador0.png");
        texturaDino_1 = juego.getManager().get("Dinosaurios/Dino000.png");
        texturaDino_2 = juego.getManager().get("EdadAntigua/Minotauro.png");
        texturaDino_3 = juego.getManager().get("Dinosaurios/Dino001.png");

        //Items
        texturaItemRojo = juego.getManager().get("Items/ItemRojo.png");
        texturaItemAzul = juego.getManager().get("Items/ItemAzul.png");
        texturaItemDorado = juego.getManager().get("Items/ItemDorado.png");
        texturaItemMalo0 = juego.getManager().get("Items/ItemMalo0.png");
        texturaItemMalo1 = juego.getManager().get("Items/ItemMalo1.png");



    }

    private void crearArrEnemigos() {
        arrObstaculos = new Array<>();
    }


    private void crearTexto() {
        texto = new Texto("Fuentes/game.fnt");
    }


    private void crearVaquero() {
      //Texture texturaVaquero = new Texture("Vaquero/Correr2.png");
      //Texture texturaMuriendo = new Texture("Vaquero/Dead__000.png");
      Texture texturaVaquero = juego.getManager().get("Vaquero/Correr2.png");
      Texture texturaMuriendo = juego.getManager().get("Vaquero/Dead.png");
      vaquero = new Vaquero(texturaVaquero, texturaMuriendo, 0, 0, 200, 244);
    }

    @Override
    public void render(float delta) {
        // Actualizar objetos
        if (estado == EstadoJuego.JUGANDO){
            actualizar();
            verificarChoques();
        }else{
        }
        cambioEpoca();

        borrarPantalla();
        batch.setProjectionMatrix(camara.combined);

        batch.begin();

        //Fondo
        batch.draw(texturaFondo, xFondo,0);
        batch.draw(texturaFondo, xFondo + texturaFondo.getWidth(), 0);

        //Botón pausa
        batch.draw(texturaBtnPausa, texturaBtnPausa.getWidth(), ALTO*0.85f);

        //Dibujar Enemigos
        dibujarEnemigos();

        //Vaquero
        dibujarVaquero();

        // Texto
        dibujarTexto();

        //Obstaculos

        batch.end();

        // Dibujar el menú de pausa sí esta en PAUSA
        if (estado == EstadoJuego.PAUSADO) {
            batch.setProjectionMatrix(camaraHUD.combined);
            escenaPausa.draw();
            batch.begin();
            texto.mostrarMensaje(batch, "Pausa",ANCHO/2,
                    ALTO*0.70f);
            batch.end();
        }

        //Dibujar el menú de GameOver si esta en GAME_OVER
        if (estado == EstadoJuego.GAME_OVER) {
            batch.setProjectionMatrix(camaraHUD.combined);
            escenaGameOver.draw();
            batch.begin();
            texto.mostrarMensaje(batch, "Game Over",ANCHO/2,
                    ALTO*0.70f);
            batch.end();
        }

        //escenaJuego.draw();
    }

    private void dibujarVaquero() {
        if(estado==EstadoJuego.JUGANDO){
            vaquero.render(batch);
        }
        else{
            vaquero.renderSinAnimacion(batch);
        }
    }

    private void cambioEpoca() {
        if(puntos >= 50 && puntos < 125){  //Prehistoria
            epoca = Epocas.PREHISTORIA;
            texturaFondo = texturaPrehistoria;
            if (!aumento){
                aumentarVelocidadOBstaculo(2);
                vaquero.moverVaquero();
                musicaGeneral.stop();
                musicaGeneral = musicaPrehistoria;
                controlMusica();
                aumento = true;
            }

        }else if(puntos >= 125 && puntos < 250){ // Edad antigua
            epoca = Epocas.EDAD_ANTIGUA;
            texturaFondo = texturaEdadAntigua;
            if (aumento){
                aumentarVelocidadOBstaculo(2);
                aumentarPuntosExtra(0.0166666666666666666f);
                vaquero.moverVaquero();
                musicaGeneral.stop();
                musicaGeneral = musicaAntigua;
                controlMusica();
                aumento = false;
            }
        }else if(puntos >= 250 && puntos < 400){ // Edad Media
            epoca = Epocas.EDAD_ANTIGUA;
            texturaFondo = texturaEdadMedieval;
            if (!aumento){
                aumentarVelocidadOBstaculo(1);
                vaquero.moverVaquero();
                musicaGeneral.stop();
                musicaGeneral = musicaMedieval;
                controlMusica();
                aumento = true;
            }
        }else if(puntos >= 400 && puntos < 700){ // Edad Mezosoica
            epoca = Epocas.MESOZOICA;
            texturaFondo = texturaMezesoica;
            if (aumento){
                aumentarPuntosExtra(0.0166666666666666666f);
                vaquero.moverVaquero();
                aumentarVelocidadOBstaculo(1);
                vaquero.moverVaquero();
                musicaGeneral.stop();
                musicaGeneral = musicaMezo;
                controlMusica();
                aumento = false;
            }
        }else if(puntos >= 700 && puntos < 1000){ // Edad Prehistoria
            epoca = Epocas.PREHISTORIA;
            texturaFondo = texturaPrehistoria;
            if (!aumento){
                aumentarVelocidadOBstaculo(1);
                vaquero.moverVaquero();
                musicaGeneral.stop();
                musicaGeneral = musicaPrehistoria;
                controlMusica();
                aumento = true;
            }
        }else if(puntos >= 1000 && puntos < 1500){ // Edad Antigua
            epoca = Epocas.EDAD_ANTIGUA;
            texturaFondo = texturaEdadAntigua;
            if (aumento){
                aumentarVelocidadOBstaculo(1);
                aumentarPuntosExtra(0.0166666666666666666f);
                vaquero.moverVaquero();
                musicaGeneral.stop();
                musicaGeneral = musicaAntigua;
                controlMusica();
                aumento = false;
            }
        }else if(puntos >= 1500){ // Edad Media
            epoca = Epocas.EDAD_ANTIGUA;
            texturaFondo = texturaEdadMedieval;
            if (!aumento){
                aumentarPuntosExtra(0.0166666666666666666f);
                aumentarVelocidadOBstaculo(1);
                vaquero.moverVaquero();
                musicaGeneral.stop();
                musicaGeneral = musicaMedieval;
                controlMusica();
                aumento = true;
            }
        }
    }


    private void aumentarVelocidadOBstaculo(int i) {
        velocidadObstaculos += i;
    }

    private void aumentarPuntosExtra(float i) {
        puntosExtra += i;
    }

    private void dibujarEnemigos() {
        for (Obstaculo obstaculo: arrObstaculos) {
            if (obstaculo.getTipo() == 0 && estado == EstadoJuego.JUGANDO)
                obstaculo.render(batch);
            else
                obstaculo.renderSinAnimacion(batch);
            if (estado == EstadoJuego.JUGANDO)
                obstaculo.moverIzquierda(velocidadObstaculos);
        }
    }

    private void dibujarTexto() {
        int puntosInt = (int)puntos;
        texto.mostrarMensaje(batch, "" + puntosInt, ANCHO*0.9f, 0.9f*ALTO);
        if(vidaExtra){
            texto.mostrarMensaje(batch, "Tienes vida extra", ANCHO/2,ALTO*0.9f);
        }
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
        puntos += (Gdx.graphics.getDeltaTime());
        puntos += puntosExtra;

    }

    private void actualizarObstaculos() {
        timerCrearObstaculos += Gdx.graphics.getDeltaTime();
        if(timerCrearObstaculos >= TIEMPO_CREA_OBSTACULOS){
            timerCrearObstaculos = 0;
            TIEMPO_CREA_OBSTACULOS = tiempoBase + MathUtils.random()*2;
            if(tiempoBase>0){
                tiempoBase -= -0.01f;
            }

            int texturaRandom = MathUtils.random(0,3);
            alturaObstaculo = 0;
            int w = 0, h = 0;
            switch (epoca){
                case MESOZOICA:
                    switch(texturaRandom){
                        case 0:
                            if (MathUtils.random(0,2) == 0){
                                int item = MathUtils.random(0,4);
                                if (item == 0){
                                    texturaRandom = 10;
                                }else if (item == 1 && vidaExtra == false){
                                    texturaRandom = 11;
                                }else if (item == 2){
                                    texturaRandom = 12;
                                }else if(item == 3){
                                    texturaRandom = 13;
                                }else{
                                    texturaRandom = 14;
                                }
                                Gdx.app.log("ITEM", ""+ texturaRandom);
                            }else {
                                texturaGeneral = texturaDino_0;
                                alturaObstaculo = 200;
                                w = 150;
                                h = 133;
                            }
                            break;
                        case 1:
                            if (MathUtils.random(0,2) == 0){
                                int item = MathUtils.random(0,4);
                                if (item == 0){
                                    texturaRandom = 10;
                                }else if (item == 1 && vidaExtra == false){
                                    texturaRandom = 11;
                                }else if (item == 2){
                                    texturaRandom = 12;
                                }else if(item == 3){
                                    texturaRandom = 13;
                                }else{
                                    texturaRandom = 14;
                                }
                                Gdx.app.log("ITEM", ""+ texturaRandom);
                            }else{
                                texturaGeneral = texturaDino_1;
                                w = 286;
                                h = 300;
                                break;
                            }
                            break;
                        case 2:
                            if (MathUtils.random(0,2) == 0){
                                int item = MathUtils.random(0,4);
                                if (item == 0){
                                    texturaRandom = 10;
                                }else if (item == 1 && vidaExtra == false){
                                    texturaRandom = 11;
                                }else if (item == 2){
                                    texturaRandom = 12;
                                }else if(item == 3){
                                    texturaRandom = 13;
                                }else{
                                    texturaRandom = 14;
                                }
                                Gdx.app.log("ITEM", ""+ texturaRandom);
                            }else {
                                texturaGeneral = texturaDino_2;
                                w = 240;
                                h = 208;
                            }
                            break;
                        default:
                            if (MathUtils.random(0,2) == 0){
                                int item = MathUtils.random(0,4);
                                if (item == 0){
                                    texturaRandom = 10;
                                }else if (item == 1 && vidaExtra == false){
                                    texturaRandom = 11;
                                }else if (item == 2){
                                    texturaRandom = 12;
                                }else if(item == 3){
                                    texturaRandom = 13;
                                }else{
                                    texturaRandom = 14;
                                }
                                Gdx.app.log("ITEM", ""+ texturaRandom);
                            }else{
                                texturaGeneral = texturaDino_3;
                                w = 370;
                                h = 220;
                                break;
                            }

                    }
                    break;
                case PREHISTORIA:
                    switch(texturaRandom){
                        case 0:
                            if (MathUtils.random(0,2) == 0){
                                int item = MathUtils.random(0,4);
                                if (item == 0){
                                    texturaRandom = 10;
                                }else if (item == 1 && vidaExtra == false){
                                    texturaRandom = 11;
                                }else if (item == 2){
                                    texturaRandom = 12;
                                }else if(item == 3){
                                    texturaRandom = 13;
                                }else{
                                    texturaRandom = 14;
                                }
                                Gdx.app.log("ITEM", ""+ texturaRandom);
                            }else {
                                texturaGeneral = texturaDino_0;
                                alturaObstaculo = 200;
                                w = 150;
                                h = 133;
                            }
                            break;
                        case 1:
                            if (MathUtils.random(0,2) == 0){
                                int item = MathUtils.random(0,4);
                                if (item == 0){
                                    texturaRandom = 10;
                                }else if (item == 1 && vidaExtra == false){
                                    texturaRandom = 11;
                                }else if (item == 2){
                                    texturaRandom = 12;
                                }else if(item == 3){
                                    texturaRandom = 13;
                                }else{
                                    texturaRandom = 14;
                                }
                                Gdx.app.log("ITEM", ""+ texturaRandom);
                            }else{
                                texturaGeneral = texturaDino_1;
                                w = 286;
                                h = 300;
                                break;
                            }
                            break;
                        case 2:
                            if (MathUtils.random(0,2) == 0){
                                int item = MathUtils.random(0,4);
                                if (item == 0){
                                    texturaRandom = 10;
                                }else if (item == 1 && vidaExtra == false){
                                    texturaRandom = 11;
                                }else if (item == 2){
                                    texturaRandom = 12;
                                }else if(item == 3){
                                    texturaRandom = 13;
                                }else{
                                    texturaRandom = 14;
                                }
                                Gdx.app.log("ITEM", ""+ texturaRandom);
                            }else {
                                texturaGeneral = texturaDino_2;
                                w = 240;
                                h = 208;
                            }
                            break;
                        default:
                            if (MathUtils.random(0,2) == 0){
                                int item = MathUtils.random(0,4);
                                if (item == 0){
                                    texturaRandom = 10;
                                }else if (item == 1 && vidaExtra == false){
                                    texturaRandom = 11;
                                }else if (item == 2){
                                    texturaRandom = 12;
                                }else if(item == 3){
                                    texturaRandom = 13;
                                }else{
                                    texturaRandom = 14;
                                }
                                Gdx.app.log("ITEM", ""+ texturaRandom);
                            }else{
                                texturaGeneral = texturaDino_3;
                                w = 370;
                                h = 220;
                                break;
                            }

                    }
                    break;
                case EDAD_ANTIGUA:
                    switch(texturaRandom){
                        case 0:
                            if (MathUtils.random(0,2) == 0){
                                int item = MathUtils.random(0,4);
                                if (item == 0){
                                    texturaRandom = 10;
                                }else if (item == 1 && vidaExtra == false){
                                    texturaRandom = 11;
                                }else if (item == 2){
                                    texturaRandom = 12;
                                }else if(item == 3){
                                    texturaRandom = 13;
                                }else{
                                    texturaRandom = 14;
                                }
                                Gdx.app.log("ITEM", ""+ texturaRandom);
                            }else {
                                texturaGeneral = texturaDino_0;
                                alturaObstaculo = 200;
                                w = 150;
                                h = 133;
                            }
                            break;
                        case 1:
                            if (MathUtils.random(0,2) == 0){
                                int item = MathUtils.random(0,4);
                                if (item == 0){
                                    texturaRandom = 10;
                                }else if (item == 1 && vidaExtra == false){
                                    texturaRandom = 11;
                                }else if (item == 2){
                                    texturaRandom = 12;
                                }else if(item == 3){
                                    texturaRandom = 13;
                                }else{
                                    texturaRandom = 14;
                                }
                                Gdx.app.log("ITEM", ""+ texturaRandom);
                            }else{
                                texturaGeneral = texturaDino_1;
                                w = 286;
                                h = 300;
                                break;
                            }
                            break;
                        case 2:
                            if (MathUtils.random(0,2) == 0){
                                int item = MathUtils.random(0,4);
                                if (item == 0){
                                    texturaRandom = 10;
                                }else if (item == 1 && vidaExtra == false){
                                    texturaRandom = 11;
                                }else if (item == 2){
                                    texturaRandom = 12;
                                }else if(item == 3){
                                    texturaRandom = 13;
                                }else{
                                    texturaRandom = 14;
                                }
                                Gdx.app.log("ITEM", ""+ texturaRandom);
                            }else {
                                texturaGeneral = texturaDino_2;
                                w = 240;
                                h = 208;
                            }
                            break;
                        default:
                            if (MathUtils.random(0,2) == 0){
                                int item = MathUtils.random(0,4);
                                if (item == 0){
                                    texturaRandom = 10;
                                }else if (item == 1 && vidaExtra == false){
                                    texturaRandom = 11;
                                }else if (item == 2){
                                    texturaRandom = 12;
                                }else if(item == 3){
                                    texturaRandom = 13;
                                }else{
                                    texturaRandom = 14;
                                }
                                Gdx.app.log("ITEM", ""+ texturaRandom);
                            }else{
                                texturaGeneral = texturaDino_3;
                                w = 370;
                                h = 220;
                                break;
                            }

                    }
                    break;
                case EDAD_MEDIA:
                    switch(texturaRandom){
                        case 0:
                            if (MathUtils.random(0,2) == 0){
                                int item = MathUtils.random(0,4);
                                if (item == 0){
                                    texturaRandom = 10;
                                }else if (item == 1 && vidaExtra == false){
                                    texturaRandom = 11;
                                }else if (item == 2){
                                    texturaRandom = 12;
                                }else if(item == 3){
                                    texturaRandom = 13;
                                }else{
                                    texturaRandom = 14;
                                }
                                Gdx.app.log("ITEM", ""+ texturaRandom);
                            }else {
                                texturaGeneral = texturaDino_0;
                                alturaObstaculo = 200;
                                w = 150;
                                h = 133;
                            }
                            break;
                        case 1:
                            if (MathUtils.random(0,2) == 0){
                                int item = MathUtils.random(0,4);
                                if (item == 0){
                                    texturaRandom = 10;
                                }else if (item == 1 && vidaExtra == false){
                                    texturaRandom = 11;
                                }else if (item == 2){
                                    texturaRandom = 12;
                                }else if(item == 3){
                                    texturaRandom = 13;
                                }else{
                                    texturaRandom = 14;
                                }
                                Gdx.app.log("ITEM", ""+ texturaRandom);
                            }else{
                                texturaGeneral = texturaDino_1;
                                w = 286;
                                h = 300;
                                break;
                            }
                            break;
                        case 2:
                            if (MathUtils.random(0,2) == 0){
                                int item = MathUtils.random(0,4);
                                if (item == 0){
                                    texturaRandom = 10;
                                }else if (item == 1 && vidaExtra == false){
                                    texturaRandom = 11;
                                }else if (item == 2){
                                    texturaRandom = 12;
                                }else if(item == 3){
                                    texturaRandom = 13;
                                }else{
                                    texturaRandom = 14;
                                }
                                Gdx.app.log("ITEM", ""+ texturaRandom);
                            }else {
                                texturaGeneral = texturaDino_2;
                                w = 240;
                                h = 208;
                            }
                            break;
                        default:
                            if (MathUtils.random(0,2) == 0){
                                int item = MathUtils.random(0,4);
                                if (item == 0){
                                    texturaRandom = 10;
                                }else if (item == 1 && vidaExtra == false){
                                    texturaRandom = 11;
                                }else if (item == 2){
                                    texturaRandom = 12;
                                }else if(item == 3){
                                    texturaRandom = 13;
                                }else{
                                    texturaRandom = 14;
                                }
                                Gdx.app.log("ITEM", ""+ texturaRandom);
                            }else{
                                texturaGeneral = texturaDino_3;
                                w = 370;
                                h = 220;
                                break;
                            }

                    }
                    break;
                case EDAD_MODERNA:
                    switch(texturaRandom){
                        case 0:
                            if (MathUtils.random(0,2) == 0){
                                int item = MathUtils.random(0,4);
                                if (item == 0){
                                    texturaRandom = 10;
                                }else if (item == 1 && vidaExtra == false){
                                    texturaRandom = 11;
                                }else if (item == 2){
                                    texturaRandom = 12;
                                }else if(item == 3){
                                    texturaRandom = 13;
                                }else{
                                    texturaRandom = 14;
                                }
                                Gdx.app.log("ITEM", ""+ texturaRandom);
                            }else {
                                texturaGeneral = texturaDino_0;
                                alturaObstaculo = 200;
                                w = 150;
                                h = 133;
                            }
                            break;
                        case 1:
                            if (MathUtils.random(0,2) == 0){
                                int item = MathUtils.random(0,4);
                                if (item == 0){
                                    texturaRandom = 10;
                                }else if (item == 1 && vidaExtra == false){
                                    texturaRandom = 11;
                                }else if (item == 2){
                                    texturaRandom = 12;
                                }else if(item == 3){
                                    texturaRandom = 13;
                                }else{
                                    texturaRandom = 14;
                                }
                                Gdx.app.log("ITEM", ""+ texturaRandom);
                            }else{
                                texturaGeneral = texturaDino_1;
                                w = 286;
                                h = 300;
                                break;
                            }
                            break;
                        case 2:
                            if (MathUtils.random(0,2) == 0){
                                int item = MathUtils.random(0,4);
                                if (item == 0){
                                    texturaRandom = 10;
                                }else if (item == 1 && vidaExtra == false){
                                    texturaRandom = 11;
                                }else if (item == 2){
                                    texturaRandom = 12;
                                }else if(item == 3){
                                    texturaRandom = 13;
                                }else{
                                    texturaRandom = 14;
                                }
                                Gdx.app.log("ITEM", ""+ texturaRandom);
                            }else {
                                texturaGeneral = texturaDino_2;
                                w = 240;
                                h = 208;
                            }
                            break;
                        default:
                            if (MathUtils.random(0,2) == 0){
                                int item = MathUtils.random(0,4);
                                if (item == 0){
                                    texturaRandom = 10;
                                }else if (item == 1 && vidaExtra == false){
                                    texturaRandom = 11;
                                }else if (item == 2){
                                    texturaRandom = 12;
                                }else if(item == 3){
                                    texturaRandom = 13;
                                }else{
                                    texturaRandom = 14;
                                }
                                Gdx.app.log("ITEM", ""+ texturaRandom);
                            }else{
                                texturaGeneral = texturaDino_3;
                                w = 370;
                                h = 220;
                                break;
                            }

                    }
                    break;
                case EDAD_CONTEMPORANEA:
                    switch(texturaRandom){
                        case 0:
                            if (MathUtils.random(0,2) == 0){
                                int item = MathUtils.random(0,4);
                                if (item == 0){
                                    texturaRandom = 10;
                                }else if (item == 1 && vidaExtra == false){
                                    texturaRandom = 11;
                                }else if (item == 2){
                                    texturaRandom = 12;
                                }else if(item == 3){
                                    texturaRandom = 13;
                                }else{
                                    texturaRandom = 14;
                                }
                                Gdx.app.log("ITEM", ""+ texturaRandom);
                            }else {
                                texturaGeneral = texturaDino_0;
                                alturaObstaculo = 200;
                                w = 150;
                                h = 133;
                            }
                            break;
                        case 1:
                            if (MathUtils.random(0,2) == 0){
                                int item = MathUtils.random(0,4);
                                if (item == 0){
                                    texturaRandom = 10;
                                }else if (item == 1 && vidaExtra == false){
                                    texturaRandom = 11;
                                }else if (item == 2){
                                    texturaRandom = 12;
                                }else if(item == 3){
                                    texturaRandom = 13;
                                }else{
                                    texturaRandom = 14;
                                }
                                Gdx.app.log("ITEM", ""+ texturaRandom);
                            }else{
                                texturaGeneral = texturaDino_1;
                                w = 286;
                                h = 300;
                                break;
                            }
                            break;
                        case 2:
                            if (MathUtils.random(0,2) == 0){
                                int item = MathUtils.random(0,4);
                                if (item == 0){
                                    texturaRandom = 10;
                                }else if (item == 1 && vidaExtra == false){
                                    texturaRandom = 11;
                                }else if (item == 2){
                                    texturaRandom = 12;
                                }else if(item == 3){
                                    texturaRandom = 13;
                                }else{
                                    texturaRandom = 14;
                                }
                                Gdx.app.log("ITEM", ""+ texturaRandom);
                            }else {
                                texturaGeneral = texturaDino_2;
                                w = 240;
                                h = 208;
                            }
                            break;
                        default:
                            if (MathUtils.random(0,2) == 0){
                                int item = MathUtils.random(0,4);
                                if (item == 0){
                                    texturaRandom = 10;
                                }else if (item == 1 && vidaExtra == false){
                                    texturaRandom = 11;
                                }else if (item == 2){
                                    texturaRandom = 12;
                                }else if(item == 3){
                                    texturaRandom = 13;
                                }else{
                                    texturaRandom = 14;
                                }
                                Gdx.app.log("ITEM", ""+ texturaRandom);
                            }else{
                                texturaGeneral = texturaDino_3;
                                w = 370;
                                h = 220;
                                break;
                            }

                    }
                    break;
                case EDAD_FUTURA:
                    switch(texturaRandom){
                        case 0:
                            if (MathUtils.random(0,2) == 0){
                                int item = MathUtils.random(0,4);
                                if (item == 0){
                                    texturaRandom = 10;
                                }else if (item == 1 && vidaExtra == false){
                                    texturaRandom = 11;
                                }else if (item == 2){
                                    texturaRandom = 12;
                                }else if(item == 3){
                                    texturaRandom = 13;
                                }else{
                                    texturaRandom = 14;
                                }
                                Gdx.app.log("ITEM", ""+ texturaRandom);
                            }else {
                                texturaGeneral = texturaDino_0;
                                alturaObstaculo = 200;
                                w = 150;
                                h = 133;
                            }
                            break;
                        case 1:
                            if (MathUtils.random(0,2) == 0){
                                int item = MathUtils.random(0,4);
                                if (item == 0){
                                    texturaRandom = 10;
                                }else if (item == 1 && vidaExtra == false){
                                    texturaRandom = 11;
                                }else if (item == 2){
                                    texturaRandom = 12;
                                }else if(item == 3){
                                    texturaRandom = 13;
                                }else{
                                    texturaRandom = 14;
                                }
                                Gdx.app.log("ITEM", ""+ texturaRandom);
                            }else{
                                texturaGeneral = texturaDino_1;
                                w = 286;
                                h = 300;
                                break;
                            }
                            break;
                        case 2:
                            if (MathUtils.random(0,2) == 0){
                                int item = MathUtils.random(0,4);
                                if (item == 0){
                                    texturaRandom = 10;
                                }else if (item == 1 && vidaExtra == false){
                                    texturaRandom = 11;
                                }else if (item == 2){
                                    texturaRandom = 12;
                                }else if(item == 3){
                                    texturaRandom = 13;
                                }else{
                                    texturaRandom = 14;
                                }
                                Gdx.app.log("ITEM", ""+ texturaRandom);
                            }else {
                                texturaGeneral = texturaDino_2;
                                w = 240;
                                h = 208;
                            }
                            break;
                        default:
                            if (MathUtils.random(0,2) == 0){
                                int item = MathUtils.random(0,4);
                                if (item == 0){
                                    texturaRandom = 10;
                                }else if (item == 1 && vidaExtra == false){
                                    texturaRandom = 11;
                                }else if (item == 2){
                                    texturaRandom = 12;
                                }else if(item == 3){
                                    texturaRandom = 13;
                                }else{
                                    texturaRandom = 14;
                                }
                                Gdx.app.log("ITEM", ""+ texturaRandom);
                            }else{
                                texturaGeneral = texturaDino_3;
                                w = 370;
                                h = 220;
                                break;
                            }

                    }
                    break;
            }

            Obstaculo obstaculo;
            if (texturaRandom == 10){
                alturaObstaculo = MathUtils.random(0, 500);
                obstaculo = new Obstaculo(texturaItemRojo, ANCHO + 50, alturaObstaculo, 1);
            }else if(texturaRandom == 11){
                alturaObstaculo = MathUtils.random(0, 500);
                obstaculo = new Obstaculo(texturaItemDorado, ANCHO + 50, alturaObstaculo, 2);
            }else if(texturaRandom == 12){
                alturaObstaculo = MathUtils.random(0, 500);
                obstaculo = new Obstaculo(texturaItemAzul, ANCHO + 50, alturaObstaculo, 3);
            }else if(texturaRandom == 13){
                alturaObstaculo = MathUtils.random(0, 500);
                obstaculo = new Obstaculo(texturaItemMalo0, ANCHO + 50, alturaObstaculo, 4);
            }else if(texturaRandom == 14){
                alturaObstaculo = MathUtils.random(0, 500);
                obstaculo = new Obstaculo(texturaItemMalo1, ANCHO + 50, alturaObstaculo, 5);
            }
            else{
                obstaculo = new Obstaculo(texturaGeneral, ANCHO + 50, alturaObstaculo, w, h, 0);
            }
            arrObstaculos.add(obstaculo);

        }

        for (int i = arrObstaculos.size-1; i >= 0; i--) {
            Obstaculo obstaculo = arrObstaculos.get(i);
            if (obstaculo.sprite.getX() < 0 - obstaculo.sprite.getWidth()){
                arrObstaculos.removeIndex(i);
            }
        }
    }

    private void verificarChoques() {
        for (int i = arrObstaculos.size-1; i >= 0 ; i--) {
            Obstaculo obstaculo = arrObstaculos.get(i);
            Rectangle rectVaquero = new Rectangle(vaquero.sprite.getX(), vaquero.sprite.getY(), vaquero.sprite.getWidth()-50, vaquero.sprite.getHeight());
            Rectangle rectObstaculo = new Rectangle(obstaculo.sprite.getX(), obstaculo.sprite.getY(), obstaculo.sprite.getWidth()-50, obstaculo.sprite.getHeight());
            if (rectVaquero.overlaps(rectObstaculo)){
                // CHOCO, hay que revisar si es obstaculo o item
                if(obstaculo.getTipo() == 1)   // ITEMROJO!
                {
                    arrObstaculos.removeIndex(i);
                    puntos += 20;
                    sonidoItem.play(0.1f);
                }else if(obstaculo.getTipo() == 2){ //ITEMDORADO
                    arrObstaculos.removeIndex(i);
                    vidaExtra = true;
                    sonidoItem.play(0.1f);
                }else if(obstaculo.getTipo() == 3){ //ITEMAZUL
                    arrObstaculos.removeIndex(i);
                    aumentarPuntosExtra(0.008333333333f);
                    sonidoItem.play(0.1f);
                }else if(obstaculo.getTipo() == 4){ //ITEMMALO0
                    arrObstaculos.removeIndex(i);
                    aumentarVelocidadOBstaculo(3);
                    sonidoItemMalo.play(0.1f);
                }else if(obstaculo.getTipo() == 5){ //ITEMMALO1
                    arrObstaculos.removeIndex(i);
                    puntos-= 20;
                    sonidoItemMalo.play(0.1f);
                }
                else if(vidaExtra){
                    arrObstaculos.removeIndex(i);
                    vidaExtra = false;
                }
                // Obstaculo
                else{
                    vaquero.sprite.setY(ALTO);
                    vaquero.setEstado(EstadosVaquero.MURIENDO);
                    arrObstaculos.removeIndex(i);
                    estado = EstadoJuego.GAME_OVER;
                    guardarPreferencias();
                    Gdx.input.setInputProcessor(escenaGameOver);
                    Gdx.app.log("COLISION CON OBSTACULO", "El vaquero choco con obstaculo");
                    break;
                }

            }
        }
    }

    private void guardarPreferencias() {
        Preferences prefs = Gdx.app.getPreferences("marcador");
        if(prefs.getFloat("PUNTOS") < puntos)
            prefs.putFloat("PUNTOS", puntos);
        //prefs = Gdx.app.getPreferences("marcador");
        prefs.flush();  // OBLIGATORIO
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
            float xBoton = texturaBtnPausa.getWidth();
            float yBoton = ALTO*0.85f;
            float anchBoton = texturaBtnPausa.getWidth();
            float altoBoton = texturaBtnPausa.getHeight();
            Rectangle rectBoton = new Rectangle(xBoton, yBoton, anchBoton, altoBoton);
            if (rectBoton.contains(v.x, v.y)){  //Pico botón de Pausa
                if (estado == EstadoJuego.JUGANDO){
                    estado = EstadoJuego.PAUSADO;
                    sonidoPausa.play(0.1f);
                    // Cambiar el InputProcessor
                    Gdx.input.setInputProcessor(escenaPausa);
                    Gdx.app.log("PAUSA", "Cambia a pausado....");
                }else if (estado == EstadoJuego.PAUSADO) {
                    estado = EstadoJuego.JUGANDO;
                    Gdx.app.log("PAUSA", "Cambia a jugando....");
                }
            }
            //Mover al vaquero
            else if(v.x<= ANCHO/2){
                // Izq
                // Deslizar
                Gdx.app.log("CLICK IZQUIERDO", "Se clickeo el lado izquierdo");
                if (vaquero.getEstado() != EstadosVaquero.SALTANDO){
                    sonidoSlide.play(0.1f);
                    vaquero.deslizar();
                }

            }else{
                // derecha
                // vaquero.moverDerecha(); //
                if (vaquero.getEstado() != EstadosVaquero.SALTANDO && vaquero.getEstado() != EstadosVaquero.DESLIZANDO)
                {
                    sonidoSalto.play(0.1f);
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
            //Texture texturaBtnReanudar = new Texture("btnsPausa/btnReanudar.png");
            Texture texturaBtnReanudar = juego.getManager().get("btnsPausa/btnReanudar.png");
            TextureRegionDrawable trdBtnReanudar = new TextureRegionDrawable(new TextureRegion(texturaBtnReanudar));
            //Retroalimentación
            //Texture texturaBtnReanudarRetro = new Texture("btnsPausa/btnReanudarRetro.png");
            Texture texturaBtnReanudarRetro = juego.getManager().get("btnsPausa/btnReanudarRetro.png");
            TextureRegionDrawable trdBtnReanudarRetro = new TextureRegionDrawable(new TextureRegion(texturaBtnReanudarRetro));
            final ImageButton btnReanudar = new ImageButton(trdBtnReanudar,trdBtnReanudarRetro);
            btnReanudar.setPosition(ANCHO/2,ALTO/2, Align.center);

            //btnSalir
            //Texture texturaBtnSalir = new Texture("btnsPausa/btnSalir.png");
            Texture texturaBtnSalir = juego.getManager().get("btnsPausa/btnSalir.png");
            TextureRegionDrawable trdBtnSalir = new TextureRegionDrawable(new TextureRegion(texturaBtnSalir));
            //Retroalimentación
            //Texture texturaBtnSalirRetro = new Texture("btnsPausa/btnSalirRetro.png");
            Texture texturaBtnSalirRetro = juego.getManager().get("btnsPausa/btnSalirRetro.png");
            TextureRegionDrawable trdBtnJugarRetro = new TextureRegionDrawable(new TextureRegion(texturaBtnSalirRetro));
            ImageButton btnSalir = new ImageButton(trdBtnSalir,trdBtnJugarRetro);
            btnSalir.setPosition(ANCHO/2,ALTO/2-87, Align.center);

            //btnSonido
            //Texture texturaBtnSonido = new Texture("btnsPausa/btnSonido.png");
            Texture texturaBtnSonido = juego.getManager().get("btnsPausa/btnSonido.png");
            TextureRegionDrawable trdBtnSonido = new TextureRegionDrawable(new TextureRegion(texturaBtnSonido));
            //Retroalimentación
            //Texture texturaBtnSonidoRetro = new Texture("btnsPausa/btnSonidoRetro.png");
            Texture texturaBtnSonidoRetro = juego.getManager().get("btnsPausa/btnSonidoRetro.png");
            TextureRegionDrawable trdBtnSonidoRetro = new TextureRegionDrawable(new TextureRegion(texturaBtnSonidoRetro));
            // Estilos de botón
            final Button.ButtonStyle estiloPrendido= new Button.ButtonStyle(trdBtnSonido, trdBtnSonidoRetro, null);
            final Button.ButtonStyle estiloApagado = new Button.ButtonStyle(trdBtnSonidoRetro, trdBtnSonido, null);

            final ImageButton.ImageButtonStyle Prendido = new ImageButton.ImageButtonStyle(estiloPrendido);
            final ImageButton.ImageButtonStyle Apagado = new ImageButton.ImageButtonStyle(estiloApagado);

            final ImageButton btnSonido = new ImageButton(trdBtnSonido, trdBtnSonidoRetro);
            btnSonido.setPosition(ANCHO*0.8f,ALTO*0.188f, Align.center);


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
                    musicaGeneral.stop();
                    //*********CAMBIADO**********
                    juego.setScreen(new PantallaCargando(juego,Pantallas.MENU));
                }
            });

            btnSonido.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    guardarPreferenciaSonido();
                    if (musicaGeneral.isPlaying()){
                        btnSonido.setStyle(Prendido);
                    }else{
                        btnSonido.setStyle(Apagado);
                    }
                }
            });

            this.addActor(btnReanudar);
            this.addActor(btnSalir);
            this.addActor(btnSonido);
        }

        private void guardarPreferenciaSonido() {
            Preferences prefs = Gdx.app.getPreferences("sonido");
            if (prefs.getBoolean("PLAY"))
            {
                prefs.putBoolean("PLAY", false);
                musicaGeneral.stop();
            }else {
                prefs.putBoolean("PLAY", true);
                musicaGeneral.play();
            }


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
            //Texture texturaBtnSalir = new Texture("btnsGameOver/btnSalir.png");
            Texture texturaBtnSalir = juego.getManager().get("btnsGameOver/btnSalir.png");
            TextureRegionDrawable trdBtnSalir = new TextureRegionDrawable(new TextureRegion(texturaBtnSalir));
            //Retroalimentación
            //Texture texturaBtnSalirRetro = new Texture("btnsGameOver/btnSalirRetro.png");
            Texture texturaBtnSalirRetro = juego.getManager().get("btnsGameOver/btnSalirRetro.png");
            TextureRegionDrawable trdBtnSalirRetro = new TextureRegionDrawable(new TextureRegion(texturaBtnSalirRetro));
            ImageButton btnSalir = new ImageButton(trdBtnSalir,trdBtnSalirRetro);
            btnSalir.setPosition(ANCHO/2,ALTO/2-87, Align.center);

            //btnReiniciar
            //Texture texturaBtnReiniciar = new Texture("btnsGameOver/btnReiniciar.png");
            Texture texturaBtnReiniciar = juego.getManager().get("btnsGameOver/btnReiniciar.png");
            TextureRegionDrawable trdBtnReiniciar= new TextureRegionDrawable(new TextureRegion(texturaBtnReiniciar));
            //Retroalimentación
            //Texture texturaBtnReiniciarRetro = new Texture("btnsGameOver/btnReinciarRetro.png");
            Texture texturaBtnReiniciarRetro = juego.getManager().get("btnsGameOver/btnReiniciarRetro.png");
            TextureRegionDrawable trdBtnReiniciarRetro = new TextureRegionDrawable(new TextureRegion(texturaBtnReiniciarRetro));
            ImageButton btnReiniciar = new ImageButton(trdBtnReiniciar,trdBtnReiniciarRetro);
            btnReiniciar.setPosition(ANCHO/2,ALTO/2, Align.center);

            // Programar listener del los botones
            //btnSalir
            btnSalir.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    // QUITAR Pausa
                    musicaGeneral.stop();
                    juego.setScreen(new PantallaCargando(juego, Pantallas.MENU));
                }
            });

            //btnReiniciar
            btnReiniciar.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    // QUITAR Pausa
                    musicaGeneral.stop();
                    juego.setScreen(new PantallaCargando(juego, Pantallas.JUEGO));
                }
            });

            this.addActor(btnSalir);
            this.addActor(btnReiniciar);
        }
    }
}
