package graph;

import java.util.*;

/**
 * <P>This class represents a general "directed graph", which could 
 * be used for any purpose.  The graph is viewed as a collection 
 * of vertices, which are sometimes connected by weighted, directed
 * edges.</P> 
 * 
 * <P>This graph will never store duplicate vertices.</P>
 * 
 * <P>The weights will always be non-negative integers.</P>
 * 
 * <P>The WeightedGraph will be capable of performing three algorithms:
 * Depth-First-Search, Breadth-First-Search, and Djikatra's.</P>
 * 
 * <P>The Weighted Graph will maintain a collection of 
 * "GraphAlgorithmObservers", which will be notified during the
 * performance of the graph algorithms to update the observers
 * on how the algorithms are progressing.</P>
 */
public class WeightedGraph<V> {

	/* STUDENTS:  You decide what data structure(s) to use to
	 * implement this class.
	 * 
	 * You may use any data structures you like, and any Java 
	 * collections that we learned about this semester.  Remember 
	 * that you are implementing a weighted, directed graph.
	 */

	// Resource on creating directed graphs: https://algorithms.tutorialhorizon.com/weighted-graph-implementation-java/

	protected class Edge
	{
		V source;
		V destination;
		int weight;

		public Edge(V source, V destination, int weight)
		{
			this.source = source;
			this.destination = destination;
			this.weight = weight;
		}
	}

	// These are the verticies we have to work with
	/* How the maps is storing information
	Key: Vertex1
	Value: [Edge1, Edge2, ....]
	*/
	protected HashMap<V, LinkedList<Edge>> graph;
	
	/* Collection of observers.  Be sure to initialize this list
	 * in the constructor.  The method "addObserver" will be
	 * called to populate this collection.  Your graph algorithms 
	 * (DFS, BFS, and Dijkstra) will notify these observers to let 
	 * them know how the algorithms are progressing. 
	 */
	private Collection<GraphAlgorithmObserver<V>> observerList;
	

	/** Initialize the data structures to "empty", including
	 * the collection of GraphAlgorithmObservers (observerList).
	 */
	public WeightedGraph() {
		observerList = new LinkedList<>();
		graph = new HashMap<>();

	}

	/** Add a GraphAlgorithmObserver to the collection maintained
	 * by this graph (observerList).
	 * 
	 * @param observer
	 */
	public void addObserver(GraphAlgorithmObserver<V> observer) {
		observerList.add(observer);
	}

	/** Add a vertex to the graph.  If the vertex is already in the
	 * graph, throw an IllegalArgumentException.
	 * 
	 * @param vertex vertex to be added to the graph
	 * @throws IllegalArgumentException if the vertex is already in
	 * the graph
	 */
	public void addVertex(V vertex) {
		if(graph.containsKey(vertex)){
			throw new IllegalArgumentException();
		}else{
			graph.put(vertex, null); // This can either be null or an empty list. (empty list would get rid of an if statement in add edge)
		}

	}
	
	/** Searches for a given vertex.
	 * 
	 * @param vertex the vertex we are looking for
	 * @return true if the vertex is in the graph, false otherwise.
	 */
	public boolean containsVertex(V vertex) {
		if(graph.containsKey(vertex)){
			return true;
		}
		return false;
	}

	/** 
	 * <P>Add an edge from one vertex of the graph to another, with
	 * the weight specified.</P>
	 * 
	 * <P>The two vertices must already be present in the graph.</P>
	 * 
	 * <P>This method throws an IllegalArgumentExeption in three
	 * cases:</P>
	 * <P>1. The "from" vertex is not already in the graph.</P>
	 * <P>2. The "to" vertex is not already in the graph.</P>
	 * <P>3. The weight is less than 0.</P>
	 * 
	 * @param from the vertex the edge leads from
	 * @param to the vertex the edge leads to
	 * @param weight the (non-negative) weight of this edge
	 * @throws IllegalArgumentException when either vertex
	 * is not in the graph, or the weight is negative.
	 */
	public void addEdge(V from, V to, Integer weight) {
		// Check for both source and destination
		if(graph.containsKey(from) || graph.containsKey(to) || weight >= 0)
		{
			// Check if source already contains linked list
			if(graph.get(from) == null) // Then we need to create a list
			{
				//graph.put(from, new LinkedList<Edge>(new Edge(from, to, weight)));
				graph.put(from, new LinkedList<>());

			}

			// Always add edge to the list
			graph.get(from).add(new Edge(from, to, weight));

		}
		else
		{
			throw new IllegalArgumentException();
		}
	}

	/** 
	 * <P>Returns weight of the edge connecting one vertex
	 * to another.  Returns null if the edge does not
	 * exist.</P>
	 * 
	 * <P>Throws an IllegalArgumentException if either
	 * of the vertices specified are not in the graph.</P>
	 * 
	 * @param from vertex where edge begins
	 * @param to vertex where edge terminates
	 * @return weight of the edge, or null if there is
	 * no edge connecting these vertices
	 * @throws IllegalArgumentException if either of
	 * the vertices specified are not in the graph.
	 */
	public Integer getWeight(V from, V to) {
		if (graph.containsKey(from) && graph.containsKey(to))
		{
			// Get list of edges from map using key "from"
			LinkedList<Edge> temp = graph.get(from);

			// Loop through those edges
			for (Edge e : temp)
			{
				// if the edge source and dest equal the given from and to
				if (e.source.equals(from) && e.destination.equals(to)) // Then we've found the correct edge
				{
					return e.weight;
				}
			}
			return null; // None of the edges matched
		}
		else
		{
			throw new IllegalArgumentException();
		}
	}

	/** 
	 * <P>This method will perform a Breadth-First-Search on the graph.
	 * The search will begin at the "start" vertex and conclude once
	 * the "end" vertex has been reached.</P>
	 * 
	 * <P>Before the search begins, this method will go through the
	 * collection of Observers, calling notifyBFSHasBegun on each
	 * one.</P>
	 * 
	 * <P>Just after a particular vertex is visited, this method will
	 * go through the collection of observers calling notifyVisit
	 * on each one (passing in the vertex being visited as the
	 * argument.)</P>
	 * 
	 * <P>After the "end" vertex has been visited, this method will
	 * go through the collection of observers calling 
	 * notifySearchIsOver on each one, after which the method 
	 * should terminate immediately, without processing further 
	 * vertices.</P> 
	 * 
	 * @param start vertex where search begins
	 * @param end the algorithm terminates just after this vertex
	 * is visited
	 */
	public void DoBFS(V start, V end) {
		for (GraphAlgorithmObserver<V> observer : observerList)
		{
			observer.notifyBFSHasBegun();
		}

		// Create visited
		ArrayList<V> visitedSet = new ArrayList<>();

		// Create Queue
		Queue<V> queue = new LinkedList<>();

		queue.add(start);

		boolean reachedEnd = false;

		while(!queue.isEmpty() && reachedEnd == false)
		{
			// Grab head of queue and remove it
			V current = queue.remove();

			// If final vertex has been reached
			if (current.equals(end)) {
				// Notify observers
				for (GraphAlgorithmObserver<V> observer : observerList)
				{
					observer.notifySearchIsOver();
				}
				reachedEnd = true;
			}
			else if (!visitedSet.contains(current)) { // Not at last vertex, prevent duplicates
				// Add current to visited set
				visitedSet.add(current);

				for (GraphAlgorithmObserver<V> observer : observerList)
				{
					observer.notifyVisit(current);
				}

				// Loop through all adjacent
				LinkedList<Edge> adjacencyList = graph.get(current);
				for (Edge edge : adjacencyList) {
					// If the adjacent vector (edge.destination) hasn't been visited
					if (!visitedSet.contains(edge.destination)) {
						// Visit the adjacent vector and add to queue
						queue.add(edge.destination);
					}
				}
			}
		}
	}
	
	/** 
	 * <P>This method will perform a Depth-First-Search on the graph.
	 * The search will begin at the "start" vertex and conclude once
	 * the "end" vertex has been reached.</P>
	 * 
	 * <P>Before the search begins, this method will go through the
	 * collection of Observers, calling notifyDFSHasBegun on each
	 * one.</P>
	 * 
	 * <P>Just after a particular vertex is visited, this method will
	 * go through the collection of observers calling notifyVisit
	 * on each one (passing in the vertex being visited as the
	 * argument.)</P>
	 * 
	 * <P>After the "end" vertex has been visited, this method will
	 * go through the collection of observers calling 
	 * notifySearchIsOver on each one, after which the method 
	 * should terminate immediately, without visiting further 
	 * vertices.</P> 
	 * 
	 * @param start vertex where search begins
	 * @param end the algorithm terminates just after this vertex
	 * is visited
	 */
	public void DoDFS(V start, V end) {
		for (GraphAlgorithmObserver<V> observer : observerList)
		{
			observer.notifyDFSHasBegun();
		}

		// Create collections
		ArrayList<V> visitedSet = new ArrayList<>();
		Stack<V> stack = new Stack<>();
		boolean reachedEnd = false;

		// Add first vertex onto stack
		stack.push(start);

		while (stack.isEmpty() == false && reachedEnd == false) {
			// Grab from top of stack
			V current = stack.pop();

			if (current.equals(end)) {
				for (GraphAlgorithmObserver<V> observer : observerList)
				{
					observer.notifySearchIsOver();
				}

				reachedEnd = true;
			}

			// If this vertex hasn't been visited
			else if (visitedSet.contains(current) == false) {
				for (GraphAlgorithmObserver<V> observer : observerList)
				{
					observer.notifyVisit(current);
				}

				visitedSet.add(current);

				for (Edge adj : graph.get(current)) {
					if (visitedSet.contains(adj) == false) {
						stack.push(adj.destination);
					}
				}

			}

		}
	}
	
	/** 
	 * <P>Perform Dijkstra's algorithm, beginning at the "start"
	 * vertex.</P>
	 * 
	 * <P>The algorithm DOES NOT terminate when the "end" vertex
	 * is reached.  It will continue until EVERY vertex in the
	 * graph has been added to the finished set.</P>
	 * 
	 * <P>Before the algorithm begins, this method goes through 
	 * the collection of Observers, calling notifyDijkstraHasBegun 
	 * on each Observer.</P>
	 * 
	 * <P>Each time a vertex is added to the "finished set", this 
	 * method goes through the collection of Observers, calling 
	 * notifyDijkstraVertexFinished on each one (passing the vertex
	 * that was just added to the finished set as the first argument,
	 * and the optimal "cost" of the path leading to that vertex as
	 * the second argument.)</P>
	 * 
	 * <P>After all of the vertices have been added to the finished
	 * set, the algorithm will calculate the "least cost" path
	 * of vertices leading from the starting vertex to the ending
	 * vertex.  Next, it will go through the collection 
	 * of observers, calling notifyDijkstraIsOver on each one, 
	 * passing in as the argument the "lowest cost" sequence of 
	 * vertices that leads from start to end (I.e. the first vertex
	 * in the list will be the "start" vertex, and the last vertex
	 * in the list will be the "end" vertex.)</P>
	 * 
	 * @param start vertex where algorithm will start
	 * @param end special vertex used as the end of the path 
	 * reported to observers via the notifyDijkstraIsOver method.
	 */
	public void DoDijsktra(V start, V end) {

		//FinishedSet = 
//					Pred[ ] = “null” for all vertices
//					Cost[start] = 0,
//					Cost[ ] =  for all other vertices
//					while ( not all vertices in FinishedSet )
//					find vertex K not in FinishedSet with smallest Cost
//					add K to FinishedSet
//					for each neighbor of K that is not in FinishedSet
//					if (Cost[K] + weight (K,J) < Cost[J] )
//					Cost[J] = Cost[K] + weight (K,J)
//					Pred[J] = K

		for (GraphAlgorithmObserver<V> observer : observerList)
		{
			observer.notifyDijkstraHasBegun();
		}

		// List of verts included in the shortest path
		LinkedList<V> finishedSet = new LinkedList<>();

		// List of weights
		HashMap<V, Integer> costMap = new HashMap<>();

		// Predecessor
		HashMap<V, V> predMap = new HashMap<>();

		// Initialize weight map to infinite
		for (V vert : graph.keySet())
		{
			costMap.put(vert, Integer.MAX_VALUE);
		}

		costMap.put(start, 0);

		// Creating the predMap
		while (finishedSet.size() < graph.size()) {

			V minVertex = null;
			int smallestCost = Integer.MAX_VALUE;

			// Find minimum vertex not in finished set
			for(V vertex : costMap.keySet()) {
				if((costMap.get(vertex) < smallestCost) && !finishedSet.contains(vertex)) {
					minVertex = vertex;
				}
			}
			finishedSet.add(minVertex); //put observers
			for (GraphAlgorithmObserver<V> observer : observerList)
			{
				observer.notifyDijkstraVertexFinished(minVertex, costMap.get(minVertex));
			}

			// For each neighbor of minVertex
			for (Edge adjEdge : graph.get(minVertex)) {
				// If not in the finished set
				//if (finishedSet.contains(adjEdge.destination) == false) {
					// Calculate weight (Cost[K] + weight (K,J))
					int tempWeight = costMap.get(minVertex) + adjEdge.weight;

					if (tempWeight < costMap.get(adjEdge.destination)) {
						// Cost[J] = Cost[K] + weight (K,J)
						costMap.put(adjEdge.destination, tempWeight);

						//Pred[J] = K
						predMap.put(adjEdge.destination, minVertex);
					}
				//}
			}

			//iterate over the adjacent set
		}

		/************************************
		***   CALCULATE LEAST COST PATH   ***
		 ***********************************/

		System.out.println("predMap: " + predMap);
		System.out.println("costMap: " + costMap);

		LinkedList<V> leastCostPath = new LinkedList<>();

		/*
		Reverse the predmap:

		current = end

		while not reached start
		add current to stack
		pred = predMap[current]
		current = pred

		CREATED LEAST COST PATH IN ORDER
		while stack isn't empty
		pop from stack and add to least cost path (and notify observers)

		 */
		V current = end;

		Stack<V> stack = new Stack<>();
		boolean reachedStart = false;
		while (reachedStart == false) {
			stack.push(current);
			V pred = predMap.get(current);
			current = pred;

			if (current == null)
			{
				reachedStart = true;
			}
		}

		while (stack.isEmpty() == false) {
			current = stack.pop();
			leastCostPath.add(current);
		}

		for (GraphAlgorithmObserver<V> observer : observerList)
		{
			observer.notifyDijkstraIsOver(leastCostPath);
		}

	}
}