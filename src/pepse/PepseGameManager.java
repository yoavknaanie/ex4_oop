package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.ScheduledTask;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Vector2;
import pepse.world.*;
import pepse.world.cloud.Cloud;
import pepse.world.cloud.CloudPiece;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Flora;
import pepse.world.trees.Fruit;
import pepse.world.trees.Leaf;
import pepse.world.trees.Tree;
import java.util.List;

/**
 * The PepseGameManager class is responsible for managing the game world, initializing objects,
 * and handling updates. It sets up the terrain, avatar, flora, clouds, and other dynamic elements.
 */
public class PepseGameManager extends GameManager {
    private static final int[] RECYCLED_LAYERS = new int[]{Layer.STATIC_OBJECTS, Layer.DEFAULT};
    private static final int CLOUD_LAYER = Layer.BACKGROUND + 1;
    public static final int LEFT_SCREEN_CORNER_START = 0;
    public static final int ZERO = 0;
    public static final int DROP_WAIT_TIME = 3;
    public static final int CAMERA_HEIGHT_FACTOR = 125;
    private static final float HALF = 1/2f;
    private static final int LEFT_BORDER_IDX = 0;
    private static final int RIGHT_BORDER_IDX = 1;
    private static final int UPDATE_BORDER_FACTOR = 4;
    private static final String LEAF_TAG = "leaf";

    private Vector2 windowDimensions;
    private static final int seed = 42;
    public static final int CYCLE_LENGTH = 30;
    private Terrain terrain;
    private Flora flora;
    private static final Vector2 ENERGY_POSITION =  new Vector2(10, 10);
    private Avatar avatar;
    private WindowController windowController;
    private float[] borders;


    /**
     * Initializes the game by setting up the game world, terrain, flora, avatar, and other objects.
     *
     * @param imageReader The image reader for loading images.
     * @param soundReader The sound reader for loading sounds.
     * @param inputListener The input listener for managing user input.
     * @param windowController The window controller for managing the window.
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        this.windowDimensions = windowController.getWindowDimensions();
        this.windowController = windowController;
        createSky();

        // Create Terrain:
        createTerrain();

        // create night:
        GameObject night = Night.create(windowDimensions, CYCLE_LENGTH);
        gameObjects().addGameObject(night, Layer.FOREGROUND);

        // create sun:
        GameObject sun = Sun.create(windowDimensions, CYCLE_LENGTH);
        gameObjects().addGameObject(sun, Layer.BACKGROUND);

        // create Halo
        gameObjects().addGameObject(SunHalo.create(sun), Layer.BACKGROUND);

        //create avatar
        createAvatar(inputListener, imageReader, windowController);

        // create flora
        createFlora(terrain, LEFT_SCREEN_CORNER_START - UPDATE_BORDER_FACTOR * Block.SIZE,
                (int) windowDimensions.x() + UPDATE_BORDER_FACTOR * Block.SIZE);
        gameObjects().layers().shouldLayersCollide(Layer.DEFAULT, Layer.STATIC_OBJECTS, true);

        // create cloud
        createCloud();

        this.borders = getNewBoarders();
//                new float[]{LEFT_SCREEN_CORNER_START, windowDimensions.x()};
    }


    private void createCloud() {
        Cloud cloud = new Cloud(windowDimensions, this::createDrop, avatar::isAvatarJumping);
        List<CloudPiece> blocks = cloud.getBlocks();
        for (CloudPiece block: blocks) {
            gameObjects().addGameObject(block,CLOUD_LAYER);
        }
    }


    private void createFlora(Terrain terrain, int left, int right) {
        this.flora = new Flora(terrain::groundHeightAt);
        createFloraInRange(left, right);
    }

    private void createFloraInRange(int left, int right) {
        System.out.println();
        List<Tree> trees = flora.createInRange(left, right);
        for (Tree tree: trees) {
            gameObjects().addGameObject(tree, Layer.STATIC_OBJECTS);
            //create leaves:
            List<Leaf> leaves = flora.createLeaves(tree);
            for (Leaf leaf: leaves) {
                gameObjects().addGameObject(leaf, Layer.BACKGROUND);
//                gameObjects().addGameObject(leaf, Layer.STATIC_OBJECTS);
            }
            //create fruits:
            List<Fruit> fruits = flora.createFruits(tree);
            for (Fruit fruit: fruits) {
                gameObjects().addGameObject(fruit, Layer.STATIC_OBJECTS);
            }
        }
    }

    private void createTerrain() {
        terrain = new Terrain(windowDimensions, seed);
        List<Block> blocks =
                terrain.createInRange(LEFT_SCREEN_CORNER_START - UPDATE_BORDER_FACTOR * Block.SIZE,
                (int) windowDimensions.x() + UPDATE_BORDER_FACTOR * Block.SIZE);
        for (Block block: blocks){
            gameObjects().addGameObject(block, Layer.STATIC_OBJECTS);
        }
    }

    private void createTerrainInRange(int minX, int maxX) {
        List<Block> blocks = terrain.createInRange(minX , maxX);
        for (Block block: blocks){
            gameObjects().addGameObject(block, Layer.STATIC_OBJECTS);
        }
    }

    private void createAvatar(UserInputListener inputListener, ImageReader imageReader,
                           WindowController windowController) {
        float x = windowDimensions.x() * HALF;
//        float avatarFloorY = terrain.getGroundHeightAtX0();
        float avatarFloorY = terrain.groundHeightAt(x);
        Vector2 avatarFloorPos = new Vector2(x, avatarFloorY);
        avatar = new Avatar(avatarFloorPos,  inputListener, imageReader);
        gameObjects().addGameObject(avatar);

        EnergyRenderer energyDisplay = new EnergyRenderer(ENERGY_POSITION,
                avatar::getEnergyLevel);
        gameObjects().addGameObject(energyDisplay, Layer.UI);

        setCamera(new Camera(avatar,
                Vector2.UP.mult(CAMERA_HEIGHT_FACTOR),
                windowController.getWindowDimensions(),
                windowController.getWindowDimensions()));
    }

    private void createSky() {
        // Create sky:
        GameObject sky = Sky.create(windowDimensions);
        gameObjects().addGameObject(sky, Layer.BACKGROUND);
    }
    /**
     * Creates and schedules the removal of a cloud drop after a delay.
     *
     * @param gameObject The cloud drop to be removed after the specified delay.
     */
    public void createDrop(GameObject gameObject) {
        gameObjects().addGameObject(gameObject, Layer.BACKGROUND);
        new ScheduledTask(
                gameObject,
                DROP_WAIT_TIME,
                false,
                () -> gameObjects().removeGameObject(gameObject)
        );
    }

    private float[] getNewBoarders() {
        return new float[]{
                // todo maybe add some extra margins:
                (this.avatar.getCenter().x() - windowDimensions.x() * HALF),
                (this.avatar.getCenter().x() + windowDimensions.x() * HALF),
        };
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float[] newBorders = getNewBoarders();
        int oldLeft = (int) borders[LEFT_BORDER_IDX] - Block.SIZE * UPDATE_BORDER_FACTOR;
        int oldRight = (int) borders[RIGHT_BORDER_IDX] + Block.SIZE * UPDATE_BORDER_FACTOR;
        int newLeft = (int) newBorders[LEFT_BORDER_IDX] - Block.SIZE * UPDATE_BORDER_FACTOR;
        int newRight = (int) newBorders[RIGHT_BORDER_IDX] + Block.SIZE * UPDATE_BORDER_FACTOR;

        if (newRight > oldRight + Block.SIZE * UPDATE_BORDER_FACTOR) { // walk right
//            createTerrainInRange(oldRight, newRight + Block.SIZE * UPDATE_BORDER_FACTOR);
            createTerrainInRange(oldRight, newRight);
//            createFlora(terrain, oldRight, newRight + Block.SIZE * UPDATE_BORDER_FACTOR);
            createFlora(terrain, oldRight, newRight);
            this.borders = newBorders;
            removeOffScreen(newLeft, newRight);
        }
        else if (newLeft < oldLeft - Block.SIZE * UPDATE_BORDER_FACTOR) { // walk left
//            createTerrainInRange(newLeft - Block.SIZE * UPDATE_BORDER_FACTOR, oldLeft);
            createTerrainInRange(newLeft, oldLeft);
//            createFlora(terrain, newLeft - Block.SIZE * UPDATE_BORDER_FACTOR, oldLeft);
            createFlora(terrain, newLeft, oldLeft);
            this.borders = newBorders;
            removeOffScreen(newLeft, newRight);
        }
//        removeOffScreen(newLeft, newRight);
    }

    private void removeOffScreen(float left, float right) {
        for (GameObject object : this.gameObjects()) {
//            if (object.getCenter().x() > right + (UPDATE_BORDER_FACTOR * Block.SIZE) ||
//                    object.getCenter().x() < left - (UPDATE_BORDER_FACTOR * Block.SIZE)) {
            if (object.getCenter().x() > right ||
                    object.getCenter().x() < left) {
                for (int layer : RECYCLED_LAYERS) {
                    this.gameObjects().removeGameObject(object, layer);
                }
                if (object.getTag().equals(LEAF_TAG)){
                    this.gameObjects().removeGameObject(object, Layer.BACKGROUND);
                }
            }
        }
    }

    /**
     * Main method to run the game.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        new PepseGameManager().run();
    }
}