package pepse.world;

import danogl.GameObject;
import danogl.collisions.Collision;
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
    private static final double PAUSE_BETWEEN_ANIMAITONS = 0.3;
    //    private static final float GRAVITY = 650;
    // fields
    private final UserInputListener inputListener;
    private final ImageReader imageReader;
    private final AnimationRenderable idleAnimaiton;
    private final AnimationRenderable jumpingAnimaiton;
    private final AnimationRenderable sidesAnimaiton;
    private float energyLevel = MAX_ENERGY;
    private boolean isJump = false;

    // animations
    private static final String[] IDLE_ANIMATION = new String[]{"assets/idle_0.png", "assets/idle_1.png",
            "assets/idle_2.png", "assets/idle_3.png"};
    private static final String[] JUMP_ANIMATION = new String[]{"assets/jump_0.png", "assets/jump_1.png",
            "assets/jump_2.png", "assets/jump_3.png"};
    private static final String[] SIDES_ANIMATION = new String[]{"assets/run_0.png", "assets/run_1.png",
            "assets/run_2.png", "assets/run_3.png", "assets/run_4.png", "assets/run_5.png"};

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
        this.idleAnimaiton = new AnimationRenderable(IDLE_ANIMATION, imageReader, false,
                PAUSE_BETWEEN_ANIMAITONS);
        this.jumpingAnimaiton = new AnimationRenderable(JUMP_ANIMATION, imageReader, false,
                PAUSE_BETWEEN_ANIMAITONS);
        this.sidesAnimaiton = new AnimationRenderable(SIDES_ANIMATION, imageReader, false,
                PAUSE_BETWEEN_ANIMAITONS);
        renderer().setRenderable(this.idleAnimaiton);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float prevXVel = getVelocity().x();
        float prevYVel = getVelocity().y();
        float xVel = 0;

        if(inputListener.isKeyPressed(KeyEvent.VK_SPACE) && getVelocity().y() == 0){
            if(energyLevel >= JUMPING_ENERGY_INTAKE) {
                transform().setVelocityY(VELOCITY_Y);
                increaceEnergy(-JUMPING_ENERGY_INTAKE);
                renderer().setRenderable(jumpingAnimaiton);
                isJump = true;
            }
        }
        if(inputListener.isKeyPressed(KeyEvent.VK_LEFT)){
            xVel -= VELOCITY_X;
            renderer().setRenderable(sidesAnimaiton);
            renderer().setIsFlippedHorizontally(true);
        }
        if(inputListener.isKeyPressed(KeyEvent.VK_RIGHT)){
            xVel += VELOCITY_X;
            renderer().setRenderable(sidesAnimaiton);
            renderer().setIsFlippedHorizontally(false);
        }
        // if there is no movement sideways
        if(xVel == 0){
            if (getVelocity().y() == 0){
                increaceEnergy(1);
                renderer().setRenderable(idleAnimaiton);
            }
        }else if (energyLevel > SIDES_MOVES_ENERGY_INTAKE){
            increaceEnergy(-SIDES_MOVES_ENERGY_INTAKE);
        }else{
            xVel = 0;
        }
//        else if (getVelocity().y() == 0 ){ // if moves sidways
//            if (energyLevel >= SIDES_MOVES_ENERGY_INTAKE){ // if you have energy for this
//                increaceEnergy(-SIDES_MOVES_ENERGY_INTAKE);
//            }
        //transform
        transform().setVelocityX(xVel);
    }

    //    fill this function
    private void manageAnimation(float prevXVal, float xVel, float prevYVal) {
        if ((prevYVal != 0) && (getVelocity().y() == 0)){
            renderer().setRenderable(idleAnimaiton);
        }
        if (prevXVal != xVel){
            if (xVel == 0){
                if (getVelocity().y() == 0){
                    renderer().setRenderable(idleAnimaiton);
                }
                else {
                    renderer().setRenderable(jumpingAnimaiton);
                }
            }
            else if (xVel > 0){
                renderer().setRenderable(sidesAnimaiton);
                renderer().setIsFlippedHorizontally(false);
            }
            else {
                renderer().setRenderable(sidesAnimaiton);
                renderer().setIsFlippedHorizontally(true);
            }
        }
    }


    private void increaceEnergy(float val){
        energyLevel+=val;
        if (energyLevel > MAX_ENERGY){
            energyLevel = MAX_ENERGY;
        } else if (energyLevel < 0) {
            energyLevel = 0;
        }
    }

    public int getEnergyLevel() {
        return (int) energyLevel;
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (other.getTag().equals("block")) {
            this.transform().setVelocityY(0);
        }
    }
}

//package pepse.world;
//
//import danogl.GameObject;
//import danogl.collisions.Collision;
//import danogl.gui.ImageReader;
//import danogl.gui.UserInputListener;
//import danogl.gui.rendering.AnimationRenderable;
//import danogl.util.Vector2;
//
//import java.awt.event.KeyEvent;
//
///**
// * The Avatar class represents the player's character in the game.
// * It handles movement, animations, and energy consumption.
// */
//public class Avatar extends GameObject {
//    private static final String AVATAR_IMG_PATH = "assets/idle_0.png";
//    private static final String AVATAR_TAG = "avatar";
//    private static final int MAX_ENERGY = 100;
//    private static final Vector2 AVATAR_DIMENSIONS = new Vector2(50, 80);
//    private static final float VELOCITY_X = 400;
//    private static final float VELOCITY_Y = -400;
//    private static final float SIDES_MOVES_ENERGY_INTAKE = 0.5f;
//    private static final float JUMPING_ENERGY_INTAKE = 10;
//    private static final double PAUSE_BETWEEN_ANIMATIONS = 0.3;
//
//    private final UserInputListener inputListener;
//    private final ImageReader imageReader;
//    private final AnimationRenderable idleAnimation;
//    private final AnimationRenderable jumpingAnimation;
//    private final AnimationRenderable sidesAnimation;
//    private float energyLevel = MAX_ENERGY;
//    private boolean isJump = false;
//
//    /**
//     * Constructs an Avatar object.
//     *
//     * @param pos          The initial position of the avatar.
//     * @param inputListener The input listener for handling user input.
//     * @param imageReader  The image reader for loading textures.
//     */
//    public Avatar(Vector2 pos, UserInputListener inputListener, ImageReader imageReader) {
//        super(pos.subtract(new Vector2(0, AVATAR_DIMENSIONS.y())), AVATAR_DIMENSIONS,
//                imageReader.readImage(AVATAR_IMG_PATH, true));
//        this.inputListener = inputListener;
//        this.imageReader = imageReader;
//        this.setTag(AVATAR_TAG);
//        this.physics().preventIntersectionsFromDirection(Vector2.ZERO);
//        this.transform().setAccelerationY(400);
//        this.idleAnimation = new AnimationRenderable(new String[]{"assets/idle_0.png", "assets/idle_1.png", "assets/idle_2.png", "assets/idle_3.png"}, imageReader, false, PAUSE_BETWEEN_ANIMATIONS);
//        this.jumpingAnimation = new AnimationRenderable(new String[]{"assets/jump_0.png", "assets/jump_1.png", "assets/jump_2.png", "assets/jump_3.png"}, imageReader, false, PAUSE_BETWEEN_ANIMATIONS);
//        this.sidesAnimation = new AnimationRenderable(new String[]{"assets/run_0.png", "assets/run_1.png", "assets/run_2.png", "assets/run_3.png", "assets/run_4.png", "assets/run_5.png"}, imageReader, false, PAUSE_BETWEEN_ANIMATIONS);
//        renderer().setRenderable(this.idleAnimation);
//    }
//
//    @Override
//    public void update(float deltaTime) {
//        super.update(deltaTime);
//        float prevXVel = getVelocity().x();
//        float prevYVel = getVelocity().y();
//        float xVel = 0;
//
//        if(inputListener.isKeyPressed(KeyEvent.VK_SPACE) && getVelocity().y() == 0){
//            if(energyLevel >= JUMPING_ENERGY_INTAKE) {
//                transform().setVelocityY(VELOCITY_Y);
//                increaceEnergy(-JUMPING_ENERGY_INTAKE);
//                renderer().setRenderable(jumpingAnimaiton);
//                isJump = true;
//            }
//        }
//        if(inputListener.isKeyPressed(KeyEvent.VK_LEFT)){
//            xVel -= VELOCITY_X;
//        }
//        if(inputListener.isKeyPressed(KeyEvent.VK_RIGHT)){
//            xVel += VELOCITY_X;
//        }
//        // if there is no movement sideways
//        if(xVel == 0 && getVelocity().y() == 0){
//            increaceEnergy(1);
//        }
//        else if (xVel != 0 && getVelocity().y() == 0 ){ // if moves sidways
//            if (energyLevel >= SIDES_MOVES_ENERGY_INTAKE){ // if you have energy for this
//                increaceEnergy(-SIDES_MOVES_ENERGY_INTAKE);
//            }
//            else{ // if you dont have energy, dont move
//                xVel = 0;
//            }
//        }
//
//        //transform
//        manageAnimation(prevXVel, xVel, prevYVel);
//        transform().setVelocityX(xVel);
//
//    }
//
//    //    fill this function
//    private void manageAnimation(float prevXVal, float xVel, float prevYVal) {
//        if ((prevYVal != 0) && (getVelocity().y() == 0)){
//            renderer().setRenderable(idleAnimaiton);
//        }
//        if (prevXVal != xVel){
//            if (xVel == 0){
//                if (getVelocity().y() == 0){
//                    renderer().setRenderable(idleAnimaiton);
//                }
//                else {
//                    renderer().setRenderable(jumpingAnimaiton);
//                }
//            }
//            else if (xVel > 0){
//                renderer().setRenderable(sidesAnimaiton);
//                renderer().setIsFlippedHorizontally(false);
//            }
//            else {
//                renderer().setRenderable(sidesAnimaiton);
//                renderer().setIsFlippedHorizontally(true);
//            }
//        }
//    }
//
//
////     * Increases or decreases the avatar's energy level.
////     *
////     * @param val The amount to adjust the energy level by.
//    private void increaseEnergy(float val) {
//        energyLevel += val;
//        if (energyLevel > MAX_ENERGY) {
//            energyLevel = MAX_ENERGY;
//        } else if (energyLevel < 0) {
//            energyLevel = 0;
//        }
//    }
//
//    /**
//     * Retrieves the current energy level of the avatar.
//     *
//     * @return The avatar's energy level as an integer.
//     */
//    public int getEnergyLevel() {
//        return (int) energyLevel;
//    }
//
//    /**
//     * Handles collisions between the avatar and other game objects.
//     *
//     * @param other     The other game object involved in the collision.
//     * @param collision The collision details.
//     */
//    @Override
//    public void onCollisionEnter(GameObject other, Collision collision) {
//        super.onCollisionEnter(other, collision);
//        if (other.getTag().equals("block")) {
//            this.transform().setVelocityY(0);
//        }
//    }
//}
