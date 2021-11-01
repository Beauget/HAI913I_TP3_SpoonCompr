package models;

public class ClassCouple {
	ClassAndContent class1;
	ClassAndContent class2;
	double value;
	
	public ClassCouple(ClassAndContent class1, ClassAndContent class2) {
		this.class1 =class1;
		this.class2 = class2;
		value = getAvgOfCalls();
	}
	
	private Integer getNumberOfCalls() {
		Integer rslt = 0;
		//On parcours les methodes de chaques classes pour voir les invocations
		for(Method method : class1.getMethods()) {
			for(MethodInvocated methodInvocated : method.getMethodsInvocated()) {
				//On identifie les methode qui on pour classe classe2
				if(class2.getName().equals(methodInvocated.getClassOrigin())) {
					rslt+= methodInvocated.getNumberOfTime();
				}	
			}
		}
		for(Method method : class2.getMethods()) {
			for(MethodInvocated methodInvocated : method.getMethodsInvocated()) {
				//On identifie les methode qui on pour classe classe1
				if(class1.getName().equals(methodInvocated.getClassOrigin())) {
					rslt+= methodInvocated.getNumberOfTime();
				}	
			}
		}
		System.out.println("ClassCouple.java : "+class1.getName()+ " et "+ class2.getName() +" se sont appelee l'une l'autre "+ rslt +" fois. \n");
		return rslt;
	}
	
	private double getAvgOfCalls() {
		double rslt = 0;
		//recuperation des appels d'une classe ou l'autre
		double numberOfCalls = this.getNumberOfCalls();
		double totalNumberOfInvocation = class1.numberOfInvocations()+class2.numberOfInvocations();

		if(totalNumberOfInvocation >0) {
			rslt = numberOfCalls/ (class1.numberOfInvocations()+class2.numberOfInvocations());
		}
		//si les classes ne font jamais d'invocations on renvoie 0
		System.out.println("ClassCouple.java : "+"Les classes invoque au total "+ totalNumberOfInvocation +" methode(s) \n");
		System.out.println("ClassCouple.java : "+"Moyenne : "+ rslt);
		return rslt;
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ClassCouple.java : "+"Classes : "+this.class1.getName()+" "+ this.class2.getName()+ "\n");

		
		return builder.toString();
	}
}
