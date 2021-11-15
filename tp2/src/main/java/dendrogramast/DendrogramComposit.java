package dendrogramast;

import graphs.CallGraph;
import models.ClassCouples;

public abstract class DendrogramComposit {
	public abstract double getValue(DendrogramComposit DendrogramComposit,ClassCouples classCouples);
	public abstract double getValue(ClassCouples classCouples);
	public abstract double getSize();
	public abstract String getName();
	public abstract DendrogramComposit getChildLeft();
	public abstract DendrogramComposit getChildRight();
	public abstract boolean isLeaf();
}
