package io.github.tinyquestcmu.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.GdxRuntimeException;
import io.github.tinyquestcmu.TinyQuestCMUGame;
import io.github.tinyquestcmu.actors.NPC;
import io.github.tinyquestcmu.actors.Player;
import io.github.tinyquestcmu.dialogue.Dialogue;
import io.github.tinyquestcmu.dialogue.DialogueNode;
import io.github.tinyquestcmu.dialogue.DialogueSystem;
import io.github.tinyquestcmu.quest.QuestFlag;

public class IntroScreen extends BaseScreen {

    // ---------- CONFIG: แก้ชื่อไฟล์ TMX ที่นี่ให้ตรงกับ assets/tmx ของคุณ ----------
    private static final String MAP_FILE = "assets/tmx/bridge.tmx";
    // ตัวอย่างถ้าจะใช้ village: private static final String MAP_FILE = "assets/tmx/village.tmx";
    // ---------------------------------------------------------------------------

    private Texture loadTex(String path){
        try { return new Texture(path); }
        catch (GdxRuntimeException e){
            System.out.println("[TinyQuestCMU] Missing texture: "+path+" => "+e.getMessage());
            return null;
        }
    }

    private OrthographicCamera cam;
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer tiledRenderer;

    private Player player = new Player(380, 200);
    private NPC parent = new NPC("Parent", "Mom/Dad", 380, 240);
    private DialogueSystem ds = new DialogueSystem();

    public IntroScreen(TinyQuestCMUGame game){
        super(game);
        Dialogue d = new Dialogue(
                new DialogueNode("Honey, your brother is missing… Please find him.",
                        new DialogueNode("Last seen near the bridge at CMU.",
                                new DialogueNode("Seek the legend there—she will guide you.", null)))
        );
        ds.start(d);
    }

    @Override
    public void show() {
        // กล้อง
        cam = new OrthographicCamera();
        cam.setToOrtho(false, 800, 480);

        // โหลด TMX
        try {
            tiledMap = new TmxMapLoader().load(MAP_FILE);
            tiledRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1f);
        } catch (Exception e) {
            System.out.println("[TinyQuestCMU] TMX load failed: " + MAP_FILE + " => " + e.getMessage());
            tiledMap = null;
            tiledRenderer = null;
        }

        // เท็กซ์เจอร์ผู้เล่น/ผู้ปกครอง (ถ้าไม่มีจะวาดเป็นสี่เหลี่ยมแทน)
        Texture texPlayer = loadTex("assets/sprites/player_sheet.png");
        if (texPlayer != null) {
            texPlayer.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
            player.setTexture(texPlayer);
        }
        Texture texParent = loadTex("assets/sprites/parent.png"); // ถ้าไม่มีได้
        if (texParent != null) parent.setTexture(texParent);
    }

    @Override
    public void render(float dt) {
        clear(0.1f, 0.1f, 0.12f);

        // อัปเดตผู้เล่น + กล้องติดตาม gggg
        player.update(dt);
        cam.position.set(player.getX() + 16, player.getY() + 16, 0);
        cam.update();

        tiledRenderer.setView(new OrthographicCamera());

        // ---------- วาด TMX ----------
        if (tiledRenderer != null) {
            tiledRenderer.setView(cam);
            tiledRenderer.render();
        }

        // ---------- วาดสไปรต์ทับแผนที่ ----------
        game.batch.setProjectionMatrix(cam.combined);
        game.batch.begin();
        player.drawSprite(game.batch);
        parent.drawLabel(game.batch, game.font); // ชื่อ parent (ถ้าตำแหน่งอยู่ในฉาก)
        player.drawLabel(game.batch, game.font);
        game.batch.end();

        // ---------- วาด UI/Dialogue ----------
        ds.update();
        ds.draw(shapes, game.batch, game.font, 800);
        drawHud();

        // ---------- ข้ามฉากเมื่อจบบทสนทนา ----------
        if (!ds.isActive()) {
            game.questManager.set(QuestFlag.TALKED_TO_PARENT);
            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                game.setScreen(new BridgeScreen(game));
            }
            game.batch.begin();
            game.font.draw(game.batch, "Press ENTER to go to the bridge", 260, 80);
            game.batch.end();
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        if (tiledRenderer != null) tiledRenderer.dispose();
        if (tiledMap != null) tiledMap.dispose();
    }
}
