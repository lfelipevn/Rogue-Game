package ch.epfl.cs107.play.game.icrogue.area.level0;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.area.ICrogueRoom;
import ch.epfl.cs107.play.game.icrogue.area.Level;
import ch.epfl.cs107.play.game.icrogue.area.level0.rooms.*;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.signal.logic.Or;

import java.util.ArrayList;
import java.util.List;

public class Level0 extends Level {

    private final static int DEFAULT_WIDTH = 4;
    private final static int DEFAULT_HEIGHT = 2;
    public final static DiscreteCoordinates DEFAULT_ARRIVAL_COORDINATES = new DiscreteCoordinates(2, 2);

    private final static DiscreteCoordinates DEFAULT_STARTING_ROOM_COORDS = new DiscreteCoordinates(1, 1);
    private final static int PART_1_KEY_ID = 1;
    private final static int BOSS_KEY_ID = 2;


    /**
     * Default constructor of Level0
     */
    public Level0() {
        super(false, DEFAULT_STARTING_ROOM_COORDS, null, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public Level0(boolean randomMap){
        super(randomMap, DEFAULT_STARTING_ROOM_COORDS, RoomType.SPAWN.getDistributionList(),DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }


    public void generateFixedMap(){
        //generateMap1();
        generateMap2();
    }

    /**
     * Fixed map 1
     */
    private void generateMap1() {
        DiscreteCoordinates room00 = new DiscreteCoordinates(0, 0);
        setRoom(room00, new Level0KeyRoom(room00, PART_1_KEY_ID));
        setRoomConnector(room00, "icrogue/level010", Level0Room.Level0Connectors.E);
        lockRoomConnector(room00, Level0Room.Level0Connectors.E,  PART_1_KEY_ID);

        DiscreteCoordinates room10 = new DiscreteCoordinates(1, 0);
        setRoom(room10, new Level0Room(room10));
        setRoomConnector(room10, "icrogue/level000", Level0Room.Level0Connectors.W);
    }

    /**
     * Fixed map 2
     */
    private void generateMap2() {
        DiscreteCoordinates room00 = new DiscreteCoordinates(0, 0);
        setRoom(room00, new Level0TurretRoom(room00));
        setRoomConnector(room00, "icrogue/level010", Level0Room.Level0Connectors.E);

        DiscreteCoordinates room10 = new DiscreteCoordinates(1,0);
        setRoom(room10, new Level0Room(room10));
        setRoomConnector(room10, "icrogue/level011", Level0Room.Level0Connectors.S);
        setRoomConnector(room10, "icrogue/level020", Level0Room.Level0Connectors.E);

        lockRoomConnector(room10, Level0Room.Level0Connectors.W,  BOSS_KEY_ID);
        setRoomConnectorDestination(room10, "icrogue/level000", Level0Room.Level0Connectors.W);

        DiscreteCoordinates room20 = new DiscreteCoordinates(2,0);
        setRoom(room20,  new Level0StaffRoom(room20));
        setRoomConnector(room20, "icrogue/level010", Level0Room.Level0Connectors.W);
        setRoomConnector(room20, "icrogue/level030", Level0Room.Level0Connectors.E);

        DiscreteCoordinates room30 = new DiscreteCoordinates(3,0);
        setRoom(room30, new Level0KeyRoom(room30, BOSS_KEY_ID));
        setRoomConnector(room30, "icrogue/level020", Level0Room.Level0Connectors.W);

        DiscreteCoordinates room11 = new DiscreteCoordinates(1, 1);
        setRoom (room11, new Level0Room(room11));
        setRoomConnector(room11, "icrogue/level010", Level0Room.Level0Connectors.N);
    }


    /**
     * Given room placement and a room, it sets  up its connectors
     * @param roomsPlacement (MapState [][])
     * @param room (ICrogueRoom)
     */
    @Override
    protected void setUpConnector(MapState[][] roomsPlacement, ICrogueRoom room) {

        //With investment map, get neighbour rooms to given room whose state is CREATED
        List<Orientation> connectorsToCreate = checkNeighSlotsState(roomsPlacement, room.getRoomCoordinates().y, room.getRoomCoordinates().x, MapState.CREATED);
        List<Level0Room.Level0Connectors> connectorsList = orientationToLevel0Array(connectorsToCreate);
        DiscreteCoordinates destination;
        String destinationName;
        Vector destinationDirection;
        // For each of them, set it up with destination name and destination coordinates
        for (int i = 0; i < connectorsToCreate.size(); ++i){

            //This is made because in the array of the map, going up means substracting 1 from y and
            // Going down is adding 1
            if (connectorsToCreate.get(i) == Orientation.UP || connectorsToCreate.get(i) == Orientation.DOWN ){
                destinationDirection = connectorsToCreate.get(i).opposite().toVector();
            } else {
                destinationDirection = connectorsToCreate.get(i).toVector();
            }


            destination =
                    room.getRoomCoordinates().jump(destinationDirection);
            destinationName = "icrogue/level0" + destination.x + destination.y;
            setRoomConnector(room.getRoomCoordinates(), destinationName, connectorsList.get(i));
        }

        // The same is done but with state BOSS_ROOM instead of CREATED so the boss room is connected to the
        //rest of the map
        // This is done separately because connectors leading to boss room must be locked with BOSS_KEY_ID
        List<Orientation> connectorsToCreateBoss = checkNeighSlotsState(roomsPlacement, room.getRoomCoordinates().y, room.getRoomCoordinates().x, MapState.BOSS_ROOM);
        connectorsList = orientationToLevel0Array(connectorsToCreateBoss);
        for (int i = 0; i < connectorsToCreateBoss.size(); ++i){
            if (connectorsToCreateBoss.get(i) == Orientation.UP || connectorsToCreateBoss.get(i) == Orientation.DOWN ){
                destinationDirection = connectorsToCreateBoss.get(i).opposite().toVector();
            } else {
                destinationDirection = connectorsToCreateBoss.get(i).toVector();
            }
            destination =
                    room.getRoomCoordinates().jump(destinationDirection);
            destinationName = "icrogue/level0" + destination.x + destination.y;

            setRoomConnector(room.getRoomCoordinates(), destinationName, connectorsList.get(i));
            lockRoomConnector(room.getRoomCoordinates(), connectorsList.get(i),  BOSS_KEY_ID);


        }


    }

    /**
     * Creates a room in map in the DiscreteCoordinates coord of type roomType
     * @param map (ICrogueRoom[][])
     * @param roomType (int)
     * @param coord (DiscreteCoordinates)
     */
    @Override
    protected void createRoom(ICrogueRoom[][] map, int roomType, DiscreteCoordinates coord) {
        if (roomType == RoomType.TURRET_ROOM.ordinal()){
            map[coord.y][coord.x] = new Level0TurretRoom(coord);
        } else if (roomType == RoomType.STAFF_ROOM.ordinal()) {
            map[coord.y][coord.x] = new Level0StaffRoom(coord);
        } else if (roomType == RoomType.BOSS_KEY.ordinal()) {
            // Create key room with BOSS_KEY_ID
            map[coord.y][coord.x] = new Level0KeyRoom(coord, BOSS_KEY_ID);
        } else if (roomType == RoomType.SPAWN.ordinal()) {
            // Normal room but sets spawn here
            map[coord.y][coord.x] = new Level0Room(coord);
            setArrivalCoord(coord);
        } else if (roomType == RoomType.NORMAL.ordinal()) {
            map[coord.y][coord.x] = new Level0Room(coord);
        }
    }

    /**
     * Creates boss room (unique type)
     * @param map (ICrogueRoom[][]): Map where boss room will be created
     * @param position (DiscreteCoordinates): BossRoom position
     */
    @Override
    protected void createBossRoom(ICrogueRoom[][] map, DiscreteCoordinates position) {
        map[position.y][position.x] = new Level0BossRoom(position);
    }

    /**
     * Given orientation list, returns corresponding Level0Room.Level0Connectors
     * ( UP -> N, DOWN -> S, RIGHT -> E, LEFT -> W)
     * @param orientationList (List<Orientation>)
     * @return (ArrayList<Level0Room.Level0Connectors>)
     */
    private List<Level0Room.Level0Connectors> orientationToLevel0Array(List<Orientation> orientationList){
        List<Level0Room.Level0Connectors> connectorList = new ArrayList<>();
        for (Orientation orientation: orientationList){
            if (orientation == Orientation.UP){
                connectorList.add(Level0Room.Level0Connectors.N);
            } else if (orientation == Orientation.DOWN) {
                connectorList.add(Level0Room.Level0Connectors.S);
            } else if (orientation == Orientation.RIGHT) {
                connectorList.add(Level0Room.Level0Connectors.E);
            } else {
                connectorList.add(Level0Room.Level0Connectors.W);
            }
        }
        return connectorList;
    }

    /**
     * Enum with number of rooms per room type
     */
    public enum RoomType {
        TURRET_ROOM (3), // type and number of rooms
        STAFF_ROOM (1),
        BOSS_KEY (1),
        SPAWN(1),
        NORMAL(1);

        private final int numberRooms;
        RoomType(int i) {
            numberRooms = i;
        }


        /**
         * Returns int array of number of rooms per room type in same order as enum
         * @return (int[])
         */
        public int[] getDistributionList(){
            int[] list = new int[RoomType.values().length];
            for (int i = 0; i < list.length; ++i){
                list[i] = RoomType.values()[i].numberRooms;
            }
            return list;
        }
    }

}
