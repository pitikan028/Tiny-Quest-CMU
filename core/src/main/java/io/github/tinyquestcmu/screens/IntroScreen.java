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
    // Add Rin and Pavo back in
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
        // Use the Camera from BaseScreen, no need to create a new one
        try {
            tiledMap = new TmxMapLoader().load(MAP_FILE);
            tiledRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        } catch (Exception e) {
            System.out.println("ERROR: TMX load failed: " + MAP_FILE + " => " + e.getMessage());
        }
    }

    @Override
    public void render(float dt) {
        // สำคัญมาก: เรียก BaseScreen เพื่อจัดการ Camera และเคลียร์จอ
        // Very important: Call BaseScreen to manage the Camera and clear the screen
        super.render(dt);

        // ให้ Player จัดการการเคลื่อนไหวของตัวเอง
        // Let the Player manage its own movement
        player.update(dt);
        ds.update();

        // --- วาดแผนที่ ---
        // --- Render the map ---
        if (tiledRenderer != null) {
            tiledRenderer.setView(cam); // ใช้ cam จาก BaseScreen
            // Use cam from BaseScreen
            tiledRenderer.render();
        }

        // --- วาดตัวละครทั้งหมด ---
        // --- Draw all characters ---
        game.batch.setProjectionMatrix(cam.combined);
        game.batch.begin();

        player.drawSprite(game.batch);
        rin.drawSprite(game.batch);
        pavo.drawSprite(game.batch);

        // วาดชื่อ
        // Draw names
        player.drawLabel(game.batch, game.font);
        rin.drawLabel(game.batch, game.font);
        pavo.drawLabel(game.batch, game.font);

        game.batch.end();

        // --- วาด UI และ Dialogue ---
        // --- Draw UI and Dialogue ---
        ds.draw(shapes, game.batch, game.font, 800);

        hudBatch.begin();
        if (!ds.isActive()) {
            game.questManager.set(QuestFlag.TALKED_TO_CMULEGEND); // อาจจะต้องเปลี่ยน Flag นี้ทีหลัง
            // This Flag might need to be changed later
            game.font.draw(hudBatch, "Press ENTER to go to the bridge", 260, 80);
        }
        hudBatch.end();

        // --- ตรวจสอบ Input เพื่อเปลี่ยนฉาก ---
        // --- Handle input for changing screens ---
        if (!ds.isActive() && Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            game.setScreen(new BridgeScreen(game));
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        if (tiledRenderer != null) tiledRenderer.dispose();
        if (tiledMap != null) tiledMap.dispose();

        // เพิ่ม dispose ให้ครบทุกตัวละคร
        // Add dispose for all characters
        player.dispose();
        rin.dispose();
        pavo.dispose();
    }
}
