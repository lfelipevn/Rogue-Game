package ch.epfl.cs107.play.game.icrogue;

import ch.epfl.cs107.play.game.areagame.AreaGame;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.ICRoguePlayer;
import ch.epfl.cs107.play.game.icrogue.area.Level;
import ch.epfl.cs107.play.game.icrogue.area.level0.Level0;
import ch.epfl.cs107.play.game.icrogue.area.level0.rooms.Level0Room;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;

public class ICrogue extends AreaGame {

    public final static float CAMERA_SCALE_FACTOR = 13.f;

    private ICRoguePlayer player;

    private Level currentLevel;


    @Override
    public final String getTitle() {
        return "ICrogue";
    }


    /**
     * Creates rooms for levels and spawns player in default spawn position
     */
    private void initLevel(){
        currentLevel = new Level0(true);
        currentLevel.addAreas(this);
        setCurrentArea(currentLevel.getStartRoomTitle(), true);

        player = new ICRoguePlayer(getCurrentArea(), Orientation.UP, Level0.DEFAULT_ARRIVAL_COORDINATES);
        player.enterArea(getCurrentArea(), Level0.DEFAULT_ARRIVAL_COORDINATES);
    }


    /**
     * Initializes level
     * @param window (Window): display context. Not null
     * @param fileSystem (FileSystem): given file system. Not null
     * @return (boolean)
     */
    @Override
    public boolean begin(Window window, FileSystem fileSystem) {

        if (super.begin(window, fileSystem)) {
            initLevel();
            return true;
        }
        return false;
    }

    /**
     * Updates game, changes the player of room, restarts game if R is pressed
     * If hp are 0 or less ends game with "Game over"
     * If level is passed, game ends with "You won!"
     * @param deltaTime elapsed time since last update, in seconds, non-negative
     */
    @Override
    public void update(float deltaTime) {
        if (player.getHp() <= 0){
            System.out.println("Game over");
            end();
            System.exit(0);
        }
        else if (currentLevel.isOn()){
            System.out.println("You won!");
            end();
            System.exit(0);
        }
        Keyboard keyboard= getCurrentArea().getKeyboard();
        super.update(deltaTime);

        // If key R is pressed restart game
        if (keyboard.get(Keyboard.R).isPressed()){
            player.leaveArea();
            initLevel();
        }

        if (player.getIsChangingRoom()){
            switchRoom();
        }

    }


    /**
     * Player leaves room and enters another one according to connector it takes
     */
    private void switchRoom(){
        player.leaveArea();
        setCurrentArea(player.getDestinationRoomName(), false);
        player.enterArea(getCurrentArea(), player.getDestinationCoord());
    }

    /**
     * Closes window
     */
    @Override
    public void end() {
        getWindow().dispose();
    }


}