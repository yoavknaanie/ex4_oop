package pepse.world;

import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.Sound;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Avatar extends GameObject {
    //    constants
    private static final String AVTAR_IMG_PATH = "assets/idle_0.png";
    private static final String AVATAR_TAG = "avatar";
    private static final int MAX_ENERGY = 100;
    private static final Vector2 avatarDimensions = new Vector2(50, 80); // todo check size
    private static final float VELOCITY_X = 400;
    private static final float VELOCITY_Y = -400;
    private static final float SIDES_MOVES_ENERGY_INTAKE = 0.5f;
    private static final float JUMPING_ENERGY_INTAKE = 10;
    //    private static final float GRAVITY = 650;
    // fields
    private final UserInputListener inputListener;
    private final ImageReader imageReader;
    private float energyLevel = MAX_ENERGY;
    private boolean isJump = false;
    boolean jump = false;

    public Avatar(Vector2 pos, UserInputListener inputListener, ImageReader imageReader) {
        super(pos.subtract(new Vector2(0, avatarDimensions.y())), avatarDimensions,
                imageReader.readImage(AVTAR_IMG_PATH, true));
        this.inputListener = inputListener;
        this.imageReader = imageReader;
        this.setTag(AVATAR_TAG);
        // prevents avatar from going through ground
        this.physics().preventIntersectionsFromDirection(Vector2.ZERO);
        // acceleration of Y axis, always pull the character down.
        this.transform().setAccelerationY(400);
//        todo
//        animationRenderable = new AnimationRenderable[NUM_OF_ANIMATION_SEQUENCES];
//        formAnimations();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float xVel = 0;
        //if standing
//        if (!isJump && ((inputListener.isKeyPressed(KeyEvent.VK_LEFT) &&
//                inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) ||
//                (!inputListener.isKeyPressed(KeyEvent.VK_LEFT) &&
//                        !inputListener.isKeyPressed(KeyEvent.VK_RIGHT)))){
//                increaceEnergy(1);
//        } else {
        if(inputListener.isKeyPressed(KeyEvent.VK_LEFT)){
            xVel -= VELOCITY_X;
        }
        if(inputListener.isKeyPressed(KeyEvent.VK_RIGHT)){
            xVel += VELOCITY_X;
        }
        if(inputListener.isKeyPressed(KeyEvent.VK_SPACE) && getVelocity().y() == 0){
            if(energyLevel >= JUMPING_ENERGY_INTAKE) {
                transform().setVelocityY(VELOCITY_Y);
                increaceEnergy(-JUMPING_ENERGY_INTAKE);
            }
        }
        // if no movement sideways
        if(xVel == 0){
            if(getVelocity().y() == 0){
                increaceEnergy(1);
            }
        }
        else if (energyLevel >= SIDES_MOVES_ENERGY_INTAKE){
            increaceEnergy(-SIDES_MOVES_ENERGY_INTAKE);
        }
        transform().setVelocityX(xVel);
    }

    private void increaceEnergy(float val){
        energyLevel+=val;
        if (energyLevel > MAX_ENERGY){
            energyLevel = MAX_ENERGY;
        } else if (energyLevel < 0) {
            energyLevel = 0;
        }
    // some code for rendering the energy level
    }
}

