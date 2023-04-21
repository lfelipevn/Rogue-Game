package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.items.Item;
import ch.epfl.cs107.play.game.icrogue.actor.items.Staff;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.ArrayList;

public class Level0StaffRoom extends Level0ItemRoom{

    private final DiscreteCoordinates staffCoord = new DiscreteCoordinates(5, 5);

    /**
     * Constructor for the room with a staff
     * @param roomCoordinates (DiscreteCoordinates): coordinates of the room.
     */
    public Level0StaffRoom(DiscreteCoordinates roomCoordinates) {
        super(roomCoordinates);
        addItem(new Staff(this, Orientation.UP, staffCoord));
    }


}
