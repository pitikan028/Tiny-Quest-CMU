package io.github.tinyquestcmu.actors;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public interface Interactable {
    Rectangle getBounds();
    void draw(ShapeRenderer shapes);
    void drawSprite(SpriteBatch batch);
    void onInteract();
    boolean isActive();
}
