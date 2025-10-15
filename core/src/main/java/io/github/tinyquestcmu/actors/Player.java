package io.github.tinyquestcmu.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class Player extends Entity {
    private float speed = 120f;
    private String name = "Hero";

    // ✨ สร้าง Array สำหรับเก็บ Texture ของแต่ละท่าทาง ✨
    private Texture[] frontFrames;
    private Texture[] backFrames;
    private Texture[] leftFrames;
    private Texture[] rightFrames;

    private Texture currentTexture; // Texture ปัจจุบันที่จะวาด
    private float animTime = 0f;
    private boolean isMoving = false;

    public Player(float x, float y) {
        super(x, y, 32, 48); // ปรับขนาดให้พอดีกับตัวละคร

        // ✨ โหลด Texture ทุกไฟล์เข้ามาเก็บใน Array ✨
        try {
            frontFrames = new Texture[] {
                new Texture("assets/Char/Man_Stand.png"),
                new Texture("assets/Char/Man_StandLeft.png"),
                new Texture("assets/Char/Man_StandRight.png")
            };
            backFrames = new Texture[] {
                new Texture("assets/Char/Man_Back.png"),
                new Texture("assets/Char/Man_BackLeft.png"),
                new Texture("assets/Char/Man_BackRight.png")
            };
            leftFrames = new Texture[] {
                new Texture("assets/Char/Man_StandLeft.png"), // ท่ายืน
                new Texture("assets/Char/Man_Left1.png"),
                new Texture("assets/Char/Man_Left2.png")
            };
            rightFrames = new Texture[] {
                new Texture("assets/Char/Man_StandRight.png"), // ท่ายืน
                new Texture("assets/Char/Man_Right1.png"),
                new Texture("assets/Char/Man_Right2.png")
            };

            // ตั้งค่า Texture เริ่มต้น
            currentTexture = frontFrames[0];

        } catch (GdxRuntimeException e) {
            System.out.println("ERROR: Could not load player textures from assets/Char/: " + e.getMessage());
        }
    }

    public void update(float dt) {
        isMoving = false;
        float dx = 0, dy = 0;
        Texture[] currentDirectionFrames = null;

        // --- Logic การเคลื่อนไหว ---
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            dx = -speed * dt;
            isMoving = true;
            currentDirectionFrames = leftFrames;
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            dx = speed * dt;
            isMoving = true;
            currentDirectionFrames = rightFrames;
        } else if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            dy = speed * dt;
            isMoving = true;
            currentDirectionFrames = backFrames;
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            dy = -speed * dt;
            isMoving = true;
            currentDirectionFrames = frontFrames;
        }

        if (isMoving) {
            animTime += dt;
            x += dx;
            y += dy;

            // --- Logic การเลือก Animation Frame ---
            int frameIndex = (int)(animTime * 6) % (currentDirectionFrames.length - 1) + 1; // สลับระหว่างเฟรมเดิน
            currentTexture = currentDirectionFrames[frameIndex];

        } else {
            animTime = 0;
            // ถ้าหยุดเดิน ให้กลับไปใช้ท่ายืนของทิศทางล่าสุด
            if (currentTexture == leftFrames[1] || currentTexture == leftFrames[2]) currentTexture = leftFrames[0];
            else if (currentTexture == rightFrames[1] || currentTexture == rightFrames[2]) currentTexture = rightFrames[0];
            else if (currentTexture == backFrames[1] || currentTexture == backFrames[2]) currentTexture = backFrames[0];
            else currentTexture = frontFrames[0]; // Default
        }

        super.update(dt);
    }

    public void drawSprite(SpriteBatch batch) {
        if (currentTexture != null) {
            batch.draw(currentTexture, x, y, 32, 48);
        }
    }

    public void dispose() {
        // ✨ สำคัญ: ต้อง dispose Texture ทุกไฟล์เพื่อคืน Memory ✨
        for (Texture t : frontFrames) t.dispose();
        for (Texture t : backFrames) t.dispose();
        for (Texture t : leftFrames) t.dispose();
        for (Texture t : rightFrames) t.dispose();
    }

    // --- เมธอดอื่นๆ ที่เหลือ ---
    @Override
    public void draw(ShapeRenderer shapes) { if (currentTexture == null) shapes.rect(x, y, w, h); }
    public void drawLabel(SpriteBatch batch, BitmapFont font) { if (font != null) font.draw(batch, name, x - 4, y + 58); }
    public void setName(String name) { this.name = name; }
}
