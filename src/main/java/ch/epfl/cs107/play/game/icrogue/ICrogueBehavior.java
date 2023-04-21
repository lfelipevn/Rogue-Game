package ch.epfl.cs107.play.game.icrogue;

import ch.epfl.cs107.play.game.actor.Entity;
import ch.epfl.cs107.play.game.areagame.AreaBehavior;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.game.tutosSolution.Tuto2Behavior;
import ch.epfl.cs107.play.window.Window;

public class ICrogueBehavior extends AreaBehavior {


    public enum ICrogueCellType{
        //https://stackoverflow.com/questions/25761438/understanding-bufferedimage-getrgb-output-values
        NONE(0,false), // Should cannever be used except in the toType method
        GROUND(-16777216, true ), // traversable
        WALL(-14112955, false), // not traversable
        HOLE(-65536, true),;

        private final int type;
        private final boolean isWalkable;

        /**
         * ICrogueCellType Constructor
         * @param type (type): type of cell depending on the color
         * @param isWalkable (boolean): indicates if cell is walkable
         */
        ICrogueCellType(int type, boolean isWalkable){
            this.type = type;
            this.isWalkable = isWalkable;
        }


        /**
         * depending on integer type (depending on color), it returns corresponding ICrogueCellType
         */
        private static ICrogueBehavior.ICrogueCellType toType(int type){
            for(ICrogueBehavior.ICrogueCellType ict : ICrogueBehavior.ICrogueCellType.values()){
                if(ict.type == type)
                    return ict;
            }
            // When you add a new color, you can print the int value here before assign it to a type
            System.out.println(type);
            return NONE;
        }

    }

    /**
     * Default ICrogueBehavior Constructor
     * @param window (Window), not null
     * @param name (String): Name of the Behavior, not null
     */
    public ICrogueBehavior(Window window, String name){
        super(window, name);
        int height = getHeight();
        int width = getWidth();
        for(int y = 0; y < height; y++) {
            for (int x = 0; x < width ; x++) {
                ICrogueBehavior.ICrogueCellType color = ICrogueBehavior.ICrogueCellType.toType(getRGB(height-1-y, x));
                setCell(x,y, new ICrogueBehavior.ICRogueCell(x,y,color));
            }
        }
    }


    /**
     * Cell adapted to the ICRogue game
     */
    public class ICRogueCell extends Cell{
        /// Type of the cell following the enum
        private final ICrogueBehavior.ICrogueCellType type;


        /**
         * Default ICRogueCell Constructor
         * @param x (int): x coordinate of the cell
         * @param y (int): y coordinate of the cell
         * @param type (ICrogueCellType), not null
         */
        private  ICRogueCell(int x, int y, ICrogueBehavior.ICrogueCellType type){
            super(x, y);
            this.type = type;
        }

        @Override
        protected boolean canLeave(Interactable entity) {
            return true;
        }

        @Override
        protected boolean canEnter(Interactable entity) {
            // entity can enter the cell only if it is walkable
            // and there are no entities in the cell that take the cell space
            if (type.isWalkable) {
                for (Interactable entity1: entities)
                    if (entity1.takeCellSpace()){
                        return false;
                    }
                return true;
            } else {
                return false;
            }

        }

        @Override
        public boolean isCellInteractable() {
            return true;
        }

        @Override
        public boolean isViewInteractable() {
            return false;
        }

        @Override
        public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
            ((ICRogueInteractionHandler)v).interactWith(this , isCellInteraction);
        }

        public ICrogueCellType getCellType(){
            return type;
        }
    }
}
