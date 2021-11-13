package models;

import java.util.ArrayList;

import graphs.CallGraph;

public class DendrogrammeComposite extends DendrogrammeComposant {
	
	DendrogrammeComposant childLeft;
	DendrogrammeComposant childRight;

	public DendrogrammeComposite(DendrogrammeComposant childLeft, DendrogrammeComposant childRight) {
		this.childLeft = childLeft;
		this.childRight =childRight;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public DendrogrammeComposant getChildLeft() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DendrogrammeComposant getChildRight() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isLeaf() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public double getValue(DendrogrammeComposant dendrogrammeComposant, ClassCouples classCouples) {
		double output = 0;
		if(dendrogrammeComposant.isLeaf()) {
			dendrogrammeComposant.getValue(this, classCouples);
		}
		
		else {
			output += this.getChildLeft().getValue(dendrogrammeComposant.getChildRight(), classCouples);
			output += this.getChildLeft().getValue(dendrogrammeComposant.getChildLeft(), classCouples);
			output += this.getChildRight().getValue(dendrogrammeComposant.getChildRight(), classCouples);
			output += this.getChildRight().getValue(dendrogrammeComposant.getChildLeft(), classCouples);
		}
		return output;
		
	}

	
}
/*
schéma algorithmique simplifié
getCoupling(DendrogramNode other, CouplingGraph couplingGraph)
if (other is a DendrogramLeafNode)
     return other.getCoupling(this, couplingGraph);

double coupling = firstChild.getCoupling(other.firstChild,
couplingGraph);
coupling += firstChild.getCoupling(other.secondChild, couplingGraph);
coupling += secondChild.getCoupling(other.firstChild, couplingGraph);
coupling += secondChild.getCoupling(other.secondChild, couplingGraph);
coupling /= 4;

return coupling;
*/
