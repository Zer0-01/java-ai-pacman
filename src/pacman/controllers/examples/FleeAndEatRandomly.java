package pacman.controllers.examples;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Random;
import java.util.EnumMap;
import pacman.controllers.Controller;
import pacman.game.Constants.*;
import pacman.game.Game;

public class FleeAndEatRandomly extends Controller<MOVE> {
    private static final int MIN_DISTANCE = 10;    // if a ghost is this close, run away
    private Random random;
    
    public FleeAndEatRandomly() {
        random = new Random();
    }
   
	    public MOVE getMove(Game game, long timeDue) {
	        int current = game.getPacmanCurrentNodeIndex();

	        // Strategy 1: if any non-edible ghost is too close (less than MIN_DISTANCE), run away
	        for (GHOST ghost : GHOST.values()) {
	            if (game.getGhostEdibleTime(ghost) == 0 && game.getGhostLairTime(ghost) == 0) {
	                if (game.getShortestPathDistance(current, game.getGhostCurrentNodeIndex(ghost)) < MIN_DISTANCE) {
	                    return game.getNextMoveAwayFromTarget(current, game.getGhostCurrentNodeIndex(ghost), DM.PATH);
	                }
	            }
	        }

	        // Strategy 2: find the nearest edible ghost and go after them
	        int minDistance = Integer.MAX_VALUE;
	        GHOST minGhost = null;

	        for (GHOST ghost : GHOST.values()) {
	            if (game.getGhostEdibleTime(ghost) > 0) {
	                int distance = game.getShortestPathDistance(current, game.getGhostCurrentNodeIndex(ghost));

	                if (distance < minDistance) {
	                    minDistance = distance;
	                    minGhost = ghost;
	                }
	            }
	        }

	        if (minGhost != null) {    // we found an edible ghost
	            return game.getNextMoveTowardsTarget(current, game.getGhostCurrentNodeIndex(minGhost), DM.PATH);
	        }

	        // No non-edible ghosts nearby and no edible ghosts available, choose a random valid move
	        MOVE[] possibleMoves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade());

	        // Generate a random index to select a random move
	        int randomIndex = random.nextInt(possibleMoves.length);

	        // Return the randomly selected move
	        return possibleMoves[randomIndex];
	    }
	}