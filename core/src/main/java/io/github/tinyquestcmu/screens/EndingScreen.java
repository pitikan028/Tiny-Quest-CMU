package io.github.tinyquestcmu.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;             // ✅ สำคัญ
import com.badlogic.gdx.utils.GdxRuntimeException;   // ✅ สำคัญ
import io.github.tinyquestcmu.TinyQuestCMUGame;
import io.github.tinyquestcmu.quest.*;

public class EndingScreen extends BaseScreen {
    private float timer = 0f;
    private Texture tGrass; // ✅ ประกาศไว้ในคลาสนี้แน่นอน

    public EndingScreen(TinyQuestCMUGame game) {
        super(game);
        game.questManager.set(QuestFlag.FINISHED);
    }

    private Texture loadTex(String path){
        try {
            return new Texture(path);
        } catch (GdxRuntimeException e){
            System.out.println("[TinyQuestCMU] Missing texture: " + path + " => " + e.getMessage());
            return null;
        }
    }

    @Override
    public void show() {
        tGrass = loadTex("assets/grass.png");
    }

    @Override
    public void render(float dt) {
        clear(0,0,0);
        timer += dt;

        shapes.begin(com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled);
        shapes.setColor(Color.BLACK);
        shapes.rect(0,0,800,480);
        shapes.setColor(Color.WHITE);
        shapes.rect(180,160,440,160);
        shapes.end();

        game.batch.begin();
        game.font.setColor(Color.BLACK);
        game.font.draw(game.batch, "The family is reunited.", 260, 280);
        game.font.draw(game.batch, "Thank you for playing TinyQuestCMU.", 220, 240);
        game.font.setColor(Color.WHITE);
        if (timer > 2f) game.font.draw(game.batch, "Press ENTER to restart", 300, 100);
        game.batch.end();

//        drawHud();

        if (timer > 2f && (Gdx.input.isKeyJustPressed(Input.Keys.ENTER))) {
            game.setScreen(new IntroScreen(game));
        }
    }
}
