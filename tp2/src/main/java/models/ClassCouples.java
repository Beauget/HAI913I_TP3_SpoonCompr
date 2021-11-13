package models;

import java.util.ArrayList;

import graphs.CallGraph;

public class ClassCouples {
	static ArrayList<ClassCouple> couples = new ArrayList<ClassCouple>();
	
	public ClassCouples(String projectPath, CallGraph callGraph) {
		// TODO Auto-generated constructor stub
	}
	
	public double getValueInCoupleFromClassNames(String name1, String name2) {
		for(ClassCouple classCouple : couples) {
			if(classCouple.isSameCouple(name1, name2)) {
				return classCouple.getValue();
			}
		}
		return -1;
	}

}
