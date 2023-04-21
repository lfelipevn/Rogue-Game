package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.Turret;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.ArrayList;

public class Level0BossRoom extends Level0EnemyRoom{

    /**
     * Constructor for the room
     * As turret room, adds turret top right corner and bottom left
     * Additionally, two more turrets in cords (3,3) and (6,6) that shoot in the 4 directions (orientations)
     * @param roomCoordinates (DiscreteCoordinates): coordinates of the room.
     */
    public Level0BossRoom(DiscreteCoordinates roomCoordinates) {
        super(roomCoordinates);

        ArrayList<Orientation> shootOrientations1 = new ArrayList<>();
        shootOrientations1.add(Orientation.DOWN);
        shootOrientations1.add(Orientation.RIGHT);
        addEnemy(new Turret(this, Orientation.UP, new DiscreteCoordinates(1, 8),  shootOrientations1));
        ArrayList<Orientation> shootOrientations2 = new ArrayList<>();
        shootOrientations2.add(Orientation.UP);
        shootOrientations2.add(Orientation.LEFT);
        addEnemy(new Turret(this, Orientation.UP, new DiscreteCoordinates(8, 1),  shootOrientations2));
        ArrayList<Orientation> shootOrientations3 = new ArrayList<>();
        shootOrientations3.add(Orientation.UP);
        shootOrientations3.add(Orientation.LEFT);
        shootOrientations3.add(Orientation.RIGHT);
        shootOrientations3.add(Orientation.DOWN);
        addEnemy(new Turret(this, Orientation.UP, new DiscreteCoordinates(6, 6),  shootOrientations3));
        addEnemy(new Turret(this, Orientation.UP, new DiscreteCoordinates(3, 3),  shootOrientations3));
    }
}
