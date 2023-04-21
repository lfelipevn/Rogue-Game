package ch.epfl.cs107.play.game.icrogue.actor.projectiles;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.ICrogueBehavior;
import ch.epfl.cs107.play.game.icrogue.actor.ICRoguePlayer;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class Arrow extends Projectile{
    Sprite sprite;
    ArrowInteractionHandler handler;

    private final static int ARROW_DAMAGE_POINTS = 1;
    private final static int ARROW_MOVE_DURATION = 3;

    /**
     * Arrow Constructor
     * @param owner (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param coordinates (Coordinate): Initial position of the entity. Not null
     */
    public Arrow(Area owner, Orientation orientation, DiscreteCoordinates coordinates) {
        super(owner, orientation, coordinates, ARROW_DAMAGE_POINTS, ARROW_MOVE_DURATION);
        sprite = new Sprite("zelda/arrow", 1f, 1f, this , new
                RegionOfInterest(32* orientation.ordinal(), 0, 32, 32), new
                Vector(0, 0));
        handler = new ArrowInteractionHandler();


    }

    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
    }

    /**
     * Accepts interaction if not consumed
     * @param v (AreaInteractionVisitor) : the visitor
     * @param isCellInteraction (boolean)
     */
    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICRogueInteractionHandler)v).interactWith(this , isCellInteraction);
    }


    /**
     * Delegates interactions to its handler if ot consumed
     * @param other (Interactable). Not null
     * @param isCellInteraction True if this is a cell interaction
     */
    @Override
    public void interactWith(Interactable other , boolean isCellInteraction) {
        if (!isConsumed()){
            other.acceptInteraction(handler , isCellInteraction);
        }
    }
    private class ArrowInteractionHandler implements ICRogueInteractionHandler{

        /**
         * If next cell is a WALL or HOLE, consume
         * @param cell (Cell)
         * @param isCellInteraction (boolean)
         */
        @Override
        public void interactWith(ICrogueBehavior.ICRogueCell cell, boolean isCellInteraction) {
            if (!isCellInteraction && (cell.getCellType() == ICrogueBehavior.ICrogueCellType.WALL || cell.getCellType() == ICrogueBehavior.ICrogueCellType.HOLE)){
                consume();
            }
        }

        /**
         * Player takes damage if it is in the same cell or one ahead.
         * @param player (ICRoguePlayer)
         * @param isCellInteraction (boolean)
         */
        public void interactWith(ICRoguePlayer player, boolean isCellInteraction){
            consume();
            player.takeDamage(ARROW_DAMAGE_POINTS);
        }

    }
}
