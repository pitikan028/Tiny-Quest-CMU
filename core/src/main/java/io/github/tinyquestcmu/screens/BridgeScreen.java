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

    private Player player = new Player(100, 200);
    private NPC legend = new NPC("Kawin", "CMU Legend", 360, 220);
    private DialogueSystem ds = new DialogueSystem();

    public BridgeScreen(TinyQuestCMUGame game) {
        super(game);
    }

    @Override
    public void show() {
        // Load the TMX map for this screen
        try {
            map = new TmxMapLoader().load("assets/tmx/secondmap.tmx");
            tiledRenderer = new OrthogonalTiledMapRenderer(map);
        } catch (Exception e) {
            System.out.println("[TinyQuestCMU] TMX load failed: " + e.getMessage());
        }

        // The Player class loads its own texture. We only need to load the legend's.
        try {
            legend.setTexture(new Texture("assets/kawin_white_elephant.png"));
        } catch (Exception e) {
            System.out.println("Missing texture: assets/kawin_white_elephant.png");
        }
    }

    @Override
    public void render(float dt) {
        // 1. Let BaseScreen handle the camera and clearing the screen
        super.render(dt);

        // --- UPDATE LOGIC ---
        player.update(dt);
        ds.update();

        if (legend.isPlayerNear(player.getBounds()) && !ds.isActive()) {
            Dialogue d = new Dialogue(new DialogueNode(
                "I am Kawin, the White Elephant—guardian by the bridge.",
                new DialogueNode("Go to the village and help the people.",
                    new DialogueNode("They will guide you into the forest.", null))));
            ds.start(d);
            game.questManager.set(QuestFlag.MET_LEGEND);
        }

        // --- DRAWING ---
        // 2. Render the TMX map
        if (tiledRenderer != null) {
            tiledRenderer.setView(cam);
            tiledRenderer.render();
        }

        // 3. Draw sprites and labels
        game.batch.setProjectionMatrix(cam.combined);
        game.batch.begin();
        player.drawSprite(game.batch);
        legend.drawSprite(game.batch);
        legend.drawLabel(game.batch, game.font);
        player.drawLabel(game.batch, game.font);
        game.batch.end();

        // 4. Draw UI (Dialogue and HUD)
        ds.draw(shapes, game.batch, game.font, 800);

        hudBatch.begin();
        if (!ds.isActive()) {
            game.font.draw(hudBatch, "Press ENTER to go to the village", 270, 80);
        }
        hudBatch.end();

        // --- INPUT HANDLING ---
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
