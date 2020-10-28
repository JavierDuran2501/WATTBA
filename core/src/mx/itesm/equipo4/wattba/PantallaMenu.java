package mx.itesm.equipo4.wattba;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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
Autor: Javier Alejandro Durán Díaz
 */
//Creación de camara, batch, texturas, render y dispose.

public class PantallaMenu extends Pantalla {

    private final Juego juego;

    //Fondo de menú principalx
    private Texture texturaFondo;

    //Botones del menú
    private Stage escenaMenu; //Botones del menú (contenedor)


    public PantallaMenu(Juego juego) {
        this.juego = juego;
    }

    private Music musicaFondo;

    //Entra en ejecución cuando la pantalla se va a mostrar; inicializa objetos
    @Override
    public void show() {
        texturaFondo = new Texture("Pantallas/pantallaTituloWATTBA.png");

        crearMenu();
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

        //btnTienda
        Texture texturaBtnTienda = new Texture("btnsMenu/btnTienda.png");
        TextureRegionDrawable trdBtnTienda = new TextureRegionDrawable(new TextureRegion(texturaBtnTienda));
        //Retroalimentación
        Texture texturaBtnTiendaRetro = new Texture("btnsMenu/btnTiendaRetro.png");
        TextureRegionDrawable trdBtnTiendaRetro = new TextureRegionDrawable(new TextureRegion(texturaBtnTiendaRetro));
        ImageButton btnTienda = new ImageButton(trdBtnTienda,trdBtnTiendaRetro);
        btnTienda.setPosition(ANCHO/2,ALTO/2-87, Align.center);

        //btnSonido
        Texture texturaBtnSonido = new Texture("btnsPausa/btnSonido.png");
        TextureRegionDrawable trdBtnSonido = new TextureRegionDrawable(new TextureRegion(texturaBtnSonido));
        //Retroalimentación
        Texture texturaBtnSonidoRetro = new Texture("btnsPausa/btnReanudarRetro.png");
        TextureRegionDrawable trdBtnSonidoRetro = new TextureRegionDrawable(new TextureRegion(texturaBtnSonidoRetro));
        ImageButton btnSonido = new ImageButton(trdBtnSonido);
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

        //btn Tienda
        btnTienda.addListener( new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                // Cambiamos de pantalla
                juego.setScreen(new PantallaTienda(juego));
            }
        });

        //btn Juego
        btnJugar.addListener( new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                // Cambiamos de pantalla
                juego.setScreen(new PantallaJugar(juego));
            }
        });

        btnSonido.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

            }
        });

        escenaMenu.addActor(btnJugar);
        escenaMenu.addActor(btnAcercaDe);
        escenaMenu.addActor(btnTienda);
        escenaMenu.addActor(btnSonido);

        Gdx.input.setInputProcessor(escenaMenu);
    }

    @Override
    public void render(float delta) {
        borrarPantalla();
        batch.setProjectionMatrix(camara.combined);

        batch.begin();
        batch.draw(texturaFondo,0,0);
        batch.end();

        escenaMenu.draw();
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
