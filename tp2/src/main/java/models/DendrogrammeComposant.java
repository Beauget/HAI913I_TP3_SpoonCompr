package models;

import graphs.CallGraph;

public abstract class DendrogrammeComposant {
	public abstract double getValue(DendrogrammeComposant dendrogrammeComposant,ClassCouples classCouples);
	public abstract String getName();
	public abstract DendrogrammeComposant getChildLeft();
	public abstract DendrogrammeComposant getChildRight();
	public abstract boolean isLeaf();
}
