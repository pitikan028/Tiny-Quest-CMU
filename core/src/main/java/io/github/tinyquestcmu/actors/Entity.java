package io.github.tinyquestcmu.actors;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

/**
 * คลาสพื้นฐานสำหรับทุกตัวละคร (Player, NPC ฯลฯ)
 * มี hitbox (bounds) สำหรับตรวจชน และพิกัดขนาด
 * The base class for all characters (Player, NPC, etc.).
 * Contains a hitbox (bounds) for collision detection, and position/size coordinates.
 */
public abstract class Entity {
    protected float x, y;     // พิกัด
    // Coordinates
    protected float w, h;     // ขนาด
    // Size
    protected Rectangle bounds; // เพิ่ม hitbox rectangle
    // Add a hitbox rectangle

    public Entity(float x, float y, float w, float h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.bounds = new Rectangle(x, y, w, h);
    }

    public void update(float dt) {
        // subclass จะ override เอง
        // Subclasses will override this
        // อัปเดตตำแหน่ง rectangle ตามพิกัดล่าสุด
        // Update the rectangle's position to the latest coordinates
        bounds.setPosition(x, y);
    }

    public abstract void drawSprite(SpriteBatch batch);

    public abstract void dispose();

    public void draw(ShapeRenderer shapes) {
        // subclass override ได้
        // Subclasses can override this
    }

    // getter สำหรับตำแหน่ง
    // Getters for position
    public float getX() { return x; }
    public float getY() { return y; }
    public float getW() { return w; }
    public float getH() { return h; }

    // setter สำหรับตำแหน่ง
    // Setter for position
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        bounds.setPosition(x, y);
    }

    public Rectangle getBounds() { return bounds; }
}
