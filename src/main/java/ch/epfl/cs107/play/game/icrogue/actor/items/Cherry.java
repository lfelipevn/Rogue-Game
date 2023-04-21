package ch.epfl.cs107.play.game.icrogue.actor.items;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Cherry extends Item{

    /**
     * Default Cherry constructor
     * @param area (Area): Owner area. Not null
     * @param position (Coordinate): Initial position of the entity. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     */
    public Cherry(Area area, Orientation orientation, DiscreteCoordinates position){
        super(area, orientation, position);
        sprite = new Sprite("icrogue/cherry", 0.6f, 0.6f, this);
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
}
