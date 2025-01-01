package pepse.world;

import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.NoiseGenerator;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class Terrain {
    private static final Color BASE_GROUND_COLOR = new Color(212, 123,74);
    public static final String GROUND_tag = "ground";
    private final Vector2 windowDimensions;
    private final int seed;
    public final float groundHeightAtX0; //todo should be static?
    private static final int TERRAIN_DEPTH = 20;

    public Terrain(Vector2 windowDimensions, int seed){
        groundHeightAtX0 =  ((float)2/3) * windowDimensions.y();
        this.windowDimensions = windowDimensions;
        this.seed = seed;
    }

    public float groundHeightAt(float x) {
//        new NoiseGenerator(seed, (int)groundHeightAtX0);
        return groundHeightAtX0;
    }

    public List<Block> createInRange(int minX, int maxX) {
        int leftBlockLocation = (int) Math.floor((double) minX /Block.SIZE) * Block.SIZE;
        int rightBlockLocation = (int) Math.floor((double) maxX /Block.SIZE) * Block.SIZE;
//        int numberOfBricksInRow = (rightBlockLocation - leftBlockLocation)/Block.SIZE;
        List<Block> blocks = new ArrayList<Block>();

        for(int colX = leftBlockLocation; colX != rightBlockLocation; colX += Block.SIZE) {
            int y = (int) Math.floor((double) groundHeightAtX0 / Block.SIZE) * Block.SIZE;
            for (int rowBlocks = 0; rowBlocks < TERRAIN_DEPTH; rowBlocks++){
                addBlockToBlock(colX, y + rowBlocks * Block.SIZE, blocks);
            }
        }
        return blocks;
    }

//    public List<Block> createInRange(int minX, int maxX) {
//        int leftBlockLocation = (int) Math.floor((double) minX /Block.SIZE) * Block.SIZE;
//        int rightBlockLocation = (int) Math.floor((double) maxX /Block.SIZE) * Block.SIZE;
////        int numberOfBricksInRow = (rightBlockLocation - leftBlockLocation)/Block.SIZE;
//        List<Block> blocks = new ArrayList<Block>();
//
//        for(int colX = leftBlockLocation; colX != rightBlockLocation; colX += Block.SIZE) {
//            int y = (int) Math.floor((double) groundHeightAtX0 / Block.SIZE) * Block.SIZE;
//            for (; y < windowDimensions.y(); y += Block.SIZE) {
//                addBlockToBlock(colX, y, blocks);
//            }
//        }
//        return blocks;
//    }

    private static void addBlockToBlock(int colX, int y, List<Block> blocks) {
        RectangleRenderable groundRender =
                new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR));
        Vector2 newBlockLocation = new Vector2(colX, y);
        Block block = new Block(newBlockLocation, groundRender);
        block.setTag(GROUND_tag);
        blocks.add(block);
    }

    public float getGroundHeightAtX0(){
        return groundHeightAtX0;
    }
}
