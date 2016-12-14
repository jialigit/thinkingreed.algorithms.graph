package thinkingreed.algorithms.graph;

public class Route {
	
	private Integer distance;
	
	private String[] vertexs;
	//public Integer 
	
	public Route(String route){
		String[] vertexs = route.split("-");
		this.vertexs = vertexs;
	}
	
	public String[] vertexs(){
		return this.vertexs;
	}
}
