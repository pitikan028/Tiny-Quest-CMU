package io.github.tinyquestcmu.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
// <<< The incorrect import line has been removed from here.

/**
 * คลาสผู้เล่นหลักของเกม Tiny Quest CMU
 * เดินได้ 4 ทิศ มีแอนิเมชัน และชื่อแสดงเหนือหัว
 */
public class Player extends Entity { // This will now work correctly
    private float speed = 120f; // ความเร็วเดิน px/s
    private Texture spriteSheet;
    private TextureRegion[][] frames;
    private int cols = 3, rows = 4; // 3 เฟรม/ทิศทาง, 4 ทิศ (ลง,ซ้าย,ขวา,ขึ้น)
    private int facing = 0; // 0=ลง,1=ซ้าย,2=ขวา,3=ขึ้น
    private float animTime = 0f;
    private String name = "Hero";

    public Player(float x, float y) {
        super(x, y, 32, 32);

        try {
            spriteSheet = new Texture("assets/player.png");
            spriteSheet.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
            int fw = spriteSheet.getWidth() / cols;
            int fh = spriteSheet.getHeight() / rows;
            frames = TextureRegion.split(spriteSheet, fw, fh);
        } catch (Exception e) {
            System.out.println("[TinyQuestCMU] Missing player.png: " + e.getMessage());
            spriteSheet = null;
        }
    }

    public void setTexture(Texture t) {
        if (this.spriteSheet != null) {
            this.spriteSheet.dispose();
        }

        this.spriteSheet = t;
        if (t != null) {
            int fw = t.getWidth() / cols;
            int fh = t.getHeight() / rows;
            frames = TextureRegion.split(t, fw, fh);
        }
    }

    public void update(float dt) {
        float dx = 0, dy = 0;

        boolean isMoving = Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A) ||
            Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D) ||
            Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W) ||
            Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S);

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

        if(isMoving) {
            animTime += dt;
        }

        x += dx;
        y += dy;

        super.update(dt);
    }

    @Override
    public void draw(ShapeRenderer shapes) {
        if (spriteSheet == null) {
            shapes.rect(x, y, w, h);
        }
    }

    public void drawSprite(SpriteBatch batch) {
        if (spriteSheet == null || frames == null) return;

        boolean isMoving = Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A) ||
            Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D) ||
            Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W) ||
            Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S);

        int frame = 0; // Stand still frame
        if(isMoving) {
            frame = ((int)(animTime * 8)) % cols; // Walking animation
        }

        TextureRegion tr = frames[facing][frame];
        batch.draw(tr, (int) x, (int) y, 32, 32);
    }

    public void drawLabel(SpriteBatch batch, com.badlogic.gdx.graphics.g2d.BitmapFont font) {
        if (font != null) {
            font.draw(batch, name, x - 4, y + 42);
        }
    }

    public void setName(String name) { this.name = name; }
    public String getName() { return name; }

    public void dispose() {
        if (spriteSheet != null) {
            spriteSheet.dispose();
        }
    }
}
