package io.github.tinyquestcmu.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import io.github.tinyquestcmu.TinyQuestCMUGame;
import io.github.tinyquestcmu.actors.Player;

public class VillageScreen extends BaseScreen {

    private static final String MAP_FILE = "assets/tmx/StartingHouseMap.tmx";

    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private float mapWidth;
    private float mapHeight;

    private Player player;

    // ประตูไปป่า (อยู่ขอบขวาของ Village)
    private Rectangle exitToForest;

    public VillageScreen(TinyQuestCMUGame game) {
        super(game);
    }

    private Texture loadTex(String path){
        try { return new Texture(path); }
        catch (GdxRuntimeException e){ System.out.println("[Village] Missing texture: " + path); return null; }
    }

    @Override public void show() {
        // --- ใช้ cam และ viewport จาก BaseScreen ไม่ต้องสร้างใหม่ ---

        map = new TmxMapLoader().load(MAP_FILE);
        renderer = new OrthogonalTiledMapRenderer(map); // ไม่ต้องใช้ unitScale

        // คำนวณขนาดแมพเป็น pixel
        MapProperties prop = map.getProperties();
        int mapWidthInTiles = prop.get("width", Integer.class);
        int mapHeightInTiles = prop.get("height", Integer.class);
        int tilePixelWidth = prop.get("tilewidth", Integer.class);
        int tilePixelHeight = prop.get("tileheight", Integer.class);
        mapWidth = mapWidthInTiles * tilePixelWidth;
        mapHeight = mapHeightInTiles * tilePixelHeight;

        player = new Player(150, 250); // กำหนดตำแหน่งเริ่มต้นในแผนที่
        player.setTexture(loadTex("assets/player.png")); // ตั้งค่า texture player

        // สร้างประตูไปป่า ใช้หน่วยเป็น pixel
        exitToForest = new Rectangle(mapWidth - 32, 0, 32, mapHeight);
    }

    @Override public void render(float dt) {
        // 1. เรียก BaseScreen เพื่อจัดการเคลียร์จอและ Camera
        super.render(dt);

        // 2. อัปเดต Player
        player.update(dt);

        // 3. วาดแผนที่ TMX
        renderer.setView(cam); // ใช้ cam จาก BaseScreen
        renderer.render();

        // 4. วาดผู้เล่น
        game.batch.setProjectionMatrix(cam.combined); // สำคัญ: บอกให้ batch วาดตามกล้อง
        game.batch.begin();
        player.drawSprite(game.batch);
        game.batch.end();

        // 5. วาด Objective
        drawHud();

        // 6. เช็คประตูเพื่อเปลี่ยนฉาก
        if (player.getBounds().overlaps(exitToForest)) {
            // เราไม่ต้องส่งตำแหน่งไปแล้ว เพราะ ForestScreen จะมีตำแหน่งเริ่มต้นของตัวเอง
            game.setScreen(new ForestScreen(game));
        }
    }

    // เพิ่มเมธอดวาด Objective เข้ามา
    private void drawHud() {
        // ใช้ batch คนละอันกับ game.batch เพื่อให้พิกัดอ้างอิงกับหน้าจอเสมอ ไม่เลื่อนตามกล้อง
        hudBatch.begin();
        game.font.draw(hudBatch, "Objective: Find the way into the forest", 20, 460);
        hudBatch.end();
    }


    @Override public void dispose() {
        super.dispose();
        if (renderer != null) renderer.dispose();
        if (map != null) map.dispose();
        player.dispose(); // อย่าลืม dispose texture ของ player
    }
}
