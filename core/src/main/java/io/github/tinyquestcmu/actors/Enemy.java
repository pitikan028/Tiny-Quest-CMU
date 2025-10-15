package io.github.tinyquestcmu.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Enemy extends Entity {
    private Texture texture;
    private int maxHp = 50;
    public int currentHp;

    public Enemy(float x, float y) {
        super(x, y, 50, 66);
        currentHp = maxHp;
        texture = new Texture("assets/sprites/enemy_sprite.png");
    }

    public boolean isAlive() { return currentHp > 0; }

    public void takeDamage(int damage) {
        currentHp -= damage;
        if (currentHp < 0) currentHp = 0;
    }

    public void drawSprite(SpriteBatch batch) {
        if (isAlive()) {
            batch.draw(texture, x, y, 50, 66);
        }
    }

    public void drawHealthBar(ShapeRenderer shapes) {
        if (isAlive()) {
            shapes.setColor(Color.RED);
            shapes.rect(x, y + h + 5, w, 5);
            shapes.setColor(Color.GREEN);
            shapes.rect(x, y + h + 5, w * ((float)currentHp / maxHp), 5);
        }
    }

    public void dispose() {
        texture.dispose();
    }
}
