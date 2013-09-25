package motion;

import static java.lang.Math.*;
import searchalgorithm.Node;
import searchproblem.*;

public class RoverProblem extends InformedSearchProblem {
	
	RoverState goal;

	public RoverProblem(State initial, RoverState goal) {
		super(initial, goal);
		
		this.goal= goal;
	}


	@Override
	public double heuristic(Node n) {
		
		State currentState = n.getState();
		int x = ((RoverState) currentState).getCoordX();
		int y = ((RoverState) currentState).getCoordY();
		
		AnimatedSearch.draw(x, y);
		
		return  Math.sqrt(Math.pow(goal.getCoordX()- x, 2) + Math.pow(goal.getCoordY()- y, 2)) ;
	}
	

}
