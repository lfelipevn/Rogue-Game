package ch.epfl.cs107.play.game.icrogue.actor.projectiles;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.actor.ICRogueActor;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.Collections;
import java.util.List;

public abstract class Projectile extends ICRogueActor implements Consumable, Interactor {

    private final int damagePoints;
    private int moveDuration;
    private boolean isConsumed;
    private final static int DEFAULT_DAMAGE = 1;
    private final static int DEFAULT_MOVE_DURATION = 10;

    protected Sprite sprite;


    /**
     * Projectile constructor with different potentialDamage and moveDuration
     * @param owner (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param coordinates (Coordinate): Initial position of the entity. Not null
     * @param damagePoints (int): potential damage
     * @param moveDuration (int): The number of ”frames” used for the calculation of the displacement
     */
    public Projectile(Area owner, Orientation orientation, DiscreteCoordinates coordinates, int damagePoints, int moveDuration) {
        super(owner, orientation, coordinates);
        this.damagePoints = damagePoints;
        this.moveDuration = moveDuration;
    }

    /**
     * Default Projectile constructor
     * @param owner (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param coordinates (Coordinate): Initial position of the entity. Not null
     */
    public Projectile(Area owner, Orientation orientation, DiscreteCoordinates coordinates){
        super(owner, orientation, coordinates);
        damagePoints = DEFAULT_DAMAGE;
        moveDuration = DEFAULT_MOVE_DURATION;
    }

    /**
     * Updates projectile by moving it depending on moveDuration
     * If it is consumed, leave area
     * @param deltaTime elapsed time since last update, in seconds, non-negative
     */
    @Override
    public void update(float deltaTime) {
        if (isConsumed()){
            leaveArea();
        }
        super.update(deltaTime);
        move(moveDuration);
    }


    /**
     * change isConsumed to true and leave are (unregister projectile from area)
     */
    @Override
    public void consume() {
        isConsumed = true;
        leaveArea();
    }

    @Override
    public boolean isConsumed() {
        return isConsumed;
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

    @Override
    public boolean wantsViewInteraction() {
        return true;
    }

}
