package ch.epfl.cs107.play.game.icrogue.area;

import ch.epfl.cs107.play.game.areagame.AreaGame;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.ICrogue;
import ch.epfl.cs107.play.game.icrogue.RandomHelper;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.signal.logic.Logic;

import java.awt.desktop.SystemSleepEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Level implements Logic {

    //Rooms that make up the level
    private ICrogueRoom[][] map;
    private DiscreteCoordinates arrivalCoordinates;
    //Position of the boss room
    private int[] bossRoomPosition;
    private String startRoomName;


    /**
     * Constructor for level
     * @param randomMap (boolean): indicates if random map is generated or fixed map
     * @param startPosition (DiscreteCoordinates): Coordinates of spawn room
     * @param roomsDistribution (int[]): For random generated maps, indicates how many rooms of each type
     *                          will be generated
     * @param width (int): map width for fixed map
     * @param height (int): map height for fixed map
     */

    public Level(boolean randomMap, DiscreteCoordinates startPosition, int[] roomsDistribution, int width, int height){
        // Default values
        arrivalCoordinates = startPosition;
        bossRoomPosition = new int[2];
        //Generates fixed map or random map depending on boolean randomMap

        if (randomMap){
            int sum = 0;
            for (int i: roomsDistribution){
                sum += i;
            }
            map = new ICrogueRoom[sum][sum];
            // Room placement is generated
            MapState[][] investmentMap = generateRandomRoomPlacement(sum);
            // Rooms placed random order using room placed generated before
            map = generateRandomMap(roomsDistribution, investmentMap);

        }
        else {
            map = new ICrogueRoom[height][width];
            generateFixedMap();
        }
        setStartRoomName(arrivalCoordinates);
    }


    protected void setArrivalCoord(DiscreteCoordinates arrivalCoordinates){
        this.arrivalCoordinates = arrivalCoordinates;
    }

    /**
     * Generates a random room placement for the indicated number of rooms and adds boss room
     * @param nbRooms (int): Number of rooms excluding boss room
     * @return (MapState [][]): with nbRooms MapState.PLACED or MapState.EXPLORED, one BOSS_ROOM
     * adjacent to MapState.PLACED and the rest MapState.NULL.
     */
    protected MapState [][] generateRandomRoomPlacement(int nbRooms){

        MapState[][] mapStates = new MapState[nbRooms][nbRooms];
        //Fill array with MapState.NULL
        for (MapState[] mapState : mapStates) {
            Arrays.fill(mapState, MapState.NULL);
        }
        // Middle room state Placed
        mapStates[(nbRooms/2)][(nbRooms/2)] = MapState.PLACED;

        int roomsToPlace = nbRooms - 1;
        int toBePlaced;
        List<Integer> chosenRooms;
        List<Orientation> freeSlots;
        List<Integer> freeSlotsInteger;
        DiscreteCoordinates newRoomCoords;
        Vector direction;

        // While there are still rooms to place, iterate over mapStates
        while (roomsToPlace > 0){
            for (int y = 0; y < mapStates.length; ++y){
                for (int x = 0; x < mapStates[y].length; ++x){

                    // If a cell is MapState.PLACED, check free (null) neighbour rooms
                    // If we still have rooms to place
                    // randomly choose 1 to min(freeSlots.size(), roomsToPlace) and change state to PLACED
                    // Update rooms to place
                    // Change cell whose neighbors where analyzed to EXPLORED
                    if (mapStates[y][x] == MapState.PLACED){
                        freeSlots = checkNeighSlotsState(mapStates, y, x, MapState.NULL);
                        freeSlotsInteger = orientationToIntArray(freeSlots);
                        if (freeSlots.size() > 0 && roomsToPlace > 0){
                            toBePlaced = RandomHelper.roomGenerator.nextInt(1, Math.min(freeSlots.size(), roomsToPlace) + 1);
                            chosenRooms = RandomHelper.chooseKInList(toBePlaced, freeSlotsInteger);
                            for (int k: chosenRooms){
                                if (Orientation.fromInt(k) == Orientation.UP || Orientation.fromInt(k) == Orientation.DOWN){
                                    direction = Orientation.fromInt(k).opposite().toVector();
                                } else {
                                    direction = Orientation.fromInt(k).toVector();
                                }
                                newRoomCoords = (new DiscreteCoordinates(x, y)).jump(direction);
                                mapStates[newRoomCoords.y][newRoomCoords.x] = MapState.PLACED;
                                roomsToPlace -= 1;
                            }
                        }
                        mapStates[y][x] = MapState.EXPLORED;
                    }
                }
            }
        }

        // Get cordinates of a NULL room next to PLACED room
        DiscreteCoordinates bossRoomCoord = generateBossRoomCord(mapStates);

        // Assing BOSS_ROOM to this coordinates and update class attribute
        mapStates[bossRoomCoord.y][bossRoomCoord.x] = MapState.BOSS_ROOM;
        bossRoomPosition = new int[]{bossRoomCoord.x, bossRoomCoord.y};

        return mapStates;
    }


    /**
     * Given number of rooms of each type to create and random placement, generates map
     * @param roomsDistribution (int[]): number of rooms of each type to create
     * @param investmentMap (MapState[][]): Room Placement
     * @return (ICrogueRoom[][]): Random map generated
     */
    protected ICrogueRoom[][] generateRandomMap(int[] roomsDistribution, MapState[][] investmentMap){
        int roomsToCreate = 0;
        for (int i : roomsDistribution){
            roomsToCreate += i;
        }
        List<Integer> possibleSlots;
        List<Integer> selectedSlots;
        List<DiscreteCoordinates> createdRoomsCords = new ArrayList<>();

        // For each type of room to create
        for (int i = 0; i < roomsDistribution.length; ++i){
            // Randomly select position for this type of rooms
            possibleSlots = consecutiveIntegerList(roomsToCreate);
            selectedSlots = RandomHelper.chooseKInList(roomsDistribution[i], possibleSlots);
            createdRoomsCords.clear();
            for (int j : selectedSlots){
                // Get Coordinates of the j th room PLACED or EXPLORED (room placement)
                DiscreteCoordinates slotCoord = getPlacedRoomK(investmentMap, j);
                // Create a room in map of type i in the coordinates just obtained
                createRoom(map, i, slotCoord);
                // Keep record of created rooms to change their state to CREATED in room placement
                createdRoomsCords.add(slotCoord);
                // Update roomsToCreate
                roomsToCreate -= 1;
            }
            // Change state of created room to CREATED in room placement
            for (DiscreteCoordinates coords: createdRoomsCords){
                investmentMap[coords.y][coords.x] = MapState.CREATED;
            }
        }

        // For every created room, set up its connectors

        for (int i = 0; i < map.length; ++i){
            for (int j = 0; j < map[i].length; ++j){
                if (investmentMap[i][j] == MapState.CREATED){
                    setUpConnector(investmentMap , map[i][j]);
                }
            }
        }

        //Creates boss room in assigned position
        createBossRoom(map, new DiscreteCoordinates(bossRoomPosition[0], bossRoomPosition[1]));
        // Set up its connectors (wont be used but will be functional)
        setUpConnector(investmentMap, map[bossRoomPosition[1]][bossRoomPosition[0]]);

        return map;
    }

    /**
     * Each level creates its BossRoom
     * @param map (ICrogueRoom[][]): Map where boss room will be created
     * @param position (DiscreteCoordinates): BossRoom position
     */
    protected abstract void createBossRoom(ICrogueRoom[][] map, DiscreteCoordinates position);

    /**
     * Converts a List of orientations to List of Integers with its corresponding .ordinal()
     * Useful because RandomHelper.chooseKInList takes List of Integers
     * @param freeSlots (List<Orientation>): Orientations to convert to Integers
     * @return (ArrayList<Integer>): ArrayList with .ordinal() corresponding to parameter list
     */
    private List<Integer> orientationToIntArray(List<Orientation> freeSlots){
        List<Integer> intArray = new ArrayList<>();
        for (Orientation orientation : freeSlots){
            intArray.add(orientation.ordinal());
        }
        return intArray;
    }

    /**
     * Each level sets up its connectors
     * @param roomsPlacement (MapState [][])
     * @param room (ICrogueRoom)
     */
    protected abstract void setUpConnector(MapState [][] roomsPlacement , ICrogueRoom room);

    /**
     * Given a room placement, returns coordinates of Kth room Placed or Explored
     * @param investmentMap (MapState[][]): room placement
     * @param selectedPlace (int): k
     * @return (DiscreteCoordinates): Coordinates of Kth room Placed or Explored
     */
    private DiscreteCoordinates getPlacedRoomK(MapState[][] investmentMap, int selectedPlace){
        int count = 0;
        for (int i = 0; i < investmentMap.length; ++i){
            for (int j = 0; j < investmentMap.length; ++j){
                if (investmentMap[i][j] == MapState.PLACED || investmentMap[i][j] == MapState.EXPLORED){
                    if (count == selectedPlace){
                        return new DiscreteCoordinates(j, i);
                    }
                    count += 1;
                }
            }
        }
        return null;
    }

    /**
     * Creates a room(ICrogueRoom) of type roomType(int) in the given coordinates.
     * Each level must implement this
     * @param map (ICrogueRoom[][])
     * @param roomType (int)
     * @param coord (DiscreteCoordinates)
     */
    protected abstract void createRoom(ICrogueRoom[][] map, int roomType, DiscreteCoordinates coord);

    /**
     * Returns a list of integers from 0 to bound (not included)
     * Useful because RandomHelper.chooseKInList takes list of Integers
     * ( this is made to randomly choose a position to create new rooms)
     * @param bound (int): upper bound for the list
     * @return (ArrayList<Integer>)
     */
    private List<Integer> consecutiveIntegerList(int bound){
        List<Integer> numberList = new ArrayList<Integer>();
        for (int i = 0; i < bound; ++i){
            numberList.add(i);
        }
        return numberList;
    }

    /**
     * Returns coordinates of a free room (Null) net to a placed one.
     * Boss room will be created here
     * @param mapStates (MapState[][]): Room placement
     * @return (DiscreteCoordinates)
     */
        private DiscreteCoordinates generateBossRoomCord(MapState[][] mapStates){
        for (int i = 0; i < mapStates.length; ++i){
            for (int j = 0; j < mapStates[i].length; ++j){
                if (mapStates[i][j] == MapState.NULL){
                    List<Orientation> placedNeighbors = checkNeighSlotsState(mapStates, i , j, MapState.PLACED);
                    if (placedNeighbors.size() > 0){
                        return new DiscreteCoordinates(j, i);
                    }
                }
            }
        }
        return null;
    }

    /**
     * Given an investment map, some coordinates and a state, this method returns
     * the orientation of the neighbours (up, down, right or left) that have the given state
     * in the investment map
     * @param mapStates (MapState[][]): Room placement
     * @param i (int): y coordinate
     * @param j (int): x coordinate
     * @param mapState (MapState): State to look for
     * @return
     */
    protected List<Orientation> checkNeighSlotsState(MapState[][] mapStates, int i, int j, MapState mapState){
        List<Orientation> slots = new ArrayList<>();
        int length = mapStates.length - 1;
        if (i > 0){
            if (mapStates[i-1][j] == mapState){
                slots.add(Orientation.UP);
            }
        }
        if (i < length){
            if (mapStates[i+1][j] == mapState){
                slots.add(Orientation.DOWN);
            }
        }
        if (j > 0){
            if (mapStates[i][j-1] == mapState){
                slots.add(Orientation.LEFT);
            }
        }
        if (j < length) {
            if (mapStates[i][j + 1] == mapState) {
                slots.add(Orientation.RIGHT);
            }
        }
        return slots;
    }




        protected abstract void generateFixedMap();

    /**
     * Assigns a room to certain coordinates in map
     * @param coords (DiscreteCoordinates) : coordinates where the given room will be placed
     * @param room (ICrogueRoom) : room to be placed
     */
    protected void setRoom(DiscreteCoordinates coords, ICrogueRoom room){
        map[coords.y][coords.x] = room;
    }


    /**
     * Sets connector destination area coordinates depending on connector's index
     * @param coords (DiscreteCoordinates): room coordinates
     * @param destination (String): destination area name
     * @param connector (ConnectorInRoom) connector that we want to set destination area and coordinates
     */

    protected void setRoomConnectorDestination(DiscreteCoordinates coords, String destination,
                                               ConnectorInRoom connector){
        map[coords.y][coords.x].setConnectorDestinationArea(connector.getIndex(), destination);
        map[coords.y][coords.x].setConnectorArrivalCoordinates(connector.getIndex());
    }

    /**
     * Sets connector destination area coordinates depending on connector's index
     * (Same as method setRoomConnectorDestination but makes connector state closed)
     * @param coords (DiscreteCoordinates): room coordinates
     * @param destination (String): destination area name
     * @param connector (ConnectorInRoom) connector that we want to set destination area and coordinates
     */
    protected void setRoomConnector(DiscreteCoordinates coords, String destination,
                          ConnectorInRoom connector){
        setRoomConnectorDestination(coords, destination, connector);
        map[coords.y][coords.x].closeConnector(connector.getIndex());
    }

    /**
     * Locks a connector and assigns a keyId that opens it
     * @param coords (DiscreteCoordinates): Room coordinates of the connector
     * @param connector (ConnectorInRoom): Connector we want to lock
     * @param keyId (int): Sets keyId that will open this door
     */

    protected void lockRoomConnector(DiscreteCoordinates coords, ConnectorInRoom connector,
                                     int keyId){

        map[coords.y][coords.x].lockConnector(connector.getIndex(), keyId);
    }

    /**
     * Initializes starting room name attribute
     * @param coords (DiscreteCoordinates): Starting room coordinates.
     */
    protected void setStartRoomName(DiscreteCoordinates coords){
        startRoomName = map[coords.y][coords.x].getTitle();
    }

    /**
     * Adds every room in the map as an area to given AreaGame
     * @param areaGame (AreaGame): AreaGame where rooms will be added as areas
     */
    public void addAreas(AreaGame areaGame){
        for (int i = 0; i < map.length; ++i){
            for (int j = 0; j < map[i].length; ++j){
                if (map[i][j] != null){
                    areaGame.addArea(map[i][j]);
                }
            }
        }
    }



    public String getStartRoomTitle(){
        return startRoomName;
    }

    /**
     * Level is passed if boss room is passed
     * @return indicates boolean
     */
    @Override
    public boolean isOn() {
        if (map[bossRoomPosition[1]][bossRoomPosition[0]] != null){
            return map[bossRoomPosition[1]][bossRoomPosition[0]].isOn();
        }
        else return false;
    }



    @Override
    public boolean isOff() {
        return !isOn();
    }

    @Override
    public float getIntensity() {
        if (isOn()){
            return TRUE.getIntensity();
        }
        else return FALSE.getIntensity();
    }

    protected enum MapState {
        NULL , // Empty space
        PLACED , // The room has been placed but not yet explored by the room placement algorithm
        EXPLORED , // The room has been placed and explored by the algorithm
        BOSS_ROOM , // The room is a boss room
        CREATED; // The room has been instantiated in the room map
        @Override
        public String toString() {
            return Integer.toString(ordinal());
        }
        }
}
