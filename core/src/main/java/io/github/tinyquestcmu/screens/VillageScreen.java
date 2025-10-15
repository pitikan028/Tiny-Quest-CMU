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
import io.github.tinyquestcmu.actors.Chest;
import com.badlogic.gdx.graphics.Texture;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class VillageScreen extends BaseScreen {
    private Texture loadTex(String path){
        try { return new Texture(path); } catch (GdxRuntimeException e){ System.out.println("[TinyQuestCMU] Missing texture: "+path+" => "+e.getMessage()); return null; }
    }

    private TileMap map;
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer tiledRenderer;
    private Texture tGrass, tDirt, tWater, tBridge, tHouse, tTree, tSpecial;
    private Texture texPlayer;
    private Player player = new Player(100, 160);
    // Themed villager: Eira (Penguin) at Library square, acting as helper
    private NPC villager = new NPC("Eira", "Helpful Villager", 380, 200);
    private DialogueSystem ds = new DialogueSystem();
    private boolean gaveMission = false;
    private Chest chest;

    public VillageScreen(TinyQuestCMUGame game){ super(game); }

    @Override public void render(float dt) {
        clear(0.18f,0.12f,0.05f);
        player.update(dt);

        // draw TMX map (if available)
        if(tiledRenderer != null){
            tiledRenderer.setView(cam);
            tiledRenderer.render();
        }
        // draw map + sprites
        if(map!=null){
            game.batch.begin();
        player.drawSprite(game.batch); villager.drawSprite(game.batch);
            map.draw(game.batch);
            game.batch.end();
        }


        if(!gaveMission && villager.isPlayerNear(player.getBounds())){
            Dialogue d = new Dialogue(new DialogueNode(
                "Please help us! In the forest, find a special tree—",
                new DialogueNode("it looks different from the rest.",
                new DialogueNode("Maybe your brother is there…", null))));
            ds.start(d);
            gaveMission = true;
            game.questManager.set(QuestFlag.GOT_FOREST_MISSION);
        }
        ds.update();

        shapes.begin(com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled);
        shapes.setColor(Color.BROWN); shapes.rect(0,0,800,480); // ground
        shapes.setColor(Color.GOLD); villager.draw(shapes);
        shapes.setColor(Color.WHITE); player.draw(shapes);
        // decorative houses
        shapes.setColor(Color.FIREBRICK); for(int i=0;i<5;i++) shapes.rect(40+i*140,300,60,40);
        shapes.end();

        game.batch.begin();
        player.drawSprite(game.batch); villager.drawSprite(game.batch);
        villager.drawLabel(game.batch, game.font);
        player.drawLabel(game.batch, game.font);
        game.batch.end();

        ds.draw(shapes, game.batch, game.font, 800);
        drawHud();

        if(gaveMission && !ds.isActive()){
            game.batch.begin();
        player.drawSprite(game.batch); villager.drawSprite(game.batch);
            game.font.draw(game.batch, "Press ENTER to go to the forest", 275, 80);
            game.batch.end();
            if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
                game.setScreen(new ForestScreen(game));
            }

            chest = new Chest(520, 180,
                    new Texture("assets/sprites/chest_closed.png"),
                    new Texture("assets/sprites/chest_open.png"));

            if(chest != null
                    && chest.getBounds().overlaps(player.getBounds())
                    && com.badlogic.gdx.Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.E)) {
                chest.onInteract();
                // TODO: เพิ่มเสียง/ปล่อยไอเท็ม/อัปเดตเควสต์ที่นี่
            }

            game.batch.begin();
            if(chest != null) chest.drawSprite(game.batch);
            game.batch.end();

            if(chest!=null && chest.getBounds().overlaps(player.getBounds())){
                game.font.draw(game.batch, "[E] Open", chest.getBounds().x-6, chest.getBounds().y+44);
            }

        }
    }
}
