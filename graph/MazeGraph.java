package graph;
import graph.WeightedGraph;
import maze.Juncture;
import maze.Maze;

import java.util.LinkedList;

/** 
 * <P>The MazeGraph is an extension of WeightedGraph.  
 * The constructor converts a Maze into a graph.</P>
 */
public class MazeGraph extends WeightedGraph<Juncture> {

	/* STUDENTS:  SEE THE PROJECT DESCRIPTION FOR A MUCH
	 * MORE DETAILED EXPLANATION ABOUT HOW TO WRITE
	 * THIS CONSTRUCTOR
	 */
	
	/** 
	 * <P>Construct the MazeGraph using the "maze" contained
	 * in the parameter to specify the vertices (Junctures)
	 * and weighted edges.</P>
	 * 
	 * <P>The Maze is a rectangular grid of "junctures", each
	 * defined by its X and Y coordinates, using the usual
	 * convention of (0, 0) being the upper left corner.</P>
	 * 
	 * <P>Each juncture in the maze should be added as a
	 * vertex to this graph.</P>
	 * 
	 * <P>For every pair of adjacent junctures (A and B) which
	 * are not blocked by a wall, two edges should be added:  
	 * One from A to B, and another from B to A.  The weight
	 * to be used for these edges is provided by the Maze. 
	 * (The Maze methods getMazeWidth and getMazeHeight can
	 * be used to determine the number of Junctures in the
	 * maze. The Maze methods called "isWallAbove", "isWallToRight",
	 * etc. can be used to detect whether or not there
	 * is a wall between any two adjacent junctures.  The 
	 * Maze methods called "getWeightAbove", "getWeightToRight",
	 * etc. should be used to obtain the weights.)</P>
	 * 
	 * @param maze to be used as the source of information for
	 * adding vertices and edges to this MazeGraph.
	 */
	Maze maze;

	public MazeGraph(Maze maze) {
		this.maze = maze;
		Juncture currentJuncture;

		for(int y = 0; y < maze.getMazeHeight(); y++)
		{
			for (int x = 0; x < maze.getMazeWidth(); x++)
			{
				// Set current juncture
				currentJuncture = new Juncture(x, y);

				// Add as vertex to the graph
				if (!this.containsVertex(currentJuncture))
				{
					this.addVertex(currentJuncture);
				}

				// Add edges for current juncture (this will check for walls)
				createEdgeLinks(currentJuncture);
			}
		}
	}

	// Add edges
	private void createEdgeLinks(Juncture current)
	{
		/*
		Above = current.getX(), current.getY() - 1
		Right = current.getX() + 1, current.getY()
		Below = current.getX(), current.getY() + 1
		Left = current.getX() - 1, current.getY()
		 */

		// Temp vars
		Juncture tempJuncture;
		int tempWeight;

		// ABOVE
		if (!maze.isWallAbove(current))
		{
			tempJuncture = new Juncture(current.getX(), current.getY() - 1);
			tempWeight = maze.getWeightAbove(current);

			// Add double edges to make undirected graph
			if (!this.containsVertex(tempJuncture)) {
				this.addVertex(tempJuncture);
			}

			this.addEdge(current, tempJuncture, tempWeight);
			this.addEdge(tempJuncture, current, tempWeight);
		}

		// RIGHT
		if (!maze.isWallToRight(current))
		{
			tempJuncture = new Juncture(current.getX() + 1, current.getY());
			tempWeight = maze.getWeightToRight(current);

			// Add double edges to make undirected graph
			if (!this.containsVertex(tempJuncture)) {
				this.addVertex(tempJuncture);
			}

			this.addEdge(current, tempJuncture, tempWeight);
			this.addEdge(tempJuncture, current, tempWeight);
		}

		// BELOW
		if (!maze.isWallBelow(current))
		{
			tempJuncture = new Juncture(current.getX(), current.getY() + 1);
			tempWeight = maze.getWeightBelow(current);

			// Add double edges to make undirected graph
			if (!this.containsVertex(tempJuncture)) {
				this.addVertex(tempJuncture);
			}

			this.addEdge(current, tempJuncture, tempWeight);
			this.addEdge(tempJuncture, current, tempWeight);
		}

		// LEFT
		if (!maze.isWallToLeft(current))
		{
			tempJuncture = new Juncture(current.getX() - 1, current.getY());
			tempWeight = maze.getWeightToLeft(current);

			// Add double edges to make undirected graph
			if (!this.containsVertex(tempJuncture)) {
				this.addVertex(tempJuncture);
			}

			this.addEdge(current, tempJuncture, tempWeight);
			this.addEdge(tempJuncture, current, tempWeight);
		}
	}
}