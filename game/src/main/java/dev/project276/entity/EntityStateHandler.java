package dev.project276.entity;

import dev.project276.main.GameInfo;

import java.awt.*;
import java.util.ArrayList;

/**
 * Class that updates the Player and entityList accordingly
 * with which entities are on the same tile as the Player
 */
public class EntityStateHandler {
    private final GameInfo gameInfo;

    public EntityStateHandler(GameInfo info){
        gameInfo = info;
    }

    /**
     * The main method to use.
     * Modifies Player health, keys, score according to
     * which entities share the Player's tiles.
     * Could be extended to check Enemy as well.
     * (eg. lower enemy hp if it runs into the Player)
     * @param movingEntity The MovingEntity (Player) to check.
     * @return true if the entity may move, false if the movement should
     * be undone (collision)
     */
    public boolean updateEntityState(MovingEntity movingEntity){
        if (movingEntity.getEntityType() == Entity.EntityType.player){
            Player player = (Player) movingEntity;
            int x = player.getX();
            int y = player.getY();

            // check the walls
            if (gameInfo.getEntityAt(x, y).getEntityType() == Entity.EntityType.wall){
                return false;
            }

            // get all entities that are on the same tile as the Player
            ArrayList<Entity> collidedEntities = gameInfo.getAllListEntitiesAt(x, y);

            // list of entities to remove after all is updated
            // avoids a ConcurrentModificationException
            ArrayList<Entity> removeList = new ArrayList<>();

            // attack the enemy
            ArrayList<Point> attackedTile = player.getAttackedTile();
            Sword sword = player.getSword();
            if (sword != null){
                for (Point tile: attackedTile){
                    if (gameInfo.checkBoardBounds(tile.x, tile.y) ){
                        for (Entity entity: gameInfo.getAllListEntitiesAt(tile.x, tile.y)){
                            if (entity.getEntityType() == Entity.EntityType.enemy){
                                Enemy enemy = (Enemy) entity;
                                System.out.println("Nice damage!");
                                if (!enemy.takeDamage(sword.getDamage())){
                                    System.out.println("success!");
                                    removeList.add(enemy);
                                }
                            }

                        }
                    }
                }
            }


            for (Entity entity : collidedEntities) {
                switch (entity.getEntityType()){
                    case reward:
                        Reward reward = (Reward) entity;

                        // Player gets reward: add it to the Player
                        // and give the Player score accordingly
                        switch (reward.getRewardType()){
                            case key: player.addKey(); break;
                            case sword: player.addSword((Sword)reward); break;
                            case ladder: player.addLadder(); break;
                            case coin: player.multiplyScore( ((Coin)entity).getScoreMultiplier() ); break;
                        }
                        player.addScore(reward.getBonus());

                        // remove the reward after the player steps on it
                        removeList.add(entity);
                        break;

                    case trap:
                        Trap trap = (Trap) entity;

                        // Player steps on a trap: deal damage to them
                        // also lower the Player's score accordingly
                        boolean damaged = player.takeDamage(trap.getDamage());
                        if (damaged){
                            player.addScore(trap.getPenalty());

                            // remove the trap after the player steps on it
                            removeList.add(entity);
                        }
                        break;

                    case closedDoor:
                        Door door = (Door) entity;

                        // open the door and use a key if the
                        // player has enough keys
                        if (player.getKeys() >= 1){
                            door.setOpen();
                            player.useKey();
                        }
                        // no keys, can't pass
                        else{
                            return false;
                        }
                        break;

                    case exit:
                        Exit exit = (Exit) entity;

                        if (player.getLadders() >= 1){
                            player.useLadder();
                            player.winLevel();
                            System.out.println("You win!");
                        }
                        // no ladder, can't exit
                        else {
                            return false;
                        }
                        break;

                    case enemy:
                        Enemy enemy = (Enemy) entity;

                        // currently if the player and enemy are on the same tile,
                        // the player takes damage equal to the enemy's damage
                        // can be changed in the future
                        player.takeDamage(enemy.getDamage());
                        break;
                }
            }
            // FIXME: Right now, we can't remove elements from the entityList without
            //  an exception being thrown. How can this be fixed?
            for (Entity entity: removeList){
                gameInfo.removeEntity(entity);
            }
        }
        return true;
    }

}
