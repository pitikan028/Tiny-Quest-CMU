package io.github.tinyquestcmu.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.GdxRuntimeException;
import io.github.tinyquestcmu.TinyQuestCMUGame;
import io.github.tinyquestcmu.actors.NPC;
import io.github.tinyquestcmu.actors.Player;
import io.github.tinyquestcmu.dialogue.Dialogue;
import io.github.tinyquestcmu.dialogue.DialogueNode;
import io.github.tinyquestcmu.dialogue.DialogueSystem;
import io.github.tinyquestcmu.quest.QuestFlag;

public class IntroScreen extends BaseScreen {

    private static final String MAP_FILE = "assets/tmx/village.tmx"; // เปลี่ยนเป็น village.tmx ได้

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

    // ขนาดแมพ (พิกเซล)
    private int mapWidthPx, mapHeightPx;

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

        try {
            tiledMap = new TmxMapLoader().load(MAP_FILE);
            tiledRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1f);

            // คำนวณขนาดแมพจาก TMX
            MapProperties p = tiledMap.getProperties();
            int mapWidthTiles  = p.get("width", Integer.class);
            int mapHeightTiles = p.get("height", Integer.class);
            int tileWidth      = p.get("tilewidth", Integer.class);
            int tileHeight     = p.get("tileheight", Integer.class);
            mapWidthPx  = mapWidthTiles  * tileWidth;
            mapHeightPx = mapHeightTiles * tileHeight;

        } catch (Exception e) {
            System.out.println("[TinyQuestCMU] TMX load failed: " + MAP_FILE + " => " + e.getMessage());
            tiledMap = null;
            tiledRenderer = null;
        }

        // ผู้เล่น/ผู้ปกครอง
        Texture texPlayer = loadTex("assets/sprites/player_sheet.png");
        if (texPlayer != null) {
            texPlayer.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
            player.setTexture(texPlayer);
        }
        Texture texParent = loadTex("assets/sprites/parent.png");
        if (texParent != null) parent.setTexture(texParent);
    }

    @Override
    public void render(float dt) {
        clear(0.1f, 0.1f, 0.12f);

        // อัปเดตผู้เล่น
        player.update(dt);

        // กล้องติดตาม + CLAMP ไม่ให้หลุดนอกแมพ
        float camHalfW = cam.viewportWidth  * 0.5f;
        float camHalfH = cam.viewportHeight * 0.5f;
        float targetX = player.getX() + 16; // กะกลางตัวละคร (tile 32 => half 16)
        float targetY = player.getY() + 16;

        // ถ้าอยากโฟกัสทั้งแมพทันทีสามารถใช้ cam.zoom > 1 ได้ เช่น cam.zoom = 1.2f;

        // clamp ขอบ: [camHalfW, mapWidthPx - camHalfW]
        float clampedX = MathUtils.clamp(targetX, camHalfW, Math.max(camHalfW, mapWidthPx - camHalfW));
        float clampedY = MathUtils.clamp(targetY, camHalfH, Math.max(camHalfH, mapHeightPx - camHalfH));
        cam.position.set(clampedX, clampedY, 0);
        cam.update();

        // ---------- วาด TMX ----------
        if (tiledRenderer != null) {
            // อย่ารีเซ็ตเป็นกล้องใหม่: ลบบรรทัดนี้ทิ้ง
            // tiledRenderer.setView(new OrthographicCamera());

            // ใช้กล้องเดียวกันเสมอ
            tiledRenderer.setView(cam);
            tiledRenderer.render();
        }

        // ---------- วาดสไปรต์ทับแผนที่ ----------
        game.batch.setProjectionMatrix(cam.combined);
        game.batch.begin();
        player.drawSprite(game.batch);
        parent.drawLabel(game.batch, game.font);
        player.drawLabel(game.batch, game.font);
        game.batch.end();

        // ---------- วาด UI/Dialogue ----------
        ds.update();
        ds.draw(shapes, game.batch, game.font, 800);
//        drawHud();

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
