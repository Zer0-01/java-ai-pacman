package pacman.controllers.examples;

import java.util.ArrayList;
import pacman.controllers.Controller;
import pacman.game.Game;

import static pacman.game.Constants.*;

import java.util.Random;


/*
 * Pac-Man controller as part of the starter package - simply upload this file as a zip called
 * MyPacMan.zip and you will be entered into the rankings - as simple as that! Feel free to modify 
 * it or to start from scratch, using the classes supplied with the original software. Best of luck!
 * 
 * This controller utilises 3 tactics, in order of importance:
 * 1. Get away from any non-edible ghost that is in close proximity
 * 2. Go after the nearest edible ghost
 * 3. Go to the nearest pill/power pill
 */
public class RunFromGhostOnly extends Controller<MOVE> {
	private static final int MIN_DISTANCE = 20; // if a ghost is this close, run away

    public MOVE getMove(Game game, long timeDue) {
        int current = game.getPacmanCurrentNodeIndex();

        // Check if any non-edible ghost is too close (less than MIN_DISTANCE), if so, run away
        for (GHOST ghost : GHOST.values()) {
            if (game.getGhostEdibleTime(ghost) == 0 && game.getGhostLairTime(ghost) == 0) {
                if (game.getShortestPathDistance(current, game.getGhostCurrentNodeIndex(ghost)) < MIN_DISTANCE) {
                    return game.getNextMoveAwayFromTarget(current, game.getGhostCurrentNodeIndex(ghost), DM.PATH);
                }
            }
        }

        // Check if any ghost is edible, if so, go to the nearest available pill
        boolean anyGhostEdible = false;
        for (GHOST ghost : GHOST.values()) {
            if (game.getGhostEdibleTime(ghost) > 0) {
                anyGhostEdible = true;
                break;
            }
        }

        if (anyGhostEdible) {
            int[] pills = game.getPillIndices();
            ArrayList<Integer> targets = new ArrayList<Integer>();

            for (int i = 0; i < pills.length; i++) {
                if (game.isPillStillAvailable(i)) {
                    targets.add(pills[i]);
                }
            }

            int[] targetsArray = new int[targets.size()];

            for (int i = 0; i < targetsArray.length; i++) {
                targetsArray[i] = targets.get(i);
            }

            return game.getNextMoveTowardsTarget(current, game.getClosestNodeIndexFromNodeIndex(current, targetsArray, DM.PATH), DM.PATH);
        }

        // If no ghost is close and no ghost is edible, go to the nearest available power pill
        int[] powerPills = game.getPowerPillIndices();
        ArrayList<Integer> targets = new ArrayList<Integer>();

        for (int i = 0; i < powerPills.length; i++) {
            if (game.isPowerPillStillAvailable(i)) {
                targets.add(powerPills[i]);
            }
        }

        if (!targets.isEmpty()) {
            int[] targetsArray = new int[targets.size()];
            for (int i = 0; i < targetsArray.length; i++) {
                targetsArray[i] = targets.get(i);
            }
            return game.getNextMoveTowardsTarget(current, game.getClosestNodeIndexFromNodeIndex(current, targetsArray, DM.PATH), DM.PATH);
        }

        return MOVE.NEUTRAL;
    }
}
