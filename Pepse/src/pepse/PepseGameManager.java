package pepse;

import danogl.GameManager;
import danogl.GameObject;
import pepse.world.Sky;

public class PepseGameManager extends GameManager {

    void initializeGame(){
        Sky sky = new Sky();
        gameObjects().addGameObject(sky, skyLayer);
    }

    public static void main(String[] args) {
        new PepseGameManager().run();
    }
}
