package io.github.tinyquestcmu.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import io.github.tinyquestcmu.TinyQuestCMUGame;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import io.github.tinyquestcmu.actors.Player;
import io.github.tinyquestcmu.actors.NPC;
import io.github.tinyquestcmu.dialogue.*;
import io.github.tinyquestcmu.quest.*;

import com.badlogic.gdx.utils.GdxRuntimeException;

public class RescueScreen extends BaseScreen {
    private TiledMap map;
    private OrthogonalTiledMapRenderer tiledRenderer;

    private Player player = new Player(200, 180);
    private NPC brother = new NPC("Brother", "Lost Sibling", 200, 220); // ปรับตำแหน่ง y ให้อยู่ในรั้ว
    // Adjust y-position to be inside the fence
    private DialogueSystem ds = new DialogueSystem();
    private boolean freed = false;

    public RescueScreen(TinyQuestCMUGame game){
        super(game);
    }

    @Override
    public void show (){
        map = new TmxMapLoader().load("assets/tmx/BrotherMap.tmx");
        tiledRenderer = new OrthogonalTiledMapRenderer(map);
    }

    @Override
    public void render(float dt) {
        // 1. เรียก render ของ BaseScreen เพื่อตั้งค่า Viewport และเคลียร์จอ
        // 1. Call BaseScreen's render to set up the Viewport and clear the screen
        super.render(dt);

        // 2. อัปเดตสถานะต่างๆ
        // 2. Update various statuses
        player.update(dt);
        ds.update();

        // เพิ่มเข้ามา: ทำให้กล้องตามผู้เล่น
        // Added: Make the camera follow the player
        cam.position.set(player.getX(), player.getY(), 0);
        cam.update();
        // ------------------------------------

        // 3. วาดแผนที่ TMX
        // 3. Render the TMX map
        tiledRenderer.setView(cam);
        tiledRenderer.render();

        // 4. วาด Sprites (ใช้ batch ของเกมที่เลื่อนตามกล้อง)
        // 4. Draw Sprites (use the game's batch that moves with the camera)
        game.batch.setProjectionMatrix(cam.combined);
        game.batch.begin();
        player.drawSprite(game.batch);
        brother.drawSprite(game.batch);
        brother.drawLabel(game.batch, game.font);
        player.drawLabel(game.batch, game.font);
        game.batch.end();

        // 5. วาด UI (ใช้ hudBatch ที่ไม่เลื่อนตามกล้อง)
        // 5. Draw UI (use hudBatch that doesn't move with the camera)
        hudBatch.begin();
        if (freed && !ds.isActive()) {
            game.font.draw(hudBatch, "Press ENTER to return home", 290, 84);
        }
        // วาด Dialogue ถ้ามี
        // Draw Dialogue if active
        ds.draw(shapes, game.batch, game.font, 800);
        hudBatch.end();


        // 6. จัดการ Logic ของเกม
        // 6. Handle game logic
        boolean near = brother.isPlayerNear(player.getBounds());
        if (near && !ds.isActive() && !freed) {
            Dialogue d = new Dialogue(new DialogueNode(
                "You found me! The tree protected me.",
                new DialogueNode("I'm free now—let's go home.", null)));
            ds.start(d);
            freed = true;
            game.questManager.set(QuestFlag.FREED_BROTHER);
        }

        // 7. ตรวจสอบ Input เพื่อเปลี่ยนหน้าจอ
        // 7. Check for input to change the screen
        if (freed && !ds.isActive() && Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            game.setScreen(new EndingScreen(game));
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        if (map != null) map.dispose();
        if (tiledRenderer != null) tiledRenderer.dispose();

        // เพิ่มเข้ามา: dispose ตัวละครทั้งหมด
        // Added: dispose all characters
        if (player != null) player.dispose();
        if (brother != null) brother.dispose();
    }
}
