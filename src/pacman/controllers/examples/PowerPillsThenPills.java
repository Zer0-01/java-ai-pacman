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
public class PowerPillsThenPills extends Controller<MOVE>
{	
	private static final int MIN_DISTANCE=8;	//if a ghost is this close, run away
	
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
			return game.getNextMoveTowardsTarget(current,game.getGhostCurrentNodeIndex(minGhost),DM.PATH);
		
		//Strategy 3: go after the pills and power pills
		int[] pills=game.getPillIndices();
		int[] powerPills=game.getPowerPillIndices();		
		
		ArrayList<Integer> targetsPills=new ArrayList<Integer>();
		ArrayList<Integer> targetsPower=new ArrayList<Integer>();
		
		for(int i=0;i<pills.length;i++)					//check which pills are available			
			if(game.isPillStillAvailable(i))
				targetsPills.add(pills[i]);
		
		for(int i=0;i<powerPills.length;i++)			//check with power pills are available
			if(game.isPowerPillStillAvailable(i))
				targetsPower.add(powerPills[i]);				
		
		int[] targetsArrayPower=new int[targetsPower.size()];
		int[] targetsArrayPills=new int[targetsPills.size()];	//convert from ArrayList to array
		
		for(int i=0;i<targetsArrayPills.length;i++) {
			targetsArrayPills[i]=targetsPills.get(i);
		}
		
		for(int i=0;i<targetsArrayPower.length;i++) {
			targetsArrayPower[i]=targetsPower.get(i);
		}
		
		/*int distancePower;
		
		//return the next direction once the closest target has been identified
		for(int i=0;i<targetsArrayPills.length;i++) {
			for (int j=0;j<targetsArrayPower.length;j++) {
				distancePower=game.getShortestPathDistance(current,targetsArrayPower[j]);
				if (distancePower<-1) {
					return game.getNextMoveTowardsTarget(current,game.getClosestNodeIndexFromNodeIndex(current,targetsArrayPower,DM.PATH),DM.PATH);
				}
			}
			
		}*/
		if (targetsArrayPower.length==0)
			return game.getNextMoveTowardsTarget(current,game.getClosestNodeIndexFromNodeIndex(current,targetsArrayPills,DM.PATH),DM.PATH);
		return game.getNextMoveTowardsTarget(current,game.getClosestNodeIndexFromNodeIndex(current,targetsArrayPower,DM.PATH),DM.PATH);
	}
}





















