package mx.itesm.equipo4.wattba;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class PantallaCargando extends Pantalla
{
    // Animación cargando (espera...)
    private Sprite spriteCargando;
    public static final float TIEMPO_ENTRE_FRAMES = 0.05f;
    private float timerAnimacion = TIEMPO_ENTRE_FRAMES;
    private Texture texturaCargando;

    // AssetManager
    private AssetManager manager;

    //Música
    private Music musicaMenu;
    private Music musicaJuego;

    // Referencia al juego
    private Juego juego;    // Para hacer setScreen

    // Identifica las pantallas del juego
    private Pantallas siguientePantalla;

    // % de carga
    private int avance;

    // Para mostrar mensajes
    private Texto texto;

    public PantallaCargando(Juego juego, Pantallas siguientePantalla) {
        this.juego = juego;
        this.siguientePantalla = siguientePantalla;
    }

    @Override
    public void show() {
        texturaCargando = new Texture("Cargando/loading.png");
        spriteCargando = new Sprite(texturaCargando);
        spriteCargando.setPosition(ANCHO/2 - spriteCargando.getWidth()/2,
                ALTO/2 - spriteCargando.getHeight()/2);
        cargarRecursos(siguientePantalla);
        texto = new Texto("Fuentes/game.fnt");
    }

    private void cargarRecursos(Pantallas siguientePantalla) {
        manager = juego.getManager();
        avance = 0; // % de carga
        switch (siguientePantalla) {
            case MENU:
                cargarRecursosMenu();
                break;
            case JUEGO:
                cargarRecursosRunner();
                break;
        }
    }

    private void cargarRecursosMenu() {
        manager.load("Pantallas/pantallaTituloWATTBA.png", Texture.class);
        // Botones/escena
        manager.load("btnsMenu/btnJugar.png", Texture.class);
        manager.load("btnsMenu/btnJugarRetro.png", Texture.class);
        manager.load("btnsMenu/btnAcercaDe.png", Texture.class);
        manager.load("btnsMenu/btnAcercaDeRetro.png", Texture.class);
        manager.load("btnsMenu/btnInstrucciones.png", Texture.class);
        manager.load("btnsMenu/btnInstruccionesRetro.png", Texture.class);
        manager.load("btnsMenu/btnRegresar.png", Texture.class);
        manager.load("btnsPausa/btnSonido.png", Texture.class);
        manager.load("btnsPausa/btnSonidoRetro.png", Texture.class);
        manager.load("Musica/musicaMenu.mp3", Music.class);
        manager.finishLoading();

    }

    private void cargarRecursosRunner() {
        //Pantallas
        manager.load("Pantallas/Juego.jpg",Texture.class);
        manager.load("Pantallas/edadAntiguaFondo.jpg", Texture.class);
        manager.load("Pantallas/Prehistoria.jpg", Texture.class);

        //Música y sonido
        manager.load("Musica/musicaMezo.mp3", Music.class);
        manager.load("Musica/musicaPrehistoria.mp3",Music.class);
        manager.load("Musica/MusicaAntigua.mp3",Music.class);
        manager.load("Sonidos/sonidoSalto.mp3", Sound.class);
        manager.load("Sonidos/sonidoItem.mp3",Sound.class);
        manager.load("Sonidos/sonidoItemMalo.mp3",Sound.class);
        manager.load("Sonidos/sonidoCambioEpoca.mp3",Sound.class);

        //Enemigos
        manager.load("Dinosaurios/Dino000.png",Texture.class);
        manager.load("Dinosaurios/Dino001.png",Texture.class);

        //Items
        manager.load("Items/ItemRojo.png",Texture.class);
        manager.load("Items/ItemAzul.png",Texture.class);
        manager.load("Items/ItemDorado.png",Texture.class);
        manager.load("Items/ItemMalo0.png",Texture.class);
        manager.load("Items/ItemMalo1.png",Texture.class);

        //Personaje
        manager.load("Vaquero/Correr2.png",Texture.class);
        manager.load("Vaquero/Dead.png",Texture.class);

        //Botones
        manager.load("btnsPausa/btnReanudar.png",Texture.class);
        manager.load("btnsPausa/btnReanudarRetro.png",Texture.class);
        manager.load("btnsPausa/btnSalir.png",Texture.class);
        manager.load("btnsPausa/btnSalirRetro.png",Texture.class);
        manager.load("btnsPausa/btnSonido.png",Texture.class);
        manager.load("btnsPausa/btnSonidoRetro.png",Texture.class);
        manager.load("btnsGameOver/btnReiniciar.png",Texture.class);
        manager.load("btnsGameOver/btnReiniciarRetro.png",Texture.class);
        manager.load("btnsGameOver/btnSalir.png",Texture.class);
        manager.load("btnsGameOver/btnSalirRetro.png",Texture.class);
        manager.finishLoading();
    }

    @Override
    public void render(float delta) {
        borrarPantalla(0.5f, 0.2f, 0.5f);

        batch.setProjectionMatrix(camara.combined);

        batch.begin();
        spriteCargando.draw(batch);
        texto.mostrarMensaje(batch, avance + "%", ANCHO/2, ALTO/2);
        batch.end();

        // Actualizar
        timerAnimacion -= delta;
        if (timerAnimacion<=0) {
            timerAnimacion = TIEMPO_ENTRE_FRAMES;
            spriteCargando.rotate(20);
        }

        // Actualizar carga
        actualizarCarga();
    }

    private void actualizarCarga() {
        // ¿Cómo va la carga de recursos?
        if (manager.update()) {
            // Terminó!
            switch (siguientePantalla) {
                case MENU:
                    juego.setScreen(new PantallaMenu(juego));
                    break;
                // Agregar las otras pantallas
                case JUEGO:
                    juego.setScreen(new PantallaJugar(juego));
            }
        }

        avance = (int)(manager.getProgress()*100);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        texturaCargando.dispose();
    }
}
