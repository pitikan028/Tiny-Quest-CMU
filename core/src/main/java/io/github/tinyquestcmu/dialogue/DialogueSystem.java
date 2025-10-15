package io.github.tinyquestcmu.dialogue;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class DialogueSystem {
    private Dialogue dialogue;
    private boolean active = false;

    public void start(Dialogue d){ this.dialogue = d; this.dialogue.reset(); active = true; }
    public boolean isActive(){ return active; }

    public void update(){
        if(!active) return;
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isTouched()){
            if(!dialogue.advance()) active = false;
        }
    }

    
    public void draw(ShapeRenderer shapes, SpriteBatch batch, BitmapFont font, float screenW){
        if(!active) return;
        // draw dialogue panel
        shapes.begin(com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled);
        shapes.rect(16, 16, screenW-32, 64);
        shapes.end();

        // draw text
        batch.begin();
        font.draw(batch, dialogue.getCurrentText(), 24, 64);
        batch.end();
    }

}
