package io.github.tinyquestcmu.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
        super(x, y, 50, 50); // Adjusted size to match drawing
        this.name = name;
        this.role = role;

        // ✅ โหลด texture ตามชื่อ
        try {
            if (name.equalsIgnoreCase("Rin")) {
                this.sprite = new Texture("assets/sprites/rin_sheet.png");
            } else if (name.equalsIgnoreCase("Pavo")) {
                this.sprite = new Texture("assets/sprites/pavo_sheet.png");
            } else if (name.equalsIgnoreCase("Ansia")) {
                this.sprite = new Texture("assets/ansia_sheet.png");
            }
        } catch (Exception e) {
            System.out.println("Could not load texture for NPC: " + name);
            this.sprite = null;
        }
    }

    // setter (ถ้าอยากเปลี่ยน texture ภายหลัง)
    public void setTexture(Texture t) {
        // Dispose of the old texture before assigning a new one
        if (this.sprite != null) {
            this.sprite.dispose();
        }
        this.sprite = t;
    }

    public String getName() { return name; }
    public String getRole() { return role; }

    public boolean isPlayerNear(Rectangle playerBounds) {
        return playerBounds.overlaps(this.bounds);
    }

    public void draw(ShapeRenderer shapes) {
        if (sprite == null) shapes.rect(x, y, w, h);
    }

    public void drawSprite(SpriteBatch batch) {
        if (sprite != null) batch.draw(sprite, (int)x, (int)y, 50, 50);
    }

    public void drawLabel(SpriteBatch batch, BitmapFont font) {
        font.draw(batch, name + " (" + role + ")", x, y + 55);
    }

    /**
     * <<< This method has been added >>>
     * Releases the texture from memory to prevent memory leaks.
     */
    public void dispose() {
        if (sprite != null) {
            sprite.dispose();
        }
    }
}
