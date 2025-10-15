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

public class BridgeScreen extends BaseScreen {
    private Texture loadTex(String path){
        try { return new Texture(path); } catch (GdxRuntimeException e){ System.out.println("[TinyQuestCMU] Missing texture: "+path+" => "+e.getMessage()); return null; }
    }

    private TileMap map;
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer tiledRenderer;
    private Texture tGrass, tDirt, tWater, tBridge, tHouse, tTree, tSpecial;
    private Texture texPlayer;
    private Player player = new Player(100, 200);
    // Themed legend: Kawin (White Elephant) near CMU bridge
    private NPC legend = new NPC("Kawin", "CMU Legend", 360, 220);
    private DialogueSystem ds = new DialogueSystem();

    public BridgeScreen(TinyQuestCMUGame game){
        super(game);
    }

    @Override public void show() {

        // Load Tiled map
        try {
            tiledMap = new TmxMapLoader().load("assets/tmx/StartingHouseMap.tmx");
            tiledRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1f);
        } catch (Exception e) {
            System.out.println("[TinyQuestCMU] TMX load failed: bridge => " + e.getMessage());
        }

        legend.setTexture(loadTex("assets/kawin_white_elephant.png"));

        tGrass=loadTex("assets/grass.png"); tDirt=loadTex("assets/dirt.png"); tWater=loadTex("assets/water.png"); tBridge=loadTex("assets/bridge.png"); tHouse=loadTex("assets/house.png"); tTree=loadTex("assets/tree.png"); tSpecial=loadTex("assets/special_tree.png"); texPlayer=loadTex("assets/player.png"); player.setTexture(texPlayer); legend.setTexture(loadTex("assets/kawin_white_elephant.png"));
        int[][] data = new int[15][25];
        for(int y=0;y<15;y++){ for(int x=0;x<25;x++){ data[y][x]=1; } }
        for(int x=0;x<25;x++){ data[6][x]=3; data[7][x]=3; }
        for(int x=8;x<17;x++){ data[7][x]=4; }
        map = new TileMap(data,tGrass,tDirt,tWater,tBridge,tHouse,tTree,tSpecial);
    }

    @Override public void render(float dt) {
        clear(0.05f,0.12f,0.18f);
        player.update(dt);

        // draw TMX map (if available)
        if(tiledRenderer != null){
            tiledRenderer.setView(cam);
            tiledRenderer.render();
        }
        // draw map + sprites
        if(map!=null){
            game.batch.begin();
            player.drawSprite(game.batch); legend.drawSprite(game.batch);
            map.draw(game.batch);
            game.batch.end();
        }


        if(legend.isPlayerNear(player.getBounds()) && !ds.isActive()){
            Dialogue d = new Dialogue(new DialogueNode(
                "I am Kawin, the White Elephant—guardian by the bridge.",
                new DialogueNode("Go to the village and help the people.",
                    new DialogueNode("They will guide you into the forest.", null))));
            ds.start(d);
            game.questManager.set(QuestFlag.MET_LEGEND);
        }
        ds.update();

        shapes.begin(com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled);
        shapes.setColor(Color.SKY); shapes.rect(0,0,800,480); // sky
        shapes.setColor(Color.NAVY); shapes.rect(0,180,800,40); // river
        shapes.setColor(Color.FOREST); shapes.rect(340,180,120,20); // bridge
        shapes.setColor(Color.ORANGE); legend.draw(shapes);
        shapes.setColor(Color.WHITE); player.draw(shapes);
        shapes.end();

        game.batch.begin();
        player.drawSprite(game.batch); legend.drawSprite(game.batch);
        legend.drawLabel(game.batch, game.font);
        player.drawLabel(game.batch, game.font);
        game.batch.end();

        ds.draw(shapes, game.batch, game.font, 800);
//        drawHud();

        if(!ds.isActive()){
            game.batch.begin();
            player.drawSprite(game.batch); legend.drawSprite(game.batch);
            game.font.draw(game.batch, "Press ENTER to go to the village", 270, 80);
            game.batch.end();
            if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
                game.setScreen(new VillageScreen(game));
            }
        }
    }
}
