package dev.project276.main;

import dev.project276.entity.*;
import dev.project276.levels.MazeThing;
import dev.project276.levels.StarterLevel;
import dev.project276.levels.StarterLevel2;

import java.util.ArrayList;


/**
 * A class for containing the game's levels (GameInfos) and switching between them.
 */
public class GameLevelSwitcher {
    /**
     * The ArrayList of levels in the entire game.
     */
    ArrayList<GameInfo> levels = new ArrayList<>();
    int currentLevelIndex = 0;

    public GameLevelSwitcher(){
        init();
    }

    /**
     * Handles entity updating
     */
    public void updateCurrentLevel(){
        //System.out.println("\tTimer: " + timer + "; InputUpdates: " + inputUpdates);
        // FIXME: Right now, we can't remove elements from the entityList without
        //  an exception being thrown. How can this be fixed?
        Player player = null;
        // current solution: duplicate the entity list so removing elements is ok
        ArrayList<Entity> entityList = new ArrayList<>(getCurrentLevel().getEntityList());
        if (!isDone()){
            for (Entity entity : entityList) {
                if (entity.getEntityType() == Entity.EntityType.player){
                    player = (Player) entity;
                }
                else{
                    entity.update();
                }
            }
        }

        if (player != null) {
            player.update();
            if (player.getWinLevel()){
                goNextLevel();
            }
        }
    }

    /**
     * Initalizes all of the levels.
     */
    private void init(){
        addLevel(new MazeThing(), 0);
        addLevel(new StarterLevel(), 1);
        addLevel(new StarterLevel2(), 2);
    }

    /**
     * Used to return the current level index to test
     */
    public int getCurrentLevelIndex(){
        return currentLevelIndex;
    }

    /**
     * Gets the current level from the list of levels.
     * @return A GameInfo reference representing the current level.
     */
    public GameInfo getCurrentLevel(){
        if (levels.size() > 0 && currentLevelIndex < levels.size()){
            return levels.get(currentLevelIndex);
        }
        return levels.get(levels.size()-1);
    }

    /**
     * Re-initializes the current level.
     * Used to restart the current level.
     */
    public void initCurrentLevel(){
        levels.set(currentLevelIndex, levels.get(currentLevelIndex).restartLevel());
    }

    /**
     * Goes to the next level by incrementing the current level index.
     */
    public void goNextLevel(){
        currentLevelIndex++;
    }

    /**
     * Returns if the player is done with all of the levels.
     * @return true if all of the levels have been exhausted. false otherwise.
     */
    public boolean isDone(){
        return currentLevelIndex >= levels.size();
    }

    /**
     * Sets the current level of the GameLevelSwitcher to the specified level
     * in the parameter
     * @param gi The level to replace with.
     */
    public void replaceCurrentLevel(GameInfo gi){
        if (!isDone()){
            levels.set(currentLevelIndex, gi);
        }
    }

    /**
     * Adds a level to the list of levels.
     * @param info The GameInfo object representing the level to add.
     * @param level The current level number that is being added. If the level
     *              has been already added, the info level will not be added.
     */
    private void addLevel(GameInfo info, int level) {
        if (level <= levels.size()){
            levels.add(level, info);
        }
    }

    /**
     * Initializes Level 1.
     * @return The GameInfo object representing Level 1.
     */
    private GameInfo initLevel1(){
        GameInfo level1 = new StarterLevel();
        level1.initLevel();
        return level1;
    }

    /**
     * Initializes Level 2.
     * @return The GameInfo object representing Level 2.
     */
    private GameInfo initLevel2(){
        GameInfo level2 = new StarterLevel2();
        level2.initLevel();
        return level2;
    }
}
