package ch.epfl.cs107.play.game.icrogue.handler;

import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.ICrogueBehavior;
import ch.epfl.cs107.play.game.icrogue.actor.Connector;
import ch.epfl.cs107.play.game.icrogue.actor.ICRoguePlayer;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.Turret;
import ch.epfl.cs107.play.game.icrogue.actor.items.Cherry;
import ch.epfl.cs107.play.game.icrogue.actor.items.Key;
import ch.epfl.cs107.play.game.icrogue.actor.items.Staff;
import ch.epfl.cs107.play.game.icrogue.actor.projectiles.Arrow;
import ch.epfl.cs107.play.game.icrogue.actor.projectiles.Fire;


/**
     * This interface defines an empty method for each Interactable in the game.
     * Each Interactor must define a inner class like <<InteractorName>>Handler
     * (implementing this interface)
     * that specifies with which Interactables they interact, and how.
     * This way it is just needed to override the methods with the Interactables
     * the Interactor will interact.
     *
     * Finally, each Interactor must have an attribute of its own handler type
     * and the Interactor delegates its interactions to its handler.
     */
public interface ICRogueInteractionHandler extends AreaInteractionVisitor {

    default void interactWith(Connector connector, boolean isCellInteraction){};

    default void interactWith(Cherry cherry, boolean isCellInteraction){};

    default void interactWith(Staff staff, boolean isCellInteraction){};

    default void interactWith(ICrogueBehavior.ICRogueCell cell, boolean isCellInteraction){};

    default void interactWith(Key key, boolean isCellInteraction){};

    default void interactWith(Fire fire, boolean isCellInteraction) {}

    default void interactWith(Arrow arrow, boolean isCellInteraction) {}

    default void interactWith(ICRoguePlayer player, boolean isCellInteraction) {}

    default void interactWith(Turret turret, boolean isCellInteraction) {}
}
