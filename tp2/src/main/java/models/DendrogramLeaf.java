package models;

import graphs.CallGraph;

public class DendrogramLeaf extends DendrogramComposit {
	String name;
	
	public DendrogramLeaf(String name) {
		this.name = name;
	}

	public double getValue() {
		return -1;
	}

	public DendrogramComposit getChildLeft() {
		return null;
	}

	public DendrogramComposit getChildRight() {
		return null;
	}

	public String getName() {
		return this.name;
	}

	public boolean isLeaf() {
		return true;
	}

	public double getValue(DendrogramComposit DendrogramComposit,ClassCouples classCouples) {
		double output = 0;
		if(DendrogramComposit.isLeaf()) {
			output = classCouples.getValueInCoupleFromClassNames(this.name , DendrogramComposit.getName());
			}
		else {
			output = getValue(DendrogramComposit.getChildLeft(),classCouples);
			output += getValue(DendrogramComposit.getChildRight(),classCouples);
		}
		return output;
	}
	
	public String toString(){
		return (this.name);
	}

}
