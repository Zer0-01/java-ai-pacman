package pacman.controllers.examples;

import java.util.ArrayList;
import pacman.controllers.Controller;
import pacman.game.Game;

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
public class Test4 extends Controller<MOVE>
{	
	private static final int MIN_DISTANCE=20;	//if a ghost is this close, run away
	private boolean reachedPowerPill = false;
	
	public MOVE getMove(Game game,long timeDue)
	{			
		int current=game.getPacmanCurrentNodeIndex();
		
		//Strategy 1: if any non-edible ghost is too close (less than MIN_DISTANCE), run away
		for(GHOST ghost : GHOST.values())
			if(game.getGhostEdibleTime(ghost)==0 && game.getGhostLairTime(ghost)==0)
				if(game.getShortestPathDistance(current,game.getGhostCurrentNodeIndex(ghost))<MIN_DISTANCE)
					return game.getNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(),game.getGhostCurrentNodeIndex(ghost),DM.PATH);
		
		//Strategy 2: find the nearest edible ghost and go after them 
		int minDistance=Integer.MAX_VALUE;
		GHOST minGhost=null;		
		
		for(GHOST ghost : GHOST.values())
			if(game.getGhostEdibleTime(ghost)>0)
			{
				int distance=game.getShortestPathDistance(current,game.getGhostCurrentNodeIndex(ghost));
				
				if(distance<minDistance)
				{
					minDistance=distance;
					minGhost=ghost;
				}
			}
		
		if(minGhost!=null)	//we found an edible ghost
			return game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(),game.getGhostCurrentNodeIndex(minGhost),DM.PATH);
		
		 int[] pills = game.getPillIndices();
	        int[] powerPills = game.getPowerPillIndices();
	        int[] targetsArray;

	        // Check if Pac-Man is near a power pill
	        if (reachedPowerPill || game.getShortestPathDistance(current, getNearestPowerPill(game)) > 100) {
	            targetsArray = pills; // Pac-Man is far from power pills, continue going after the pills
	        } else {
	            reachedPowerPill = true; // Pac-Man reached a power pill, set the flag to true
	            targetsArray = powerPills; // Pac-Man should go after the power pills now
	        }

	        ArrayList<Integer> targets = new ArrayList<Integer>();

	        for (int i = 0; i < targetsArray.length; i++) {
	            if (game.isPillStillAvailable(i)) {
	                targets.add(targetsArray[i]);
	            }
	        }

	        int[] targetsArrayUpdated = new int[targets.size()];

	        for (int i = 0; i < targetsArrayUpdated.length; i++) {
	            targetsArrayUpdated[i] = targets.get(i);
	        }

	        return game.getNextMoveTowardsTarget(current, game.getClosestNodeIndexFromNodeIndex(current, targetsArrayUpdated, DM.PATH), DM.PATH);
	    }

	    private int getNearestPowerPill(Game game) {
	        int current = game.getPacmanCurrentNodeIndex();
	        int[] powerPills = game.getPowerPillIndices();
	        int minDistance = Integer.MAX_VALUE;
	        int nearestPowerPill = -1;

	        for (int powerPill : powerPills) {
	            int distance = game.getShortestPathDistance(current, powerPill);

	            if (distance < minDistance) {
	                minDistance = distance;
	                nearestPowerPill = powerPill;
	            }
	        }

	        return nearestPowerPill;
	    }
}























