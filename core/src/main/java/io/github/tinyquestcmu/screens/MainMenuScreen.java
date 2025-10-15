package io.github.tinyquestcmu.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

public class MainMenuScreen extends BaseScreen {
    private Texture bg;
    public MainMenuScreen(TinyQuestCMUGame game){ super(game); }

    @Override public void show() {
        try { bg = new Texture("assets/ui/main_menu_bg.png"); } catch (Exception e) { bg = null; }
    }

    @Override public void render(float dt){
        clear(0.9f,0.9f,0.9f);
        game.batch.begin();
        if(bg!=null) game.batch.draw(bg, 0, 0, 800, 480);
        game.font.setColor(Color.FOREST);
        game.font.getData().setScale(1.2f);
        game.font.draw(game.batch, "Tiny Quest CMU", 290, 360);
        game.font.getData().setScale(1f);
        game.font.setColor(Color.WHITE);
        game.font.draw(game.batch, "Press ENTER to Start", 300, 220);
        game.font.draw(game.batch, "Press O for Options", 315, 190);
        game.batch.end();

        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
            game.setScreen(new IntroScreen(game));
        }
    }
}
