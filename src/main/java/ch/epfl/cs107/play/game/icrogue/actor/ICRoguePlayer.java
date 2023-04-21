package ch.epfl.cs107.play.game.icrogue.actor;

import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.Turret;
import ch.epfl.cs107.play.game.icrogue.actor.items.Cherry;
import ch.epfl.cs107.play.game.icrogue.actor.items.Key;
import ch.epfl.cs107.play.game.icrogue.actor.items.Staff;
import ch.epfl.cs107.play.game.icrogue.actor.projectiles.Arrow;
import ch.epfl.cs107.play.game.icrogue.actor.projectiles.Fire;
import ch.epfl.cs107.play.game.icrogue.area.ICrogueRoom;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ICRoguePlayer extends ICRogueActor implements Interactor, Interactable {

    private Animation[] animations;

    private ArrayList<Integer> keyIds = new ArrayList<Integer>();

    private final static int MOVE_DURATION = 3;

    // Handler for interactions

    private final ICRoguePlayerInteractionHandler handler;


    // Indicates if player has staff
    private boolean hasStaff;

    //Indicates if player wants to change room
    private boolean isChangingRoom;
    private String destinationRoomName;
    private DiscreteCoordinates destinationCoord;

    //health points
    private int hp;
    private final int INIT_HP = 5;

    private TextGraphics message;


    /**
     * Default ICRoguePlayer constructor
     * @param owner (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param coordinates (DiscreteCoordinates): Initial position of the entity. Not null
     */
    public ICRoguePlayer(Area owner, Orientation orientation, DiscreteCoordinates coordinates){
        super(owner, orientation, coordinates);

        handler = new ICRoguePlayerInteractionHandler();

        hasStaff = false;

        Sprite[][] sprites;

        sprites = Sprite.extractSprites("zelda/player", 4, .75f, 1.5f, this, 16, 32, new Vector( .15f, -.15f), new Orientation[]{Orientation.DOWN, Orientation.RIGHT, Orientation.UP, Orientation.LEFT});

        animations = Animation.createAnimations(1, sprites);

        hp = INIT_HP;

        message = new TextGraphics(Integer.toString((int)hp), 0.3f, Color.GREEN);
        message.setParent(this);
        message.setAnchor(new Vector(-0.2f, 0.1f));

        resetMotion();
    }


    /**
     * Moves player with arrows, invokes Fire with X if hasStaff
     * And player isChangingRoom is now false
     * @param deltaTime elapsed time since last update, in seconds, non-negative
     */
    @Override
    public void update(float deltaTime) {
        Keyboard keyboard= getOwnerArea().getKeyboard();

        moveIfPressed(Orientation.LEFT, keyboard.get(Keyboard.LEFT));
        moveIfPressed(Orientation.UP, keyboard.get(Keyboard.UP));
        moveIfPressed(Orientation.RIGHT, keyboard.get(Keyboard.RIGHT));
        moveIfPressed(Orientation.DOWN, keyboard.get(Keyboard.DOWN));

        message.setText(Integer.toString((int)hp));
        // If player has Staff and X is pressed, fire
        if (hasStaff){
            if (keyboard.get(Keyboard.X).isPressed()){
                getOwnerArea().registerActor(new Fire(getOwnerArea(),getOrientation(), getCurrentMainCellCoordinates().jump(getOrientation().toVector())));
            }
        }
        isChangingRoom = false;
        super.update(deltaTime);
    }

    /**
     * Indicates if players is changing room (through connector)
     * @return (boolean) isChangingRoom
     */
    public boolean getIsChangingRoom(){
        return isChangingRoom;
    }

    /**
     * Reduces hp by damage
     * @param damage (int) damage made to player
     */

    public void takeDamage(int damage){
        hp -= damage;
    }

    public int getHp(){
        return hp;
    }

    public DiscreteCoordinates getDestinationCoord(){
        return new DiscreteCoordinates(destinationCoord.x, destinationCoord.y);
    }

    /**
     * Used in update. Orientates and moves player according to arrows pressed and updates animation
     * @param orientation (Orientation): New orientation of the player
     * @param b (Button): Button of the arrows
     */
    private void moveIfPressed(Orientation orientation, Button b){
        if(b.isDown()) {
            if (!isDisplacementOccurs()) {
                orientate(orientation);
                move(MOVE_DURATION);
                for (Animation animation : animations){
                    animation.update(1);
                }
            }
        }
    }

    public String getDestinationRoomName() {
        return destinationRoomName;
    }

    @Override
    public void draw(Canvas canvas) {
        animations[getOrientation().ordinal()].draw(canvas);
        message.draw(canvas);
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return super.getCurrentCells();
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return Collections.singletonList(
                getCurrentMainCellCoordinates().jump(
                        getOrientation().toVector()));
    }

    @Override
    public boolean wantsCellInteraction() {
        return true;
    }

    /**
     * Always view interactable (So arrows dont overlap with player and
     * damage him just before touching (Seems like point touched)
     * @return (boolean) true
     */
    @Override
    public boolean isViewInteractable() {
        return true;
    }

    /**
     * Player requests view interaction if W is pressed
     * @return (boolean) if player wants view interaction
     */
    @Override
    public boolean wantsViewInteraction() {
        Keyboard keyboard= getOwnerArea().getKeyboard();
        return keyboard.get(Keyboard.W).isDown();
    }

    /**
     * Enters area and changes visitedBefore to true
     * @param area (Area): initial area, not null
     * @param position (DiscreteCoordinates): initial position, not null
     */
    @Override
    public void enterArea(Area area, DiscreteCoordinates position) {
        super.enterArea(area, position);
        ((ICrogueRoom)area).visited();
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICRogueInteractionHandler)v).interactWith(this , isCellInteraction);
    }


    @Override
    public void interactWith(Interactable other , boolean isCellInteraction) {
        other.acceptInteraction(handler , isCellInteraction);
    }


    private class ICRoguePlayerInteractionHandler implements ICRogueInteractionHandler{

        /**
         * Collects cherry
         * @param cherry (Cherry)
         * @param isCellInteraction (boolean)
         */
        public void interactWith(Cherry cherry, boolean isCellInteraction) {
            if (isCellInteraction){
                cherry.collect();
            }
        }

        /**
         * Collects staff, hasStaff is true and changes animation
         * @param staff (Staff)
         * @param isCellInteraction (boolean)
         */

        public void interactWith(Staff staff, boolean isCellInteraction) {
            if (!isCellInteraction){
                staff.collect();
                hasStaff = true;
                Sprite[][] sprites;
                sprites = Sprite.extractSprites("zelda/player.staff_water", 4, 1.5f, 1.5f, ICRoguePlayer.this, 32, 32, new Vector( .15f, -.15f), new Orientation[]{Orientation.DOWN, Orientation.UP, Orientation.RIGHT, Orientation.LEFT});
                animations = Animation.createAnimations(1, sprites);
            }
        }


        /**
         * collets key and adds it to keyIds
         * @param key (Key)
         * @param isCellInteraction (boolean)
         */
        public void interactWith(Key key, boolean isCellInteraction){
            if (isCellInteraction){
                key.collect();
                keyIds.add(key.getKeyId());
            }
        }



        /**
         * For contact interactions, it changes player's room
         *   For view interactions, if connector is closed and player has
         *   its key, it opens it
         * @param connector (Connector):
         * @param isCellInteraction (boolean)
         */
        public void interactWith(Connector connector, boolean isCellInteraction){
            if (!isCellInteraction){
                if (connector.getState() == Connector.ConnectorState.LOCKED){
                    for (int keyId : keyIds){
                        if (keyId == connector.getKeyId()){
                            connector.openLockedDoor(keyId);
                        }
                    }
                }
            }
            if (isCellInteraction && (!isDisplacementOccurs())){
                isChangingRoom = true;
                destinationRoomName = connector.getDestinationArea();
                destinationCoord = connector.getArrivalCoordinates();
            }
        }

        /**
         * If player steps on Turret, it dies
         * @param turret (Turret): Turret interactable
         * @param isCellInteraction (boolean)
         */
        public void interactWith(Turret turret, boolean isCellInteraction){
            if (isCellInteraction){
                turret.die();
            }
        }
    }




}
