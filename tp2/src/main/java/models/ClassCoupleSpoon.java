package models;

public class ClassCoupleSpoon {
	
	String classA;
	String classB;
	
	double couplingValue;
	
	public ClassCoupleSpoon(String classA, String classB, double value) {
		this.classA = classA;
		this.classB = classB;
		this.couplingValue = value;
	}
	
	
	public String getClassNameA() {
		return this.classA;
	}

	public String getClassNameB() {
		return this.classB;
	}


	public double getCouplageMetricValue() {
		return this.couplingValue;
	}

	
	public String toString() {
		return  "Couple : " + this.classA + " - " + this.classB + " = " + this.couplingValue + "\n";
	}
}
