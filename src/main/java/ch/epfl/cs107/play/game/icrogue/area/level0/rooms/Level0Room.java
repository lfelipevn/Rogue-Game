package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.area.ConnectorInRoom;
import ch.epfl.cs107.play.game.icrogue.area.ICrogueRoom;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.ArrayList;
import java.util.List;

public class Level0Room extends ICrogueRoom {

    /**
     * Constructor for the room
     * @param roomCoordinates (DiscreteCoordinates): coordinates of the room.
     */
    public Level0Room(DiscreteCoordinates roomCoordinates){
        super(Level0Connectors.getAllConnectorsPosition(), Level0Connectors.getAllConnectorsOrientation(), "icrogue/Level0Room" , roomCoordinates);
    }

    /**
     *
     * @return Name of level + room coordinates
     */
    @Override
    public String getTitle() {
        String title = "icrogue/level0";
        title = title + getRoomCoordinates().x + getRoomCoordinates().y;
        return title;
    }

    /**
     * Sets connector arrival coordinates
     * @param index (int): index in array of connector to set arrival coordinates
     */
    @Override
    protected void setConnectorArrivalCoordinates(int index) {
        super.setConnectorArrivalCoordinates(index, Level0Connectors.values()[index].destination);
    }

    /**
     * Creates area and registers actor Background
     */
    protected void createArea() {
        super.createArea();
        // Base
        registerActor(new Background(this , getBehaviorName()));
    }

    @Override
    protected DiscreteCoordinates getPlayerSpawnPosition() {
        return new DiscreteCoordinates(2,2);
    }

    public enum Level0Connectors implements ConnectorInRoom {
        // order of attributes: position , destination , orientation
        W(new DiscreteCoordinates(0, 4), new DiscreteCoordinates(8, 5) ,
                Orientation.LEFT),
        S(new DiscreteCoordinates(4, 0), new DiscreteCoordinates(5, 8),
                Orientation.DOWN),
        E(new DiscreteCoordinates(9, 4), new DiscreteCoordinates(1, 5),
                Orientation.RIGHT ),
        N(new DiscreteCoordinates(4, 9), new DiscreteCoordinates(5, 1),
                Orientation.UP);

        final DiscreteCoordinates position;
        final DiscreteCoordinates destination;
        final Orientation orientation;


        Level0Connectors(DiscreteCoordinates position, DiscreteCoordinates destination, Orientation orientation) {
            this.position = position;
            this.destination = destination;
            this.orientation = orientation;
        }

        @Override
        public DiscreteCoordinates getDestination() {
            return destination;
        }

        @Override
        public int getIndex() {
            return this.ordinal();
        }

        /**
         * Returns all connectors orientations in enum order
         * @return (List <Orientation >) List of all room's connectors Orientations
         */
        static List <Orientation > getAllConnectorsOrientation(){
            List <Orientation > orientations = new ArrayList<>();
            for (Level0Connectors level0connectors : Level0Connectors.values()){
                orientations.add(level0connectors.orientation);
            }
            return orientations;
        }


        /**
         * Returns all connectors position in enum order
         * @return (List <DiscreteCoordinates >) List of all room's connectors position
         */
        static List<DiscreteCoordinates > getAllConnectorsPosition(){
            List <DiscreteCoordinates > orientations = new ArrayList<>();
            for (Level0Connectors level0connectors : Level0Connectors.values()){
                orientations.add(level0connectors.position);
            }
            return orientations;
        }

    }
}
