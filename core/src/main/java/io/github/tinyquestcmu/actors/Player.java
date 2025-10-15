package io.github.tinyquestcmu.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * คลาสผู้เล่นหลักของเกม Tiny Quest CMU
 * เดินได้ 4 ทิศ มีแอนิเมชัน และชื่อแสดงเหนือหัว
 */
public class Player extends Entity {
    private float speed = 120f; // ความเร็วเดิน px/s
    private Texture spriteSheet;
    private TextureRegion[][] frames;
    private int cols = 3, rows = 4; // 3 เฟรม/ทิศทาง, 4 ทิศ (ลง,ซ้าย,ขวา,ขึ้น)
    private int facing = 0; // 0=ลง,1=ซ้าย,2=ขวา,3=ขึ้น
    private float animTime = 0f;
    private String name = "Hero"; // ✅ ชื่อผู้เล่นแสดงเหนือหัว

    public Player(float x, float y) {
        super(x, y, 32, 32);

        // ✅ โหลดสไปรต์ชีตเริ่มต้น
        try {
            spriteSheet = new Texture("assets/sprites/player_sheet.png");
            spriteSheet.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
            int fw = spriteSheet.getWidth() / cols;
            int fh = spriteSheet.getHeight() / rows;
            frames = TextureRegion.split(spriteSheet, fw, fh);
        } catch (Exception e) {
            System.out.println("[TinyQuestCMU] Missing player_sheet.png: " + e.getMessage());
            spriteSheet = null;
        }
    }

    // ฟังก์ชันเปลี่ยน texture (กรณีเปลี่ยนชุด/ตัวละคร)
    public void setTexture(Texture t) {
        this.spriteSheet = t;
        if (t != null) {
            int fw = t.getWidth() / cols;
            int fh = t.getHeight() / rows;
            frames = TextureRegion.split(t, fw, fh);
        }
    }

    // อัปเดตตำแหน่งและทิศทาง
    public void update(float dt) {
        float dx = 0, dy = 0;
        animTime += dt;

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            dx -= speed * dt;
            facing = 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            dx += speed * dt;
            facing = 2;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            dy += speed * dt;
            facing = 3;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            dy -= speed * dt;
            facing = 0;
        }

        // อัปเดตตำแหน่ง
        x += dx;
        y += dy;

        // อัปเดต bounds จาก Entity
        super.update(dt);
    }

    @Override
    public void draw(ShapeRenderer shapes) {
        if (spriteSheet == null) {
            shapes.rect(x, y, w, h);
        }
    }

    // ✅ วาดตัวละครตามเฟรมปัจจุบัน
    public void drawSprite(SpriteBatch batch) {
        if (spriteSheet != null) {
            // ปรับขนาดการวาดให้เล็กลง (เช่น 32x32 หรือ 64x64)
            batch.draw(spriteSheet, x, y, 32, 32); // แทนที่จะใช้ขนาดเต็มของ texture
        }
    }

    // ✅ วาดชื่อผู้เล่นเหนือหัว (แก้ error ใน BridgeScreen)
    public void drawLabel(SpriteBatch batch, com.badlogic.gdx.graphics.g2d.BitmapFont font) {
        if (font != null) {
            font.draw(batch, name, x - 4, y + 42);
        }
    }

    // ✅ ตั้งชื่อผู้เล่น (เผื่อเปลี่ยนตอนเริ่มเกม)
    public void setName(String name) { this.name = name; }
    public String getName() { return name; }
}
