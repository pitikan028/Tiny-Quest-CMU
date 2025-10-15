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

        // สร้าง Player ขึ้นมา แล้วปล่อยให้ constructor ของ Player โหลด Texture เอง
        player = new Player(startX, startY);

        // <<< ลบบรรทัดนี้ออก >>>
        // player.setTexture(new Texture("assets/player.png"));
        // เพราะ Player โหลด Texture ของตัวเองอยู่แล้ว ไม่ต้องโหลดซ้ำ
    }

    @Override
    public void render(float dt) {
        super.render(dt);

        player.update(dt);

        tiledRenderer.setView(cam);
        tiledRenderer.render();

        // ใช้ Projection Matrix ของกล้อง เพื่อให้ UI ที่วาดเลื่อนตามกล้อง
        game.batch.setProjectionMatrix(cam.combined);
        game.batch.begin();
        player.drawSprite(game.batch);
        specialTree.drawLabel(game.batch, game.font);
        player.drawLabel(game.batch, game.font);

        boolean nearTree = specialTree.getBounds().overlaps(player.getBounds());
        if (nearTree) {
            game.font.draw(game.batch, "Press ENTER to proceed", player.getX() - 80, player.getY() - 20);
            game.questManager.set(QuestFlag.FOUND_SPECIAL_TREE);
        }
        game.batch.end();

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

        // บรรทัดนี้จะทำงานได้ถูกต้องแล้ว
        if (player != null) {
            player.dispose();
        }
    }
}
