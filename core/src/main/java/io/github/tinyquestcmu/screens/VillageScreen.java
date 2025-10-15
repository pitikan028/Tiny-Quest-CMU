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

    // ★ 1. ประกาศตัวแปรสำหรับ Object ใหม่ ★
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

            // ★ 2. สร้าง Object ใหม่และกำหนดตำแหน่ง ★
            chest = new TreasureChest(200, 150);
            slime = new Enemy(250, 200);

        } catch (Exception e) {
            System.out.println("ERROR loading VillageScreen: " + e.getMessage());
        }
    }

    @Override public void render(float dt) {
        super.render(dt);
        player.update(dt);

        // ★ 3. เพิ่ม Logic การควบคุม ★
        // เปิดกล่อง
        boolean isNearChest = player.getBounds().overlaps(chest.getBounds());
        if (isNearChest && Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            chest.open();
        }
        // โจมตี
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            player.attack(slime);
        }

        // --- วาดทุกอย่าง ---
        if (renderer != null) {
            renderer.setView(cam);
            renderer.render();
        }

        game.batch.setProjectionMatrix(cam.combined);
        game.batch.begin();
        player.drawSprite(game.batch);
        chest.drawSprite(game.batch); // วาดกล่อง
        slime.drawSprite(game.batch); // วาดศัตรู
        game.batch.end();

        // วาดหลอดเลือด
        shapes.setProjectionMatrix(cam.combined);
        shapes.begin(com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled);
        slime.drawHealthBar(shapes);
        shapes.end();

        // วาด UI
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
        // ★ 4. อย่าลืม dispose Object ใหม่ ★
        if (chest != null) chest.dispose();
        if (slime != null) slime.dispose();
    }
}
