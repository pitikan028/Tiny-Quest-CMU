package io.github.tinyquestcmu.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import io.github.tinyquestcmu.TinyQuestCMUGame;
import io.github.tinyquestcmu.actors.NPC;
import io.github.tinyquestcmu.actors.Player;
import io.github.tinyquestcmu.dialogue.Dialogue;
import io.github.tinyquestcmu.dialogue.DialogueNode;
import io.github.tinyquestcmu.dialogue.DialogueSystem;
import io.github.tinyquestcmu.quest.QuestFlag;

public class IntroScreen extends BaseScreen {
    private static final String MAP_FILE = "assets/tmx/village.tmx";

    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer tiledRenderer;

    private Player player = new Player(380, 200);
    // เพิ่ม Rin และ Pavo กลับเข้ามา
    private NPC rin = new NPC("Rin", "Engineer", 240, 240);
    private NPC pavo = new NPC("Pavo", "Peaclock", 560, 240);
    private DialogueSystem ds = new DialogueSystem();

    public IntroScreen(TinyQuestCMUGame game) {
        super(game);
        Dialogue d = new Dialogue(
            new DialogueNode("Your brother is missing… you must find him.",
                new DialogueNode("He was last seen near the bridge at CMU.",
                    new DialogueNode("Seek the legend there for guidance.", null)))
        );
        ds.start(d);
    }

    @Override
    public void show() {
        // ใช้ Camera จาก BaseScreen, ไม่ต้องสร้างใหม่
        try {
            tiledMap = new TmxMapLoader().load(MAP_FILE);
            tiledRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        } catch (Exception e) {
            System.out.println("ERROR: TMX load failed: " + MAP_FILE + " => " + e.getMessage());
        }
    }

    @Override
    public void render(float dt) {
        super.render(dt); // ✨ สำคัญมาก: เรียก BaseScreen เพื่อจัดการ Camera และเคลียร์จอ

        // ✨ ให้ Player จัดการการเคลื่อนไหวของตัวเอง ✨
        player.update(dt);
        ds.update();

        // --- วาดแผนที่ ---
        if (tiledRenderer != null) {
            tiledRenderer.setView(cam); // ใช้ cam จาก BaseScreen
            tiledRenderer.render();
        }

        // --- วาดตัวละครทั้งหมด ---
        game.batch.setProjectionMatrix(cam.combined);
        game.batch.begin();

        player.drawSprite(game.batch);
        rin.drawSprite(game.batch);
        pavo.drawSprite(game.batch);

        // วาดชื่อ
        player.drawLabel(game.batch, game.font);
        rin.drawLabel(game.batch, game.font);
        pavo.drawLabel(game.batch, game.font);

        game.batch.end();

        // --- วาด UI และ Dialogue ---
        ds.draw(shapes, game.batch, game.font, 800);

        hudBatch.begin();
        if (!ds.isActive()) {
            game.questManager.set(QuestFlag.TALKED_TO_PARENT); // อาจจะต้องเปลี่ยน Flag นี้ทีหลัง
            game.font.draw(hudBatch, "Press ENTER to go to the bridge", 260, 80);
        }
        hudBatch.end();

        if (!ds.isActive() && Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            game.setScreen(new BridgeScreen(game));
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        if (tiledRenderer != null) tiledRenderer.dispose();
        if (tiledMap != null) tiledMap.dispose();

        //  เพิ่ม dispose ให้ครบทุกตัวละคร
        player.dispose();
        rin.dispose();
        pavo.dispose();
    }
}
