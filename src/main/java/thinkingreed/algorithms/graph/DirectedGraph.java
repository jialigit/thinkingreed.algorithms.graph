package thinkingreed.algorithms.graph;

/*****************************************************************************
 * File: DirectedGraph.java
 * Author: Keith Schwarz (htiek@cs.stanford.edu)
 *
 * A class representing a directed graph.  Internally, the class is represented
 * by an adjacency list.
 */
import java.util.*; // For HashMap, HashSet

public final class DirectedGraph<T> {
	private Map<T, Node<T>> adjacencyList = new HashMap<>();;

	public Map getAdjacencyList(){
		return this.adjacencyList;
	}
	/**
	 * Adds a vertex to the graph.
	 * 
	 * @param vertex
	 *            vertex to add
	 */
	public boolean addVertex(T vertex) {
		if (adjacencyList.containsKey(vertex)) {
			return false;
		}
		adjacencyList.put(vertex, new Node<>(vertex));
		return true;
	}

	/**
	 * Adds a directed edge between two vertices in the graph.
	 * 
	 * @param vertex1
	 *            vertex where the (directed) edge begins
	 * @param vertex2
	 *            vertex where the (directed) edge ends
	 */
	public boolean addEdge(T vertex1, T vertex2) {
		return addEdge(vertex1, vertex2, 0);
	}

	/**
	 * Adds a weighted directed edge between two vertices in the graph.
	 * 
	 * @param vertex1
	 *            vertex where the (directed) edge begins
	 * @param vertex2
	 *            vertex where the (directed) edge ends
	 * @param weight
	 *            weight of the edge
	 */
	public boolean addEdge(T vertex1, T vertex2, int weight) {
		if (!containsVertex(vertex1) || !containsVertex(vertex2)) {
			throw new RuntimeException("Vertex does not exist");
		}

		// add the edge
		Node<T> node1 = getNode(vertex1);
		Node<T> node2 = getNode(vertex2);
		return node1.addEdge(node2, weight);
	}

	/**
	 * Remove a vertex from the graph.
	 * 
	 * @param vertex
	 *            vertex to be removed
	 * @return true if the vertex was removed, false if no such vertex was
	 *         found.
	 */
	public boolean removeVertex(T vertex) {
		if (!adjacencyList.containsKey(vertex)) {
			return false;
		}

		// get node to be removed
		final Node<T> toRemove = getNode(vertex);

		// remove all incoming edges to node
		adjacencyList.values().forEach(node -> node.removeEdge(toRemove));

		// remove the node
		adjacencyList.remove(vertex);
		return true;
	}

	/**
	 * Method to remove a directed edge between two vertices in the graph.
	 * 
	 * @param vertex1
	 *            vertex where the (directed) edge begins
	 * @param vertex2
	 *            vertex where the (directed) edge ends
	 * @return true if the edge was removed, false if no such edge was found.
	 */
	public boolean removeEdge(T vertex1, T vertex2) {
		if (!containsVertex(vertex1) || !containsVertex(vertex2)) {
			return false;
		}
		return getNode(vertex1).removeEdge(getNode(vertex2));
	}

	/**
	 * Method to get the number of vertices in the graph.
	 * 
	 * @return count of vertices
	 */
	public int vertexCount() {
		return adjacencyList.keySet().size();
	}

	/**
	 * Method to get the number of edges in the graph.
	 * 
	 * @return count of edges
	 */
	public int edgeCount() {
		return adjacencyList.values().stream().mapToInt(Node::getEdgeCount).sum();
	}

	/**
	 * Method to check if a vertex exists in the graph.
	 * 
	 * @param vertex
	 *            vertex which is to be checked
	 * @return returns true if the graph contains the vertex, false otherwise
	 */
	public boolean containsVertex(T vertex) {
		return adjacencyList.containsKey(vertex);
	}

	/**
	 * Method to check if an edge exists in the graph.
	 * 
	 * @param vertex1
	 *            vertex where the (directed) edge begins
	 * @param vertex2
	 *            vertex where the (directed) edge ends
	 * @return returns true if the graph contains the edge, false otherwise
	 */
	public boolean containsEdge(T vertex1, T vertex2) {
		if (!containsVertex(vertex1) || !containsVertex(vertex2)) {
			return false;
		}
		return getNode(vertex1).hasEdge(getNode(vertex2));
	}

	public Edge edgeIfExist(T vertex1,T vertex2){
		if(this.containsEdge(vertex1, vertex2)){
			Node node1 = getNode(vertex1);
			List<Edge> edges = node1.edges();
			for(Edge edge:edges){
				Node toNode = edge.toNode();
				if(toNode.vertex().equals(vertex2)){
					return edge;
				}
			}
			return null;
		}else{
			return null;
		}
	}
	
	public int edgeWeightIfExist(T vertex1,T vertex2) throws Exception{
		Edge edge = edgeIfExist(vertex1,vertex2);
		if(edge!=null){
			return edge.weight();
		}
		
		throw new Exception("no edge exist for vertexs "+vertex1+"and "+vertex2);
	}
	/**
	 * Method to get the shortest path from startVertex to endVertex.
	 * 
	 * @param startVertex
	 *            vertex where the path begins
	 * @param endVertex
	 *            vertex where the path ends
	 * @return list of vertices in the shortest path from startVertex to
	 *         endVertex, null if no such path exists.
	 */
	// @Nullable
	public List<T> shortestPath(T startVertex, T endVertex) {
		// if nodes not found, return empty path
		if (!containsVertex(startVertex) || !containsVertex(endVertex)) {
			return null;
		}
		// run bfs on the graph
		runBFS(startVertex);

		List<T> path = new ArrayList<>();
		// trace path back from end vertex to start
		Node<T> end = getNode(endVertex);
		while (end != null && end != getNode(startVertex)) {
			path.add(end.vertex());
			end = end.parent();
		}
		// if end is null, node not found
		if (end == null) {
			return null;
		} else {
			Collections.reverse(path);
		}
		return path;
	}

	private void runBFS(T startVertex) {
		if (!containsVertex(startVertex)) {
			throw new RuntimeException("Vertex does not exist.");
		}

		// reset the graph
		resetGraph();

		// init the queue
		Queue<Node<T>> queue = new LinkedList<>();
		Node<T> start = getNode(startVertex);
		queue.add(start);

		// explore the graph
		while (!queue.isEmpty()) {
			Node<T> first = queue.remove();
			first.setVisited(true);
			first.edges().forEach(edge -> {
				Node<T> neighbour = edge.toNode();
				if (!neighbour.isVisited()) {
					neighbour.setParent(first);
					queue.add(neighbour);
				}
			});
		}
	}

	public Node<T> getNode(T value) {
		return adjacencyList.get(value);
	}

	private void resetGraph() {
		adjacencyList.keySet().forEach(key -> {
			Node<T> node = getNode(key);
			node.setParent(null);
			node.setVisited(false);
		});
	}
}