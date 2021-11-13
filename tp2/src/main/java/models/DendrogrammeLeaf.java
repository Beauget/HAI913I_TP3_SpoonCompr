package models;

import graphs.CallGraph;

public class DendrogrammeLeaf extends DendrogrammeComposant {
	String name;
	
	public DendrogrammeLeaf(String name) {
		this.name = name;
	}

	public double getValue() {
		return -1;
	}

	public DendrogrammeComposant getChildLeft() {
		return null;
	}

	public DendrogrammeComposant getChildRight() {
		return null;
	}

	public String getName() {
		return this.name;
	}

	public boolean isLeaf() {
		// TODO Auto-generated method stub
		return false;
	}

	public double getValue(DendrogrammeComposant dendrogrammeComposant,ClassCouples classCouples) {
		double output = 0;
		if(dendrogrammeComposant.isLeaf()) {
			output = classCouples.getValueInCoupleFromClassNames(this.name , dendrogrammeComposant.getName());
			}
		else {
			output = getValue(dendrogrammeComposant.getChildLeft(),classCouples);
			output += getValue(dendrogrammeComposant.getChildRight(),classCouples);
		}
		return output;
		}

}
/*
	getCoupling(DendrogramNode other, CouplingGraph couplingGraph)
	coupling = 0;
	if (other is a DendrogramLeafNode) {
	     coupling = couplingGraph.getCoupling(this.cls, ((DendrogramLeafNode)
	other).cls);
	}
	
	else {
	     coupling = getCoupling(other.firstChild, couplingGraph);
	     coupling += getCoupling(node.secondChild, couplingGraph);
	     coupling /= 2;
	}
	
	return coupling;
	
*/