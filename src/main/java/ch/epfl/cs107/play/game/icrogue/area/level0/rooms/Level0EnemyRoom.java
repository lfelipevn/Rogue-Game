package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;

import ch.epfl.cs107.play.game.icrogue.actor.enemies.Enemy;
import ch.epfl.cs107.play.game.icrogue.actor.items.Item;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class Level0EnemyRoom extends Level0Room{

    private List<Enemy> enemyList;
    /**
     * Constructor for the room
     * @param roomCoordinates (DiscreteCoordinates): coordinates of the room.
     */
    public Level0EnemyRoom(DiscreteCoordinates roomCoordinates) {
        super(roomCoordinates);
        enemyList = new ArrayList<Enemy>();
    }

    protected void addEnemy(Enemy enemy){
        enemyList.add(enemy);
    }

    /**
     * Room is completed if enemyList is empty (all dead)
     * @return (boolean) is room completed
     */
    @Override
    public boolean isOn() {
        return enemyList.isEmpty();
    }

    /**
     * Creates area and registers enemies
     */
    @Override
    protected void createArea() {
        super.createArea();
        for (Enemy enemy: enemyList){
            registerActor(enemy);
        }
    }

    @Override
    public void update(float deltaTime) {
        enemyList.removeIf(enemy -> !enemy.isAlive());
        super.update(deltaTime);
    }
}
