package io.github.tinyquestcmu.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import io.github.tinyquestcmu.TinyQuestCMUGame;
import io.github.tinyquestcmu.actors.Enemy;
import io.github.tinyquestcmu.actors.Player;
import io.github.tinyquestcmu.actors.TreasureChest;

public class VillageScreen extends BaseScreen {

    private static final String MAP_FILE = "assets/tmx/StartingHouseMap.tmx";

    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private Player player;
    private Rectangle exitToForest;

    private TreasureChest chest;
    private Enemy slime;

    public VillageScreen(TinyQuestCMUGame game) {
        super(game);
    }

    @Override public void show() {
        try {
            map = new TmxMapLoader().load(MAP_FILE);
            renderer = new OrthogonalTiledMapRenderer(map);

            MapProperties prop = map.getProperties();
            int mapWidth = prop.get("width", Integer.class) * prop.get("tilewidth", Integer.class);
            int mapHeight = prop.get("height", Integer.class) * prop.get("tileheight", Integer.class);

            player = new Player(150, 250);
            exitToForest = new Rectangle(mapWidth - 32, 0, 32, mapHeight);

            //แก้ไขตำแหน่งของกล่องสมบัติที่นี่
            chest = new TreasureChest(40, 100);
            slime = new Enemy(100, 100);

        } catch (Exception e) {
            System.out.println("ERROR loading VillageScreen: " + e.getMessage());
        }
    }

    @Override public void render(float dt) {
        super.render(dt);
        player.update(dt);

        // --- Handle Input ---
        boolean isNearChest = player.getBounds().overlaps(chest.getBounds());
        if (isNearChest && Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            chest.open();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            player.attack(slime);
        }

        // --- Drawing ---
        if (renderer != null) {
            renderer.setView(cam);
            renderer.render();
        }

        game.batch.setProjectionMatrix(cam.combined);
        game.batch.begin();
        player.drawSprite(game.batch);
        chest.drawSprite(game.batch);
        slime.drawSprite(game.batch);
        game.batch.end();

        // Draw health bar
        shapes.setProjectionMatrix(cam.combined);
        shapes.begin(com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled);
        slime.drawHealthBar(shapes);
        shapes.end();

        // Draw UI
        hudBatch.begin();
        if (isNearChest && !chest.isOpen()) {
            game.font.draw(hudBatch, "Press E to open", 350, 80);
        }
        game.font.draw(hudBatch, "Objective: Find the way into the forest", 20, 460);
        hudBatch.end();

        if (player.getBounds().overlaps(exitToForest)) {
            game.setScreen(new ForestScreen(game));
        }
    }

    @Override public void dispose() {
        super.dispose();
        if (renderer != null) renderer.dispose();
        if (map != null) map.dispose();
        if (player != null) player.dispose();
        if (chest != null) chest.dispose();
        if (slime != null) slime.dispose();
    }
}
