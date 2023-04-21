package ch.epfl.cs107.play.game.icrogue.actor.enemies;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.actor.projectiles.Arrow;
import ch.epfl.cs107.play.game.icrogue.actor.projectiles.Fire;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.List;

public class Turret extends Enemy{
    private final Sprite sprite;
    // Orientations where it shoots
    private final List<Orientation> shootDirections;
    // Cooldown to shoot again
    private final static double COOLDOWN = 2f;
    //Used for the countdown
    private double counter = 0;

    /**
     *
     * @param owner (Area): Belonging area
     * @param orientation (Orientation): Turret's orientation
     * @param initialPosition (DiscreteCoordinates): Where turret is init
     * @param shootDirections (List<Orientation>) Orientations where turret shoots
     */
    public Turret(Area owner, Orientation orientation, DiscreteCoordinates initialPosition, List<Orientation> shootDirections) {
        super(owner, orientation, initialPosition);
        sprite = new Sprite("icrogue/static_npc", 1.5f, 1.5f,
                this , null , new Vector(-0.25f, 0));
        this.shootDirections = shootDirections;
    }


    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
    }

    /**
     * Creates new arrow depending on shootDirections
     */
    private void shoot(){
        for (Orientation direction: shootDirections){
            getOwnerArea().registerActor(new Arrow(getOwnerArea(), direction, getCurrentMainCellCoordinates().jump(direction.toVector())));
        }
    }

    /**
     * Shoots with a constant rate
     * @param deltaTime elapsed time since last update, in seconds, non-negative
     */
    @Override
    public void update(float deltaTime) {
        counter += deltaTime;
        if (counter >= COOLDOWN){
            counter = 0;
            shoot();
        }
        super.update(deltaTime);
    }

    /**
     * Accepts interaction if it is alive
     * @param v (AreaInteractionVisitor) : the visitor
     * @param isCellInteraction (boolean)
     */
    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        if (isAlive()){
            ((ICRogueInteractionHandler)v).interactWith(this , isCellInteraction);
        }
    }

}
