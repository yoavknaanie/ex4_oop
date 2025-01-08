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
import pepse.world.trees.Fruit;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Avatar extends GameObject {
    //    constants
    private static final String AVTAR_IMG_PATH = "assets/idle_0.png";
    public static final String AVATAR_TAG = "avatar";
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
        if (getVelocity().y() == 0){
            isJump = false;
        }
        //transform
        transform().setVelocityX(xVel);
    }

    public boolean isAvatarJumping(){
        return isJump;
    }

    public void increaceEnergy(float val){
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
