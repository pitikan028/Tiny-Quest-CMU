package io.github.tinyquestcmu.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import io.github.tinyquestcmu.TinyQuestCMUGame;
import com.badlogic.gdx.graphics.Texture;
import io.github.tinyquestcmu.assets.TileMap;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import io.github.tinyquestcmu.actors.Player;
import io.github.tinyquestcmu.actors.NPC;
import io.github.tinyquestcmu.dialogue.*;
import io.github.tinyquestcmu.quest.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class RescueScreen extends BaseScreen {
    private Texture loadTex(String path){
        try { return new Texture(path); } catch (GdxRuntimeException e){ System.out.println("[TinyQuestCMU] Missing texture: "+path+" => "+e.getMessage()); return null; }
    }

    private TileMap map;
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer tiledRenderer;
    private Texture tGrass, tDirt, tWater, tBridge, tHouse, tTree, tSpecial;
    private Texture texPlayer;
    private Player player = new Player(200, 180);
    private NPC brother = new NPC("Brother", "Lost Sibling", 420, 200);
    private DialogueSystem ds = new DialogueSystem();
    private boolean freed = false;

    public RescueScreen(TinyQuestCMUGame game){ super(game); }

    @Override public void render(float dt) {
        clear(0.08f,0.05f,0.12f);
        player.update(dt);

        // draw TMX map (if available)
        if(tiledRenderer != null){
            tiledRenderer.setView(cam);
            tiledRenderer.render();
        }
        // draw map + sprites
        if(map!=null){
            game.batch.begin();
        player.drawSprite(game.batch); brother.drawSprite(game.batch);
            map.draw(game.batch);
            game.batch.end();
        }


        boolean near = brother.isPlayerNear(player.getBounds());
        if(near && !ds.isActive() && !freed){
            Dialogue d = new Dialogue(new DialogueNode(
                "You found me! The tree protected me.",
                new DialogueNode("I'm free now—let's go home.", null)));
            ds.start(d);
            freed = true;
            game.questManager.set(QuestFlag.FREED_BROTHER);
        }
        ds.update();

        shapes.begin(com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled);
        shapes.setColor(Color.OLIVE); shapes.rect(0,0,800,480);
        shapes.setColor(Color.SALMON); brother.draw(shapes);
        shapes.setColor(Color.WHITE); player.draw(shapes);
        shapes.end();

        game.batch.begin();
        player.drawSprite(game.batch); brother.drawSprite(game.batch);
        brother.drawLabel(game.batch, game.font);
        player.drawLabel(game.batch, game.font);
        if(freed && !ds.isActive()) game.font.draw(game.batch, "Press ENTER to return home", 290, 84);
        game.batch.end();

        drawHud();

        if(freed && !ds.isActive() && Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
            game.setScreen(new EndingScreen(game));
        }
    }
}
