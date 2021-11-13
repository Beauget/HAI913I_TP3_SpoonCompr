package models;

import java.util.ArrayList;
import java.util.List;

import graphs.CallGraph;

public class Dendrogram {
	ClassCouples classCouples;
	List<DendrogramComposit> nodes;
	
	public Dendrogram(String projectPath, CallGraph callGraph) {
		this.classCouples = new ClassCouples(projectPath, callGraph);
		this.nodes = initNodes();
	}
	
	private DendrogramComposit getMostCoupledNodePair(List<DendrogramComposit> nodes){
		DendrogramComposit childLeft = null ;
		DendrogramComposit childRight = null;
		double mostNodeValue = -1;
		
		for (int i = 0; i <nodes.size(); i++) {
			for (int j = i+1; j < nodes.size(); j++) {
				double nodeValue = nodes.get(i).getValue(nodes.get(j), classCouples);
				if(nodeValue>mostNodeValue){
					childLeft=nodes.get(i);
					childRight= nodes.get(j);
					nodeValue=mostNodeValue;
				}
			}
		}
		return new DendrogramNode(childLeft,childRight);
	}
	
	public void clustering() {
		int i = 0;
		DendrogramComposit mostCoupledNodePair = null ;
		while(i<2) {
			mostCoupledNodePair = getMostCoupledNodePair(nodes);
			nodes.remove(mostCoupledNodePair.getChildLeft());
			nodes.remove(mostCoupledNodePair.getChildRight());
			nodes.add(mostCoupledNodePair);
			i++;
		}
	}
	
	private List<DendrogramComposit> initNodes() {
		List<DendrogramComposit> output = new ArrayList<DendrogramComposit>();
		ArrayList<String> classes = classCouples.getClasses();
		for(String s : classes) {
			DendrogramComposit leaf = new DendrogramLeaf(s);
			output.add(leaf);
		}
		return output;
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		for(DendrogramComposit d : nodes) {
			builder.append(nodes).append("\n");
		}
		
		return builder.toString();
	}
	
	
}
