package thinkingreed.algorithms.graph;

public class Edge<T> {
	  private Node<T> node1;

	    private Node<T> node2;

	    private int weight;

	    public Edge(Node<T> node1, Node<T> node2, int weight) {
	        this.node1 = node1;
	        this.node2 = node2;
	        this.weight = weight;
	    }

	    public Node<T> fromNode() {
	        return node1;
	    }

	    public Node<T> toNode() {
	        return node2;
	    }

	    public int weight(){
	    	return weight;
	    }
	    
	    public boolean isBetween(Node<T> node1, Node<T> node2) {
	        return (this.node1 == node1 && this.node2 == node2);
	    } 
	
	    @Override
	    public String toString(){
	    	return node1.vertex().toString()+node2.vertex().toString()+weight;
	    }
}
