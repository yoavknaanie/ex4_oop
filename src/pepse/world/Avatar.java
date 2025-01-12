package pepse.world;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;
/**
 * The Avatar class represents a character in the game world that can move, jump, and interact with objects.
 * It has energy levels that are affected by movement and actions, such as jumping or running.
 * The avatar can play different animations based on its actions (idle, jumping, running).
 */
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
    public static final int ZERO = 0;
    public static final int AVATAR_ACCELERATION = 400;
    public static final int ONE = 1;
    public static final String BLOCK_TAG = "block";
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
    /**
     * Constructs a new Avatar object.
     *
     * @param pos The position of the avatar in the game world.
     * @param inputListener The listener for user input (key presses).
     * @param imageReader The image reader used to load images for the avatar's animations.
     */
    public Avatar(Vector2 pos, UserInputListener inputListener, ImageReader imageReader) {
        super(pos.subtract(new Vector2(ZERO, avatarDimensions.y())), avatarDimensions,
                imageReader.readImage(AVTAR_IMG_PATH, true));
        this.inputListener = inputListener;
        this.imageReader = imageReader;
        this.setTag(AVATAR_TAG);
        this.physics().preventIntersectionsFromDirection(Vector2.ZERO);
        this.transform().setAccelerationY(AVATAR_ACCELERATION);
        this.idleAnimaiton = new AnimationRenderable(IDLE_ANIMATION, imageReader, false,
                PAUSE_BETWEEN_ANIMAITONS);
        this.jumpingAnimaiton = new AnimationRenderable(JUMP_ANIMATION, imageReader, false,
                PAUSE_BETWEEN_ANIMAITONS);
        this.sidesAnimaiton = new AnimationRenderable(SIDES_ANIMATION, imageReader, false,
                PAUSE_BETWEEN_ANIMAITONS);
        renderer().setRenderable(this.idleAnimaiton);
    }
    /**
     * Updates the avatar's position and state based on user input and physics.
     *
     * @param deltaTime The time elapsed since the last update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float xVel = ZERO;

        if(inputListener.isKeyPressed(KeyEvent.VK_SPACE) && getVelocity().y() == ZERO){
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
        if(xVel == ZERO){
            if (getVelocity().y() == ZERO){
                increaceEnergy(ONE);
                renderer().setRenderable(idleAnimaiton);
            }
        }else if (energyLevel > SIDES_MOVES_ENERGY_INTAKE){
            increaceEnergy(-SIDES_MOVES_ENERGY_INTAKE);
        }else{
            xVel = ZERO;
        }
        if (getVelocity().y() == ZERO){
            isJump = false;
        }
        //transform
        transform().setVelocityX(xVel);
    }
    /**
     * Returns whether the avatar is currently jumping.
     *
     * @return true if the avatar is jumping, false otherwise.
     */
    public Boolean isAvatarJumping() {
        return isJump;
    }

    /**
     * Increases or decreases the avatar's energy level.
     *
     * @param val The amount by which the energy level is changed.
     */
    public void increaceEnergy(float val) {
        energyLevel += val;
        if (energyLevel > MAX_ENERGY) {
            energyLevel = MAX_ENERGY;  // Cap energy at MAX_ENERGY
        } else if (energyLevel < ZERO) {
            energyLevel = ZERO;  // Ensure energy doesn't go below zero
        }
    }

    /**
     * Returns the current energy level of the avatar.
     *
     * @return The current energy level as an integer.
     */
    public int getEnergyLevel() {
        return (int) energyLevel;
    }

    /**
     * Handles collisions with other game objects.
     * If the avatar collides with a block, it stops vertical movement (land on ground).
     *
     * @param other The other game object that the avatar collided with.
     * @param collision The collision details.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (other.getTag().equals(BLOCK_TAG)) {
            this.transform().setVelocityY(ZERO);
        }
    }
}
