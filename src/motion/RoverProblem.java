package motion;

import static java.lang.Math.*;
import searchalgorithm.Node;
import searchproblem.*;

public class RoverProblem extends InformedSearchProblem {

	public RoverProblem(State initial, RoverState goal) {
		super(initial, goal);
	}


	@Override
	public double heuristic(Node n) {
		
			
		State s = n.getState();
		int x = ((RoverState) s).getCoordX();
		int y = ((RoverState) s).getCoordY();
		
		AnimatedSearch.draw(x, y);
		
		return 0; //Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)) ;
	}
	

}
