package io.github.tinyquestcmu.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

/**
 * NPC ตัวละครทั่วไปที่สืบทอดจาก Entity
 */
public class NPC extends Entity {
    private String name;
    private String role;
    private Texture sprite;

    public NPC(String name, String role, float x, float y) {
        super(x, y, 16, 16);
        this.name = name;
        this.role = role;

        // ✅ โหลด texture ตามชื่อ
        if (name.equalsIgnoreCase("Rin")) {
            this.sprite = new Texture("assets/sprites/rin_sheet.png");
        } else if (name.equalsIgnoreCase("Pavo")) {
            this.sprite = new Texture("assets/sprites/pavo_sheet.png");
        } else if (name.equalsIgnoreCase("Ansia")) {
            this.sprite = new Texture("assets/sprites/ansia_sheet.png");
        }
    }

    // setter (ถ้าอยากเปลี่ยน texture ภายหลัง)
    public void setTexture(Texture t) { this.sprite = t; }

    public String getName() { return name; }
    public String getRole() { return role; }

    // ✅ ตรวจว่า player เข้าใกล้ NPC หรือไม่
    public boolean isPlayerNear(Rectangle playerBounds) {
        return playerBounds.overlaps(this.bounds);
    }

    // ✅ วาด placeholder ถ้าไม่มี texture
    public void draw(ShapeRenderer shapes) {
        if (sprite == null) shapes.rect(x, y, w, h);
    }

    // ✅ วาด texture ถ้ามี
    public void drawSprite(SpriteBatch batch) {
        if (sprite != null) batch.draw(sprite, (int)x, (int)y, 32, 32);
    }

    // ✅ วาดชื่อ NPC เหนือหัว
    public void drawLabel(SpriteBatch batch, com.badlogic.gdx.graphics.g2d.BitmapFont font) {
        font.draw(batch, name + " (" + role + ")", x - 6, y + 24);
    }
}
