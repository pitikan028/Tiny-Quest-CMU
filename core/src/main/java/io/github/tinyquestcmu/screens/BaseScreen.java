package io.github.tinyquestcmu.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Color;

public abstract class BaseScreen implements Screen {
    protected final TinyQuestCMUGame game;
    protected OrthographicCamera cam;
    protected ShapeRenderer shapes;

    public BaseScreen(TinyQuestCMUGame game){
        this.game = game;
        this.cam = new OrthographicCamera();
        cam.setToOrtho(false, 800, 480);
        this.shapes = new ShapeRenderer();
    }

    protected void clear(float r, float g, float b){
        com.badlogic.gdx.Gdx.gl.glClearColor(r,g,b,1);
        com.badlogic.gdx.Gdx.gl.glClear(com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT);
    }

    protected void drawHud(){
        game.batch.begin();
        game.font.setColor(Color.WHITE);
        game.font.draw(game.batch, "Objective: " + game.questManager.hud(), 16, 472);
        game.batch.end();
    }

    @Override public void show() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose(){ shapes.dispose(); }
}
