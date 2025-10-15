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
    private NPC brother = new NPC("Brother", "Lost Sibling", 200, 100); // ปรับตำแหน่ง brother ให้อยู่ในฉาก
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
        // 1. เรียก render ของ BaseScreen เพื่อตั้งค่ากล้องและเคลียร์จอ
        super.render(dt);

        // 2. อัปเดตสถานะต่างๆ
        player.update(dt);
        ds.update();

        // 3. วาดแผนที่ TMX
        tiledRenderer.setView(cam);
        tiledRenderer.render();

        // 4. วาด Sprites และ UI ทั้งหมดในรอบเดียว
        game.batch.begin();

        player.drawSprite(game.batch);
        brother.drawSprite(game.batch);
        brother.drawLabel(game.batch, game.font);
        player.drawLabel(game.batch, game.font);

        if (freed && !ds.isActive()) {
            game.font.draw(game.batch, "Press ENTER to return home", 290, 84);
        }

        // วาด Dialogue ถ้ามี
//        ds.draw(game.batch, game.font);

        game.batch.end();

        // 5. จัดการ Logic ของเกม
        boolean near = brother.isPlayerNear(player.getBounds());
        if (near && !ds.isActive() && !freed) {
            Dialogue d = new Dialogue(new DialogueNode(
                    "You found me! The tree protected me.",
                    new DialogueNode("I'm free now—let's go home.", null)));
            ds.start(d);
            freed = true;
            game.questManager.set(QuestFlag.FREED_BROTHER);
        }

        // 6. ตรวจสอบ Input เพื่อเปลี่ยนหน้าจอ
        if (freed && !ds.isActive() && Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            game.setScreen(new EndingScreen(game));
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        if (map != null) {
            map.dispose();
        }
        if (tiledRenderer != null) {
            tiledRenderer.dispose();
        }
    }
}
