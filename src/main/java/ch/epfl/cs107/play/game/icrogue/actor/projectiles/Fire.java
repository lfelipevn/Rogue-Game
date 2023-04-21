package ch.epfl.cs107.play.game.icrogue.actor.projectiles;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.ICrogueBehavior;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.Turret;
import ch.epfl.cs107.play.game.icrogue.actor.items.Staff;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class Fire extends Projectile{
    private final static int FIRE_DAMAGE_POINTS = 1;
    private final static int FIRE_MOVE_DURATION = 3;

    private Animation animation;

    private final FireInteractionHandler handler;


    /**
     *  Fire Constructor
     * @param owner (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param coordinates (Coordinate): Initial position of the entity. Not null
     */
    public Fire(Area owner, Orientation orientation, DiscreteCoordinates coordinates){
        super(owner, orientation, coordinates, FIRE_DAMAGE_POINTS, FIRE_MOVE_DURATION);
        Sprite[] sprites = Sprite.extractSprites("zelda/fire", 7, 1f, 1f, this, 16, 16);
        animation = new Animation(4, sprites);
        handler = new FireInteractionHandler();
    }

    @Override
    public void draw(Canvas canvas) {
        animation.draw(canvas);
        animation.update(1);
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

    /**
     * Accepts interaction if not consumed
     * @param v (AreaInteractionVisitor) : the visitor
     * @param isCellInteraction (boolean)
     */
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        if (!isConsumed()){
            ((ICRogueInteractionHandler)v).interactWith(this , isCellInteraction);
        }
    }



    private class FireInteractionHandler implements ICRogueInteractionHandler{

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
         * If it is in the same cell as a turret, kill turret and consume fire
         * @param turret (Turret)
         * @param isCellInteraction (boolean)
         */
        public void interactWith(Turret turret, boolean isCellInteraction){
            if (isCellInteraction){
                turret.die();
                consume();
            }
        }
    }


}
