package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.Turret;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.ArrayList;

public class Level0TurretRoom extends Level0EnemyRoom{

    /**
     * Constructor for the room
     * Adds turret top right corner and bottom left
     * @param roomCoordinates (DiscreteCoordinates): coordinates of the room.
     */
    public Level0TurretRoom(DiscreteCoordinates roomCoordinates) {
        super(roomCoordinates);

        ArrayList<Orientation> shootOrientations1 = new ArrayList<>();
        shootOrientations1.add(Orientation.DOWN);
        shootOrientations1.add(Orientation.RIGHT);
        addEnemy(new Turret(this, Orientation.UP, new DiscreteCoordinates(1, 8),  shootOrientations1));
        ArrayList<Orientation> shootOrientations2 = new ArrayList<>();
        shootOrientations2.add(Orientation.UP);
        shootOrientations2.add(Orientation.LEFT);
        addEnemy(new Turret(this, Orientation.UP, new DiscreteCoordinates(8, 1),  shootOrientations2));
    }

}
