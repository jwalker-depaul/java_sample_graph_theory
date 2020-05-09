package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import graph.WeightedGraph;

public class PublicTests {

	@Test
	public void testAddVertexAndContainsVertex() {
		WeightedGraph<String> graph = new WeightedGraph<String>();
		graph.addVertex("A");
		graph.addVertex("B");
		graph.addVertex("C");
		graph.addVertex("D");
		assertTrue(graph.containsVertex("A"));
		assertTrue(graph.containsVertex("B"));
		assertTrue(graph.containsVertex("C"));
		assertTrue(graph.containsVertex("D"));
		assertFalse(graph.containsVertex("E"));
	}
	
	@Test
	public void testAddEdgeAndGetWeight() {
		WeightedGraph<String> graph = new WeightedGraph<String>();
		graph.addVertex("A");
		graph.addVertex("B");
		graph.addVertex("C");
		graph.addVertex("D");
		graph.addEdge("A", "B", 1);
		graph.addEdge("A", "C", 2);
		graph.addEdge("A", "D", 3);
		graph.addEdge("B", "C", 4);
		graph.addEdge("D", "C", 5);
		assertTrue(graph.getWeight("A", "B") == 1);
		assertTrue(graph.getWeight("B", "A") == null);
		assertTrue(graph.getWeight("A", "C") == 2);
		assertTrue(graph.getWeight("A", "D") == 3);
		assertTrue(graph.getWeight("B", "C") == 4);
		assertTrue(graph.getWeight("D", "C") == 5);
		boolean caught = false;
		try {
			graph.getWeight("X",  "A");
		} catch (IllegalArgumentException e) {
			caught = true;
		}
		assertTrue(caught);
		caught = false;
		try {
			graph.getWeight("A", "X");
		} catch (IllegalArgumentException e) {
			caught = true;
		}
		assertTrue(caught);
		assertTrue(graph.getWeight("B", "D") == null);
	}

	@Test
	public void testDijsktras() {
		WeightedGraph<String> graph = new WeightedGraph<String>();

		// Create verticies
		graph.addVertex("0");
		graph.addVertex("1");
		graph.addVertex("2");
		graph.addVertex("3");
		graph.addVertex("4");
		graph.addVertex("5");

		// Add edges
		graph.addEdge("0", "1", 4);
		graph.addEdge("0", "2", 3);
		graph.addEdge("1", "2", 5);
		graph.addEdge("1", "3", 2);
		graph.addEdge("2", "3", 7);
		graph.addEdge("3", "4", 2);
		graph.addEdge("4", "0", 4);
		graph.addEdge("4", "1", 4);
		graph.addEdge("4", "5", 6);

		// Check weights
		assertTrue(graph.getWeight("0", "1") == 4);
		assertTrue(graph.getWeight("0", "2") == 3);
		assertTrue(graph.getWeight("1", "2") == 5);
		assertTrue(graph.getWeight("1", "3") == 2);
		assertTrue(graph.getWeight("2", "3") == 7);
		assertTrue(graph.getWeight("3", "4") == 2);
		assertTrue(graph.getWeight("4", "0") == 4);
		assertTrue(graph.getWeight("4", "1") == 4);
		assertTrue(graph.getWeight("4", "5") == 6);

		graph.DoDijsktra("0", "5");
	}
	
}