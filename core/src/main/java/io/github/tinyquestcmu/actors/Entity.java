package io.github.tinyquestcmu.actors;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

/**
 * คลาสพื้นฐานสำหรับทุกตัวละคร (Player, NPC ฯลฯ)
 * มี hitbox (bounds) สำหรับตรวจชน และพิกัดขนาด
 */
public class Entity {
    protected float x, y;     // พิกัด
    protected float w, h;     // ขนาด
    protected Rectangle bounds; // ✅ เพิ่ม hitbox rectangle

    public Entity(float x, float y, float w, float h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.bounds = new Rectangle(x, y, w, h);
    }

    public void update(float dt) {
        // subclass จะ override เอง
        // อัปเดตตำแหน่ง rectangle ตามพิกัดล่าสุด
        bounds.setPosition(x, y);
    }

    public void draw(ShapeRenderer shapes) {
        // subclass override ได้
    }

    // getter สำหรับตำแหน่ง
    public float getX() { return x; }
    public float getY() { return y; }
    public float getW() { return w; }
    public float getH() { return h; }

    // setter สำหรับตำแหน่ง
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        bounds.setPosition(x, y);
    }

    public Rectangle getBounds() { return bounds; }
}
