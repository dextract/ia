package motion;

import searchalgorithm.Node;
import searchproblem.*;

public class RoverProblem extends InformedSearchProblem {
	
	RoverState goal;
	RoverState initial;
	Terrain t;
	double maxH;

	public RoverProblem(State initial, RoverState goal, Terrain t) {
		super(initial, goal);
		this.t = t;
		this.goal= goal;
		this.initial = (RoverState) initial;
	}

	@Override
	public double heuristic(Node n) {
		
		RoverState currentState = (RoverState) n.getState();
		int x = currentState.getCoordX();
		int y = currentState.getCoordY();
		int gx = goal.getCoordX();
		int gy = goal.getCoordY();
		
		AnimatedSearch.draw(x, y);
	    int dx = Math.abs(x - gx);
	    int dy = Math.abs(y - gy);
	    
	    //double h = Math.sqrt(dx*dx+dy*dy);
	    double h = Math.max(dx, dy);
	    double h1 = Math.sqrt(dx*dx+dy*dy);
	    
	 //   System.out.println(h);

	 /*   if(h>maxH) maxH = h;
	    if((dx==0)&&(dy==0))
	    	System.out.println(maxH);*/
	    return h1*2;
	}
	

}
