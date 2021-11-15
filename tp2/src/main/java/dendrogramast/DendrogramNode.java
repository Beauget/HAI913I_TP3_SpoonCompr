package dendrogramast;

import java.util.ArrayList;

import graphs.CallGraph;
import models.ClassCouples;

public class DendrogramNode extends DendrogramComposit {
	String name;
	DendrogramComposit childLeft;
	DendrogramComposit childRight;
	static Integer cluster = 0;

	public DendrogramNode(DendrogramComposit childLeft, DendrogramComposit childRight) {
		this.childLeft = childLeft;
		this.childRight =childRight;
		this.name = new String('"'+"C"+cluster.toString()+'"');
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
	public double getValue(DendrogramComposit other, ClassCouples classCouples) {

		if(other.isLeaf()==true) {
			return other.getValue(this, classCouples);
		}
		

		double output = this.getChildLeft().getValue(other.getChildRight(), classCouples);
		output += this.getChildLeft().getValue(other.getChildLeft(), classCouples);
		output += this.getChildRight().getValue(other.getChildRight(), classCouples);
		output += this.getChildRight().getValue(other.getChildLeft(), classCouples);

		return output;
		
	}


	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		if(getChildLeft()!= null && getChildRight()!=null) {
			if(this.getChildLeft().isLeaf()&& this.getChildRight().isLeaf()){
				builder.append("\n" + this.getName()+" -> "+ childLeft.toString());
				builder.append("\n" + this.getName()+" -> "+ childRight.toString()+"\n");
			}
			else if( this.getChildLeft().isLeaf()==false && this.getChildRight().isLeaf()==false){
				builder.append("\n" + this.getName() +" -> " +this.getChildLeft().getName());
				builder.append("\n" + this.getName() +" -> " +this.getChildRight().getName());
				builder.append(this.getChildRight().toString());
				builder.append(this.getChildLeft().toString());
			}
			//builder.append("\n" +" {"+this.childLeft.toString()+"}" +" {"+this.childRight.toString()+"}" );
			else if((this.getChildLeft().isLeaf()==false) && this.getChildRight().isLeaf()==true){
				builder.append("\n" + this.getName() +" -> " +this.getChildLeft().getName());
				builder.append("\n" + this.getName() +" -> " +this.getChildRight().toString());
				builder.append(this.getChildLeft().toString());
			}
			else if(this.getChildLeft().isLeaf()==true && this.getChildRight().isLeaf()==false){
				builder.append("\n" + this.getName() +" -> " + this.getChildLeft().toString());
				builder.append("\n" + this.getName() +" -> " +this.getChildRight().getName());
				builder.append(this.getChildRight().toString());
			}
		}
		/*
		
		else if(getChildLeft()!= null) {
			if(!getChildLeft().isLeaf()) {
				builder.append("\n" + this.getName() +" -- " + this.getChildLeft().getName());
				builder.append(this.getChildLeft().toString());
			}
			else
				builder.append("\n" + this.getName() +" -- " + this.getChildLeft().toString());
		}
		
		else if(getChildRight()!= null) {
			if(!getChildRight().isLeaf()) {
				builder.append("\n" + this.getName() +" -- " + this.getChildRight().getName());
				builder.append(this.getChildRight().toString());
			}
			else
				builder.append("\n" + this.getName() +" -- " + this.getChildRight().toString());
		}	*/	
		return builder.toString();
	}


	@Override
	public double getValue(ClassCouples classCouples) {
		double output = 0;
		if(this.getChildLeft().isLeaf()==true && this.getChildRight().isLeaf()==true)
			return classCouples.getValueInCoupleFromClassNames(this.getChildLeft().getName(), this.getChildRight().getName());

		else if(this.getChildRight().isLeaf()==true) {
			output+= this.getChildRight().getValue(this.childLeft,classCouples);
			output+= this.getChildLeft().getValue(classCouples);
		}
		else if(this.getChildLeft().isLeaf()==true) {
			output+= this.getChildLeft().getValue(this.childRight,classCouples);
			output+= this.getChildRight().getValue(classCouples);
		}
		return output;
			
	}


	@Override
	public double getSize() {
		double output = 0;
		output+=this.getChildLeft().getSize();
		output+=this.getChildRight().getSize();
		return output;
	}
	
}

