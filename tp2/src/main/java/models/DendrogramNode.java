package models;

import java.util.ArrayList;

import graphs.CallGraph;

public class DendrogramNode extends DendrogramComposit {
	String name;
	DendrogramComposit childLeft;
	DendrogramComposit childRight;
	static Integer cluster = 0;

	public DendrogramNode(DendrogramComposit childLeft, DendrogramComposit childRight) {
		this.childLeft = childLeft;
		this.childRight =childRight;
		this.name = new String("C"+cluster.toString());
		cluster++;
	}


	@Override
	public String getName() {
		return name;
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
		/*if(childLeft.isLeaf()&&childRight.isLeaf()){
			builder.append("\n" +childLeft.toString()+" -> "+ childRight.toString());
		}
		
		else if(!(childLeft.isLeaf()&&childRight.isLeaf())){
			builder.append("\n" + this.getName() +" -- " +'"'+this.getChildLeft().getName()+'"' );
			builder.append("\n" + this.getName() +" -- " +'"'+this.getChildRight().getName()+'"');
			builder.append(this.getChildRight().toString());
		}*/
		builder.append("\n" +" {"+this.childLeft.toString()+"}" +" {"+this.childRight.toString()+"}" );
		

			
		return builder.toString();
	}
	
}

