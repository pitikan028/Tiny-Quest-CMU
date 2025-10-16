package io.github.tinyquestcmu.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
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

public class BridgeScreen extends BaseScreen {

    private TiledMap map;
    private OrthogonalTiledMapRenderer tiledRenderer;

    // แก้ไขตำแหน่งเริ่มต้นของ Player และ NPC ให้อยู่บนสะพานในแผนที่ โดยกำหนดค่าให้แกน x และ y
    //Set the starting X and Y coordinates to place them on the bridge in the map
    private Player player = new Player(160, 100);
    private NPC legend = new NPC("Ansia", "CMU Legend", 220, 100);
    private DialogueSystem ds = new DialogueSystem();

    public BridgeScreen(TinyQuestCMUGame game) {
        super(game);
    }

    @Override
    public void show() {
        try {
            map = new TmxMapLoader().load("assets/tmx/StartingHouseMap.tmx");
            tiledRenderer = new OrthogonalTiledMapRenderer(map);
        } catch (Exception e) {
            System.out.println("[TinyQuestCMU] TMX load failed: " + e.getMessage());
        }

        try {
            legend.setTexture(new Texture("assets/sprites/ansia_sheet.png"));
        } catch (Exception e) {
            System.out.println("Missing texture: assets/ansia_sheet.png");
        }
    }

    @Override
    public void render(float dt) {
        super.render(dt);

        player.update(dt);
        ds.update();

        // เพิ่มเข้ามา: ทำให้กล้องตามผู้เล่นไปทุกที่
        //Added: Make the camera follow the player
        cam.position.set(player.getX(), player.getY(), 0);
        cam.update();
        // -----------------------------------------

        if (legend.isPlayerNear(player.getBounds()) && !ds.isActive()) {
            Dialogue d = new Dialogue(new DialogueNode(
                "I am Ansia, the goose of Chiang Mai's historical door by the bridge.",
                new DialogueNode("Go to the village and help the people by kill enemy slime.",
                    new DialogueNode("They will guide you into the forest.", null))));
            ds.start(d);
            game.questManager.set(QuestFlag.MET_LEGEND);
        }

        if (tiledRenderer != null) {
            tiledRenderer.setView(cam);
            tiledRenderer.render();
        }

        game.batch.setProjectionMatrix(cam.combined);
        game.batch.begin();
        player.drawSprite(game.batch);
        legend.drawSprite(game.batch);
        legend.drawLabel(game.batch, game.font);
        player.drawLabel(game.batch, game.font);
        game.batch.end();

        ds.draw(shapes, game.batch, game.font, 800);

        hudBatch.begin();
        if (!ds.isActive()) {
            game.font.draw(hudBatch, "Press ENTER to go to the village", 270, 80);
        }
        hudBatch.end();

        if (!ds.isActive() && Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            game.setScreen(new VillageScreen(game));
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        if (map != null) map.dispose();
        if (tiledRenderer != null) tiledRenderer.dispose();
        player.dispose();
        legend.dispose();
    }
}
