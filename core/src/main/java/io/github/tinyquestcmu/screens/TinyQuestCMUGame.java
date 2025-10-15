package io.github.tinyquestcmu.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.tiled.TiledMap;
import io.github.tinyquestcmu.quest.QuestManager;

public class TinyQuestCMUGame extends Game {
    public SpriteBatch batch;
    public BitmapFont font;
    public QuestManager questManager;
    private TiledMap map;

    @Override
    public void create() {
//        map = new TmxMapLoader().load("assets/tmx/village.tmx");

        batch = new SpriteBatch();
        font = new BitmapFont();
        questManager = new QuestManager();
        setScreen(new IntroScreen(this));
        setScreen(new MainMenuScreen(this));

    }

    @Override
    public void dispose() {
        if (getScreen() != null) getScreen().dispose();
        batch.dispose();
        font.dispose();
    }
}
