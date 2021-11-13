package models;

import java.util.ArrayList;

import graphs.CallGraph;

public class DendrogramNode extends DendrogramComposit {
	
	DendrogramComposit childLeft;
	DendrogramComposit childRight;

	public DendrogramNode(DendrogramComposit childLeft, DendrogramComposit childRight) {
		this.childLeft = childLeft;
		this.childRight =childRight;
	}


	@Override
	public String getName() {
		return null;
	}

	@Override
	public DendrogramComposit getChildLeft() {
		return childLeft;
	}

	@Override
	public DendrogramComposit getChildRight() {
		return childRight;
	}

	@Override
	public boolean isLeaf() {
		return false;
	}

	@Override
	public double getValue(DendrogramComposit DendrogramComposit, ClassCouples classCouples) {
		double output = 0;
		if(DendrogramComposit.isLeaf()) {
			DendrogramComposit.getValue(this, classCouples);
		}
		
		else {
			output += this.getChildLeft().getValue(DendrogramComposit.getChildRight(), classCouples);
			output += this.getChildLeft().getValue(DendrogramComposit.getChildLeft(), classCouples);
			output += this.getChildRight().getValue(DendrogramComposit.getChildRight(), classCouples);
			output += this.getChildRight().getValue(DendrogramComposit.getChildLeft(), classCouples);
		}
		return output;
		
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{ "+ childLeft.getName()+" } "+" { "+ childRight.getName()+"}");
		return builder.toString();
	}
	
}

