package io.github.tinyquestcmu.assets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TileMap {

    public int[][] tiles;
    public Texture grass, dirt, water, bridge, house, tree, specialTree;

    public TileMap(
            int[][] tiles,
            Texture grass,
            Texture dirt,
            Texture water,
            Texture bridge,
            Texture house,
            Texture tree,
            Texture specialTree
    ) {
        this.tiles = tiles;
        this.grass = grass;
        this.dirt = dirt;
        this.water = water;
        this.bridge = bridge;
        this.house = house;
        this.tree = tree;
        this.specialTree = specialTree;
    }

    public void draw(SpriteBatch batch) {
        if (tiles == null) return;

        for (int y = 0; y < tiles.length; y++) {
            for (int x = 0; x < tiles[y].length; x++) {
                int t = tiles[y][x];
                Texture tex = null;

                switch (t) {
                    case 1: tex = grass; break;
                    case 2: tex = dirt; break;
                    case 3: tex = water; break;
                    case 4: tex = bridge; break;
                    case 5: tex = house; break;
                    case 6: tex = tree; break;
                    case 7: tex = specialTree; break;
                    default: tex = grass; break;
                }

                if (tex != null) {
                    batch.draw(tex, x * 32, y * 32, 32, 32);
                }
            }
        }
    }
}
