package io.github.tinyquestcmu.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout; // ★ 1. Import GlyphLayout ★
import io.github.tinyquestcmu.TinyQuestCMUGame;
import io.github.tinyquestcmu.quest.*;

public class EndingScreen extends BaseScreen {
    private float timer = 0f;
    private GlyphLayout layout = new GlyphLayout(); // ★ 2. สร้าง instance ของ GlyphLayout ★

    public EndingScreen(TinyQuestCMUGame game) {
        super(game);
        game.questManager.set(QuestFlag.FINISHED);
    }

    @Override
    public void render(float dt) {
        clear(0,0,0);
        timer += dt;

        // --- วาดกล่องข้อความ ---
        shapes.begin(com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled);
        shapes.setColor(Color.WHITE);
        shapes.rect(180, 160, 440, 160);
        shapes.end();

        // --- วาดข้อความ (ใช้ hudBatch เพื่อไม่ให้เลื่อนตามกล้อง) ---
        hudBatch.begin();

        // ★ 3. คำนวณและวาดข้อความให้อยู่กึ่งกลาง ★
        String line1 = "The family is reunited.";
        String line2 = "Thank you for playing TinyQuestCMU.";
        String line3 = "Press ENTER to restart";

        // วาดบรรทัดที่ 1 (กึ่งกลางในกล่องขาว)
        layout.setText(game.font, line1);
        float textX1 = 180 + (440 - layout.width) / 2; // (boxX + (boxWidth - textWidth) / 2)
        game.font.setColor(Color.BLACK);
        game.font.draw(hudBatch, layout, textX1, 280);

        // วาดบรรทัดที่ 2 (กึ่งกลางในกล่องขาว)
        layout.setText(game.font, line2);
        float textX2 = 180 + (440 - layout.width) / 2;
        game.font.draw(hudBatch, layout, textX2, 240);

        // วาดบรรทัดที่ 3 (กึ่งกลางหน้าจอ)
        if (timer > 2f) {
            layout.setText(game.font, line3);
            float textX3 = (WORLD_WIDTH - layout.width) / 2; // (screenWidth - textWidth) / 2
            game.font.setColor(Color.WHITE);
            game.font.draw(hudBatch, layout, textX3, 100);
        }

        hudBatch.end();

        if (timer > 2f && (Gdx.input.isKeyJustPressed(Input.Keys.ENTER))) {
            game.setScreen(new IntroScreen(game));
        }
    }
}
