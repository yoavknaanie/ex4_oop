package pepse;

import danogl.GameManager;
import danogl.GameObject;
import pepse.world.Sky;

public class PepseGameManager extends GameManager {
    private Vector2 windowDimensions;
    private static final int seed = 42;
    private static final int CYCLE_LENGTH = 30;

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        this.windowDimensions = windowController.getWindowDimensions();

        createSky();

        // Create Terrain:
        Terrain terrain = new Terrain(windowDimensions, seed);
        List<Block> blocks = terrain.createInRange(0 , (int) windowDimensions.x());
        for (Block block: blocks){
            gameObjects().addGameObject(block, Layer.STATIC_OBJECTS);
        }

        // create night:
        GameObject night = Night.create(windowDimensions, CYCLE_LENGTH);
        gameObjects().addGameObject(night, Layer.FOREGROUND); //todo valid FOREGROUND

        // create sun:
        GameObject sun = Sun.create(windowDimensions, CYCLE_LENGTH);
        gameObjects().addGameObject(sun, Layer.BACKGROUND); //todo valid BACKGROUND

        // create Halo
        gameObjects().addGameObject(SunHalo.create(sun), Layer.BACKGROUND);

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