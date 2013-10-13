package searchalgorithm;

import java.util.*;

import searchproblem.*;

public class AStarSearch implements SearchAlgorithm {

	private GraphSearch graph;
	
	public AStarSearch(final SearchProblem p) {
		Queue<Node> pqueue = new PriorityQueue<Node>(11, new Comparator<Node>() {	
			public int compare(Node o1, Node o2) {
			
			double expectedCost01 = o1.getPathCost() +
					((InformedSearchProblem)p).heuristic(o1);
			double expectedCost02 = o2.getPathCost() +
					((InformedSearchProblem)p).heuristic(o2);
			
			if( expectedCost01 > expectedCost02 ) {
				//System.out.println(o2.getPathCost());
				return 1;
			}
			else if ( expectedCost01 < expectedCost02 ) {
				//System.out.println(o1.getPathCost());
				return -1;
			}
			else
				return 0;
			}});
		graph = new GraphSearch(p,pqueue);
	}
	
	public Node searchSolution() {
		return graph.searchSolution();
	}
	
	public Map<String,Number> getMetrics() {
		return graph.getMetrics();
	}

}