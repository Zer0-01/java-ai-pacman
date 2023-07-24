package pacman.controllers.examples;

import java.util.ArrayList;
import pacman.controllers.Controller;
import pacman.game.Game;
import pacman.game.Constants.DM;

import static pacman.game.Constants.*;

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
public class Test2 extends Controller<MOVE> {
	private static final int MIN_DISTANCE = 20; // if a ghost is this close, run away

	public MOVE getMove(Game game, long timeDue) {
		int current = game.getPacmanCurrentNodeIndex();

		int[] powerPills = game.getPowerPillIndices();
		int minDistance=Integer.MAX_VALUE;
		int minPowerPills = 0;


		ArrayList<Integer> targetsPower = new ArrayList<Integer>();

		for (int i = 0; i < powerPills.length; i++) { // check with power pills are available
			if (game.isPowerPillStillAvailable(i)) {
				int distance = game.getShortestPathDistance(current, game.getPowerPillIndex(i));
				if(distance < minDistance) {
					minDistance = distance;
					minPowerPills = game.getPowerPillIndex(i);
				}
			}
		}
		
		if(minPowerPills < 20) {
			return game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(),minPowerPills,DM.PATH);

		}

		

		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		// go after the nearest pills
		int[] pills = game.getPillIndices();

		ArrayList<Integer> targets = new ArrayList<Integer>();

		for (int i = 0; i < pills.length; i++) // check which pills are available
			if (game.isPillStillAvailable(i))
				targets.add(pills[i]);

		int[] targetsArray = new int[targets.size()]; // convert from ArrayList to array

		for (int i = 0; i < targetsArray.length; i++)
			targetsArray[i] = targets.get(i);

		// return the next direction once the closest target has been identified
		return game.getNextMoveTowardsTarget(current,
				game.getClosestNodeIndexFromNodeIndex(current, targetsArray, DM.PATH), DM.PATH);

	}
}
