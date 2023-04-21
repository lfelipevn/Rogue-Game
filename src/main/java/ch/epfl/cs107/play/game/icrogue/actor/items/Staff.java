package ch.epfl.cs107.play.game.icrogue.actor.items;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class Staff extends Item{

    private Animation animation;


    public Staff(Area area, Orientation orientation, DiscreteCoordinates position){
        super(area, orientation, position);
        //sprite = new Sprite("zelda/staff_water.icon", .5f, .5f, this);
        Sprite[] sprites = Sprite.extractSprites("zelda/staff", 8, 1f, 1f, this, 32, 32);
        animation = new Animation(10, sprites);
    }

    @Override
    public void draw(Canvas canvas) {
        animation.draw(canvas);
        animation.update(1);
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

    @Override
    public boolean takeCellSpace() {
        return true;
    }

    @Override
    public boolean isViewInteractable() {
        return true;
    }
}
