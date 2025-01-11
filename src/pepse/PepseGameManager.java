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

public class PepseGameManager extends GameManager {
    private static final int[] RECYCLED_LAYERS = new int[]{Layer.STATIC_OBJECTS, Layer.DEFAULT};
    private static final int CLOUD_LAYER = Layer.BACKGROUND + 1;
    private static final int LEAVES_LAYER = Layer.STATIC_OBJECTS + 1;


    private Vector2 windowDimensions;
    private static final int seed = 42;
    public static final int CYCLE_LENGTH = 4;
    private Terrain terrain;
    private Flora flora;
    private static final Vector2 ENERGY_POSITION =  new Vector2(10, 10);
    private Avatar avatar;
    private WindowController windowController;
    private float[] borders;

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
        gameObjects().addGameObject(night, Layer.FOREGROUND); //todo valid FOREGROUND

        // create sun:
        GameObject sun = Sun.create(windowDimensions, CYCLE_LENGTH);
        gameObjects().addGameObject(sun, Layer.BACKGROUND); //todo valid BACKGROUND

        // create Halo
        gameObjects().addGameObject(SunHalo.create(sun), Layer.BACKGROUND);

        //create avatar
        createAvatar(inputListener, imageReader, windowController);

        // create flora
        createFlora(terrain);
        gameObjects().layers().shouldLayersCollide(Layer.DEFAULT, Layer.STATIC_OBJECTS, true);

        // create cloud
        createCloud();

        this.borders = new float[]{0, windowDimensions.x()};
    }


    private void createCloud() {
        Cloud cloud = new Cloud(windowDimensions, this::createDrop, avatar::isAvatarJumping);
        List<CloudPiece> blocks = cloud.getBlocks();
        for (CloudPiece block: blocks) {
            gameObjects().addGameObject(block,CLOUD_LAYER);
        }
    }


    private void createFlora(Terrain terrain) {
        this.flora = new Flora(terrain::groundHeightAt);
        // todo when implamant infinite world, minX shouldnt be 0:
        List<Tree> trees = flora.createInRange(0, (int) windowDimensions.x());
        for (Tree tree: trees) {
            gameObjects().addGameObject(tree, Layer.STATIC_OBJECTS);
            List<Leaf> leaves = flora.createLeaves(tree);
            for (Leaf leaf: leaves) {
                gameObjects().addGameObject(leaf, Layer.BACKGROUND);
            }
            List<Fruit> fruits = flora.createFruits(tree);
            for (Fruit fruit: fruits) {
                gameObjects().addGameObject(fruit, Layer.STATIC_OBJECTS);
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

    private void createTerrainInRange(int minX, int maxX) {
        List<Block> blocks = terrain.createInRange(minX , maxX);
        for (Block block: blocks){
            gameObjects().addGameObject(block, Layer.STATIC_OBJECTS);
        }
    }

    private void createAvatar(UserInputListener inputListener, ImageReader imageReader,
                           WindowController windowController) {
        float avatarFloorY = terrain.getGroundHeightAtX0();
        Vector2 avatarFloorPos = new Vector2(0, avatarFloorY);
        avatar = new Avatar(avatarFloorPos,  inputListener, imageReader);
        gameObjects().addGameObject(avatar);

        EnergyRenderer energyDisplay = new EnergyRenderer(ENERGY_POSITION,
                avatar::getEnergyLevel);
        gameObjects().addGameObject(energyDisplay, Layer.UI);

        setCamera(new Camera(avatar,
                Vector2.UP.mult(125),
                windowController.getWindowDimensions(),
                windowController.getWindowDimensions()));
    }

    private void createSky() {
        // Create sky:
        GameObject sky = Sky.create(windowDimensions);
        gameObjects().addGameObject(sky, Layer.BACKGROUND);
    }

    public void createDrop(GameObject gameObject) {
        gameObjects().addGameObject(gameObject, Layer.BACKGROUND);
        new ScheduledTask(
                gameObject,
                3,
                false,
                () -> gameObjects().removeGameObject(gameObject)
        );
    }

    private float[] getNewBoarders() {
        return new float[]{
                // todo maybe add some extra margins:
                (this.avatar.getCenter().x() - windowDimensions.x()/2),
                (this.avatar.getCenter().x() + windowDimensions.x()/2 ),
        };
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float[] newBorders = getNewBoarders();
        int oldLeft = (int) borders[0];
        int oldRight = (int) borders[1];
        int newLeft = (int) newBorders[0];
        int newRight = (int) newBorders[1];

        if (newRight > oldRight) { // walk right
//            System.out.println("oldLeft = " + oldLeft);
//            System.out.println("oldRight = " + oldRight);
//            System.out.println("newLeft = " + newLeft);
//            System.out.println("newRight = " + newRight);
            createTerrainInRange(oldRight, newRight + Block.SIZE * 10);
            this.borders = newBorders;
//                System.out.println("created right terrein");
//            removeOffScreen(oldLeft - Block.SIZE, newLeft);
        }
        if (newLeft < oldLeft) { // walk left
//            System.out.println("oldLeft = " + oldLeft);
//            System.out.println("oldRight = " + oldRight);
//            System.out.println("newLeft = " + newLeft);
//            System.out.println("newRight = " + newRight);
            createTerrainInRange(newLeft - Block.SIZE * 10, oldLeft);
            this.borders = newBorders;
//                System.out.println("created left terrein");
//            removeOffScreen(newRight + Block.SIZE, oldRight);
        }
    }

    private void removeOffScreen(float left, float right) {

        for (GameObject object : this.gameObjects()) {
            if (object.getCenter().x() > right || object.getCenter().x() < left ) {
                for (int layer : RECYCLED_LAYERS) {
                    this.gameObjects().removeGameObject(object, layer);
                }
                if (object.getTag().equals("leaf")){
                    this.gameObjects().removeGameObject(object, Layer.BACKGROUND);
                }
            }
        }
    }

    public static void main(String[] args) {
        new PepseGameManager().run();
    }
}