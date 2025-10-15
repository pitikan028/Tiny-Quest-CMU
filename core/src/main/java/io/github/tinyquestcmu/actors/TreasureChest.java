package io.github.tinyquestcmu.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class TreasureChest extends Entity {
    private Texture closedTexture;
    private Texture openTexture;
    private boolean isOpen = false;

    public TreasureChest(float x, float y) {
        super(x, y, 32, 32); // ขนาดของกล่องสมบัติ

        try {
            closedTexture = new Texture("assets/sprites/chest_closed.png"); // ★ ต้องมีรูปนี้
            openTexture = new Texture("assets/sprites/chest_open.png");   // ★ และรูปนี้
        } catch (GdxRuntimeException e) {
            System.out.println("ERROR: Missing treasure chest textures!");
        }
    }

    // เมธอดสำหรับเปิดกล่อง
    public void open() {
        if (!isOpen) {
            isOpen = true;
            System.out.println("You Must Kill Enemy Slime! and then you will go right "); // แสดงข้อความใน console
            System.out.println("On the bright to the forrest and you can find some tree for help your brother!");
        }
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void drawSprite(SpriteBatch batch) {
        Texture currentTexture = isOpen ? openTexture : closedTexture;
        if (currentTexture != null) {
            batch.draw(currentTexture, x, y, 32, 32);
        }
    }

    public void dispose() {
        if (closedTexture != null) closedTexture.dispose();
        if (openTexture != null) openTexture.dispose();
    }
}
