package mx.itesm.equipo4.wattba;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

/*
Pantalla de menú
 */
//Creación de camara, batch, texturas, render y dispose.

public class PantallaMenu extends Pantalla {

    private final Juego juego;

    //Fondo de menú principalx
    private Texture texturaFondo;

    //Botones del menú
    private Stage escenaMenu; //Botones del menú (contenedor)

    // Texto
    private Texto texto;
    private float puntos;

    // Musica
    private Music musicaFondo;


    public PantallaMenu(Juego juego) {
        this.juego = juego;
    }

    //Entra en ejecución cuando la pantalla se va a mostrar; inicializa objetos
    @Override
    public void show() {
        texturaFondo = new Texture("Pantallas/pantallaTituloWATTBA.png");
        cargarPreferencias();
        crearMenu();
        crearTexto();
        crearAudio();
    }

    private void crearAudio() {
        AssetManager manager = new AssetManager();
        manager.load("Musica/musicaMenu.mp3", Music.class);
        manager.finishLoading();
        musicaFondo = manager.get("Musica/musicaMenu.mp3");
        musicaFondo.setVolume(0.1f);
        musicaFondo.setLooping(true);


    }

    private void crearTexto() {
        texto = new Texto("Fuentes/game.fnt");
    }

    private void cargarPreferencias() {
        Preferences prefs = Gdx.app.getPreferences("marcador");
        puntos = prefs.getFloat("PUNTOS", 0);
    }

    private void crearMenu() {
        escenaMenu = new Stage(vista);

        //btnJugar
        Texture texturaBtnJugar = new Texture("btnsMenu/btnJugar.png");
        TextureRegionDrawable trdBtnJugar = new TextureRegionDrawable(new TextureRegion(texturaBtnJugar));
        //Retroalimentación
        Texture texturaBtnJugarRetro = new Texture("btnsMenu/btnJugarRetro.png");
        TextureRegionDrawable trdBtnJugarRetro = new TextureRegionDrawable(new TextureRegion(texturaBtnJugarRetro));
        ImageButton btnJugar = new ImageButton(trdBtnJugar,trdBtnJugarRetro);
        btnJugar.setPosition(ANCHO/2,ALTO/2, Align.center);

        //btnAcercaDe
        Texture texturaBtnAcercaDe = new Texture("btnsMenu/btnAcercaDe.png");
        TextureRegionDrawable trdBtnAcercaDe = new TextureRegionDrawable(new TextureRegion(texturaBtnAcercaDe));
        //Retroalimentación
        Texture texturaBtnAcercaDeRetro = new Texture("btnsMenu/btnAcercaDeRetro.png");
        TextureRegionDrawable trdBtnAcercaDeRetro = new TextureRegionDrawable(new TextureRegion(texturaBtnAcercaDeRetro));
        ImageButton btnAcercaDe = new ImageButton(trdBtnAcercaDe,trdBtnAcercaDeRetro);
        btnAcercaDe.setPosition(ANCHO/2,ALTO/2-175, Align.center);

        //btnInstrucciones
        Texture texturaBtnInstrucciones = new Texture("btnsMenu/btnInstrucciones.png");
        TextureRegionDrawable trdBtnInstrucciones = new TextureRegionDrawable(new TextureRegion(texturaBtnInstrucciones));
        //Retroalimentación
        Texture texturaBtnInstruccionesRetro = new Texture("btnsMenu/btnInstruccionesRetro.png");
        TextureRegionDrawable trdBtnInstruccionesRetro = new TextureRegionDrawable(new TextureRegion(texturaBtnInstruccionesRetro));
        ImageButton btnInstrucciones = new ImageButton(trdBtnInstrucciones,trdBtnInstruccionesRetro);
        btnInstrucciones.setPosition(ANCHO/2,ALTO/2-87, Align.center);

        //btnSonido
        Texture texturaBtnSonido = new Texture("btnsPausa/btnSonido.png");
        TextureRegionDrawable trdBtnSonido = new TextureRegionDrawable(new TextureRegion(texturaBtnSonido));
        //Retroalimentación
        Texture texturaBtnSonidoRetro = new Texture("btnsPausa/btnSonidoRetro.png");
        TextureRegionDrawable trdBtnSonidoRetro = new TextureRegionDrawable(new TextureRegion(texturaBtnSonidoRetro));
        ImageButton btnSonido = new ImageButton(trdBtnSonido, trdBtnSonidoRetro);
        btnSonido.setPosition(ANCHO*0.8f,ALTO*0.188f, Align.center);

        // Listeners de los botones
        //btn Acerca de
        btnAcercaDe.addListener( new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                // Cambiamos de pantalla
                juego.setScreen(new PantallaAcercaDe(juego));
            }
        });

        //btn Instrucciones
        btnInstrucciones.addListener( new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                // Cambiamos de pantalla
                juego.setScreen(new PantallaInstrucciones(juego));
            }
        });

        //btn Juego
        btnJugar.addListener( new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                // Cambiamos de pantalla
                musicaFondo.stop();
                juego.setScreen(new PantallaJugar(juego));
            }
        });

        //btn Sonido
        btnSonido.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                guardarPreferenciaSonido();


            }
        });

        escenaMenu.addActor(btnJugar);
        escenaMenu.addActor(btnAcercaDe);
        escenaMenu.addActor(btnInstrucciones);
        escenaMenu.addActor(btnSonido);

        Gdx.input.setInputProcessor(escenaMenu);
    }

    private void guardarPreferenciaSonido() {
        Preferences prefs = Gdx.app.getPreferences("sonido");
        if (prefs.getBoolean("sonido"))

            prefs.putBoolean("sonido", false);

        else
            prefs.putBoolean("sonido", true);


    }

    @Override
    public void render(float delta) {
        borrarPantalla();
        controlMusica();
        batch.setProjectionMatrix(camara.combined);

        batch.begin();
        batch.draw(texturaFondo,0,0);

        dibujarTexto();
        batch.end();

        escenaMenu.draw();
    }

    private void controlMusica() {
        Preferences pref = Gdx.app.getPreferences("sonido");
        if (pref.getBoolean("sonido"))
            musicaFondo.play();
        else
            musicaFondo.stop();
    }

    private void dibujarTexto() {
        int puntosInt = (int)puntos;
        texto.mostrarMensaje(batch, "HighScore: " + puntosInt, ANCHO*0.2f, 0.1f*ALTO);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        texturaFondo.dispose();
        batch.dispose();
    }
}
