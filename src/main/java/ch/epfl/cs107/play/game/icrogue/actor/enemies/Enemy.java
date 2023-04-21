package ch.epfl.cs107.play.game.icrogue.actor.enemies;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.actor.ICRogueActor;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public abstract class Enemy extends ICRogueActor implements Interactable {

    // Indicates if enemy is alive
    private boolean isAlive;

    /**
     *
     * @param owner (Area): Belonging area
     * @param orientation (Orientation): Enemy's orientation
     * @param initialPosition (DiscreteCoordinates): Enemy's initial position
     */
    public Enemy(Area owner, Orientation orientation, DiscreteCoordinates initialPosition ){
        super(owner, orientation, initialPosition);
        isAlive = true;
    }

    public boolean isAlive() {
        return isAlive;
    }

    /**
     * isAlive to false and leaveas area
     */
    public void die(){
        isAlive = false;
        leaveArea();
    }
    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }
}
