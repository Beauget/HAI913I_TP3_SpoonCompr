package dendrogramast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import graphs.CallGraph;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.parse.Parser;
import models.ClassCouples;
import models.Pile;

public class DendrogramAST {
	ClassCouples classCouples;
	List<DendrogramComposit> nodes;
	Pile pile = new Pile();
	
	public DendrogramAST(String projectPath, CallGraph callGraph) {
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
					mostNodeValue=nodeValue;
				}
			}
		}
		return new DendrogramNode(childLeft,childRight);
	}
	
	public void clustering() {
		DendrogramComposit mostCoupledNodePair = null ;
		while(nodes.size()>1) {
			mostCoupledNodePair = getMostCoupledNodePair(nodes);
			nodes.remove(mostCoupledNodePair.getChildLeft());
			nodes.remove(mostCoupledNodePair.getChildRight());
			nodes.add(mostCoupledNodePair);
			pile.empile(mostCoupledNodePair);
		}
	}
	
	public ArrayList<DendrogramComposit> selection_Cluster() {
		ArrayList<DendrogramComposit> partition= new ArrayList<DendrogramComposit>();
		while(!pile.isEmpty()) {
			DendrogramComposit pere = pile.depile();
			DendrogramComposit f1 = pere.getChildLeft();
			DendrogramComposit f2 = pere.getChildRight();
			
			if(f1!=null && f2!=null) {
				if(pere.getValue(classCouples)>(f1.getValue(classCouples)+f2.getValue(classCouples))/pere.getSize()) {
					partition.add(pere);
				}
				else {
					pile.empile(f1);
					pile.empile(f2);
				}
			}

		}
		return partition;
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
			builder.append(d).append("\n");
		}

		return builder.toString();
	}
	

	public String getGraphAsDot() {
		StringBuilder builder = new StringBuilder("digraph \"Dendrogram\" {\n");
	//+ "node [fontname=\"DejaVu-Sans\", shape=circle] \n");
		//ArrayList<String> classes = classCouples.getClasses();
		//liste des noeuds
		//for(String s : classes) {
			//builder.append('"'+s+'"'+"\n");
		//}
		builder.append(this.toString());
		builder.append("\n}");
		return builder.toString();
	}
	
	public void saveGraph() {
		try {
			FileWriter fw = new FileWriter("DendrogramGraphAST.dot", false);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter out = new PrintWriter(bw);
			out.println(this.getGraphAsDot());
			out.close();
			bw.close();
			fw.close();
		} catch (IOException e) {
			System.err.println("Exception ecriture fichier");
			e.printStackTrace();
		}}
	public void saveGraphAsPNG() throws IOException {
		MutableGraph g = new Parser().read(this.getGraphAsDot());
		Graphviz.fromGraph(g).render(Format.PNG).toFile(new File("dendrogramAST.png"));
	}
	public void createFiles() throws IOException {
		this.saveGraph();
		this.saveGraphAsPNG();
	}
	
	
}
