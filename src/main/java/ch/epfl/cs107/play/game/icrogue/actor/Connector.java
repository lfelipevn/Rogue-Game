package ch.epfl.cs107.play.game.icrogue.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.area.ConnectorInRoom;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.List;

public class Connector extends ICRogueActor implements Interactable {

    final static int NO_KEY_ID = 0;
    private ConnectorState state;
    private String destinationArea;
    private DiscreteCoordinates arrivalCoordinates;

    //key that opens the connector
    int keyId;
    Sprite sprite;


    /**
     * Connector's constructor, by default invisible
     * @param owner (Area): Connector's Area
     * @param orientation (Orientation): Connector's orientation
     * @param coordinates (DiscreteCoordinates): Connector's position
     */
    public Connector(Area owner, Orientation orientation, DiscreteCoordinates coordinates) {
        super(owner, orientation, coordinates);
        state = ConnectorState.INVISIBLE;
        keyId = NO_KEY_ID;

        sprite = new Sprite("icrogue/invisibleDoor_"+orientation.opposite().ordinal(),
                (orientation.ordinal()+1)%2+1, orientation.ordinal()%2+1, this);

    }

    public enum ConnectorState{
        OPEN(),
        CLOSED(),
        LOCKED(),
        INVISIBLE(),;
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        DiscreteCoordinates coord = getCurrentMainCellCoordinates();
        return List.of(coord , coord.jump(new
                Vector((getOrientation().ordinal()+1)%2,
                getOrientation().ordinal()%2)));
    }


    @Override
    public void draw(Canvas canvas) {
        if (state != ConnectorState.OPEN){
            sprite.draw(canvas);
        }
    }

    /**
     * change state from closed to open
     */
    public void open(){
        if (state == ConnectorState.CLOSED){
            state = ConnectorState.OPEN;
        }
    }

    /**
     * If key is the same for the locked connector, open it
     * @param keyId (int)
     */
    public void openLockedDoor(int keyId){
        if (this.keyId == keyId){
            if (state == ConnectorState.LOCKED){
                state = ConnectorState.OPEN;
            }
        }
    }


    /**
     * Change state to closed
     */
    public void close(){
        state = ConnectorState.CLOSED;
        sprite = new Sprite("icrogue/door_"+getOrientation().opposite().ordinal(),
                (getOrientation().ordinal()+1)%2+1, getOrientation().ordinal()%2+1, this);

    }

    /**
     * Locks door with given id (can only be unlocked with same id key)
     * @param id (int): id of the key that will unlock door
     */
    public void lock(int id){
        state = ConnectorState.LOCKED;
        sprite = new Sprite("icrogue/lockedDoor_"+ getOrientation().opposite().ordinal(),
                (getOrientation().ordinal()+1)%2+1, getOrientation().ordinal()%2+1,
                this);
        keyId = id;
    }


    /**
     * change state from open to close and vice-versa
     */
    public void switchState(){
        if (state == ConnectorState.OPEN){
            close();
        } else if (state == ConnectorState.CLOSED) {
            open();
        }
    }



    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICRogueInteractionHandler)v).interactWith(this , isCellInteraction);
    }

    public ConnectorState getState() {
        return state;
    }

    public void setDestinationArea(String destinationArea){
        this.destinationArea = destinationArea;
    }

    public void setArrivalCoordinates(DiscreteCoordinates arrivalCoordinates){
        this.arrivalCoordinates = arrivalCoordinates;
    }

    public int getKeyId(){
        return keyId;
    }

    public String getDestinationArea() {
        return destinationArea;
    }

    public DiscreteCoordinates getArrivalCoordinates() {
        return new DiscreteCoordinates(arrivalCoordinates.x, arrivalCoordinates.y);
    }

    @Override
    public boolean takeCellSpace() {
        return state != ConnectorState.OPEN;
    }

    @Override
    public boolean isViewInteractable() {
        return true;
    }
}
