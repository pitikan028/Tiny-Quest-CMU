package io.github.tinyquestcmu.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.Color;

public class Chest implements Interactable {
    private Rectangle bounds;
    private boolean opened = false;
    private Texture spriteClosed, spriteOpen;

    public Chest(float x, float y, Texture closed, Texture open){
        this.bounds = new Rectangle(x, y, 32, 32);
        this.spriteClosed = closed;
        this.spriteOpen = open;
    }

    @Override public Rectangle getBounds(){ return bounds; }
    @Override public boolean isActive(){ return !opened; }
    @Override public void onInteract(){ opened = true; }

    @Override public void draw(ShapeRenderer shapes){
        if(spriteClosed==null && spriteOpen==null){
            shapes.setColor(opened ? Color.GOLD : Color.BROWN);
            shapes.rect(bounds.x, bounds.y, bounds.width, bounds.height);
        }
    }
    @Override public void drawSprite(SpriteBatch batch){
        if(spriteClosed==null && spriteOpen==null) return;
        batch.draw(opened ? spriteOpen : spriteClosed, bounds.x, bounds.y, 32, 32);
    }
}
