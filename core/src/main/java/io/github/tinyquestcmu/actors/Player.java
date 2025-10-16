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
    private String name = "Entaneer ISNE12";

    // --- Animation Textures ---
    // --- เท็กซ์เจอร์สำหรับอนิเมชั่น ---
    private Texture[] frontFrames, backFrames, leftFrames, rightFrames;
    private Texture attackLeftTexture, attackRightTexture;
    private float animTime = 0f;
    private boolean isMoving = false;
    private int facing = 0;

    // --- Attack State ---
    // --- สถานะการโจมตี ---
    private boolean isAttacking = false;
    private float attackAnimDuration = 0.3f;
    private float attackAnimTimer = 0f;
    private float attackCooldown = 0.5f;
    private float attackTimer = 0f;

    // 1. เพิ่มค่าคงที่สำหรับขนาดตัวละคร
    // 1. Add constants for character size
    private static final float NORMAL_WIDTH = 32;
    private static final float NORMAL_HEIGHT = 48;
    private static final float ATTACK_WIDTH = 48;
    private static final float ATTACK_HEIGHT = 72;

    public Player(float x, float y) {
        super(x, y, NORMAL_WIDTH, NORMAL_HEIGHT);

        try {
            // โหลด Texture ท่าเดิน
            // Load walking textures
            frontFrames = new Texture[] { new Texture("assets/Char/Man_Stand.png"), new Texture("assets/Char/Man_StandLeft.png"), new Texture("assets/Char/Man_StandRight.png") };
            backFrames = new Texture[] { new Texture("assets/Char/Man_Back.png"), new Texture("assets/Char/Man_BackLeft.png"), new Texture("assets/Char/Man_BackRight.png") };
            leftFrames = new Texture[] { new Texture("assets/Char/Man_StandLeft.png"), new Texture("assets/Char/Man_Left1.png"), new Texture("assets/Char/Man_Left2.png") };
            rightFrames = new Texture[] { new Texture("assets/Char/Man_StandRight.png"), new Texture("assets/Char/Man_Right1.png"), new Texture("assets/Char/Man_Right2.png") };

            // โหลด Texture ท่าโจมตี
            // Load attack textures
            attackLeftTexture = new Texture("assets/Char/Man_Attack_Left.png");
            attackRightTexture = new Texture("assets/Char/Man_Attack_Right.png");

        } catch (GdxRuntimeException e) {
            System.out.println("ERROR: Could not load player textures: " + e.getMessage());
        }
    }

    public void update(float dt) {
        if (isAttacking) {
            attackAnimTimer -= dt;
            if (attackAnimTimer <= 0) {
                isAttacking = false;
            }
            return;
        }

        // --- Logic การเคลื่อนไหว ---
        // --- Movement Logic ---
        isMoving = false;
        float dx = 0, dy = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) { dx = -speed * dt; facing = 1; isMoving = true; }
        else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) { dx = speed * dt; facing = 2; isMoving = true; }
        else if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) { dy = speed * dt; facing = 3; isMoving = true; }
        else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) { dy = -speed * dt; facing = 0; isMoving = true; }

        if (isMoving) { animTime += dt; x += dx; y += dy; }
        else { animTime = 0; }

        if (attackTimer > 0) {
            attackTimer -= dt;
        }
        super.update(dt);
    }

    public void attack(Enemy target) {
        if (attackTimer <= 0 && target != null && target.isAlive()) {
            if (this.getBounds().overlaps(target.getBounds())) {
                System.out.println("Player attacks!");
                target.takeDamage(10);
                attackTimer = attackCooldown;
                isAttacking = true;
                attackAnimTimer = attackAnimDuration;
            }
        }
    }

    @Override
    public void drawSprite(SpriteBatch batch) {
        Texture currentTexture = null;
        float drawX = x;
        float drawY = y;
        float drawWidth = NORMAL_WIDTH;
        float drawHeight = NORMAL_HEIGHT;

        if (isAttacking) {
            // ถ้ากำลังโจมตี, ให้ใช้ท่าโจมตีและขนาดใหญ่ขึ้น
            // If attacking, use the attack pose and a larger size
            currentTexture = (facing == 1) ? attackLeftTexture : attackRightTexture;
            drawWidth = ATTACK_WIDTH;
            drawHeight = ATTACK_HEIGHT;

            // ปรับตำแหน่งการวาดเพื่อให้ขยายออกจากจุดศูนย์กลาง
            // Adjust the drawing position to expand from the center
            drawX = x - (ATTACK_WIDTH - NORMAL_WIDTH) / 2;
            drawY = y - (ATTACK_HEIGHT - NORMAL_HEIGHT) / 2;

        } else if (!isMoving) {
            // ถ้าไม่เคลื่อนไหว, ให้ใช้ท่ายืน
            // If not moving, use the standing pose
            if (facing == 0) currentTexture = frontFrames[0];
            else if (facing == 1) currentTexture = leftFrames[0];
            else if (facing == 2) currentTexture = rightFrames[0];
            else if (facing == 3) currentTexture = backFrames[0];
        } else {
            // ถ้าเคลื่อนไหว, ให้ใช้ท่าเดิน
            // If moving, use the walking animation
            Texture[] walkFrames = (facing == 1) ? leftFrames : (facing == 2) ? rightFrames : (facing == 3) ? backFrames : frontFrames;
            int frameIndex = (int)(animTime * 6) % (walkFrames.length - 1) + 1;
            currentTexture = walkFrames[frameIndex];
        }

        if (currentTexture != null) {
            batch.draw(currentTexture, drawX, drawY, drawWidth, drawHeight);
        }
    }

    @Override
    public void dispose() {
        if(frontFrames == null) return;
        for (Texture t : frontFrames) t.dispose();
        for (Texture t : backFrames) t.dispose();
        for (Texture t : leftFrames) t.dispose();
        for (Texture t : rightFrames) t.dispose();
        if (attackLeftTexture != null) attackLeftTexture.dispose();
        if (attackRightTexture != null) attackRightTexture.dispose();
    }

    // --- เมธอดอื่นๆ ---
    // --- Other methods ---
    @Override
    public void draw(ShapeRenderer shapes) { if (frontFrames == null) shapes.rect(x, y, w, h); }
    public void drawLabel(SpriteBatch batch, BitmapFont font) { if (font != null) font.draw(batch, name, x - 4, y + 58); }
    public void setName(String name) { this.name = name; }
}
