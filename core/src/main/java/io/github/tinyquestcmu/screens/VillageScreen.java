package io.github.tinyquestcmu.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import io.github.tinyquestcmu.TinyQuestCMUGame;
import io.github.tinyquestcmu.actors.Player;

public class VillageScreen extends BaseScreen {

    private static final String MAP_FILE = "assets/tmx/StartingHouseMap.tmx";

    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private float mapWidth;
    private float mapHeight;

    private Player player;

    private Rectangle exitToForest;

    public VillageScreen(TinyQuestCMUGame game) {
        super(game);
    }

    @Override public void show() {
        try {
            map = new TmxMapLoader().load(MAP_FILE);
            renderer = new OrthogonalTiledMapRenderer(map);

            MapProperties prop = map.getProperties();
            int mapWidthInTiles = prop.get("width", Integer.class);
            int mapHeightInTiles = prop.get("height", Integer.class);
            int tilePixelWidth = prop.get("tilewidth", Integer.class);
            int tilePixelHeight = prop.get("tileheight", Integer.class);
            mapWidth = mapWidthInTiles * tilePixelWidth;
            mapHeight = mapHeightInTiles * tilePixelHeight;

            player = new Player(150, 250);

            // ★ ลบบรรทัดนี้ออก เพราะ Player โหลด Texture เองแล้ว ★
            // player.setTexture(loadTex("assets/player.png"));

            exitToForest = new Rectangle(mapWidth - 32, 0, 32, mapHeight);
        } catch (Exception e) {
            System.out.println("ERROR loading VillageScreen: " + e.getMessage());
        }
    }

    @Override public void render(float dt) {
        super.render(dt);

        player.update(dt);

        if (renderer != null) {
            renderer.setView(cam);
            renderer.render();
        }

        game.batch.setProjectionMatrix(cam.combined);
        game.batch.begin();
        player.drawSprite(game.batch);
        game.batch.end();

        drawHud();

        if (player.getBounds().overlaps(exitToForest)) {
            game.setScreen(new ForestScreen(game));
        }
    }

    private void drawHud() {
        hudBatch.begin();
        game.font.draw(hudBatch, "Objective: Find the way into the forest", 20, 460);
        hudBatch.end();
    }

    @Override public void dispose() {
        super.dispose();
        if (renderer != null) renderer.dispose();
        if (map != null) map.dispose();
        if (player != null) player.dispose();
    }
}
