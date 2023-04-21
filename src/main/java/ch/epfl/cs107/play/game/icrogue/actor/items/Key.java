package ch.epfl.cs107.play.game.icrogue.actor.items;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Key extends Item{

    // id indicates which door can a key open
    private int id;

    /**
     * Default Key constructor
     * @param area (Area): Owner area. Not null
     * @param position (Coordinate): Initial position of the entity. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param id (int): id that indicates door it can open
     */
    public Key(Area area, Orientation orientation, DiscreteCoordinates position, int id){
        super(area, orientation, position);
        sprite = new Sprite("icrogue/key", 0.6f, 0.6f, this);
        this.id = id;
    }

    /**
     * Accepts interaction if hasn't been collected
     * @param v (AreaInteractionVisitor) : the visitor
     * @param isCellInteraction (boolean)
     */
    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        if (!isCollected()){
            ((ICRogueInteractionHandler)v).interactWith(this , isCellInteraction);
        }
    }

    public int getKeyId() {
        return id;
    }
}
