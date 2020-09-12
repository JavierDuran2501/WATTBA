package mx.itesm.equipo4.wattba;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/*
Juego principal
Autor: Javier Alejandro Durán Díaz
 */

public class Juego extends Game {
	@Override
	public void create () {
		//Primer ventana
		setScreen(new PantallaMenu(this)); //Pasamos el controlador
	}

	@Override
	public void render () {
		super.render();
	}

}
