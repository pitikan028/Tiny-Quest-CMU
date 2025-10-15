package io.github.tinyquestcmu.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import io.github.tinyquestcmu.assets.TileMap;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import io.github.tinyquestcmu.actors.Player;
import io.github.tinyquestcmu.actors.NPC;
import io.github.tinyquestcmu.quest.*;

import com.badlogic.gdx.utils.GdxRuntimeException;

public class ForestScreen extends BaseScreen {
    private Texture loadTex(String path){
        try { return new Texture(path); } catch (GdxRuntimeException e){ System.out.println("[TinyQuestCMU] Missing texture: "+path+" => "+e.getMessage()); return null; }
    }

    private TileMap map;
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer tiledRenderer;
    private Texture tGrass, tDirt, tWater, tBridge, tHouse, tTree, tSpecial;
    private Texture texPlayer;
    private Player player = new Player(60, 60);
    private NPC specialTree = new NPC("Special Tree", "Glowing Bark", 400, 220);

    public ForestScreen(TinyQuestCMUGame game){ super(game); }

    @Override public void render(float dt) {
        clear(0.02f,0.18f,0.06f);
        player.update(dt);

        // draw TMX map (if available)
        if(tiledRenderer != null){
            tiledRenderer.setView(cam);
            tiledRenderer.render();
        }
        // draw map + sprites
        if(map!=null){
            game.batch.begin();
        player.drawSprite(game.batch);
            map.draw(game.batch);
            game.batch.end();
        }


        boolean nearTree = specialTree.getBounds().overlaps(player.getBounds());
        if(nearTree){
            game.questManager.set(QuestFlag.FOUND_SPECIAL_TREE);
        }

        shapes.begin(com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled);
        shapes.setColor(Color.FOREST); shapes.rect(0,0,800,480);
        shapes.setColor(Color.LIME); // normal trees
        for(int i=0;i<8;i++) shapes.rect(80 + i*80, 120 + (i%2)*60, 12, 24);
        shapes.setColor(Color.RED); specialTree.draw(shapes); // special tree
        shapes.setColor(Color.WHITE); player.draw(shapes);
        shapes.end();

        game.batch.begin();
        player.drawSprite(game.batch);
        specialTree.drawLabel(game.batch, game.font);
        player.drawLabel(game.batch, game.font);
        if(nearTree) game.font.draw(game.batch, "Press ENTER to proceed", 310, 84);
        game.batch.end();

        drawHud();

        if(nearTree && Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
            game.setScreen(new RescueScreen(game));
        }
    }
}
