package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.util.Vector2;
import pepse.world.*;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Flora;
import pepse.world.trees.Leaf;
import pepse.world.trees.Tree;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class PepseGameManager extends GameManager {
    private Vector2 windowDimensions;
    private static final int seed = 42;
    private static final int CYCLE_LENGTH = 30;
    private Terrain terrain;
    private static final Vector2 ENERGY_POSITION =  new Vector2(10, 10);

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        this.windowDimensions = windowController.getWindowDimensions();

        createSky();

        // Create Terrain:
        createTerrain();

        // create night:
        GameObject night = Night.create(windowDimensions, CYCLE_LENGTH);
        gameObjects().addGameObject(night, Layer.FOREGROUND); //todo valid FOREGROUND

        // create sun:
        GameObject sun = Sun.create(windowDimensions, CYCLE_LENGTH);
        gameObjects().addGameObject(sun, Layer.BACKGROUND); //todo valid BACKGROUND

        // create Halo
        gameObjects().addGameObject(SunHalo.create(sun), Layer.BACKGROUND);

        createAvatar(inputListener, imageReader);

        // create flora
        createFlora(terrain);
        gameObjects().layers().shouldLayersCollide(Layer.DEFAULT, Layer.STATIC_OBJECTS, true);

    }

    private void createFlora(Terrain terrain) {
        Flora flora = new Flora(terrain);
        List<Tree> trees = flora.createInRange(0, (int) windowDimensions.x(), terrain);
        // infinite world minX shouldnt be 0?
        for (Tree tree: trees) {
            gameObjects().addGameObject(tree, Layer.STATIC_OBJECTS);
            List<Leaf> leaves = flora.createLeaves(tree);
            for (Leaf leaf: leaves) {
                gameObjects().addGameObject(leaf, Layer.BACKGROUND);
            }
        }
    }

    private void createTerrain() {
        terrain = new Terrain(windowDimensions, seed);
        List<Block> blocks = terrain.createInRange(0 , (int) windowDimensions.x());
        for (Block block: blocks){
            gameObjects().addGameObject(block, Layer.STATIC_OBJECTS);
        }
    }

    private void createAvatar(UserInputListener inputListener, ImageReader imageReader) {
        float avatarFloorY = terrain.getGroundHeightAtX0();
        Vector2 avatarFloorPos = new Vector2(0, avatarFloorY);
        Avatar avatar = new Avatar(avatarFloorPos,  inputListener, imageReader);
        gameObjects().addGameObject(avatar);

        EnergyRenderer energyDisplay = new EnergyRenderer(ENERGY_POSITION,
                avatar::getEnergyLevel);
        gameObjects().addGameObject(energyDisplay, Layer.UI);
    }

    private void createSky() {
        // Create sky:
        GameObject sky = Sky.create(windowDimensions);
        gameObjects().addGameObject(sky, Layer.BACKGROUND);
    }


    public static void main(String[] args) {
        new PepseGameManager().run();
    }
}