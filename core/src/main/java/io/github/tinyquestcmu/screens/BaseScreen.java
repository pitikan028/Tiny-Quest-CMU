package io.github.tinyquestcmu.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch; // Import SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.tinyquestcmu.TinyQuestCMUGame;

public abstract class BaseScreen implements Screen {

    protected TinyQuestCMUGame game;
    protected OrthographicCamera cam;
    protected Viewport viewport;
    protected ShapeRenderer shapes;

    // STEP 1: Declare the new SpriteBatch for the HUD
    protected SpriteBatch hudBatch;

    public static final float WORLD_WIDTH = 800;
    public static final float WORLD_HEIGHT = 480;

    public BaseScreen(TinyQuestCMUGame game) {
        this.game = game;

        cam = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, cam);
        shapes = new ShapeRenderer();

        // STEP 2: Create the new SpriteBatch
        hudBatch = new SpriteBatch();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply();
        cam.update();

        game.batch.setProjectionMatrix(cam.combined);
        shapes.setProjectionMatrix(cam.combined);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    protected void clear(float r, float g, float b) {
        Gdx.gl.glClearColor(r, g, b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void dispose() {
        shapes.dispose();
        // STEP 3: Dispose of the hudBatch to prevent memory leaks
        if (hudBatch != null) {
            hudBatch.dispose();
        }
    }

    @Override public void show() { }
    @Override public void pause() { }
    @Override public void resume() { }
    @Override public void hide() { }
}
