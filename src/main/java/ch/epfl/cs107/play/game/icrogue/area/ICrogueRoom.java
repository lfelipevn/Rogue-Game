package ch.epfl.cs107.play.game.icrogue.area;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.ICrogueBehavior;
import ch.epfl.cs107.play.game.icrogue.actor.Connector;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;
import java.util.ArrayList;
import java.util.List;

public abstract class ICrogueRoom extends Area implements Logic {

    private DiscreteCoordinates roomCoordinates;
    private String behaviorName;
    private ArrayList<Connector> connectors;
    private boolean visitedBefore = false;


    /**
     * Generic ICrogueRoom is passed if it is once visited
     * @return (boolean) visitedBefore
     */
    @Override
    public boolean isOn() {
        return visitedBefore;
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



    /**
     *
     * @param connectorsCoordinates (List<DiscreteCoordinates >): Discrete coordinates for the connectors
     * @param orientations ( List<Orientation>): Orientation of the connectors (same order as connectorsCoordinaates)
     * @param behaviorName (String): name of the behavior
     * @param roomCoordinates (DiscreteCoordinates): coordinates of the room
     */
    protected ICrogueRoom(List<DiscreteCoordinates > connectorsCoordinates ,
                       List<Orientation> orientations , String behaviorName , DiscreteCoordinates roomCoordinates){
        assert connectorsCoordinates.size() == orientations.size();
        this.roomCoordinates = roomCoordinates;
        this.behaviorName = behaviorName;
        // Initializing connectors
        connectors = new ArrayList<>();
        for (int i = 0; i < connectorsCoordinates.size(); ++i){
            connectors.add(new Connector(this, orientations.get(i), connectorsCoordinates.get(i)));
        }

    }

    /**
     * Add area and register connectors
     */
    protected void createArea(){
        for (Connector connector : connectors){
            registerActor(connector);
        }
    };

    @Override
    public final float getCameraScaleFactor() {
        return 11;
    }

    protected abstract DiscreteCoordinates getPlayerSpawnPosition();

    /**
     * Sets behavior and creates area
     * @param window (Window): display context. Not null
     * @param fileSystem (FileSystem): given file system. Not null
     * @return
     */
    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            // Set the behavior map
            ICrogueBehavior behavior = new ICrogueBehavior(window, behaviorName);
            setBehavior(behavior);
            createArea();
            return true;
        }
        return false;
    }

    /**
     * Updates room and opens closed connectors if room is passed
     * @param deltaTime elapsed time since last update, in seconds, non-negative
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        Keyboard keyboard= getKeyboard();

        if (isOn()){
            for (Connector connector : connectors){
                connector.open();
            }
        }
    }


    protected String getBehaviorName(){
        return behaviorName;
    }

    public DiscreteCoordinates getRoomCoordinates(){
        DiscreteCoordinates coords = new DiscreteCoordinates(roomCoordinates.x, roomCoordinates.y);
        return coords;
    }

    /**
     * Set destination area of a connector
     * @param index (int): position of the connector in array connectors
     * @param destination (String): destination area
     */
    protected void setConnectorDestinationArea(int index, String destination){
        connectors.get(index).setDestinationArea(destination);
    }


    abstract protected void setConnectorArrivalCoordinates(int index);

    /**
     * Closes a connector based on index
     * @param index (int): index of the connector in connectors array to close
     */
    protected void closeConnector(int index) {
        connectors.get(index).close();
    }


    /**
     * Locks a connector based on index
     * @param index (int): index of the connector in connectors array to lock
     */
    protected void lockConnector(int index, int keyId) {
        connectors.get(index).lock(keyId);
    }

    /**
     * Sets a conector arrival coordinates
     * @param index (int): index of the connector in connectors array
     * @param destination (DiscreteCoordinates): arrival coordinates
     */
    protected void setConnectorArrivalCoordinates(int index, DiscreteCoordinates destination){
        connectors.get(index).setArrivalCoordinates(destination);
    }

    /**
     * Indicates room has been visited
     */
    public void visited(){
        visitedBefore = true;
    }





}
