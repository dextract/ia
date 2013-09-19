package searchalgorithm;

import java.util.*;

import searchproblem.SearchProblem;

public class GraphSearch implements SearchAlgorithm {

	private boolean done = false;
	private SearchProblem problem;
	private Node goal;
	private Queue<Node> frontier;
	private HashMap<Node,Node> explored;
	private int expansions;
	private int generated;
	
	public GraphSearch(SearchProblem p, Queue<Node> q) {
		explored = new HashMap<Node,Node>();
		problem = p;
		goal = null;
		frontier = q;
		expansions = 0;
		generated = 0;
	}
	
	public Node searchSolution() {
		if( !done ) {
			goal = search();
			done = true;
			frontier = null;
			problem = null;
		}
		return goal;
	}

	private Node search() {		
		frontier.clear();
		frontier.add(new Node(problem.getInitial()));
		for(;;) {
			if( frontier.isEmpty() ) {
				return null;
			}
			Node n = frontier.remove();
			if( problem.goalTest(n.getState())) {
				return n;
			}
			if(!explored.containsKey(n)) {
				List<Node> children = n.Expand();
				explored.put(n, n);
				frontier.addAll(children);
				generated += children.size();
				expansions++;
			}
		}
	}
	
	public Map<String,Number> getMetrics() {
		Map<String,Number> metrics = new LinkedHashMap<String,Number>();
		
		metrics.put("Node Expansions",expansions);
		metrics.put("Nodes Generated",generated);
		return metrics;
	}
	
}