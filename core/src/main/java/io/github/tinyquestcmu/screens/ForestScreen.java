package io.github.tinyquestcmu.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.GdxRuntimeException;
import io.github.tinyquestcmu.TinyQuestCMUGame;
import io.github.tinyquestcmu.actors.NPC;
import io.github.tinyquestcmu.actors.Player;
import io.github.tinyquestcmu.quest.QuestFlag;

public class ForestScreen extends BaseScreen {

    private TiledMap map;
    private OrthogonalTiledMapRenderer tiledRenderer;

    private Player player;
    private NPC specialTree = new NPC("Special Tree", "Glowing Bark", 400, 220);
    private float startX, startY;

    public ForestScreen(TinyQuestCMUGame game) {
        this(game, 60, 240);
    }

    public ForestScreen(TinyQuestCMUGame game, float startX, float startY) {
        super(game);
        this.startX = startX;
        this.startY = startY;
    }

    @Override
    public void show() {
        map = new TmxMapLoader().load("assets/tmx/ForestMap.tmx");
        tiledRenderer = new OrthogonalTiledMapRenderer(map);
        player = new Player(startX, startY);
    }

    @Override
    public void render(float dt) {
        // ★ 1. เรียก BaseScreen เพื่อจัดการ Viewport และเคลียร์จอ ★
        super.render(dt);

        player.update(dt);

        // ★ 2. เพิ่มโค้ดให้กล้องตามผู้เล่น ★
        cam.position.set(player.getX(), player.getY(), 0);
        cam.update();
        // ------------------------------------

        // --- วาดแผนที่ ---
        tiledRenderer.setView(cam);
        tiledRenderer.render();

        // --- วาดตัวละคร (ใช้ batch ของเกมที่เลื่อนตามกล้อง) ---
        game.batch.setProjectionMatrix(cam.combined);
        game.batch.begin();
        player.drawSprite(game.batch);
        specialTree.drawSprite(game.batch); // ควรวาด sprite ของ NPC ด้วย
        player.drawLabel(game.batch, game.font);
        specialTree.drawLabel(game.batch, game.font);
        game.batch.end();

        // --- วาด UI (ใช้ hudBatch ที่ไม่เลื่อนตามกล้อง) ---
        hudBatch.begin();
        boolean nearTree = specialTree.getBounds().overlaps(player.getBounds());
        if (nearTree) {
            game.font.draw(hudBatch, "Press ENTER to proceed", 310, 84);
            game.questManager.set(QuestFlag.FOUND_SPECIAL_TREE);
        }
        hudBatch.end();


        if (nearTree && Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            game.setScreen(new RescueScreen(game));
        }

        if (player.getX() < 10) {
            game.setScreen(new VillageScreen(game));
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        if (map != null) map.dispose();
        if (tiledRenderer != null) tiledRenderer.dispose();
        if (player != null) player.dispose();
        specialTree.dispose();
    }
}
