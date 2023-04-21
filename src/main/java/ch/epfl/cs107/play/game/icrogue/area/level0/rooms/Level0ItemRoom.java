package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;

import ch.epfl.cs107.play.game.icrogue.actor.items.Item;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.ArrayList;
import java.util.List;

public abstract class Level0ItemRoom extends Level0Room{
    List<Item> itemList;

    /**
     * Constructor for the room with an item
     *
     * @param roomCoordinates (DiscreteCoordinates): coordinates of the room.
     */
    public Level0ItemRoom(DiscreteCoordinates roomCoordinates) {
        super(roomCoordinates);
        itemList = new ArrayList<Item>();
    }

    /**
     * adds item to itemList
     * @param item (Item): item to add in itemList
     */
    public void addItem(Item item){
        itemList.add(item);
    }

    /**
     * Creates area and registers items in it
     */
    @Override
    protected void createArea() {
        super.createArea();
        for (Item item: itemList){
            registerActor(item);
        }
    }

    /**
     * Room is completed if all items are collected
     * @return (boolean): is room completed
     */
    @Override
    public boolean isOn() {
        if (super.isOn()){
            for (Item item: itemList){
                if (!item.isCollected()){
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }
}
