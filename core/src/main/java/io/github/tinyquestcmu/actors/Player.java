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
    private String name = "Isne12";

    private Texture[] frontFrames, backFrames, leftFrames, rightFrames;
    private float animTime = 0f;
    private boolean isMoving = false;
    private int facing = 0; // 0=down, 1=left, 2=right, 3=up

    private float attackCooldown = 0.5f;
    private float attackTimer = 0f;

    public Player(float x, float y) {
        super(x, y, 32, 48);

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
                new Texture("assets/Char/Man_StandLeft.png"),
                new Texture("assets/Char/Man_Left1.png"),
                new Texture("assets/Char/Man_Left2.png")
            };
            rightFrames = new Texture[] {
                new Texture("assets/Char/Man_StandRight.png"),
                new Texture("assets/Char/Man_Right1.png"),
                new Texture("assets/Char/Man_Right2.png")
            };
        } catch (GdxRuntimeException e) {
            System.out.println("ERROR: Could not load player textures: " + e.getMessage());
        }
    }

    public void update(float dt) {
        isMoving = false;
        float dx = 0, dy = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            dx = -speed * dt;
            facing = 1;
            isMoving = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            dx = speed * dt;
            facing = 2;
            isMoving = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            dy = speed * dt;
            facing = 3;
            isMoving = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            dy = -speed * dt;
            facing = 0;
            isMoving = true;
        }

        if (isMoving) {
            animTime += dt;
            x += dx;
            y += dy;
        } else {
            animTime = 0;
        }

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
            }
        }
    }

    public void drawSprite(SpriteBatch batch) {
        Texture currentTexture = null;

        if (frontFrames == null) return; // Don't try to draw if textures failed to load

        if (!isMoving) {
            if (facing == 0) currentTexture = frontFrames[0];
            else if (facing == 1) currentTexture = leftFrames[0];
            else if (facing == 2) currentTexture = rightFrames[0];
            else if (facing == 3) currentTexture = backFrames[0];
        } else {
            Texture[] walkFrames = frontFrames;
            if (facing == 1) walkFrames = leftFrames;
            else if (facing == 2) walkFrames = rightFrames;
            else if (facing == 3) walkFrames = backFrames;

            int frameCount = walkFrames.length - 1;
            int frameIndex = (int)(animTime * 6) % frameCount + 1;
            currentTexture = walkFrames[frameIndex];
        }

        if (currentTexture != null) {
            batch.draw(currentTexture, x, y, 32, 48);
        }
    }

    @Override
    public void dispose() {
        if(frontFrames == null) return;
        for (Texture t : frontFrames) t.dispose();
        for (Texture t : backFrames) t.dispose();
        for (Texture t : leftFrames) t.dispose();
        for (Texture t : rightFrames) t.dispose();
    }

    @Override
    public void draw(ShapeRenderer shapes) { if (frontFrames == null) shapes.rect(x, y, w, h); }

    // ★ FIX: Removed @Override from this method ★
    public void drawLabel(SpriteBatch batch, BitmapFont font) {
        if (font != null) font.draw(batch, name, x - 4, y + 58);
    }

    public void setName(String name) { this.name = name; }
}
